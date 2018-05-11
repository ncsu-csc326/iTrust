<%@taglib uri="/WEB-INF/tags.tld" prefix="itrust"%>
<%@page errorPage="/auth/exceptionHandler.jsp"%>

<%@page import="java.util.List"%>
<%@page import="java.util.ArrayList"%>
<%@page import="edu.ncsu.csc.itrust.action.ViewPersonnelAction"%> <!-- Used for specialty-checking -->
<%@page import="edu.ncsu.csc.itrust.beans.PersonnelBean"%> <!-- Used for specialty-checking -->
<%@page import="edu.ncsu.csc.itrust.action.EditOphthalmologySurgeryAction"%>
<%@page import="edu.ncsu.csc.itrust.action.ViewOphthalmologySurgeryAction"%>
<%@page import="edu.ncsu.csc.itrust.beans.OphthalmologySurgeryRecordBean"%>
<%@page import="edu.ncsu.csc.itrust.action.ViewPatientAction"%> 
<%@page import="edu.ncsu.csc.itrust.beans.PatientBean"%>
<%@page import="edu.ncsu.csc.itrust.exception.ITrustException"%>
<%@page import="edu.ncsu.csc.itrust.exception.FormValidationException" %>
<%@page import="edu.ncsu.csc.itrust.action.EditOPDiagnosesAction"%> 
<%@page import="edu.ncsu.csc.itrust.beans.OphthalmologyDiagnosisBean"%>
<%@page import="edu.ncsu.csc.itrust.validate.OphthalmologyDiagnosisBeanValidator"%>
<%@page import="edu.ncsu.csc.itrust.BeanBuilder"%>

<%@include file="/global.jsp"%>

<%pageTitle = "iTrust - Edit Surgical Ophththalmology Record";%>

