<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="java.io.*"%>
<%@page import="java.util.*"%>
<%@page import="java.util.regex.Pattern" %>
<%@page import="javax.xml.parsers.*" %>
<%@page import="org.xml.sax.*" %>
<%@page import="org.xml.sax.helpers.*" %>
<%@page import="java.text.SimpleDateFormat" %>
<%@page import="java.text.ParseException" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<style type="text/css">
		.fancyTable {
			font-family:"Trebuchet MS", Arial, Helvetica, sans-serif;
			width:100%;
			border-collapse:collapse;
		}
		
		.fancyTable td, .fancyTable th {
			font-size:.8em;
			background-color: #FFFFFF;
			border:1px solid #4F708D;
			padding:2px 2px 2px 2px;
		}
		
		.fancyTable .testName {
            padding:2px 2px 2px 10px;
            font-size: 1.0em;
		}
		
		.fancyTable th {
			font-size:1em;
			text-align:center;
			padding-top:0px;
			padding-bottom:0px;
			background-color:#4F708D;
			color:#ffffff;
		}
		
		.fancyTable tr td {
            color:#000000;
            background-color:#EEE;
		}
		
		.fancyTable tr.alt td {
			color:#000000;
			background-color:#DDDDFF;
		}
		
		.fancyTable a {
		    color: rgb(0,0,0);
		    text-decoration: none;
		}
		
		.fancyTable a:hover{
		     text-decoration: underline;
		}
		
		.fancyTable ul, .fancyTable ol {
		    padding-left: 25px;
		}
		
	</style>
	<script>
	function empty() { }
	function showrow(e) {
        // object attribute detection...
        // Firefox supports event.target.  Internet Explorer supports 
        // event.srcElement.  This if statement selects the appropriate 
        // attribute. (Google Chrome supports both attributes.)  
        if (e.target) {
        	src = e.target;
        } else {
        	src = e.srcElement;
        }
        row = src.parentNode.parentNode;
		for (i=1; i<row.children.length; i++) {
		    cell = row.children[i];
		    span = cell.children[0];
		    if (span.style.display=='none') {
		    	src.style.fontWeight = 'bold'; 
		    	span.style.display = 'inline';
		    } else {
                src.style.fontWeight = 'normal'; 
		    	span.style.display = 'none';
		    }
	    }
		//console.log(e);
	}
	</script>
</head>
<body style="margin-left:50px;">
<% 
final String path = "http://localhost:8080/iTrust/util/blackbox";
class TestParser {
	class BlackBoxTest
	{
		private String id = "";
		private String dateAdded = "";
		private String dateModified = "";
		private String author = "";
		private String role = "";
		private String useCase = "0";
		private String description = "";
		private LinkedList<String> precondition = new LinkedList<String>();
		private LinkedList<String> step = new LinkedList<String>();
		private String expectedResults = "";
		private LinkedList<String> eResult = new LinkedList<String>();
		private String actualResults = "";
		private LinkedList<String> aResult = new LinkedList<String>();
		
		public void setId(String id)
		{
			this.id = id;
		}
		
		public String getId()
		{
			return id;
		}
		
		public void setAuthor(String author)
		{
			this.author = author;
		}
		
		public String getAuthor()
		{
			return author;
		}
		
		public void setRole(String role)
		{
			this.role = role;
		}
		
		public String getRole()
		{
			return role;
		}
		
		public void setUseCase(String uc)
		{
			this.useCase = uc;
		}
		
		public String getUseCase()
		{
			return useCase;
		}
		
		public void setDateAdded(String da)
		{
			dateAdded = da;
		}
		
		public String getDateAdded()
		{
			return dateAdded;
		}
		
		public void setDateModified(String dm)
		{
			dateModified = dm;
		}
		
		public String getDateModified()
		{
			return dateModified;
		}
		
