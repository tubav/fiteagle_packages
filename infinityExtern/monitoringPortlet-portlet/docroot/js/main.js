function getStatus(statusString) {
	$.get("/xipimonitoring/rest/v1/status", function(data) {
		console.log("showStatus is called ok!");
		showStatus(data);
	});
	$(document).on(
			"click",
			".detailsButton",
			function() {
				console.log(".statusRow on click is called!");
				rowId = this.id;
				$.get("/xipimonitoring/rest/v1/status/getById?id=" + rowId,
						function(data) {
							showDetailedStatus(data);
						});

			});
}

showStatus = function(data) {
	sortStatusLines(data);

	for ( var k in data) {
		console.log(data[k].id);
		$('#statusTable > tbody').append(buildRow(data[k]));
	}
}

function showDetailedStatus(data) {
	console.log("showDetailedStatus is called!");

	$("#tbName").html(data.id);
	time = new Date(data.lastCheck).toString();
	if (data.lastCheck) {
		$("#tbUpdate").html(time);
	} else {
		$("#tbUpdate").html("");
	}
	$("#tbStatusImage").html(getStatusIcon(data.status));
	componentStatus = "";
	if (data.components) {
		sortStatusLines(data.components);
		for ( var k in data.components) {

			// the testbed name can be added into the components name, while
			// defining the schema names in OML (oml4py.py). Delete this for a
			// better look.
			if (data.components[k].id.indexOf(data.id + "_") >= 0)
				data.components[k].id = data.components[k].id.replace(data.id
						+ "_", '');

			componentStatus += buildDetailRow(data.components[k]);
		}
		$("#tbComponentStatus").html(componentStatus);
	} else {
		$("#tbComponentStatus").html("");
	}
}

sortStatusLines = function(data) {

	return data
			.sort(function(a, b) {

				if (a.status == 'up' && b.status != 'up')
					return -1;
				if (a.status == 'upAndLastCheckedOld' && b.status != 'up'
						&& b.status != 'upAndLastCheckedOld')
					return -1;
				if (a.status == 'partially' && b.status != 'up'
						&& b.status != 'upAndLastCheckedOld'
						&& b.status != 'partially')
					return -1;
				if (a.status == 'down' && b.status != 'up'
						&& b.status != 'upAndLastCheckedOld'
						&& b.status != 'partially' && b.status != 'down')
					return -1;

				if (a.status == b.status) {
					if (a.id < b.id)
						return -1;
					if (a.id > b.id)
						return 1;
					return 0;
				}

				return 1;
			});
}

buildRow = function(testbedstatus) {
	// var row =
	// '<tr><td>'+getStatusIcon(testbedstatus.status)+'</td><td>'+testbedstatus.id+'</td><td>'+new
	// Date(testbedstatus.lastCheck).toString()+'</td><td class="statusRow"
	// '+'id="'+testbedstatus.id+'">Details</td></tr>';
	var lastCheckStr = 'unknown';
	if (testbedstatus.lastCheck != null)
		lastCheckStr = new Date(testbedstatus.lastCheck).toString();

	// var row = '<tr><td>'+getStatusIcon(testbedstatus.status)+'</td><td
	// class="rowAlignLeft">'+testbedstatus.id+'</td><td>'+lastCheckStr+'</td><td
	// class="detailsButton" '+'id="'+testbedstatus.id+'"><a
	// href="/infrastructure/-/infrastructure/view/"'+theID+'"#tab-status">Details</a></td></tr>';
	// var row = '<tr><td>'+getStatusIcon(testbedstatus.status)+'</td><td
	// class="rowAlignLeft">'+testbedstatus.id+'</td><td>'+lastCheckStr+'</td><td
	// class="detailsButton" '+'id="'+testbedstatus.id+'"><a
	// href="/path/to/your/stuff/?id='+theID+'">Details</a></td></tr>';
	var row = '<tr><td>' + getStatusIcon(testbedstatus.status)
			+ '</td><td class="rowAlignLeft">' + testbedstatus.id + '</td><td>'
			+ lastCheckStr + '</td><td class="detailsButton" ' + 'id="'
			+ testbedstatus.id + '">Details</td></tr>';
	return row;
}

buildDetailRow = function(testbedstatus) {
	// var row =
	// '<tr><td>'+getStatusIcon(testbedstatus.status)+'</td><td>'+testbedstatus.id+'</td><td>'+new
	// Date(testbedstatus.lastCheck).toString()+'</td></tr>';

	var lastCheckStr = 'unknown';
	if (testbedstatus.lastCheck != null)
		lastCheckStr = new Date(testbedstatus.lastCheck).toString();
	
	var statusMessgId = "message"+testbedstatus.id;
	var row = '<tr><td>' + getStatusIcon(testbedstatus.status)
			+ '</td><td class="rowAlignLeft">' + testbedstatus.id + '</td><td>'
			+ lastCheckStr + '</td>' + '<td id="' + statusMessgId
			+ '" class="statusMsg">' + 
			'<ul>status message'+
			'<li id="' + "txt"+statusMessgId + '" class="hidden">' + testbedstatus.statusMessage + '</li>'
			+'</ul>'
			
			+'</td>' +
			+'</tr>';
	

	$(document).on(
			"click",
			'#'+statusMessgId,
			function() {
				$('#txt'+statusMessgId).slideToggle();
			});

	return row;
}

getStatusIcon = function(status) {
	if (status === 'up') {
		return '<img src="/monitoringPortlet-portlet/images/green.png" class="status">';
	} else if (status === 'down') {
		return '<img src="/monitoringPortlet-portlet/images/red.png" class="status">';
	} else if (status === 'partially') {
		return '<img src="/monitoringPortlet-portlet/images/yellow.png" class="status">';
	} else if (status === 'upAndLastCheckedOld') {
		return '<img src="/monitoringPortlet-portlet/images/gray.png" class="status">';
	} else if (status === 'undefined') {
		return '<img src="/monitoringPortlet-portlet/images/unknown.png" class="status">';
	} else if (status === null) {
		return '<img src="/monitoringPortlet-portlet/images/unknown.png" class="status">';
	}

}

getStatus();