<%@page import="edu.ncsu.csc.itrust.action.ViewPatientReferralsAction"%>
<%@page import="edu.ncsu.csc.itrust.action.ViewReportAction"%>
<%@page import="edu.ncsu.csc.itrust.action.SendMessageAction"%>
<%@page import="edu.ncsu.csc.itrust.action.ViewMyMessagesAction"%>
<%@page import="edu.ncsu.csc.itrust.beans.ReferralBean"%>
<%@page import="edu.ncsu.csc.itrust.beans.PersonnelBean"%>
<%@page import="edu.ncsu.csc.itrust.beans.MessageBean"%>
<%@page import="edu.ncsu.csc.itrust.beans.OfficeVisitBean"%>
<%@page import="edu.ncsu.csc.itrust.exception.FormValidationException"%>
<%@page import="edu.ncsu.csc.itrust.enums.SortDirection"%>
<%@page import="edu.ncsu.csc.itrust.beans.VerboseReferralBean"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.List"%>
<%@page import="java.text.DateFormat"%>
<%@page import="java.util.Date" %>
<%@page errorPage="/auth/exceptionHandler.jsp" %>
<%@taglib prefix="itrust" uri="/WEB-INF/tags.tld" %>

<%@include file="/global.jsp"%>

<%
    pageTitle = "iTrust - View Referrals";
%>

<%@include file="/header.jsp"%>

<%

ViewPatientReferralsAction action = new ViewPatientReferralsAction(prodDAO, loggedInMID);

ViewReportAction hcp = new ViewReportAction(prodDAO, loggedInMID);

String sortBy = request.getParameter("sortBy"); 
if (sortBy==null)
	sortBy = "priority";
String sortOrderString = request.getParameter("sortOrder"); 
if (sortOrderString==null)
	sortOrderString = "ascending";
SortDirection sortOrder = SortDirection.parse(sortOrderString);

String submittedFormName = request.getParameter("formName");
%>
<script type="text/javascript">
    function showReferral(value) {
        document.getElementById("referalID").value = value;
        document.forms["patientReferralForm"].submit();
    }
    
    function sendMessage(value) {
        document.getElementById("sendMessage").value = value;
        document.forms["sendMessageForm"].submit();
    }
    
</script>


<form action="viewPatientReferrals.jsp" method="post" id="patientReferralForm">
<input type="hidden" name="formName" value="patientReferralForm" />
<input type="hidden" id="referalID" name="referalID" value="" />
</form>

<form action="viewPatientReferrals.jsp" method="post" id="sendMessageForm">
<input type="hidden" name="formName" value="sendMessageForm" />
<input type="hidden" id="sendMessage" name="sendMessage" value="" />
<input type="hidden" id="sendMessageBtn" name="sendMessageBtn" value="" />
</form>


<%
	SendMessageAction messageAction = new SendMessageAction(prodDAO, loggedInMID.longValue());
	ViewMyMessagesAction myMesssages = new ViewMyMessagesAction(prodDAO, loggedInMID.longValue());
	if (request.getParameter("sendMessageBtn") != null && request.getParameter("sendMessageBtn").equals("Send")) {
		try {
		MessageBean message = new MessageBean();
		message.setFrom(loggedInMID.longValue());
		message.setTo(Long.parseLong(request.getParameter("hcp")));
		message.setBody(request.getParameter("messageBody"));
		message.setSubject(request.getParameter("subject"));
		message.setRead(0);
		messageAction.sendMessage(message);
		
		loggingAction.logEvent(TransactionType.MESSAGE_SEND, message.getFrom(), message.getTo() , "");

		MessageBean mBean = myMesssages.getAllMySentMessagesNameDescending().get(0);
		action.setReferralMessage(mBean.getMessageId(),Long.parseLong(request.getParameter("sendMessage")));
		
		%> <div align=center><span class="iTrustMessage">Your message has successfully been sent!</span></div><%
		} catch (FormValidationException e){
			%>
			<div align=center><span class="iTrustError"><%=StringEscapeUtils.escapeHtml(e.getMessage())%></span></div>
			<%
		}
	}

