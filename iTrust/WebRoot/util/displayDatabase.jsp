<%@page import="edu.ncsu.csc.itrust.dao.DAOFactory"%>
<%@page import="java.sql.*"%>
<%@page import="edu.ncsu.csc.itrust.dao.DAOFactory"%>
<%@page import="java.util.LinkedList"%>
<%@page import="org.apache.commons.lang.StringEscapeUtils"%>


<%@page import="java.awt.image.BufferedImage"%><html>
<head>
	<title>Display Database</title>
	<style type="text/css">
		body {
		margin: 4px;
		font-family: Arial;
		font-size: 0.8em;
		}
		.results { 
		 border-collapse: collapse;
		}
		.results tr th {
		 font-size: 0.9em;
		 padding: 0px 5px 0px 5px;
		 background-color: Navy;
		 color: White;
		}
		.results tr td {
		 font-size: 0.8em;
		 padding: 0px 5px 0px 5px;
		}
		.results tr th, .results tr td {
		 border: 1px solid Gray;
		}
	</style>
</head>
<body>
<a href="/iTrust">Back to iTrust</a> - <a href="displayWikiDatabase.jsp">View wiki format</a>
<h2>FOR TESTING PURPOSES ONLY</h2>
<%
	
	LinkedList<String> tableList = new LinkedList<String>();

	Connection conn = DAOFactory.getProductionInstance().getConnection();
	ResultSet tableRS = conn.createStatement().executeQuery("show tables");
	while(tableRS.next()){
		String tableName = tableRS.getString(1);
		tableList.add(tableName);
		ResultSet rs = conn.createStatement().executeQuery("SELECT * FROM " + tableName);
		int numCol = rs.getMetaData().getColumnCount();
		%><b><a name="<%=tableName%>"><%= StringEscapeUtils.escapeHtml("" + (tableName)) %></b><br /><table id="<%=tableName%>" class="results"><tr><%
		for(int i=1; i<=numCol;i++){
			%><th><%= StringEscapeUtils.escapeHtml("" + (rs.getMetaData().getColumnName(i))) %></th><%
		}
		%></tr><%
		while(rs.next()){
			%><tr><%
			long mid = 0l;
			for(int i=1;i<=numCol;i++){
				try{
					String data = rs.getString(i);
					
					if ("profilephotos".equals(tableName.toLowerCase()) && i==2)
					{
						%><td>Photo</td><%
					}
					else
					{
					%><td><%= StringEscapeUtils.escapeHtml("" + (data)) %></td><%
					}				
				} catch(SQLException e){
					%><td>--Error in date, might be empty--</td><%
				}
			}
			%></tr><%
		}
		%></table><br /><br /><%
	}
	conn.close();
%>
</body>
</html>
