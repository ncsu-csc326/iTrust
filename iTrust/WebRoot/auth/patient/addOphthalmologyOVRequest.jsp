<%@page import="edu.ncsu.csc.itrust.beans.OphthalmologyScheduleOVRecordBean"%>
<%@page import="edu.ncsu.csc.itrust.action.AddOphthalmologyScheduleOVAction"%>
<%@page import="java.text.ParseException"%>
<%@page import="java.sql.Timestamp"%>
<%@page import="java.util.Date"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="edu.ncsu.csc.itrust.dao.mysql.PersonnelDAO"%>
<%@page import="edu.ncsu.csc.itrust.action.ViewPersonnelAction"%>
<%@page import="edu.ncsu.csc.itrust.beans.PersonnelBean"%>
<%@page errorPage="/auth/exceptionHandler.jsp"%>
<%@page import="java.util.List"%>

<%@include file="/global.jsp"%>

<%
	pageTitle = "iTrust - Add Ophthalmology Office Visit Request";
%>
<%@include file="/header.jsp"%>
<%
	ViewPersonnelAction personnelAction = new ViewPersonnelAction(prodDAO, loggedInMID);
	AddOphthalmologyScheduleOVAction addAction = new AddOphthalmologyScheduleOVAction(prodDAO,loggedInMID);
	List<PersonnelBean> personnel = addAction.getAllOphthalmologyPersonnel();
	
	String msg = "";
	long hcpid = 0L;
	String comment = "";
	String date = "";
	String hourI = "";
	String minuteI = "";
	String tod = "";
	if (request.getParameter("request") != null) {
		OphthalmologyScheduleOVRecordBean bean = new OphthalmologyScheduleOVRecordBean();
		bean.setPatientmid(loggedInMID);
		long doctormid = Long.parseLong(request.getParameter("lhcp"));
		bean.setDoctormid(doctormid);
		PersonnelBean doctor = personnelAction.getPersonnel("" + doctormid);
		bean.setDocFirstName(doctor.getFirstName());
		bean.setDocLastName(doctor.getLastName());
		bean.setComment(request.getParameter("comment"));
		SimpleDateFormat frmt = new SimpleDateFormat("MM/dd/yyyy hh:mm a");
		date = request.getParameter("startDate");
		date = date.trim();
		hourI = request.getParameter("time1");
		minuteI = request.getParameter("time2");
		tod = request.getParameter("time3");
		bean.setPending(true);
		try {
			if(date.length() == 10){
				Date d = frmt.parse(date + " " + hourI + ":" + minuteI
						+ " " + tod);
				bean.setDate(new Timestamp(d.getTime()));
			}else{
				msg = "ERROR: Date must by in the format: MM/dd/yyyy";
			}
		} catch (ParseException e) {
			msg = "ERROR: Date must by in the format: MM/dd/yyyy";
		}
		if(!msg.contains("ERROR")){
			addAction.addOphthalmologyOV(bean);
			msg = "Your Ophthalmology Office Visit Request has been saved and is pending.";
		}	
	}
%>
<h1>Request an Ophthalmology Office Visit</h1>
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
<form action="addOphthalmologyOVRequest.jsp" method="post">
	<p>HCP:</p>
	<select name="lhcp">
		<%
			for (PersonnelBean doctor : personnel) {
		%><option
			<%if (doctor.getMID() == hcpid)
					out.println("selected");%>
			value="<%=doctor.getMID()%>"><%=doctor.getFullName()%></option>
		<%
			}
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
<%@include file="/footer.jsp"%>
