<%@include file="/global.jsp" %>
<%@page import="java.util.List" %>
<%@page import="edu.ncsu.csc.itrust.action.SearchUsersAction" %>
<%@page import="edu.ncsu.csc.itrust.model.old.beans.PatientBean" %>

<%
pageTitle = "iTrust - Please Select an Expert";
%>

<%@include file="/header.jsp" %>

<%@include file="/util/getUserFrame.jsp"%>	
	
	
<%
loggingAction.logEvent(TransactionType.VIEW_EXPERT_SEARCH_NAME, loggedInMID, loggedInMID, "");
%>
<script type="text/javascript">
 $(function(){
	
	$("#searchBox").keyup(function(){
		$.ajax({
			url:"FindExpertServlet",
			data:{
				query: $("#searchBox").val()
			},
			success:function(e){
				$("#target").html(e);
			}
		});
	});
 });
</script>
<form>
<h2> Select an Expert</h2>
<b>Search by name:</b><br/>
<div style="border: 1px solid Gray; padding:5px;float:left;">
	<input id="searchBox" name="search" style="width: 250px;" type="text" />
	<br />
</div>
<br>
<br>
<p>
<div id="target" align="left"></div>
</p>
</form>
<%@include file="/footer.jsp" %>
