<%@page import="edu.ncsu.csc.itrust.model.old.enums.TransactionType"%>
<%@page import="edu.ncsu.csc.itrust.model.old.dao.DAOFactory"%>
<%@page import="java.util.List"%>
<%@page import="edu.ncsu.csc.itrust.model.old.beans.TransactionBean"%>
<%@page import="org.apache.commons.lang.StringEscapeUtils"%>

<html>
<head>
<title>FOR TESTING PURPOSES ONLY</title>
<style>
body, td{
	font-family: sans-serif;
	font-size: 8pt;
}
</style>
</head>
<body>
<h1>Test Utilities</h1>
A few clarifications:
<ul>
	<li>The <b>Type</b> is the name of the Java enum (from <code>edu.ncsu.csc.itrust.model.enums.TransactionType</code>)</li>
	<li>The <b>Code</b> is the actual key that gets stored in the database, defined in the Transaction Type enum. Here's the <a href="#transactioncodes">table of transaction codes</a></a></li>
	<li>The <b>Description</b> is plain-English description of that logging type
</ul>
<table border=1>
	<tr>
		<th>ID></th>
		<th>Time Logged</th>
		<th>Type</th>
		<th>Code</th>
		<th>Description</th>
		<th>Logged in User MID</th>
		<th>Secondary MID</th>
		<th>Extra Info</th>
	</tr>
	<%
		
		List<TransactionBean> list = DAOFactory.getProductionInstance().getTransactionDAO().getAllTransactions();
		for (TransactionBean t : list) {
	%>
	<tr>
		<td><%= StringEscapeUtils.escapeHtml("" + (t.getTransactionID())) %></td>
		<td><%= StringEscapeUtils.escapeHtml("" + (t.getTimeLogged())) %></td>
		<td><%= StringEscapeUtils.escapeHtml("" + (t.getTransactionType().name())) %></td>
		<td><%= StringEscapeUtils.escapeHtml("" + (t.getTransactionType().getCode())) %></td>
		<td><%= StringEscapeUtils.escapeHtml("" + (t.getTransactionType().getDescription())) %></td>
		<td><%= StringEscapeUtils.escapeHtml("" + (t.getLoggedInMID())) %></td>
		<td><%= StringEscapeUtils.escapeHtml("" + (t.getSecondaryMID())) %></td>
		<td><%= StringEscapeUtils.escapeHtml("" + (t.getAddedInfo())) %></td>
	</tr>
	<%
	}
	%>
</table>
<h1><a href="/iTrust">Back to iTrust</a></h1>
<h1>Transaction Code Reference</h1>
<a name="transactioncodes"></a>
List is automatically generated from the <code>edu.ncsu.csc.itrust.model.enums.TransactionType</code> enum.
<table border=1>
<tbody>
<tr>
<th>Type</th>
<th>Code</th>
<th>Description</th>
</tr>
<%
for(TransactionType type : TransactionType.values()){
	%><tr><td><%=type.name()%></td><%
	%><td><%=type.getCode()%></td><%
	%><td><%=type.getDescription()%></td></tr><%
}
%>
</tbody>
</table>
<h1><a href="/iTrust">Back to iTrust</a></h1>
</body>
</html>
