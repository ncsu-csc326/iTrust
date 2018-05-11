<%@taglib prefix="itrust" uri="/WEB-INF/tags.tld"%>
<%@page errorPage="/auth/exceptionHandler.jsp"%>

<%@page import="edu.ncsu.csc.itrust.dao.DAOFactory"%>
<%@page import="edu.ncsu.csc.itrust.beans.PatientBean"%>
<%@page import="edu.ncsu.csc.itrust.action.EditPatientAction"%>
<%@page import="edu.ncsu.csc.itrust.BeanBuilder"%>
<%@page import="edu.ncsu.csc.itrust.enums.Ethnicity"%>
<%@page import="edu.ncsu.csc.itrust.enums.BloodType"%>
<%@page import="edu.ncsu.csc.itrust.exception.FormValidationException"%>
<%@page import="edu.ncsu.csc.itrust.enums.Gender"%>
<%@page import="edu.ncsu.csc.itrust.beans.DiagnosisBean"%>

<%@include file="/global.jsp"%>

<%
	pageTitle = "iTrust - Edit Patient";
%>

<%@include file="/header.jsp"%>
<itrust:patientNav thisTitle="Demographics" />
<%
	/* Require a Patient ID first */
	String pidString = (String) session.getAttribute("pid");
	if (pidString == null || pidString.equals("") || 1 > pidString.length()) {
		out.println("pidstring is null");
		response.sendRedirect("/iTrust/auth/getPatientID.jsp?forward=hcp-uap/editPatient.jsp");
		return;
	}
	
	/* If the patient id doesn't check out, then kick 'em out to the exception handler */
	EditPatientAction action = new EditPatientAction(prodDAO,
			loggedInMID.longValue(), pidString);

	/* Now take care of updating information */
	
	boolean formIsFilled = request.getParameter("formIsFilled") != null
			&& request.getParameter("formIsFilled").equals("true");
	PatientBean p;
	if (formIsFilled) {
		p = new BeanBuilder<PatientBean>().build(request
				.getParameterMap(), new PatientBean());
		try {
			action.updateInformation(p);
			loggingAction.logEvent(TransactionType.DEMOGRAPHICS_EDIT, loggedInMID.longValue(), p.getMID(), "");

	
%>
	<br />
	<div align=center>
		<span class="iTrustMessage">Information Successfully Updated</span>
	</div>
	<br />
<%
	} catch (FormValidationException e) {
%>
	<br />
	<div align=center>
		<span class="iTrustError"><%=StringEscapeUtils.escapeHtml(e.getMessage()) %></span>
	</div>
	<br />
<%
		}
	} else {
		p = action.getPatient();
		loggingAction.logEvent(TransactionType.DEMOGRAPHICS_VIEW, loggedInMID.longValue(), p.getMID(), "");
	}
	
%>

<% 
	boolean hasHistory = action.hasHistory();
	if (hasHistory) {
%>		
	<br />	
	<form id="viewHistory" action="viewPatientDemographicsHistory.jsp" method="post">
		<table class="fTable" align=center style="width: 350px;">
			<tr>
				<th colspan=2>Demographic History</th>
			</tr>
			<tr>
				<td class="subHeaderVertical">Past Demographics:</td>
				<td><input type="submit" value="View" /></td>
			</tr>		
		</table>
	</form>
<% } %>	

<form id="editForm" action="editPatient.jsp" method="post"><input type="hidden"
	name="formIsFilled" value="true"> <br />
