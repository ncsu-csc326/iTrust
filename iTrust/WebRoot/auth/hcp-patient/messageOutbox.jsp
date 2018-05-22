<%@page errorPage="/auth/exceptionHandler.jsp"%>

<%@page import="java.util.List"%>

<%@page import="edu.ncsu.csc.itrust.action.ViewMyMessagesAction"%>
<%@page import="edu.ncsu.csc.itrust.model.old.beans.MessageBean"%>
<%@page import="edu.ncsu.csc.itrust.model.old.dao.DAOFactory"%>

<%@include file="/global.jsp" %>

<%
pageTitle = "iTrust - View My Sent Messages";
session.setAttribute("outbox",true);
session.setAttribute("isHCP",userRole.equals("hcp"));

%>

<%@include file="/header.jsp" %>

<div align=center>
	<h2>My Sent Messages</h2>
	<% if(userRole.equals("hcp")){%>
		<a href="/iTrust/auth/hcp/sendMessage.jsp">Compose a Message</a>
	<% }else{%>
		<a href="/iTrust/auth/patient/sendMessage.jsp">Compose a Message</a>
	<% } %>
	<br /><br />
	<%@include file="/auth/hcp-patient/mailbox.jsp" %>
</div>

<%@include file="/footer.jsp" %>
