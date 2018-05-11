
<%@page import="edu.ncsu.csc.itrust.enums.TransactionType"%>
<%@page import="edu.ncsu.csc.itrust.validate.DiagnosisBeanValidator"%>
<%@page import="edu.ncsu.csc.itrust.action.EditDiagnosesAction"%>
<%@page import="edu.ncsu.csc.itrust.beans.DiagnosisBean"%>

<% { %>

<%

String updateMessage = "";
String updateErrorMessage = "";

if ("removeDiagnosisForm".equals(submittedFormName)) {
    EditDiagnosesAction diagnoses = ovaction.diagnoses();
    
    String remID = request.getParameter("removeDiagID");
    DiagnosisBean bean = new DiagnosisBean();
    bean.setOvDiagnosisID(Long.parseLong(remID));
    diagnoses.deleteDiagnosis(bean);
   	ovaction.logOfficeVisitEvent(TransactionType.OFFICE_VISIT_EDIT);
   	ovaction.logOfficeVisitEvent(TransactionType.DIAGNOSIS_REMOVE);
    updateMessage = "Diagnosis information successfully updated.";
}

if ("diagnosisForm".equals(submittedFormName)) {
	EditDiagnosesAction diagnoses = ovaction.diagnoses();
    
	DiagnosisBean bean = new BeanBuilder<DiagnosisBean>().build(request.getParameterMap(), new DiagnosisBean());
	//validator requires description but DiagnosesDAO does not. Set here to pass validation.
	bean.setDescription("no description");
    try {
    	DiagnosisBeanValidator validator = new DiagnosisBeanValidator();
    	validator.validate(bean);
    	bean.setVisitID(ovaction.getOvID());
        diagnoses.addDiagnosis(bean);
   		ovaction.logOfficeVisitEvent(TransactionType.OFFICE_VISIT_EDIT);
       	ovaction.logOfficeVisitEvent(TransactionType.DIAGNOSIS_ADD);
        updateMessage = "Diagnosis information successfully updated.";
    } catch (FormValidationException e) {
 
        updateErrorMessage = e.printHTMLasString();
    }
	   
}
if (!"".equals(updateMessage)) {
	%>  <span class="iTrustMessage"><%= updateMessage %></span>  <%
}
if (!"".equals(updateErrorMessage)) {
	%>  <span class="iTrustError"><%= updateErrorMessage %></span>  <%
}
%>

<script type="text/javascript">
    function removeDiagID(value) {
        document.getElementById("removeDiagID").value = value;
        document.forms["removeDiagnosisForm"].submit();
    }
</script>


<div align="center">

<form action="editOfficeVisit.jsp" method="post" id="removeDiagnosisForm">
<input type="hidden" name="formName" value="removeDiagnosisForm" />
<input type="hidden" id="removeDiagID" name="removeDiagID" value="" />
<input type="hidden" name="ovID" value="<%= StringEscapeUtils.escapeHtml("" + (ovaction.getOvID())) %>" />
</form>

<form action="editOfficeVisit.jsp" method="post" id="diagnosisForm">

<input type="hidden" name="formIsFilled" value="true" />
<input type="hidden" name="formName" value="diagnosisForm" />
<input type="hidden" name="ovID" value="<%= StringEscapeUtils.escapeHtml("" + (ovaction.getOvID())) %>" />

<table class="fTable" align="center">
    <tr>
        <th colspan="3"><a href="#" class="topLink">[Top]</a>Diagnoses</th>
    </tr>
    <tr class="subHeader">
        <td>ICD Code</td>
        <td>Description</td>
        <td style="width: 60px;">Action</td>
    </tr>

    <%if(ovaction.diagnoses().getDiagnoses().size()==0){ %>
    <tr>
        <td  colspan="3" style="text-align: center;">No Diagnoses on record</td>
    </tr>
    <%} else { 
            for(DiagnosisBean d : ovaction.diagnoses().getDiagnoses()) { %>
    <tr>
        <td align=center><%= StringEscapeUtils.escapeHtml("" + (d.getICDCode())) %></td>
        <td ><%= StringEscapeUtils.escapeHtml("" + (d.getDescription())) %></td>
        <td ><a
            href="javascript:removeDiagID('<%= StringEscapeUtils.escapeHtml("" + (d.getOvDiagnosisID())) %>');">Remove</a></td>
    </tr>
    <%      }
        }%>
    <tr>
        <th colspan="3" style="text-align: center;">New</th>
    </tr>
    <tr>
        <td colspan="3" align=center>
            <select name="ICDCode" style="font-size:10" <%= disableSubformsString %> >
            <option value="">-- None Selected --</option>
            <%for(DiagnosisBean diag : ovaction.diagnoses().getDiagnosisCodes()) { %>
            <option value="<%=diag.getICDCode()%>"><%= StringEscapeUtils.escapeHtml("" + (diag.getICDCode())) %>
            - <%= StringEscapeUtils.escapeHtml("" + (diag.getDescription())) %></option>
            <%}%>
            </select>
            <input type="submit" id="add_diagnosis" value="Add Diagnosis" <%= disableSubformsString %> >
        </td>
    </tr>
</table>

</form>

</div>


<% } %>

