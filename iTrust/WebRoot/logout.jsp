<%@include file="/global.jsp" %>
<%@page import="edu.ncsu.csc.itrust.model.old.enums.TransactionType"%>

<%
pageTitle = "iTrust Logout";

session.invalidate();
validSession = false;
if(loggedInMID != null) {
	authDAO.logUserLoggedOut(loggedInMID);
}
response.sendRedirect("/iTrust");
%>
