<%
/**
 * Copyright (c) 2000-2011 Liferay, Inc. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */
%>

<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet" %>

<portlet:defineObjects />

<html>
<head>
<title>FITeagle | Future Internet Testbed Experimentation and
	Management Framework | Status Page</title>

</head>

<body id="fiteagle">

	<!-- -------------------- HEADER ------------------------------------- -->
	<div id="header" class="row-fluid">
		<div id="logoImg" class="span1 offset3">
			<img src="img/logo_fiteagle.png" alt="" />
		</div>
		<div id="logoTxt" class="span8">
			<h1 id="project_title">FITeagle</h1>
			<h2 id="project_tagline">Future Internet Testbed Experimentation
				and Management Framework</h2>
		</div>
	</div>
	<!-- _____________________END HEADER__________________________________ -->

	<!-- ----------------- NAVIGATION TOOLBAR ----------------------------	 -->
	<div id="navigation" class="row-fluid">
		<div id="navs" class="span4 offset4">
			<ul class="nav nav-pills">
				<li><a href="#home" data-toggle="tab">Home</a></li>
				<li><a href="#registration" data-toggle="tab">Registration</a>
				</li>
				<li><a href="#aboutUs" data-toggle="tab">About Us</a></li>
				<li class="active"><a href="#status" data-toggle="tab">Status</a>
				</li>
			</ul>
		</div>
	</div>

	<!--_________________END NAVIGATION TOOLBAR__________________________-->

	<!------------------- MAIN SECTION ------------------------------------>
	<div id="main">
		<table id="statusTable">
			<thead>
				<tr>
					<th>Monitoring Status</th>
					<th>Testbed Name</th>
					<th>Last checked</th>
					<th>Components</th>
				</tr>
			</thead>
			<tbody>
<!-- 				<tr> -->
<!-- 					<td>somethingSomething</td> -->
<!-- 					<td>testbedStatusId</td> -->
<!-- 					<td>testbedstatusdate</td> -->
<!-- 					<td class="statusRow" id="testbedStatusId">Details</td> -->
<!-- 				</tr> -->
			</tbody>
		</table>
	</div>
	<button onclick="myFunction()">get status</button>
	<div id="footer" class="row-fluid"></div>
	<div id="popup" class="modal hide fade">
		<button type="button" class="close" data-dismiss="modal"
			aria-hidden="true">X</button>
		<div class="modal-body"></div>
	</div>
     This is the <b>MonitoringPortlet</b> portlet.
</body>
</html>