<table cellspacing=0 align=center cellpadding=0>
	<tr>
		<td valign=top>
		<table class="fTable" align=center style="width: 350px;">
			<tr>
				<th colspan=2>Patient Information</th>
			</tr>		
			<tr>
			
				<td class="subHeaderVertical">First Name:</td>
				<td><input name="firstName" value="<%= StringEscapeUtils.escapeHtml("" + (p.getFirstName())) %>" type="text"></td>
			</tr>
			<tr>
				<td class="subHeaderVertical">Last Name:</td>
				<td><input name="lastName" value="<%= StringEscapeUtils.escapeHtml("" + (p.getLastName())) %>" type="text"></td>
			</tr>
			<tr>
				<td class="subHeaderVertical">Email:</td>
				<td><input name="email" value="<%= StringEscapeUtils.escapeHtml("" + (p.getEmail())) %>" type="text"></td>
			</tr>
			<tr>
				<td class="subHeaderVertical">Address:</td>
				<td><input name="streetAddress1"
					value="<%= StringEscapeUtils.escapeHtml("" + (p.getStreetAddress1())) %>" type="text"><br />
				<input name="streetAddress2" value="<%= StringEscapeUtils.escapeHtml("" + (p.getStreetAddress2())) %>" type="text"></td>
			</tr>
			<tr>
				<td class="subHeaderVertical">City:</td>
				<td><input name="city" value="<%= StringEscapeUtils.escapeHtml("" + (p.getCity())) %>" type="text">
				</td>
			</tr>
			<tr>
				<td class="subHeaderVertical">State:</td>
				<td><itrust:state name="state" value="<%= StringEscapeUtils.escapeHtml(p.getState()) %>" /></td>
			</tr>
			<tr>
				<td class="subHeaderVertical">Zip:</td>
				<td>
					<input name="zip" value="<%= StringEscapeUtils.escapeHtml("" + (p.getZip())) %>" maxlength="10" type="text" size="10">
				</td>
			</tr>
			<tr>
				<td class="subHeaderVertical">Phone:</td>
				<td>
					<input name="phone" value="<%= StringEscapeUtils.escapeHtml("" + (p.getPhone())) %>" type="text" size="12" maxlength="12">
			</tr>
			<tr>
				<td class="subHeaderVertical">Mother MID:</td>
				<td><input name="motherMID" value="<%= StringEscapeUtils.escapeHtml("" + (p.getMotherMID())) %>"
					maxlength="10" type="text"></td>
			</tr>

			<tr>
				<td class="subHeaderVertical">Father MID:</td>
				<td><input name="fatherMID" value="<%= StringEscapeUtils.escapeHtml("" + (p.getFatherMID())) %>"
					maxlength="10" type="text"></td>
			</tr>
			<tr>
				<td class="subHeaderVertical">Credit Card Type:</td>
				<td><select name="creditCardType">
				<option value="">Select:</option>
				<option value="MASTERCARD" <%= StringEscapeUtils.escapeHtml("" + ( p.getCreditCardType().equals("MASTERCARD") ? "selected" : "" )) %>>Mastercard</option>
				<option value="VISA" <%= StringEscapeUtils.escapeHtml("" + ( p.getCreditCardType().equals("VISA") ? "selected" : "" )) %>>Visa</option>
				<option value="DISCOVER" <%= StringEscapeUtils.escapeHtml("" + ( p.getCreditCardType().equals("DISCOVER") ? "selected" : "" )) %>>Discover</option>
				<option value="AMEX" <%= StringEscapeUtils.escapeHtml("" + ( p.getCreditCardType().equals("AMEX") ? "selected" : "" )) %>>American Express</option>
				</select>
				</td>
			</tr>
			<tr>
				<td class="subHeaderVertical">Credit Card Number:</td>
				<td><input name="creditCardNumber" value="<%= StringEscapeUtils.escapeHtml("" + (p.getCreditCardNumber())) %>"
					maxlength="19" type="text"></td>
			</tr>
			<tr>
				<td class="subHeaderVertical">Directions to Home:</td>
				<td ><textarea name="directionsToHome"><%= StringEscapeUtils.escapeHtml("" + (p.getDirectionsToHome())) %></textarea></td>
			</tr>
			<tr>
				<td class="subHeaderVertical">Religion:</td>
				<td ><input name="religion" value="<%= StringEscapeUtils.escapeHtml("" + (p.getReligion())) %>" type="text"></td>
			</tr>
			<tr>
				<td class="subHeaderVertical">Language:</td>
				<td ><input name="language" value="<%= StringEscapeUtils.escapeHtml("" + (p.getLanguage())) %>" type="text"></td>
			</tr>
			<tr>
				<td class="subHeaderVertical">Spiritual Practices:</td>
				<td ><textarea name="spiritualPractices"><%= StringEscapeUtils.escapeHtml("" + (p.getSpiritualPractices())) %></textarea></td>
			</tr>
			<tr>
				<td class="subHeaderVertical">Alternate Name:</td>
				<td ><input name="alternateName" value="<%= StringEscapeUtils.escapeHtml("" + (p.getAlternateName())) %>" type="text"></td>
			</tr>
		</table>
		<br />
		<table class="fTable" align=center style="width: 350px;">
			<tr>
				<th colspan=2>Insurance Information</th>
			</tr>
			<tr>
				<td class="subHeaderVertical">Name:</td>
				<td><input name="icName" value="<%= StringEscapeUtils.escapeHtml("" + (p.getIcName())) %>" type="text">
				</td>
			</tr>
			<tr>
				<td class="subHeaderVertical">Address:</td>
				<td><input name="icAddress1" value="<%= StringEscapeUtils.escapeHtml("" + (p.getIcAddress1())) %>"
					type="text"><br />
				<input name="icAddress2" value="<%= StringEscapeUtils.escapeHtml("" + (p.getIcAddress2())) %>" type="text">
				</td>
			</tr>
			<tr>
				<td class="subHeaderVertical">City:</td>
				<td><input name="icCity" value="<%= StringEscapeUtils.escapeHtml("" + (p.getIcCity())) %>" type="text">
				</td>
			</tr>

			<tr>
				<td class="subHeaderVertical">State:</td>
				<td><itrust:state name="icState" value="<%= StringEscapeUtils.escapeHtml(p.getIcState()) %>" />
				</td>
			</tr>
			<tr>
				<td class="subHeaderVertical">Zip:</td>
				<td>
					<input name="icZip" value="<%= StringEscapeUtils.escapeHtml("" + (p.getIcZip())) %>" maxlength="10" type="text" size="10">
				</td>
			</tr>
			<tr>
				<td class="subHeaderVertical">Phone:</td>
				<td>
					<input name="icPhone" value="<%= StringEscapeUtils.escapeHtml("" + (p.getIcPhone())) %>" type="text" size="12" maxlength="12">
				</td>
			</tr>
			<tr>
				<td class="subHeaderVertical">Insurance ID:</td>
				<td><input name="icID" value="<%= StringEscapeUtils.escapeHtml("" + (p.getIcID())) %>" type="text">
				</td>
			</tr>
		</table>
		</td>
		<td width="15px">&nbsp;</td>
		<td valign=top>
		<table class="fTable" align=center style="width: 350px;">
			<tr>
				<th colspan=2>Emergency Contact</th>
			</tr>
			<tr>
				<td class="subHeaderVertical">Name:</td>
				<td><input name="emergencyName"
					value="<%= StringEscapeUtils.escapeHtml("" + (p.getEmergencyName())) %>" type="text"></td>
			</tr>
			<tr>
				<td class="subHeaderVertical">Phone:</td>
				<td>
					<input name="emergencyPhone" value="<%= StringEscapeUtils.escapeHtml("" + (p.getEmergencyPhone())) %>" type="text" size="12" maxlength="12">
				</td>
			</tr>
		</table>
		<br />
		<table class="fTable" align=center style="width: 350px;">
			<tr>
				<th colspan=2>Health Information</th>
			</tr>
			<tr>
				<td class="subHeaderVertical">Ethnicity:</td>
				<td><select name="ethnicityStr">
					<%
						String selected = "";
						for (Ethnicity eth : Ethnicity.values()) {
							selected = (eth.equals(p.getEthnicity())) ? "selected=selected"
									: "";
					%>
					<option value="<%=eth.getName()%>" <%= StringEscapeUtils.escapeHtml("" + (selected)) %>><%= StringEscapeUtils.escapeHtml("" + (eth.getName())) %></option>
					<%
						}
					%>
				</select></td>
			</tr>
			<tr>
				<td class="subHeaderVertical">Blood Type:</td>
				<td><select name="bloodTypeStr">
					<%
						for (BloodType bt : BloodType.values()) {
							selected = (bt.equals(p.getBloodType())) ? "selected=selected"
									: "";
					%>
					<option value="<%=bt.getName()%>" <%= StringEscapeUtils.escapeHtml("" + (selected)) %>><%= StringEscapeUtils.escapeHtml("" + (bt.getName())) %></option>
					<%
						}
					%>
				</select>
			</tr>
			<tr>
				<td class="subHeaderVertical">Gender:</td>
				<td><select name="genderStr">
					<%
						for (Gender g : Gender.values()) {
							selected = (g.equals(p.getGender())) ? "selected=selected" : "";
					%>
					<option value="<%=g.getName()%>" <%= StringEscapeUtils.escapeHtml("" + (selected)) %>><%= StringEscapeUtils.escapeHtml("" + (g.getName())) %></option>
					<%
						}
					%>
				</select></td>
			</tr>
			<tr>
				<td class="subHeaderVertical">Date Of Birth:</td>
				<td><input type=text name="dateOfBirthStr" maxlength="10"
					size="10" value="<%= StringEscapeUtils.escapeHtml("" + (p.getDateOfBirthStr())) %>"> <input
					type=button value="Select Date"
					onclick="displayDatePicker('dateOfBirthStr');"></td>
			</tr>
			<tr>
				<td class="subHeaderVertical">Date Of Death:</td>
				<td><input type=text name="dateOfDeathStr" maxlength="10"
					size="10" value="<%= StringEscapeUtils.escapeHtml("" + (p.getDateOfDeathStr())) %>"> <input
					type=button value="Select Date"
					onclick="displayDatePicker('dateOfDeathStr');"></td>
			</tr>
			<tr>
				<td class="subHeaderVertical">Cause of Death:</td>
				<td><select name="causeOfDeath" style="font-size: 10">
					<option value="">-- None Selected --</option>
