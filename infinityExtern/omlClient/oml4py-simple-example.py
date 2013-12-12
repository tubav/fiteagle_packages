#!/usr/bin/env python
#
# Copyright (c) 2012-2013 NICTA, Olivier Mehani
# Copyright (c) 2013 TUB, Alexander Willner
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

omlInst = oml4py.OMLBase("APP_NAME", "TESTBED_NAME", "SENDER_ID",
                         "tcp:XIPI_MONITORING_URL:XIPI_MONITORING_IP")

#Definition of testbed components
#schema: resource statusMessage:string up:double last_check:string
omlInst.addmp("RES_NAME1", "statusMessage:string up:double last_check:string")
omlInst.addmp("RES_NAME2", "statusMessage:string up:double last_check:string") 
omlInst.addmp("RES_NAME3", "statusMessage:string up:double last_check:string")

omlInst.start()

while True:
    tz=pytz.timezone("Europe/Berlin")
    aware_dt=tz.localize(datetime.now())
    current=aware_dt.isoformat()
    omlInst.inject("RES_NAME1", [ "up and running", 1, current])
    omlInst.inject("RES_NAME2", [ "up and running", 1, current])
    omlInst.inject("RES_NAME3", [ "executing server update", 0, current])
    time.sleep(7)

omlInst.close()
