<%@page import="edu.ncsu.csc.itrust.dao.mysql.PatientDAO"%>
<%@page import="edu.ncsu.csc.itrust.dao.mysql.BillingDAO"%>
<%@page import="edu.ncsu.csc.itrust.action.ViewMyMessagesAction"%>
<%@page import="edu.ncsu.csc.itrust.action.ViewMyBillingAction"%>
<%@page import="edu.ncsu.csc.itrust.action.ViewMyApptsAction"%>
<%@page import="edu.ncsu.csc.itrust.action.ViewMyLabProceduresAction"%>
<%@page import="edu.ncsu.csc.itrust.action.DeclareHCPAction"%>
<%@page import="edu.ncsu.csc.itrust.action.ViewMyRemoteMonitoringListAction"%>
<%@page import="edu.ncsu.csc.itrust.action.EditRepresentativesAction"%>
<%@page import="edu.ncsu.csc.itrust.beans.MessageBean"%>
<%@page import="edu.ncsu.csc.itrust.beans.BillingBean"%>
<%@page import="edu.ncsu.csc.itrust.beans.ApptBean"%>
<%@page import="edu.ncsu.csc.itrust.beans.PersonnelBean"%>
<%@page import="edu.ncsu.csc.itrust.beans.RemoteMonitoringDataBean"%>
<%@page import="edu.ncsu.csc.itrust.beans.PatientBean"%>
<%@page import="edu.ncsu.csc.itrust.action.ViewPatientReferralsAction"%>
<%@page import="edu.ncsu.csc.itrust.beans.ReferralBean"%>
<%@page import="edu.ncsu.csc.itrust.beans.HospitalBean"%>
<%@page import="edu.ncsu.csc.itrust.dao.mysql.WardDAO"%>
<%@page import="java.sql.Timestamp"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Date"%>



<%
ViewMyMessagesAction messageAction = new ViewMyMessagesAction(prodDAO, loggedInMID.longValue());
ViewMyBillingAction billingAction = new ViewMyBillingAction(prodDAO, loggedInMID.longValue());
ViewMyApptsAction apptAction = new ViewMyApptsAction(prodDAO, loggedInMID.longValue());
ViewMyLabProceduresAction labProcsAction = new ViewMyLabProceduresAction(prodDAO, loggedInMID.longValue());
ViewPatientReferralsAction referralAction = new ViewPatientReferralsAction(prodDAO, loggedInMID);
List <ApptBean> appointments = apptAction.getAppointments(loggedInMID.longValue());
DeclareHCPAction hcpAction = new DeclareHCPAction(prodDAO, loggedInMID.longValue());
List<PersonnelBean> hcps = hcpAction.getDeclaredHCPS();
ViewMyRemoteMonitoringListAction remoteMonitoringAction = new ViewMyRemoteMonitoringListAction(prodDAO, loggedInMID.longValue());
List<RemoteMonitoringDataBean> remoteData = remoteMonitoringAction.getPatientsData();
EditRepresentativesAction representativeAction = new EditRepresentativesAction(prodDAO, loggedInMID.longValue(), String.valueOf(loggedInMID.longValue()));
List<PatientBean> representees = representativeAction.getRepresented(loggedInMID.longValue());
representees.add(0, new PatientDAO(prodDAO).getPatient(loggedInMID.longValue()));
loggingAction.logEvent(TransactionType.NOTIFICATIONS_VIEW, loggedInMID, 0, "");

int labProcsCount = labProcsAction.getUnviewedCount();
int referralCount = referralAction.getReferralsForPatientUnread();
Date rightNow = new Date();
List<BillingBean> unpaidBills = billingAction.getMyUnpaidBills();
BillingDAO bills = prodDAO.getBillingDAO();
%>
<div class="col-sm-12">
<div id="notificationArea">
    <div class="page-header"><h1>Notifications</h1></div>
	<div class="col-sm-6">
	<div class="panel panel-primary panel-notification">
	<div class="panel-heading"><h3 class="panel-title">Message Notification</h3></div>
	<div class="panel-body">
<!-- Begin Message Notification -->    
    <% WardDAO wardDAO = new WardDAO(prodDAO);
    HospitalBean hospital = wardDAO.getHospitalByPatientID(loggedInMID.longValue());
    %>
    <ul style="list-style-type: none;">
    
<% if(messageAction.getUnreadCount() == 0) { %>
	<li>
	   <img class="icon" src="/iTrust/image/icons/inboxEmpty.png" style="border:0px;">
        No unread messages.
	</li>
<% } else { %>
	<li>
	   <a href="/iTrust/auth/hcp-patient/messageInbox.jsp">
	     <img class="icon" src="/iTrust/image/icons/inboxUnread.png" style="border:0px;"></a>
       <a href="/iTrust/auth/hcp-patient/messageInbox.jsp">
	     <%= StringEscapeUtils.escapeHtml("" + (messageAction.getUnreadCount())) %></a>
	   Unread message(s).
	</li>
<% } %>

