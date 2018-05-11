<%@page import="edu.ncsu.csc.itrust.enums.Role"%>
<%@page import="edu.ncsu.csc.itrust.beans.PersonnelBean"%>
<%@page import="edu.ncsu.csc.itrust.beans.ProcedureBean"%>
<%@page import="edu.ncsu.csc.itrust.beans.MedicationBean"%>
<%@page import="edu.ncsu.csc.itrust.beans.PrescriptionBean"%>
<%@page import="edu.ncsu.csc.itrust.beans.DiagnosisBean"%>
<%@page import="edu.ncsu.csc.itrust.dao.mysql.ICDCodesDAO"%>
<%@page import="edu.ncsu.csc.itrust.report.MedicalReportFilter"%>
<%@page
	import="edu.ncsu.csc.itrust.report.DemographicReportFilter.DemographicReportFilterType"%>
<%@page
	import="edu.ncsu.csc.itrust.report.PersonnelReportFilter.PersonnelReportFilterType"%>
<%@page
	import="edu.ncsu.csc.itrust.report.MedicalReportFilter.MedicalReportFilterType"%>
<%@page import="edu.ncsu.csc.itrust.report.DemographicReportFilter"%>
<%@page import="edu.ncsu.csc.itrust.report.PersonnelReportFilter"%>
<%@page errorPage="/auth/exceptionHandler.jsp"%>

<%@page import="java.util.List"%>

<%@include file="/global.jsp"%>

<%
	pageTitle = "iTrust - Generate Group Report";
%>

