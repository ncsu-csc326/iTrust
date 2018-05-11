<%@taglib prefix="itrust" uri="/WEB-INF/tags.tld" %>
<%@page errorPage="/auth/exceptionHandler.jsp" %>

<%@page import="java.util.List"%>
<%@page import="java.util.Map"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.Date"%>

<%@page import="edu.ncsu.csc.itrust.EmailUtil"%>
<%@page import="edu.ncsu.csc.itrust.beans.OfficeVisitBean"%>
<%@page import="edu.ncsu.csc.itrust.beans.LabProcedureBean"%>
<%@page import="edu.ncsu.csc.itrust.beans.LOINCbean"%>
<%@page import="edu.ncsu.csc.itrust.dao.DAOFactory"%>
<%@page import="edu.ncsu.csc.itrust.beans.DiagnosisBean"%>
<%@page import="edu.ncsu.csc.itrust.beans.MedicationBean"%>
<%@page import="edu.ncsu.csc.itrust.beans.OverrideReasonBean"%>
<%@page import="edu.ncsu.csc.itrust.beans.ProcedureBean"%>
<%@page import="edu.ncsu.csc.itrust.action.EditOfficeVisitAction"%>
<%@page import="edu.ncsu.csc.itrust.action.AddOfficeVisitAction"%>
<%@page import="edu.ncsu.csc.itrust.beans.forms.EditOfficeVisitForm"%>
<%@page import="edu.ncsu.csc.itrust.exception.ITrustException"%>
<%@page import="edu.ncsu.csc.itrust.beans.HospitalBean"%>
<%@page import="edu.ncsu.csc.itrust.beans.BillingBean"%>
<%@page import="edu.ncsu.csc.itrust.BeanBuilder"%>
<%@page import="edu.ncsu.csc.itrust.exception.FormValidationException"%>
<%@page import="edu.ncsu.csc.itrust.beans.PersonnelBean"%>
<%@page import="edu.ncsu.csc.itrust.dao.mysql.PersonnelDAO"%>
<%@page import="edu.ncsu.csc.itrust.beans.PrescriptionBean"%>
<%@page import="edu.ncsu.csc.itrust.beans.ApptTypeBean"%>
<%@page import="edu.ncsu.csc.itrust.dao.mysql.ApptTypeDAO"%>
<%@page import="edu.ncsu.csc.itrust.dao.mysql.BillingDAO"%>

<%@include file="/global.jsp" %>

<%
String visitName = "Office Visit";
pageTitle = "iTrust - Document "+visitName;
%>

<%@include file="/header.jsp" %>

<%
	boolean createVisit = false;

    String submittedFormName = request.getParameter("formName");
    
	String ovIDString = request.getParameter("ovID");
	String pidString = (String)session.getAttribute("pid");
	
    if (pidString == null || pidString.length() == 0) {
        response.sendRedirect("/iTrust/auth/getPatientID.jsp?forward=iTrust/auth/hcp-uap/editOfficeVisit.jsp?ovID=" + ovIDString);
        return;
    }
	
	EditOfficeVisitAction ovaction = null;
	
	if(ovIDString == null || ovIDString.length() == 0 || ovIDString.equals("-1")) {
		// submittedFormName must be: "mainForm" or null
		ovaction = new EditOfficeVisitAction(prodDAO, loggedInMID, pidString);
		createVisit = true;
	} else {
		ovaction = new EditOfficeVisitAction(prodDAO, loggedInMID, pidString, ovIDString);
	}
	
    String confirm = "";
    String warning = "";
    if ("mainForm".equals(submittedFormName)) {
    	OfficeVisitBean visit = ovaction.getOfficeVisit();
    	EditOfficeVisitForm form = new BeanBuilder<EditOfficeVisitForm>().build(request.getParameterMap(), new EditOfficeVisitForm());
    	form.setHcpID("" + visit.getHcpID());
        form.setPatientID("" + visit.getPatientID());
        form.setIsBilled(request.getParameter("isBilled"));
        try {
        	confirm = ovaction.updateInformation(form, false);
        	ovIDString = ""+ovaction.getOvID();
        	if (createVisit) {        		
       			ovaction.logOfficeVisitEvent(TransactionType.OFFICE_VISIT_CREATE);
        		createVisit = false;
        		if(form.getIsBilled()){
					BillingDAO bDAO = prodDAO.getBillingDAO();
					BillingBean b = new BillingBean();
					b.setApptID(Integer.parseInt(ovIDString));
					b.setAmt(40);
					b.setHcp(Long.parseLong(form.getHcpID()));
					b.setStatus("Unsubmitted");
					b.setPatient(Long.parseLong(form.getPatientID()));
					b.setAmt(Integer.parseInt(request.getParameter("billAmt")));
					bDAO.addBill(b);
					ovaction.logOfficeVisitBillingEvent(TransactionType.OFFICE_VISIT_BILLED);
        		}
        	} else {
                ovaction.logOfficeVisitEvent(TransactionType.OFFICE_VISIT_EDIT);
        	}
        } catch (FormValidationException e) {
            e.printHTML(pageContext.getOut());
            confirm = "Input not valid";
        }
    } else if (!createVisit) {
        ovaction.logOfficeVisitEvent(TransactionType.OFFICE_VISIT_VIEW);
    }
    
    String disableSubformsString = createVisit ? "disabled=\"true\"" : "";
    String disableMainformString = createVisit ? "" : "disabled=\"true\"";
    
	OfficeVisitBean ovbean = ovaction.getOfficeVisit();
	visitName = ovbean.isERIncident() || userRole.equals("er") ? "ER Visit" : "Office Visit";
	List<HospitalBean> hcpHospitals = ovaction.getHospitals();
	ApptTypeDAO atDAO = new ApptTypeDAO(prodDAO);
	List<ApptTypeBean> apptTypes = atDAO.getApptTypes();
	