<% if(labProcsCount == 0) { %>
	<li>
	   <img class="icon" src="/iTrust/image/icons/beaker.png" style="border:0px; margin: 0 0 0 -2px;">
	   No new completed lab procedures.
	</li>
<%	} else { %>
		<li>
		  <a href="viewMyLabProcedures.jsp"><img class="icon" src="/iTrust/image/icons/beaker.png" style="border:0px; margin: 0 0 0 -2px;"></a>
		  <a href="viewMyLabProcedures.jsp">
		    <%= StringEscapeUtils.escapeHtml("" + labProcsCount) %></a>
		  new completed lab procedure<%= ((labProcsCount > 1) ? "s" : "") %>.
		</li>
 <% } %>
 <%if(referralCount == 0) { %>
 	<li>
	   <img class="icon" src="/iTrust/image/icons/icon-doctor.png" style="border:0px;">
        No new referrals.
	</li>
<%} else { %>
     <li>
		  <a href="viewPatientReferrals.jsp"><img class="icon" src="/iTrust/image/icons/icon-doctor.png" style="border:0px; margin: 0 0 0 ;"></a>
		  <a href="viewPatientReferrals.jsp">
		    <%= StringEscapeUtils.escapeHtml("" + referralCount) %></a>
		  new patient referral<%= ((referralCount > 1) ? "s" : "") %>.
	</li>
 <% } %>
 <!-- Begin Billing Information UC60 -->
 <%
		if(unpaidBills.size() == 0) {
			%>
			<li>
	   			<img class="icon" src="/iTrust/image/icons/dollar.png" style="border:0px;">
        		No unpaid bills.
			</li>
		<%	} else { %>
			<li>
	   			<a href="/iTrust/auth/patient/myBills.jsp">
	     		<img class="icon" src="/iTrust/image/icons/dollar.png" style="border:0px;"></a>
	     		<a href="/iTrust/auth/patient/myBills.jsp">
		    <%= StringEscapeUtils.escapeHtml("" + unpaidBills.size()) %></a> new bill<%= ((unpaidBills.size() > 1) ? "s" : "") %>.
			</li>

			 <% } %>
			 <%
	if(bills.getDeniedNum(loggedInMID.longValue()) == 0) {
			%>
			<li>
	   			<img class="icon" src="/iTrust/image/icons/approved.png" style="border:0px;">
        		No denied insurance claims.
			</li>
		<%	} else { %>
			<li>
	   			<a href="/iTrust/auth/patient/myBills.jsp">
	     		<img class="icon" src="/iTrust/image/icons/denied.png" style="border:0px;"></a>
	     		<a href="/iTrust/auth/patient/myBills.jsp">
		    <%= StringEscapeUtils.escapeHtml("" + bills.getDeniedNum(loggedInMID.longValue())) %></a> denied insurance claim<%= ((bills.getDeniedNum(loggedInMID.longValue()) > 1) ? "s" : "") %>.
			</li>

		 <% } %>
	 <%
	if(bills.getApprovedNum(loggedInMID.longValue()) == 0) {
	%>
			<li>
	   			<img class="icon" src="/iTrust/image/icons/approved.png" style="border:0px;">
        		No approved insurance claims.
			</li>
		<%	} else { %>
			<li>
	   			<a href="/iTrust/auth/patient/myBills.jsp">
	     		<img class="icon" src="/iTrust/image/icons/approved.png" style="border:0px;"></a>
	     		<a href="/iTrust/auth/patient/myBills.jsp">
		    <%= StringEscapeUtils.escapeHtml("" + bills.getApprovedNum(loggedInMID.longValue())) %></a> approved insurance claim<%= ((bills.getApprovedNum(loggedInMID.longValue()) > 1) ? "s" : "") %>.
			</li>

		 <% } %>
<!-- End Billing Information -->
    </ul>
	</div>
	</div>
	</div>
<!-- End Message Notification -->
	

<!-- Begin Telemedine Notification -->
<%
	if (remoteMonitoringAction.getMonitoringHCPs().size() > 0 && remoteData.size() <= 0) {
%>
	<div class="col-sm-6">
	<div class="panel panel-primary panel-notification">
	<div class="panel-heading"><h3 class="panel-title">Telemedicine Notifications</h3></div>
	<div class="panel-body">
		<img style="border:0px" src="/iTrust/image/icons/noTelemedicine.png" />
		You haven't entered remote monitoring information for today yet!
	</div>
	</div>
	</div>
<%
	}
%>
<!-- End Telemedicine Notification -->

<!-- Begin Designated HCP Information -->
	<div class="col-sm-6">
	<div class="panel panel-primary panel-notification">
	<div class="panel-heading"><h3 class="panel-title">Your Designated HCP</h3></div>
	<div class="panel-body">
	<%
	if(hcps.size() > 0) {
		for(PersonnelBean hcp : hcps) {
			%>
			<img class="userpic" src="/iTrust/image/user/<%= StringEscapeUtils.escapeHtml("" + (hcp.getMID() )) %>.png" alt="/iTrust/image/user/">
			<%= StringEscapeUtils.escapeHtml("" + (hcp.getFullName() )) %><br />
			<%= StringEscapeUtils.escapeHtml("" + (hcp.getPhone() )) %><br />
			<%= StringEscapeUtils.escapeHtml("" + (hcp.getEmail() )) %><br />
			<%
		}
	} else {
		%>
		You have no declared LHCP.<br />
		<%
	}
	%>
	</div>
	</div>
	</div>
	</div>
	</div>
