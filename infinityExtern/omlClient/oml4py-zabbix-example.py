#!/usr/bin/env python
# Copyright (c) 2013 TUB, Yahya Al-Hazmi
#
# Permission is hereby granted, free of charge, to any person obtaining a copy
# of this software and associated documentation files (the "Software"), to deal
# in the Software without restriction, including without limitation the rights
# to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
# copies of the Software, and to permit persons to whom the Software is
# furnished to do so, subject to the following conditions:
#
# The above copyright notice and this permission notice shall be included in
# all copies or substantial portions of the Software.
#
# THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
# IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
# FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.  IN NO EVENT SHALL THE
# AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
# LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
# OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
# THE SOFTWARE.
#
#
# = facility-monitoring-xipi.py
#
# == Description
#
# Example of how to push Zabbix monitoring data to XiPi.
#
import oml4py
import time
import math
from datetime import datetime
import pytz
import logging
import logging.handlers
import ast
import sys
import exceptions
import cStringIO
import pycurl
import os
from zabbix_api import ZabbixAPI
import tempfile
import time
from threading import Thread

def init_logger(settings,name):
    logger=logging.getLogger(name)
    logfilename=settings['logger_filename']
    if(settings['logger_loglevel']=="DEBUG"):
        loglevel=logging.DEBUG
    elif settings['logger_loglevel']=="INFO":
        loglevel=logging.INFO
    elif settings['logger_loglevel']=="WARNING":
        loglevel=logging.WARNING
    else:
        loglevel=logging.ERROR
    
    logformatter=logging.Formatter(settings['logger_formatter'])
    logger.setLevel(loglevel)
    if(settings['logger_toconsole']=="1"):
        ch1 = logging.StreamHandler()
        ch1.setLevel(loglevel)
        ch1.setFormatter(logformatter)
        logger.addHandler(ch1)
    ch2 = logging.handlers.RotatingFileHandler(
        logfilename,
        maxBytes=int(settings['logger_maxBytes']),
        backupCount=int(settings['logger_backupCount']))
    ch2.setLevel(loglevel)
    ch2.setFormatter(logformatter)
    logger.addHandler(ch2) 
    return logger

def read_config(filename):
    try:
        f = open(filename, "r")
    except:
        logger.error("can not read file %s, script terminated" % (filename))
        sys.exit()
    try:
        dictionsry = {}
        for line in f:
            splitchar = '='
            kv = line.split(splitchar)
            if (len(kv)==2):
                dictionsry[kv[0]] = str(kv[1])[1:-2]
        return dictionsry
    except:
        logger.error(
        "can not read file %s to a dictionary, format must be KEY=VALUE" % (filename))
        sys.exit()

def get_status(itemid):
	str = zapi.history.get({"output":"extend","history":1,"itemids":itemid,
	      "sortfield":"clock","sortorder":"DESC","limit":1})
	s= str.pop()
    	current= time.strftime("%Y-%m-%dT%H:%M:%S+00:00",time.gmtime(int(s['clock'])))
	ret={}
	ret['current'] = current
	ret['status'] = s['value']
	return ret
	
 
# ----------------------------------------------------------
# 			Script start
# ----------------------------------------------------------

# read the configuration file from to the dictionary settings
try:
	settings=read_config('facility-monitoring.cfg')
except:
	sys.stderr.write("ERROR: Can not read file facility-monitoring.cfg.\n")
	sys.exit()

logger=init_logger(settings,'facility-monitoring.py')

