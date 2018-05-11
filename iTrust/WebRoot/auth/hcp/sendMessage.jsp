<%@page import="edu.ncsu.csc.itrust.action.ViewVisitedHCPsAction"%>
<%@page errorPage="/auth/exceptionHandler.jsp"%>

<%@page import="edu.ncsu.csc.itrust.action.SendMessageAction"%>
<%@page import="edu.ncsu.csc.itrust.beans.MessageBean"%>
<%@page import="edu.ncsu.csc.itrust.dao.mysql.PatientDAO"%>
<%@page import="edu.ncsu.csc.itrust.exception.ITrustException"%>
<%@page import="edu.ncsu.csc.itrust.exception.FormValidationException"%>
<%@page import="java.util.List"%>
<%@page import="java.util.ArrayList"%>

<%@page import="edu.ncsu.csc.itrust.beans.HCPVisitBean"%>
<%@page import="edu.ncsu.csc.itrust.action.ViewVisitedHCPsAction"%>
<%@page import="edu.ncsu.csc.itrust.dao.mysql.MessageDAO"%>

<%@include file="/global.jsp" %>

<%
	pageTitle = "iTrust - Send a Message";
%>

<%@include file="/header.jsp" %>

<%
	SendMessageAction action = new SendMessageAction(prodDAO, loggedInMID.longValue());
	PatientDAO patientDAO = prodDAO.getPatientDAO();
	ViewVisitedHCPsAction vHcpAction = null;
	long patientID = 0L;
	
	if (session.getAttribute("pid") != null) {
		String pidString = (String) session.getAttribute("pid");
		patientID = Long.parseLong(pidString);
		try {
	action.getPatientName(patientID);
	
		} catch (ITrustException ite) {
	patientID = 0L;
	session.removeAttribute("pid");
		}
		vHcpAction = new ViewVisitedHCPsAction(DAOFactory.getProductionInstance(), patientID);
	}
	else {
	
	}
	
	if (patientID == 0L) {
		response.sendRedirect("/iTrust/auth/getPatientID.jsp?forward=hcp/sendMessage.jsp");
	} else {	
		
		if (request.getParameter("messageBody") != null) {
	String body = request.getParameter("messageBody");
	MessageBean message = new MessageBean();
	message.setBody(request.getParameter("messageBody"));
	message.setSubject(request.getParameter("subject"));
	message.setRead(0);
	message.setFrom(loggedInMID);
	message.setTo(patientID);
	try {
		action.sendMessage(message);
		
		MessageDAO mDAO = DAOFactory.getProductionInstance().getMessageDAO();
		
		List<MessageBean> ms = mDAO.getMessagesFromTimeAscending(loggedInMID);
		
		long sentMsgId = ms.get(ms.size()-1).getMessageId();
		String ccList = "";
		
		String checkMids_S[] = request.getParameterValues("cc");
		if(checkMids_S != null && checkMids_S.length > 0) {
			for (String id : checkMids_S) {	
				MessageBean ccMessage = new MessageBean();
				ccMessage.setFrom(loggedInMID.longValue());
				ccMessage.setOriginalMessageId(sentMsgId);
				ccMessage.setTo(Long.parseLong(id));
				ccMessage.setBody(request.getParameter("messageBody"));
				ccMessage.setSubject(request.getParameter("subject"));
				ccMessage.setRead(0);
				action.sendMessage(ccMessage);
				ccList += id + ",";
			}
		}
		ccList = ccList.length() > 1?ccList.substring(0, ccList.length() - 1):ccList;
		
		loggingAction.logEvent(TransactionType.MESSAGE_SEND, message.getFrom(), message.getTo() , ccList);
		
		session.removeAttribute("pid");
		
		response.sendRedirect("/iTrust/auth/hcp-patient/messageOutbox.jsp");

	} catch (FormValidationException e){
%>
			<div align=center><span class="iTrustError"><%=StringEscapeUtils.escapeHtml(e.getMessage())%></span></div>
			<%	
			}
		}
		String subject = "";
%>

<div align="left">
	<h2>Send a Message</h2>
	<h4>to <%= StringEscapeUtils.escapeHtml("" + ( action.getPatientName(patientID) )) %> (<a href="/iTrust/auth/getPatientID.jsp?forward=hcp/sendMessage.jsp">someone else</a>):</h4>
	<!-- end cc checkbox -->
	<form id="mainForm" method="post" action="sendMessage.jsp">
		<% long ignoreMID = loggedInMID; %>
		<%@include file="/auth/hcp-patient/composeMessage.jsp" %>
	</form>
	<br />
	<br />
</div>
<%	} %>

<%@include file="/footer.jsp" %>
