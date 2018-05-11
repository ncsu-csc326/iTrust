<%@page errorPage="/auth/exceptionHandler.jsp" %>

<%@page import="java.net.URLEncoder" %>
<%@page import="java.util.List"%>
<%@page import="edu.ncsu.csc.itrust.*"%>
<%@page import="edu.ncsu.csc.itrust.dao.DAOFactory"%>
<%@page import="edu.ncsu.csc.itrust.beans.HospitalBean"%>
<%@page import="edu.ncsu.csc.itrust.action.UpdateHospitalListAction"%>
<%@page import="edu.ncsu.csc.itrust.exception.FormValidationException"%>
<%@page import="edu.ncsu.csc.itrust.action.ViewDiagnosisStatisticsAction"%>
<%@page import="edu.ncsu.csc.itrust.beans.DiagnosisBean"%>
<%@page import="edu.ncsu.csc.itrust.beans.DiagnosisStatisticsBean"%>
<%@page import="edu.ncsu.csc.itrust.dao.mysql.DiagnosesDAO"%>
<%@page import="edu.ncsu.csc.itrust.exception.FormValidationException"%>
<%@page import="edu.ncsu.csc.itrust.dao.mysql.ICDCodesDAO"%>

<%@include file="/global.jsp" %>

<%
pageTitle = "iTrust - Maintain Diagnoses Links";
%>

<%@include file="/header.jsp" %>

<% 
	DiagnosesDAO diagnosesDAO = prodDAO.getDiagnosesDAO();
	ICDCodesDAO icdcodesDAO = prodDAO.getICDCodesDAO();
	//Taken from viewDiagnosisTrends.jsp
	//log the page view

	boolean formIsFilled = request.getParameter("formIsFilled") != null
		&& request.getParameter("formIsFilled").equals("true");
	boolean diagnosisIsChanged = request.getParameter("diagnosisIsChanged") != null
			&& request.getParameter("diagnosisIsChanged").equals("true");
	int whichOne = 0;

	ViewDiagnosisStatisticsAction diagnoses = new ViewDiagnosisStatisticsAction(prodDAO);
	DiagnosisStatisticsBean dsBean = null;

	//get form data
	String startDate = request.getParameter("startDate");
	String endDate = request.getParameter("endDate");
	
	String zipCode = request.getParameter("zipCode");
	if (zipCode == null)
		zipCode = "";
	
	String icdCode = request.getParameter("icdCode");
	
	//try to get the statistics. If there's an error, print it. If null is returned, it's the first page load
	try{
		dsBean = diagnoses.getDiagnosisStatistics(startDate, endDate, icdCode, zipCode);
	} catch(FormValidationException e){
		e.printHTML(pageContext.getOut());
	}
	
	if (startDate == null)
		startDate = "";
	if (endDate == null)
		endDate = "";
	if (icdCode == null)
		icdCode = "";
	
	DiagnosisBean[] myBean = new DiagnosisBean[diagnoses.getDiagnosisCodes().size()];
	String[] urlNames = new String[diagnoses.getDiagnosisCodes().size()];
	int index = 0;
	for(DiagnosisBean diag : diagnoses.getDiagnosisCodes()) {
		myBean[index] = diag; urlNames[index] = diag.getURL(); index++;
	}
	//From editMyDemographics.jsp
	if (diagnosisIsChanged) {
		//p = new BeanBuilder<PatientBean>().build(request
				//.getParameterMap(), new PatientBean());
		//try {
			//action.updateInformation(p);
			//saction.updateInformation(s);
			//loggingAction.logEvent(TransactionType.DEMOGRAPHICS_EDIT, loggedInMID, loggedInMID, "");
			boolean foundIt = false;
			for(DiagnosisBean diag : diagnoses.getDiagnosisCodes()) { 
				if(request.getParameter("diagnoses").equalsIgnoreCase(diag.getICDCode())){
					foundIt=true;
				}
				if(!foundIt){
					whichOne++;
				}
			}
			
			if(formIsFilled){
				//diagnoses.
			myBean[whichOne].setURL(request.getParameter("url"));
			//diagnosesDAO.edit(myBean[whichOne]);
			icdcodesDAO.updateCode((myBean[whichOne]));
			//Logs URL
			loggingAction.logEvent(TransactionType.DIAGNOSIS_URL_EDIT, loggedInMID.longValue(), 0, "");
			
%>
		<div align=center>
			<span class="iTrustMessage"><%= myBean[whichOne].getDescription() %>'s (<%= myBean[whichOne].getICDCode() %>) URL has been successfully updated to <%= myBean[whichOne].getURL().toString() %>.</span>
		</div>
<%
		//} catch (FormValidationException e) {
%>
		<div align=center>
			<span class="iTrustError"></span>
		</div>
<%
		}
		//p = action.getPatient();
		//}
	} else {
		//p = action.getPatient();
		//s = saction.retrieveInformation();
		//loggingAction.logEvent(TransactionType.DEMOGRAPHICS_VIEW, loggedInMID, pid, "");
	}
