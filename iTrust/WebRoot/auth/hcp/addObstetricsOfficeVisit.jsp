<%@page import="edu.ncsu.csc.itrust.dao.mysql.ObstetricsRecordDAO"%>
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
<%@page import="edu.ncsu.csc.itrust.action.EditPHRAction"%>
<%@page import="edu.ncsu.csc.itrust.beans.ObstetricsRecordBean"%>
<%@page import="edu.ncsu.csc.itrust.beans.FlagsBean"%>
<%@page import="edu.ncsu.csc.itrust.enums.DeliveryType"%>
<%@page import="edu.ncsu.csc.itrust.enums.PregnancyStatus"%>
<%@page import="edu.ncsu.csc.itrust.enums.FlagValue"%>
<%@page import="edu.ncsu.csc.itrust.exception.FormValidationException" %>

<%@include file="/global.jsp"%>

<%pageTitle = "iTrust - Add Obstetrics Office Visit";%>

<%@include file="/header.jsp"%>
<%
	/* Require a Patient ID first */
	String pidString = (String) session.getAttribute("pid");
	if (pidString == null || 1 > pidString.length()) {
		response.sendRedirect("/iTrust/auth/getPatientID.jsp?forward=/iTrust/auth/hcp/addObstetricsOfficeVisit.jsp");
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
	
	//if specialty is not ob/gyn, simply redirect them to the regular edit office visit page
	ViewPersonnelAction personnelAction = new ViewPersonnelAction(prodDAO, loggedInMID);
	PersonnelBean currentPersonnel = personnelAction.getPersonnel("" + loggedInMID);
	if (!currentPersonnel.getSpecialty().equalsIgnoreCase("ob/gyn")) {
		response.sendRedirect("/iTrust/auth/hcp-uap/editOfficeVisit.jsp");
	}
	
	//get the most recent Initial record for this person, or redirect if they don't have one
	ViewObstetricsAction obstetricsAction = new ViewObstetricsAction(prodDAO, loggedInMID);
	List<ObstetricsRecordBean> initialBeans = obstetricsAction.getViewableObstetricsRecordsByMIDType(
			Long.parseLong(pidString), PregnancyStatus.Initial);
	ObstetricsRecordBean initial = null;
	if (initialBeans == null || initialBeans.isEmpty()) {
		response.sendRedirect("/iTrust/auth/hcp/obstetricsHome.jsp");
		throw new ITrustException("The patient chosen is not a current obstetrics patient");
	}
	else {
		initial = initialBeans.get(0);
	}
	
	//get the twins and LLP flags from the DB and set as appropriate
	boolean twinsChecked = obstetricsAction.getFlagForRecord(initial, FlagValue.Twins).isFlagged();
	boolean placentaChecked = obstetricsAction.getFlagForRecord(initial, FlagValue.LowLyingPlacenta).isFlagged();
	
	//don't run unless the form was actually submitted
	if ("true".equals(request.getParameter("formIsFilled"))) {
		//prepare to add beans!
		AddObstetricsAction addAction = new AddObstetricsAction(prodDAO, loggedInMID);
		boolean addedSomething = false;
		
		//add the initial record
		String lmp = request.getParameter("lmp");
		String edd = request.getParameter("edd");
		String date = request.getParameter("date");
		String weeksPregnant = request.getParameter("weeksPregnant");
		String weightString = request.getParameter("weight");
		String bpsString = request.getParameter("bloodPressureS");
		String bpdString = request.getParameter("bloodPressureD");
		String fhrString = request.getParameter("fhr");
		String fhuString = request.getParameter("fhu");
		try {
			String clientSideErrors = "<p class=\"iTrustError\">This form has not been validated correctly." +
			"The following field are not properly filled in: [";
			boolean hasCSErrors = false;
			ObstetricsRecordBean ovBean = new ObstetricsRecordBean();
				ovBean.setMid(Long.parseLong(pidString));
				ovBean.setLmp(lmp);
				ovBean.setDateVisit(date);
				ovBean.setEdd(edd);
				ovBean.setWeeksPregnant(weeksPregnant);
				//parse weight
				try {
					double weight = Double.parseDouble(weightString);
					ovBean.setWeight(weight);
				}
				catch (NumberFormatException e) {
					clientSideErrors += "Weight must have a positive numeric value";
					hasCSErrors = true;
				}
				//parse BPD
				try {
					int bpd = Integer.parseInt(bpdString);
					ovBean.setBloodPressureD(bpd);
				}
				catch (NumberFormatException e) {
					if (hasCSErrors)
						clientSideErrors += ", ";
					clientSideErrors += "Blood pressure diastolic must be an integer";
					hasCSErrors = true;
				}
				//parse BPS
				try {
					int bps = Integer.parseInt(bpsString);
					ovBean.setBloodPressureS(bps);
				}
				catch (NumberFormatException e) {
					if (hasCSErrors)
						clientSideErrors += ", ";
					clientSideErrors += "Blood pressure systolic must be an integer";
					hasCSErrors = true;
				}
				//parse fhr
				try {
					int fhr = Integer.parseInt(fhrString);
					ovBean.setFhr(fhr);
				}
				catch (NumberFormatException e) {
					if (hasCSErrors)
						clientSideErrors += ", ";
					clientSideErrors += "Fetal heart rate must be an integer";
					hasCSErrors = true;
				}
				//parse fhu
				try {
					double fhu = Double.parseDouble(fhuString);
					ovBean.setFhu(fhu);
				}
				catch (NumberFormatException e) {
					if (hasCSErrors)
						clientSideErrors += ", ";
					clientSideErrors += "Fundal height of the uterus must be a double";
					hasCSErrors = true;
				}
				ovBean.setPregnancyStatus(PregnancyStatus.Office);
				ovBean.setDeliveryType(DeliveryType.Vaginal);
				
			//Calculate all the flags here
			
			//Twins flag
			ArrayList<FlagsBean> flags = new ArrayList<FlagsBean>();
			if (request.getParameter("twins") != null) {
				FlagsBean twins = new FlagsBean();
				twins.setMid(ovBean.getMid());
				twins.setPregId(ovBean.getPregId());
				twins.setFlagValue(FlagValue.Twins);
				twins.setFlagged(request.getParameter("twins").equals("on")); //true if on, false if off
				flags.add(twins);
			}
			//Low-lying placenta flag
			if (request.getParameter("placenta") != null) {
				FlagsBean placenta = new FlagsBean();
				placenta.setMid(ovBean.getMid());
				placenta.setPregId(ovBean.getPregId());
				placenta.setFlagValue(FlagValue.LowLyingPlacenta);
				placenta.setFlagged(request.getParameter("placenta").equals("on")); //true if on, false if off
				flags.add(placenta);
			}
			
			addAction.addObstetricsRecord(ovBean, flags);
			addedSomething = true;
			
			if (hasCSErrors) {
				out.write(clientSideErrors + "</p>");
			}
			else if (addedSomething) {
				response.sendRedirect("/iTrust/auth/hcp/obstetricsHome.jsp?addOV");
			}
		}
		catch(FormValidationException e) {
			if (e.getMessage().contains("The patient chosen is not a current obstetrics patient")) {
				response.sendRedirect("/iTrust/auth/hcp/obstetricsHome.jsp");
				throw new ITrustException("The patient chosen is not a current obstetrics patient");
			}
			out.write("<p class=\"iTrustError\">" + e.getMessage() + "</p>");
		}
	}
	
	//Do get previous visit 
	//
	ObstetricsRecordDAO obsDao = new ObstetricsRecordDAO(prodDAO);
	List<ObstetricsRecordBean> pastVisits = obsDao.getObstetricsRecordsByMIDPregStatus(chosenPatient.getMID(), PregnancyStatus.Office.toString());
	if (pastVisits != null && pastVisits.size() > 0) {
		%>
		<input type="hidden" value="<%=pastVisits.get(0).getDateVisit().toString()%>" id="prevDate" />
		<%
	}
%>

<div id="mainpage" align="center">

<div id="rhNegAlert" align="center" padding="1em"></div>
	<form action="/iTrust/auth/hcp/addObstetricsOfficeVisit.jsp" method="post" id="officeVisit" >
		<input type="hidden" name="formIsFilled" value="true" />
		<input type="hidden" name="rhNeg" id="rhNeg" value="<% out.write(chosenPatient.getBloodType().toString());%>"/>
		<table class="fTable" align="center">
			<tr><th colspan="3">Add Obstetrics Office Visit</th></tr>
			<tr>
				<td width="200px"><label for="date">Date of visit: </label></td>
				<td width="200px">
					<input type="text" name="date" id="date" onchange="doVisitDateOnChange();" size="7" />
					<input type="button" value="Select Date" onclick="displayDatePicker('date');" />
				</td>
				<td width="200px" id="date-invalid"></td>
			</tr>
			<tr>
				<td><label for="lmp">Last menstrual period: </label></td>
				<td>
					<input readonly type="text" name="lmp" id="lmp" size="7" value="<% out.write(initial.getLmpString()); %>"/>
				</td>
				<td></td>
			</tr>
			<tr>
				<td><label for="edd">Estimated delivery date: </label></td>
				<td><input readonly type="text" name="edd" id="edd" size="7" value="<% out.write(initial.getEddString()); %>"/></td>
				<td></td>
			</tr>
			<tr>
				<td><label for="weeksPregnant">Weeks pregnant: </label></td>
				<td><input readonly type="text" name="weeksPregnant" id="weeksPregnant" size="3" /></td>
				<td></td>
			</tr>
			<tr>
				<td><label for="weight">Weight: </label></td>
				<td><input type="text" name="weight" id="weight" size="3" /> lbs</td>
				<td></td>
			</tr>
			<tr>
				<td><label for="weight">Blood Pressure: </label></td>
				<td>
					<input type="text" name="bloodPressureS" id="bloodPressureS" size="2" /> / 
					<input type="text" name="bloodPressureD" id="bloodPressureD" size="2" /> mm Hg
				</td>
				<td></td>
			</tr>
			<tr>
				<td><label for="fhr">Fetal Heart Rate: </label></td>
				<td><input type="text" name="fhr" id="fhr" size="3" /> bpm</td>
				<td></td>
			</tr>
			<tr>
				<td><label for="fhu">Fundal Height of the Uterus: </label></td>
				<td><input type="text" name="fhu" id="fhu" size="3" /> cm</td>
				<td></td>
			</tr>
			<tr>
				<td><label for="twins">Twins: </label></td>
				<td><input type="checkbox" name="twins" id="twins"  <% if (twinsChecked) { out.write("checked=\"checked\" value=\"on\""); }%> /></td>
				<td></td>
			</tr>
			<tr>
				<td><label for="placenta">Low-lying Placenta: </label></td>
				<td><input type="checkbox" name="placenta" id="placenta"  <% if (placentaChecked) { out.write("checked=\"checked\" value=\"on\""); }%> /></td>
				<td></td>
			</tr>
		</table>
		
		<br />
		<input type="submit" id="submit" value="Submit" /><!-- onclick="checkRHNeg();" -->
	</form>
</div>
<p><br/><a href="/iTrust/auth/hcp/obstetricsHome.jsp">Back to Home</a></p>

<script type="text/javascript">
	//called when (you guessed it) the Date Picker is closed.
	function datePickerClosed() {
		doVisitDateOnChange();
	}
	
	function doVisitDateOnChange() {
		calculateWeeksPregnant();
		checkRHNeg();
		return true;
	}

	function calculateWeeksPregnant() {
		var lmp = document.getElementById("lmp");
		var date = document.getElementById("date");
		if (lmp.value != "" && date.value != "") {
			var weeksPregnant = document.getElementById("weeksPregnant");
			var dateDate = new Date(date.value);
				dateDate.setHours(dateDate.getHours() + 4); //+4 is for JS bug with some dates being 10:00 the night before
			var lmpDate = new Date(lmp.value);
				lmpDate.setHours(lmpDate.getHours() + 4); //see above for explanation
			
			if (!isNaN(lmpDate.getTime()) && !isNaN(dateDate.getTime())) {
				var daysPreg = (dateDate.getTime() - lmpDate.getTime()) / 1000 / 60 / 60 / 24;
				var weeksPreg = ~~(daysPreg / 7);
				var daysPreg = ~~(daysPreg % 7);
		    	//and then put the weeksPregnancy values
				weeksPregnant.value = weeksPreg + "-" + daysPreg;
		    	document.getElementById("date-invalid").innerHTML = "";
		    }
		    else {
		    	weeksPregnant.value = "";
		    	if (isNaN(dateDate.getTime())) {
		    		document.getElementById("date-invalid").innerHTML = "<span class=\"iTrustError\">Invalid Date</span>";
		    	}
		    }
		}
	}
	
	function checkRHNeg() {
		var lmp = document.getElementById("lmp");
		var date = document.getElementById("date");
		var prevDate = document.getElementById("prevDate");
		var weeksPregnant = document.getElementById("weeksPregnant");
		var dateDate = new Date(date.value);
			dateDate.setHours(dateDate.getHours() + 4); //+4 is for JS bug with some dates being 10:00 the night before
		var lmpDate = new Date(lmp.value);
			lmpDate.setHours(lmpDate.getHours() + 4); //see above for explanation
		var daysPreg = (dateDate.getTime() - lmpDate.getTime()) / 1000 / 60 / 60 / 24;
		var weeksPreg = ~~(daysPreg / 7);
		var bloodType = document.getElementById("rhNeg").value;
		//console.log(bloodType);
		if (bloodType.indexOf("-") != -1 && weeksPreg >= 28) {
			var show = false;
			if (prevDate == null) {
				show = true;
			} else {
				weeksPregnant = document.getElementById("weeksPregnant");
				dateDate = new Date(prevDate.value);
				dateDate.setHours(dateDate.getHours() + 4); //+4 is for JS bug with some dates being 10:00 the night before
				lmpDate = new Date(lmp.value);
				lmpDate.setHours(lmpDate.getHours() + 4); //see above for explanation
				daysPreg = (dateDate.getTime() - lmpDate.getTime()) / 1000 / 60 / 60 / 24;
				weeksPreg = ~~(daysPreg / 7);
				if (weeksPreg >= 28) {
					show = false;
				} else {
					show = true;
				}
			}
			if (show) {
				document.getElementById("rhNegAlert").innerHTML = '<b style="color:red;">Mother is RH- and past 28 weeks.<br />Please administer a RH immunoglobulin shot.</b><br />';
			}
		} else {
			document.getElementById("rhNegAlert").innerHTML = '';
		}
		return true;
	}
	
</script>
<%@include file="/footer.jsp" %>