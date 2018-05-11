<%@page import="edu.ncsu.csc.itrust.action.ActivityFeedAction"%>
<%@page import="java.util.List"%>
<%@page import="edu.ncsu.csc.itrust.action.EditPatientAction"%>
<%@page import="edu.ncsu.csc.itrust.action.ViewPersonnelAction"%>
<%@page import="edu.ncsu.csc.itrust.action.ViewPatientAction"%>
<%@page import="edu.ncsu.csc.itrust.beans.TransactionBean"%>
<%@page import="edu.ncsu.csc.itrust.exception.FormValidationException"%>
<%@page import="edu.ncsu.csc.itrust.enums.TransactionType"%>
<%@page import="edu.ncsu.csc.itrust.beans.PersonnelBean"%>
<%@page import="edu.ncsu.csc.itrust.dao.mysql.PersonnelDAO"%>
<%@page import="edu.ncsu.csc.itrust.beans.PatientBean"%>
<%@page import="edu.ncsu.csc.itrust.dao.mysql.PatientDAO"%>
<%@page import="edu.ncsu.csc.itrust.exception.ITrustException"%>
<%@page errorPage="/auth/exceptionHandler.jsp" %>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.Date"%>

<%
	loggingAction.logEvent(TransactionType.ACTIVITY_FEED_VIEW, loggedInMID.longValue(), 0, "");
		
	pageTitle = "iTrust - View My Access Log";
	ViewPatientAction vpa = new ViewPatientAction(prodDAO, loggedInMID, "" + loggedInMID);
	List<PatientBean> viewable = vpa.getViewablePatients();
	List<TransactionBean> accesses;
	Date viewTime = new Date();

	int pageNumber = 1;
	
	if (request.getParameter("date") != null && request.getParameter("page") != null) {
		try {
	viewTime = new Date(Long.parseLong(request.getParameter("date")));
		} catch (NumberFormatException e) {
	viewTime = new Date();
		}
		try {
	pageNumber = Integer.parseInt(request.getParameter("page"));
		} catch (NumberFormatException e) {
	pageNumber = 1;
		}
	}
	
	ArrayList<PersonnelBean> hcpList = new ArrayList<PersonnelBean>(); 
	session.setAttribute("personnelList", hcpList);
	
