<%@page import="edu.ncsu.csc.itrust.beans.OfficeVisitBean"%>
<%@page import="edu.ncsu.csc.itrust.beans.PersonnelBean"%>
<%@page import="edu.ncsu.csc.itrust.beans.LabProcedureBean"%>
<%@page import="edu.ncsu.csc.itrust.beans.LOINCbean" %>
<%@page import="edu.ncsu.csc.itrust.action.ViewMyRecordsAction"%>
<%@page import="edu.ncsu.csc.itrust.dao.DAOFactory"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@page import="java.text.DateFormat"%>

<%@page errorPage="/auth/exceptionHandler.jsp" %>
<%@taglib prefix="itrust" uri="/WEB-INF/tags.tld" %>

<%@include file="/global.jsp"%>

<%
	pageTitle = "iTrust - View Lab Procedures";
%>

<%@include file="/header.jsp"%>
<%
loggingAction.logEvent(TransactionType.LAB_RESULTS_VIEW, loggedInMID.longValue(), 0, "");

ViewMyRecordsAction action = new ViewMyRecordsAction(prodDAO, loggedInMID.longValue());
List<LabProcedureBean> procs = action.getLabs();

action.setViewed(procs);

%>

<br />
<table class="fTable" align="center" id="labProceduresTable">
	<tr>
		<th colspan="10">Lab Procedures</th>
	</tr>
	<tr class="subHeader">
	    <td>HCP</td>
        <td>Office Visit Date</td>
        <td>Lab Procedure</td>
        <td>Status</td>
        <td>HCP Comments</td>
        <td colspan="2">Numerical<br/>Result</td>
        <td>Normality</td>
        <td>Updated Date</td>
        <td>Lab Procedure Chart</td>
	</tr>
<%
	if(procs.size() > 0 ) {
		for (LabProcedureBean bean : procs) {
			OfficeVisitBean ovbean = action.getCompleteOfficeVisit(bean.getOvID());
			String ovid = Long.toString(ovbean.getID());
			String hcpname = action.getPersonnel(ovbean.getHcpID()).getFullName();
			String ovdate = ovbean.getVisitDateStr();
			String status = bean.getStatus();
			String result = bean.getResults();
			String commentary = "";
			String numericalResult = "";
			String numericalResultUnit = "";
			String lowerBound = "";
			String upperBound = "";
			String normality = "Normal";
			
			String loincnum = bean.getLoinc();
					
			List<LOINCbean> loincs = action.getProcedureName(loincnum);
				
			if (status.equals(LabProcedureBean.Completed)) {
			
	            commentary =       StringEscapeUtils.escapeHtml("" + (bean.getCommentary()));
	            numericalResult =  StringEscapeUtils.escapeHtml("" + (bean.getNumericalResult()));
	            numericalResultUnit = StringEscapeUtils.escapeHtml("" + (bean.getNumericalResultUnit()));
	            lowerBound =       StringEscapeUtils.escapeHtml("" + (bean.getLowerBound()));
	            upperBound =       StringEscapeUtils.escapeHtml("" + (bean.getUpperBound()));
	            
	            float res = java.lang.Float.parseFloat(numericalResult);
	            float lower = java.lang.Float.parseFloat(lowerBound);
	            float upper = java.lang.Float.parseFloat(upperBound);
	            
	            if ( res < lower ) {
	            	
	            	normality = "Abnormal";
	            	
	            } 
	            
	            if ( res > upper ) {
	            	
	            	normality = "Abnormal";
	            	
	            }
	            
			}
%>
			<tr>
				<td><%= StringEscapeUtils.escapeHtml(hcpname) %></td>
                <td>
                    <a href="viewOfficeVisit.jsp?ovID=<%= ovid %>"><%= StringEscapeUtils.escapeHtml(ovdate) %></a>
                </td>
				<td><%= StringEscapeUtils.escapeHtml("" + (loincs.get(0).getComponent())) %></td>
		        <td><%= status %></td>
		        <td><%= commentary %></td>
		        <td><%= numericalResult %></td>
		        <td><%= numericalResultUnit %></td>
		        <td><%= normality %></td>
		        <td><%= StringEscapeUtils.escapeHtml("" + (bean.getTimestamp())) %></td>
		        <td>(<a href="/iTrust/auth/patient/LabResultsChart.jsp?dataType=<%=loincs.get(0).getLabProcedureCode()%>" id="viewResultsChart">View Chart</a>)</td>
			</tr>
<%
		}
	}
	else {
%>
		<tr>
			<td colspan=10 align=center>No Lab Procedures!</td>
		</tr>
<%
	}
%>
</table>
<br />
<%@include file="/footer.jsp"%>
