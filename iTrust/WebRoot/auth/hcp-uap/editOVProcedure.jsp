
<%@page import="edu.ncsu.csc.itrust.action.EditProceduresAction"%>
<%@page import="edu.ncsu.csc.itrust.beans.ProcedureBean"%>

<% { %>

<%

String updateMessage = "";

if ("removeProcedureForm".equals(submittedFormName)) {
    EditProceduresAction procedures = ovaction.procedures();
    
    String remID = request.getParameter("removeProcID");
    ProcedureBean bean = new ProcedureBean();
    bean.setOvProcedureID(Long.parseLong(remID));
    procedures.deleteProcedure(bean);
   	ovaction.logOfficeVisitEvent(TransactionType.OFFICE_VISIT_EDIT);
   	ovaction.logOfficeVisitEvent(TransactionType.PROCEDURE_REMOVE);
    updateMessage = "Medical Procedure information successfully updated.";
}

if ("procedureForm".equals(submittedFormName)) {
    EditProceduresAction procedures = ovaction.procedures();
    
    ProcedureBean bean = new BeanBuilder<ProcedureBean>().build(request.getParameterMap(), new ProcedureBean());
    bean.setVisitID(ovaction.getOvID());
    procedures.addProcedure(bean);
   	ovaction.logOfficeVisitEvent(TransactionType.OFFICE_VISIT_EDIT);
   	ovaction.logOfficeVisitEvent(TransactionType.PROCEDURE_ADD);
    updateMessage = "Medical Procedure information successfully updated.";
}
if (!"".equals(updateMessage)) {
    %>  <span class="iTrustMessage"><%= updateMessage %></span>  <%
}

%>

<script type="text/javascript">
    function removeProcID(value) {
        document.getElementById("removeProcID").value = value;
        document.forms["removeProcedureForm"].submit();
    }
</script>


<div align="center">

<form action="editOfficeVisit.jsp" method="post" id="removeProcedureForm">
	<input type="hidden" name="formName" value="removeProcedureForm" />
	<input type="hidden" id="removeProcID" name="removeProcID" value="" />
	<input type="hidden" name="ovID" value="<%= StringEscapeUtils.escapeHtml("" + (ovaction.getOvID())) %>" />
</form>

<form action="editOfficeVisit.jsp" method="post" id="procedureForm">

<input type="hidden" name="formIsFilled" value="true" />
<input type="hidden" name="formName" value="procedureForm" />
<input type="hidden" name="ovID" value="<%= StringEscapeUtils.escapeHtml("" + (ovaction.getOvID())) %>" />

<table class="fTable" align="center" id="proceduresTable">
    <tr>
        <th colspan="3"><a href="#" class="topLink">[Top]</a>Procedures</th>
    </tr>
    <tr class="subHeader">
        <td>CPT Code</td>
        <td>Description</td>
        <td style="width: 60px;">Action</td>
    </tr>
    <% if (0 == ovaction.procedures().getProcedures().size()) { %>
    <tr>
        <td colspan="3" style="text-align: center;">No Procedures on record</td>
    </tr>
    <% } 
       else { %>
    <% for (ProcedureBean proc : ovaction.procedures().getProcedures()) { 
        if (null == proc.getAttribute() || !proc.getAttribute().equals("immunization")) {%>
    <tr>
        <td align="center"><%= StringEscapeUtils.escapeHtml("" + (proc.getCPTCode())) %></td>
        <td ><%= StringEscapeUtils.escapeHtml("" + (proc.getDescription())) %></td>
        <td ><a href="javascript:removeProcID('<%= StringEscapeUtils.escapeHtml("" + (proc.getOvProcedureID())) %>');">Remove</a></td>
    </tr>
    <% } } } %>
    <tr>
        <th colspan="3" style="text-align: center;">New</th>
    </tr>
    <tr>
        <td colspan="3" align="center">
            <select name="CPTCode" style="font-size: 10px;" <%= disableSubformsString %> >
                <option value="">-- Please Select a Procedure --</option>
                <% for (ProcedureBean proc : ovaction.procedures().getProcedureCodes()) {
                    if (null == proc.getAttribute() || !proc.getAttribute().equals("immunization")) { %>
                <option value="<%=proc.getCPTCode() %>"><%= StringEscapeUtils.escapeHtml("" + (proc.getCPTCode() )) %> - <%= StringEscapeUtils.escapeHtml("" + (proc.getDescription() )) %></option>
                <% } } %>
            </select>
            <input type="submit" id="add_procedure" name="addP" value="Add Procedure" <%= disableSubformsString %> >
        </td>
    </tr>
</table>

</form>

</div>

<% } %>