%>
<div class="col-sm-12 panel-group" id="act-accord">
<div class="panel panel-primary panel-notification">	
<%
	for(int k = 0; k < viewable.size(); k++) {
		ActivityFeedAction action = new ActivityFeedAction(prodDAO, viewable.get(k).getMID());
		accesses = action.getTransactions(viewTime, pageNumber);
		boolean more = false;
		if(accesses.size() == 21) {
			more = true;
		}
%>
<div class="panel-heading accordion-heading">
    <a class="accordion-toggle" data-toggle="collapse" data-parent="#act-accord" href="#collapse-<%=StringEscapeUtils.escapeHtml("" + k) %>">
    <h3 class="panel-title" style="color:#ffffff;">
    <% if(k == 0) {%>
    Your Activity Feed<span class="glyphicon glyphicon-chevron-down" style="font-color:#fff; float:right; font-size:11px;"></span></h3>
    <%} else { %>
    Activity Feed for <%= StringEscapeUtils.escapeHtml(viewable.get(k).getFullName()) %><span class="glyphicon glyphicon-chevron-down" style="font-color:#fff; float:right; font-size:11px;"></span></h3>
    <%} %>
    </a>
</div>
<%
if(k == 0){
%>
      <div class="accordion-body collapse in" id="collapse-<%=StringEscapeUtils.escapeHtml("" + k) %>">
<%} else { %>
	<div class="accordion-body collapse" id="collapse-<%=StringEscapeUtils.escapeHtml("" + k) %>">
<%} %>
        <div class="panel-body">
          <ul>
<%
	if (pageNumber > 1) {
%>
	<li style="width: 100%;">
		<span style="float: left;"><a href="home.jsp?page=1">Refresh</a></span>
		<%
			if(accesses.size() == pageNumber * 20 + 1) {
		%>
			<span style="float: right;"><a href="home.jsp?date=<%=StringEscapeUtils.escapeHtml("" + ( viewTime.getTime()))%>&page=<%=StringEscapeUtils.escapeHtml("" + (pageNumber + 1 ))%>">Older Activities</a></span>
		<%
			}
		%>
		<br />
	</li>
<%
	}
	ViewPersonnelAction personnels = new ViewPersonnelAction(prodDAO, loggedInMID);
	EditPatientAction patients;
	
	String name = "";
	int zebraStripes = 0;
	int hcpCount = 0;
	
	if (pageNumber == 1) {
		if(accesses.isEmpty()) {
%>
			<li>
				<%=StringEscapeUtils.escapeHtml("" + ( "No recent activity" ))%>
			</li>
			<%
				} else {
									/* getTransactions() returns one more than necessary */
				for(int i=0; i < (more ? accesses.size() - 1 : accesses.size()); i++) { 
					TransactionBean t = accesses.get(i);
					
					try {
						PersonnelBean hcp = personnels.getPersonnel(t.getLoggedInMID() + "");
						if (hcp != null) {
							name = "<a href=\"/iTrust/auth/viewPersonnel.jsp?personnel=" + hcpCount++ + "\">" +
								StringEscapeUtils.escapeHtml(hcp.getFullName()) + "</a>";
							hcpList.add(hcp);
						}
					} catch (ITrustException e) {
						patients = new EditPatientAction(prodDAO, loggedInMID, t.getLoggedInMID() + "");
						PatientBean eventPatient = patients.getPatient();
						name = eventPatient.getFullName();
						StringEscapeUtils.escapeHtml(name);
					}
			%>
				<li>
					<%=action.getMessageAsSentence(name, t.getTimeLogged(), t.getTransactionType())%>
				</li>
	<%
		}
			}
			if(accesses.size() == 21) {
	%>
		<a href="home.jsp?date=<%=StringEscapeUtils.escapeHtml("" + ( viewTime.getTime() ))%>&page=2">Older Activities</a>
	<%
		}
		} else {
			for(int i=0; i < (more ? accesses.size() - 1 : accesses.size()); i++) { 
		TransactionBean t = accesses.get(i);
		try {
			PersonnelBean hcp = personnels.getPersonnel(t.getLoggedInMID() + "");
			if (hcp != null) {
				name = "<a href=\"/iTrust/auth/viewPersonnel.jsp?personnel=" + hcpCount++ + "\">" +
					StringEscapeUtils.escapeHtml(hcp.getFullName()) + "</a>";
				hcpList.add(hcp);
			}
		} catch (ITrustException e) {
			patients = new EditPatientAction(prodDAO, loggedInMID, t.getLoggedInMID() + "");
			PatientBean eventPatient = patients.getPatient();
			name = StringEscapeUtils.escapeHtml(eventPatient.getFullName());
		}
	%>
			<li>
				<%= action.getMessageAsSentence(name, t.getTimeLogged(), t.getTransactionType()) %>
			</li>
	<%
		}
	%>
	<li style="width: 100%;">
		<span style="float: left;"><a href="home.jsp?page=1">Refresh</a></span>
	<%
	if(accesses.size() == pageNumber * 20 + 1) {
	%>
		<span style="float: right;"><a href="home.jsp?date=<%= StringEscapeUtils.escapeHtml("" + ( viewTime.getTime())) %>&page=<%= StringEscapeUtils.escapeHtml("" + (pageNumber + 1 )) %>">Older Activities</a></span>
	<%
	}
	%>
	</li>
<%
	}
%>
</ul>
</div>
</div>
<%
	}
%>
</div>
</div>
</div>
<!-- close the panel div -->
</div>
