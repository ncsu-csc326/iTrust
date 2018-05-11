<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@page import="org.apache.commons.lang.StringEscapeUtils"%>

<%@include file="/authenticate.jsp" %>

<%
	if(validSession) {
		errorMessage = (String) session.getAttribute("errorMessage");
		session.removeAttribute("errorMessage");
	}
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<title><%= StringEscapeUtils.escapeHtml("" + (pageTitle )) %></title>
		<link href="/iTrust/css/main.css" type="text/css" rel="stylesheet" />
		<link href="/iTrust/css/datepicker.css" type="text/css" rel="stylesheet" />
		<script src="/iTrust/js/DatePicker.js" type="text/javascript"></script>
		<script src="/iTrust/js/jquery-1.7.2.js" type="text/javascript"></script>		
	</head>
	<body>
