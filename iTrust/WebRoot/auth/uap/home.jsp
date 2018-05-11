<%@include file="/global.jsp" %>

<%
pageTitle = "iTrust - UAP Home";
loggingAction.logEvent(TransactionType.HOME_VIEW, loggedInMID, 0, "UAP Home");
%>

<%@include file="/header.jsp" %>

<%@include file="/auth/uap/notificationArea.jsp" %>

<div style="text-align: center; height: 300px;">
	<h2>Welcome <%= StringEscapeUtils.escapeHtml("" + (userName )) %>!</h2>
</div>



<%@include file="/footer.jsp" %>
