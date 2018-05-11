<%@taglib prefix="itrust" uri="/WEB-INF/tags.tld" %>
<%@page errorPage="/auth/exceptionHandler.jsp" %>

<%@page import="java.util.List"%>
<%@page import="java.util.Map"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.Date"%>

<%@page import="edu.ncsu.csc.itrust.action.EditLabProceduresAction"%>
<%@page import="edu.ncsu.csc.itrust.action.EditOfficeVisitAction"%>
<%@page import="edu.ncsu.csc.itrust.beans.LabProcedureBean"%>
<%@page import="edu.ncsu.csc.itrust.beans.PersonnelBean"%>
<%@page import="edu.ncsu.csc.itrust.validate.LabProcedureValidator"%>
<%@page import="edu.ncsu.csc.itrust.exception.FormValidationException"%>
<%@page import="edu.ncsu.csc.itrust.dao.mysql.PersonnelDAO"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.HashMap"%>

<%@include file="/global.jsp" %>

<%
pageTitle = "iTrust - Delete Lab Procedure";
%>

<%@include file="/header.jsp" %>


<%

// list lab proc info
// form with text area
// submit with no errors returns to editOfficeVisit
// submit with errors remains here and refill text area

String updateMessage = "";

String pidString = (String)session.getAttribute("pid");

String ovIDString = request.getParameter("ovID");
String labProcIDString = request.getParameter("labProcID");
long labProcID = Long.parseLong(labProcIDString);

EditOfficeVisitAction ovaction = new EditOfficeVisitAction(prodDAO, loggedInMID, pidString, ovIDString);
EditLabProceduresAction action = ovaction.labProcedures();

LabProcedureBean proc = action.getLabProcedure(labProcID);

String errmsg = "";
if (request.getParameter("cancelDelete") != null) {
	response.sendRedirect("editOfficeVisit.jsp?ovID=" + StringEscapeUtils.escapeHtml("" + ovIDString));
} 
else if (request.getParameter("confirmDelete") != null) {
	long ltid = proc.getLTID();
    action.deleteLabProcedure(proc);
   	ovaction.logOfficeVisitEvent(TransactionType.OFFICE_VISIT_EDIT);
   	ovaction.logOfficeVisitEvent(TransactionType.LAB_PROCEDURE_REMOVE);
    loggingAction.logEvent(TransactionType.LAB_RESULTS_REMOVE, 
    		               loggedInMID.longValue(), 
    		               Long.parseLong(pidString), 
                           "lab tech id = "+ltid+", lab procedure id = "+labProcIDString);
	response.sendRedirect("editOfficeVisit.jsp?ovID=" + StringEscapeUtils.escapeHtml("" + ovIDString) +"&labProcDeleted=true");
}
/*else {
	// New form
	newLabTechIDString = Long.toString(proc.getLTID());
	newPriorityString = Integer.toString(proc.getPriorityCode());
	newLabTechID = Long.parseLong(newLabTechIDString);
}*/

%>

<% if (!"".equals(errmsg)) { %>
    <div style="background-color:yellow;color:black" align="center">
        <%= errmsg %>
    </div>
<% } %>

<form action="editOVLabProcedureConfirmDelete.jsp" id="deleteLabProcedureForm" method="post">

<table class="fTable" align="center" id="labProceduresTable">
    <input type="hidden" id="ovID" name="ovID" value="<%= ovIDString %>" />
    <input type="hidden" id="labProcID" name="labProcID" value="<%= labProcIDString %>" />
    
    <tr>
        <th colspan="8">Laboratory Procedure</th>
    </tr>
    <tr class="subHeader">
        <td>LOINC Code</td>
        <td>Lab Tech</td>
        <td>Status</td>
        <td>Results</td>
        <td>Numerical<br/>Results</td>
        <td colspan="2">Confidence<br/>Interval</td>
        <td style="width: 60px;">Updated Date</td>
    </tr>

    <tr>
        <td align=center><%= StringEscapeUtils.escapeHtml("" + (proc.getLoinc())) %></td>
        <td align=center><%= StringEscapeUtils.escapeHtml("" + (action.getLabTechName(proc.getLTID()))) %></td>
        <td align=center><%= StringEscapeUtils.escapeHtml("" + (proc.getStatus())) %></td>
        <td align=center><%= StringEscapeUtils.escapeHtml("" + (proc.getResults())) %></td>
        <td align=center><%= StringEscapeUtils.escapeHtml("" + (proc.getNumericalResult())) %></td>
        <td align=center><%= StringEscapeUtils.escapeHtml("" + (proc.getLowerBound())) %></td>
        <td align=center><%= StringEscapeUtils.escapeHtml("" + (proc.getUpperBound())) %></td>
        <td align=center><%= StringEscapeUtils.escapeHtml("" + (proc.getTimestamp())) %></td>
    </tr>
    
    <tr>
        <td colspan="8" style="text-align: center; font-weight: bold; color: rgb(0,0,0); background-color: rgb(255,160,0);">
            Are you sure you want to delete this lab procedure?
        </td>
    </tr>
    <tr>
        <td colspan="8" style="text-align: center;">
            <input type="submit" id="confirmDelete" name="confirmDelete" value="Delete Lab Procedure">
            <input type="submit" id="cancelDelete" name="cancelDelete" value="Cancel">
        </td>
    <tr>
    
</table>

</form>

<br /><br /><br />
<itrust:patientNav />
<br />

<form></form>

<%@include file="/footer.jsp" %>
