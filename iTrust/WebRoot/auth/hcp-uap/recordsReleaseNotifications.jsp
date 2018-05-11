<%@taglib uri="/WEB-INF/tags.tld" prefix="itrust"%>
<%@page errorPage="/auth/exceptionHandler.jsp"%>

<%@page import="java.util.List"%>

<%@page import="edu.ncsu.csc.itrust.action.ViewRecordsReleaseAction"%>
<%@page import="edu.ncsu.csc.itrust.model.old.beans.RecordsReleaseBean"%>

<%@include file="/global.jsp" %>

<%
pageTitle = "iTrust - View Records Release Requests";
session.setAttribute("userRole",userRole);
loggingAction.logEvent(TransactionType.INBOX_VIEW, loggedInMID.longValue(), 0L, "");

ViewRecordsReleaseAction viewAction = new ViewRecordsReleaseAction(prodDAO, loggedInMID);
List<RecordsReleaseBean> allRequests = viewAction.getHospitalReleaseRequests();
List<RecordsReleaseBean> pendingRequests = viewAction.filterPendingRequests(allRequests);

int index = 0;
%>

<%@include file="/header.jsp" %>

<div align=center>
	<h2>Records Release Requests</h2>
	
	<table id="mailbox" class="display fTable">
	<thead>		
		<tr>
			<th align="center" width="200"><div align="center">Request Date</div></th>
			<th align="center" with="200"><div align="center">Requesting Patient</div></th>
			<th align="center" width="200"><div align="center">Release Hospital</div></th>
			<th width="100"></th>
		</tr>
	</thead>
	<tbody>
		<%
		session.setAttribute("recRequests", pendingRequests);
		for (RecordsReleaseBean rec : pendingRequests) {	
		%>
		<input type="hidden" value=<%=index%> name="index" id="index"></input>	
		<tr>
			<td align="center"><%=rec.getDateRequestedStr()%></td>
			<td align="center"><%=viewAction.getPatientName(rec.getPid())%></td>
			<td align="center"><%=viewAction.getHospitalName(rec.getReleaseHospitalID())%></td>		
			<td align="center"><a href="/iTrust/auth/hcp-uap/approveRecordsRelease.jsp?index=<%=index%>">View</a></td>
		</tr>
		<%
		index++;
		}
		%>			
	</tbody>
</table>
</div>

<%@include file="/footer.jsp" %>