<%@page import="com.mysql.jdbc.StringUtils"%>
<%@taglib prefix="itrust" uri="/WEB-INF/tags.tld" %>
<%@page errorPage="/auth/exceptionHandler.jsp" %>

<%@page import="edu.ncsu.csc.itrust.model.old.beans.BillingBean"%>
<%@page import="edu.ncsu.csc.itrust.action.VerifyClaimAction"%>

<%@page import="java.util.List"%>
<%@page import="java.text.SimpleDateFormat" %>

<%@include file="/global.jsp" %>

<%
pageTitle = "iTrust - Verify Insurance Claim";
%>

<%@include file="/header.jsp" %>

<%
	long bID = 0;
	try{ 
		bID = Long.parseLong(request.getParameter("claimID"));
	} catch (NumberFormatException e){
		response.sendRedirect("viewClaims.jsp");
		return;
	}
	
	VerifyClaimAction action = new VerifyClaimAction(prodDAO, bID);
	BillingBean myBill = action.getBill();
	
	String doWhat = request.getParameter("action");
	if(doWhat != null && !doWhat.equals("null")){
		if(doWhat.equals("Approve")){
			if(myBill.getSubmissions() == 1)
				loggingAction.logEvent(TransactionType.UAP_INITIAL_APPROVAL, loggedInMID.longValue(), loggedInMID.longValue(), "");
			else if(myBill.getSubmissions() == 2)
				loggingAction.logEvent(TransactionType.UAP_SECOND_APPROVAL, loggedInMID.longValue(), loggedInMID.longValue(), "");
			action.approveClaim();
		} else if(doWhat.equals("Deny")) {
			if(myBill.getSubmissions() == 1)
				loggingAction.logEvent(TransactionType.UAP_INITIAL_DENIAL, loggedInMID.longValue(), loggedInMID.longValue(), "");
			else if(myBill.getSubmissions() == 2)
				loggingAction.logEvent(TransactionType.UAP_SECOND_DENIAL, loggedInMID.longValue(), loggedInMID.longValue(), "");
			action.denyClaim();
		}
		response.sendRedirect("verifyClaim.jsp?claimID="+ bID);
		return;
	}
%>

<form>
<table class="fTable" align="center">
	<tr>
	<th colspan="2">Office Visit Information</th>
	</tr>
	<tr>
		<td>Bill Sent on:</td>
		<td><%= StringEscapeUtils.escapeHtml(new SimpleDateFormat("MM/dd/YYYY").format(myBill.getBillTime())) %></td>
	</tr>
	<tr>
		<td>Claim Submitted on:</td>
		<td><%= StringEscapeUtils.escapeHtml( new SimpleDateFormat("MM/dd/YYYY").format(myBill.getSubDate()) ) %></td>
	</tr>
	<tr>
	<th colspan="2">Insurance Information</th>
	</tr>
	<tr>
		<td>Insurance Policy ID:</td>
		<td><%= StringEscapeUtils.escapeHtml("" + myBill.getInsID()) %></td>
	</tr>
	<tr>
		<td>Insurance Holder Name:</td>
		<td><%= StringEscapeUtils.escapeHtml("" + myBill.getInsHolderName()) %></td>
	</tr>
	<tr>
		<td>Insurance Provider:</td>
		<td><%= StringEscapeUtils.escapeHtml("" + myBill.getInsProviderName()) %></td>
	</tr>
	<tr>
		<td>Insurance Address 1:</td>
		<td><%= StringEscapeUtils.escapeHtml("" + myBill.getInsAddress1()) %></td>
	</tr>
	<tr>
		<td>Insurance Address 2:</td>
		<td><%= StringEscapeUtils.escapeHtml("" + myBill.getInsAddress2()) %></td>
	</tr>
	<tr>
		<td>Insurance City:</td>
		<td><%= StringEscapeUtils.escapeHtml("" + myBill.getInsCity()) %></td>
	</tr>
	<tr>
		<td>Insurance State:</td>
		<td><%= StringEscapeUtils.escapeHtml("" + myBill.getInsState()) %></td>
	</tr>
	<tr>
		<td>Insurance Zip Code:</td>
		<td><%= StringEscapeUtils.escapeHtml("" + myBill.getInsZip()) %></td>
	</tr>
	<tr>
		<td>Insurance Phone:</td>
		<td><%= StringEscapeUtils.escapeHtml("" + myBill.getInsPhone()) %></td>
	</tr>
	<%if(!myBill.getStatus().equals(BillingBean.APPROVED) &&
			!myBill.getStatus().equals(BillingBean.DENIED)){ %>
	<tr>
		<td><input type="text" name="claimID" value=<%=StringEscapeUtils.escapeHtml("" + ( bID )) %> style="display:none;" />
			<input type="submit" name="action" value="Approve">
		</td>
		<td><input type="submit" name="action" value="Deny"></td>
	</tr>
	<%} else if(myBill.getStatus().equals(BillingBean.APPROVED)){ %>
	<tr>
		<td colspan="2">Claim has been approved.</td>
	</tr>
	<%} else if(myBill.getStatus().equals(BillingBean.DENIED)){ %>
	<tr>
		<td colspan="2">Claim has been denied.</td>
	</tr>
	<%} %>
</table>



</form>
<%@include file="/footer.jsp" %>