<%@page errorPage="/auth/exceptionHandler.jsp"%>

<%@page import="edu.ncsu.csc.itrust.action.SendMessageAction"%>
<%@page import="edu.ncsu.csc.itrust.model.old.beans.MessageBean"%>
<%@page import="edu.ncsu.csc.itrust.model.old.dao.mysql.MessageDAO"%>
<%@page import="java.util.List"%>

<%@include file="/global.jsp" %>

<%
pageTitle = "iTrust - Reply";
%>

<%@include file="/header.jsp" %>

<%

	SendMessageAction action = new SendMessageAction(prodDAO, loggedInMID);
	MessageBean original = null;
	
	if (session.getAttribute("message") != null) {
		original = (MessageBean)session.getAttribute("message");
		session.setAttribute("original", original);
		session.removeAttribute("message");
	} else if (request.getParameter("messageBody") != null) {
		if (session.getAttribute("original") != null) {
			original = (MessageBean)session.getAttribute("original");
			MessageBean messageNew = new MessageBean();
			messageNew.setBody(request.getParameter("messageBody"));
			messageNew.setFrom(loggedInMID);
			messageNew.setTo(original.getFrom());
			messageNew.setSubject(request.getParameter("subject"));
			messageNew.setRead(0);
			messageNew.setParentMessageId(original.getMessageId());
			action.sendMessage(messageNew);
			
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
			
			loggingAction.logEvent(TransactionType.MESSAGE_SEND, messageNew.getFrom(), messageNew.getTo() , ccList);
			
			response.sendRedirect("messageInbox.jsp");

		} 
	} else {
		response.sendRedirect("messageInbox.jsp");
	}
	String subject="RE: "+original.getSubject();

	long ignoreMID = -1;
	String name = "";
	if(userRole.equals("hcp")){
		ignoreMID = loggedInMID;
		name = action.getPatientName(original.getFrom());
	}else{
		ignoreMID = original.getFrom();
		name = action.getPersonnelName(original.getFrom());
	}
%>

	<h2>Reply</h2>
	<h4>to a message from <%= StringEscapeUtils.escapeHtml(name) %>:</h4>
	<form id="mainForm" method="post" action="reply.jsp">
		<%@include file="/auth/hcp-patient/composeMessage.jsp" %>
	</form>
	<br />
	<br />


<%@include file="/footer.jsp" %>
