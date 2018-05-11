<%@page import="edu.ncsu.csc.itrust.action.ViewPatientInstructionsAction"%>
<%@page import="edu.ncsu.csc.itrust.beans.PatientInstructionsBean"%>
<%@page import="edu.ncsu.csc.itrust.beans.OfficeVisitBean"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.List"%>

<%@page errorPage="/auth/exceptionHandler.jsp" %>
<%@taglib prefix="itrust" uri="/WEB-INF/tags.tld" %>

<%@include file="/global.jsp"%>

<%
    pageTitle = "iTrust - View Patient-Specific Instructions";
%>

<%@include file="/header.jsp"%>

<%

ViewPatientInstructionsAction action = new ViewPatientInstructionsAction(prodDAO, loggedInMID.toString());
List<OfficeVisitBean> ovisits = action.getOfficeVisitsWithInstructions();
Map<Long,String> nameLookup = action.getHCPNameLookup();
SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm a");

loggingAction.logEvent(TransactionType.PATIENT_INSTRUCTIONS_VIEW, loggedInMID.longValue(), loggedInMID.longValue() , "");

%>

<table class="fTable" align="center" id="patientInstructionsTable">
    <tr>
        <th colspan="5">Patient-Specific Instructions</th>
    </tr>

    <tr class="subHeader">
        <td>Office Visit<br>Date</td>
        <td>HCP</td>
        <td>Name</td>
        <td>Comments</td>
        <td>Modified<br>Date</td>
    </tr>
    
<%
for (OfficeVisitBean ovbean: ovisits) {
	List<PatientInstructionsBean> insts = action.getInstructionsForOfficeVisit(ovbean.getID());
	for (PatientInstructionsBean bean: insts) {
%>
        <tr>
            <td style="text-align: center">
                <a href="/iTrust/auth/patient/viewOfficeVisit.jsp?ovID=<%= Long.toString(ovbean.getID()) %>">
                  <%= StringEscapeUtils.escapeHtml(dateFormat.format(ovbean.getVisitDate())) %>
                </a>
            </td>
            <td style="text-align: center; min-width: 8em">
                <%= StringEscapeUtils.escapeHtml(nameLookup.get(ovbean.getHcpID())) %>
            </td>
            <td style="text-align: center; min-width: 8em">
                <a href="<%= StringEscapeUtils.escapeHtml(bean.getUrl()) %>">
                    <%= StringEscapeUtils.escapeHtml(bean.getName()) %>
                </a>
            </td>
            <td>
                <%= StringEscapeUtils.escapeHtml(bean.getComment()) %>
            </td>
            <td style="text-align: center">
                <%= StringEscapeUtils.escapeHtml(dateFormat.format(bean.getModified())) %>
                <br>
                <%= StringEscapeUtils.escapeHtml(timeFormat.format(bean.getModified())) %>
            </td>
        </tr>
<%
	}
}
%>
    
</table>


<%@include file="/footer.jsp"%>
