<%@page import="java.text.ParseException"%>
<%@page errorPage="/auth/exceptionHandler.jsp"%>

<%@page import="java.util.List"%>
<%@page import="java.util.Date"%>
<%@page import="java.sql.Timestamp"%>
<%@page import="java.text.DateFormat"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="edu.ncsu.csc.itrust.model.old.beans.ApptBean"%>
<%@page import="edu.ncsu.csc.itrust.model.old.beans.ApptTypeBean"%>
<%@page import="edu.ncsu.csc.itrust.action.AddApptAction"%>
<%@page import="edu.ncsu.csc.itrust.action.EditApptTypeAction"%>
<%@page import="edu.ncsu.csc.itrust.action.ViewMyApptsAction"%>
<%@page import="edu.ncsu.csc.itrust.model.old.dao.mysql.PatientDAO"%>
<%@page import="edu.ncsu.csc.itrust.model.old.dao.mysql.ApptTypeDAO"%>
<%@page import="edu.ncsu.csc.itrust.exception.ITrustException"%>
<%@page import="edu.ncsu.csc.itrust.exception.FormValidationException"%>

<%@include file="/global.jsp" %>

<%
	pageTitle = "iTrust - Schedule an Appointment";

String headerMessage = "Please fill out the form properly - comments are optional.";
%>

<%@include file="/header.jsp" %>
<form id="mainForm" method="post" action="scheduleAppt.jsp">
		
