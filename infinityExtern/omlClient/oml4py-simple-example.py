#!/usr/bin/env python
#
# Copyright (c) 2012-2013 NICTA, Olivier Mehani
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
# = oml4py-simple-example.py
#
# == Description
#
# A very simple straightforward example of OML4Py.
#
import oml4py
import time
import math
from datetime import datetime
import pytz

#"fiteagle" => app-name, not needed
#"FOKUS FUSECO Playground" => domain (testbed name)
#"fuseco.fokus.fraunhofer.de" => sender-id, not needed
#"tcp:130.149.22.71:3434" => the address to send the stream
omlInst = oml4py.OMLBase("fiteagle", "FOKUS FUSECO Playground", "fuseco.fokus.fraunhofer.de", "tcp:130.149.22.71:3434")

#Definition of testbed components...
#schema instances defining the structure of the stream.
#schema: 1 FOKUS FUSECO Playground_laptop node:string up:double last_check:string
omlInst.addmp("laptop", "node:string up:double last_check:string")
#schema: 2 FOKUS FUSECO Playground_epc_wifi node:string up:double last_check:string
omlInst.addmp("epc_wifi", "node:string up:double last_check:string")
#schema: 3 FOKUS FUSECO Playground_fiteagle node:string up:double last_check:string
omlInst.addmp("fiteagle", "node:string up:double last_check:string")

omlInst.start()

#for i in range(15):
while True:
    tz=pytz.timezone("Europe/Berlin")
    aware_dt=tz.localize(datetime.now())
    current=aware_dt.isoformat() #datetime.now().isoformat() #time.time()
    #"laptop" => component name
    #"Fed4FIRE EPC Demo Laptop" => component message, not needed
    #1(component status) => component is up and running, if 0 => component is down
    #"2013-03-14T12:34:34.102734+02:00" => date of last check on component
    omlInst.inject("laptop", [ "Fed4FIRE EPC Demo Laptop", 1, "2013-03-14T12:34:34.102734+02:00" ])
    omlInst.inject("epc_wifi", [ "Fed4FIRE EPC WiFi", 0, current ])
    omlInst.inject("fiteagle", [ "FITeagle AM", 1, current ])
    time.sleep(7)

omlInst.close()
