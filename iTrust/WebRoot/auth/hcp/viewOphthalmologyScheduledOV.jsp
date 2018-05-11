<%@page import="edu.ncsu.csc.itrust.dao.mysql.ApptTypeDAO"%>
<%@page errorPage="/auth/exceptionHandler.jsp"%>

<%@page import="java.util.Date"%>
<%@page import="java.text.DateFormat"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="edu.ncsu.csc.itrust.action.ViewOphthalmologyScheduleOVAction"%>
<%@page import="edu.ncsu.csc.itrust.action.ViewPersonnelAction"%>
<%@page import="edu.ncsu.csc.itrust.beans.PersonnelBean"%>
<%@page import="edu.ncsu.csc.itrust.beans.OphthalmologyScheduleOVRecordBean"%>
<%@page import="edu.ncsu.csc.itrust.dao.mysql.PatientDAO"%>
<%@page import="edu.ncsu.csc.itrust.dao.mysql.OphthalmologyScheduleOVDAO"%>
<%@page import="java.util.List"%>

<%@include file="/global.jsp" %>

<%
pageTitle = "iTrust - View Scheduled Ophthalmology Office Visit";
%>

<%@include file="/header.jsp" %>
<%
	//if specialty is not oph or opt, simply redirect them to the regular edit office visit page
	ViewPersonnelAction personnelAction = new ViewPersonnelAction(prodDAO, loggedInMID);
	PersonnelBean currentPersonnel = personnelAction.getPersonnel("" + loggedInMID);
	if (!currentPersonnel.getSpecialty().equalsIgnoreCase("ophthalmologist") && !currentPersonnel.getSpecialty().equalsIgnoreCase("optometrist")) {
		response.sendRedirect("/iTrust/auth/hcp/viewMyAppts.jsp");
	}
%>

<%
	PatientDAO pDAO = prodDAO.getPatientDAO();
	ViewOphthalmologyScheduleOVAction scheduleAction = new ViewOphthalmologyScheduleOVAction(prodDAO, loggedInMID);
	OphthalmologyScheduleOVRecordBean original = null;
	
	if (request.getParameter("oid") != null) {
		String aptParameter = request.getParameter("oid");
		try {
			int oid = Integer.parseInt(aptParameter);
			original = scheduleAction.getOphthalmologyScheduleOVForHCP(oid);
			if (original == null){
				response.sendRedirect("viewOphthalmologyScheduleOV.jsp");
			}
		} catch (NullPointerException npe) {
			response.sendRedirect("viewOphthalmologyScheduleOV.jsp");
		} catch (NumberFormatException e) {
			response.sendRedirect("viewOphthalmologyScheduleOV.jsp");
		}
	} else {
		response.sendRedirect("viewOphthalmologyScheduleOV.jsp");
	}
	
	if (original != null) {
		if (loggedInMID == original.getDoctormid()) {
			Date d = new Date(original.getDate().getTime());
			DateFormat format = new SimpleDateFormat("MM/dd/yyyy hh:mm a");
			
			String status = "";
			if(original.isPending()){
				status = "Pending";
			}else if(original.isAccepted()){
				status = "Accepted";
			}else{
				status = "Rejected";
			}
%>
			<div>
				<table width="99%">
					<tr>
						<th>Scheduled Ophthalmology Office Visit Info</th>
					</tr>
					<tr>
						<td><b>Patient:</b> <%= StringEscapeUtils.escapeHtml("" + ( pDAO.getName(original.getPatientmid()) )) %></td>
					</tr>
					<tr>
						<td><b>Date/Time:</b> <%= StringEscapeUtils.escapeHtml("" + ( format.format(d) )) %></td>
					</tr>
					<tr>
						<td><b>Status:</b> <%= StringEscapeUtils.escapeHtml("" + ( status )) %></td>
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
