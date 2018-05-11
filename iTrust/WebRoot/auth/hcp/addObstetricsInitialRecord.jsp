<%@taglib uri="/WEB-INF/tags.tld" prefix="itrust"%>
<%@page errorPage="/auth/exceptionHandler.jsp"%>

<%@page import="java.util.List"%>
<%@page import="java.util.ArrayList"%>
<%@page import="edu.ncsu.csc.itrust.action.ViewPatientAction"%> <!-- Used for gender-checking -->
<%@page import="edu.ncsu.csc.itrust.enums.Gender"%> <!-- Used for gender-checking -->
<%@page import="edu.ncsu.csc.itrust.beans.PatientBean"%> <!-- Used for gender-checking -->
<%@page import="edu.ncsu.csc.itrust.exception.ITrustException"%> <!-- Used for gender-checking -->
<%@page import="edu.ncsu.csc.itrust.action.ViewPersonnelAction"%> <!-- Used for specialty-checking -->
<%@page import="edu.ncsu.csc.itrust.beans.PersonnelBean"%> <!-- Used for specialty-checking -->
<%@page import="edu.ncsu.csc.itrust.action.AddObstetricsAction"%>
<%@page import="edu.ncsu.csc.itrust.action.ViewObstetricsAction"%>
<%@page import="edu.ncsu.csc.itrust.beans.ObstetricsRecordBean"%>
<%@page import="edu.ncsu.csc.itrust.beans.FlagsBean"%>
<%@page import="edu.ncsu.csc.itrust.enums.DeliveryType"%>
<%@page import="edu.ncsu.csc.itrust.enums.PregnancyStatus"%>
<%@page import="edu.ncsu.csc.itrust.enums.FlagValue"%>
<%@page import="edu.ncsu.csc.itrust.exception.FormValidationException" %>

<%@include file="/global.jsp"%>

<%pageTitle = "iTrust - Initialize Obstetrics Record";%>

