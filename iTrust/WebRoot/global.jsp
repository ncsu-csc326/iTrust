<%@page buffer="64kb" %>
<%@page import="edu.ncsu.csc.itrust.action.LoginFailureAction"%>
<%@page import="edu.ncsu.csc.itrust.model.old.dao.DAOFactory"%>
<%@page import="edu.ncsu.csc.itrust.model.old.dao.mysql.AuthDAO"%>
<%@page import="edu.ncsu.csc.itrust.action.EventLoggingAction"%>
<%@page import="edu.ncsu.csc.itrust.model.old.enums.TransactionType"%>

<%
DAOFactory prodDAO = DAOFactory.getProductionInstance(); 
AuthDAO authDAO    = prodDAO.getAuthDAO();
EventLoggingAction loggingAction = new EventLoggingAction(prodDAO);
LoginFailureAction loginFailureAction = (LoginFailureAction)session.getAttribute("loginFailureAction"); 

if(loginFailureAction == null)
{
	loginFailureAction = new LoginFailureAction(prodDAO, request.getRemoteAddr());
	session.setAttribute("loginFailureAction", loginFailureAction);
}



String pageTitle    = null;
String loginMessage = null;
String userName     = null; //"Andy Programmer";
String errorMessage = null;
String selectedPatientName = null;

boolean validSession = true;

Long loggedInMID = new Long(0L);
String userRole  = "";
try {
	loggedInMID = (Long) session.getAttribute("loggedInMID");
	userRole    = (String) session.getAttribute("userRole");

	if (userRole == null) {
		if (request.isUserInRole("patient")) {
			userRole = "patient";
		}
		else if (request.isUserInRole("uap")) {
			userRole = "uap";
		}
		else if (request.isUserInRole("hcp")) {
			userRole = "hcp";
		}
		else if (request.isUserInRole("er")) {
			userRole = "er";
		}
		else if (request.isUserInRole("admin")) {
			userRole = "admin";
		}
		else if (request.isUserInRole("pha")) {
			userRole = "pha";
		}
		else if (request.isUserInRole("tester")) {
			userRole = "tester";
		}
		else if (request.isUserInRole("lt")) {
			userRole = "lt";
		}
		session.setAttribute("userRole", userRole);
	}
	
} catch (IllegalStateException ise) {
	validSession = false;
}

%>
