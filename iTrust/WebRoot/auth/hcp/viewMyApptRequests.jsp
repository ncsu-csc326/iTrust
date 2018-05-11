<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.sql.Timestamp"%>
<%@page import="edu.ncsu.csc.itrust.model.old.dao.mysql.PatientDAO"%>
<%@page import="edu.ncsu.csc.itrust.model.old.beans.ApptRequestBean"%>
<%@page import="java.util.List"%>
<%@page import="edu.ncsu.csc.itrust.action.ViewApptRequestsAction"%>
<%@page errorPage="/auth/exceptionHandler.jsp"%>

<%@include file="/global.jsp"%>

<%
	pageTitle = "iTrust - View My Appointment Requests";
%>

<%@include file="/header.jsp"%>

<%
	ViewApptRequestsAction viewReqAction = new ViewApptRequestsAction(
			loggedInMID, prodDAO);
	List<ApptRequestBean> reqs = viewReqAction.getApptRequests();
	PatientDAO pDAO = prodDAO.getPatientDAO();
%>
<h1>My Appointment Requests</h1>
<%
	String msg = "";
	if ((request.getParameter("req_id") != null)
			&& (request.getParameter("status") != null)) {
		boolean myReq = false;
		ApptRequestBean theReq = null;
		for (ApptRequestBean req : reqs) {
			if ((req.getRequestedAppt().getApptID() + "")
					.equals(request.getParameter("req_id"))) {
				myReq = true;
				theReq = req;
			}
		}
	
		if (myReq) {
			int reqID = Integer.valueOf(request.getParameter("req_id"));
			if ("approve".equals(request.getParameter("status"))) {
				msg = viewReqAction.acceptApptRequest(reqID, loggedInMID, theReq.getRequestedAppt().getPatient());
			} else if ("reject".equals(request.getParameter("status"))) {
				msg = viewReqAction.rejectApptRequest(reqID, loggedInMID, theReq.getRequestedAppt().getPatient());
			}
			reqs = viewReqAction.getApptRequests();
			if(reqs == null){
				msg = "There are currently no appointment requests.";
			}
		} else {
			msg = "That request ID does not reference an appointment request for you.";
		}
	}
	else if(request.getParameter("req_id") == null || request.getParameter("status") == null){
	    msg = "Turns out you do not have any appointment requests right now.";
    }
%>
<%=msg%>
<ul style="list-style-type: none;">
	<%
		for (ApptRequestBean req : reqs) {
			if (req.getRequestedAppt().getDate()
					.compareTo(new Timestamp(System.currentTimeMillis())) >= 0) {
				out.print("<li style=\"padding: 1em; clear: both;\">");
				out.print("<div style=\"float: left; width: 20em;\">");
				out.print("Request from: "
						+ pDAO.getName(req.getRequestedAppt().getPatient())
						+ "<br />");
				out.print("Appointment type: "
						+ req.getRequestedAppt().getApptType() + "<br />");
				SimpleDateFormat frmt = new SimpleDateFormat("MM/dd/yyy hh:mm a");
				out.print("At time: " + frmt.format(req.getRequestedAppt().getDate())
						+ "<br />");
				out.print("Comment: " + req.getRequestedAppt().getComment());
				out.print("</div>");
				out.print("<div style=\"float: left; margin-left: 1em;\">");
				if (req.isPending()) {
					out.print("<a href=\"viewMyApptRequests.jsp?req_id="
							+ req.getRequestedAppt().getApptID()
							+ "&status=approve\">Approve</a><br />");
					out.print("<a href=\"viewMyApptRequests.jsp?req_id="
							+ req.getRequestedAppt().getApptID()
							+ "&status=reject\">Reject</a><br />");

				} else if (!req.isPending() && req.isAccepted()) {
					out.print("Approved");
				} else if (!req.isPending() && !req.isAccepted()) {
					out.print("Rejected");
				}
				out.print("</div>");
				out.println("</li>");
			}
		}
	%>
</ul>

<%@include file="/footer.jsp"%>