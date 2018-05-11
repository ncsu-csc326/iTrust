<%@page import="java.io.DataInputStream"%>
<%@page errorPage="/auth/exceptionHandler.jsp" %>
<%@page import="java.util.List"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.Iterator"%>
<%@page import="java.io.InputStream"%>
<%@page import="edu.ncsu.csc.itrust.model.old.dao.DAOFactory"%>
<%@page import="edu.ncsu.csc.itrust.action.AddPatientFileAction"%> 
<%@page import="edu.ncsu.csc.itrust.exception.CSVFormatException"%>
<%@page import="edu.ncsu.csc.itrust.exception.AddPatientFileException"%>
<%@page import="edu.ncsu.csc.itrust.exception.FormValidationException"%>
<%@page import="org.apache.commons.fileupload.servlet.ServletFileUpload" %>
<%@page import="org.apache.commons.fileupload.DefaultFileItemFactory" %>
<%@page import="org.apache.commons.fileupload.FileItemFactory" %>
<%@page import="org.apache.commons.fileupload.FileItem" %>

<%@include file="/global.jsp" %>

<%
	pageTitle = "iTrust - Upload Patient File";
%>

<%@include file="/header.jsp" %>

<%
	boolean isMultipart = ServletFileUpload.isMultipartContent(request);

	AddPatientFileAction apfa = null;

	if(!isMultipart){
%>
	<p>Upload a CSV file. The first line of the file must contain header fields. Valid header fields include:
	<ul>
		<li>REQUIRED FIELDS</li>
		<li><b>firstName</li>
		<li>lastName</li>
		<li>email</b></li>
		<br>
		<li>OPTIONAL FIELDS</li>
		<li>streetAddress1</li>
		<li>streetAddress2</li>
		<li>city</li>
		<li>state</li>
		<li>zip</li>
		<li>phone</li>
		<li>motherMID</li>
		<li>fatherMID</li>
		<li>creditCardType</li>
		<li>creditCardNumber</li>
	</ul>
	</p>
		<form name="mainForm" enctype="multipart/form-data" action="uploadPatientFile.jsp" method="post">
			<table class="fTable mainTable" align="center">
				<tr><th colspan="3">Choose File</th></tr>		
				<tr><td>
					<input name="patientFile" type="file"/>
				</td>
				<td>
					<input type="submit" value="Send File" id="sendFile" name="sendFile">
				</td>
			</tr></table>
		</form>
		<%
			}else{
				String headerMessage = "Upload Successful";
				
				String error = "";
				Boolean fatal = false;
				List<String> results = new ArrayList<String>();	
				InputStream fileStream = null;
				boolean ignore = true;
				FileItemFactory factory = new DefaultFileItemFactory();
				ServletFileUpload upload = new ServletFileUpload(factory);
				List<FileItem> items = upload.parseRequest(request);
				Iterator iter = items.iterator();
				
				while (iter.hasNext()) {
				    FileItem item = (FileItem) iter.next();
				    fileStream = items.get(0).getInputStream();
				}

				if(fileStream!=null){
			try{
				apfa = new AddPatientFileAction(fileStream,prodDAO,loggedInMID.longValue());
			}catch(CSVFormatException e){
				fatal = true;
				error = e.getMessage();
			}catch(AddPatientFileException e){
				fatal = true;
				error = e.getMessage();
			}
				}else{
			fatal = true;
			error = "Please choose a file to upload.";
				}
				
			
				String headerColor = "#00CCCC";
				if(fatal || apfa == null){
			headerMessage = "File upload was unsuccessful. "+error;
			headerColor = "#ff3333";
				}else if(apfa.getErrors().hasErrors()){
			headerMessage = "File upload was successful, but some patients could not be added. Please review the following errors:<br />";
			List<String> errors=apfa.getErrors().getMessageList();
			for(int i=0;i<errors.size();i++){
				headerMessage=headerMessage+StringEscapeUtils.escapeHtml("" + (errors.get(i)))+"<br />";
			}
			headerColor = "#ff3333";
				}
		%>
	
	<div align=center>
	<span class="iTrustMessage" style="color:<%=headerColor %>"><%= headerMessage %></span>
	<br />
	
	<%
	if(apfa!=null){
	%>
		<table class="fTable">
				<tr>
					<th>MID</th>
					<th>First Name</th>
					<th>Last Name</th>
					<th>Temporary Password</th>
				</tr>

		<%
		for(int i=0;i<apfa.getPatients().size();i++){
			%>
			<tr>
				<td><%= StringEscapeUtils.escapeHtml("" + (apfa.getPatients().get(i).getMID())) %></td>
				<td><%= StringEscapeUtils.escapeHtml("" + (apfa.getPatients().get(i).getFirstName())) %></td>
				<td><%= StringEscapeUtils.escapeHtml("" + (apfa.getPatients().get(i).getLastName())) %></td>
				<td><%= StringEscapeUtils.escapeHtml("" + (apfa.getPatients().get(i).getPassword())) %></td>
			</tr>
			<%
		}
		%>
	
	</table>
	<%
	}
	%>
	
<% } %>

</div>
<br />


<%@include file="/footer.jsp" %>