<%@include file="/header.jsp"%>
<%
	/* Require a Patient ID first */
	String pidString = (String) session.getAttribute("pid");
	if (pidString == null || 1 > pidString.length()) {
		response.sendRedirect("/iTrust/auth/getPatientID.jsp?forward=/iTrust/auth/hcp/addObstetricsInitialRecord.jsp");
		return;
	}
	
	//if we do have a patient ID, check their gender
	ViewPatientAction patientAction = new ViewPatientAction(prodDAO, loggedInMID, pidString);
	PatientBean chosenPatient = patientAction.getPatient(pidString);

	//this test works and so does the redirect, but it has to be an exeption that's thrown for it to show up
	if (chosenPatient == null || !chosenPatient.getGender().equals(Gender.Female)) {
		response.sendRedirect("/iTrust/auth/getPatientID.jsp?forward=/iTrust/auth/hcp/addObstetricsInitialRecord.jsp");
		throw new ITrustException("The patient is not eligible for obstetrics care."); //this shows up
	}
	
	//if specialty is not ob/gyn, throw an error and return them to the obstetrics homepage
	ViewPersonnelAction personnelAction = new ViewPersonnelAction(prodDAO, loggedInMID);
	PersonnelBean currentPersonnel = personnelAction.getPersonnel("" + loggedInMID);
	if (!currentPersonnel.getSpecialty().equalsIgnoreCase("ob/gyn")) {
		response.sendRedirect("/iTrust/auth/hcp/obstetricsHome.jsp");
		throw new ITrustException("Initialize obstetrics patients not available for non-obstetrics HCPs."); //this shows up
	}
	
	//prepare to add beans!
	AddObstetricsAction addAction = new AddObstetricsAction(prodDAO, loggedInMID);
	boolean addedSomething = false;
	
	//add the initial record
	String lmp = request.getParameter("lmp");
	String edd = request.getParameter("edd");
	String date = request.getParameter("date");
	String weeksPregnant = request.getParameter("weeksPregnant");
	
	//don't run unless the form was actually submitted
	if ("true".equals(request.getParameter("formIsFilled"))) {
		try {
			//grab all past pregnancies and make records for those, too
			int numPriors = 0;
			int numMiscarriages = 0;
			ArrayList<FlagsBean> initialFlags = new ArrayList<FlagsBean>();
			String yearConception = request.getParameter("yearConception" + numPriors);
			String weeksPregnantTemp = request.getParameter("weeksPregnant" + numPriors);
			String daysPregnantTemp = request.getParameter("daysPregnant" + numPriors);
			String weeksPregnantFull = weeksPregnantTemp + "-" + daysPregnantTemp;
			String hoursLabor = request.getParameter("hoursInLabor" + numPriors);
			String deliveryType = request.getParameter("deliveryType" + numPriors); //will be null if add button was never pressed
			while (deliveryType != null && weeksPregnantFull != null && yearConception != null
					&& !weeksPregnantFull.equals("") && !yearConception.equals("")) {
				String errors = ""; //added to if parsing fails
				//parse out the things
				int yearConceptionInt = 0;
				try {
					yearConceptionInt = Integer.parseInt(yearConception);
				} catch (NumberFormatException e) {
					errors += "Year conception must be an integer. ";
				}
				double hoursLaborDouble = 0;
				if (hoursLabor != null && !hoursLabor.equals("")) {
					try {
						hoursLaborDouble = Double.parseDouble(hoursLabor);
					} catch (NumberFormatException e) {
						errors += "Hours in labor must be a double. ";
					}
				}
				
				DeliveryType deliveryTypeEnum = DeliveryType.valueOf(deliveryType.split("[ ]")[0]);
				
				if (errors.equals("")) {
					//finally, make the bean
					ObstetricsRecordBean prior = new ObstetricsRecordBean();
					prior.setMid(Long.parseLong(pidString));
					prior.setYearConception(yearConceptionInt);
					prior.setDateVisit("01/01/" + yearConceptionInt);
					prior.setWeeksPregnant(weeksPregnantFull);
					prior.setHoursInLabor(hoursLaborDouble);
					prior.setDeliveryType(deliveryTypeEnum);
					prior.setPregnancyStatus(PregnancyStatus.Complete);
					ArrayList<FlagsBean> priorFlags = new ArrayList<FlagsBean>();
					//check for twins, set the appropriate flag
					if (request.getParameter("twins" + numPriors) != null) {
						FlagsBean twinsFlag = new FlagsBean();
						twinsFlag.setMid(prior.getMid());
						twinsFlag.setFlagValue(FlagValue.Twins);
						twinsFlag.setFlagged(request.getParameter("twins" + numPriors).equals("on"));
						priorFlags.add(twinsFlag);
					}
					//check for genetic miscarriage, set the appropriate flag
					if (deliveryTypeEnum == DeliveryType.Miscarriage) {
					  	numMiscarriages++;
					}
					if (numMiscarriages > 1) {
						FlagsBean miscarriageFlag = new FlagsBean();
						miscarriageFlag.setMid(prior.getMid());
						miscarriageFlag.setFlagValue(FlagValue.GeneticMiscarriage);
						miscarriageFlag.setFlagged(true);
						initialFlags.add(miscarriageFlag);
					}
					
					addAction.addObstetricsRecord(prior, priorFlags);
				}
				else {
					throw new FormValidationException(errors);
				}
				
				//and increment
				numPriors++;
				yearConception = request.getParameter("yearConception" + numPriors);
				weeksPregnantTemp = request.getParameter("weeksPregnant" + numPriors);
				daysPregnantTemp = request.getParameter("daysPregnant" + numPriors);
				weeksPregnantFull = weeksPregnantTemp + "-" + daysPregnantTemp;
				hoursLabor = request.getParameter("hoursInLabor" + numPriors);
				deliveryType = request.getParameter("deliveryType" + numPriors);
			}
			
			ObstetricsRecordBean initialBean = new ObstetricsRecordBean();
			
			initialBean.setMid(Long.parseLong(pidString));
			initialBean.setLmp(lmp);
			initialBean.setDateVisit(date);
			initialBean.setEdd(edd);
			initialBean.setWeeksPregnant(weeksPregnant);
			initialBean.setPregnancyStatus(PregnancyStatus.Initial);
			initialBean.setDeliveryType(DeliveryType.Vaginal);
			
			addAction.addObstetricsRecord(initialBean, initialFlags);
			addedSomething = true;
			
			if (addedSomething) {
				response.sendRedirect("/iTrust/auth/hcp/obstetricsHome.jsp?initial");
			}
		}
		catch(FormValidationException e) {
			out.write("<p class=\"iTrustError\">" + e.getMessage() + "</p>");
		}
	}
	
%>