if ("sendMessageForm".equals(submittedFormName)){
	ReferralBean referralMessage = action.getReferralByID(Integer.parseInt(request.getParameter("sendMessage")));
	PersonnelBean receivingHCP = hcp.getPersonnel(referralMessage.getReceiverID());

%>
<form action="viewPatientReferrals.jsp" method="post" id="sendMessageForm2">

	<h4>To <%= StringEscapeUtils.escapeHtml("" + receivingHCP.getFullName()) %></h4>
	<input type="hidden" id="sendMessage" name="sendMessage" value="<%=referralMessage.getId() %>" />
	<input name="hcp" type="hidden" value="<%= referralMessage.getReceiverID() %>"/>
	<span>Subject: </span><input type="text" name="subject" size="50" value="Set Up Appointment for <%=hcp.getPatient(referralMessage.getPatientID()).getFullName() %>"/><br /><br />
	<span>Message: </span><br />
	<textarea name="messageBody" cols="100" rows="10"></textarea><br />
	<br />
	<input type="submit" value="Send" name="sendMessageBtn"/>

</form>

<%} else if("patientReferralForm".equals(submittedFormName)){

ReferralBean referralByID = action.getReferralByID(Integer.parseInt(request.getParameter("referalID")));
PersonnelBean sendingByID = hcp.getPersonnel(referralByID.getSenderID());
PersonnelBean receivingByID = hcp.getPersonnel(referralByID.getReceiverID());


loggingAction.logEvent(TransactionType.CONSULTATION_REFERRAL_VIEW, loggedInMID, 0, "The Patient has viewed referral: "+referralByID.getId());


//The patient has viewed this referral
referralByID.setViewedByPatient(true);
action.updateReferral(referralByID);

OfficeVisitBean ovBean = action.getOVDate(referralByID.getOvid());


%>
<table class="fTable" align="center" id="patientViewingReferral" style="margin:0 0 20px 0;">
    <tr>
        <th colspan="7">Patient Referral Complete Information</th>
    </tr>

    <tr class="subHeader">
    	<td>Priority</td>
    	<td>Sending HCP &#40;specialty&#41;</td>
        <td>Receiving HCP &#40;specialty&#41;</td>
        <td>Office Visit Date</td>
        <td>Notes</td>
        <td>Time Generated</td>
        <td>Send this HCP a Message</td>
    </tr>
   	<tr>
            <td style="text-align: center">
                <%= Long.toString(referralByID.getPriority()) %>
                  
              
            </td>
            <td style="text-align: center; min-width: 8em">
                <%= sendingByID.getFullName()%> &#40;<%= sendingByID.getSpecialty() %>&#41;
               
                
            </td>
            <td style="text-align: center; min-width: 8em">
                <%= receivingByID.getFullName() %> &#40;<%= receivingByID.getSpecialty() %>&#41;
                
            </td>
            <td style="text-align: center; min-width: 8em">
                  <a href="/iTrust/auth/patient/viewOfficeVisit.jsp?ovID=<%=ovBean.getID() %>"><%= StringEscapeUtils.escapeHtml(ovBean.getVisitDateStr()) %></a>
            </td>
            <td style="text-align: center; min-width: 8em">
                <%= StringEscapeUtils.escapeHtml(referralByID.getReferralDetails()) %>
            </td>
            <td style="text-align: center; min-width: 8em">
                <%= StringEscapeUtils.escapeHtml(referralByID.getTimeStamp()) %>
            </td>
            <td style="text-align: center; min-width: 8em">
                <a href="javascript:sendMessage('<%= StringEscapeUtils.escapeHtml("" + (referralByID.getId())) %>');">Email <%= receivingByID.getFullName() %></a>
            </td>
            
        </tr>

 </table>

<%} 

