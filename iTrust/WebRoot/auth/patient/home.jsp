<%@page import="edu.ncsu.csc.itrust.beans.OfficeVisitBean"%>
<%@page import="edu.ncsu.csc.itrust.beans.LabProcedureBean"%>
<%@page import="edu.ncsu.csc.itrust.beans.TransactionBean"%>
<%@page import="edu.ncsu.csc.itrust.dao.mysql.LabProcedureDAO"%>
<%@page import="edu.ncsu.csc.itrust.dao.mysql.PatientDAO"%>
<%@page import="edu.ncsu.csc.itrust.dao.mysql.TransactionDAO"%>
<%@page import="edu.ncsu.csc.itrust.dao.mysql.PersonnelDAO"%>
<%@page import="edu.ncsu.csc.itrust.action.ViewMyRecordsAction"%>
<%@page import="edu.ncsu.csc.itrust.action.ViewMyReportRequestsAction"%>
<%@page import="edu.ncsu.csc.itrust.beans.PatientBean"%>
<%@page import="edu.ncsu.csc.itrust.beans.PersonnelBean"%>
<%@page import="edu.ncsu.csc.itrust.beans.ReportRequestBean"%>
<%@page import="java.util.Hashtable"%>
<%@page import="java.sql.Timestamp"%>
<%@page import="java.util.Date"%>
<%@page import="java.util.List"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.LinkedList"%>

<%@include file="/global.jsp" %>

<%
	pageTitle = "iTrust - Patient Home";
%>

<%@include file="/header.jsp" %>

<%
	session.removeAttribute("personnelList");
ViewMyRecordsAction surveyAction = new ViewMyRecordsAction(prodDAO,loggedInMID.longValue());
List <OfficeVisitBean> surList = prodDAO.getOfficeVisitDAO().getOfficeVisitsWithNoSurvey(loggedInMID.longValue());
PatientBean patient = new PatientDAO(prodDAO).getPatient(loggedInMID.longValue());
List<ReportRequestBean> reports = surveyAction.getReportRequests();
ViewMyReportRequestsAction reportAction = new ViewMyReportRequestsAction(prodDAO, loggedInMID.longValue());
LabProcedureDAO lpDAO = new LabProcedureDAO(prodDAO);
List<LabProcedureBean> labProcedures = lpDAO.getLabProceduresForPatientForNextMonth(loggedInMID.longValue());
List<PatientBean> represented = new PatientDAO(prodDAO).getRepresented(loggedInMID.longValue());
TransactionDAO transDAO = new TransactionDAO(prodDAO);
PersonnelDAO personnelDAO = new PersonnelDAO(prodDAO);
// Create an ArrayList and index to hide MIDs from user
List<PersonnelBean> personnelList = new ArrayList<PersonnelBean>();
int personnel_counter = 0;

loggingAction.logEvent(TransactionType.HOME_VIEW, loggedInMID.longValue(), 0, "");
%>

<%
	if(request.getParameter("rep") != null && request.getParameter("rep").equals("1")){
%>
<span class="iTrustMessage"><%=StringEscapeUtils.escapeHtml("" + ("Adverse Event Successfully Reported"))%></span>
<%
	}
%>
<%@include file="/auth/patient/notificationArea.jsp" %>
<%@include file="/auth/patient/activityFeed.jsp" %>
</div>

<%@include file="/footer.jsp" %>
