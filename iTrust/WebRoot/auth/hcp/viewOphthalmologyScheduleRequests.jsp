<%@page import="java.util.List"%>
<%@page import="edu.ncsu.csc.itrust.action.EditOphthalmologyScheduleOVAction"%>
<%@page import="edu.ncsu.csc.itrust.action.ViewOphthalmologyScheduleOVAction"%>
<%@page import="edu.ncsu.csc.itrust.action.ViewPersonnelAction"%>
<%@page import="edu.ncsu.csc.itrust.beans.PersonnelBean"%>
<%@page import="edu.ncsu.csc.itrust.beans.OphthalmologyScheduleOVRecordBean"%>
<%@page import="edu.ncsu.csc.itrust.dao.mysql.PatientDAO"%>
<%@page import="edu.ncsu.csc.itrust.dao.mysql.OphthalmologyScheduleOVDAO"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.sql.Timestamp"%>

<%@taglib uri="/WEB-INF/tags.tld" prefix="itrust"%>
<%@page errorPage="/auth/exceptionHandler.jsp"%>

<%@include file="/global.jsp"%>

<%pageTitle = "iTrust - Scheduled Ophthalmology Office Visit Requests";%>

<%@include file="/header.jsp"%>
<%
	//if specialty is not oph or opt, simply redirect them to the regular edit office visit page
	ViewPersonnelAction personnelAction = new ViewPersonnelAction(prodDAO, loggedInMID);
	PersonnelBean currentPersonnel = personnelAction.getPersonnel("" + loggedInMID);
	if (!currentPersonnel.getSpecialty().equalsIgnoreCase("ophthalmologist") && !currentPersonnel.getSpecialty().equalsIgnoreCase("optometrist")) {
		response.sendRedirect("/iTrust/auth/hcp-uap/editOfficeVisit.jsp");
	}
%>
<h1>My Ophthalmology Office Visit Requests</h1>
<%	
	PatientDAO pDAO = prodDAO.getPatientDAO();
	EditOphthalmologyScheduleOVAction editAction = new EditOphthalmologyScheduleOVAction(prodDAO, loggedInMID);
	ViewOphthalmologyScheduleOVAction scheduleAction = new ViewOphthalmologyScheduleOVAction(prodDAO, loggedInMID);
	List<OphthalmologyScheduleOVRecordBean> requests = scheduleAction.getOphthalmologyScheduleOVByDOCTORMID(loggedInMID);
	String msg = "";
	if(requests.size() == 0){
		msg = "Turns out you do not have any appointment requests right now.";
	}else{
		if ((request.getParameter("req_id") != null)
				&& (request.getParameter("status") != null)) {
			boolean myReq = false;
			OphthalmologyScheduleOVRecordBean theReq = null;
			for (OphthalmologyScheduleOVRecordBean req : requests) {
				if ((req.getOid() + "")
						.equals(request.getParameter("req_id"))) {
					myReq = true;
					theReq = req;
				}
			}
			if (myReq) {
				int reqID = Integer.valueOf(request.getParameter("req_id"));
				if ("approve".equals(request.getParameter("status"))) {
					msg = "The Ophthalmology Office Visit Request has been accepted.";
					theReq.setPending(false);
					theReq.setAccepted(true);
					editAction.editOphthalmologyScheduleOV(reqID, theReq);
				} else if ("reject".equals(request.getParameter("status"))) {
					msg = "The Ophthalmology Office Visit Request has been rejected.";
					theReq.setPending(false);
					theReq.setAccepted(false);
					editAction.editOphthalmologyScheduleOV(reqID, theReq);
				}
				requests = scheduleAction.getOphthalmologyScheduleOVByDOCTORMID(loggedInMID);
				if(requests == null){
					msg = "There are currently no appointment requests.";
				}
			} else {
				msg = "That Scheduled OID does not reference an appointment request for you.";
			}
		}
	}
%>
<%=msg%>
<ul style="list-style-type: none;">
	<%
		for (OphthalmologyScheduleOVRecordBean req : requests) {
			if (req.getDate().compareTo(new Timestamp(System.currentTimeMillis())) >= 0) {
				out.print("<li style=\"padding: 1em; clear: both;\">");
				out.print("<div style=\"float: left; width: 20em;\">");
				out.print("Request from: " + pDAO.getName(req.getPatientmid()) + "<br />");
				SimpleDateFormat frmt = new SimpleDateFormat("MM/dd/yyy hh:mm a");
				out.print("At time: " + frmt.format(req.getDate()) + "<br />");
				out.print("Comment: " + req.getComment());
				out.print("</div>");
				out.print("<div style=\"float: left; margin-left: 1em;\">");
				if (req.isPending()) {
					out.print("<a href=\"viewOphthalmologyScheduleRequests.jsp?req_id="
							+ req.getOid()
							+ "&status=approve\">Accept</a><br />");
					out.print("<a href=\"viewOphthalmologyScheduleRequests.jsp?req_id="
							+ req.getOid()
							+ "&status=reject\">Reject</a><br />");
				} else if (!req.isPending() && req.isAccepted()) {
					out.print("Accepted");
				} else if (!req.isPending() && !req.isAccepted()) {
					out.print("Rejected");
				}
				out.print("</div>");
				out.println("</li>\n\n");
			}
		}
	%>
</ul>
<%@include file="/footer.jsp" %>