<div id="mainpage" align=center>
	<form action="/iTrust/auth/hcp/addObstetricsInitialRecord.jsp" method="post" id="newRecord">
		<input type="hidden" name="formIsFilled" value="true" />
		<table class="fTable" align="center">
			<tr><th colspan="3">Initialize New Pregnancy</th></tr>
			<tr>
				<td width="200px"><label for="lmp">Last menstrual period: </label></td>
				<td width="200px">
					<input type="text" name="lmp" id="lmp" onchange="calculateEDD();calculateWeeksPregnant();" size="7" />
					<input type="button" value="Select Date" onclick="displayDatePicker('lmp');" />
				</td>
				<td id="lmp-invalid" width="200px"></td>
			</tr>
			<tr>
				<td><label for="date">Date of visit: </label></td>
				<td>
					<input type="text" name="date" id="date" onchange="calculateWeeksPregnant();" size="7" />
					<input type="button" value="Select Date" onclick="displayDatePicker('date');" />
				</td>
				<td id="date-invalid"></td>
			</tr>
			<tr>
				<td><label for="edd">Estimated delivery date: </label></td>
				<td><input readonly type="text" name="edd" id="edd" /></td>
				<td></td>
			</tr>
			<tr>
				<td><label for="weeksPregnant">Weeks-Days pregnant: </label></td>
				<td><input readonly type="text" name="weeksPregnant" id="weeksPregnant" /></td>
				<td></td>
			</tr>
		</table>
		
		<br />
		<div id="priorPregnancies"></div>
		<p><button type="button" id="priorPregnancy" onclick="showNewPriorPregnancyForm()">Add Prior Pregnancy</button></p>
		
		<input type="submit" id="submit" value="Submit" />
	</form>
	
	<!-- Show any past pregnancies already existing in the system here -->
	<%
		ViewObstetricsAction viewObstetrics = new ViewObstetricsAction(prodDAO, loggedInMID);
		List<ObstetricsRecordBean> priorPregnancies = viewObstetrics.getViewableObstetricsRecordsByMIDType(Long.parseLong(pidString), PregnancyStatus.Complete);
		if (priorPregnancies != null && !priorPregnancies.isEmpty()) {
			// Use a StringBuilder here to be memory efficient.
			// Create the table of past pregnancies.
			StringBuilder past = new StringBuilder();
			past.append("<br /><h2>Past Pregnancies</h2><table class=\"fTable\">")
			.append("<tr>")
				.append("<th>Year of Conception</th>")
				.append("<th># Weeks Pregnant</th>")
				.append("<th># Hours in Labor</th>")
				.append("<th>Delivery Type</th>")
			.append("</tr>");
			int count = 0;
			for (ObstetricsRecordBean bean : priorPregnancies) {
				if (bean != null) {
					past.append("<tr>")
						.append("<td>").append(bean.getYearConception()).append("</td>")
						.append("<td>").append(bean.getWeeksPregnant()).append("</td>")
						.append("<td>").append(bean.getHoursInLabor()).append("</td>")
						.append("<td>").append(bean.getDeliveryType().toString()).append("</td>")
					.append("</tr>");
					count++;
				}
			}
			past.append("</table>");
			//only if we found any actual past pregnancies do we show the resulting form
			if (count != 0)
				out.write(past.toString());
		}
	%>
</div>
<p><br/><a href="/iTrust/auth/hcp/obstetricsHome.jsp">Back to Home</a></p>

