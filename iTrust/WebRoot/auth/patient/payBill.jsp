
<%@page errorPage="/auth/exceptionHandler.jsp"%>

<%@page import="edu.ncsu.csc.itrust.action.PayBillAction"%>
<%@page import="edu.ncsu.csc.itrust.beans.BillingBean"%>
<%@page import="edu.ncsu.csc.itrust.beans.OfficeVisitBean"%>
<%@page import="edu.ncsu.csc.itrust.beans.HospitalBean"%>
<%@page import="edu.ncsu.csc.itrust.dao.mysql.HospitalsDAO"%>

<%@page import="org.apache.commons.lang.StringUtils"%>
<%@page import="java.text.SimpleDateFormat"%>

<%@include file="/global.jsp" %>

<%
pageTitle = "iTrust - Pay Bill";
%>
<%@include file="/header.jsp" %>

<script type="text/javascript">
$(document).ready(function(){
	$("#ccInfo").hide();
	$("#insInfo").hide();
	var tableWidth = Math.max(parseInt($("#genInfo").css("width"), 10), parseInt($("#ccInfo").css("width"), 10));
	tableWidth = Math.max(parseInt($("#insInfo").css("width"), 10), tableWidth);
	$("#genInfo").css("width", tableWidth + "px");
	$("#ccTable").css("width", tableWidth + "px");
	$("#insTable").css("width", tableWidth + "px");
	$("#typeTable").css("width", tableWidth + "px");
	$("#optTable").css("width", tableWidth + "px");
	$("#CC").click(function(){
		$("#ccInfo").slideDown(750);
		$("#insInfo").slideUp(750);
	});
	$("#Ins").click(function(){
		$("#ccInfo").slideUp(750);
		$("#insInfo").slideDown(750);
	});
});
</script>
<%
	HospitalsDAO hospital = prodDAO.getHospitalsDAO();
	long bID = 0;
	try{ 
		bID = Long.parseLong(request.getParameter("billID"));
	} catch (NumberFormatException e){
		response.sendRedirect("myBills.jsp");
		return;
	}
	PayBillAction action = new PayBillAction(prodDAO, bID);
	BillingBean myBill = action.getBill();
	OfficeVisitBean ovBean = action.getOVBean();
	
	String type = request.getParameter("type");
	
	String error = null;
	String ccNum = request.getParameter("ccNumber");
	String ccHolder = request.getParameter("ccHolder");
	String ccType = request.getParameter("ccType");
	String billAddress = request.getParameter("billAddress");
	String cVV = request.getParameter("cvv");
	String insHolder = request.getParameter("insHolder");
	String insProvider = request.getParameter("insProvider");
	String insID = request.getParameter("insID");
	String insAdd1 = request.getParameter("insAdd1");
	String insAdd2 = request.getParameter("insAdd2");
	String insCity = request.getParameter("insCity");
	String insState = request.getParameter("insState");
	String insZip = request.getParameter("insZip");
	String insPhone = request.getParameter("insPhone");
	
	if(type != null && !type.equals("null")){
		if( type.equals("CC") ){
			error = action.payBillWithCC(ccNum, ccHolder, ccType, billAddress, cVV);
			if(error == null){
				loggingAction.logEvent(TransactionType.PATIENT_PAYS_BILL, loggedInMID.longValue(), loggedInMID.longValue(), "");
				response.sendRedirect("/iTrust/auth/patient/payBill.jsp?billID=" + bID);
			}
		} else if( type.equals("Ins")){
			error = action.payBillWithIns(insHolder, insProvider, insID, insAdd1, insAdd2, insCity, insState, insZip, insPhone);
			if(error == null){
				if(myBill.getSubmissions() == 1)
					loggingAction.logEvent(TransactionType.PATIENT_SUBMITS_INSURANCE, loggedInMID.longValue(), loggedInMID.longValue(), "");
				else if(myBill.getSubmissions() == 2)
					loggingAction.logEvent(TransactionType.PATIENT_RESUBMITS_INSURANCE, loggedInMID.longValue(), loggedInMID.longValue(), "");
				response.sendRedirect("/iTrust/auth/patient/payBill.jsp?billID=" + bID);
			}
		}
	}
	
	if( error != null ){
%>
<span class="iTrustError"><%= StringEscapeUtils.escapeHtml(error) %></span>		
<%
	}
