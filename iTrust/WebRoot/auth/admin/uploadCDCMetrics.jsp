<%@page import="com.sun.xml.internal.messaging.saaj.packaging.mime.MultipartDataSource"%>
<%@page import="java.net.URLEncoder" %>
<%@page import="java.net.URLDecoder" %>

<%@page import="org.apache.commons.fileupload.FileItem"%>
<%@page import="org.apache.commons.fileupload.servlet.ServletFileUpload"%>
<%@page import="org.apache.commons.fileupload.disk.DiskFileItemFactory"%>
<%@page import="org.apache.commons.fileupload.FileUploadException"%>
<%@page import="org.apache.commons.io.FilenameUtils"%>

<%@page import="edu.ncsu.csc.itrust.action.UploadReferenceTablesAction"%>

<%@page import="java.io.InputStream"%>
<%@page import="java.util.Scanner"%>
<%@page import="java.util.List"%>
<%@page import="java.lang.Exception"%>

<%@include file="/global.jsp" %>

<%pageTitle = "iTrust - Import CDC Percentile Metrics";%>

<%@include file="/header.jsp" %>

<%
// forward via application/x-www-form-urlencoded address by default (happens in default case)
//create our forward address, which will clear their session cache so we don't run into errors errywar.
//String forwardAddress = StringEscapeUtils.escapeHtml("" + (request.getParameter("forward") ));
UploadReferenceTablesAction action;
InputStream inStream = null;

//this will determine which upload method we call.
String metric = "";
try {
	   //process the request from the form. this 
       List<FileItem> items = new ServletFileUpload(new DiskFileItemFactory()).parseRequest(request);
       for (FileItem item : items) {
			if (item.isFormField()) {
				String fieldname = item.getFieldName();
				String fieldvalue = item.getString();
				//if (fieldname.equals("return")){
				//	response.sendRedirect(forwardAddress);
				//}
				 //forward via multipart/form-data (happens when form is used before forwarding)
				//else if (fieldname.equals("forward")){
               		//forwardAddress = fieldvalue;
				//} else 
				if(fieldname.equals("metric")){
					metric = fieldvalue;
				}
           } else {
               // Process form file field (input type="file").
               String fieldvalue = item.getName();
               if(fieldvalue != null && fieldvalue.length() > 0 && !fieldvalue.equals("null")) {
                   inStream = item.getInputStream();
               }
           }
       }
       //if the file is opened correctly and a valid inputstream is created then we can attempt to process it.
		if(inStream != null) {
		       action = new UploadReferenceTablesAction(prodDAO);
			   boolean isSuccess = true;
		       
				//TODO: Make sure Alex doesn't the whole internet
		       if(metric.equals("BMI")){
		    	   isSuccess = action.storeBMIStats(inStream);
		       } else if(metric.equals("Weight")){
		    	   isSuccess =  action.storeWeightStats(inStream);
		       }else if(metric.equals("HeadCircumfrence")){
		    	   isSuccess =  action.storeHeadCircStats(inStream);
		       }else if(metric.equals("Height")){
		    	   isSuccess =  action.storeHeightStats(inStream);
		       }
			   
			   if(isSuccess){
				  %><p style="font-size:14px"><font color="#04B404"><b> CDC Metrics updated successfully! </b></font></p> <%
				  //now add me some good ol' transaction logging.
				  loggingAction.logEvent(TransactionType.ADMIN_UPLOAD_CDCMETRICS, loggedInMID.longValue(),loggedInMID.longValue(), "");

			   } else{
				   %> Please make sure the file you are submitting is formatted as per the CDC's instructions! <%
			   }
		       
		       //response.sendRedirect(forwardAddress);
		}
   } catch (FileUploadException e) {
       %> Please only upload CSV files from the CDC's website. <%
   }

%>

<h1>Import CDCodes</h1>
<form enctype="multipart/form-data" action="uploadCDCMetrics.jsp" id="NDCodeUploadForm" method="post" />
	File: <input name="fileIn" type="file" />
	<br/>
	Select the which metric this data should replace: 
	<select name="metric">
	  <option value="BMI" selected>Body Mass Index</option>
	  <option value="Weight">Weight</option>
	  <option value="HeadCircumfrence" selected>Head Circumfrence</option>
	  <option value="Height">Height</option>
	</select>
	<br/>
	<input type="submit" name="import" id="import" value="Send File" />
	<br/>
</form>
<br/><br/>
Health Metric data may be downloaded at the CDC's website <a target="_blank" href="http://www.cdc.gov/growthcharts/percentile_data_files.htm">here.</a>
<%@include file="/footer.jsp" %>