if (!"sendMessageForm".equals(submittedFormName)) {
List<VerboseReferralBean> beanList = action.getReferrals(sortBy, sortOrder);
%>


<table class="fTable" align="center" id="patientReferralsTable">
    <tr>
        <th colspan="4">Patient Referrals</th>
    </tr>

 	<tr>
 		<td align="center">
            <form action="viewPatientReferrals.jsp" id="sortByPriority" method="post">
                <input type="hidden" name="sortBy" value="priority" />
                <% if (sortBy.equals("priority") && sortOrderString.equals("ascending")) { %>
                    <input type="hidden" name="sortOrder" value="descending" />
                <% } else { %>
                    <input type="hidden" name="sortOrder" value="ascending" />
                <% } %>
                <input type="submit" value="Sort" >
            </form>
        </td>
        
        <td align="center">
            <form action="viewPatientReferrals.jsp" id="sortByReceivingHCP" method="post">
                <input type="hidden" name="sortBy" value="receiverName" />
                <% if (sortBy.equals("receiverName") && sortOrderString.equals("ascending")) { %>
                    <input type="hidden" name="sortOrder" value="descending" />
                <% } else { %>
                    <input type="hidden" name="sortOrder" value="ascending" />
                <% } %>
                <input type="submit" value="Sort">
            </form>
        </td>
        <td align="center">
            <form action="viewPatientReferrals.jsp" id="sortByUpdated" method="post">
                <input type="hidden" name="sortBy" value="timestamp" />
                <% if (sortBy.equals("timestamp") && sortOrderString.equals("descending")) { %>
                    <input type="hidden" name="sortOrder" value="ascending" />
                <% } else { %>
                    <input type="hidden" name="sortOrder" value="descending" />
                <% } %>
                <input type="submit" value="Sort">
            </form>
        </td>
        <td></td>
    </tr>
    
    <tr class="subHeader">
    	<td>Priority</td>
        <td>Receiving HCP</td>
        <td>Time Generated</td>
        <td>View This Referral</td>
    </tr>
    
<%
for (VerboseReferralBean refbean: beanList) {
	PersonnelBean sendingAll = hcp.getPersonnel(refbean.getSenderID());
	PersonnelBean receivingAll = hcp.getPersonnel(refbean.getReceiverID());
	
	//If statement is used to check if the patient has viewed a referral
	if(refbean.isViewedByPatient() == false){	
%>
        <tr>
            <td style="text-align: center">
                <b><%= Long.toString(refbean.getPriority()) %></b>
                  
              
            </td>
            <td style="text-align: center; min-width: 8em">
                <b><%= receivingAll.getFullName() %></b>
            </td>
            
            <td>
                <b><%= StringEscapeUtils.escapeHtml(refbean.getTimeStamp()) %></b>
            </td>
            <td style="text-align:center;">
            	<b><a href="javascript:showReferral('<%= StringEscapeUtils.escapeHtml("" + (refbean.getId())) %>');">View</a></b>
            </td>
            
        </tr>
<%
		}else{
			%>
			<tr>
            <td style="text-align: center">
                <%= Long.toString(refbean.getPriority()) %>
                  
              
            </td>
            <td style="text-align: center; min-width: 8em">
                <%=receivingAll.getFirstName() + " " + receivingAll.getLastName() %>
            </td>
            
            <td>
                <%= StringEscapeUtils.escapeHtml(refbean.getTimeStamp()) %>
            </td>
            <td style="text-align:center;">
            	<a href="javascript:showReferral('<%= StringEscapeUtils.escapeHtml("" + (refbean.getId())) %>');">View</a>
            </td>
            
        </tr>
			<%
		}
	}
	if(beanList.size() == 0){%>
		<tr>
			<td style="text-align:center;" colspan="4">
			<%="You have no referrals!" %>
			</td>
		
		</tr>
	
	<%}
}
%>
    
</table>


<%@include file="/footer.jsp"%>
