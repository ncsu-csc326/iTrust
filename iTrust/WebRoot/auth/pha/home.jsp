<%@page errorPage="/auth/exceptionHandler.jsp" %>

<%@page import="java.util.List"%>

<%@include file="/global.jsp" %>

<%
pageTitle = "iTrust - PHA Home";
loggingAction.logEvent(TransactionType.HOME_VIEW, loggedInMID, 0, "PHA Home");
%>

<%@include file="/header.jsp" %>

<div style="text-align: center;">
	<h2>Welcome PHA <%= StringEscapeUtils.escapeHtml("" + (userName )) %>!</h2>
</div>

<%@include file="/footer.jsp" %>