<script type="text/javascript">
	//called when (you guessed it) the Date Picker is closed.
	function datePickerClosed() {
		var lmp = document.getElementById("lmp");
		var date = document.getElementById("date");
		if (lmp.value != "") {
			calculateEDD();
		}
		if (date.value != "") {
			calculateWeeksPregnant();
		}
	}

	function calculateEDD() {
	    var lmp = document.getElementById("lmp");
	    if (lmp.value != "") {
		    var edd = document.getElementById("edd");
		    var lmpDate = new Date(lmp.value);
		  	//this fixes the weird thing where JS thinks 2014-07-01 is Jul 30 2014 at 20:00
		  	//and also doesn't break the other formats (because we only care about yr/mon/day)
		    lmpDate.setHours(lmpDate.getHours() + 4);
		    var eddDate = new Date(lmpDate.getTime() + (280 * 1000 * 60 * 60 * 24));
		    //catch if eddDate is not a number, so don't show the calculation
		    if (!isNaN(eddDate.getTime())) {
		    	edd.value = (eddDate.getMonth() + 1) + "/" + eddDate.getDate() + "/" + eddDate.getFullYear();
		    	document.getElementById("lmp-invalid").innerHTML = "";
		    }
		    else {
		    	//edd.value = "";
		    	document.getElementById("lmp-invalid").innerHTML = "<span class=\"iTrustError\">Invalid LMP</span>";
		    }
		}
	}
	
	function calculateWeeksPregnant() {
		var lmp = document.getElementById("lmp");
		var date = document.getElementById("date");
		if (lmp.value != "" && date.value != "") {
			var weeksPregnant = document.getElementById("weeksPregnant");
			var dateDate = new Date(date.value);
				dateDate.setHours(dateDate.getHours() + 4); //see above method for explanation
			var lmpDate = new Date(lmp.value);
				lmpDate.setHours(lmpDate.getHours() + 4); //see above method for explanation
			
			if (!isNaN(lmpDate.getTime()) && !isNaN(dateDate.getTime())) {
				var daysPreg = (dateDate.getTime() - lmpDate.getTime()) / 1000 / 60 / 60 / 24;
				var weeksPreg = ~~(daysPreg / 7);
				var daysPreg = ~~(daysPreg % 7);
		    	//and then put the weeksPregnany values
				weeksPregnant.value = weeksPreg + "-" + daysPreg;
		    	document.getElementById("lmp-invalid").innerHTML = "";
		    	document.getElementById("date-invalid").innerHTML = "";
		    }
		    else {
		    	weeksPregnant.value = "";
		    	if (isNaN(lmpDate.getTime())) {
		    		document.getElementById("lmp-invalid").innerHTML = "<span class=\"iTrustError\">Invalid LMP</span>";
		    	}
		    	if (isNaN(dateDate.getTime())) {
		    		document.getElementById("date-invalid").innerHTML = "<span class=\"iTrustError\">Invalid Date</span>";
		    	}
		    }
		}
	}
	
	function showNewPriorPregnancyForm() {
		//this is how you do a static field in JavaScript
		if (typeof showNewPriorPregnancyForm.formNum == 'undefined') {
	        //If it doesn't exist yet, then make it equal 0
	        showNewPriorPregnancyForm.formNum = 0;
	    }
		var formNum = showNewPriorPregnancyForm.formNum; //so I don't have to type this nonsense every time
		var form = "<table class=\"fTable\" id=\"priorPregnancyTable" + formNum + "\">\n";
			form+= "<tr><th colspan=\"3\">Prior Pregnancy " + (formNum + 1) + "</th></tr>\n";
			//Year Conception
			form+= "<tr>\n";
			form+= "<td width=\"200px\"><label for=\"yearConception" + formNum + "\">Year of conception: </label></td>\n";
			form+= "<td width=\"200px\"><input type=\"text\" name=\"yearConception" + formNum + "\" id=\"yearConception" + formNum + "\" maxlength=\"4\" size=\"4\" /></td>\n";
			form+= "<td width=\"200px\"></td>\n";
			form+= "</tr>\n";
			//Weeks Pregnant
			form+= "<tr>\n";
			form+= "<td width=\"200px\"><label for=\"weeksPregnant" + formNum + "\">Weeks-Days pregnant: </label></td>\n";
			form+= "<td width=\"200px\">";
			form+= "<input type=\"text\" name=\"weeksPregnant" + formNum + "\" id=\"weeksPregnant" + formNum + "\" maxlength=\"2\" size=\"1\"/> - ";
			form+= "<input type=\"text\" name=\"daysPregnant" + formNum + "\" id=\"daysPregnant" + formNum + "\" maxlength=\"1\" size=\"1\"/>";
			form+= "</td>\n";
			form+= "<td width=\"200px\"></td>\n";
			form+= "</tr>\n";
			//Hours in Labor
			form+= "<tr>\n";
			form+= "<td width=\"200px\"><label for=\"hoursInLabor" + formNum + "\">Hours in labor: </label></td>\n";
			form+= "<td width=\"200px\"><input type=\"text\" name=\"hoursInLabor" + formNum + "\" id=\"hoursInLabor" + formNum + "\" /></td>\n";
			form+= "<td width=\"200px\"></td>\n";
			form+= "</tr>\n";
			//Delivery Type (TODO: update to use the enumerator if possible instead of hard-coding)
			form+= "<tr>\n";
			form+= "<td width=\"200px\"><label for=\"deliveryType" + formNum + "\">Delivery type: </label></td>\n";
			form+= "<td width=\"200px\">";
			form+= "<select name=\"deliveryType" + formNum + "\" id=\"deliveryType" + formNum + "\" />";
			form+= "<option name=\"vaginalDelivery\">Vaginal Delivery</option>";
			form+= "<option name=\"cSection\">Caesarean Section</option>";
			form+= "<option name=\"miscarriage\">Miscarriage</option>";
			form+= "</select></td>\n";
			form+= "<td width=\"200px\"></td>\n";
			form+= "</tr>\n";
			//Twins flag checkbox
			form+= "<tr>\n";
			form+= "<td width=\"200px\"><label for=\"twins" + formNum + "\">Twins: </label></td>\n";
			form+= "<td width=\"200px\"><input type=\"checkbox\" name=\"twins" + formNum + "\" id=\"twins" + formNum + "\" /></td>\n";
			form+= "<td width=\"200px\"></td>\n";
			form+= "</tr>\n";
			//end table
			form+= "</table>\n";
			form+= "<br />\n"
		var newTable = document.createElement("div");
		newTable.innerHTML = form;
		var priorDiv = document.getElementById("priorPregnancies");
		var parent = priorDiv.parentNode;
		parent.insertBefore(newTable, priorDiv);
		
		++showNewPriorPregnancyForm.formNum;
	}
	
	function insertAfter(newNode, referenceNode) {
	    referenceNode.parentNode.insertBefore(newNode, referenceNode.nextSibling);
	}
</script>
<%@include file="/footer.jsp" %>