<%@include file="/header.jsp"%>
<%
	/* Require a Patient ID first */
	String clientSideErrors ="";
	String pidString = (String) session.getAttribute("pid");
	if (pidString == null || 1 > pidString.length()) {
		response.sendRedirect("/iTrust/auth/getPatientID.jsp?forward=/iTrust/auth/hcp/viewOphthalmologySurgery.jsp");
		return;
	}
	
	OphthalmologySurgeryRecordBean bean = null;
	PatientBean patient = null;
	ViewOphthalmologySurgeryAction viewAction = new ViewOphthalmologySurgeryAction(prodDAO, loggedInMID);
	
	//get the OphthalmologyRecordBean given by the URL param
	String oidString = request.getParameter("oid");
	if (oidString != null && !oidString.equals("")) {
		long oid = Long.parseLong(request.getParameter("oid"));
		bean = viewAction.getOphthalmologySurgeryForHCP(oid);
		
		//then grab the associated PatientBean
		ViewPatientAction viewPatient = new ViewPatientAction(prodDAO, loggedInMID, pidString);
		patient = viewPatient.getPatient(pidString);
	}
	else {
		throw new ITrustException("Invalid Ophthalmology ID passed to the Edit page: " + oidString);
	}
	
	//now double-check this bean's status AND the HCP's specialty to see if should redirect back to view
	ViewPersonnelAction personnelAction = new ViewPersonnelAction(prodDAO, loggedInMID);
	PersonnelBean currentPersonnel = personnelAction.getPersonnel("" + loggedInMID);
	if (!currentPersonnel.getSpecialty().equalsIgnoreCase("ophthalmologist")) {
		response.sendRedirect("/iTrust/auth/hcp/viewOphthalmologySurgery.jsp?oid=" + oidString);
	}
	
	if ("true".equals(request.getParameter("formIsFilled")) && "removediagnosesForm".equals(request.getParameter("submittedFormName"))){
	    EditOPDiagnosesAction diagnoses = new EditOPDiagnosesAction(prodDAO,oidString);
	    
	    String remID = request.getParameter("removeDiagID");
	    OphthalmologyDiagnosisBean beanSub = new OphthalmologyDiagnosisBean();
	    beanSub.setOpDiagnosisID(Long.parseLong(remID));
	    diagnoses.deleteDiagnosis(beanSub);
	}
	
	//check if a new diagnoses was added.
	else if ("true".equals(request.getParameter("formIsFilled")) && "diagnosesForm".equals(request.getParameter("submittedFormName"))){
		EditOPDiagnosesAction diagnoses =  new EditOPDiagnosesAction(prodDAO,oidString); 
		OphthalmologyDiagnosisBean beanSub = new BeanBuilder<OphthalmologyDiagnosisBean>().build(request.getParameterMap(), new OphthalmologyDiagnosisBean());
		//validator requires description but DiagnosesDAO does not. Set here to pass validation.
		beanSub.setDescription("no description");
    	try {
    		OphthalmologyDiagnosisBeanValidator validator = new OphthalmologyDiagnosisBeanValidator();
    		validator.validate(beanSub);
    		beanSub.setVisitID(Integer.parseInt(oidString));
        	diagnoses.addDiagnosis(beanSub);
       		clientSideErrors = "Diagnosis information successfully updated.";
   	 	} catch (FormValidationException e) {
 			clientSideErrors = e.printHTMLasString();
    	}
	}
	//don't run unless the form was actually submitted
	else if ("true".equals(request.getParameter("formIsFilled"))) {
		//prepare to add beans!
				EditOphthalmologySurgeryAction editAction = new EditOphthalmologySurgeryAction(prodDAO, loggedInMID);
				boolean addedSomething = false;
				
				//add the initial record
				String date = request.getParameter("date");
				String visAcNumOD = request.getParameter("vaNumOD");
				String visAcDenumOD = request.getParameter("vaDenOD");
				String visAcNumOS = request.getParameter("vaNumOS");
				String visAcDenumOS = request.getParameter("vaDenOS");
				String sphereOD = request.getParameter("sphereOD");
				String sphereOS = request.getParameter("sphereOS");
				String cylOD = request.getParameter("cylinderOD");
				String cylOS = request.getParameter("cylinderOS");
				String axisOD = request.getParameter("axisOD");
				String axisOS = request.getParameter("axisOS");
				String addOD = request.getParameter("addOD");
				String addOS = request.getParameter("addOS");
				String surgery = request.getParameter("surgery");
				String surgeryNotes = request.getParameter("surgeryNotes");
				
				try {
					clientSideErrors = "<p class=\"iTrustError\">This form has not been validated correctly. " +
					"The following field are not properly filled in: [";
					boolean hasCSErrors = false;
					OphthalmologySurgeryRecordBean ophBean = new OphthalmologySurgeryRecordBean();
						ophBean.setMid(Long.parseLong(pidString));
						ophBean.setVisitDate(date);
						//We reuse the first and last name that were already present
						//as an edit shouldn't change who created the surigcal office visit.
						ophBean.setLastName(bean.getLastName());
						ophBean.setFirstName(bean.getFirstName());
						//parse acuity numer OD
						try {
							int vaNum = Integer.parseInt(visAcNumOD);
							if(vaNum <= 0){
								throw new NumberFormatException();
							}
							ophBean.setVaNumOD(vaNum);
						}
						catch (NumberFormatException e) {
							clientSideErrors += "Visual Acuity Numerator OD is required, and must be a positive integer.";
							hasCSErrors = true;
						}
						//parse acuity numer OS
						try {
							int vaNum = Integer.parseInt(visAcNumOS);
							if(vaNum <= 0){
								throw new NumberFormatException();
							}
							ophBean.setVaNumOS(vaNum);
						}
						catch (NumberFormatException e) {
							clientSideErrors += "Visual Acuity Numerator OS is required, and must be a positive integer.";
							hasCSErrors = true;
						}
						//parse acuity denum OD
						try {
							int vaDen = Integer.parseInt(visAcDenumOD);
							if(vaDen <= 0){
								throw new NumberFormatException();
							}
							ophBean.setVaDenOD(vaDen);
						}
						catch (NumberFormatException e) {
							if (hasCSErrors)
								clientSideErrors += ", ";
							clientSideErrors += "Visual Acuity Denumerator OD is required, and must be a positive integer.";
							hasCSErrors = true;
						}
						//parse acuity denum OS
						try {
							int vaDen = Integer.parseInt(visAcDenumOS);
							if(vaDen <= 0){
								throw new NumberFormatException();
							}
							ophBean.setVaDenOS(vaDen);
						}
						catch (NumberFormatException e) {
							if (hasCSErrors)
								clientSideErrors += ", ";
							clientSideErrors += "Visual Acuity Denumerator OS is required, and must be a positive integer.";
							hasCSErrors = true;
						}
						//parse sphereOD
						try {
							double sphOD = Double.parseDouble(sphereOD);
							if(sphOD > 10.00 || sphOD < -10.00 || sphOD % 0.25 != 0){
								throw new NumberFormatException();
							}
							ophBean.setSphereOD(sphOD);
						}
						catch (NumberFormatException e) {
							if (hasCSErrors)
								clientSideErrors += ", ";
							clientSideErrors += "SphereOD is required, must be between -10.00 and 10.00 inclusive, and must be rounded to the nearest quarter diopter";
							hasCSErrors = true;
						}
						//parse sphereOS
						try {
							double sphOS = Double.parseDouble(sphereOS);
							if(sphOS > 10.00 || sphOS < -10.00 || sphOS % 0.25 != 0){
								throw new NumberFormatException();
							}
							ophBean.setSphereOS(sphOS);
						}
						catch (NumberFormatException e) {
							if (hasCSErrors)
								clientSideErrors += ", ";
							clientSideErrors += "SphereOS is required, must be between -10.00 and 10.00 inclusive, and must be rounded to the nearest quarter diopter";
							hasCSErrors = true;
						}
						//parse cylinderOD
						try {
							if(!cylOD.equals("")){
								double cylinOD = Double.parseDouble(cylOD);
								if(cylinOD > 10.00 || cylinOD < -10.00 || cylinOD % 0.25 != 0){
									throw new NumberFormatException();
								}
								ophBean.setCylinderOD(cylinOD);
							}
						}
						catch (NumberFormatException e) {
							if (hasCSErrors)
								clientSideErrors += ", ";
							clientSideErrors += "CylinderOD must be between -10.00 and 10.00 inclusive, and must be rounded to the nearest quarter diopter";
							hasCSErrors = true;
						}
						//parse cylinderOS
						try {
							if(!cylOS.equals("")){
								double cylinOS = Double.parseDouble(cylOS);
								if(cylinOS > 10.00 || cylinOS < -10.00 || cylinOS % 0.25 != 0){
									throw new NumberFormatException();
								}
								ophBean.setCylinderOS(cylinOS);
							}
						}
						catch (NumberFormatException e) {
							if (hasCSErrors)
								clientSideErrors += ", ";
							clientSideErrors += "CylinderOS must be between -10.00 and 10.00 inclusive, and must be rounded to the nearest quarter diopter";
							hasCSErrors = true;
						}
						//parse AxisOD
						try {
							if(!cylOD.equals("") && axisOD.equals("")){ //if we DO have a cylinder value, we need an axis, too.
								throw new NumberFormatException();
							}
							if(!axisOD.equals("")){ //we don't have to have one, so only try if we do.
								int axOD = Integer.parseInt(axisOD);
								if(axOD > 180 || axOD < 1){
									throw new NumberFormatException();
								}
									ophBean.setAxisOD(axOD);
							}
						}
						catch (NumberFormatException e) {
							if (hasCSErrors)
								clientSideErrors += ", ";
							clientSideErrors += "AxisOD is required if a CylinderOD is given, and must be between 1 and 180 inclusive";
							hasCSErrors = true;
						}
						//parse AxisOS
						try {
							if(!cylOS.equals("") && axisOS.equals("")){ //if we DO have a cylinder value, we need an axis, too.
								throw new NumberFormatException();
							}
							if(!axisOS.equals("")){ //we don't have to have one, so only try if we do.
								int axOS = Integer.parseInt(axisOD);
								if(axOS > 180 || axOS < 1){
									throw new NumberFormatException();
								}
									ophBean.setAxisOS(axOS);
							}
						}
						catch (NumberFormatException e) {
							if (hasCSErrors)
								clientSideErrors += ", ";
							clientSideErrors += "AxisOS is required if a CylinderOS is given, and must be between 1 and 180 inclusive";
							hasCSErrors = true;
						}
						//parse addOD
						try {
							double aOD = Double.parseDouble(addOD);
							if(aOD > 3.00 || aOD < 0.75 || aOD % 0.25 != 0){
								throw new NumberFormatException();
							}
							ophBean.setAddOD(aOD);
						}
						catch (NumberFormatException e) {
							if (hasCSErrors)
								clientSideErrors += ", ";
							clientSideErrors += "AddOD is required, must be between 0.75 and 3.00 inclusive, and must be rounded to the nearest quarter diopter";
							hasCSErrors = true;
						}
						//parse addOS
						try {
							double aOS = Double.parseDouble(addOS);
							if(aOS > 3.00 || aOS < 0.75 || aOS % 0.25 != 0){
								throw new NumberFormatException();
							}
							ophBean.setAddOS(aOS);
						}
						catch (NumberFormatException e) {
							if (hasCSErrors)
								clientSideErrors += ", ";
							clientSideErrors += "AddOS is required, must be between 0.75 and 3.00 inclusive, and must be rounded to the nearest quarter diopter";
							hasCSErrors = true;
						}
						ophBean.setSurgery(surgery);
						ophBean.setSurgeryNotes(surgeryNotes);
						
			if (hasCSErrors) {
				out.write(clientSideErrors + "]</p>");
			} else{	
			editAction.editOphthalmologySurgery(bean.getOid(), ophBean);
			addedSomething = true;
			}
			
			if (addedSomething) {
				response.sendRedirect("/iTrust/auth/hcp/ophthalmologySurgeryHome.jsp?editSurgery");
			}
		}
		catch(FormValidationException e) {
			out.write("<p class=\"iTrustError\">" + e.getMessage() + "</p>");
		}
	}
