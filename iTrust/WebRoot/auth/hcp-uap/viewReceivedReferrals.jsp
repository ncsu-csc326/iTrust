<%@page import="edu.ncsu.csc.itrust.action.ViewReceivingReferralsAction"%>
<%@page import="edu.ncsu.csc.itrust.action.ViewReportAction"%>
<%@page import="edu.ncsu.csc.itrust.action.SendMessageAction"%>
<%@page import="edu.ncsu.csc.itrust.beans.ReferralBean"%>
<%@page import="edu.ncsu.csc.itrust.beans.PersonnelBean"%>
<%@page import="edu.ncsu.csc.itrust.beans.PatientBean"%>
<%@page import="edu.ncsu.csc.itrust.beans.OfficeVisitBean"%>
<%@page import="edu.ncsu.csc.itrust.exception.FormValidationException"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.List"%>
<%@page import="java.text.DateFormat"%>
<%@page import="java.util.Date" %>
<%@page errorPage="/auth/exceptionHandler.jsp" %>
<%@taglib prefix="itrust" uri="/WEB-INF/tags.tld" %>

<%@include file="/global.jsp"%>

<%
    pageTitle = "iTrust - View Received Referrals";
%>

<%@include file="/header.jsp"%>

<%

ViewReceivingReferralsAction action = new ViewReceivingReferralsAction(prodDAO, loggedInMID);
ViewReportAction hcp = new ViewReportAction(prodDAO, loggedInMID);



String submittedFormName = request.getParameter("formName");
%>
<script type="text/javascript">
    function showReferral(value) {
        document.getElementById("referalID").value = value;
        document.forms["receivedReferralForm"].submit();
    }
</script>


<form action="viewReceivedReferrals.jsp" method="post" id="receivedReferralForm">
<input type="hidden" name="formName" value="receivedReferralForm" />
<input type="hidden" id="referalID" name="referalID" value="" />
</form>

<%

if("receivedReferralForm".equals(submittedFormName) || (request.getParameter("referalID") != null && !request.getParameter("referalID").equals(""))){

ReferralBean referralByID = action.getReferralByID(Integer.parseInt(request.getParameter("referalID")));
PersonnelBean sendingByID = hcp.getPersonnel(referralByID.getSenderID());
PatientBean patientByID = hcp.getPatient(referralByID.getPatientID());


loggingAction.logEvent(TransactionType.CONSULTATION_REFERRAL_VIEW, loggedInMID, 0, "The receiving HCP has viewed referral: "+referralByID.getId());


//The hcp has viewed this referral
referralByID.setViewedByHCP(true);
action.updateReferral(referralByID);
OfficeVisitBean ovBean = action.getOVDate(referralByID.getOvid());
session.setAttribute("pid", ""+referralByID.getPatientID());



%>
<table class="fTable" align="center" id="receivedViewingReferral" style="margin:0 0 20px 0; width:100%;">
    <tr>
        <th colspan="7">Received Referral Complete Information</th>
    </tr>

    <tr class="subHeader">
    	<td>Priority</td>
    	<td>Sending HCP &#40;specialty&#41;</td>
        <td>Patient</td>
        <td>Office Visit Date</td>
        <td>Notes</td>
        <td>Time Generated</td>
    </tr>
   	<tr>
            <td style="text-align: center">
                <%= Long.toString(referralByID.getPriority()) %>
                  
              
            </td>
            <td style="text-align: center; min-width: 8em">
                <%= sendingByID.getFullName()%> &#40;<%= sendingByID.getSpecialty() %>&#41;
               
                
            </td>
            <td style="text-align: center; min-width: 8em">
                <%= patientByID.getFullName() %>
                
            </td>
            <td style="text-align: center; min-width: 8em">
                <a href="/iTrust/auth/hcp-uap/editOfficeVisit.jsp?ovID=<%= Long.toString(ovBean.getID()) %>">
                  <%= StringEscapeUtils.escapeHtml(ovBean.getVisitDateStr()) %>
                </a>
            </td>
            <td style="text-align: center; min-width: 8em">
                <%= StringEscapeUtils.escapeHtml(referralByID.getReferralDetails()) %>
            </td>
            <td style="text-align: center; min-width: 8em">
                <%= StringEscapeUtils.escapeHtml(referralByID.getTimeStamp()) %>
            </td>
        </tr>

 </table>

<%} 

List<ReferralBean> beanList = action.getReferralsForReceivingHCP();

%>


<table class="fTable" align="center" id="receivedReferralsTable">
    <tr>
        <th colspan="4">Received Referrals</th>
    </tr>

    <tr class="subHeader">
    	<td>Priority</td>
        <td>Patient Name</td>
        <td>Time Generated</td>
        <td>View This Referral</td>
    </tr>
    	<%if(beanList.size() == 0){%>
	<tr>
		<td style="text-align:center;" colspan="4">
		<%="You have no referrals!" %>
		</td>
	
	</tr>

<%}

for (ReferralBean refbean: beanList) {
	
	//If statement is used to check if the patient has viewed a referral
	if(refbean.isViewedByHCP() == false){	
%>
        <tr>
            <td style="text-align: center">
                <b><%= Long.toString(refbean.getPriority()) %></b>
                  
              
            </td>
            <td style="text-align: center; min-width: 8em">
                <b><%= hcp.getPatient(refbean.getPatientID()).getFullName() %></b>
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
                <%=hcp.getPatient(refbean.getPatientID()).getFullName() %>
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
%>
    
</table>


<%@include file="/footer.jsp"%>
