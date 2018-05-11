<%@include file="/global.jsp" %>
<%@page import="edu.ncsu.csc.itrust.enums.TransactionType"%>

<%
pageTitle = "iTrust Logout";

session.invalidate();
validSession = false;
if(loggedInMID != null) {
	loggingAction.logEvent(TransactionType.LOGOUT, loggedInMID, loggedInMID, "");
}
response.sendRedirect("/iTrust");
%>
