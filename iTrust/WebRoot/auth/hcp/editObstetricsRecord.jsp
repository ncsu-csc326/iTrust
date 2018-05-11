<%@taglib uri="/WEB-INF/tags.tld" prefix="itrust"%>
<%@page errorPage="/auth/exceptionHandler.jsp"%>

<%@page import="java.util.List"%>
<%@page import="java.util.ArrayList"%>
<%@page import="edu.ncsu.csc.itrust.action.ViewPersonnelAction"%> <!-- Used for specialty-checking -->
<%@page import="edu.ncsu.csc.itrust.beans.PersonnelBean"%> <!-- Used for specialty-checking -->
<%@page import="edu.ncsu.csc.itrust.action.EditObstetricsAction"%>
<%@page import="edu.ncsu.csc.itrust.action.ViewObstetricsAction"%>
<%@page import="edu.ncsu.csc.itrust.beans.ObstetricsRecordBean"%>
<%@page import="edu.ncsu.csc.itrust.beans.FlagsBean"%>
<%@page import="edu.ncsu.csc.itrust.enums.PregnancyStatus"%>
<%@page import="edu.ncsu.csc.itrust.enums.FlagValue"%>
<%@page import="edu.ncsu.csc.itrust.action.ViewPatientAction"%> 
<%@page import="edu.ncsu.csc.itrust.beans.PatientBean"%>
<%@page import="edu.ncsu.csc.itrust.exception.ITrustException"%>
<%@page import="edu.ncsu.csc.itrust.exception.FormValidationException" %>

<%@include file="/global.jsp"%>

<%pageTitle = "iTrust - Edit Obstetrics Record";%>

<%@include file="/header.jsp"%>
<%
	/* Require a Patient ID first */
	String pidString = (String) session.getAttribute("pid");
	if (pidString == null || 1 > pidString.length()) {
		response.sendRedirect("/iTrust/auth/getPatientID.jsp?forward=/iTrust/auth/hcp/viewObstetricsRecord.jsp");
		return;
	}
	
	ObstetricsRecordBean bean = null;
	PatientBean patient = null;
	ViewObstetricsAction viewObstetrics = new ViewObstetricsAction(prodDAO, loggedInMID);
	
	//get the ObstetricsRecordBean given by the URL param
	String oidString = request.getParameter("oid");
	if (oidString != null && !oidString.equals("")) {
		long oid = Long.parseLong(request.getParameter("oid"));
		bean = viewObstetrics.getViewableObstetricsRecords(oid);
		
		//then grab the associated PatientBean
		ViewPatientAction viewPatient = new ViewPatientAction(prodDAO, loggedInMID, pidString);
		patient = viewPatient.getPatient(pidString);
	}
	else {
		throw new ITrustException("Invalid Obstetrics ID passed to the Edit page: " + oidString);
	}
	
	//now double-check this bean's status AND the HCP's specialty to see if should redirect back to view
	ViewPersonnelAction personnelAction = new ViewPersonnelAction(prodDAO, loggedInMID);
	PersonnelBean currentPersonnel = personnelAction.getPersonnel("" + loggedInMID);
	if (!bean.getPregnancyStatus().equals(PregnancyStatus.Office)
			|| !currentPersonnel.getSpecialty().equalsIgnoreCase("ob/gyn")) {
		response.sendRedirect("/iTrust/auth/hcp/viewObstetricsRecord.jsp?oid=" + oidString);
	}
	
	//finally, let's grab the manual flags to check checkboxes as necessary (twins and low-lying placenta)
	boolean twinsChecked = viewObstetrics.getFlagForRecord(bean, FlagValue.Twins).isFlagged();
	boolean placentaChecked = viewObstetrics.getFlagForRecord(bean, FlagValue.LowLyingPlacenta).isFlagged();
	
	//don't run unless the form was actually submitted
	if ("true".equals(request.getParameter("formIsFilled"))) {
		//prepare to add beans!
		EditObstetricsAction editAction = new EditObstetricsAction(prodDAO, loggedInMID);
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
				ovBean.setOid(Long.parseLong(oidString));
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
					clientSideErrors += "Fundal height of the uterus must be an double";
					hasCSErrors = true;
				}
				ovBean.setPregnancyStatus(PregnancyStatus.Office);
				ovBean.setDeliveryType(bean.getDeliveryType());
			
			//now do the flags
			ArrayList<FlagsBean> flags = new ArrayList<FlagsBean>();
			FlagsBean twins = new FlagsBean();
				twins.setMid(ovBean.getMid());
				twins.setPregId(ovBean.getPregId());
				twins.setFlagValue(FlagValue.Twins);
				twins.setFlagged(request.getParameter("twins") != null); //is null if unchecked
				flags.add(twins);
			FlagsBean placenta = new FlagsBean();
				placenta.setMid(ovBean.getMid());
				placenta.setPregId(ovBean.getPregId());
				placenta.setFlagValue(FlagValue.LowLyingPlacenta);
				placenta.setFlagged(request.getParameter("placenta") != null); //is null if unchecked
				flags.add(placenta);
				
			editAction.editObstetricsRecord(bean.getOid(), ovBean, flags);
			addedSomething = true;
			
			if (hasCSErrors) {
				out.write(clientSideErrors + "</p>");
			}
			else if (addedSomething) {
				response.sendRedirect("/iTrust/auth/hcp/obstetricsHome.jsp?editOV");
			}
		}
		catch(FormValidationException e) {
			out.write("<p class=\"iTrustError\">" + e.getMessage() + "</p>");
		}
	}