%>
<form>
<table class="fTable" align="center" id="genInfo">
	<tr>
	<% if(myBill.getStatus().equals(BillingBean.UNSUBMITTED)||
			myBill.getStatus().equals(BillingBean.DENIED)){ %>
	<th colspan="2">Bill Information</th>
	<% } else { %>
	<th colspan="2">Payment Information</th>
	<% } %>
	</tr>
	<tr>
		<td>Submitted on:</td>
		<td><%= StringEscapeUtils.escapeHtml(new SimpleDateFormat("MM/dd/YYYY").format(myBill.getBillTime())) %></td>
	</tr>
	<tr>
		<td>Bill Amount:</td>
		<td><%= StringEscapeUtils.escapeHtml("$" + myBill.getAmt()) %></td>
	</tr>
	<tr>
		<td>Status:</td>
		<td><%= StringEscapeUtils.escapeHtml( myBill.getStatus() ) %></td>
	</tr>
	<tr>
		<td>Patient Name:</td>
		<td><%= StringEscapeUtils.escapeHtml( action.getPatient() ) %></td>
	</tr>
	<tr>
		<td>Office Visit Date:</td>
		<td><%= StringEscapeUtils.escapeHtml( ovBean.getVisitDateStr() ) %></td>
	</tr>
	<%
	HospitalBean hop = hospital.getHospital(ovBean.getHospitalID());
	String hospitalName = hop == null ? "Not Specified" : hop.getHospitalName();
	%>
	<tr>
		<td>Hospital Name:</td>
		<td><%= StringEscapeUtils.escapeHtml( hospitalName ) %></td>
	</tr>
	<tr>
		<td>Office Visit Type:</td>
		<td><%= StringEscapeUtils.escapeHtml( ovBean.getAppointmentType() ) %></td>
	</tr>
	<tr>
		<td>Notes:</td>
		<td><%= StringEscapeUtils.escapeHtml( ovBean.getNotes() ) %></td>
	</tr>
</table>
	<% if(myBill.getStatus().equals(BillingBean.UNSUBMITTED) ||
			myBill.getStatus().equals(BillingBean.DENIED)){ %>
<table id="typeTable" class="fTable" align="center">
	<tr>
	<th colspan="2">Payment Type</th>
	</tr>
	<tr>
		<td>Payment Method:</td>
		<td>
			<input type="radio" name="type" value="CC" id="CC"> Credit Card <br />
	<%if(myBill.getSubmissions() < BillingBean.MAX_SUBMISSIONS){ %>
			<input type="radio" name="type" value="Ins" id="Ins"> Insurance
	<%} %>
		</td>
	</tr>
</table>
<div id="ccInfo" style="margin-left:auto; margin-right:auto;">
<table id="ccTable" class="fTable" align="center">
	<tr>
		<th colspan="2">Credit Card Payment Information</th>
	<tr>
		<td>Credit Card Number:</td>
		<td><input name="ccNumber" /></td>
	</tr>
	<tr>
		<td>CVV:</td>
		<td><input name="cvv" /></td>
	</tr>
	<tr>
		<td>Credit Card Type:</td>
		<td>
		<select name="ccType">
			<option value="Visa">Visa</option>
			<option value="MasterCard">MasterCard</option>
			<option value="AmericanExpress">American Express</option>
			<option value="Discover">Discover</option>
		</select>
		</td>
	<tr>
		<td>Credit Card Holder:</td>
		<td><input name="ccHolder" /></td>
	</tr>
	<tr>
		<td>Billing Address:</td>
		<td><input name="billAddress" /></td>
	</tr>
</table>
</div>
	<%if(myBill.getSubmissions() < BillingBean.MAX_SUBMISSIONS){ %>
<div  id="insInfo" style="margin-left: auto; margin-right:auto;">
<table id="insTable" class="fTable" align="center">
	<tr>
		<th colspan="2">Insurance Information</th>
	<tr>
		<td>Insurance Policy ID:</td>
		<td><input name="insID" /></td>
	</tr>
	<tr>
		<td>Insurance Holder Name:</td>
		<td><input name="insHolder" /></td>
	</tr>
	<tr>
		<td>Insurance Provider:</td>
		<td><input name="insProvider" /></td>
	</tr>
	<tr>
		<td>Insurance Address 1:</td>
		<td><input name="insAdd1" /></td>
	</tr>
	<tr>
		<td>Insurance Address 2:</td>
		<td><input name="insAdd2" /></td>
	</tr>
	<tr>
		<td>Insurance City:</td>
		<td><input name="insCity" /></td>
	</tr>
	<tr>
		<td>Insurance State:</td>
		<td><input name="insState" /></td>
	</tr>
	<tr>
		<td>Insurance Zip Code:</td>
		<td><input name="insZip" /></td>
	</tr>
	<tr>
		<td>Insurance Phone:</td>
		<td><input name="insPhone" /></td>
	</tr>
</table>
</div>
	<%} %>
<table id="optTable" class="fTable" align="center">
	<tr>
		<td><input type="submit"><input type="text" id="billID" name="billID" value=<%=StringEscapeUtils.escapeHtml("" + ( bID )) %> style="display:none;" /></td>
		<td><button onclick="javascript:$('#billID').val('back');">Cancel</button></td>
	</tr>
	<%} %>
</table>



</form>

<%@include file="/footer.jsp" %>