%>
<script type="text/javascript">
    function removeDiagID(value) {
        document.getElementById("removeDiagID").value = value;
        document.forms["removeDiagnosisForm"].submit();
    }
</script>
<div id="mainpage" align="center">

	<form <% out.print("action=\"/iTrust/auth/hcp/editOphthalmologySurgery.jsp?oid=" + oidString + "\""); %> method="post" id="officeVisit" >
		<input type="hidden" name="formIsFilled" value="true" />
		<table class="fTable" align="center">
			<tr><th colspan="3">Edit Surgical Ophthalmology Office Visit</th></tr>
			<tr>
				<td width="200px"><label for="date">Date of visit: </label></td>
				<td width="200px">
					<input type="text" name="date" id="date" <% out.write("value=\"" + bean.getVisitDateString() + "\""); %> size="7" /> -->
					<input type="button" value="Select Date" onclick="displayDatePicker('date');" />
				</td>
			</tr>
			<tr>
				<td><label for="visAcNumOD">Visual Acuity Numerator OD: </label></td>
				<td><input type="text" name="vaNumOD" id="vaNumOD" <% out.write("value=\"" + bean.getVaNumOD() + "\""); %>size="4" /> </td>
			</tr>
			<tr>
				<td><label for="visAcDenumOD">Visual Acuity Denumerator OD: </label></td>
				<td><input type="text" name="vaDenOD" id="vaDenOD" <% out.write("value=\"" + bean.getVaDenOD() + "\""); %> size="4" /> </td>
			</tr>
			<tr>
				<td><label for="visAcNumOS">Visual Acuity Numerator OS: </label></td>
				<td><input type="text" name="vaNumOS" id="vaNumOS" <% out.write("value=\"" + bean.getVaNumOS() + "\""); %> size="4" /> </td>
			</tr>
			<tr>
				<td><label for="visAcDenumOS">Visual Acuity Denumerator OS: </label></td>
				<td><input type="text" name="vaDenOS" id="vaDenOS" <% out.write("value=\"" + bean.getVaDenOS() + "\""); %> size="4" /> </td>
			</tr>
			<tr>
				<td><label for="sphereOD">Sphere(OD): </label></td>
				<td><input type="text" name="sphereOD" id="sphereOD" <% out.write("value=\"" + bean.getSphereOD() + "\""); %> size="3" /> diopters</td>
			</tr>
			<tr>
				<td><label for="sphereOS">Sphere(OS): </label></td>
				<td><input type="text" name="sphereOS" id="sphereOS" <% out.write("value=\"" + bean.getSphereOS() + "\""); %> size="3" /> diopters</td>
			</tr>
			<%
			String cylODOut = ""; 
			if(bean.getCylinderOD() != null){
				cylODOut = Double.toString(bean.getCylinderOD());
			}
			%>
			<tr>
				<td><label for="cylinderOD">Cylinder(OD): </label></td>
				<td><input type="text" name="cylinderOD" id="cylinderOD" <% out.write("value=\"" + cylODOut + "\""); %> size="3" /> diopters</td>
			</tr>
			<%
			String cylOSOut = "";
			if(bean.getCylinderOS() != null){ 
				cylOSOut = Double.toString(bean.getCylinderOS());
			}
			%>
			<tr>
				<td><label for="cylinderOS">Cylinder(OS): </label></td>
				<td><input type="text" name="cylinderOS" id="cylinderOS" <% out.write("value=\"" + cylOSOut + "\""); %> size="3" /> diopters</td>
			</tr>
			<%
			String axisODOut = ""; 
			if(bean.getAxisOD() != null){
				axisODOut = Integer.toString(bean.getAxisOD());
			}
			%>
			<tr>
				<td><label for="axisOD">Axis(OD): </label></td>
				<td><input type="text" name="axisOD" id="axisOD" <% out.write("value=\"" + axisODOut + "\""); %> size="3" /> </td>
			</tr>
			<%
			String axisOSOut = ""; 
			if(bean.getAxisOS() != null){
				axisOSOut = Integer.toString(bean.getAxisOS());
			}
			%>
			<tr>
				<td><label for="axisOS">Axis(OS): </label></td>
				<td><input type="text" name="axisOS" id="axisOS" <% out.write("value=\"" + axisOSOut + "\""); %> size="3" /> </td>
			</tr>
			<tr>
				<td><label for="addOD">Add(OD): </label></td>
				<td><input type="text" name="addOD" id="addOD" <% out.write("value=\"" + bean.getAddOD() + "\""); %> size="3" /> diopters</td>
			</tr>
			<tr>
				<td><label for="addOS">Add(OS): </label></td>
				<td><input type="text" name="addOS" id="addOS" <% out.write("value=\"" + bean.getAddOS() + "\""); %> size="3" /> diopters</td>
			</tr>
						<tr>
				<td><label for="surgery">Surgery: </label></td>
				<td><select name="surgery" id ="surgery">
					<%if("-- None Selected --".equals(bean.getSurgery())) {
						%><option value="-- None Selected --">-- None Selected --</option><%;%>
					<%} else {
						%><option value="<%= StringEscapeUtils.escapeHtml("" + (bean.getSurgery())) %>"><%= StringEscapeUtils.escapeHtml("" + (bean.getSurgery())) %></option><%; }%>
					<%if(!"Cataract surgery".equals(bean.getSurgery()))
						%><option value="Cataract surgery">Cataract surgery</option><%;%>
					<%if(!"Laser surgery".equals(bean.getSurgery()))
						%><option value="Laser surgery">Laser surgery</option><%;%>
					<%if(!"Refractive surgery".equals(bean.getSurgery()))
						%><option value="Refractive surgery">Refractive surgery</option><%;%>
				</select></td>
			</tr>
			<tr>
				<td><label for="surgeryNotes">Surgery Notes: </label></td>
				<td><input type="text" name="surgeryNotes" id="surgeryNotes" <% out.write("value=\"" + bean.getSurgeryNotes() + "\""); %> size="50" /></td>
			</tr>
		</table>
		</table>
		
		<br />
		<br />
		
		<input type="submit" id="submit" value="Submit" form="officeVisit" hidden="hidden"/>
	</form>
