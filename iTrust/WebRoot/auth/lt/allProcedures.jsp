<%@taglib prefix="itrust" uri="/WEB-INF/tags.tld" %>
<%@page errorPage="/auth/exceptionHandler.jsp" %>

<%@page import="java.util.List"%>
<%@page import="edu.ncsu.csc.itrust.beans.LabProcedureBean"%>
<%@page import="edu.ncsu.csc.itrust.validate.LabProcedureValidator"%>
<%@page import="edu.ncsu.csc.itrust.exception.FormValidationException"%>
<%@page import="edu.ncsu.csc.itrust.action.LabProcLTAction"%>


<%@include file="/global.jsp" %>

<%
pageTitle = "iTrust - View Laboratory Procedures";
%>

<%@include file="/header.jsp" %>

<%
String actionMessage = "";
LabProcedureValidator validator = new LabProcedureValidator();

if(request.getParameter("action") != null){
	actionMessage = request.getParameter("action");%>
	<%if(actionMessage.equals("received")){ %>
		<span class="iTrustMessage">You have received the in transit lab procedure!<br /></span>
	<%} else if(actionMessage.equals("complete")){%>
			<span class="iTrustMessage">You have completed the lab procedure!<br /></span>
	<%} else if(actionMessage.equals("error")){ %>
			<span class="iTrustError">The "Numerical Result" and "Confidence Level" fields must contain only numerical values, an optional + or - sign, and/or an optional decimal!<br /></span>
	<%} %>
<%}
long pid = (Long) session.getAttribute("loggedInMID");
String pidString = "" + pid;
LabProcLTAction action = new LabProcLTAction(prodDAO);

String submittedFormName = request.getParameter("formName");
List<LabProcedureBean> lpTesting = action.viewTestingProcedures(pid);
List<LabProcedureBean> lpReceived = action.viewReceivedProcedures(pid);
List<LabProcedureBean> lpInTransit = action.viewInTransitProcedures(pid);

if ("testingForm".equals(submittedFormName)) {
	LabProcedureBean proc = action.getLabProcedure(Long.parseLong(request.getParameter("pidTest")));
	try {
		
		proc.setNumericalResult(request.getParameter("numericalResult"));
		proc.setNumericalResultUnit(request.getParameter("numericalResultUnit"));
		proc.setUpperBound(request.getParameter("upperBound"));
		proc.setLowerBound(request.getParameter("lowerBound"));
		
		validator.validate(proc);
		
		if(request.getParameter("numericalResult") == null || request.getParameter("numericalResult").equals("")){
			throw new FormValidationException();
		}
		
		if(request.getParameter("upperBound") == null || request.getParameter("upperBound").equals("")){
			throw new FormValidationException();
		}
		
		if(request.getParameter("lowerBound") == null || request.getParameter("lowerBound").equals("")){
			throw new FormValidationException();
		}
		
		
		action.submitResults(request.getParameter("pidTest"), request.getParameter("numericalResult"), request.getParameter("numericalResultUnit"), request.getParameter("upperBound"), request.getParameter("lowerBound"));
		
		//First checks to see if the testing list is empty. This keeps it from throwing an exception
		if(lpTesting.isEmpty() && !lpReceived.isEmpty()){
			action.setToTesting(lpReceived.get(0).getProcedureID());
		}
		loggingAction.logEvent(TransactionType.LAB_RESULTS_RECORD, loggedInMID.longValue(), pid, "Added results for a lab procedure.");
		response.sendRedirect(request.getContextPath()+"/auth/lt/allProcedures.jsp?action=complete");

	} catch (FormValidationException e) {
		response.sendRedirect(request.getContextPath()+"/auth/lt/allProcedures.jsp?action=error&numericalResult=" + request.getParameter("numericalResult") + "&numericalResultUnit=" + request.getParameter("numericalResultUnit") + "&upperBound=" + request.getParameter("upperBound") + "&lowerBound=" + request.getParameter("lowerBound"));
	}
}

if("transitForm".equals(submittedFormName)){
	action.submitReceived(request.getParameter("inTransitSubmitBtn"));
	loggingAction.logEvent(TransactionType.LAB_RESULTS_RECEIVED, loggedInMID.longValue(), pid, "Received a Lab Procedure");
	response.sendRedirect(request.getContextPath()+"/auth/lt/allProcedures.jsp?action=received");
}


if(lpTesting.isEmpty() && !lpReceived.isEmpty()){
	boolean work = action.setToTesting(lpReceived.get(0).getProcedureID());
	//Essentially refreshed by making a redirect to the same page in which the form was submitted
	if(actionMessage.equals("complete")){
		response.sendRedirect(request.getContextPath()+"/auth/lt/allProcedures.jsp?action=complete");
	} else if(actionMessage.equals("error")){
		response.sendRedirect(request.getContextPath()+"/auth/lt/allProcedures.jsp?action=error");
	} else {
		response.sendRedirect(request.getContextPath()+"/auth/lt/allProcedures.jsp?action=received");
	}
}

%>

<%loggingAction.logEvent(TransactionType.LAB_RESULTS_VIEW_QUEUE, loggedInMID.longValue(), pid, "Viewed Lab Procedure queue.");%>
<%@include file="testing.jsp" %>
<%@include file="received.jsp" %>
<%@include file="inTransit.jsp" %>

<%@include file="/footer.jsp" %>
