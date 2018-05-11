<%@page import="java.io.DataInputStream"%>
<%@page errorPage="/auth/exceptionHandler.jsp" %>
<%@page import="java.util.List"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.Iterator"%>
<%@page import="java.io.InputStream"%>
<%@page import="edu.ncsu.csc.itrust.dao.DAOFactory"%>
<%@page import="edu.ncsu.csc.itrust.action.UpdateLOINCListAction"%> 
<%@page import="edu.ncsu.csc.itrust.exception.FormValidationException"%>
<%@page import="org.apache.commons.fileupload.servlet.ServletFileUpload" %>
<%@page import="org.apache.commons.fileupload.DefaultFileItemFactory" %>
<%@page import="org.apache.commons.fileupload.FileItemFactory" %>
<%@page import="org.apache.commons.fileupload.FileItem" %>

<%@include file="/global.jsp" %>

<%
pageTitle = "iTrust - Upload LOINC Codes";
%>

<%@include file="/header.jsp" %>

<%
	boolean isMultipart = ServletFileUpload.isMultipartContent(request);

	if(!isMultipart){
		
		%>
		<form name="mainForm" enctype="multipart/form-data" action="uploadLOINC.jsp" method="post">
			<table class="fTable mainTable" align="center">
				<tr><th colspan="3">Choose File</th></tr>		
				<tr><td>
					<input name="loincFile" type="file"/>
				</td>
				<td>
					Options <br/>
					<select name="ignoreDupData">
						<option value="1">Ignore Duplicates</option>
						<option value="0">Replace Duplicates</option>
					</select>
				<td>
					<input type="submit" value="Send File" id="sendFile" name="sendFile">
				</td>
			</tr></table>
		</form>
		<%
	}else{
		String headerMessage = "Upload Successful";
		
		String error = "";
		List<String> results = new ArrayList<String>();	
		
			
		UpdateLOINCListAction action = new UpdateLOINCListAction(prodDAO);
		InputStream fileStream = null;
		boolean ignore = true;

		FileItemFactory factory = new DefaultFileItemFactory();
		ServletFileUpload upload = new ServletFileUpload(factory);

		List<FileItem> items = upload.parseRequest(request);
		
		Iterator iter = items.iterator();
		while (iter.hasNext()) {
		    FileItem item = (FileItem) iter.next();

		    if (item.isFormField()) {
		        if(item.getFieldName().equals("ignoreDupData")){
		        	if(item.getString().equals("0")){
		        		ignore=false;
		        	}
		        }
		    } else {
		    	fileStream = items.get(0).getInputStream();
		    }
		}

		if(fileStream!=null){
			results = action.parseLOINCFile(fileStream, ignore);
		}else{
			error = "Please choose a file to upload.";
		}
				
	
		String headerColor = "#00CCCC";
		if(error.length()>0 || !results.get(results.size()-1).contains("Successfully")){
			headerMessage = "File upload was unsuccessful. Please review the following errors.";
			headerColor = "#ff3333";
			if(error.length()>0){
				results.add(error);
			}
		}else{
			loggingAction.logEvent(TransactionType.LOINC_CODE_FILE_ADD, loggedInMID, 0, "");
		}

	%>
	
	<div align=center>
	
	
	
	<span class="iTrustMessage" style="color:<%=headerColor %>"><%= StringEscapeUtils.escapeHtml("" + (headerMessage )) %></span>
	<br />
	<table class="fTable">
	<tr><th>Results</th></tr>
	<tr><td>
	<div style="text-align:left;white-space: nowrap;width:700px;min-height:200px; max-height:600px;overflow:scroll;padding:10px;">
	<%
	if(results != null){
		for(String msg : results){
			out.print(msg+"<br/>");
		}
	}
	
	%>
	
	</div>
	</td></tr>
	</table>
<% } %>

<a href="/iTrust/auth/admin/editLOINCCodes.jsp">Return to LOINC Codes List</a>
</div>
<br />


<%@include file="/footer.jsp" %>
