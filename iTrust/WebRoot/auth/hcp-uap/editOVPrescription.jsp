<style type="text/css">
	table{
		width: 80%;
	}
</style>
<%@page import="edu.ncsu.csc.itrust.action.EditPrescriptionsAction"%>
<%@page import="edu.ncsu.csc.itrust.beans.forms.EditPrescriptionsForm"%>
<%@page import="edu.ncsu.csc.itrust.beans.PrescriptionBean"%>
<%@page import="edu.ncsu.csc.itrust.exception.PrescriptionWarningException"%>
<%@page import="edu.ncsu.csc.itrust.exception.FormValidationException"%>

<% { %>

<%

String updateMessage = "";

boolean prescriptionError = false;
String lastMedID = "";
String lastDosage = "";
String lastStartDate = "";
String lastEndDate = "";
String lastInstructions = "";

String prescriptionErrorMsg = "";
String fieldErrorMsg = "";

String defaultInstructions = "-- Instructions --";

if ("removePrescriptionForm".equals(submittedFormName)) {
	EditPrescriptionsAction prescriptions = ovaction.prescriptions();
	
	String remID = request.getParameter("removeMedID");
	PrescriptionBean bean = new PrescriptionBean();
    bean.setId(Long.parseLong(remID));
    prescriptions.deletePrescription(bean);
    ovaction.logOfficeVisitEvent(TransactionType.OFFICE_VISIT_EDIT);
    ovaction.logOfficeVisitEvent(TransactionType.PRESCRIPTION_REMOVE);
    updateMessage = "Prescription information successfully updated.";
}

if ("prescriptionForm".equals(submittedFormName)) {
    // Handle submitting the prescriptions form.
    EditPrescriptionsAction prescriptions = ovaction.prescriptions();
    
    EditPrescriptionsForm form = new BeanBuilder<EditPrescriptionsForm>().build(request.getParameterMap(), new EditPrescriptionsForm());
    form.setOverrideCodes(request.getParameterValues("overrideCode"));
    try {
    	PrescriptionBean bean = prescriptions.formToBean(form, defaultInstructions);
        prescriptions.addPrescription(bean);
       	ovaction.logOfficeVisitEvent(TransactionType.OFFICE_VISIT_EDIT);
       	ovaction.logOfficeVisitEvent(TransactionType.PRESCRIPTION_ADD);
        updateMessage = "Prescription information successfully updated.";
    } catch (PrescriptionWarningException e) {
    	prescriptionErrorMsg = e.getDisplayMessage();
    	prescriptionError = true;
        lastMedID = request.getParameter("medID");
        lastDosage = request.getParameter("dosage");
        lastStartDate = request.getParameter("startDate");
        lastEndDate = request.getParameter("endDate");
        lastInstructions = request.getParameter("instructions");
    } catch (FormValidationException e) {
        fieldErrorMsg = e.printHTMLasString();
        prescriptionError = true;
        lastMedID = request.getParameter("medID");
        lastDosage = request.getParameter("dosage");
        lastStartDate = request.getParameter("startDate");
        lastEndDate = request.getParameter("endDate");
        lastInstructions = request.getParameter("instructions");
    }
}
if ("true".equals(request.getParameter("prescriptionEdited"))) {
    updateMessage = "Prescription information successfully updated.";
}
if (!"".equals(updateMessage)) {
    %>  <div align="center"><span class="iTrustMessage" align="center"><%= updateMessage %></span></div>  <%
}
%>

<script type="text/javascript">
    function removeMedID(value) {
        document.getElementById("removeMedID").value = value;
        document.forms["removePrescriptionForm"].submit();
    }
</script>

<form></form>

<div align="center">

<form action="editOfficeVisit.jsp#prescriptions" method="post" id="removePrescriptionForm">
	<input type="hidden" name="formName" value="removePrescriptionForm" />
	<input type="hidden" id="removeMedID" name="removeMedID" value="" />
	<input type="hidden" name="ovID" value="<%= StringEscapeUtils.escapeHtml("" + (ovaction.getOvID())) %>" />
</form>


<% if (!"".equals(fieldErrorMsg)) { %>
    <div style="background-color:yellow;color:black" align="center">
        <%= fieldErrorMsg %>
    </div>
<% } %>

<form action="editOfficeVisit.jsp#prescriptions" method="post" id="prescriptionForm">

<input type="hidden" name="formIsFilled" value="true" />
<input type="hidden" name="formName" value="prescriptionForm" />
<input type="hidden" name="ovID" value="<%= StringEscapeUtils.escapeHtml("" + (ovaction.getOvID())) %>" />

<table class="fTable" align="center" id="prescriptionsTable">
    <tr>
        <th colspan="6"><a href="#" class="topLink">[Top]</a>Prescriptions</th>
    </tr>
    <tr class="subHeader">
        <td>Medication</td>
        <td>Dosage</td>
        <td>Dates</td>
        <td colspan=2>Instructions</td>
        <td style="width: 60px;">Action</td>
    </tr>
    
    <%if (ovaction.prescriptions().getPrescriptions().size()==0){ %>
        <tr>
            <td colspan="6" style="text-align: center;">No Prescriptions on record</td>
        </tr>
    <%} else { %>
        <%for(PrescriptionBean pres : ovaction.prescriptions().getPrescriptions()){ %>
        <tr>
            <td align=center>
                <a href="./editPrescription.jsp?presID=<%= StringEscapeUtils.escapeHtml("" + (pres.getId())) %>&ovID=<%= StringEscapeUtils.escapeHtml("" + (ovIDString)) %>">
                    <%= StringEscapeUtils.escapeHtml("" + (pres.getMedication().getDescription())) %> (<%= StringEscapeUtils.escapeHtml("" + (pres.getMedication().getNDCode())) %>)
                </a>
            </td>
            <td align=center><%= StringEscapeUtils.escapeHtml("" + (pres.getDosage())) %>mg</td>
            <td align=center><%= StringEscapeUtils.escapeHtml("" + (pres.getStartDateStr())) %> to <%= StringEscapeUtils.escapeHtml("" + (pres.getEndDateStr())) %></td>                        
            <td align=center colspan=2><%= StringEscapeUtils.escapeHtml("" + (pres.getInstructions())) %></td>                      
            <td align=center>
                <a href="javascript:removeMedID('<%= StringEscapeUtils.escapeHtml("" + (pres.getId())) %>');">Remove</a>
            </td>
        </tr>
        <%}
    }%>
    <tr>
        <th colspan="6" style="text-align: center;">Add New</th>
    </tr>
     <tr>
        <td align=center>
            <select name="medID" id="medID" style="font-size:10px;" <%= disableSubformsString %> >
                <option value=""> -- Please Select a Medication -- </option>
                <%for(MedicationBean med : prodDAO.getNDCodesDAO().getAllNDCodes()){%>
                    <option value="<%=med.getNDCode()%>"
                        <% if (prescriptionError && lastMedID.equals(med.getNDCode())) %> selected="true" <% ; %>
                        >
                        <%= StringEscapeUtils.escapeHtml("" + (med.getNDCode())) %> - <%= StringEscapeUtils.escapeHtml("" + (med.getDescription())) %>
                    </option>
                                            
                <%}%>
            </select>
        </td>
        <td align=center>
            <input type="text" name="dosage" id="dosage" maxlength="6" style="width: 50px;" <%= disableSubformsString %> 
                <% if (prescriptionError) %> value="<%= lastDosage %>" <% ; %>
            > mg
        </td>
        <td align=center colspan=2>
            <input type="text" name="startDate" id="startDate" style="width: 80px;" <%= disableSubformsString %>
                onclick="displayDatePicker('startDate');" 
                onselect="displayDatePicker('startDate');"
                <% if (prescriptionError) {%>
                    value="<%= StringEscapeUtils.escapeHtml(lastStartDate) %>"
                <% } else { %>
                    value="<%= StringEscapeUtils.escapeHtml("" + (new SimpleDateFormat("MM/dd/yyyy").format(new Date()))) %>"
                <% } %>
                >
            to 
            <input type="text" name="endDate" id="endDate" style="width: 80px;" <%= disableSubformsString %>
                onclick="displayDatePicker('endDate');" 
                onselect="displayDatePicker('endDate');"
                <% if (prescriptionError) {%>
                    value="<%= StringEscapeUtils.escapeHtml(lastEndDate) %>"
                <% } else { %>
                    value="<%= StringEscapeUtils.escapeHtml("" + (new SimpleDateFormat("MM/dd/yyyy").format(new Date()))) %>"
                <% } %>
                >
        </td>
        <td align=center>
            <input type="text" name="instructions" id="instructions" 
                <% if (prescriptionError) {%>
                    value="<%= StringEscapeUtils.escapeHtml(lastInstructions) %>"
                <% } else { %>
                    value="<%= defaultInstructions %>" 
                <% } %>
                maxlength=500 <%= disableSubformsString %> >
        </td>
        <td>
            <!-- <input type="button" <%= disableSubformsString %> id="addprescription" onclick="setVar()" value="Add Prescription"> -->
            <input type="submit" name="addprescription" id="addprescription" value="Add Prescription" <%= disableSubformsString %> >
        </td>
     </tr>
     <tr><td colspan="6">
     <%
if (!("".equals(prescriptionErrorMsg) )){ %>
<br/><div style="background-color:yellow" align="center">
    <div style="color:black"><%= StringEscapeUtils.escapeHtml("" + (prescriptionErrorMsg)) %></div>
    
    Select an override reason (Hold control to select multiple reasons)<br/>
    <select multiple="multiple" size="9" name="overrideCode" title="To select multiple reasons use Control+Click. (Or if on a Mac, Command+Click)" id="overrideCode" style="font-size:10px;">
        <%for(OverrideReasonBean bean : prodDAO.getORCodesDAO().getAllORCodes()){%>
            <option value="<%=bean.getORCode()%>"><%= StringEscapeUtils.escapeHtml("" + (bean.getORCode())) %> - <%= StringEscapeUtils.escapeHtml("" + (bean.getDescription())) %></option>
                                    
        <%}%>
        <option value="00000">Other</option>
            
    </select>
    <div id="overrideReasonOtherForm">
    Additional Comments:<br/>
    <textarea name="overrideOther" id="overrideOther" ROWS=6 COLS=40 maxlength=210"></textarea></div>
    
    <script type="text/javascript">
   		$("#overrideReasonOtherForm").hide();
    	$("#overrideCode").change(function(){
    		 if ($("#overrideCode option[value='00000']:selected").length){
    			 $("#overrideReasonOtherForm").show();
    		 }else{
    			 
    			 $("#overrideReasonOtherForm").hide();
    		 }
    	});
    </script>
    <br/>
        <input type="submit" value="Continue" name="continue" id="continue"/>
        <input type="button" id="cancel" onclick="location.href='editOfficeVisit.jsp?ovID=<%= StringEscapeUtils.escapeHtml("" + ovIDString) %>'" value="Cancel" name="cancel" id="cancel"/><br/>
        
    </div>
    <BR>
    
<%}; %>
     </td></tr>
</table>




</form>

</div>

<% } %>
