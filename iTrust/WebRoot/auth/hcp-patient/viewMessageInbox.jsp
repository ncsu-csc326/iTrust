<%@page errorPage="/auth/exceptionHandler.jsp"%>

<%@page import="edu.ncsu.csc.itrust.action.ViewMyMessagesAction"%>
<%@page import="edu.ncsu.csc.itrust.model.old.beans.MessageBean"%>
<%@page import="java.util.List"%>

<%@include file="/global.jsp" %>

<%
pageTitle = "iTrust - View Message";
%>

<%@include file="/header.jsp" %>

<%
	
	ViewMyMessagesAction action = new ViewMyMessagesAction(prodDAO, loggedInMID.longValue());
	MessageBean original = null;
	
	if (request.getParameter("msg") != null) {
		String msgParameter = request.getParameter("msg");
		int msgIndex = 0;
		try {
			msgIndex = Integer.parseInt(msgParameter);
		} catch (NumberFormatException nfe) {
			response.sendRedirect("messageInbox.jsp");
		}
		List<MessageBean> messages = null; 
		if (session.getAttribute("messages") != null) {
			messages = (List<MessageBean>) session.getAttribute("messages");
			if(msgIndex >= messages.size() || msgIndex < 0) {
				msgIndex = 0;
				response.sendRedirect("oops.jsp");
			}
		} else {
			response.sendRedirect("messageInbox.jsp");
		}
		original = (MessageBean)messages.get(msgIndex);
		action.setRead(original);
		session.setAttribute("message", original);
	}
	else {
		response.sendRedirect("messageInbox.jsp");
	}
	
%>
	<div>
		<table width="99%">
			<tr>
				<td><b>From:</b> <%= StringEscapeUtils.escapeHtml("" + ( action.getName(original.getFrom()) )) %></td>
			</tr>
			<tr>
				<td><b>Subject:</b> <%= StringEscapeUtils.escapeHtml("" + ( original.getSubject() )) %></td>
			</tr>
			<tr>
				<td><b>Date &amp; Time:</b> <%= StringEscapeUtils.escapeHtml("" + ( original.getSentDate() )) %></td>
			</tr>
		</table>
	</div>
	
	<table>
		<tr>
			<td colspan="2"><a href="/iTrust/auth/hcp-patient/reply.jsp" style="font-size: 18px;">Reply</a></td>
		</tr>
		<tr>
			<td colspan="2"><b>Message:</b></td>
		</tr>
		<tr>
			<td colspan="2"><%= StringEscapeUtils.escapeHtml("" + ( original.getBody() )).replace("\n","<br/>") %></td>
		</tr>
	</table>


<%@include file="/footer.jsp" %>
