<%@page import="java.text.ParseException"%>
<%@page import="java.sql.Timestamp"%>
<%@page import="java.util.Date"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="edu.ncsu.csc.itrust.beans.ApptBean"%>
<%@page import="edu.ncsu.csc.itrust.beans.ApptTypeBean"%>
<%@page import="edu.ncsu.csc.itrust.dao.mysql.ApptTypeDAO"%>
<%@page import="edu.ncsu.csc.itrust.beans.HCPVisitBean"%>
<%@page import="edu.ncsu.csc.itrust.action.ViewVisitedHCPsAction"%>
<%@page import="edu.ncsu.csc.itrust.beans.PersonnelBean"%>
<%@page errorPage="/auth/exceptionHandler.jsp"%>
<%@page import="edu.ncsu.csc.itrust.action.AddApptRequestAction"%>
<%@page import="edu.ncsu.csc.itrust.beans.ApptRequestBean"%>
<%@page import="java.util.List"%>

<%@include file="/global.jsp"%>

<%
	pageTitle = "iTrust - Appointment Requests";
%>
<%@include file="/header.jsp"%>
<%
	AddApptRequestAction action = new AddApptRequestAction(prodDAO); //ViewAppt in HCP
	ViewVisitedHCPsAction hcpAction = new ViewVisitedHCPsAction(
			prodDAO, loggedInMID.longValue()); //ViewAppt in HCP

	List<HCPVisitBean> visits = hcpAction.getVisitedHCPs();

	ApptTypeDAO apptTypeDAO = prodDAO.getApptTypeDAO();
	List<ApptTypeBean> apptTypes = apptTypeDAO.getApptTypes();
	String msg = "";
	long hcpid = 0L;
	String comment = "";
	String date = "";
	String hourI = "";
	String minuteI = "";
	String tod = "";
	String apptType = "";
	String prompt = "";
	if (request.getParameter("request") != null) {
		ApptBean appt = new ApptBean();
		appt.setPatient(loggedInMID);
		hcpid = Long.parseLong(request.getParameter("lhcp"));
		appt.setHcp(hcpid);
		comment = request.getParameter("comment");
		appt.setComment(comment);
		SimpleDateFormat frmt = new SimpleDateFormat(
				"MM/dd/yyyy hh:mm a");
		date = request.getParameter("startDate");
		hourI = request.getParameter("time1");
		minuteI = request.getParameter("time2");
		tod = request.getParameter("time3");
		apptType = request.getParameter("apptType");
		appt.setApptType(apptType);
		try {
			Date d = frmt.parse(date + " " + hourI + ":" + minuteI
					+ " " + tod);
			appt.setDate(new Timestamp(d.getTime()));
			ApptRequestBean req = new ApptRequestBean();
			req.setRequestedAppt(appt);
			msg = action.addApptRequest(req);
			if (msg.contains("conflicts")) {
				msg = "ERROR: " + msg;
				frmt = new SimpleDateFormat("MM/dd/yyyy hh:mm a");
				List<ApptBean> open = action.getNextAvailableAppts(3, appt);
				prompt="<br/>The following nearby time slots are available:<br/>";
				int index = 0;
				for(ApptBean possible : open) {
					index++;
					String newDate = frmt.format(possible.getDate());
					prompt += "<div style='padding:5px;margin:5px;float:left;border:1px solid black;'><b>Option " + index+ "</b><br/>"+ frmt.format(possible.getDate()); 
					prompt+="<form action='appointmentRequests.jsp' method='post'>"
						+"<input type='hidden' name='lhcp' value='"+hcpid+"'/>"
						+"<input type='hidden' name='apptType' value='"+apptType+"'/>	"
						+"<input type='hidden' name='startDate' value='"+newDate.substring(0,10)+"'/>"
						+"<input type='hidden' name='time1' value='"+newDate.substring(11,13)+"'/>"
						+"<input type='hidden' name='time2' value='"+newDate.substring(14,16)+"'/>"
						+"<input type='hidden' name='time3' value='"+newDate.substring(17)+"'/>"
						+"<input type='hidden' name='comment' value='"+comment+"'/>"
						+"<input type='submit' name='request' value='Select this time'/>"
					+"</form></div>";
					
				}
				prompt+="<div style='clear:both;'><br/></div>";
			} else {
				loggingAction.logEvent(
						TransactionType.APPOINTMENT_REQUEST_SUBMITTED,
						loggedInMID, hcpid, "");
			}
		} catch (ParseException e) {
			msg = "ERROR: Date must by in the format: MM/dd/yyyy";
		}
	}
%>
<h1>Request an Appointment</h1>
<%
	if (msg.contains("ERROR")) {
%>
<span class="iTrustError"><%=msg%></span>
<%
	} else {
%>
<span class="iTrustMessage"><%=msg%></span>
<%
	}
%>
<%=prompt%>
<form action="appointmentRequests.jsp" method="post">
	<p>HCP:</p>
	<select name="lhcp">
		<%
			for (HCPVisitBean visit : visits) {
		%><option
			<%if (visit.getHCPMID() == hcpid)
					out.println("selected");%>
			value="<%=visit.getHCPMID()%>"><%=visit.getHCPName()%></option>
		<%
			}
		%>
	</select>
	<p>Appointment Type:</p>
	<select name="apptType">
		<%
			for (ApptTypeBean appt : apptTypes) {
		%><option
			<%if (appt.getName().equals(apptType))
					out.print("selected='selected'");%>
			value="<%=appt.getName()%>"><%=appt.getName()%></option>
		<%
			}
			String startDate = "";
		%>
	</select>
	<p>Date:</p>
	<input name="startDate"
		value="<%=StringEscapeUtils.escapeHtml("" + (date))%>" size="10">
	<input type=button value="Select Date"
		onclick="displayDatePicker('startDate');">
	<p>Time:</p>
	<select name="time1">
		<%
			String hour = "";
			for (int i = 1; i <= 12; i++) {
				if (i < 10)
					hour = "0" + i;
				else
					hour = i + "";
		%>
		<option <%if (hour.equals(hourI))
					out.print("selected");%>
			value="<%=hour%>"><%=StringEscapeUtils.escapeHtml("" + (hour))%></option>
		<%
			}
		%>
	</select>:<select name="time2">
		<%
			String min = "";
			for (int i = 0; i < 60; i += 5) {
				if (i < 10)
					min = "0" + i;
				else
					min = i + "";
		%>
		<option <%if (min.equals(minuteI))
					out.print("selected");%>
			value="<%=min%>"><%=StringEscapeUtils.escapeHtml("" + (min))%></option>
		<%
			}
		%>
	</select> <select name="time3">
		<option <%if ("AM".equals(tod))
				out.print("selected");%> value="AM">AM</option>
		<option <%if ("PM".equals(tod))
				out.print("selected");%> value="PM">PM</option>
	</select>
	<p>Comment:</p>
	<textarea name="comment" cols="100" rows="10"><%=StringEscapeUtils.escapeHtml("" + (comment))%></textarea>
	<br /> <br /> <input type="submit" name="request" value="Request" />
</form>
<%
	
%>

<%@include file="/footer.jsp"%>
