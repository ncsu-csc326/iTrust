<%@taglib uri="/WEB-INF/tags.tld" prefix="itrust" %>
<%@page errorPage="/auth/exceptionHandler.jsp" %>

<%@page import="java.util.List"%>
<%@page import="java.util.ArrayList"%>
<%@page import="edu.ncsu.csc.itrust.model.old.dao.DAOFactory"%>
<%@page import="edu.ncsu.csc.itrust.model.old.beans.PatientBean"%>
<%@page import="edu.ncsu.csc.itrust.model.old.beans.PersonnelBean"%>
<%@page import="edu.ncsu.csc.itrust.model.old.dao.mysql.PatientDAO"%>
<%@page import="edu.ncsu.csc.itrust.exception.ITrustException"%>

<%@include file="/global.jsp"%>

<%
	pageTitle = "LCHP Information";
%>

<%@include file="/header.jsp"%>

<script type="text/javascript">
	function removeHCP(HCPID,formID) {
		document.getElementById("removeID").value = HCPID;
		document.getElementById(formID).submit();
	}
</script>

<%
int index = Integer.parseInt(request.getParameter("index"));
PersonnelBean myLHCP = ((List<PersonnelBean>) session.getAttribute("personnelList")).get(index);

loggingAction.logEvent(TransactionType.LHCP_VIEW, loggedInMID.longValue(), 0, "");
%>
<div align = center>
<table class="fTable">
<tr><th colspan=2><%= StringEscapeUtils.escapeHtml("" + (myLHCP.getFullName() )) %></th></tr>
<tr><td>Specialty:</td><td><%= StringEscapeUtils.escapeHtml("" + (myLHCP.getSpecialty() == null ? "" : myLHCP.getSpecialty() )) %></td></tr>
<tr><td>Address:</td><td><%= StringEscapeUtils.escapeHtml("" + (myLHCP.getStreetAddress1() )) %> <br> 
						<%= StringEscapeUtils.escapeHtml("" + (  myLHCP.getStreetAddress2() )) %> <br>
						<%= StringEscapeUtils.escapeHtml("" + ( myLHCP.getCity().equals("") ? "" : myLHCP.getCity() + "," )) %> <%= StringEscapeUtils.escapeHtml("" + ( myLHCP.getState() + " " + myLHCP.getZip() )) %> </td></tr>
<tr><td>Phone:</td><td><%= StringEscapeUtils.escapeHtml("" + (myLHCP.getPhone().equals("--") ? "" : myLHCP.getPhone() )) %></td></tr>
<tr><td>Email:</td><td><%= StringEscapeUtils.escapeHtml("" + (myLHCP.getEmail() )) %></td></tr>
</table>
</div>

<%@include file="/footer.jsp"%>
