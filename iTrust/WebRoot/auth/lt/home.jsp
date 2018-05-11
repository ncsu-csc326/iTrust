<%@include file="/global.jsp" %>

<%
pageTitle = "iTrust - Lab Tech Home";
loggingAction.logEvent(TransactionType.HOME_VIEW, loggedInMID, 0, "LT Home");
%>

<%@include file="/header.jsp" %>

<div style="text-align: center; height: 300px;">
	<h2>Welcome <%= StringEscapeUtils.escapeHtml("" + (userName )) %>!</h2>
</div>

<%@include file="/footer.jsp" %>
