<%@page language="java" 
        contentType="text/html; charset=ISO-8859-1" 
        pageEncoding="ISO-8859-1"
        errorPage="/logout.jsp"%>
         
<%@page import="edu.ncsu.csc.itrust.model.old.enums.TransactionType"%>
<%@page import="edu.ncsu.csc.itrust.model.old.dao.DAOFactory"%>

<%@page import="edu.ncsu.csc.itrust.DBUtil"%>

<%@include file="/global.jsp" %>
<%@include file="/authenticate.jsp" %>

<%

if(request.getUserPrincipal() != null) {
	
	long mid = Long.valueOf(request.getUserPrincipal().getName());
	
	if (request.isUserInRole("patient")) {
		response.sendRedirect("patient/home.jsp");
		return;
	} 
	else if (request.isUserInRole("uap")) {
		response.sendRedirect("uap/home.jsp");
		return;	
	}
	else if (request.isUserInRole("hcp")) {
		response.sendRedirect("hcp/home.jsp");
		return;	
	}
	else if (request.isUserInRole("er")) {
		response.sendRedirect("er/home.jsp");
		return;	
	}
	else if (request.isUserInRole("pha")) {
		response.sendRedirect("pha/home.jsp");
		return;
	}
	else if (request.isUserInRole("admin")) {
		response.sendRedirect("admin/home.jsp");
		return;
	}
	else if (request.isUserInRole("tester")) {
		response.sendRedirect("tester/home.jsp"); //operationprofile
		return;
	}
	else if (request.isUserInRole("lt")) {
		response.sendRedirect("lt/home.jsp");
		return;
	}
	else if(!validSession) {
		session.invalidate();
		response.sendRedirect("/iTrust/");
	}
	else if (mid == 0)
	{
		session.invalidate();
	}
	else {
		
		response.sendRedirect("errors/noaccess.jsp");
	}
}

%>