%>

<%
if (!"".equals(confirm)) {
	if ("false".equals(request.getParameter("checkPresc"))){ %>
		<span class="iTrustMessage">Operation Canceled</span>
	<% } else if ("success".equals(confirm)) { 
			
	%>
		<span class="iTrustMessage">Information Successfully Updated</span>
<%	}
	else { %>
		<span class="iTrustError"><%= StringEscapeUtils.escapeHtml("" + (confirm)) %></span>		
<%	}
}	
%>

<script type="text/javascript">
	function removeID(type, value) {
		document.getElementById(type).value = value;
		document.forms[0].submit();
	}

	function setVar(){
		var medID = document.getElementById("addMedID");
		var medIDindex = medID.options.selectedIndex;
		var medIDtxt = medID.options[medIDindex].value;
		var medDostxt = document.getElementById("dosage").value;
		var medStarttxt = document.getElementById("startDate").value;
		var medEndtxt = document.getElementById("endDate").value;
		var medInsttxt = document.getElementById("instructions").value;
		document.getElementById("testMed").value = medIDtxt;
		document.getElementById("medDos").value = medDostxt;
		document.getElementById("medStart").value = medStarttxt;
		document.getElementById("medEnd").value = medEndtxt;
		document.getElementById("medInst").value = medInsttxt;
		document.forms[0].submit();
	}

	function presCont(){
		document.getElementById("checkPresc").value = "true";
		document.forms[0].submit();
	}

	function presCanc(){
		var medID = document.getElementById("addMedID");
		var medIDindex = medID.options.selectedIndex;
		var medIDtxt = medID.options[medIDindex].value;
		var medDostxt = document.getElementById("dosage").value;
		var medStarttxt = document.getElementById("startDate").value;
		var medEndtxt = document.getElementById("endDate").value;
		var medInsttxt = document.getElementById("instructions").value;

		document.getElementById("testMed").value = "";
		document.getElementById("medDos").value = "";
		document.getElementById("medStart").value = "";
		document.getElementById("medEnd").value = "";
		document.getElementById("medInst").value = "";
		document.getElementById("checkPresc").value = "false";
		document.forms[0].submit();
	}
	
</script>

<%--
<form action="editOfficeVisit.jsp" method="post" id="mainForm_old">
	<input type="hidden" name="formIsFilled" value="true" />
	<input type="hidden" name="ovID" value="<%= StringEscapeUtils.escapeHtml("" + (ovaction.getOvID())) %>" />
	<input type="hidden" id="checkPresc" name="checkPresc" value="" />
	<input type="hidden" id="testMed" name="testMed" value=<%= StringEscapeUtils.escapeHtml("" + (request.getParameter("testMed") )) %> />
	<input type="hidden" id="medDos" name="medDos" value=<%= StringEscapeUtils.escapeHtml("" + (request.getParameter("medDos") )) %> />
	<input type="hidden" id="medStart" name="medStart" value=<%= StringEscapeUtils.escapeHtml("" + (request.getParameter("medStart") )) %> />
	<input type="hidden" id="medEnd" name="medEnd" value=<%= StringEscapeUtils.escapeHtml("" + (request.getParameter("medEnd") )) %> />
	<input type="hidden" id="medInst" name="medInst" value=<%= StringEscapeUtils.escapeHtml("" + (request.getParameter("medInst") )) %> />
</form>
--%>
<%
String localHtmlMenu = "<div align=center style=\"margin-bottom: 0.5em\">" +
	       "<a href=\"#general\">General</a>" +
		" | <a href=\"#basic-health\">Health Metrics</a>" +
	    " | <a href=\"#prescriptions\">Prescriptions</a>" +
	    " | <a href=\"#labprocedures\">Lab Procedures</a>" +
	    " | <a href=\"#patient-specific-instructions\">Patient-Specific Instructions</a>" +
	    " | <a href=\"#diagnoses\">Diagnoses</a>" +
	    " | <a href=\"#procedures\">Procedures</a>" +
	    " | <a href=\"#immunizations\">Immunizations</a>" +
	    " | <a href=\"#referrals\">Referrals</a>" +
	"</div>";
