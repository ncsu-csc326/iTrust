<%@page import="edu.ncsu.csc.itrust.model.old.dao.DAOFactory"%>
<%@page import="edu.ncsu.csc.itrust.model.old.dao.mysql.PatientDAO"%>
<%@page import="edu.ncsu.csc.itrust.model.old.beans.PatientBean"%>
<%@page import="edu.ncsu.csc.itrust.action.EditPatientAction"%>
<%@page import="edu.ncsu.csc.itrust.action.ViewPatientAction"%>
<%@page import="edu.ncsu.csc.itrust.BeanBuilder"%>
<%@page import="edu.ncsu.csc.itrust.exception.FormValidationException"%>
<%@page import="edu.ncsu.csc.itrust.action.SetSecurityQuestionAction"%>
<%@page import="edu.ncsu.csc.itrust.model.old.beans.SecurityQA"%>
<%@page import="org.apache.commons.validator.CreditCardValidator"%>
<%@page import="java.util.List"%>

<%@taglib prefix="itrust" uri="/WEB-INF/tags.tld"%>
<%@page errorPage="/auth/exceptionHandler.jsp"%>

<%@include file="/global.jsp"%>

<%
	pageTitle = "iTrust - Edit Patient";
%>

<%@include file="/header.jsp"%>

<%
	/* If the patient id doesn't check out, then kick 'em out to the exception handler */
	ViewPatientAction viewPA = new ViewPatientAction(prodDAO,
			loggedInMID.longValue(), "" + loggedInMID.longValue());
	EditPatientAction action = null;
	SetSecurityQuestionAction saction = null;
	PatientBean parent;
	SecurityQA s;

	String cID = request.getParameter("pid");
	long changeID = -1;

	boolean isBadcID = cID == null || cID.equals("null");
	if (isBadcID) {
		action = new EditPatientAction(prodDAO,
				loggedInMID.longValue(), "" + loggedInMID.longValue());
		saction = new SetSecurityQuestionAction(prodDAO,
				loggedInMID.longValue());
	} else {
		changeID = Long.parseLong(cID);
		action = new EditPatientAction(prodDAO, changeID, "" + changeID);
		saction = new SetSecurityQuestionAction(prodDAO, changeID);
	}
	long pid = action.getPid();

	/* Now take care of updating information */
	boolean formIsFilled = request.getParameter("formIsFilled") != null
			&& request.getParameter("formIsFilled").equals("true");

	if (formIsFilled) {
		parent = new BeanBuilder<PatientBean>().build(
				request.getParameterMap(), new PatientBean());
		s = new BeanBuilder<SecurityQA>().build(
				request.getParameterMap(), new SecurityQA());
		
		try {
			action.updateInformation(parent);
			saction.updateInformation(s);
			
			Long secondaryMID = changeID == loggedInMID.longValue() ? loggedInMID : changeID;
			viewPA.logEditDemographics(loggedInMID, secondaryMID);
			//response.sendRedirect(request.getRequestURI());
%>

<div align=center>
	<span class="iTrustMessage">Information Successfully Updated</span>
</div>
<%
		} catch (FormValidationException e) {
%>
<div align=center>
	<span class="iTrustError"><%=StringEscapeUtils.escapeHtml(e.getMessage())%></span>
</div>
<%
			parent = action.getPatient();
		}
	} else {
		parent = action.getPatient();
		s = saction.retrieveInformation();
		viewPA.logViewDemographics(loggedInMID, new Long(parent.getMID()));
	}
	List<PatientBean> users = viewPA.getViewablePatients();
%>

<script type="text/javascript">
	$(document)
			.ready(
					function() {

						var current = "#table_1";
<%for (int i = 0; i < users.size(); i++) {
				if (i != 0) {%>
	$(
								"#table_"
										+
<%=StringEscapeUtils.escapeHtml("" + (i + 1))%>
	)
								.hide();
<%}%>
	$(
								"#link_"
										+
<%=StringEscapeUtils.escapeHtml("" + (i + 1))%>
	)
								.click(
										function() {
											if (current !== null
													&& current !== ("#table_" +
<%=StringEscapeUtils.escapeHtml("" + (i + 1))%>
	)) {
												$(current).slideUp(750);
												current = "#table_"
														+
<%=StringEscapeUtils.escapeHtml("" + (i + 1))%>
	;
												$(current).slideDown(750);
											}
										});
<%}%>
	});
</script>


