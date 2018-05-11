<%@taglib uri="/WEB-INF/tags.tld" prefix="itrust"%>
<%@page errorPage="/auth/exceptionHandler.jsp"%>
<%@page import="java.util.List"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.text.DateFormat"%>
<%@page import="edu.ncsu.csc.itrust.action.ViewMacronutrientsAction" %>
<%@page import="edu.ncsu.csc.itrust.beans.MacronutrientsBean" %>
<%@page import="edu.ncsu.csc.itrust.action.ViewFoodEntryAction"%>
<%@page import="edu.ncsu.csc.itrust.beans.FoodEntryBean"%>
<%@page import="edu.ncsu.csc.itrust.dao.mysql.AuthDAO"%>

<%@include file="/global.jsp"%>

<%
	pageTitle = "iTrust - View Patient Macronutrients";
%>

<%@include file="/header.jsp"%>

<style type="text/css">
	
	div.intake_rows {
		height:300px;
	}
	
	div.intake_rows div {
		position:relative;
		float:left;
		margin:0px;
	}

</style>

<itrust:patientNav />

<h2 style="text-align:center;"><%=selectedPatientName + "'s Macronutrient Intake"%></h2>

<%
	/* Require a Patient ID first */
	String pidString = (String) session.getAttribute("pid");
	if (pidString == null || 1 > pidString.length()) {
		response.sendRedirect("/iTrust/auth/getPatientID.jsp?forward=/iTrust/auth/hcp-nutritionist/macronutrients.jsp");
		return;
	}
	
	long pid = Long.parseLong(pidString);
	
	loggingAction.logEvent(TransactionType.HCP_VIEW_MACRONUTRIENTS, 
			loggedInMID.longValue(), pid, "");
	
	double expectedCarbs = 0.0;
	double expectedProtein = 0.0;
	double expectedFat = 0.0;
			
	final double PERCENT = 100.0;
		
	boolean formIsFilled = request.getParameter("formIsFilled") != null
			&& request.getParameter("formIsFilled").equals("true");
	
	String expCarbsStr = request.getParameter("expectedCarbs");
	String expProteinStr = request.getParameter("expectedProtein");
	String expFatStr = request.getParameter("expectedFat");
	
	loggingAction.logEvent(TransactionType.VIEW_MACRONUTRIENTS_GRAPH, 
			loggedInMID.longValue(), pid, "");
	
	boolean isInputValid = true;
	String errorText = "";
	
	if (formIsFilled) {
		
		if (expCarbsStr.indexOf("%") != -1) expCarbsStr = expCarbsStr.substring(0, expCarbsStr.indexOf("%"));
		if (expProteinStr.indexOf("%") != -1) expProteinStr = expProteinStr.substring(0, expProteinStr.indexOf("%"));
		if (expFatStr.indexOf("%") != -1) expFatStr = expFatStr.substring(0, expFatStr.indexOf("%"));    			
		
		expectedCarbs = Double.parseDouble(expCarbsStr);
		expectedProtein = Double.parseDouble(expProteinStr);
		expectedFat = Double.parseDouble(expFatStr);
		
		if (expectedCarbs < 45.0 || expectedCarbs > 65.0) {
			isInputValid = false;
			errorText += "! Expected carbohydrate out of range";
		}
		if (expectedProtein < 10.0 || expectedProtein > 35.0) {
			isInputValid = false;
			errorText += "! Expected protein out of range";
		}
		if (expectedFat < 20.0 || expectedFat > 35.0) {
			isInputValid = false;
			errorText += "! Expected fat out of range";
		}
		
		loggingAction.logEvent(TransactionType.CALCULATE_MACRONUTRIENTS, 
				loggedInMID.longValue(), pid, "");
	}
	
	double expectedTotal = expectedCarbs + expectedProtein + expectedFat;
	
	if (expectedTotal > PERCENT) {
		errorText += "! Total exceeds 100%";
		isInputValid = false;
	}
	
	if (!isInputValid || !formIsFilled) {
		
		expectedCarbs = 55.0;
		expectedProtein = 17.0;
		expectedFat = 28.0;
		
		expCarbsStr = "55";
		expProteinStr = "17";
		expFatStr = "28";
	}
	
	String startDate = request.getParameter("startDate");
	String endDate = request.getParameter("endDate");
	

	// Are we viewing within a specific range?
	boolean dateRange = ( startDate != null && endDate != null ) && ( !startDate.equals("") && !endDate.equals(""));

	//create a new action for viewing all of the food diary stuff
	ViewFoodEntryAction action = new ViewFoodEntryAction(prodDAO, loggedInMID.longValue());
	action.getDiary(pid);
	List<FoodEntryBean> foodBeans = action.getDiaryTotals(pid);
	
	String startDateStr = "";
	String endDateStr = "";
	
