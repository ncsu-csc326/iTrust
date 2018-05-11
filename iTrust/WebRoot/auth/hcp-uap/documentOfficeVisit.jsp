<%@taglib uri="/WEB-INF/tags.tld" prefix="itrust"%>
<%@page errorPage="/auth/exceptionHandler.jsp"%>

<%@page import="java.util.List"%>
<%@page import="edu.ncsu.csc.itrust.action.AddOfficeVisitAction"%>
<%@page import="edu.ncsu.csc.itrust.beans.OfficeVisitBean"%>

<%@include file="/global.jsp"%>

<%
	String visitName = "Office Visit";
	if (userRole.equals("er")) {
		visitName = "ER Visit";
	}
	pageTitle = "iTrust - Document " + visitName;
%>

<%@include file="/header.jsp"%>
<itrust:patientNav thisTitle="Document Office Visit" />
<%
	/* Require a Patient ID first */
	String pidString = (String) session.getAttribute("pid");
	if (pidString == null || 1 > pidString.length()) {
		response.sendRedirect("/iTrust/auth/getPatientID.jsp?forward=/iTrust/auth/hcp-uap/documentOfficeVisit.jsp");
		return;
	}

	AddOfficeVisitAction action = new AddOfficeVisitAction(prodDAO,
			pidString);
	long pid = action.getPid();
	List<OfficeVisitBean> visits = action.getAllOfficeVisits();
	if ("true".equals(request.getParameter("formIsFilled"))) {
		response.sendRedirect("/iTrust/auth/hcp-uap/editOfficeVisit.jsp");
		return;
	}
%>

<div align=center>
	<form action="documentOfficeVisit.jsp" method="post" id="formMain">

		<input type="hidden" name="formIsFilled" value="true" /> <br /> <br />
		Are you sure you want to document a <em>new</em> visit for <b><%=StringEscapeUtils.escapeHtml("" + (action.getUserName()))%></b>?<br />
		<br /> <input style="font-size: 150%; font-weight: bold;" type=submit
			value="Yes, Document <%=visitName%>">
	</form>
	<br /> Click on an old office visit to modify:<br />
	<%
		for (OfficeVisitBean ov : visits) {
	%>
	<%
		if (ov.isERIncident()) {
	%>
	<a
		href="/iTrust/auth/hcp-uap/editOfficeVisit.jsp?ovID=<%=StringEscapeUtils.escapeHtml("" + (ov.getID()))%>"><%=StringEscapeUtils.escapeHtml("ER: "
							+ (ov.getVisitDateStr()))%></a><br />
	<%
		} else {
				if (!userRole.equals("er")) {
	%>
	<a
		href="/iTrust/auth/hcp-uap/editOfficeVisit.jsp?ovID=<%=StringEscapeUtils.escapeHtml(""
								+ (ov.getID()))%>"><%=StringEscapeUtils.escapeHtml(""
								+ (ov.getVisitDateStr()))%></a><br />
	<%
				}
			}
	%>
	<%
		}
	%>

	<br /> <br /> <br />
</div>
<%@include file="/footer.jsp"%>
