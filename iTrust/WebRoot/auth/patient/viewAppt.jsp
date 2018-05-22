<%@page errorPage="/auth/exceptionHandler.jsp"%>

<%@page import="java.util.Date"%>
<%@page import="java.text.DateFormat"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="edu.ncsu.csc.itrust.action.EditApptAction"%>
<%@page import="edu.ncsu.csc.itrust.action.EditApptTypeAction"%>
<%@page import="edu.ncsu.csc.itrust.action.ViewMyApptsAction"%>
<%@page import="edu.ncsu.csc.itrust.action.EditRepresentativesAction"%>
<%@page import="edu.ncsu.csc.itrust.action.ViewPersonnelAction"%>
<%@page import="edu.ncsu.csc.itrust.model.old.beans.ApptBean"%>
<%@page import="edu.ncsu.csc.itrust.model.old.dao.mysql.ApptTypeDAO"%>
<%@page import="edu.ncsu.csc.itrust.model.old.beans.PatientBean"%>
<%@page import="edu.ncsu.csc.itrust.model.old.beans.PersonnelBean"%>
<%@page import="java.util.List"%>

<%@include file="/global.jsp" %>

<%
pageTitle = "iTrust - View Message";
%>

<%@include file="/header.jsp" %>

<%
	long mid = loggedInMID.longValue();
	ViewMyApptsAction apptAction = new ViewMyApptsAction(prodDAO, loggedInMID.longValue());
	EditApptAction editAction = null;
	ApptTypeDAO apptTypeDAO = prodDAO.getApptTypeDAO();
	
	if (request.getParameter("patient") != null) {
		String patientParameter = request.getParameter("patient");
		try {
			mid = Long.parseLong(patientParameter);
		} catch (NumberFormatException nfe) {
			response.sendRedirect("viewMyAppts.jsp");
		}
		EditRepresentativesAction representativeAction = new EditRepresentativesAction(prodDAO, loggedInMID.longValue(), String.valueOf(loggedInMID.longValue()));
		List<PatientBean> representees = representativeAction.getRepresented(loggedInMID.longValue());
		boolean isRepresented = (loggedInMID == mid);
		if (!isRepresented) {
			for(PatientBean patientDataBean: representees) {
				if(patientDataBean.getMID() == mid) {
					isRepresented = true;
					break;
				}
			}
		}
		if(!isRepresented) {
			response.sendRedirect("viewMyAppts.jsp");
		}
		session.setAttribute("appts", apptAction.getAppointments(mid));
	}
	
	apptAction.setLoggedInMID(mid);
	ApptBean original = null;
	
	if (request.getParameter("apt") != null) {
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
	}
	else {
		response.sendRedirect("viewMyAppts.jsp");
	}
	
	
	if (original != null && editAction != null) {
		EditRepresentativesAction repAction = new EditRepresentativesAction(prodDAO, loggedInMID, ""+loggedInMID);
		List<PatientBean> representees = repAction.getRepresented(loggedInMID.longValue());
		boolean authorized = false;
		for (PatientBean pBean : representees) {
			if (pBean.getMID() == original.getPatient()) {
				authorized = true;
				break;
			}
		}
		
		if (loggedInMID == original.getPatient())
			authorized = true;
		
		if (authorized) {
			Date d = new Date(original.getDate().getTime());
			DateFormat format = new SimpleDateFormat("MM/dd/yyyy hh:mm a");
			editAction.logViewAction();
		%>
	    <div align="center">
			<div>
				<table>
					<tr>
						<th>Appointment Info</th>
					</tr>
					<tr>
						<td><b>Patient:</b> <%= StringEscapeUtils.escapeHtml("" + ( apptAction.getName(original.getPatient()) )) %></td>
					</tr>
					<tr>
						<td><b>HCP:</b> <%= StringEscapeUtils.escapeHtml("" + ( apptAction.getName(original.getHcp()) )) %></td>
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
	    </div>
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
