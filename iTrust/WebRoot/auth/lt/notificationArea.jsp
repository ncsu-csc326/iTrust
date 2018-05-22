<%@page import="edu.ncsu.csc.itrust.action.ViewMyMessagesAction"%>
<%@page import="edu.ncsu.csc.itrust.action.ViewMyApptsAction"%>
<%@page import="edu.ncsu.csc.itrust.action.DeclareHCPAction"%>
<%@page import="edu.ncsu.csc.itrust.action.ViewMyRemoteMonitoringListAction"%>
<%@page import="edu.ncsu.csc.itrust.action.EditRepresentativesAction"%>
<%@page import="edu.ncsu.csc.itrust.model.old.beans.MessageBean"%>
<%@page import="edu.ncsu.csc.itrust.model.old.beans.ApptBean"%>
<%@page import="edu.ncsu.csc.itrust.model.old.beans.PersonnelBean"%>
<%@page import="edu.ncsu.csc.itrust.model.old.beans.RemoteMonitoringDataBean"%>
<%@page import="edu.ncsu.csc.itrust.model.old.beans.PatientBean"%>
<%@page import="java.sql.Timestamp"%>
<%@page import="java.text.DateFormat"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Date"%>
<%@page import="java.util.ArrayList"%>

<%
ViewMyMessagesAction messageAction = new ViewMyMessagesAction(prodDAO, loggedInMID.longValue());
ViewMyApptsAction apptAction = new ViewMyApptsAction(prodDAO, loggedInMID.longValue());
List <ApptBean> appointments = apptAction.getAppointments(loggedInMID.longValue());
ViewMyRemoteMonitoringListAction remoteMonitoringAction = new ViewMyRemoteMonitoringListAction(prodDAO, loggedInMID.longValue());
List<RemoteMonitoringDataBean> remoteData = remoteMonitoringAction.getPatientsData();

int bucketPhysiologic = 0;
int bucketWeightPedometer = 0;

for(RemoteMonitoringDataBean datum: remoteData) {
	if (datum.getSystolicBloodPressure() > 0 || datum.getDiastolicBloodPressure() > 0 || datum.getGlucoseLevel() > 0) {
		bucketPhysiologic++;
	}
	if (datum.getWeight() > 0 || datum.getPedometerReading() > 0){
		bucketWeightPedometer++;
	}
}

Date rightNow = new Date();
%>

<div id="notificationArea" style="float:right; width:40%;background-color:white;">
  <fieldset>
    <legend>Notifications</legend>
<!-- Begin Message Notification -->    
    <div class="contentBlock" style="margin:0.3em;">
<%
 	if(messageAction.getUnreadCount() == 0) 
	{
%>
	<img src="/iTrust/image/icons/inboxEmpty.png" style="border:0px;">
	No unread messages.
<%
	} else
	{
%>    
	<a href="/iTrust/auth/hcp/messageInbox.jsp">
	<img src="/iTrust/image/icons/inboxUnread.png" style="border:0px;"></a>
    <a href="/iTrust/auth/hcp/messageInbox.jsp">
    <%= StringEscapeUtils.escapeHtml("" + (messageAction.getUnreadCount())) %></a>
    Unread messages.
<%
	}
%>
    </div>
<!-- End Message Notification -->

<!-- Begin Today's Appointment Information -->
	<span class="subheading" style="font-weight: bold;">Today's Appointments</span><br />
	<div class="contentBlock" style="margin:0.3em;">
	<%
	session.setAttribute("appts", appointments);
	int index = 0;
	DateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
		for(ApptBean appt : appointments) {
			Date beanDate = new Date(appt.getDate().getTime());
			if (sdf.format(rightNow).equals(sdf.format(beanDate))) {
				Timestamp apptDate = appt.getDate();
				SimpleDateFormat dateFormat = new SimpleDateFormat();
				dateFormat.applyPattern("hh:mmaa");
				String dateString = dateFormat.format(apptDate);
				%>
					<a href="/iTrust/auth/hcp/viewAppt.jsp?apt=<%= StringEscapeUtils.escapeHtml("" + appt.getApptID()) %>">
						<img style="border:0px;" src="/iTrust/image/icons/appointment.png" /></a>
					<a href="/iTrust/auth/hcp/viewAppt.jsp?apt=<%= StringEscapeUtils.escapeHtml("" + appt.getApptID()) %>">
						<%= StringEscapeUtils.escapeHtml("" + (dateString )) %>
					</a> - <%= StringEscapeUtils.escapeHtml("" + (appt.getApptType() )) %>
				<br />
				<%
			}
			index++;
		}
%>
	</div>
<!-- End Today's Appointment Information -->

<!-- Being Telemedicine Reports Information -->	
	<span class="subheading" style="font-weight: bold;">Today's Telemedicine Reports</span><br />
	<div class="block" style="margin:0.3em;">
	<span><a href="/iTrust/auth/hcp/monitorPatients.jsp">
	<img style="border:0px" src="/iTrust/image/icons/telemedicine.png">
	<%= StringEscapeUtils.escapeHtml("" + (bucketPhysiologic )) %> physiological status reports</span></a><br />
	<span><a href="/iTrust/auth/hcp/monitorPatients.jsp">
	<img style="border:0px" src="/iTrust/image/icons/physicalActivity.png">
	<%= StringEscapeUtils.escapeHtml("" + (bucketWeightPedometer )) %> weight/pedometer status reports</span></a>
	</div>	
<!-- End Telemedicine Reports Information -->
  </fieldset>
</div>