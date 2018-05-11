
<%@page import="edu.ncsu.csc.itrust.action.EditImmunizationsAction"%>
<%@page import="edu.ncsu.csc.itrust.beans.ProcedureBean"%>

<% { %>

<%

String updateMessage = "";

if ("removeImmunizationForm".equals(submittedFormName)) {
    EditImmunizationsAction immunizations = ovaction.immunizations();
    
    String remID = request.getParameter("removeImmID");
    ProcedureBean bean = new ProcedureBean();
    bean.setOvProcedureID(Long.parseLong(remID));
    immunizations.deleteImmunization(bean);
   	ovaction.logOfficeVisitEvent(TransactionType.OFFICE_VISIT_EDIT);
   	ovaction.logOfficeVisitEvent(TransactionType.IMMUNIZATION_REMOVE);
    updateMessage = "Immunization information successfully updated.";
}

if ("immunizationForm".equals(submittedFormName)) {
	EditImmunizationsAction immunizations = ovaction.immunizations();
    
    ProcedureBean bean = new BeanBuilder<ProcedureBean>().build(request.getParameterMap(), new ProcedureBean());
    bean.setVisitID(ovaction.getOvID());
    immunizations.addImmunization(bean);
   	ovaction.logOfficeVisitEvent(TransactionType.OFFICE_VISIT_EDIT);
   	ovaction.logOfficeVisitEvent(TransactionType.IMMUNIZATION_ADD);
    updateMessage = "Immunization information successfully updated.";
}
if (!"".equals(updateMessage)) {
    %>  <span class="iTrustMessage"><%= updateMessage %></span>  <%
}

%>

<script type="text/javascript">
    function removeImmID(value) {
        document.getElementById("removeImmID").value = value;
        document.forms["removeImmunizationForm"].submit();
    }
</script>

<div align="center">

<form action="editOfficeVisit.jsp" method="post" id="removeImmunizationForm">
<input type="hidden" name="formName" value="removeImmunizationForm" />
<input type="hidden" id="removeImmID" name="removeImmID" value="" />
<input type="hidden" name="ovID" value="<%= StringEscapeUtils.escapeHtml("" + (ovaction.getOvID())) %>" />
</form>

<form action="editOfficeVisit.jsp" method="post" id="immunizationForm">

<input type="hidden" name="formIsFilled" value="true" />
<input type="hidden" name="formName" value="immunizationForm" />
<input type="hidden" name="ovID" value="<%= StringEscapeUtils.escapeHtml("" + (ovaction.getOvID())) %>" />

<table class="fTable" align="center" id="immunizationsTable">
    <tr>
        <th colspan="3"><a href="#" class="topLink">[Top]</a>Immunizations</th>
    </tr>
    <tr class="subHeader">
        <td>CPT Code</td>
        <td>Description</td>
        <td style="width: 60px;">Action</td>
    </tr>
    <% if (0 == ovaction.immunizations().getImmunizations().size()) { %>
    <tr>
        <td colspan="3" style="text-align: center;">No immunizations on record</td>
    </tr>
    <% } 
       else { %>
    <%  for (ProcedureBean proc : ovaction.immunizations().getImmunizations()) { 
            if (null != proc.getAttribute() && proc.getAttribute().equals("immunization")) { %>
    <tr>
        <td align="center"><%= StringEscapeUtils.escapeHtml("" + (proc.getCPTCode())) %></td>
        <td ><%= StringEscapeUtils.escapeHtml("" + (proc.getDescription())) %></td>
        <td ><a href="javascript:removeImmID('<%= StringEscapeUtils.escapeHtml("" + (proc.getOvProcedureID())) %>');">Remove</a></td>
    </tr>
    <% } } } %>
    <tr >
        <th colspan="3" style="text-align: center;">New</th>
    </tr>
    <tr>
        <td colspan="3" align="center">
            <select name="CPTCode" style="font-size: 10px;" <%= disableSubformsString %> >
                <option value="">-- Please Select a Procedure --</option>
                <% for (ProcedureBean proc : ovaction.immunizations().getImmunizationCodes()) {%>
                    <option value="<%=proc.getCPTCode()%>"><%= StringEscapeUtils.escapeHtml("" + (proc.getCPTCode())) %> - <%= StringEscapeUtils.escapeHtml("" + (proc.getDescription())) %></option>
                <% } %>
            </select>
            <input type="submit" id="add_immunization" name="addImmu" value="Add Immunization" <%= disableSubformsString %> >
        </td>
    </tr>
</table>

</form>

</div>


<% } %>

