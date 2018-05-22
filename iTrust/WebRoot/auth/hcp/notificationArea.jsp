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
<%@page import="edu.ncsu.csc.itrust.action.ViewApptRequestsAction"%>
<%@page import="edu.ncsu.csc.itrust.model.old.beans.ApptRequestBean"%>

<%
ViewMyMessagesAction messageAction = new ViewMyMessagesAction(prodDAO, loggedInMID.longValue());
ViewMyApptsAction apptAction = new ViewMyApptsAction(prodDAO, loggedInMID.longValue());
List <ApptBean> appointments = apptAction.getAppointments(loggedInMID.longValue());
ViewMyRemoteMonitoringListAction remoteMonitoringAction = new ViewMyRemoteMonitoringListAction(prodDAO, loggedInMID.longValue());
List<RemoteMonitoringDataBean> remoteData = remoteMonitoringAction.getPatientsData();
ViewApptRequestsAction reqAction = new ViewApptRequestsAction(loggedInMID.longValue(), prodDAO);
List<ApptRequestBean> reqs = reqAction.getApptRequests();



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

<div id="notificationArea">
    <div class="page-header"><h1>Notifications</h1></div>
	<div class="col-sm-4">
	<div class="panel panel-primary panel-notification">
	<div class="panel-heading"><h3 class="panel-title">Message Notification</h3></div>
	<div class="panel-body">
<!-- Begin Message Notification -->    
    <ul>
<% if(reqAction.getNumRequests(reqs) == 0) { %>
	<li><img class="icon" src="/iTrust/image/icons/inboxEmpty.png" style="border:0px;">
	No appointment requests.</li>
<%	} else { %>    
	<li><a href="/iTrust/auth/hcp-patient/messageInbox.jsp">
	<img class="icon" src="/iTrust/image/icons/inboxUnread.png" style="border:0px;"></a>
    <a href="/iTrust/auth/hcp-patient/messageInbox.jsp"> 
<%= StringEscapeUtils.escapeHtml("" + (reqAction.getNumRequests(reqs))) %></a>
	Appointment requests.
	</li>
<%	} %>
<% if(messageAction.getUnreadCount() == 0) { %>
	<li><img class="icon" src="/iTrust/image/icons/inboxEmpty.png" style="border:0px;">
	No unread messages.</li>
<%	} else { %>    
	<li><a href="/iTrust/auth/hcp-patient/messageInbox.jsp">
	<img class="icon" src="/iTrust/image/icons/inboxUnread.png" style="border:0px;"></a>
    <a href="/iTrust/auth/hcp-patient/messageInbox.jsp">
    <%= StringEscapeUtils.escapeHtml("" + (messageAction.getUnreadCount())) %></a>
    Unread messages.
    </li>
<%	} %>


    </ul>
    </div>
    </div>
    </div>
<!-- End Message Notification -->

<!-- Begin Today's Appointment Information -->
	<div class="col-sm-4">
	<div class="panel panel-primary panel-notification">
	<div class="panel-heading"><h3 class="panel-title">Today's Appointments</h3></div>
	<div class="panel-body">
	<ul>
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
				<li>
					<a href="/iTrust/auth/hcp/viewAppt.jsp?apt=<%= StringEscapeUtils.escapeHtml("" + appt.getApptID()) %>">
						<img class="icon" style="border:0px;" src="/iTrust/image/icons/appointment.png" /></a>
					<a href="/iTrust/auth/hcp/viewAppt.jsp?apt=<%= StringEscapeUtils.escapeHtml("" + appt.getApptID()) %>">
						<%= StringEscapeUtils.escapeHtml("" + (dateString )) %>
					</a> - <%= StringEscapeUtils.escapeHtml("" + (appt.getApptType() )) %>
				</li>
				<%
			}
			index++;
		}
%>
	</ul>
	</div>
	</div>
	</div>
<!-- End Today's Appointment Information -->

<!-- Being Telemedicine Reports Information -->	
	<div class="col-sm-4">
	<div class="panel panel-primary panel-notification">
	<div class="panel-heading"><h3 class="panel-title">Telemedicine Reports</h3></div>
	<div class="panel-body">
	<ul>
	<li>
	   <a href="/iTrust/auth/hcp/monitorPatients.jsp">
	   <img class="icon" style="border:0px" src="/iTrust/image/icons/telemedicine.png"></a>
	   <a href="/iTrust/auth/hcp/monitorPatients.jsp">
	   <%= StringEscapeUtils.escapeHtml("" + (bucketPhysiologic )) %> physiological status reports</a></li>
	<li>
	   <a href="/iTrust/auth/hcp/monitorPatients.jsp">
	   <img class="icon" style="border:0px" src="/iTrust/image/icons/physicalActivity.png"></a>
	   <a href="/iTrust/auth/hcp/monitorPatients.jsp">
	   <%= StringEscapeUtils.escapeHtml("" + (bucketWeightPedometer )) %> weight/pedometer status reports</a></li>
	</ul>
	</div>
	</div>	
	</div>
<!-- End Telemedicine Reports Information -->
  </div>
</div>