<%@taglib uri="/WEB-INF/tags.tld" prefix="itrust" %>
<%@page errorPage="/auth/exceptionHandler.jsp" %>

<%@page import="java.util.List"%>
<%@page import="edu.ncsu.csc.itrust.beans.VerboseReferralBean"%>
<%@page import="edu.ncsu.csc.itrust.action.ViewSentReferralsAction"%>
<%@page import="edu.ncsu.csc.itrust.beans.PatientBean"%>
<%@page import="edu.ncsu.csc.itrust.beans.PersonnelBean"%>
<%@page import="edu.ncsu.csc.itrust.enums.SortDirection"%>
<%@page import="edu.ncsu.csc.itrust.Localization" %>

<%@include file="/global.jsp" %>

<%
pageTitle = "iTrust - View Sent Referrals";
%>

<%@include file="/header.jsp" %>

<%

ViewSentReferralsAction action = new ViewSentReferralsAction(prodDAO, loggedInMID);
String sortBy = request.getParameter("sortBy"); 
if (sortBy==null)
	sortBy = "timestamp";
String sortOrderString = request.getParameter("sortOrder"); 
if (sortOrderString==null)
	sortOrderString = "descending";
SortDirection sortOrder = SortDirection.parse(sortOrderString);

%>

<table class="fTable" align="center" id="sentReferralsTable">
    <tr>
        <td align="center">
            <form action="viewSentReferrals.jsp" id="sortByReceivingHCP" method="post">
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
            <form action="viewSentReferrals.jsp" id="sortByPatient" method="post">
                <input type="hidden" name="sortBy" value="patientName" />
                <% if (sortBy.equals("patientName") && sortOrderString.equals("ascending")) { %>
                    <input type="hidden" name="sortOrder" value="descending" />
                <% } else { %>
                    <input type="hidden" name="sortOrder" value="ascending" />
                <% } %>
                <input type="submit" value="Sort">
            </form>
        </td>
        <td align="center">
            <form action="viewSentReferrals.jsp" id="sortByUpdated" method="post">
                <input type="hidden" name="sortBy" value="timestamp" />
                <% if (sortBy.equals("timestamp") && sortOrderString.equals("descending")) { %>
                    <input type="hidden" name="sortOrder" value="ascending" />
                <% } else { %>
                    <input type="hidden" name="sortOrder" value="descending" />
                <% } %>
                <input type="submit" value="Sort">
            </form>
        </td>
        <td align="center">
            <form action="viewSentReferrals.jsp" id="sortByPriority" method="post">
                <input type="hidden" name="sortBy" value="priority" />
                <% if (sortBy.equals("priority") && sortOrderString.equals("ascending")) { %>
                    <input type="hidden" name="sortOrder" value="descending" />
                <% } else { %>
                    <input type="hidden" name="sortOrder" value="ascending" />
                <% } %>
                <input type="submit" value="Sort" >
            </form>
        </td>
        <td></td>
    </tr>
    <tr>
        <th>Receiving HCP</th>
        <th>Patient</th>
        <th>Updated</th>
        <th>Priority</th>
        <th>Action</th>
    </tr>
    <% for (VerboseReferralBean bean: action.getReferrals(sortBy, sortOrder)) { %>
    <tr>
        <td align="center"><%= StringEscapeUtils.escapeHtml( bean.getReceiverName() ) %></td>
        <td align="center"><%= StringEscapeUtils.escapeHtml( bean.getPatientName() ) %></td>
        <td align="center"><%= StringEscapeUtils.escapeHtml( bean.getTimeStamp() ).replaceFirst(" ","<br/>") %></td>
        <td align="center"><%= StringEscapeUtils.escapeHtml( "" + bean.getPriority() ) %></td>
        <td align="center">
            <form action="/iTrust/auth/hcp-uap/editReferral.jsp" id="editReferralForm" method="post">
                <input type="hidden" name="ovID" value="<%= bean.getOvid() %>" />
                <input type="hidden" name="referralID" value="<%= bean.getId() %>" />
                <input type="hidden" name="returnToURL" value="/iTrust/auth/hcp-uap/viewSentReferrals.jsp" />
                <input type="submit" value="Details" >
            </form>
        </td>
    </tr>
    <% } %>
</table>




<%@include file="/footer.jsp" %>