try {
	if (dateRange) {
		startDateStr = request.getParameter("startDate");
		endDateStr = request.getParameter("endDate");
		
		foodBeans = action.getBoundedDiaryTotals(startDateStr, endDateStr, pid);
	}
} catch(Exception e) {
	errorText += "! Enter dates in MM/dd/yyyy";
	isInputValid = false;
}

	if (!isInputValid) out.println("<div align='center'><span class='iTrustError'>Please enter desired values within acceptable range" + errorText + "!</span></div>");
	
	ViewMacronutrientsAction msj = new ViewMacronutrientsAction(prodDAO, loggedInMID);
	MacronutrientsBean msjData = msj.getMsjData(pid);
	
	foodBeans = msj.reverse(foodBeans);
	
%>

<script type="text/javascript" src="https://www.google.com/jsapi"></script>

<script type="text/javascript">

      // Load the Visualization API and the piechart package.
      google.load('visualization', '1.0', {'packages':['corechart']});

      // Set a callback to run when the Google Visualization API is loaded.
      google.setOnLoadCallback(drawChart);

      // Callback that creates and populates a data table,
      // instantiates the pie chart, passes in the data and
      // draws it.
      function drawChart() {
		
    	var totalsArray = new Array(
    			['Date', 'Actual Calories', 'Expected Calories']
    	);
    	
    	var carbsArray = new Array(
    			['Date', 'Act', 'Exp']
    	);
    	
    	var proteinArray = new Array(
    			['Date', 'Act', 'Exp']
    	);
    	
    	var fatArray = new Array(
    			['Date', 'Act', 'Exp']
    	);
    	
        // Create the data table.
        var data = new google.visualization.DataTable();
        data.addColumn('string', 'Macronutrients');
        data.addColumn('number', 'Actual Totals');
        data.addColumn('number', 'Expected Totals');
    	
    	<%
    	
    	double msjVal = msjData.getMsj();
    	
    	final double CARB_CALS = 4.0;
    	final double PROTEIN_CALS = 4.0;
    	final double FAT_CALS = 9.0;
    	
    	double carbCalsTotal = 0.0;
    	double proteinCalsTotal = 0.0;
    	double fatCalsTotal = 0.0;
		
		double expCarbs = msjVal * expectedCarbs / PERCENT;
		double expProtein = msjVal * expectedProtein / PERCENT;
		double expFat = msjVal * expectedFat / PERCENT;
		
		double expCarbsTotal = foodBeans.size() * msjVal * expectedCarbs / PERCENT;
		double expProteinTotal = foodBeans.size() * msjVal * expectedProtein / PERCENT;
		double expFatTotal = foodBeans.size() * msjVal * expectedFat / PERCENT;
    	
    	for(FoodEntryBean b : foodBeans) {
    		double carbCals = b.getCarbGrams() * CARB_CALS;
    		double proteinCals = b.getProteinGrams() * PROTEIN_CALS;
    		double fatCals = b.getFatGrams() * FAT_CALS;
    		
    		carbCalsTotal += carbCals;
    		proteinCalsTotal += proteinCals;
    		fatCalsTotal += fatCals;
    		
    		String date = b.getDateEatenStr();
    		String dateStr = date.substring(0,5);
    		
    		double dailyCals = carbCals + proteinCals + fatCals;
    		
    		
    		out.println("totalsArray[totalsArray.length] = ['" + dateStr + "', " + dailyCals + ", " + msjVal + "];");
    		out.println("carbsArray[carbsArray.length] = ['" + dateStr + "', " + carbCals + ", " + expCarbs + "];");
    		out.println("proteinArray[proteinArray.length] = ['" + dateStr + "', " + proteinCals + ", " + expProtein + "];");
    		out.println("fatArray[fatArray.length] = ['" + dateStr + "', " + fatCals + ", " + expFat + "];");
    	}
    	
    	%>
    	
    	var intakeGraph = new google.visualization.arrayToDataTable(totalsArray);
    	
    	var carbsGraph = new google.visualization.arrayToDataTable(carbsArray);
    	var proteinGraph = new google.visualization.arrayToDataTable(proteinArray);
    	var fatGraph = new google.visualization.arrayToDataTable(fatArray);
    	
        data.addRows([
          ['Carbohydrate', <%= carbCalsTotal %>, <%= expCarbsTotal %>],
          ['Protein', <%= proteinCalsTotal %>, <%= expProteinTotal %>],
          ['Fat', <%= fatCalsTotal %>, <%= expFatTotal %>]
        ]);

        // Set chart options
        var intakeOptions = {'title':'Macronutrient Intake',
        				'curveType:':'function',
						'width':600,
						'height':400};
        
        var totalsOptions = {'title':'Macronutrient Intake Totals',
				'width':600,
				'height':400};
        
        var carbsOptions = {'title':'Carbohydrate Intake',
				'curveType:':'function',
				'width':400,
				'height':200};
        
        var proteinOptions = {'title':'Protein Intake',
				'curveType:':'function',
				'width':400,
				'height':200};
        
        var fatOptions = {'title':'Fat Intake',
				'curveType:':'function',
				'width':400,
				'height':200};

        // Instantiate and draw our chart, passing in some options.
        var intakeTotalsChart = new google.visualization.LineChart(document.getElementById('chart_div_intake_totals'));
        intakeTotalsChart.draw(intakeGraph, intakeOptions);
        
        var carbsTotalsChart = new google.visualization.LineChart(document.getElementById('chart_div_carbs_intake'));
        carbsTotalsChart.draw(carbsGraph, carbsOptions);
        
        var proteinTotalsChart = new google.visualization.LineChart(document.getElementById('chart_div_protein_intake'));
        proteinTotalsChart.draw(proteinGraph, proteinOptions);
        
        var fatTotalsChart = new google.visualization.LineChart(document.getElementById('chart_div_fat_intake'));
        fatTotalsChart.draw(fatGraph, fatOptions);

        var totalsChart = new google.visualization.ColumnChart(document.getElementById('chart_div_totals'));
        totalsChart.draw(data, totalsOptions);
      }