		public String getDescription()
		{
			if(precondition.size() == 0 && step.size() == 0)
			{
				return description;
			}
			String d = "<strong>Preconditions:</strong><br/>";
			d = d + "<ul>";
			for(String p : precondition)
			{
				d = d + "<li>" + p + "</li>";
			}
			d = d + "</ul>";
			d = d + "<br/><strong>STEPS:</strong><br/>";
			d = d + "<ol>";
			for(int i = 1; i < step.size()+1; i++)
			{
				d = d + "<li>" + step.get(i-1) + "</li>";
			}
			d = d + "</ol>";
			return d;
		}
		
		public String getExpectedResults()
		{
			String eR = "<ul>";
			for(String e : eResult)
			{
				eR = eR + "<li>" + e + "</li>";
			}
			eR = eR + "</ul>";
			return eR;
		}
		
		public String getActualResults()
		{
			String aR = "";
			for(String a : aResult)
			{
				aR = aR + "--> " + a + "<br/>";
			}
			return aR;
		}
		
		public void setPrecondition(String p)
		{
			precondition.add(p);
		}
		
		public void setStep(String s)
		{
			step.add(s);
		}
		
		public void setEResult(String e)
		{
			eResult.add(e);
		}
		
		public void setAResult(String a)
		{
			aResult.add(a);
		}
		
		public void setDescription(String d)
		{
			this.description = d;
		}
	}
 
	class SortByTestID implements Comparator<BlackBoxTest> {

	 	public int compare(BlackBoxTest arg0, BlackBoxTest arg1) {
	 		 String x = arg0.getUseCase();
	 		 int x1 = Integer.parseInt(x.trim());
	 		 String y = arg1.getUseCase();
	 		 int y1 = Integer.parseInt(y.trim());
	 		 if(x1 < y1)
	 			 return -1;
	 		 if(x1 == y1)
	 			 return 0;
	 		 else
	 			 return 1;
	 	}
	}


	private LinkedList<BlackBoxTest> bbt = new LinkedList<BlackBoxTest>();
	private LinkedList<String> roles = new LinkedList<String>();
	private BlackBoxTest test;
	private String tempVal = "";
	private int maxUseCase = 0;
	
	public LinkedList<BlackBoxTest> getTests()
	{
		return bbt;
	}
	
	public int getMaxUseCase()
	{
		return maxUseCase;
	}
	
	private void parseDocument() throws Exception{

		//get a factory
		SAXParserFactory spf = SAXParserFactory.newInstance();

			//get a new instance of parser
			SAXParser sp = spf.newSAXParser();


	DefaultHandler handler = new DefaultHandler() {

		public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {
			//reset
			tempVal = "";
			if(qName.equalsIgnoreCase("Test")) {
				//create a new instance of employee
				test = new BlackBoxTest();
				test.setId(attributes.getValue("id"));
			}
		}
	
		public void characters(char[] ch, int start, int length) throws SAXException {
			String s = new String(ch,start,length);
			s = s.replace("&","&amp;");
			tempVal += s;
		}
	
		public void endElement(String uri, String localName,
			String qName) throws SAXException {
	
			if(qName.equalsIgnoreCase("Test")) {
				//add it to the list
				bbt.add(test);
	
			}else if (qName.equalsIgnoreCase("DateAdded")) {
				test.setDateAdded(tempVal);
			}else if (qName.equalsIgnoreCase("DateModified")) {
				test.setDateModified(tempVal);
			}else if (qName.equalsIgnoreCase("Author")) {
				test.setAuthor(tempVal);
			}
			else if (qName.equalsIgnoreCase("Description"))
			{
				test.setDescription(tempVal);
			}
			else if (qName.equalsIgnoreCase("Precondition")) {
				test.setPrecondition(tempVal);
			}
			else if (qName.equalsIgnoreCase("eResult")) {
				test.setEResult(tempVal);
			}
			else if (qName.equalsIgnoreCase("Step")) {
				test.setStep(tempVal);
			}
			else if (qName.equalsIgnoreCase("aResult")) {
				test.setAResult(tempVal);
			}else if (qName.equalsIgnoreCase("Role")) {
				test.setRole(tempVal);
				String tvLower = tempVal.toLowerCase();
				boolean found = false;
				for (String r : roles) {
					if (r.toLowerCase().equals(tvLower)) {
						found = true;
						break;
					}
				}
				if (!found) {
					roles.add(tempVal);
				}
				
			}else if (qName.equalsIgnoreCase("UseCase")) {
				if(Integer.parseInt(tempVal.trim()) > maxUseCase)
				{
					maxUseCase = Integer.parseInt(tempVal);
				}
				test.setUseCase(tempVal.trim());
			}
		}
	};
		//parse the file and also register this class for call backs
		sp.parse(path+"/BlackBoxTestPlan.xml", handler);
	}
	
