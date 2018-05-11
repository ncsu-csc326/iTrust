<%@taglib uri="/WEB-INF/tags.tld" prefix="itrust" %>
<%@page errorPage="/auth/exceptionHandler.jsp" %>

<%@page import="java.util.List"%>
<%@page import="java.util.ArrayList"%>
<%@page import="edu.ncsu.csc.itrust.dao.DAOFactory"%>
<%@page import="edu.ncsu.csc.itrust.beans.PatientBean"%>
<%@page import="edu.ncsu.csc.itrust.beans.HCPVisitBean"%>
<%@page import="edu.ncsu.csc.itrust.beans.OfficeVisitBean"%>
<%@page import="edu.ncsu.csc.itrust.action.ViewVisitedHCPsAction"%>
<%@page import="edu.ncsu.csc.itrust.beans.PersonnelBean"%>
<%@page import="edu.ncsu.csc.itrust.dao.mysql.PatientDAO"%>
<%@page import="edu.ncsu.csc.itrust.exception.ITrustException"%>

<%@include file="/global.jsp"%>

<%
	pageTitle = "iTrust - My Providers";
%>

<%@include file="/header.jsp"%>

<script type="text/javascript">
	function removeHCP(HCPID,formID) {
		document.getElementById("removeID").value = HCPID;
		document.getElementById(formID).submit();
	}
    function hcp_checkbox(index, HCPID, formID) {
    	if (document.getElementsByName('doctor')[index].checked) {
    	} else {
    		document.getElementById("removeID").value = HCPID;
    	}
        document.getElementById(formID).submit();
    }
</script>

<%
	PatientBean patient = new PatientDAO(prodDAO).getPatient(loggedInMID.longValue());
String[] designateHCPs = request.getParameterValues("doctor");
String filterName = request.getParameter("filter_name");
String filterSpecialty = request.getParameter("filter_specialty");
String filterZip = request.getParameter("filter_zip");
String removeID = request.getParameter("removeID");

boolean filtered = false;

ViewVisitedHCPsAction action = new ViewVisitedHCPsAction(DAOFactory.getProductionInstance(),loggedInMID.longValue());

String confirm = "";
try {
	/* Remove designated HCP, if one was selected. */
	if (removeID != null && !removeID.equals("")) {
		confirm = action.undeclareHCP(removeID);
		long hcpid = action.getNamedHCP(removeID).getHCPMID();
		loggingAction.logEvent(TransactionType.LHCP_UNDECLARE_DLHCP, loggedInMID, hcpid, "Undeclared "+removeID);
	}
	/* Set designated HCPs, if one was selected. */
	else if (designateHCPs != null && !designateHCPs[0].equals("")) {
		for (String designateHCP : designateHCPs) {
	confirm = action.declareHCP(designateHCP);			
	loggingAction.logEvent(TransactionType.LHCP_DECLARE_DLHCP, loggedInMID, 0, "Declared "+designateHCP);	//this should be modified to log the added DLHCP's #--it logs their name, however, at the moment
		}
	}
	/* Otherwise, just view the designated HCPs. */
	else {
		loggingAction.logEvent(TransactionType.LHCP_VIEW, loggedInMID, 0L, "");
	}
}
catch (ITrustException ie) {
%>
<span ><%=StringEscapeUtils.escapeHtml(ie.getMessage())%></span>
<% 
}
if(!"".equals(confirm)){%><span><%= StringEscapeUtils.escapeHtml("" + (confirm)) %></span><%}

List<HCPVisitBean> hcplist = action.filterHCPList(filterName, filterSpecialty, filterZip);
%>


<br />

<div align=center>
	<h3>Provider list for <%= StringEscapeUtils.escapeHtml("" + (patient.getFullName())) %></h3>
	<br />

	<form name="mainForm" id="mainForm" action="viewVisitedHCPs.jsp" method="post" onSubmit="return false;" target="_top">
		<input type="hidden" id="removeID" name="removeID" value="" />

		<table id="hcp_table" class="fTable" style="text-align: center;">
			<tr>
				<th>HCP Name</th>
				<th>Specialty</th>
				<th>Address</th>
				<th>Date of Office Visit</th>
				<th>Designated HCP?</th>
			</tr>

<%
	
	int i = 0; 
	for (HCPVisitBean vb: hcplist) { 
%>
			<tr>
				<td><%= StringEscapeUtils.escapeHtml("" + (vb.getHCPName())) %></td>
				<td><%= StringEscapeUtils.escapeHtml("" + (vb.getHCPSpecialty())) %></td>
				<td><%= StringEscapeUtils.escapeHtml("" + (vb.getHCPAddr())) %></td>
				<td><%= StringEscapeUtils.escapeHtml("" + (vb.getOVDate())) %></td>
				<td>
					<input name="doctor" value="<%= StringEscapeUtils.escapeHtml("" + (vb.getHCPName())) %>" 
							type="checkbox" <%= StringEscapeUtils.escapeHtml("" + (vb.isDesignated()?"checked=\"checked\"":"")) %> 
							onClick="hcp_checkbox(<%= StringEscapeUtils.escapeHtml("" + (i)) %>,'<%= StringEscapeUtils.escapeHtml("" + (vb.getHCPName())) %>','mainForm');"
							/>
				</td>
			</tr> 
<%
		i++;
	}
%>
			<tr>
				<td colspan="5" style="color: #CC3333; text-align: right; font-weight: bold; font-size: 12px;">
					Select checkbox to update designated HCP
				</td>
			</tr>
		</table>
	</form>

</div>

<br /><br />
<form id="searchForm" action="viewVisitedHCPs.jsp" method="post">
	<div align=center>
	<table class="fTable" style="border: none;" border=0>
		<tr>
			<th colspan=2>Search HCPs</th>
		</tr>
		<tr style="text-align: left;">
			<td class="subHeaderVertical">Last Name:</td>
			<td ><input type="text" name="filter_name" size="30" maxlength="255" ></td>
		</tr>
		<tr style="text-align: left;">
			<td class="subHeaderVertical">Specialty:</td>
			<td ><input type="text" name="filter_specialty" size="30" maxlength="255" ></td>
		</tr>
		<tr style="text-align: left;">
			<td class="subHeaderVertical">Zip Code: </td>
			<td><input type="text" name="filter_zip" size="10" maxlength="10"></td>
		</tr>
	</table>
	<br />
	<input type="submit" name="update_filter" value="Search">
	</div>
</form>


<%@include file="/footer.jsp"%>
