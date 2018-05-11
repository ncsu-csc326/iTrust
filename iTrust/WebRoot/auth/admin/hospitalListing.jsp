<%@page errorPage="/auth/exceptionHandler.jsp" %>

<%@page import="java.net.URLEncoder" %>
<%@page import="java.util.List"%>
<%@page import="edu.ncsu.csc.itrust.*"%>
<%@page import="edu.ncsu.csc.itrust.dao.DAOFactory"%>
<%@page import="edu.ncsu.csc.itrust.beans.HospitalBean"%>
<%@page import="edu.ncsu.csc.itrust.action.UpdateHospitalListAction"%>
<%@page import="edu.ncsu.csc.itrust.exception.FormValidationException"%>

<%@include file="/global.jsp" %>

<%
pageTitle = "iTrust - Maintain Hospital Listing and Assignments";
%>

<%@include file="/header.jsp" %>

<%
	String headerMessage = "Current Hospital Listing";
	
	UpdateHospitalListAction hospUpdater =
		new UpdateHospitalListAction(DAOFactory.getProductionInstance(), loggedInMID);
	
	if (request.getParameter("add") != null || request.getParameter("update") != null) {
		HospitalBean hosp = new BeanBuilder<HospitalBean>().build(request.getParameterMap(), new HospitalBean());
		boolean latlngValid = true;
		if(latlngValid){
			try {
				headerMessage = (request.getParameter("add") != null)
						? hospUpdater.addHospital(hosp)
						: hospUpdater.updateInformation(hosp);
				
				if(!headerMessage.contains("Error")) {
					if(request.getParameter("add") != null) {
						loggingAction.logEvent(TransactionType.HOSPITAL_LISTING_ADD, loggedInMID, 0, "" + hosp.getHospitalID());
						
					}
					if(request.getParameter("update") != null) {
						loggingAction.logEvent(TransactionType.HOSPITAL_LISTING_EDIT, loggedInMID, 0, "" + hosp.getHospitalID());
					}
				}
			} catch(FormValidationException e) {
	%>
				<div align=center>
					<span class="iTrustError"><%=StringEscapeUtils.escapeHtml(e.getMessage()) %></span>
				</div>
	<%
				headerMessage = "Validation Errors";
			}
		}
	} else {
		loggingAction.logEvent(TransactionType.HOSPITAL_LISTING_VIEW, loggedInMID, 0, "");
	}
	String headerColor = (headerMessage.indexOf("Error") > -1)
			? "#ffcccc"
			: "#00CCCC";
%>

<div align=center>
<form id="mainForm" name="mainForm" method="post">
<input type="hidden" id="updateID" name="updateID" value="">
<input type="hidden" id="oldName" name="oldName" value="">

<script type="text/javascript">
	function fillUpdate(link) {
		// Pull data from the table row
		var data = jQuery(link).parent().parent()[0].cells;
		var id = data[0].innerText;
		var name = data[1].innerText;
		var address = data[2].innerText;
		var city = data[3].innerText;
		var state = data[4].innerText;
		var zip = data[5].innerText;
		
		// Add data to the update form
		$("#hospitalID").val(id);
		$("#hospitalName").val(name);
		$("#hospitalAddress").val(address);
		$("#hospitalCity").val(city);
		$("#hospitalState").val(state);
		$("#hospitalZip").val(zip);
	}

</script>

<h3>Hospital Listing</h3>

<span class="iTrustMessage"><%= StringEscapeUtils.escapeHtml("" + (headerMessage )) %></span>

<br />
<br />
<table class="fTable" align="center">
	<tr>
		<th colspan="8" >Update Hospital List</th>
	</tr>
	<tr class="subHeader">
		<th>Hospital ID</th>
		<th>Hospital Name</th>
		<th>Hospital Address</th>
		<th>Hospital City</th>
		<th>Hospital State</th>
		<th>Hospital Zip</th>
	</tr>
	<tr>
		<td><input type="text" name="hospitalID" id="hospitalID" size="10" maxlength="10" /></td>
		<td><input type="text" name="hospitalName" id="hospitalName" size="30" maxlength="100" /></td>
		<td><input type="text" name="hospitalAddress" id="hospitalAddress" size="30" maxlength="255" /></td>
		<td><input type="text" name="hospitalCity" id="hospitalCity" size="30" maxlength="50" /></td>
		<td><input type="text" name="hospitalState" id="hospitalState" size="10" maxlength="2" /></td>
		<td><input type="text" name="hospitalZip" id="hospitalZip" size="10" maxlength="10" /></td>
	</tr>
</table>
<br />
<input type="submit" name="add" value="Add Hospital" />
<input type="submit" name="update" value="Update Hospital Information" />
<br />
<br />
<table class="fTable" align="center">
	<tr>
		<th colspan="8">Current Hospitals</th></tr>
	<tr class="subHeader">
		<th>Hospital ID</th>
		<th>Hospital Name</th>
		<th>Hospital Address</th>
		<th>Hospital City</th>
		<th>Hospital State</th>
		<th>Hospital Zip</th>
	</tr>
	<%
		List<HospitalBean> hospList =
			DAOFactory.getProductionInstance().getHospitalsDAO().getAllHospitals();
		String tempID = "";
		String tempName = "";
		String escapedName = "";
		String tempAddress = "";
		String tempCity = "";
		String tempState = "";
		String tempZip = "";
		for (HospitalBean hospEntry : hospList) {
			tempID = hospEntry.getHospitalID() + "";
			tempName = hospEntry.getHospitalName();
			tempAddress = hospEntry.getHospitalAddress();
			tempCity = hospEntry.getHospitalCity();
			tempState = hospEntry.getHospitalState();
			tempZip = hospEntry.getHospitalZip();
			escapedName = URLEncoder.encode(tempName, "UTF-8").replaceAll("\\+", "%20");
	%>
		<tr>
			<td ><%= StringEscapeUtils.escapeHtml("" + (tempID )) %></td>
			<td ><a href="javascript:void(0)" 
					onclick="fillUpdate(this)"><%= StringEscapeUtils.escapeHtml("" + (tempName )) %></a>
			</td>
			<td ><%= StringEscapeUtils.escapeHtml("" + (tempAddress )) %></td>
			<td ><%= StringEscapeUtils.escapeHtml("" + (tempCity )) %></td>
			<td ><%= StringEscapeUtils.escapeHtml("" + (tempState )) %></td>
			<td ><%= StringEscapeUtils.escapeHtml("" + (tempZip )) %></td>
		</tr>
	<% } %>
</table>
</form>
</div>
<br />

<%@include file="/footer.jsp" %>
