<%@page import="edu.ncsu.csc.itrust.dao.mysql.OphthalmologyOVRecordDAO"%>
<%@taglib uri="/WEB-INF/tags.tld" prefix="itrust"%>
<%@page errorPage="/auth/exceptionHandler.jsp"%>

<%@page import="java.util.List"%>
<%@page import="java.util.ArrayList"%>
<%@page import="edu.ncsu.csc.itrust.action.ViewPatientAction"%>
<%@page import="edu.ncsu.csc.itrust.beans.PatientBean"%>
<%@page import="edu.ncsu.csc.itrust.beans.OphthalmologyOVRecordBean"%>
<%@page import="edu.ncsu.csc.itrust.exception.ITrustException"%>
<%@page import="edu.ncsu.csc.itrust.action.ViewPersonnelAction"%> <!-- Used for specialty-checking -->
<%@page import="edu.ncsu.csc.itrust.beans.PersonnelBean"%> <!-- Used for specialty-checking -->
<%@page import="edu.ncsu.csc.itrust.action.AddOphthalmologyOVAction"%>
<%@page import="edu.ncsu.csc.itrust.action.EditPHRAction"%>
<%@page import="edu.ncsu.csc.itrust.beans.OphthalmologyOVRecordBean"%>
<%@page import="edu.ncsu.csc.itrust.exception.FormValidationException" %>
<%@page import="edu.ncsu.csc.itrust.action.EditOPDiagnosesAction"%> 
<%@page import="edu.ncsu.csc.itrust.beans.OphthalmologyDiagnosisBean"%>
<%@page import="edu.ncsu.csc.itrust.validate.OphthalmologyDiagnosisBeanValidator"%>
<%@page import="edu.ncsu.csc.itrust.BeanBuilder"%>

<%@include file="/global.jsp"%>

<%pageTitle = "iTrust - Add Ophthalmology Office Visit";%>

<%@include file="/header.jsp"%>
<%
	/* Require a Patient ID first */
	String pidString = (String) session.getAttribute("pid");
	if (pidString == null || 1 > pidString.length()) {
		response.sendRedirect("/iTrust/auth/getPatientID.jsp?forward=/iTrust/auth/hcp/addOphthalmologyOV.jsp");
		return;
	}
	
	//get the current patient (Might be null!)
	ViewPatientAction patientAction = new ViewPatientAction(prodDAO, loggedInMID, pidString);
	PatientBean chosenPatient = patientAction.getPatient(pidString);


	
	//if specialty is not oph or opt, simply redirect them to the regular edit office visit page
	ViewPersonnelAction personnelAction = new ViewPersonnelAction(prodDAO, loggedInMID);
	PersonnelBean currentPersonnel = personnelAction.getPersonnel("" + loggedInMID);
	if (!currentPersonnel.getSpecialty().equalsIgnoreCase("ophthalmologist") && !currentPersonnel.getSpecialty().equalsIgnoreCase("optometrist")) {
		response.sendRedirect("/iTrust/auth/hcp-uap/editOfficeVisit.jsp");
	}
	
	//don't run unless the form was actually submitted
	if ("true".equals(request.getParameter("formIsFilled"))) {
		//prepare to add beans!
		AddOphthalmologyOVAction addAction = new AddOphthalmologyOVAction(prodDAO, loggedInMID);
		boolean addedSomething = false;
		
		//add the initial record
		String date = request.getParameter("date");
		String docLastName = currentPersonnel.getLastName();
		String docFirstName = currentPersonnel.getFirstName();
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
		
		try {
			String clientSideErrors = "<p class=\"iTrustError\">This form has not been validated correctly. " +
			"The following field are not properly filled in: [";
			boolean hasCSErrors = false;
			OphthalmologyOVRecordBean ophBean = new OphthalmologyOVRecordBean();
				ophBean.setMid(Long.parseLong(pidString));
				ophBean.setVisitDate(date);
				ophBean.setLastName(docLastName);
				ophBean.setFirstName(docFirstName);
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
					clientSideErrors += "SphereOD is required, must be between -10.00 and 10.00 inclusive, and must be rounded to the nearest quarter diopter.";
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
					clientSideErrors += "SphereOS is required, must be between -10.00 and 10.00 inclusive, and must be rounded to the nearest quarter diopter.";
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
					clientSideErrors += "CylinderOD must be between -10.00 and 10.00 inclusive, and must be rounded to the nearest quarter diopter.";
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
					clientSideErrors += "CylinderOS must be between -10.00 and 10.00 inclusive, and must be rounded to the nearest quarter diopter.";
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
					clientSideErrors += "AxisOD is required if a CylinderOD is given, and must be between 1 and 180 inclusive.";
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
					clientSideErrors += "AxisOS is required if a CylinderOS is given, and must be between 1 and 180 inclusive.";
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
					clientSideErrors += "AddOD is required, must be between 0.75 and 3.00 inclusive, and must be rounded to the nearest quarter diopter.";
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
					clientSideErrors += "AddOS is required, must be between 0.75 and 3.00 inclusive, and must be rounded to the nearest quarter diopter.";
					hasCSErrors = true;
				}
				
				
			if (hasCSErrors) {
				out.write(clientSideErrors + "]</p>");
				clientSideErrors = "";
			}else{
				addAction.addOphthalmologyOV(ophBean);
				if(!request.getParameter("ICDCode").equals("")){
					EditOPDiagnosesAction diagnoses =  new EditOPDiagnosesAction(prodDAO,""+ophBean.getOid()); 
					OphthalmologyDiagnosisBean beanSub = new BeanBuilder<OphthalmologyDiagnosisBean>().build(request.getParameterMap(), new OphthalmologyDiagnosisBean());
					//validator requires description but DiagnosesDAO does not. Set here to pass validation.
					beanSub.setDescription("no description");
			    	try {
			    		OphthalmologyDiagnosisBeanValidator validator = new OphthalmologyDiagnosisBeanValidator();
			    		validator.validate(beanSub);
			    		beanSub.setVisitID(ophBean.getOid());
			        	diagnoses.addDiagnosis(beanSub);
			   	 	} catch (FormValidationException e) {
			   	 		response.sendRedirect("/iTrust/auth/hcp/ophthalmologyHome.jsp");
						throw new ITrustException("Invalid data entered into Ophthalmology office visit creator."); 
						
			    	}
					
				}
					
				addedSomething = true;
			}
			
			if(addedSomething){
				response.sendRedirect("/iTrust/auth/hcp/ophthalmologyHome.jsp?addOV");
			}
		}
		catch(FormValidationException e) {
				response.sendRedirect("/iTrust/auth/hcp/ophthalmologyHome.jsp");
				throw new ITrustException("Invalid data entered into Ophthalmology office visit creator.");
		}
	}
	