try:
	zabbix_server_uri = settings['localserver']
	zapi = ZabbixAPI(server=zabbix_server_uri, log_level=int(settings['log_level']))

	zabbix_username = settings['username']	
	zabbix_password = settings['password']
	zapi.login(zabbix_username,zabbix_password)

	hosttmp=zapi.host.get({"filter":{"host":"FITeagle-Zabbix-Server"},"output":"extend"})

	hostid = hosttmp.pop()['hostid']
	itemid_Fiteagle = zapi.item.get({"output": "extend","hostids": hostid,
	    "search":{"name":"Fiteagle Server is running"}}).pop()['itemid']
	itemid_EPC_Client = zapi.item.get({"output": "extend","hostids": hostid,
	    "search":{"name":"OpenEPC client is running"}}).pop()['itemid']
	itemid_EPC_PGW = zapi.item.get({"output": "extend","hostids":hostid,
        "search":{"name":"epc-pgw is running"}}).pop()['itemid']
	itemid_EPC_EPDG = zapi.item.get({"output": "extend","hostids":hostid,
        "search":{"name":"epc-epdg is running"}}).pop()['itemid']
	itemid_EPC_MM = zapi.item.get({"output": "extend","hostids":hostid,
        "search":{"name":"epc-mm is running"}}).pop()['itemid']

 
except:
	logger.error("can not open local host.")
	sys.exit()

omlInst = oml4py.OMLBase("CLIENT_NAME", "TESTBED_NAME", "TESTBED_DOMAIN",
                         "tcp:IP_TO_XIPI_MONITORING_SERVER:XIPI_MONITORING_PORT")

omlInst.addmp("epc_client", "statusMessage:string up:double last_check:string")
omlInst.addmp("epc_wifi", "statusMessage:string up:double last_check:string")
omlInst.addmp("fiteagle", "statusMessage:string up:double last_check:string")

omlInst.start()

try:
   	str = zapi.history.get({"output":"extend","history":1,"itemids":itemid_Fiteagle,
   	    "sortfield":"clock","sortorder":"DESC","limit":1})
	s = str.pop()
	current_Fiteagle = \
		time.strftime("%Y-%m-%d %H:%M:%S UTC",time.gmtime(int(s['clock'])))
   	FITeagle_AM_Status = s['value']

	str2 = zapi.history.get({"output":"extend","history":1,"itemids":itemid_EPC_Client,
	    "sortfield":"clock","sortorder":"DESC","limit":1})
   	s2 = str2.pop()
	current_epc_client = \
		time.strftime("%Y-%m-%d %H:%M:%S UTC", time.gmtime(int(s2['clock'])))
   	OpenEPC_Client_Status = s2['value']

	EPC_PGW_Status = get_status(itemid_EPC_PGW)
	EPC_EPDG_Status = get_status(itemid_EPC_EPDG)
	EPC_MM_Status = get_status(itemid_EPC_MM)
	
except:
	logger.error("cannot fetch data from Zabbix.")
	sys.exit()

tz=pytz.timezone("Europe/Berlin")
aware_dt=tz.localize(datetime.now())
current=aware_dt.isoformat()

try: 
	EPC_Wifi_Status = int(EPC_PGW_Status['status']) & int(EPC_EPDG_Status['status'])
	EPC_Client_Status = int(OpenEPC_Client_Status) & int(EPC_MM_Status['status'])

	if (EPC_Client_Status==0):
		EPC_Client_MessageStatu= "down"
	else:
		EPC_Client_MessageStatus= "up and running"
	
	if (EPC_Wifi_Status==0):
		EPC_Wifi_MessageStatus= "down"
	else: 
		EPC_Wifi_MessageStatus= "up and running"

        if (FITeagle_AM_Status=='0'):
		FITeagle_AM_MessageStatus= "down"
	else: 
		FITeagle_AM_MessageStatus= "up and running"

	
	omlInst.inject("epc_client", [EPC_Client_MessageStatus, EPC_Client_Status, current])
	omlInst.inject("epc_wifi", [EPC_Wifi_MessageStatus, EPC_Wifi_Status, current ])
	omlInst.inject("fiteagle", [FITeagle_AM_MessageStatus, FITeagle_AM_Status, current])

except:
	logger.error("cannot inject items into Oml.")
	sys.exit()

omlInst.close()
