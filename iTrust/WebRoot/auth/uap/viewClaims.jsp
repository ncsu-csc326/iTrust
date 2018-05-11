<%@page import="com.mysql.jdbc.StringUtils"%>
<%@taglib prefix="itrust" uri="/WEB-INF/tags.tld" %>
<%@page errorPage="/auth/exceptionHandler.jsp" %>

<%@page import="edu.ncsu.csc.itrust.action.ViewClaimsAction"%>
<%@page import="edu.ncsu.csc.itrust.model.old.beans.BillingBean"%>

<%@page import="java.util.List"%>

<%@include file="/global.jsp" %>

<%
pageTitle = "iTrust - View Insurance Claims";
%>

<%@include file="/header.jsp" %>

<%
	ViewClaimsAction action = new ViewClaimsAction(prodDAO);
	List<BillingBean> bList = action.getClaims();
	
	if(bList.size() == 0){
%>
No claims to display.
<%
	} else {
%>
<table class="fTable" align="center">
	<tr>
		<th>Claim Submitted on</th>
		<th>Submitted by</th>
	</tr>
<%
		for(BillingBean bb : bList){
			String sub = action.getSubmitter(bb);
			String date = action.getDate(bb);
%>
	<tr>
		<td>
		<a href="/iTrust/auth/uap/verifyClaim.jsp?claimID=<%= StringEscapeUtils.escapeHtml("" + bb.getBillID() ) %>">
		<%= StringEscapeUtils.escapeHtml(date) %>
		</a>
		</td>
		<td>
		<%= StringEscapeUtils.escapeHtml(sub) %>
		</td>
	</tr>
<%
		}
%>
</table>
<%
	}
%>
<%@include file="/footer.jsp" %>


