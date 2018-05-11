<%@page errorPage="/auth/exceptionHandler.jsp"%>

<%@page import="java.util.List"%>
<%@page import="java.util.Date"%>
<%@page import="java.text.DateFormat"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.sql.Timestamp"%>
<%@page import="edu.ncsu.csc.itrust.action.EditApptTypeAction"%>
<%@page import="edu.ncsu.csc.itrust.action.ViewMyBillingAction"%>
<%@page import="edu.ncsu.csc.itrust.beans.BillingBean"%>
<%@page import="edu.ncsu.csc.itrust.dao.mysql.BillingDAO"%>
<%@page import="edu.ncsu.csc.itrust.dao.mysql.ApptDAO"%>
<%@page import="edu.ncsu.csc.itrust.dao.mysql.PersonnelDAO"%>
<%@page import="edu.ncsu.csc.itrust.dao.mysql.OfficeVisitDAO"%>
<%@page import="edu.ncsu.csc.itrust.dao.mysql.ApptTypeDAO"%>
<%@page import="edu.ncsu.csc.itrust.beans.OfficeVisitBean"%>
<%@page import="edu.ncsu.csc.itrust.beans.ApptTypeBean"%>
<%@page import="edu.ncsu.csc.itrust.dao.DAOFactory"%>

<%@include file="/global.jsp" %>

<%
pageTitle = "iTrust - View My Bills";
%>

<%@include file="/header.jsp" %>

<div align=center>
	<h2>My Bills</h2>
<%
	loggingAction.logEvent(TransactionType.PATIENT_BILLS_VIEW_ALL, loggedInMID.longValue(), loggedInMID.longValue(), "");
	BillingDAO billingDAO = prodDAO.getBillingDAO();
	ApptTypeDAO aTypeDAO = prodDAO.getApptTypeDAO();
	PersonnelDAO personnelDAO = prodDAO.getPersonnelDAO();
	OfficeVisitDAO visitDAO = prodDAO.getOfficeVisitDAO();
	ViewMyBillingAction action = new ViewMyBillingAction(prodDAO, loggedInMID.longValue());
	List<BillingBean> bills = action.getAllMyBills();
	session.setAttribute("bills", bills);
	if (bills.size() > 0) {
%>	
	<div style="margin-left: 5px;">
	<table class="fTable" border=1 align="center">
		<tr>
			<th>Date of Visit</th>
			<th>Appointment Type</th>
			<th>Provider</th>
			<th>Status</th>
			<th>Price</th>
		</tr>
<%		 

		int index = 0;
		for(BillingBean b : bills) { 
			OfficeVisitBean ovb = visitDAO.getOfficeVisit(b.getApptID());
			String pbean = personnelDAO.getName(b.getHcp());
					
			String row = "<tr";
%>

			<%=row+""+((index%2 == 1)?" class=\"alt\"":"")+">"%>
				<td>
				 <a href="/iTrust/auth/patient/payBill.jsp?billID=<%= StringEscapeUtils.escapeHtml("" + (b.getBillID())) %>"><%= StringEscapeUtils.escapeHtml("" + ( ovb.getVisitDateStr() )) %></a></td>
				<td><%= StringEscapeUtils.escapeHtml("" + ( ovb.getAppointmentType() )) %></td>
				<td><%= StringEscapeUtils.escapeHtml("" + ( pbean )) %></td>
				<td><%= StringEscapeUtils.escapeHtml("" + ( b.getStatus() )) %></td>
				<td><%= StringEscapeUtils.escapeHtml("$" + ( b.getAmt() )) %></td>
				
				
			</tr>
	<%		index ++; %>
	<%	} %>
	</table>
	
	
	</div>
<%	} else { %>
	<div>
		<i>You have no Bills</i>
	</div>
<%	} %>	
	<br />
</div>

<%@include file="/footer.jsp" %>
