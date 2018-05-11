
<%@page import="edu.ncsu.csc.itrust.action.EditReferralsAction"%>
<%@page import="edu.ncsu.csc.itrust.beans.ReferralBean"%>

<% { 
	String r = request.getParameter("r");
	String referralMessage = (r==null) ? "" : "The referral was successfully " + r;
%>

<div align=center>
<a name="referrals"></a>
<span class="iTrustMessage"><%= referralMessage %></span>
<table class="fTable" align="center" id="referralsTable">
    <tr>
        <th colspan="5"><a href="#" class="topLink">[Top]</a>Referrals</th>
    </tr>
    <tr class="subHeader">
        <td>Receiving HCP</td>
        <td>Comments</td>
        <td>Priority</td>
        <td>Created</td>
        <td style="width: 60px;">Action</td>
    </tr>

    <%if(ovaction.referrals().getReferrals().size()==0){ %>
    <tr>
        <td  colspan="5" style="text-align: center;">No Referrals on record</td>
    </tr>
    <%} else { 
            for(ReferralBean d : ovaction.referrals().getReferrals()) { %>
    <tr>
        <td ><%= StringEscapeUtils.escapeHtml("" + (ovaction.referrals().getReceivingHCPName(d))) %></td>
        <td ><%= StringEscapeUtils.escapeHtml("" + (d.getReferralDetails())) %></td>
        <td ><%= StringEscapeUtils.escapeHtml("" + (d.getPriority())) %></td>
        <td ><%= StringEscapeUtils.escapeHtml("" + (d.getTimeStamp())).replaceFirst(" ","<br/>") %></td>
        <td>
            <form action="editReferral.jsp" method="post">
                <input type="hidden" name="ovID" value="<%= ovaction.getOvID() %>" />
                <input type="hidden" name="referralID" value="<%= Long.toString(d.getId()) %>" />
                <input type="hidden" name="returnToURL" value="editOfficeVisit.jsp?ovID=<%= ovaction.getOvID() %>" />
                <input type="submit" value="View/Edit" <%= disableSubformsString %> >
            </form>
        </td>
    </tr>
    <%      }
        }%>
    <tr>
        <th colspan="5" style="text-align: center;">New</th>
    </tr>
    <tr>
        <td colspan="5" align=center>
            <form action="editReferral.jsp" id="createReferralForm" method="post">
                <input type="hidden" id="ovID" name="ovID" value="<%= ovaction.getOvID() %>" />
                <%-- Note: the #referrals identifier in the following URL is actually found in editOfficeVisit.jsp, which includes this file. --%>
                <input type="hidden" id="returnToURL" name="returnToURL" value="editOfficeVisit.jsp?ovID=<%= ovaction.getOvID() %>" />
                <input type="submit" id="add_referral" value="Add Referral" <%= disableSubformsString %> >
            </form>
        </td>
    </tr>
</table>

</div>


<% } %>

