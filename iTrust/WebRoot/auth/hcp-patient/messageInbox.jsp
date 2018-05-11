<%@page errorPage="/auth/exceptionHandler.jsp"%>

<%@page import="java.util.List"%>

<%@page import="edu.ncsu.csc.itrust.action.ViewMyMessagesAction"%>
<%@page import="edu.ncsu.csc.itrust.model.old.beans.MessageBean"%>
<%@page import="edu.ncsu.csc.itrust.model.old.dao.DAOFactory"%>
<%@page import="edu.ncsu.csc.itrust.model.old.dao.mysql.PatientDAO"%>

<%@include file="/global.jsp" %>

<%
pageTitle = "iTrust - View My Message ";
session.setAttribute("outbox",false);
session.setAttribute("isHCP",userRole.equals("hcp"));
%>

<%@include file="/header.jsp" %>

<div align=center>
	<h2>My Messages</h2>
	<%@include file="/auth/hcp-patient/mailbox.jsp" %>

</div>

<%@include file="/footer.jsp" %>