%>
<div align=center>
	<form <% out.print("action=\"/iTrust/auth/hcp/editObstetricsRecord.jsp?oid=" + oidString + "\""); %> method="post" id="officeVisit">
	<table class="fTable" align="center">
		<input type="hidden" name="formIsFilled" value="true" />
		<tr><th colspan="3">View Obstetrics Record</th></tr>
		<!-- These fields are universal to all pregnancy statuses -->
		<tr>
			<td width="200px"><label>Patient Name:</label></td>
			<td width="200px"><% out.write(patient.getFullName()); %></td>
			<td width="200px"></td>
		</tr>
		<tr>
			<td><label>Visit type:</label></td>
			<td><% out.write(bean.getPregnancyStatus().toString()); %></td>
			<td></td>
		</tr>
		<tr>
			<td><label for="date">Date of visit: </label></td>
			<td>
				<input type="text" name="date" id="date" <% out.write("value=\"" + bean.getDateVisitString() + "\""); %> onchange="calculateWeeksPregnant()" size="7" />
				<input type="button" value="Select Date" onclick="displayDatePicker('date');" />
			</td>
			<td id="date-invalid"></td>
		</tr>
		<tr>
			<td><label for="lmp">Last menstrual period: </label></td>
			<td>
				<input readonly type="text" name="lmp" id="lmp" size="7" <% out.write("value=\"" + bean.getLmpString() + "\""); %> />
			</td>
			<td></td>
		</tr>
		<tr>
			<td><label for="edd">Estimated delivery date: </label></td>
			<td><input readonly type="text" name="edd" id="edd" size="7" <% out.write("value=\"" + bean.getEddString() + "\""); %> /></td>
			<td></td>
		</tr>
		<tr>
			<td><label for="weeksPregnant">Weeks-Days pregnant: </label></td>
			<td><input readonly type="text" name="weeksPregnant" id="weeksPregnant" size="3" <% out.write("value=\"" + bean.getWeeksPregnant() + "\""); %> /></td>
			<td></td>
		</tr>
		<tr>
			<td><label for="weight">Weight: </label></td>
			<td><input type="text" name="weight" id="weight" size="3" <% out.write("value=\"" + bean.getWeight() + "\""); %> /> lbs</td>
			<td></td>
		</tr>
		<tr>
			<td><label for="weight">Blood Pressure: </label></td>
			<td>
				<input type="text" name="bloodPressureS" id="bloodPressureS" size="2" <% out.write("value=\"" + bean.getBloodPressureS() + "\""); %> /> / 
				<input type="text" name="bloodPressureD" id="bloodPressureD" size="2" <% out.write("value=\"" + bean.getBloodPressureD() + "\""); %> /> mm Hg
			</td>
			<td></td>
		</tr>
		<tr>
			<td><label for="fhr">Fetal Heart Rate: </label></td>
			<td><input type="text" name="fhr" id="fhr" size="3" <% out.write("value=\"" + bean.getFhr() + "\""); %> /> bpm</td>
			<td></td>
		</tr>
		<tr>
			<td><label for="fhu">Fundal Height of the Uterus: </label></td>
			<td><input type="text" name="fhu" id="fhu" size="3" <% out.write("value=\"" + bean.getFhu() + "\""); %> /> cm</td>
			<td></td>
		</tr>
		<tr>
			<td><label for="twins">Twins: </label></td>
			<td><input type="checkbox" name="twins" id="twins" <% if (twinsChecked) { out.write("checked=\"checked\" value=\"on\""); }%> /></td>
			<td></td>
		</tr>
		<tr>
			<td><label for="placenta">Low-lying Placenta: </label></td>
			<td><input type="checkbox" name="placenta" id="placenta" <% if (placentaChecked) { out.write("checked=\"checked\" value=\"on\""); }%> /></td>
			<td></td>
		</tr>
	</table>
	<br />
	
	<input type="submit" id="submit" value="Submit" />
	</form>
</div>
<p><br/><a href="/iTrust/auth/hcp/obstetricsHome.jsp">Back to Home</a></p>

<script type="text/javascript">
	//called when (you guessed it) the Date Picker is closed.
	function datePickerClosed() {
		calculateWeeksPregnant();
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
</script>

<%@include file="/footer.jsp" %>