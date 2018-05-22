<%@ page isErrorPage="true"%>
<%@page import="org.apache.commons.lang.StringEscapeUtils"%>
<%

if(exception!=null){
	exception.printStackTrace();
	request.getSession().setAttribute("errorMessage",exception.getClass().getSimpleName() + ": "+ StringEscapeUtils.escapeHtml(exception.getMessage()));
}
response.sendRedirect("/iTrust/auth/forwardUser.jsp");
%>