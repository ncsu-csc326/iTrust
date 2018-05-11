<%@page errorPage="/auth/exceptionHandler.jsp"%>

<%@page import="edu.ncsu.csc.itrust.action.EditApptAction"%>
<%@page import="edu.ncsu.csc.itrust.action.EditApptTypeAction"%>
<%@page import="edu.ncsu.csc.itrust.action.ViewMyApptsAction"%>
<%@page import="edu.ncsu.csc.itrust.beans.ApptBean"%>
<%@page import="edu.ncsu.csc.itrust.beans.ApptTypeBean"%>
<%@page import="edu.ncsu.csc.itrust.dao.mysql.ApptTypeDAO"%>
<%@page import="edu.ncsu.csc.itrust.exception.FormValidationException"%>
<%@page import="edu.ncsu.csc.itrust.exception.ITrustException"%>
<%@page import="java.sql.Timestamp"%>
<%@page import="java.text.DateFormat"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.Date"%>
<%@page import="java.util.List"%>

<%@include file="/global.jsp" %>

<%
	pageTitle = "iTrust - Edit Appointment";
%>

<%@include file="/header.jsp" %>

<%
	EditApptTypeAction types = new EditApptTypeAction(prodDAO, loggedInMID.longValue());
	EditApptAction action = new EditApptAction(prodDAO, loggedInMID.longValue());
	ViewMyApptsAction viewAction = new ViewMyApptsAction(prodDAO, loggedInMID.longValue());
	ApptTypeDAO apptTypeDAO = prodDAO.getApptTypeDAO();
	ApptBean original = null;
	List<ApptTypeBean> apptTypes = types.getApptTypes();
	String aptParameter = "";
	if (request.getParameter("apt") != null) {
		aptParameter = request.getParameter("apt");
		try {
	int apptID = Integer.parseInt(aptParameter);
	original = action.getAppt(apptID);
	if (original == null){
		response.sendRedirect("viewMyAppts.jsp");
	}
		} catch (NullPointerException npe) {
	response.sendRedirect("viewMyAppts.jsp");
		} catch (NumberFormatException e) {
	// Handle Exception
	response.sendRedirect("viewMyAppts.jsp");
		}
	} else {
		response.sendRedirect("viewMyAppts.jsp");
	}
	
	Long patientID = 0L;
	if (session.getAttribute("pid") != null) {
		String pidString = (String) session.getAttribute("pid");
		patientID = Long.parseLong(pidString);
		try {
	action.getName(patientID);
		} catch (ITrustException ite) {
	patientID = 0L;
		}
	}
	
	boolean hideForm = false;

	boolean error = false;
	String hidden = "";

	Date oldDate = new Date(original.getDate().getTime());
	DateFormat dFormat = new SimpleDateFormat("MM/dd/yyyy");
	DateFormat tFormat = new SimpleDateFormat("hhmma");
	String hPart = tFormat.format(oldDate).substring(0,2);
	String mPart = tFormat.format(oldDate).substring(2,4);
	String aPart = tFormat.format(oldDate).substring(4);
	
	String lastSchedDate=dFormat.format(oldDate);
	String lastApptType=original.getComment();
	String lastTime1=hPart;
	String lastTime2=mPart;
	String lastTime3=aPart;
	String lastComment=original.getComment();
	if(lastComment == null) lastComment="";
	
	// Handle form submit here
	if (request.getParameter("editAppt") != null && request.getParameter("apptID") != null) {
		String headerMessage = "";
		if (request.getParameter("editAppt").equals("Change")) {
	// Change the appointment
	if(!request.getParameter("schedDate").equals("")) {	
		
		lastSchedDate = request.getParameter("schedDate");
		lastApptType = request.getParameter("apptType");
		lastTime1 = request.getParameter("time1");
		lastTime2 = request.getParameter("time2");
		lastTime3 = request.getParameter("time3");
		lastComment = request.getParameter("comment");
		
		
		ApptBean appt = new ApptBean();
		appt.setApptID(Integer.parseInt(request.getParameter("apptID")));
		DateFormat format = new SimpleDateFormat("MM/dd/yyyy hh:mm a");
		Date date = format.parse(lastSchedDate+" "+lastTime1+":"+lastTime2+" "+lastTime3);
		appt.setApptType(lastApptType);
		appt.setDate(new Timestamp(date.getTime()));
		appt.setHcp(loggedInMID);
		appt.setPatient(patientID);

		boolean ignoreConflicts = false;
		if("ignore".equals(request.getParameter("override"))){
			ignoreConflicts = true;
		}
		
		
		String comment = "";
		if(request.getParameter("comment").equals("") || request.getParameter("comment").equals("No Comment"))
			comment = null;
		else 
			comment = request.getParameter("comment");
		appt.setComment(comment);
		try {
			headerMessage = action.editAppt(appt,ignoreConflicts);
			if(headerMessage.startsWith("Success")) {
				hideForm = true;
				session.removeAttribute("pid");
				loggingAction.logEvent(TransactionType.APPOINTMENT_EDIT, loggedInMID.longValue(), original.getPatient(), ""+appt.getApptID());
				if(ignoreConflicts){
					loggingAction.logEvent(TransactionType.APPOINTMENT_CONFLICT_OVERRIDE, loggedInMID.longValue(), original.getPatient(), "");
				}
%>
							<div align=center>
								<span class="iTrustMessage"><%=StringEscapeUtils.escapeHtml(headerMessage)%></span>
							</div>
						<%
					} else {
						if (headerMessage.contains("conflict")){
							hidden = "style='display: none;'";
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
								<input type="button" id="overrideButton" name="overrideButton" value="Ignore Conflict" onClick="document.getElementById('override').value='ignore';document.getElementById('editAppt').value='Change';document.getElementById('mainForm').submit()"/> 
								<input type="button" id="cancel" name="cancel" value="Cancel" onClick="$('#mainForm').css('display','block');$('#conflictTable').hide();"/>						</div>
							<%
						} else {
						%>
							<div align=center>
								<span class="iTrustError"><%=StringEscapeUtils.escapeHtml(headerMessage)%></span>
							</div>
						<%
						}
					}
				} catch (FormValidationException e){
				%>
				<div align=center><span class="iTrustError"><%=StringEscapeUtils.escapeHtml(e.getMessage())%></span></div>
				<%	
				}
			} else {
				headerMessage = "Please input a date for the appointment.";
			}
		} else if (request.getParameter("editAppt").equals("Remove")) {
			// Delete the appointment
			ApptBean appt = new ApptBean();
			appt.setApptID(Integer.parseInt(request.getParameter("apptID")));

			headerMessage = action.removeAppt(appt);
			if(headerMessage.startsWith("Success")) {
				hideForm = true;
				session.removeAttribute("pid");
				loggingAction.logEvent(TransactionType.APPOINTMENT_REMOVE, loggedInMID.longValue(), original.getPatient(), ""+original.getApptID());
				%>
				<div align=center>
					<span class="iTrustMessage"><%=StringEscapeUtils.escapeHtml(headerMessage)%></span>
				</div>
				<%
			} else {
				
				%>
					<div align=center>
						<span class="iTrustError"><%=StringEscapeUtils.escapeHtml(headerMessage)%></span>
					</div>
				<%
			}
		}
	}
	if (original != null && !hideForm) {
		boolean selected = false;
		
		loggingAction.logEvent(TransactionType.APPOINTMENT_VIEW, loggedInMID, original.getPatient(), "" + original.getApptID());
	%>
	<form id="mainForm" <%=hidden %> method="post" action="editAppt.jsp?apt=<%=aptParameter %>&apptID=<%=original.getApptID() %>">
		<div>
			<table width="99%">
				<tr>
					<th>Appointment Info</th>
				</tr>
				<tr>
					<td><b>Patient:</b> <%= StringEscapeUtils.escapeHtml("" + ( action.getName(original.getPatient()) )) %></td>
				</tr>
				<tr>
					<td>
						<b>Type:</b>
						<select name="apptType">
					<%
						for(ApptTypeBean b : apptTypes) {
					%>
							<option <%=StringEscapeUtils.escapeHtml((b.getName().equals(lastApptType)?"selected ":"")) %>value="<%= StringEscapeUtils.escapeHtml(b.getName()) %>">
								<%= StringEscapeUtils.escapeHtml(b.getName() + " - " + b.getDuration()+ " minutes") %>
							</option>
					<%
						}
					%>
						</select>
					</td>
				</tr>
				<tr>
					<td>
						<b>Date:</b>
						<input type="text" name="schedDate" value="<%=lastSchedDate %>" />
						<input type="button" value="Select Date" onclick="displayDatePicker('schedDate');" />
					</td>
				</tr>
				<tr>
					<td>
						<b>Time:</b>
						<select name="time1">
							<%
								String hour = "";
								for(int i = 1; i <= 12; i++) {
									if(i < 10) hour = "0"+i;
									else hour = i+"";
									selected = hour.equals(lastTime1);
									%>
										<option <%=selected?"selected ":"" %>value="<%=hour%>"><%= StringEscapeUtils.escapeHtml("" + (hour)) %></option>
									<%
								}
							%>
						</select>:
						<select name="time2">
							<%
								String min = "";
								for(int i = 0; i < 60; i+=5) {
									if(i < 10) min = "0"+i;
									else min = i+"";
									selected = min.equals(lastTime2);
									%>
										<option <%=selected?"selected ":"" %>value="<%=min%>"><%= StringEscapeUtils.escapeHtml("" + (min)) %></option>
									<%
								}
								selected = "AM".equals(lastTime3);
							%>
						</select>
						<select name="time3">
							<option <%=selected?"selected ":"" %>value="AM">AM</option>
							<option <%=selected?"":"selected " %>value="PM">PM</option>
						</select>
					</td>
				</tr>
			</table>
		</div>
		
		<table>
			<tr>
				<td colspan="2"><b>Comments:</b></td>
			</tr>
			<tr>
				<td>
					<textarea name="comment" cols="100" rows="10"><%=StringEscapeUtils.escapeHtml("" + ((lastComment== null)?"No Comment":original.getComment())) %></textarea>
				</td>
			</tr>
		</table>
		<input type="hidden" id="editAppt" name="editAppt" value=""/>
		<input type="submit" value="Change" name="editApptButton" id="changeButton" onClick="document.getElementById('editAppt').value='Change'"/>
		<input type="submit" value="Remove" name="editApptButton" id="removeButton" onClick="document.getElementById('editAppt').value='Remove'"/>
		
		<input type="hidden" id="override" name="override" value="noignore"/>
	</form>
<%
	}
%>

<%@include file="/footer.jsp" %>