<!-- End Designated HCP Information -->

<!-- Begin Upcoming Appointments Information -->
	<div class="col-sm-12">	
	<div class="col-sm-6 panel-group" id="appt-accord">
	<div class="panel panel-primary panel-notification">
	<%		
	int patientIndex = 0;
	for(PatientBean patientDataBean : representees) {
		List <ApptBean> patientAppointments = apptAction.getAppointments(patientDataBean.getMID());
		int index = 0;
		boolean isFirstBean = true;
		for(ApptBean appt : patientAppointments) {
			if(appt.getDate().after(rightNow)) {
				if(isFirstBean) {
					isFirstBean = false;
					if(patientIndex == 0){
						%>
							<div class="panel-heading accordion-heading">
							<a class="accordion-toggle" data-toggle="collapse" data-parent="#appt-accord" href="#appt-collapse-<%=StringEscapeUtils.escapeHtml("" + patientIndex) %>">
							<h3 class="panel-title" style="color:#ffffff;">Your Upcoming Appointments<span class="glyphicon glyphicon-chevron-down" style="font-color:#fff; float:right; font-size:11px;"></span></h3>
							</div>
							<div class="accordion-body collapse in" id="appt-collapse-<%=StringEscapeUtils.escapeHtml("" + patientIndex) %>">
							<div class="panel-body">
							<ul>
						<%
					} else {
						%>
							<div class="panel-heading">
							<a class="accordion-toggle" data-toggle="collapse" data-parent="#appt-accord" href="#appt-collapse-<%=StringEscapeUtils.escapeHtml("" + patientIndex) %>">
							<h3 class="panel-title" style="color:#ffffff;"><%= StringEscapeUtils.escapeHtml(patientDataBean.getFullName() + "'s") %> Upcoming Appointments<span class="glyphicon glyphicon-chevron-down" style="font-color:#fff; float:right; font-size:11px;"></span>
							</h3>
							</a>
							</div>
							<div class="accordion-body collapse" id="appt-collapse-<%=StringEscapeUtils.escapeHtml("" + patientIndex) %>">
							<div class="panel-body">
							<ul>
						<%
					}
				}
				Timestamp apptDate = appt.getDate();
				SimpleDateFormat dateFormat = new SimpleDateFormat();
				dateFormat.applyPattern("MM/dd/yyyy");
				String dateString = dateFormat.format(apptDate);
				%>
				<li>
					<a href="/iTrust/auth/patient/viewAppt.jsp?apt=<%= StringEscapeUtils.escapeHtml("" + appt.getApptID()) %>">
						<img class="icon" style="border:0px" src="/iTrust/image/icons/appointment.png" /></a>
					<a href="/iTrust/auth/patient/viewAppt.jsp?apt=<%= StringEscapeUtils.escapeHtml("" + appt.getApptID()) %>">
						<%= StringEscapeUtils.escapeHtml("" + (dateString )) %>
					</a> - <%= StringEscapeUtils.escapeHtml("" + (appt.getApptType() )) %>
			    </li>
				<%
			}
			index++;
		}
		patientIndex++;
		if(!isFirstBean){
			%>
			</ul>
			</div>
			</div>
			<%
		}
	}
	%>
	</div>
	</div>
	<%
	
	%>
<!-- End Upcoming Appointment Information -->

<!--  Begin Office Visit Survey Information -->	
		<div class="col-sm-6">
	<div class="panel panel-primary panel-notification">
	<div class="panel-heading"><h3 class="panel-title">Office Visit Surverys</h3></div>
	<div class="panel-body">
	<ul>
	<%
	if (0 != surList.size()) {
	%>
	<%
		for (OfficeVisitBean ov : surList) {
			if (!surveyAction.isSurveyCompleted(ov.getID())){
	%>
				<li>
				<a href="survey.jsp?ovID=<%= StringEscapeUtils.escapeHtml("" + (ov.getVisitID())) %>&ovDate=<%= StringEscapeUtils.escapeHtml("" + (ov.getVisitDateStr())) %>">
					<img class="icon" style="border:0px" src="/iTrust/image/icons/survey.png" /></a>
				<a href="survey.jsp?ovID=<%= StringEscapeUtils.escapeHtml("" + (ov.getVisitID())) %>&ovDate=<%= StringEscapeUtils.escapeHtml("" + (ov.getVisitDateStr())) %>">
					Survey
				</a> for <%= StringEscapeUtils.escapeHtml("" + (ov.getVisitDateStr())) %>
				</li>
	<%
			}
		}
	%>
	<%
	}
	else {
	%>
		<i>No Unfinished Surveys</i>
	<%
	}
	%>
	</ul>
	</div>
	</div>
<!-- End Office Visit Survey Information -->

  </div>