<%
			AddApptAction action = new AddApptAction(prodDAO, loggedInMID.longValue());
			ViewMyApptsAction viewAction = new ViewMyApptsAction(prodDAO, loggedInMID.longValue());
			EditApptTypeAction types = new EditApptTypeAction(prodDAO, loggedInMID.longValue());
			ApptTypeDAO apptTypeDAO = prodDAO.getApptTypeDAO();
			PatientDAO patientDAO = prodDAO.getPatientDAO();
			
			long patientID = 0L;
			boolean error = false;
			boolean invalidDate = false;
			String hidden = ""; 
			
			boolean isDead = false;
			if (session.getAttribute("pid") != null) {
				String pidString = (String) session.getAttribute("pid");
				patientID = Long.parseLong(pidString);
				try {
			action.getName(patientID);
				} catch (ITrustException ite) {
			patientID = 0L;
				}
				
				isDead = patientDAO.getPatient(patientID).getDateOfDeathStr().length()>0;
			}
			else {
				session.removeAttribute("pid");
			}
			
			String lastSchedDate="";
			String lastApptType="";
			String lastTime1="";
			String lastTime2="";
			String lastTime3="";
			String lastComment="";

			
			if (patientID == 0L) {
				response.sendRedirect("/iTrust/auth/getPatientID.jsp?forward=hcp/scheduleAppt.jsp");
			} else if(isDead){
		%>
		<div align=center>
			<span class="iTrustError">Cannot schedule appointment. This patient is deceased. Please return and select a different patient.</span>
			<br />
			<a href="/iTrust/auth/getPatientID.jsp?forward=hcp/scheduleAppt.jsp">Back</a>		</div>
		<%	
	}else{
		if (request.getParameter("schedule") != null) {
			if(!request.getParameter("schedDate").equals("")) {	
				
				lastSchedDate = request.getParameter("schedDate");
				lastApptType = request.getParameter("apptType");
				lastTime1 = request.getParameter("time1");
				lastTime2 = request.getParameter("time2");
				lastTime3 = request.getParameter("time3");
				lastComment = request.getParameter("comment");
				
				ApptBean appt = new ApptBean();
				DateFormat format = new SimpleDateFormat("MM/dd/yyyy hh:mm a");
				format.setLenient(false);
				try{
					Date date = format.parse(lastSchedDate+" "+lastTime1+":"+lastTime2+" "+lastTime3);
					appt.setDate(new Timestamp(date.getTime()));
				}catch(ParseException e){
					invalidDate=true;
				}
				if(invalidDate==false){
					appt.setHcp(loggedInMID);
					appt.setPatient(patientID);
					appt.setApptType(lastApptType);
					String comment = "";
					boolean ignoreConflicts = false;
					if("Override".equals(request.getParameter("scheduleButton"))){
						ignoreConflicts = true;
					}
				
					if(request.getParameter("comment").equals(""))
						comment = null;
					else 
						comment = request.getParameter("comment");
					appt.setComment(comment);
					try {
						headerMessage = action.addAppt(appt, ignoreConflicts);
						if(headerMessage.startsWith("Success")) {
							session.removeAttribute("pid");
						}else{
							error = true;
							
							if (headerMessage.contains("conflict")){
								hidden = "style='display:none;'";
								List<ApptBean> conflicts = action.getConflictsForAppt(loggedInMID.longValue(), appt);
								%>
								<div align=center id="conflictTable">
									<span class="iTrustError"><%=headerMessage %></span>
									
									<table class="fancyTable">
									<tr><th>Patient</th><th>Appointment Type</th><th>Date / Time</th><th>Duration</th></tr>
									<% for( ApptBean conflict : conflicts){ 
											Date d = new Date(conflict.getDate().getTime());
									%>
									
										<tr>
											<td><%= StringEscapeUtils.escapeHtml("" + ( viewAction.getName(conflict.getPatient()) )) %></td>
											<td><%= StringEscapeUtils.escapeHtml("" + ( conflict.getApptType() )) %></td>
												<td><%= StringEscapeUtils.escapeHtml("" + ( format.format(d) )) %></td> 
							 				<td><%= StringEscapeUtils.escapeHtml("" + ( apptTypeDAO.getApptType(conflict.getApptType()).getDuration()+" minutes" )) %></td>
										</tr>
									<% }  %>
									</table>
									<input type="submit" id="overrideButton" name="scheduleButton" value="Override"/>
									<input type="button" id="cancel" name="cancel" value="Cancel" onClick="$('#apptDiv').css('display','block');$('#conflictTable').hide();"/>
								</div>
								<%
							} else {
								%>
									<div align=center>
										<span class="iTrustError"><%=headerMessage %></span>
									</div>
								<%
							}
						}
					} catch (FormValidationException e){
					%>
					<div align=center><span class="iTrustError"><%=StringEscapeUtils.escapeHtml(e.getMessage())%></span></div>
					<%	
					}
				}else{
					headerMessage = "Please input a valid date for the appointment.";
				}
			}else{
				headerMessage = "Please input a date for the appointment.";
			}
		}
		
		List<ApptTypeBean> apptTypes = types.getApptTypes();
%>

<div align="left" <%=hidden %> id="apptDiv">
	<h2>Schedule an Appointment</h2>
	<h4>with <%= StringEscapeUtils.escapeHtml("" + ( action.getName(patientID) )) %> (<a href="/iTrust/auth/getPatientID.jsp?forward=hcp/scheduleAppt.jsp">someone else</a>):</h4>
	<span class="iTrustMessage"><%= StringEscapeUtils.escapeHtml("" + (headerMessage )) %></span><br /><br />
	<span>Appointment Type: </span>
		<select name="apptType">
			<%
				for(ApptTypeBean b : apptTypes) {
					%>
					<option <% if(b.getName().equals(lastApptType)) out.print("selected='selected'"); %> value="<%= b.getName() %>"><%= StringEscapeUtils.escapeHtml("" + ( b.getName() )) %> - <%= StringEscapeUtils.escapeHtml("" + ( b.getDuration() )) %> minutes</option>
					<%
				}
			%>
		</select>
		<br /><br />
		<span>Schedule Date: </span><input type="text" name="schedDate" 
		<% if (error) {%>
            value="<%= StringEscapeUtils.escapeHtml(lastSchedDate) %>"
        <% } else { %>
            value="<%= StringEscapeUtils.escapeHtml("" + (new SimpleDateFormat("MM/dd/yyyy").format(new Date()))) %>"
        <% } %>
		value="" /><input type="button" value="Select Date" onclick="displayDatePicker('schedDate');" /><br /><br />
		<span>Schedule Time: </span>
		<select name="time1">
			<%
				String hour = "";
				for(int i = 1; i <= 12; i++) {
					if(i < 10) hour = "0"+i;
					else hour = i+"";
					%>
						<option <% if(hour.toString().equals(lastTime1)) out.print("selected='selected'"); %> value="<%=hour%>"><%= StringEscapeUtils.escapeHtml("" + (hour)) %></option>
					<%
				}
			%>
		</select>:<select name="time2">
			<%
				String min = "";
				for(int i = 0; i < 60; i+=5) {
					if(i < 10) min = "0"+i;
					else min = i+"";
					%>
						<option <% if(min.toString().equals(lastTime2)) out.print("selected='selected'"); %> value="<%=min%>"><%= StringEscapeUtils.escapeHtml("" + (min)) %></option>
					<%
				}
			%>
		</select>
		<select name="time3"><option <% if("AM".equals(lastTime3)) out.print("selected='selected'"); %> value="AM">AM</option
		><option  <% if(!error || "PM".equals(lastTime3)) out.print("selected='selected'"); %> value="PM">PM</option></select><br /><br />
		<span>Comment: </span><br />
		<textarea name="comment" cols="100" rows="10"><% if (error) out.print(StringEscapeUtils.escapeHtml(lastComment)); %></textarea><br />
		<br />
		<input type="submit" value="Schedule" name="scheduleButton"/>
		<input type="hidden" value="Schedule" name="schedule"/>
		<input type="hidden" id="override" name="override" value="noignore"/>

	<br />
	<br />
</div>
	</form>
<%	} %>

<%@include file="/footer.jsp" %>