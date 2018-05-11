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
pageTitle = "iTrust - Reassign Lab Procedure";
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

String newLabTechIDString = request.getParameter("newLabTech");
long newLabTechID = 0;
String newPriorityString = request.getParameter("labProcPriority");
if (newPriorityString==null || newPriorityString.length()==0) {
	newPriorityString = "3";
}

String errmsg = "";
if (request.getParameter("cancelReassign") != null) {
	response.sendRedirect("editOfficeVisit.jsp?ovID=" + StringEscapeUtils.escapeHtml("" + ovIDString));
} else if (newLabTechIDString!=null && !"".equals(newLabTechIDString)) {
	// Form was subimtted with new commentary.  Verify it, and redirect.
	long ltid = Long.parseLong(newLabTechIDString);
	long oldltid = proc.getLTID();
	int priority = Integer.parseInt(newPriorityString);
	proc.setLTID(ltid);
    proc.setPriorityCode(priority);
	LabProcedureValidator validator = new LabProcedureValidator();
	try {
		validator.validate(proc);
		action.editLabProcedure(proc);
		ovaction.logOfficeVisitEvent(TransactionType.LAB_PROCEDURE_EDIT);
		loggingAction.logEvent(TransactionType.LAB_RESULTS_REASSIGN, 
				               loggedInMID.longValue(), 
				               Long.parseLong(pidString), 
	                           "old lab tech id = "+oldltid+", new lab tech id = "+ltid+", lab procedure id = "+labProcIDString);
		response.sendRedirect("editOfficeVisit.jsp?ovID=" + StringEscapeUtils.escapeHtml("" + ovIDString));
	} catch (FormValidationException e) {
		errmsg = e.printHTMLasString();
	}
} else {
	// New form
	newLabTechIDString = Long.toString(proc.getLTID());
	newPriorityString = Integer.toString(proc.getPriorityCode());
	newLabTechID = Long.parseLong(newLabTechIDString);
}

%>

<% if (!"".equals(errmsg)) { %>
    <div style="background-color:yellow;color:black" align="center">
        <%= errmsg %>
    </div>
<% } %>

<form action="editOVLabProcedureReassign.jsp" id="reassignLabProcedureForm" method="post">

<table class="fTable" align="center" id="labProceduresTable">
    <input type="hidden" id="ovID" name="ovID" value="<%= ovIDString %>" />
    <input type="hidden" id="labProcID" name="labProcID" value="<%= labProcIDString %>" />
    
    <tr>
        <th colspan="8">Laboratory Procedures</th>
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
        <td>Select a New Lab Tech: </td>
        <td colspan="3" style="border: none;">
            <select name="newLabTech">
                <option value="">-- Please Assign to a Lab Tech--</option>
                
                <% for (PersonnelBean pb : action.getLabTechs()) { %>
                    <option value="<%= pb.getMID() %>" 
                        <%= newLabTechID==pb.getMID() ? "selected=\"true\"" : "" %> >
                        <%= StringEscapeUtils.escapeHtml("" + (pb.getFullName())) %>
                        -- <%= StringEscapeUtils.escapeHtml("" + (pb.getSpecialty())) %>
                    </option>
                <% } %>
            </select>
        </td>
        <td colspan="1" style="border: none; text-align: right;">
            Priority:
        </td>
        <td colspan="5" style="border-bottom: none; border-top: none; border-left: none">
            <select name="labProcPriority">
                <option value="1" <%= "1".equals(newPriorityString) ? "selected=\"true\"" : "" %>>1</option>
                <option value="2" <%= "2".equals(newPriorityString) ? "selected=\"true\"" : "" %>>2</option>
                <option value="3" <%= "3".equals(newPriorityString) ? "selected=\"true\"" : "" %>>3</option>
            </select>
        </td>
    </tr>
    <tr>
        <td colspan="8" style="text-align: center;">
            <input  type="submit" id="setLabTech" name="setLabTech" value="Set Lab Tech">
            <input  type="submit" id="cancelReassign" name="cancelReassign" value="Cancel">
        </td>
    </tr>

</table>

</form>

<br /><br /><br />
<itrust:patientNav />
<br />

<form></form>

<%@include file="/footer.jsp" %>