</script>





<div align="center">
	
	<%
	
	
		if (foodBeans.size() > 0) {
	%>
	
	<div style="border:1px solid #F00;">
		<h3 style="text-decoration:underline;">
			Mifflin-St Jeor
		</h3>
		
		<h3>
			<b style="color:#F00;"><%=
			
			msjVal
			
			%></b> Macronutrient Calories / day
		</h3>
	</div>
	
	
	<form action="macronutrients.jsp" method="post">
		<table>
			<tr class="subHeader">
				<td>Start Date:</td>
				<td><input name="startDate"
					value="<%=StringEscapeUtils.escapeHtml((startDate == null ? "" : startDate))%>"
					size="10"> <input type="button" value="Select Date"
					onclick="displayDatePicker('startDate');"></td>
				<td>End Date:</td>
				<td><input name="endDate"
					value="<%=StringEscapeUtils.escapeHtml((endDate == null ? "" : endDate))%>"
					size="10">
					<input type="button" value="Select Date"
					onclick="displayDatePicker('endDate');"></td>
				<td><input type="submit" name="btn_filter"
					value="Filter Entries"></td>
			</tr>
		</table>
	
		<div class="intake_rows">
		<input type="hidden" name="formIsFilled" value="true" />
			<div>
				Expected Carbohydrate (45-65%):
				<input type="text" size="10" name="expectedCarbs" value="<%= expCarbsStr %>%" />
				<br />
				<div id="chart_div_carbs_intake">Carbohydrate Intake</div>
			</div>
			
			<div>
				Expected Protein (10-35%):
				<input type="text" size="10" name="expectedProtein" value="<%= expProteinStr %>%" />
				<br />
				<div id="chart_div_protein_intake">Protein Intake</div>
			</div>
			
			<div>
				Expected Fat (20-35%):
				<input type="text" size="10" name="expectedFat" value="<%= expFatStr %>%" />
				<br />
				<div id="chart_div_fat_intake">Fat Intake</div>
			</div>
			
			<div style="margin-left:40%;">
				<input type="submit" value="Re-calculate graphs" />
			</div>
			
		</div>
	</form>
	
	<div class="intake_rows">
		<div id="chart_div_intake_totals">Macronutrient Intake</div>
		
		<div id="chart_div_totals">Macronutrient Intake Totals</div>
	</div>
	<%
		} else {
	%>
	<div>
		<i>The selected patient's food diary is empty. If you were expecting entries please try again later!</i>
	</div>
	<%
		}
	%>

</div>
<%@include file="/footer.jsp"%>