%>

<div id="mainpage" align="center">

	<form action="/iTrust/auth/hcp/addOphthalmologyOV.jsp" method="post" id="officeVisit" >
		<input type="hidden" name="formIsFilled" value="true" />
		<table class="fTable" align="center">
			<tr><th colspan="3">Add Ophthalmology Office Visit</th></tr>
			<tr>
				<td width="200px"><label for="date">Date of visit: </label></td>
				<td width="200px">
					<input type="text" name="date" id="date" size="7" />
					<input type="button" value="Select Date" onclick="displayDatePicker('date');" />
				</td>
				<td width="200px" id="date-invalid"></td>
			</tr>
			<tr>
				<td><label for="visAcNumOD">Visual Acuity Numerator OD: </label></td>
				<td><input type="text" name="vaNumOD" id="vaNumOD" size="4" /> </td>
				<td></td>
			</tr>
			<tr>
				<td><label for="visAcDenumOD">Visual Acuity Denumerator OD: </label></td>
				<td><input type="text" name="vaDenOD" id="vaDenOD" size="4" /> </td>
				<td></td>
			</tr>
			<tr>
				<td><label for="visAcNumOS">Visual Acuity Numerator OS: </label></td>
				<td><input type="text" name="vaNumOS" id="vaNumOS" size="4" /> </td>
				<td></td>
			</tr>
			<tr>
				<td><label for="visAcDenumOS">Visual Acuity Denumerator OS: </label></td>
				<td><input type="text" name="vaDenOS" id="vaDenOS" size="4" /> </td>
				<td></td>
			</tr>
			<tr>
				<td><label for="sphereOD">Sphere(OD): </label></td>
				<td><input type="text" name="sphereOD" id="sphereOD" size="3" /> diopters</td>
				<td></td>
			</tr>
			<tr>
				<td><label for="sphereOS">Sphere(OS): </label></td>
				<td><input type="text" name="sphereOS" id="sphereOS" size="3" /> diopters</td>
				<td></td>
			</tr>
			<tr>
				<td><label for="cylinderOD">Cylinder(OD): </label></td>
				<td><input type="text" name="cylinderOD" id="cylinderOD" size="3" /> diopters</td>
				<td></td>
			</tr>
			<tr>
				<td><label for="cylinderOS">Cylinder(OS): </label></td>
				<td><input type="text" name="cylinderOS" id="cylinderOS" size="3" /> diopters</td>
				<td></td>
			</tr>
			<tr>
				<td><label for="axisOD">Axis(OD): </label></td>
				<td><input type="text" name="axisOD" id="axisOD" size="3" /> </td>
				<td></td>
			</tr>
			<tr>
				<td><label for="axisOS">Axis(OS): </label></td>
				<td><input type="text" name="axisOS" id="axisOS" size="3" /> </td>
				<td></td>
			</tr>
			<tr>
				<td><label for="addOD">Add(OD): </label></td>
				<td><input type="text" name="addOD" id="addOD" size="3" /> diopters</td>
				<td></td>
			</tr>
			<tr>
				<td><label for="addOS">Add(OS): </label></td>
				<td><input type="text" name="addOS" id="addOS" size="3" /> diopters</td>
				<td></td>
			</tr>
		</table>
		
		<br />
		
		<table class="fTable" align="center">
	 <tr>
        <th colspan="3" style="text-align: center;">Diagnosis</th>
    </tr>
    <tr>
        <td colspan="3" align=center>
            <select name="ICDCode" style="font-size:10" >
            <option value="">-- None Selected --</option>
            <%
            EditOPDiagnosesAction diagAction = new EditOPDiagnosesAction(prodDAO,"0");
            for(OphthalmologyDiagnosisBean diag : diagAction.getDiagnosisCodes()) { %>
            <option value="<%=diag.getICDCode()%>"><%= StringEscapeUtils.escapeHtml("" + (diag.getICDCode())) %>
            - <%= StringEscapeUtils.escapeHtml("" + (diag.getDescription())) %></option>
            <%}%>
            </select>
            
        </td>
    </tr>
</table>
 	<br />
		<input type="submit" id="submit" value="Submit" />
	</form>
</div>

<p><br/><a href="/iTrust/auth/hcp/ophthalmologyHome.jsp">Back to Home</a></p>
<a name="diagnoses"></a>

<%@include file="/footer.jsp" %>