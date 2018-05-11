<%@page import="edu.ncsu.csc.itrust.action.SendMessageAction"%>
<%@page import="edu.ncsu.csc.itrust.beans.MessageBean"%>
<%@page import="edu.ncsu.csc.itrust.beans.PatientBean"%>
<%@page import="edu.ncsu.csc.itrust.beans.PersonnelBean"%>
<%@page import="edu.ncsu.csc.itrust.exception.FormValidationException"%>
<%@page import="java.util.List"%>
<%@page import="java.util.ArrayList"%>

<%@page import="edu.ncsu.csc.itrust.beans.HCPVisitBean"%>
<%@page import="edu.ncsu.csc.itrust.action.ViewVisitedHCPsAction"%>
<%@page import="edu.ncsu.csc.itrust.dao.mysql.MessageDAO"%>

<%@taglib uri="/WEB-INF/tags.tld" prefix="itrust" %>
<%@page errorPage="/auth/exceptionHandler.jsp" %>

<%@include file="/global.jsp"%>

<%
	pageTitle = "iTrust - Send a Message";
%>

<%@include file="/header.jsp"%>


<%
	SendMessageAction action = new SendMessageAction(prodDAO, loggedInMID.longValue());
	
	
	int index;
	long dlhcpMID = -1;
	long representeeMID = -1;

	if (request.getParameter("sendMessage") != null && request.getParameter("sendMessage").equals("Send")) {
		try {
		MessageBean message = new MessageBean();
		message.setFrom(loggedInMID.longValue());
		message.setTo(((PersonnelBean)session.getAttribute("dlhcp")).getMID());
		message.setBody(request.getParameter("messageBody"));
		message.setSubject(request.getParameter("subject"));
		message.setRead(0);
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
		
		session.removeAttribute("dlhcp");
		
		loggingAction.logEvent(TransactionType.MESSAGE_SEND, message.getFrom(), message.getTo() , ccList);

		
		response.sendRedirect("/iTrust/auth/hcp-patient/messageOutbox.jsp");
		} catch (FormValidationException e){
			%>
			<div align=center><span class="iTrustError"><%=StringEscapeUtils.escapeHtml(e.getMessage())%></span></div>
			<%
		}
	}
	
	if ("Select".equals(request.getParameter("selectDLHCP"))) {
		if (request.getParameter("dlhcp") != null && !request.getParameter("dlhcp").equals("-1")) {
			dlhcpMID = Long.parseLong(request.getParameter("dlhcp"));
			
		}
	
	} else if ("Select".equals(request.getParameter("selectRep"))) {
		if (request.getParameter("representee") != null && !request.getParameter("representee").equals("-1")) {
			session.setAttribute("represent", true);
			representeeMID = Long.parseLong(request.getParameter("representee"));
			session.setAttribute("representeeMID", representeeMID);
		}
	}
	
	if(session.getAttribute("represent")==null){
		session.setAttribute("represent",false);
	}
	boolean represent = ((Boolean)session.getAttribute("represent"));
	
	if(represent){
		representeeMID = ((Long)session.getAttribute("representeeMID"));
	}
	
%>
<div align="left">
<form class="form-horizontal" role="form" id="mainForm" method="get" action="sendMessage.jsp">
	<h2>Send a Message</h2>
<% if (dlhcpMID == -1) { 
		
		List<PersonnelBean> dlhcps = null;

		if(represent){
			dlhcps = action.getDLHCPsFor(representeeMID);
		}else{
			dlhcps = action.getMyDLHCPs();
		}
		
		session.setAttribute("dlhcps", dlhcps);

		
		if(represent){%>
			<h4>To One of <%= StringEscapeUtils.escapeHtml("" + ( action.getPatientName(representeeMID))) %>'s DLHCPs</h4>
<%		}else{	%>
			<h4>To One of My DLHCPs</h4>
<%		}%>
		
<%		if (dlhcps.size() > 0) { %>
			<select name="dlhcp">
			<option value="-1"></option>
<%			index = 0; %>
<%			for(PersonnelBean dlhcp : dlhcps) { %>
				<option value="<%= dlhcp.getMID() %>"><%= StringEscapeUtils.escapeHtml("" + ( dlhcp.getFullName() )) %></option>
<%				index ++; %>
<%			} %>
			</select>
			<input type="submit" value="Select" name="selectDLHCP"/>
<%		} else { %>
			if(session.getAttribute("represent")){
				<i><%= StringEscapeUtils.escapeHtml("" + ( action.getPatientName(representeeMID) )) %> has not declared any HCPs.</i>
			}else{
				<i>You haven't declared any HCPs.</i>
			}
<%		}

		if(!represent){			
			List<PatientBean> representees = action.getMyRepresentees();
			%>
			<h4>On Behalf of One of My Representees</h4>
	<%		if (representees.size() > 0) { %>
				<select name="representee">
				<option value="-1"></option>
	<%			index = 0; %>
	<%			for(PatientBean representee : representees) { %>
					<option value="<%= representee.getMID() %>"><%= StringEscapeUtils.escapeHtml("" + ( representee.getFullName())) %></option>
	<%				index ++; %>
	<%			} %>
				</select>
				<input type="submit" value="Select" name="selectRep"/>
	<%		} else { %>
				<i>No other patients have declared you as a representative.</i>
	<%		} 
		}%>
<%	} else if (dlhcpMID >= 0) { %>
<%
	
		PersonnelBean dlhcp=action.getDLHCPByMID(dlhcpMID);
		session.setAttribute("represent", null);
		session.setAttribute("representeeMID", null);
		session.setAttribute("dlhcp", dlhcp);
		ViewVisitedHCPsAction vHcpAction=null;
		if(represent){	
			vHcpAction = new ViewVisitedHCPsAction(DAOFactory.getProductionInstance(),representeeMID);
%>			<h4>To <%= StringEscapeUtils.escapeHtml("" + ( dlhcp.getFullName() )) %> on Behalf of <%= StringEscapeUtils.escapeHtml("" + ( action.getPatientName(representeeMID) )) %></h4>
<%		}else{	
			vHcpAction = new ViewVisitedHCPsAction(DAOFactory.getProductionInstance(),loggedInMID.longValue());
%>			<h4>To <%= StringEscapeUtils.escapeHtml("" + ( dlhcp.getFullName() )) %></h4>
<%		}
		long ignoreMID = dlhcp.getMID();
		String subject= "";
%>

		<%@include file="/auth/hcp-patient/composeMessage.jsp" %>
		
<%	} %>

</form>
</div>

<%@include file="/footer.jsp"%>
