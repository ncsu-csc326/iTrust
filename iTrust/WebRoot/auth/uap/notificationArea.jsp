<%@page import="edu.ncsu.csc.itrust.model.old.dao.mysql.BillingDAO" %>
<%@page import="java.sql.Timestamp"%>
<%@page import="java.util.List"%>
<%@page import="java.util.ArrayList"%>


<%
BillingDAO bills = new BillingDAO(prodDAO);
%>

<div class="col-sm-12">
<div id="notificationArea">
    <div class="page-header"><h1>Notifications</h1></div>
	<div class="col-sm-6">
	<div class="panel panel-primary panel-notification">
	<div class="panel-heading"><h3 class="panel-title">Message Notification</h3></div>
	<div class="panel-body">
<!-- Begin Message Notification -->    
    <ul>
<% if(bills.getPendingNum() == 0) { %>
	<li><img class="icon" src="/iTrust/image/icons/inboxEmpty.png" style="border:0px;">
	No Pending Insurance Claims.</li> 
<%	} else { %>    
	<li><a href="/iTrust/auth/uap/viewClaims.jsp">
	<img class="icon" src="/iTrust/image/icons/inboxUnread.png" style="border:0px;"></a>
    <a href="/iTrust/auth/uap/viewClaims.jsp"><%= StringEscapeUtils.escapeHtml("" + (bills.getPendingNum())) %></a> Pending Insurance Claim<%= bills.getPendingNum() > 1 ? "s" : "" %>.
	</li>
<%	} %>
    </ul>
    </div>
   </div>
<!-- End Message Notification -->
   </div>
  </div>
</div>