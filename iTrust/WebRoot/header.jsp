<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@taglib uri='/WEB-INF/cewolf.tld' prefix='cewolf'%>
<%@page import="org.apache.commons.lang.StringEscapeUtils"%>

<%@include file="/authenticate.jsp"%>

<%
	if (validSession) {
		errorMessage = (String) session.getAttribute("errorMessage");
		session.removeAttribute("errorMessage");
	}

	if (loggedInMID != null
			&& session.getAttribute("loginFlag") != null
			&& session.getAttribute("loginFlag").equals("true")) {
		loggingAction.logEvent(TransactionType.LOGIN_SUCCESS,
				loggedInMID, loggedInMID, "");
		session.removeAttribute("loginFlag");
	}

	if (request.getRequestURI().contains("home.jsp")) {
		session.removeAttribute("pid");
	}
%>
<!DOCTYPE HTML>
<html>
<head>
<title><%=StringEscapeUtils.escapeHtml("" + (pageTitle))%></title>
	<meta name="viewport" content="width=device-width, initial-scale=1, user-scalable=no">
	<link href="/iTrust/css.jsp" type="text/css" rel="stylesheet" />
	<link href="/iTrust/css/bootstrap.min.css" rel="stylesheet" />
	<link href="/iTrust/css/dashboard.css" rel="stylesheet" />
	<link href="/iTrust/css/datepicker.css" type="text/css" rel="stylesheet" />
	<script src="/iTrust/js/DatePicker.js" type="text/javascript"></script>
	<script src="/iTrust/js/jquery-1.8.3.js" type="text/javascript"></script>
	<script src="/iTrust/js/SwipeableElem.js" type="text/javascript"></script>
	<script src="/iTrust/js/slidyRabbit.js" type="text/javascript"></script>
	
</head>
<body>
		<div class="navbar navbar-inverse navbar-fixed-top top-border" role="navigation">
			<div class="container-fluid">
				<div class="navbar-header">
				  <button type="button" class="navbar-toggle" id="toggleMenu" style="color:#FFFFFF; font-size: 8pt;">
				  	<span class="glyphicon glyphicon-chevron-right"></span>
				  </button>
		          <button type="button" class="navbar-toggle" data-toggle="collapse" data-target=".navbar-collapse">
		            <span class="sr-only">Toggle navigation</span>
		            <span class="icon-bar"></span>
		            <span class="icon-bar"></span>
		            <span class="icon-bar"></span>
		          </button>
		          <a class="navbar-brand" href="/iTrust/"><img src="/iTrust/image/itrust-logo.png"></a>
		        </div>
				<div class="navbar-collapse collapse">
					<%
						if (validSession) {
	
							if ((loggedInMID != null) && (loggedInMID.longValue() != 0L)) //if no one is logged in
							{
					%>
	
					<ul class="nav navbar-nav navbar-right">
						<span class="welcome">
						<%out.println("Welcome, "
							+ StringEscapeUtils.escapeHtml("" + userName)
							+ "<BR/>");%>
						</span>
						<li><a href="/iTrust">Home</a></li>
						<li><a href="/iTrust/logout.jsp">Logout</a></li>
						<li><a href="/iTrust/auth/changePassword.jsp">Change Password</a></li>
					</ul>
				</div>
				<div id="iTrustSelectedPatient">
					<%
						if (session.getAttribute("pid") != null
										&& ((String) session.getAttribute("pid")).length() > 0
										&& !session.getAttribute("pid").equals("null"))
								//if(session.getAttribute("pid") != null)
								{
					%>
					<span class="selectedPatient"> Viewing information for <b><%=selectedPatientName%></b>
						| <a
						href="/iTrust/auth/getPatientID.jsp?forward=<%=request.getRequestURI()%>">Select
							a Different Patient</a> </span>
					<%
						}
					%>
	
					<%
						} else { //no one is logged in
					%>
						</div>
					<%
						}
						} //valid session
					%>
	
				</div>
			</div>
		</div>
		
		
		<div class="container-fluid">
		<div class="row <% if (request.getRequestURL().indexOf("/login.jsp") != -1) {%>home-row<%} %>">
		
			<div id="iTrustMenu" class="col-sm-4 col-md-3 sidebar">
				<!-- 	<img id="menuPic" src="/iTrust/image/new/menu.png"  /> 
				<img src="/iTrust/image/new/menu_top.png"  /> -->
				<div class="iTrustMenuContents">
					<%
						if (validSession) {
							if ((loggedInMID != null) && (loggedInMID.longValue() != 0L)) //someone is logged in
							{
								if (userRole.equals("patient")) {
					%><%@include file="/auth/patient/menu.jsp"%>
					<%
						} else if (userRole.equals("uap")) {
					%><%@include file="/auth/uap/menu.jsp"%>
					<%
						} else if (userRole.equals("hcp")) {
					%><%@include file="/auth/hcp/menu.jsp"%>
					<%
						} else if (userRole.equals("er")) {
					%><%@include file="/auth/er/menu.jsp"%>
					<%
						} else if (userRole.equals("pha")) {
					%><%@include file="/auth/pha/menu.jsp"%>
					<%
						} else if (userRole.equals("admin")) {
					%><%@include file="/auth/admin/menu.jsp"%>
					<%
						} else if (userRole.equals("tester")) {
					%><%@include file="/auth/tester/menu.jsp"%>
					<%
						} else if (userRole.equals("lt")) {
					%><%@include file="/auth/lt/menu.jsp"%>
					<%
						}
							} //no one is logged in	
							else {
								String uri = request.getRequestURI();
								if (uri.indexOf("privacyPolicy.jsp") >= 0) { //looking at privacy policy, include logout menu.
					%>
					<%@include file="logoutMenu.jsp"%>
					<%
						} else { //we are actually logged out entirely, show login menu
					%>
					<%@include file="loginMenu.jsp"%>
					<%
						} //else
							} //else
						} //if valid session
						else {
					%>
					<%@include file="/logoutMenu.jsp"%>
					<%
						}
					%>
				</div>
				<!-- <img src="/iTrust/image/new/menu_bottom.png"  /> -->
			</div>
			<div id="iTrustPage" class="col-sm-8 col-sm-offset-4 col-md-9 col-md-offset-3 main">
				<div id="iTrustContent" id="m">
					<%
						if (errorMessage != null) {
					%>
					<div
						style="text-align: center; width: 100%; background-color: white; border:1px solid #cc0000">
						<span style="color: #cc0000; font-size: 20px; font-weight: bold;"><%=StringEscapeUtils.escapeHtml(errorMessage)%></span>
					</div>
					<%
						}
					%>
