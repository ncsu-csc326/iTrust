
<%@page errorPage="/auth/exceptionHandler.jsp"%>

<%@page import="java.util.List"%>
<%@page import="java.util.Date"%>
<%@page import="java.text.DateFormat"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.sql.Timestamp"%>
<%@page import="edu.ncsu.csc.itrust.action.ViewOphthalmologyScheduleOVAction"%>
<%@page import="edu.ncsu.csc.itrust.action.ViewPersonnelAction"%>
<%@page import="edu.ncsu.csc.itrust.beans.PersonnelBean"%>
<%@page import="edu.ncsu.csc.itrust.beans.OphthalmologyScheduleOVRecordBean"%>
<%@page import="edu.ncsu.csc.itrust.dao.mysql.PatientDAO"%>
<%@page import="edu.ncsu.csc.itrust.dao.mysql.OphthalmologyScheduleOVDAO"%>
<%@page import="edu.ncsu.csc.itrust.dao.DAOFactory"%>

<%@include file="/global.jsp" %>

<%
pageTitle = "iTrust - View Scheduled Ophthalmology Office Visits";
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
<div align=center>
	<h2>My Scheduled Ophthalmology Office Visits</h2>
<%
	PatientDAO pDAO = prodDAO.getPatientDAO();
	ViewOphthalmologyScheduleOVAction scheduleAction = new ViewOphthalmologyScheduleOVAction(prodDAO, loggedInMID);
	List<OphthalmologyScheduleOVRecordBean> requests = scheduleAction.getOphthalmologyScheduleOVByDOCTORMID(loggedInMID);
	session.setAttribute("appts", requests);
	if (requests.size() > 0) { %>	
	<table class="fTable">
		<tr>
			<th>Patient</th>
			<th>Appointment Date/Time</th>
			<th>Comments</th>
			<th>Status</th>
		</tr>
<%		 
		int index = 0;
		for(OphthalmologyScheduleOVRecordBean a : requests) { 
			String comment = "No Comment";
			if(a.getComment() != null)
				comment = "<a href='viewOphthalmologyScheduledOV.jsp?oid="+a.getOid()+"'>Read Comment</a>";
				
			Date d = new Date(a.getDate().getTime());
			Date now = new Date();
			DateFormat format = new SimpleDateFormat("MM/dd/yyyy hh:mm a");
			
			String status = "";
			if(a.isPending()){
				status = "Pending";
			}else if(a.isAccepted()){
				status = "Accepted";
			}else{
				status = "Rejected";
			}
			
			String row = "<tr";
%>
			<%=row+" "+((index%2 == 1)?"class=\"alt\"":"")+">"%>
				<td><%= StringEscapeUtils.escapeHtml("" + pDAO.getName(a.getPatientmid())) %></td>
				<td><%= StringEscapeUtils.escapeHtml("" + ( format.format(d) )) %></td>
				<td><%= comment %></td>
				<td><% if(d.after(now)) %><%=StringEscapeUtils.escapeHtml("" + ( status )) %></td>
			</tr>
	<%
			index ++;
		}
	%>
	</table>
<%	} else { %>
	<div>
		<i>You have no Appointments</i>
	</div>
<%	} %>	
	<br />
</div>

<%@include file="/footer.jsp" %>