<table cellspacing=0 align=center cellpadding=0 class="fTable">
	<%
		int i = 1;
		for (PatientBean p : users) {
	%>
	<br />
	<tr>
		<th colspan="3" id="link_<%=StringEscapeUtils.escapeHtml("" + i)%>">
			<%=StringEscapeUtils.escapeHtml(""
						+ (p.getFirstName() + " " + p.getLastName()))%>
			<%
				if (users.size() > 1) {
			%> <span
			class="glyphicon glyphicon-chevron-down"
			style="font-size: 8pt; color: #FFFFFF; float: right;"></span> <%
 	}
 %>
		</th>
	</tr>
	<tr>
		<td>
			<div id="table_<%=StringEscapeUtils.escapeHtml("" + i)%>">
				<form action="editMyDemographics.jsp" method="post"
					id="edit_<%=StringEscapeUtils.escapeHtml("" + i)%>">

					
					<table>
							<tr>
								<td valign=top>
								<div class="row">
								<div class="col-md-6">
									
									<table class="fTable" align=center style="width: 350px;">
										<tr>
											<th colspan=2>Patient Information <input type="hidden"
												name="formIsFilled" value="true"> <input
												type="hidden" name="pid"
												value="<%=StringEscapeUtils.escapeHtml("" + (p.getMID()))%>">
											</th>
										</tr>
										<tr>
											<td class="subHeaderVertical">First Name:</td>
											<td><input name="firstName"
												value="<%=StringEscapeUtils.escapeHtml("" + (p.getFirstName()))%>"
												type="text"></td>
										</tr>
										<tr>
											<td class="subHeaderVertical">Last Name:</td>
											<td><input name="lastName"
												value="<%=StringEscapeUtils.escapeHtml("" + (p.getLastName()))%>"
												type="text"></td>
										</tr>
										<tr>
											<td class="subHeaderVertical">Email:</td>
											<td><input name="email"
												value="<%=StringEscapeUtils.escapeHtml("" + (p.getEmail()))%>"
												type="text"></td>
										</tr>
										<tr>
											<td class="subHeaderVertical">Address:</td>
											<td><input name="streetAddress1"
												value="<%=StringEscapeUtils.escapeHtml(""
						+ (p.getStreetAddress1()))%>"
												type="text"><br /> <input name="streetAddress2"
												value="<%=StringEscapeUtils.escapeHtml(""
						+ (p.getStreetAddress2()))%>"
												type="text"></td>
										</tr>
										<tr>
											<td class="subHeaderVertical">City:</td>
											<td><input name="city"
												value="<%=StringEscapeUtils.escapeHtml("" + (p.getCity()))%>"
												type="text"></td>
										</tr>
										<tr>
											<td class="subHeaderVertical">State:</td>
											<td><itrust:state name="state"
													value="<%=StringEscapeUtils.escapeHtml(p.getState())%>" /></td>
										</tr>
										<tr>
											<td class="subHeaderVertical">Zip:</td>
											<td><input name="zip"
												value="<%=StringEscapeUtils.escapeHtml("" + (p.getZip()))%>"
												maxlength="10" type="text" size="10"></td>
										</tr>
										<tr>
											<td class="subHeaderVertical">Phone:</td>
											<td><input name="phone"
												value="<%=StringEscapeUtils.escapeHtml("" + (p.getPhone()))%>"
												type="text" size="12" maxlength="12">
										</tr>

										<tr>
											<td class="subHeaderVertical">Mother MID:</td>
											<td><input name="MotherMID"
												value="<%=StringEscapeUtils.escapeHtml("" + (p.getMotherMID()))%>"
												maxlength="10" type="text"></td>
										</tr>

										<tr>
											<td class="subHeaderVertical">Father MID:</td>
											<td><input name="FatherMID"
												value="<%=StringEscapeUtils.escapeHtml("" + (p.getFatherMID()))%>"
												maxlength="10" type="text"></td>
										</tr>
										<tr>
											<td class="subHeaderVertical">Credit Card Type:</td>
											<td><select name="creditCardType">
													<option value="">Select:</option>
													<option value="MASTERCARD"
														<%=StringEscapeUtils
						.escapeHtml(""
								+ (p.getCreditCardType().equals("MASTERCARD") ? "selected"
										: ""))%>>Mastercard</option>
													<option value="VISA"
														<%=StringEscapeUtils.escapeHtml(""
						+ (p.getCreditCardType().equals("VISA") ? "selected"
								: ""))%>>Visa</option>
													<option value="DISCOVER"
														<%=StringEscapeUtils
						.escapeHtml(""
								+ (p.getCreditCardType().equals("DISCOVER") ? "selected"
										: ""))%>>Discover</option>
													<option value="AMEX"
														<%=StringEscapeUtils.escapeHtml(""
						+ (p.getCreditCardType().equals("AMEX") ? "selected"
								: ""))%>>American
														Express</option>
											</select></td>
										</tr>
										<tr>
											<td class="subHeaderVertical">Credit Card Number:</td>
											<td><input name="creditCardNumber"
												value="<%=StringEscapeUtils.escapeHtml(""
						+ (p.getCreditCardNumber()))%>"
												maxlength="19" type="text"></td>
										</tr>
									</table>

								</div>
								<div class="col-md-6">
									<table class="fTable" align=center style="width: 350px;">
										<tr>
											<th colspan=2>Insurance Information</th>
										</tr>
										<tr>
											<td class="subHeaderVertical">Name:</td>
											<td><input name="icName"
												value="<%=StringEscapeUtils.escapeHtml("" + (p.getIcName()))%>"
												type="text"></td>
										</tr>
										<tr>
											<td class="subHeaderVertical">Address:</td>
											<td><input name="icAddress1"
												value="<%=StringEscapeUtils.escapeHtml("" + (p.getIcAddress1()))%>"
												type="text"><br /> <input name="icAddress2"
												value="<%=StringEscapeUtils.escapeHtml("" + (p.getIcAddress2()))%>"
												type="text"></td>
										</tr>
										<tr>
											<td class="subHeaderVertical">City:</td>
											<td><input name="icCity"
												value="<%=StringEscapeUtils.escapeHtml("" + (p.getIcCity()))%>"
												type="text"></td>
										</tr>

										<tr>
											<td class="subHeaderVertical">State:</td>
											<td><itrust:state name="icState"
													value="<%=StringEscapeUtils.escapeHtml(p.getIcState())%>" />
											</td>
										</tr>
										<tr>
											<td class="subHeaderVertical">Zip:</td>
											<td><input name="icZip"
												value="<%=StringEscapeUtils.escapeHtml("" + (p.getIcZip()))%>"
												maxlength="10" type="text" size="10"></td>
										</tr>
										<tr>
											<td class="subHeaderVertical">Phone:</td>
											<td><input name="icPhone"
												value="<%=StringEscapeUtils.escapeHtml("" + (p.getIcPhone()))%>"
												type="text" size="12" maxlength="12"></td>
										</tr>
										<tr>
											<td class="subHeaderVertical">Insurance ID:</td>
											<td><input name="icID"
												value="<%=StringEscapeUtils.escapeHtml("" + (p.getIcID()))%>"
												type="text"></td>
										</tr>
									</table> <br>
									</div>
									</div>
								</td>
								

							</tr>
						
						
						<tr height="15px">
							<td colspan=3>&nbsp;</td>
						</tr>
						<tr>
							<td valign="top">
								<div class="row">
								<div class="col-md-6">
								<table class="fTable" align=center style="width: 350px;">
									<tr>
										<th colspan=2>Authentication Information</th>
									</tr>
									<tr>
										<td class="subHeaderVertical">Security Question:</td>
										<td><input name="question"
											value="<%=StringEscapeUtils.escapeHtml("" + (s.getQuestion()))%>"
											type="text"></td>
									</tr>
									<tr>
										<td class="subHeaderVertical">Security Answer:</td>
										<td><input name="answer"
											value="<%=StringEscapeUtils.escapeHtml("" + (s.getAnswer()))%>"
											type="password"></td>
									</tr>
									<tr>
										<td class="subHeaderVertical">Confirm Security Answer:</td>
										<td><input name="confirmAnswer"
											value="<%=StringEscapeUtils.escapeHtml("" + (s.getAnswer()))%>"
											type="password"></td>
									</tr>
									
								</table>
								</div>

								<div class="col-md-6">
								<table class="fTable" align=center style="width: 350px;">
									<tr>
										<th colspan=2>Emergency Contact</th>
									</tr>
									<tr>
										<td class="subHeaderVertical">Name:</td>
										<td><input name="emergencyName"
											value="<%=StringEscapeUtils.escapeHtml(""
						+ (p.getEmergencyName()))%>"
											type="text"></td>
									</tr>
									<tr>
										<td class="subHeaderVertical">Phone:</td>
										<td><input name="emergencyPhone"
											value="<%=StringEscapeUtils.escapeHtml(""
						+ (p.getEmergencyPhone()))%>"
											type="text" size="12" maxlength="12"></td>
									</tr>
								</table>
								</div>
							</td>
							</div>
						</tr>
						<tr>
							<td>
								<div align="center">
									<input type="submit" name="action"
										style="font-size: 16pt; font-weight: bold;"
										value="Edit Patient Record">
								</div>
								</form>
							</td>
						</tr>

					</table>
											</div>
				</form>
			</div>
		</td>
	</tr>
	<br />

	<%
		i++;
		}
	%>
</table>
<br />
<br />
Note: in order to set your password, use the "Reset Password" link at
the login page.
</div>
<%@include file="/footer.jsp"%>