%>

<form name = "add_url"> 
<input type="hidden" name="diagnosisIsChanged" value="true"> 
<table class="fTable" align=center style="width: 350px;">
	<tr>
		<th align=center colspan = '2'>
			Add Links To Diagnoses
		</th>
	</tr>
	<tr>
		<td class="subHeaderVertical">
			Diagnoses
		</td>
		<td>
			<select name="diagnoses">
				<option value="">Select:</option>
				<%	
					for(DiagnosisBean diag : diagnoses.getDiagnosisCodes()) { 
						if(request.getParameter("diagnoses") != null && !request.getParameter("action").equalsIgnoreCase("Update")){
								if (diag.getICDCode().equals(icdCode)) { %>
								<option value="<%=diag.getICDCode()%>"><%= StringEscapeUtils.escapeHtml("" + (diag.getICDCode())) %>
								- <%= StringEscapeUtils.escapeHtml("" + (diag.getDescription())) %></option>
							<% 	
								} else { %>
								<option <% if(request.getParameter("diagnoses").equalsIgnoreCase(diag.getICDCode())){ %> selected="selected" <% } %> value="<%=diag.getICDCode()%>"><%= StringEscapeUtils.escapeHtml("" + (diag.getICDCode())) %>
								- <%= StringEscapeUtils.escapeHtml("" + (diag.getDescription())) %></option>
							<% 		
								} 
						}else{
							if (diag.getICDCode().equals(icdCode)) { %>
							<option selected="selected" value="<%=diag.getICDCode()%>"><%= StringEscapeUtils.escapeHtml("" + (diag.getICDCode())) %>
							- <%= StringEscapeUtils.escapeHtml("" + (diag.getDescription())) %></option>
						<% 	
							} else { %>
							<option value="<%=diag.getICDCode()%>"><%= StringEscapeUtils.escapeHtml("" + (diag.getICDCode())) %>
							- <%= StringEscapeUtils.escapeHtml("" + (diag.getDescription())) %></option>
						<% 		
							} 
						}%>
				<%}%>
			</select>
		</td>
	</tr>
<% if(diagnosisIsChanged && !request.getParameter("action").equalsIgnoreCase("Update")) { %>
	<tr>
		<td class="subHeaderVertical">
			URL
		</td>
		<td>
			<input name="url" value= "<%= myBean[whichOne].getURL() %>" type="text">
		</td>
	</tr>
	<tr>
		<td>
		</td>
		<td colspan = '2'>
			<input type="submit" name="action" id="action"
			style="font-size: 10pt; font-weight: bold;"
			value="Update" />
		</td>
	</tr>
<input type="hidden" name="formIsFilled" value="true"> 
<input type="hidden" name="diagnosisIsChanged" value="false"> 

<% } else { %>
	<tr>
		<td>
		</td>
		<td colspan = '2'>
			<input type="submit" name="action" id="action"
			style="font-size: 10pt; font-weight: bold;"
			value="Check" />
		</td>
	</tr>
	<% } %>
</table>

</form>

<%@include file="/footer.jsp" %>