%>

<%= localHtmlMenu %>
<br/>

<div align=center id="general">
<form action="editOfficeVisit.jsp" method="post" id="mainForm">
<input type="hidden" name="formIsFilled" value="true" />
<input type="hidden" name="formName" value="mainForm" />
<input type="hidden" name="ovID" value="<%= StringEscapeUtils.escapeHtml("" + (ovaction.getOvID())) %>" />

<table class="fTable" align="center">
	<tr>
		<th colspan="2"><a href="#" class="topLink">[Top]</a><%=visitName %></th>
	</tr>
	<tr>
		<td class="subHeaderVertical">Patient ID:</td>
		<td><%= StringEscapeUtils.escapeHtml("" + (prodDAO.getAuthDAO().getUserName(ovbean.getPatientID()))) %> </td>
	</tr>
	<tr>
		<td class="subHeaderVertical">Date of Visit:</td>
		<td>
		  <input name="visitDate" value="<%= StringEscapeUtils.escapeHtml("" + (ovbean.getVisitDateStr())) %>" <%= disableMainformString %>/>
		  <input type="button" value="Select Date" onclick="displayDatePicker('visitDate');" <%= disableMainformString %>/>
		</td>
	</tr>
	<tr>
		<td class="subHeaderVertical">Hospital:</td>
		<td>
		    <select name="hospitalID" <%= disableMainformString %>>
				<option value="">HCP's Home</option>
				<%for(HospitalBean hos : hcpHospitals) {%>
					<option value="<%= StringEscapeUtils.escapeHtml("" + (hos.getHospitalID())) %>" 
						<%= StringEscapeUtils.escapeHtml("" + (hos.getHospitalID().equals(ovbean.getHospitalID()) ? "selected=selected" : "")) %> > 
						<%= StringEscapeUtils.escapeHtml("" + (hos.getHospitalName())) %>
					</option>
				<%} %>
			</select>
		</td>
	</tr>
	<tr>
		<td class="subHeaderVertical">Appointment Type:</td>
		<td>
		    <select name="apptType" <%= disableMainformString %>>
				<option value="">Not Selected</option>
				<%for(ApptTypeBean at : apptTypes) {%>
					<option value="<%= StringEscapeUtils.escapeHtml("" + (at.getName())) %>" 
						<%= StringEscapeUtils.escapeHtml("" + (at.getName().equals(ovbean.getAppointmentType()) ? "selected=selected" : "")) %> > 
						<%= StringEscapeUtils.escapeHtml("" + (at.getName())) %>
					</option>
				<%} %>
			</select>
		</td>
	</tr>
	<tr>
		<td class="subHeaderVertical">Notes:</td>
		<td><textarea rows="4" 
		              style="width: 40em; height: 5em; font-family: sans-serif;" 
		              name="notes"
		              ><%= StringEscapeUtils.escapeHtml("" + (ovbean.getNotes())) %></textarea>
		</td>
	</tr>
	<tr>
		<td class="subHeaderVertical">Billed:</td>
		<td>
			<%if(ovbean.isBilled()) {%>
				<input type="checkbox" name="isBilled" value="true" checked <%= disableMainformString %>/> send bill for $
			<%}else {%>
				<input type="checkbox"  name="isBilled" value="true" <%= disableMainformString %>/> send bill for $ 
			<%}%>
				<input name="billAmt" value="100" <%= disableMainformString %>>
		</td>
	</tr>
	<tr>
	   <td colspan="2" align="center">
		<% if (createVisit) { %>
		    <input type="submit" name="update" id="update" value="Create" >
		<% } else { %>
		    <input type="submit" name="update" id="update" value="Update" >
		<% } %>
		</td>
    </tr>
</table>
</form>
</div>

<br /><br />

<a name="basic-health"></a>
<%@include file="editOVBasicHealth.jsp" %>

<br /><br />

<a name="prescriptions"></a>
<%@include file="editOVPrescription.jsp" %>

<br /><br />

<a name="labprocedures"></a>
<%@include file="editOVLabProcedure.jsp" %>

<br /><br />

<a name="patient-specific-instructions"></a>
<%@include file="editOVPatientInstructions.jsp" %>

<br /><br />


<a name="diagnoses"></a>
<%@include file="editOVDiagnosis.jsp" %>
	
<br/><br/>
	
<a name="procedures"></a>
<%@include file="editOVProcedure.jsp" %>
    
<br/><br/>
	
<a name="immunizations"></a>
<%@include file="editOVImmunization.jsp" %>


<br/><br/>

<a name="referrals"></a>
<%@include file="editOVReferral.jsp" %>
<br/>
<%= localHtmlMenu %>

<br /><br /><br />

<%if(userRole.equals("hcp")){%>
	<div align="center"><itrust:patientNav /></div>
<%}%>
<br />

<%@include file="/footer.jsp" %>


