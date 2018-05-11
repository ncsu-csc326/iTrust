<%@taglib uri="/WEB-INF/tags.tld" prefix="itrust"%>
<%@page errorPage="/auth/exceptionHandler.jsp"%>

<%@page import="java.util.List"%>
<%@page import="edu.ncsu.csc.itrust.action.ViewRecordsReleaseAction"%>
<%@page import="edu.ncsu.csc.itrust.beans.RecordsReleaseBean"%>

<%@include file="/global.jsp" %>

<%
pageTitle = "iTrust - Records Release Requests";

ViewRecordsReleaseAction action = new ViewRecordsReleaseAction(prodDAO, loggedInMID);
List<RecordsReleaseBean> releaseHistory = action.getHospitalReleaseRequests();
%>

<%@include file="/header.jsp" %>

<br />

<div align=center>
	<table id="requestHistory" class="fTable">
	<thead>	
		<tr>
		<th colspan="4" style="text-align: center;">Requested Records Release History</th>
		</tr>	
		<tr>
			<th align="center" width="200"><div align="center">Request Date</div></th>
			<th align="center" with="200"><div align="center">Requesting Patient</div></th>
			<th align="center" width="200"><div align="center">Request Status</div></th>
			<th width="100"><div align="center">View</div></th>
		</tr>
	</thead>
	<tbody>
		<%
		session.setAttribute("recRequests", releaseHistory);
		int index = 0;
		for (RecordsReleaseBean rec : releaseHistory) {	
		%>
		<tr>
			<td align="center"><%=rec.getDateRequestedStr()%></td>
			<td align="center"><%=authDAO.getUserName(rec.getPid())%></td>
			<td align="center"><%=rec.getStatusStr()%></td>	
			<td align="center"><a href="/iTrust/auth/hcp-uap/approveRecordsRelease.jsp?index=<%=index%>">View</a></td>
		</tr>
		<%
		index++;
		}
		%>	
		<th colspan="4"></th>		
	</tbody>
</table>
</div>

<%@include file="/footer.jsp"%>