<%@include file="/header.jsp"%>
<h1>Generate Group Report</h1>
<%
	if (session.getAttribute("validationError")!=null){
		session.setAttribute("validationError",null);
	%>
		<h3 style="color: red;">Invalid age range entered!  Please check the age range and try again!</h3>
	<%
	}

	if (request.getParameter("fillValues") != null) {
%>
<h2>Step 2: Input Filter Values</h2>
<form id="mainForm2" method="post" name="reportForm" action="viewReport.jsp">
	<fieldset class="filter_fieldset">
		<legend>Demographic Filters</legend>
		<%
			if (request.getParameterValues("demographics") != null) {
					String demoFilters[] = request
							.getParameterValues("demographics");
					String params = "";
					for (String filter : demoFilters) {
						params += filter + " ";
					}
					out.println("<input type=\"hidden\" name=\"demoparams\" value=\""
							+ params + "\" />");
					for (String filter : demoFilters) {
						DemographicReportFilterType type = DemographicReportFilter
								.filterTypeFromString(filter);
						out.println("<p>" + type.toString() + "</p>");
						if (type == DemographicReportFilterType.GENDER) {
							out.println("<select name=\"" + filter + "\">");
							out.print("<option value=\"Male\">Male</option>");
							out.print("<option value=\"Female\">Female</option>");
							out.print("<option value=\"Not Specified\">Not Specified</option>");
							out.println("</select>");
						} else if (type == DemographicReportFilterType.DEACTIVATED) {
							out.println("<select name=\"" + filter + "\">");
							out.print("<option value=\"allow\">Show both</option>");
							out.print("<option value=\"exclude\">Exclude deactivated</option>");
							out.print("<option value=\"only\">Show only deactivated</option>");
							out.println("</select>");
						} else {
							out.println("<input name=\"" + filter
									+ "\" type=\"text\" />");
						}
					}
				}
		%>
	</fieldset>
	<fieldset class="filter_fieldset">
		<legend>Medical Filters</legend>
		<%
			if (request.getParameterValues("medical") != null) {
					String medFilters[] = request.getParameterValues("medical");
					String params = "";
					for (String filter : medFilters) {
						params += filter + " ";
					}
					out.println("<input type=\"hidden\" name=\"medparams\" value=\""
							+ params + "\" />");
					for (String filter : medFilters) {
						MedicalReportFilterType type = MedicalReportFilter
								.filterTypeFromString(filter);
						out.println("<p>" + type.toString() + "</p>");
						if (type == MedicalReportFilterType.DIAGNOSIS_ICD_CODE
								|| type == MedicalReportFilterType.MISSING_DIAGNOSIS_ICD_CODE) {
							out.println("<select name=\"" + filter
									+ "\" multiple size=\"7\">");
							List<DiagnosisBean> diagnoses = prodDAO
									.getICDCodesDAO().getAllICDCodes();
							for (DiagnosisBean diagnosis : diagnoses) {
								out.println("<option value=\""
										+ diagnosis.getICDCode() + "\">"
										+ diagnosis.getFormattedDescription()
										+ "</option>");
							}
							out.println("</select>");
						} else if (type == MedicalReportFilterType.PASTCURRENT_PRESCRIPTIONS
								|| type == MedicalReportFilterType.CURRENT_PRESCRIPTIONS
								|| type == MedicalReportFilterType.ALLERGY) {
							out.println("<select name=\"" + filter
									+ "\" multiple size=\"7\">");
							List<MedicationBean> meds = prodDAO.getNDCodesDAO()
									.getAllNDCodes();
							for (MedicationBean med : meds) {
								out.println("<option value=\""
										+ med.getNDCode() + "\">"
										+ med.getNDCodeFormatted() + "-"
										+ med.getDescription() + "</option>");
							}
							out.println("</select>");
						} else if (type == MedicalReportFilterType.PROCEDURE) {
							out.println("<select name=\"" + filter
									+ "\" multiple size=\"7\">");
							List<ProcedureBean> procs = prodDAO
									.getCPTCodesDAO().getAllCPTCodes();
							for (ProcedureBean proc : procs) {
								out.println("<option value=\""
										+ proc.getCPTCode() + "\">"
										+ proc.getCPTCode() + "-"
										+ proc.getDescription() + "</option>");
							}
							out.println("</select>");
						} else {
							out.println("<input name=\"" + filter
									+ "\" type=\"text\" />");
						}
					}
				}
		%>
	</fieldset>
	<fieldset class="filter_fieldset">
		<legend>Personnel Filters</legend>
		<%
			if (request.getParameterValues("personnel") != null) {
					String personnelFilters[] = request
							.getParameterValues("personnel");
					String params = "";
					for (String filter : personnelFilters) {
						params += filter + " ";
					}
					out.println("<input type=\"hidden\" name=\"persparams\" value=\""
							+ params + "\" />");
					for (String filter : personnelFilters) {
						PersonnelReportFilterType type = PersonnelReportFilter
								.filterTypeFromString(filter);
						out.println("<p>" + type.toString() + "</p>");
						if (type == PersonnelReportFilterType.DLHCP) {
							out.println("<select name=\"" + filter
									+ "\" multiple>");
							List<PersonnelBean> people = prodDAO
									.getPersonnelDAO().getAllPersonnel();
							for (PersonnelBean person : people) {
								if (person.getRole() == Role.HCP) {
									out.println("<option value=\""
											+ person.getFullName() + "\">"
											+ person.getFullName()
											+ "</option>");
								}
							}
							out.println("</select>");
						} else {
							out.println("<input name=\"" + filter
									+ "\" type=\"text\" />");
						}
					}
				}
		%>
	</fieldset>
	<input class="clear_button" type="submit" name="generate" value="Generate Report" onclick="return generateReport();"/>
	<input class="clear_button" type="submit" name="download" value="Download Report" onclick="return downloadReport();"/>
</form>
<%
	} else {
%>
<h2>Step 1: Select Filters</h2>
<form id="mainForm" method="post">
	<fieldset class="filter_fieldset">
		<legend>Demographic Filters</legend>
		<%
			for (DemographicReportFilterType filter : DemographicReportFilterType
						.values()) {
					out.println("<label><input type=\"checkbox\" name=\"demographics\" value=\""
							+ filter.name()
							+ "\">"
							+ filter.toString()
							+ "</label><br />");
				}
		%>
	</fieldset>
	<fieldset class="filter_fieldset">
		<legend>Medical Filters</legend>
		<%
			for (MedicalReportFilterType filter : MedicalReportFilterType
						.values()) {
					out.println("<label><input type=\"checkbox\" name=\"medical\" value=\""
							+ filter.name()
							+ "\">"
							+ filter.toString()
							+ "</label><br />");
				}
		%>
	</fieldset>
	<fieldset class="filter_fieldset">
		<legend>Personnel Filters</legend>
		<%
			for (PersonnelReportFilterType filter : PersonnelReportFilterType
						.values()) {
					out.println("<label><input type=\"checkbox\" name=\"personnel\" value=\""
							+ filter.name()
							+ "\">"
							+ filter.toString()
							+ "</label><br />");
				}
		%>
	</fieldset>
	<input class="clear_button" type="submit" name="fillValues" value="Continue" />
</form>
<%
	}
%>
<script language="Javascript">
<!--
function downloadReport()
{
    document.reportForm.action = "groupReportGenerator"
    return true;
}

function generateReport()
{
    document.reportForm.action = "viewReport.jsp"
    return true;
}
-->
</script>
<%@include file="/footer.jsp"%>