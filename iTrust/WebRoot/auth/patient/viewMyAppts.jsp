<%@page errorPage="/auth/exceptionHandler.jsp"%>

<%@page import="java.util.List"%>
<%@page import="java.util.Date"%>
<%@page import="java.text.DateFormat"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.sql.Timestamp"%>
<%@page import="edu.ncsu.csc.itrust.action.EditApptTypeAction"%>
<%@page import="edu.ncsu.csc.itrust.action.ViewMyApptsAction"%>
<%@page import="edu.ncsu.csc.itrust.model.old.beans.ApptBean"%>
<%@page import="edu.ncsu.csc.itrust.model.old.dao.mysql.ApptTypeDAO"%>
<%@page import="edu.ncsu.csc.itrust.model.old.dao.DAOFactory"%>

<%@include file="/global.jsp" %>

<%
pageTitle = "iTrust - View My Messages";
%>

<%@include file="/header.jsp" %>

<div align=center>
	<h2>My Appointments</h2>
<%
	loggingAction.logEvent(TransactionType.APPOINTMENT_ALL_VIEW, loggedInMID.longValue(), 0, "");
	
	ViewMyApptsAction action = new ViewMyApptsAction(prodDAO, loggedInMID.longValue());
	EditApptTypeAction types = new EditApptTypeAction(prodDAO, loggedInMID.longValue());
	ApptTypeDAO apptTypeDAO = prodDAO.getApptTypeDAO();
	List<ApptBean> appts = action.getMyAppointments();
	session.setAttribute("appts", appts);
	if (appts.size() > 0) {
%>	
	<table class="fancyTable">
		<tr>
			<th>Patient</th>
			<th>Appointment Type</th>
			<th>Appointment Date/Time</th>
			<th>Duration</th>
			<th></th>
		</tr>
<%		 
		

		List<ApptBean>conflicts = action.getAllConflicts(loggedInMID.longValue());

		int index = 0;
		for(ApptBean a : appts) { 
			String comment = "";
			if(a.getComment() == null)
				comment = "No Comment";
			else
				comment = "<a href='viewAppt.jsp?apt=" + a.getApptID() + "'>Read Comment</a>";
				
			Date d = new Date(a.getDate().getTime());
			DateFormat format = new SimpleDateFormat("MM/dd/yyyy hh:mm a");
			
			String row = "";
			if(conflicts.contains(a))
				row = "<tr style='font-weight: bold;'";
			else
				row = "<tr";
%>
			<%=row+" "+((index%2 == 1)?"class=\"alt\"":"")+">"%>
				<td><%= StringEscapeUtils.escapeHtml("" + ( action.getName(a.getHcp()) )) %></td>
				<td><%= StringEscapeUtils.escapeHtml("" + ( a.getApptType() )) %></td>
				<td><%= StringEscapeUtils.escapeHtml("" + ( format.format(d) )) %></td>
 				<td><%= StringEscapeUtils.escapeHtml("" + ( apptTypeDAO.getApptType(a.getApptType()).getDuration()+" minutes" )) %></td>
				<td><%= comment %></td>
			</tr>
	<%		index ++; %>
	<%	} %>
	</table>
<%	} else { %>
	<div>
		<i>You have no Appointments</i>
	</div>
<%	} %>	
	<br />
</div>

<%@include file="/footer.jsp" %>
