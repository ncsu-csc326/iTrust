<%@page import="com.sun.xml.internal.messaging.saaj.packaging.mime.MultipartDataSource"%>
<%@page import="java.net.URLEncoder" %>
<%@page import="java.net.URLDecoder" %>

<%@page import="org.apache.commons.fileupload.FileItem"%>
<%@page import="org.apache.commons.fileupload.servlet.ServletFileUpload"%>
<%@page import="org.apache.commons.fileupload.disk.DiskFileItemFactory"%>
<%@page import="org.apache.commons.fileupload.FileUploadException"%>
<%@page import="org.apache.commons.io.FilenameUtils"%>

<%@page import="edu.ncsu.csc.itrust.action.AddDrugListAction"%>
<%@page import="edu.ncsu.csc.itrust.action.AddDrugListAction.DrugStrategy"%>
<%@page import="edu.ncsu.csc.itrust.action.AddDrugListAction.SkipDuplicateDrugStrategy"%>
<%@page import="edu.ncsu.csc.itrust.action.AddDrugListAction.OverwriteDuplicateDrugStrategy"%>

<%@page import="java.io.InputStream"%>
<%@page import="java.util.Scanner"%>
<%@page import="java.util.List"%>
<%@page import="java.lang.Exception"%>

<%@include file="/global.jsp" %>

<%
pageTitle = "iTrust - Import ND Codes";
%>

<%@include file="/header.jsp" %>

<%
// forward via application/x-www-form-urlencoded address by default (happens in default case)
String forwardAddress = StringEscapeUtils.escapeHtml("" + (request.getParameter("forward") ));
AddDrugListAction action;
InputStream is = null;
DrugStrategy strategy = new SkipDuplicateDrugStrategy(); //default behavior
try {
       List<FileItem> items = new ServletFileUpload(new DiskFileItemFactory()).parseRequest(request);
       for (FileItem item : items) {
			if (item.isFormField()) {
				String fieldname = item.getFieldName();
				String fieldvalue = item.getString();
				if (fieldname.equals("return"))
					response.sendRedirect(forwardAddress);
				// forward via multipart/form-data (happens when form is used before forwarding)
				if (fieldname.equals("forward"))
               		forwardAddress = fieldvalue;
				if (fieldname.equals("strategy")) {
					if (fieldvalue.equals("ignore"))
						strategy = new SkipDuplicateDrugStrategy();
					else if (fieldvalue.equals("update"))
						strategy = new OverwriteDuplicateDrugStrategy();
					else
						out.println("Error: Invalid strategy: " + fieldvalue);
				}
           } else {
               // Process form file field (input type="file").
               String fieldvalue = item.getName();
               if(fieldvalue != null && fieldvalue.length() > 0 && !fieldvalue.equals("null")) {
                   is = item.getInputStream();
               }
           }
       }
		if(is != null) {
		       action = new AddDrugListAction(strategy, prodDAO, loggingAction, loggedInMID.longValue());
		       action.loadFile(is);
		       response.sendRedirect(forwardAddress);
		}
   } catch (FileUploadException e) {
       
   }

%>

<h1>Import NDCodes</h1>
<form enctype="multipart/form-data" action="importNDCodes.jsp" id="NDCodeUploadForm" method="post" />
	<input type="hidden" name="forward" value="<%= forwardAddress %>" />
	File: <input name="fileIn" type="file" />
	<br/>
	On Duplicate ND Code: 
	<select name="strategy">
	  <option value="ignore" selected>Ignore Duplicate ND Codes</option>
	  <option value="update">Update Duplicate ND Codes</option>
	</select>
	<br/>
	<input type="submit" name="import" id="import" value="Send File" />
	<br/>
	<input type="submit" name="return" id="return" value="Done Importing"/>
</form>
<br/><br/>

<%@include file="/footer.jsp" %>
