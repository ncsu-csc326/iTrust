
<%@page import="java.util.List"%>
<%@page import="java.text.DateFormat"%>
<%@page import="java.text.SimpleDateFormat"%>

<%@page import="edu.ncsu.csc.itrust.model.old.beans.MessageBean"%>
<%@page import="edu.ncsu.csc.itrust.model.old.dao.DAOFactory"%>
<%@page import="edu.ncsu.csc.itrust.action.EditPersonnelAction"%>
<%@page import="edu.ncsu.csc.itrust.model.old.dao.mysql.PersonnelDAO"%>
<%@page import="edu.ncsu.csc.itrust.model.old.dao.mysql.PatientDAO"%>
<%@page import="edu.ncsu.csc.itrust.action.ViewMyMessagesAction"%>
<%@page import="edu.ncsu.csc.itrust.action.EditPatientAction"%>
<%@page import="edu.ncsu.csc.itrust.action.EditPersonnelAction"%>

			<script src="/iTrust/DataTables/media/js/jquery.dataTables.min.js" type="text/javascript"></script>
			<script type="text/javascript">
				jQuery.fn.dataTableExt.oSort['lname-asc']  = function(x,y) {
					var a = x.split(" ");
					var b = y.split(" ");
					return ((a[1] < b[1]) ? -1 : ((a[1] > b[1]) ?  1 : 0));
				};
				
				jQuery.fn.dataTableExt.oSort['lname-desc']  = function(x,y) {
					var a = x.split(" ");
					var b = y.split(" ");
					return ((a[1] < b[1]) ? 1 : ((a[1] > b[1]) ?  -1 : 0));
				};
			</script>
			<script type="text/javascript">	
   				$(document).ready(function() {
       				$("#mailbox").dataTable( {
       					"aaColumns": [ [2,'dsc'] ],
       					"aoColumns": [ { "sType": "lname" }, null, null, {"bSortable": false} ],
       					"sPaginationType": "full_numbers"
       				});
   				});
			</script>
			<style type="text/css" title="currentStyle">
				@import "/iTrust/DataTables/media/css/demo_table.css";		
			</style>

<%

boolean outbox=(Boolean)session.getAttribute("outbox");
boolean isHCP=(Boolean)session.getAttribute("isHCP");

String pageName="messageInbox.jsp";
if(outbox){
	pageName="messageOutbox.jsp";
}
	
PersonnelDAO personnelDAO = new PersonnelDAO(prodDAO);
PatientDAO patientDAO = new PatientDAO(prodDAO);

DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");

ViewMyMessagesAction action = new ViewMyMessagesAction(prodDAO, loggedInMID.longValue());

List<MessageBean> messages = outbox?action.getAllMySentMessages():action.getAllMyMessages();
session.setAttribute("messages", messages);
		
if(messages.size() > 0) { %>

<table id="mailbox" class="display fTable">
	<thead>		
		<tr>
			<th><%= outbox?"Receiver":"Sender" %></th>
			<th>Subject</th>
			<th><%= outbox?"Sent":"Received" %></th>
			<th></th>
		</tr>
	</thead>
	<tbody>
	<% 
	int index=-1;
	for(MessageBean message : messages) {
		String style = "";
		if(message.getRead() == 0) {
			style = "style=\"font-weight: bold;\"";
		}

		if(!outbox || message.getOriginalMessageId()==0){
			index ++; 
			String primaryName = action.getName(outbox?message.getTo():message.getFrom());
			List<MessageBean> ccs = action.getCCdMessages(message.getMessageId());
			String ccNames = "";
			int ccCount = 0;
			for(MessageBean cc:ccs){
				ccCount++;
				long ccMID = cc.getTo();
				ccNames += action.getPersonnelName(ccMID) + ", ";
			}
			ccNames = ccNames.length() > 0?ccNames.substring(0, ccNames.length()-2):ccNames;
			String toString = primaryName;
			if(ccCount>0){
				String ccNameParts[] = ccNames.split(",");
				toString = toString + " (CC'd: ";
				for(int i = 0; i < ccNameParts.length-1; i++) {
					toString += ccNameParts[i] + ", ";
				}
				toString += ccNameParts[ccNameParts.length - 1] + ")";
			}			
			%>					
				<tr <%=style%>>
					<td><%= StringEscapeUtils.escapeHtml("" + ( toString)) %></td>
					<td><%= StringEscapeUtils.escapeHtml("" + ( message.getSubject() )) %></td>
					<td><%= StringEscapeUtils.escapeHtml("" + ( dateFormat.format(message.getSentDate()) )) %></td>
					<td><a href="<%= outbox?"viewMessageOutbox.jsp?msg=" + StringEscapeUtils.escapeHtml("" + ( index )):"viewMessageInbox.jsp?msg=" + StringEscapeUtils.escapeHtml("" + ( index )) %>">Read</a></td>
				</tr>			
			<%
		}
		
	}	
	%>
	</tbody>
</table>
<%} else { %>
	<div>
		<i>You have no messages</i>
	</div>
<%	} %>