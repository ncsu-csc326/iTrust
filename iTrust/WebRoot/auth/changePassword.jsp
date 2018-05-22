<%@page import="edu.ncsu.csc.itrust.action.ChangePasswordAction"%>
<%@page import="edu.ncsu.csc.itrust.exception.FormValidationException"%>
<%@page import="edu.ncsu.csc.itrust.exception.ITrustException"%>

<%@include file="/global.jsp"%>

<%
	pageTitle = "iTrust - Change Password";
%>

<%@include file="/header.jsp"%>

<h1>Change Password</h1>

Your new password must be 5-20 alphanumeric characters and contain a letter and a number.
<br />
<br />
<form action="/iTrust/auth/changePassword.jsp" method="post" id="mainForm">
<table>
	<tr>
		<td>Old Password:</td>
		<td><input type=password maxlength=20 name="oldPass"></td>
	</tr>
	<tr>
		<td>New Password:</td>
		<td><input type=password maxlength=20 name="newPass"></td>
	</tr>
	<tr>
		<td>Confirm:</td>
		<td><input type=password maxlength=20 name="confirmPass"></td>
	</tr>
	<tr>
		<td colspan=2 align=center><input type="submit" value="Change Password"></td>
	</tr>

<%
	ChangePasswordAction action = new ChangePasswordAction(prodDAO);

	String oldPass = request.getParameter("oldPass");
	String newPass = request.getParameter("newPass");
	String confirmPass = request.getParameter("confirmPass");
	String returnMessage = "";
	
	if (oldPass != null && newPass != null && confirmPass != null) {
		try {
			returnMessage = action.changePassword(loggedInMID, oldPass, newPass, confirmPass);
			String color = "green";
			if (returnMessage.contains("Invalid password")) {
				color = "red";
			}
			%>
			<span style="color: <%=color%>"><b><i><%= returnMessage %></i></b></span><br />
			<%
		} catch (FormValidationException e) {
			e.printHTML(pageContext.getOut());
		}
	}
%>
</table>
</form>

<%@include file="/footer.jsp" %>

