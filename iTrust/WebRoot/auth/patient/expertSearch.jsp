<%@page errorPage="/auth/exceptionHandler.jsp" %>

<%@page import="java.util.List"%>
<%@page import="edu.ncsu.csc.itrust.model.old.dao.DAOFactory"%>
<%@page import="edu.ncsu.csc.itrust.model.old.beans.LOINCbean"%>
<%--@page import="edu.ncsu.csc.itrust.action.UpdateLabProcListAction"--%>
<%@page import="edu.ncsu.csc.itrust.action.UpdateLOINCListAction"%> 
<%@page import="edu.ncsu.csc.itrust.exception.FormValidationException"%>

<%@include file="/global.jsp" %>

<%
pageTitle = "iTrust - Find Expert Hospital";
%>

<%@include file="/header.jsp" %>

<div align=center>
<form name="mainForm" method="post">
<br />
<table class="fTable">
	<tr>
		<th colspan="7">Laboratory Procedures</th>
	</tr>
	<tr class="subHeader">
		<td>LOINC</td>
		<td>Specialty</td>
	</tr>
	<%
		List<LOINCbean> codeList = prodDAO.getLOINCDAO().getAllLOINC();
		String code = "";
		String specialty = "";
		
		for (LOINCbean codeEntry : codeList) {
			code = codeEntry.getLabProcedureCode();
			specialty = codeEntry.getComponent();
	%>
	<script type="text/javascript">	function DoNav(theUrl) {document.location.href = theUrl;}</script>
	<tr onclick="DoNav('viewExpertHospitals.jsp?Specialty=<%=specialty%>&LOINC=<%=code%>');">
		<td><a href="viewExpertHospitals.jsp?Specialty=<%=specialty%>&LOINC=<%=code %>" > <%= code %> </a></td>
		<td><a href="viewExpertHospitals.jsp?Specialty=<%=specialty%>&LOINC=<%=code %>" > <%= specialty %> </a></td>
	</tr>
	<% } %>
</table>
</form>
<br/>



</div>
<br />


<%@include file="/footer.jsp" %>