<% 
					for (DiagnosisBean diag : prodDAO.getICDCodesDAO().getAllICDCodes()){
						String select = "";
						if (diag.getICDCode().equals(p.getCauseOfDeath()))
							select = "selected=\"selected\"";
%>
					<option <%= StringEscapeUtils.escapeHtml("" + (select)) %> value="<%=diag.getICDCode()%>"><%= StringEscapeUtils.escapeHtml("" + (diag.getICDCode() )) %>
					- <jsp:expression>diag.getDescription()</jsp:expression></option>
<%					
			}
%>
				</select></td>
			</tr>
			<tr>
				<td class="subHeaderVertical">Topical Notes:</td>
				<td><textarea name="topicalNotes"><%= StringEscapeUtils.escapeHtml("" + (p.getTopicalNotes())) %></textarea>
				</td>
			</tr>
		</table>
		</td>
	</tr>
</table>
<br />
<div align=center>
	<% if(p.getDateOfDeactivationStr().equals("")){ %>
	<input type="submit" name="action" style="font-size: 16pt; font-weight: bold;" value="Edit Patient Record">
	<% } else { %>
	<span style="font-size: 16pt; font-weight: bold;">Patient is deactivated.  Cannot edit.</span>
	<% } %>
	<br /><br />
	<span style="font-size: 14px;">
		Note: in order to set the password for this user, use the "Reset Password" link at the login page.
	</span>
</div>
</form>
<br />
<br />
<itrust:patientNav thisTitle="Demographics" />

<%@include file="/footer.jsp"%>