</div>

<form <% out.print("action=\"/iTrust/auth/hcp/editOphthalmologySurgery.jsp?oid=" + oidString + "\""); %> method="post" id="removeDiagnosisForm" >
<input type="hidden" name="formIsFilled" value="true" />
<input type="hidden" name="submittedFormName" value="removediagnosesForm" />
<input type="hidden" id="removeDiagID" name="removeDiagID" value="" />
<input type="hidden" name="ovID" value="<%= StringEscapeUtils.escapeHtml("" + oidString) %>" />
</form>

<form <% out.print("action=\"/iTrust/auth/hcp/editOphthalmologySurgery.jsp?oid=" + oidString + "\""); %> method="post" id="diagnosesForm" >
		<input type="hidden" name="formIsFilled" value="true" />
		<input type="hidden" name="submittedFormName" value="diagnosesForm" />
		<table class="fTable" align="center" >
	<tr>
		<th colspan="3">Diagnoses</th>
	</tr>
	<tr  class="subHeader">
		<th>ICD Code</th>
		<th>Description</th>
		<th>URL</th>
	</tr>
	<%
	EditOPDiagnosesAction diagAction = new EditOPDiagnosesAction(prodDAO,oidString);
	if (diagAction.getDiagnoses().size() == 0) { %>
	<tr>
		<td colspan="3" >No Diagnoses for this visit</td>
	</tr>
	<% } else { 
		for(OphthalmologyDiagnosisBean d : diagAction.getDiagnoses()) { String link = d.getURL();%>
		<tr>
			<td ><itrust:icd9cm code="<%= StringEscapeUtils.escapeHtml(d.getICDCode()) %>"/></td>
			<td  style="white-space: nowrap;"><%= StringEscapeUtils.escapeHtml("" + (d.getDescription() )) %></td>
			<td ><a
            href="javascript:removeDiagID('<%= StringEscapeUtils.escapeHtml("" + (d.getOpDiagnosisID())) %>');">Remove</a></td>
		</tr>
	   <%} 
  	   }  %>
  	       <tr>
        <th colspan="3" style="text-align: center;">New</th>
    </tr>
    <tr>
        <td colspan="3" align=center>
            <select name="ICDCode" style="font-size:10" >
            <option value="">-- None Selected --</option>
            <%for(OphthalmologyDiagnosisBean diag : diagAction.getDiagnosisCodes()) { %>
            <option value="<%=diag.getICDCode()%>"><%= StringEscapeUtils.escapeHtml("" + (diag.getICDCode())) %>
            - <%= StringEscapeUtils.escapeHtml("" + (diag.getDescription())) %></option>
            <%}%>
            </select>
            <input type="submit" id="add_diagnosis" value="Add Diagnosis" >
        </td>
    </tr>
</table>
<br /><br />
</form>
<p align="middle">
<input type="submit" id="submitoutside" value="Submit" form="officeVisit"/>
</p>
<p><br/><a href="/iTrust/auth/hcp/ophthalmologySurgeryHome.jsp">Back to Home</a></p>
	
<br/><br/>
<%@include file="/footer.jsp" %>