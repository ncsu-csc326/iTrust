<%@page import="java.util.List"%>
<%@page import="java.util.ArrayList"%>
<%@page import="edu.ncsu.csc.itrust.beans.DiagnosisStatisticsBean"%>
<%@page import="edu.ncsu.csc.itrust.action.ViewDiagnosisStatisticsAction"%>
<%@page import="edu.ncsu.csc.itrust.charts.DiagnosisTrendData" %>


<!-- Use this tag to specify the location of the dataset for the chart -->
<jsp:useBean id="DSchart2" class="edu.ncsu.csc.itrust.charts.DiagnosisTrendData"/>

<%

// This calls the class from the useBean tag and initializes the Adverse Event list and pres/immu name

if ( view.equalsIgnoreCase("epidemics") ) {
	
	DSchart2.initializeAvgDiagnosisStatistics(avgBean, dsBean, view);
	
} else {
	
	return;
	
}

String chartTitle = "Diagnosis Cases Chart";

%>

<!-- The cewolf:chart tag defines attributes related to the chart you wish to generate -->
<cewolf:chart
     id="graph"
     title="<%= StringEscapeUtils.escapeHtml(chartTitle ) %>"
     type="verticalbar"
     xaxislabel="Cases By Zipcode and Region">
	<cewolf:data>
	       <cewolf:producer id="DSchart2"/>
	</cewolf:data>
</cewolf:chart>

<!-- The cewolf:img tag defines the actual chart in your JSP -->
<cewolf:img chartid="graph" renderer="/charts/" width="600" height="400" border="2"/>
