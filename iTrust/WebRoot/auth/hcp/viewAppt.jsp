<%@page import="edu.ncsu.csc.itrust.model.old.dao.mysql.ApptTypeDAO"%>
<%@page errorPage="/auth/exceptionHandler.jsp"%>

<%@page import="java.util.Date"%>
<%@page import="java.text.DateFormat"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="edu.ncsu.csc.itrust.action.EditApptAction"%>
<%@page import="edu.ncsu.csc.itrust.action.EditApptTypeAction"%>
<%@page import="edu.ncsu.csc.itrust.action.ViewMyApptsAction"%>
<%@page import="edu.ncsu.csc.itrust.model.old.beans.ApptBean"%>
<%@page import="java.util.List"%>

<%@include file="/global.jsp" %>

<%
pageTitle = "iTrust - View Message";
%>

<%@include file="/header.jsp" %>

<%
	ViewMyApptsAction action = new ViewMyApptsAction(prodDAO, loggedInMID.longValue());
	ApptTypeDAO apptTypeDAO = prodDAO.getApptTypeDAO();
	ApptBean original = null;
	EditApptAction editAction = null;
	
	if (request.getParameter("apt") != null) {
		/*String aptParameter = request.getParameter("apt");
		int aptIndex = 0;
		try {
			aptIndex = Integer.parseInt(aptParameter);
		} catch (NumberFormatException nfe) {
			response.sendRedirect("viewMyAppts.jsp");
		}
		List<ApptBean> appts = null; 
		if (session.getAttribute("appts") != null) {
			appts = (List<ApptBean>) session.getAttribute("appts");
			if(aptIndex > appts.size() || aptIndex < 0) {
				aptIndex = 0;
				response.sendRedirect("oops.jsp");
			}
		} else {
			response.sendRedirect("viewMyAppts.jsp");
		}
		original = (ApptBean)appts.get(aptIndex);
		*/
		editAction = new EditApptAction(prodDAO, loggedInMID.longValue());
		String aptParameter = request.getParameter("apt");
		try {
			int apptID = Integer.parseInt(aptParameter);
			original = editAction.getAppt(apptID);
			if (original == null){
				response.sendRedirect("viewMyAppts.jsp");
			} else {
				editAction.setOriginalApptID(original.getApptID());
				editAction.setOriginalPatient(original.getPatient());
			}
		} catch (NullPointerException npe) {
			response.sendRedirect("viewMyAppts.jsp");
		} catch (NumberFormatException e) {
			response.sendRedirect("viewMyAppts.jsp");
		}
	} else {
		response.sendRedirect("viewMyAppts.jsp");
	}
	
	if (original != null && editAction != null) {
		if (loggedInMID == original.getHcp()) {
			Date d = new Date(original.getDate().getTime());
			DateFormat format = new SimpleDateFormat("MM/dd/yyyy hh:mm a");
			editAction.logViewAction();
%>
			<div>
				<table width="99%">
					<tr>
						<th>Appointment Info</th>
					</tr>
					<tr>
						<td><b>Patient:</b> <%= StringEscapeUtils.escapeHtml("" + ( action.getName(original.getPatient()) )) %></td>
					</tr>
					<tr>
						<td><b>Type:</b> <%= StringEscapeUtils.escapeHtml("" + ( original.getApptType() )) %></td>
					</tr>
					<tr>
						<td><b>Date/Time:</b> <%= StringEscapeUtils.escapeHtml("" + ( format.format(d) )) %></td>
					</tr>
					<tr>
						<td><b>Duration:</b> <%= StringEscapeUtils.escapeHtml("" + ( apptTypeDAO.getApptType(original.getApptType()).getDuration()+" minutes" )) %></td>
					</tr>
				</table>
			</div>
			
			<table>
				<tr>
					<td colspan="2"><b>Comments:</b></td>
				</tr>
				<tr>
					<td colspan="2"><%= StringEscapeUtils.escapeHtml("" + ( (original.getComment()== null)?"No Comment":original.getComment() )) %></td>
				</tr>
			</table>
<%
		} else {
%>
		<div align=center>
			<span class="iTrustError">You are not authorized to view details of this appointment</span>
		</div>
<%
		}
	}
%>

<%@include file="/footer.jsp" %>