	private LinkedList<String> getRolesList()
	{
		return roles;
	}

	private LinkedList<BlackBoxTest> getTestsForRole(String r)
	{
		LinkedList<BlackBoxTest> testList = new LinkedList<BlackBoxTest>();
		for(BlackBoxTest b : bbt)
		{
			if(b.getRole().toLowerCase().equals(r.toLowerCase()))
			{
				testList.add(b);
			}
		}
		return testList;
	}
	
	private LinkedList<BlackBoxTest> getTestsForUseCase(int uc)
	{
		LinkedList<BlackBoxTest> testList = new LinkedList<BlackBoxTest>();
		for(BlackBoxTest b : bbt)
		{
			if(Integer.parseInt(b.getUseCase().trim()) ==  uc)
			{
				testList.add(b);
			}
		}
		return testList;
	}
	
	private LinkedList<String> getHTMLOutput(LinkedList<BlackBoxTest> list)
	{
		LinkedList<String> testList = new LinkedList<String>();
		Collections.sort(list, new SortByTestID());
		for(BlackBoxTest b : list)
		{
			String temp = "";
			temp+=		"\n\t\t<td class=\"testName\"><a href='javascript:;' onclick='showrow(event)'>" + b.getId() + "</a></td>";
			temp+=		"\n\t\t<td><span style='display: block;'>" + b.getDescription() +"</span></td>";
			temp+=		"\n\t\t<td><span style='display: block;'>" + b.getExpectedResults() + "</span></td>";
			temp+=		"\n\t\t<td><span style='display: block;'>" + b.getActualResults() + "</span></td>";
			temp+=		"\n\t\t<td>" + b.getUseCase() + "</td>";
			temp+=		"\n\t\t<td>" + b.getRole() + "</td>";
			temp+=		"\n\t\t<td>" + b.getDateAdded() + "</td>";
			temp+=		"\n\t\t<td>" + b.getDateModified() + "</td>";
			testList.add(temp);
		}
		return testList;
	}
	
}

TestParser t = new TestParser();
t.parseDocument();
LinkedList<String> rolesList = t.getRolesList();
%>

<%
	for(int j = 0; j <= t.getMaxUseCase(); j++)
	{
%>
<div align="left"><p style="font-family:verdana;font-size:1em;">
<%if(j == 0){ %>
<a name="uncategorized">Uncategorized</a>
<%} else{ %>
<a name="UC<%=j%>">UC<%=j %>
<%} %>

</p></div>
<table class="fancyTable">
	<tr class="fancyTable">
		<th style="width:10%">Test ID<br/><span style="font-weight: normal; font-size: 0.8em;">(click id to show details)</span></th>
		<th style="width:30%">Description</th>
		<th style="width:30%">Expected Results</th>
		<th style="width:5%">Actual Results</th>
		<th style="width:5%">Use Case</th>
		<th style="width:5%">Role</th>
		<th style="width:5%">Date Added</th>
		<th style="width:5%">Date Modified</th>									
	</tr>
<%	
		int i = 0;
		LinkedList<String> outputList = t.getHTMLOutput(t.getTestsForUseCase(j));
		for(String test : outputList)
		{
%>
			<tr valign="top" <%if(i%2 == 0){ %>class="alt" <%} %>>
				<%= test %>
			</tr>	
<% 
			i++;
		}
%>
	</table>
<%
	}
%>
</body>
</html>
