<%@page errorPage="/auth/exceptionHandler.jsp" %>

<%@page import="java.net.URLEncoder" %>
<%@page import="java.util.List"%>
<%@page import="edu.ncsu.csc.itrust.*"%>
<%@page import="edu.ncsu.csc.itrust.model.old.dao.DAOFactory"%>
<%@page import="edu.ncsu.csc.itrust.model.old.beans.HospitalBean"%>
<%@page import="edu.ncsu.csc.itrust.action.UpdateHospitalListAction"%>
<%@page import="edu.ncsu.csc.itrust.exception.FormValidationException"%>

<%@page import="edu.ncsu.csc.itrust.model.old.dao.mysql.HospitalsDAO"%>
<%@page import="edu.ncsu.csc.itrust.model.old.dao.mysql.PersonnelDAO"%>
<%@page import="edu.ncsu.csc.itrust.model.old.dao.mysql.PatientDAO"%>
<%@page import="edu.ncsu.csc.itrust.model.old.beans.WardBean"%>
<%@page import="edu.ncsu.csc.itrust.model.old.beans.WardRoomBean"%>
<%@page import="edu.ncsu.csc.itrust.model.old.dao.mysql.WardDAO"%>
<%@page import="edu.ncsu.csc.itrust.model.old.beans.PersonnelBean"%>

<%@page import="edu.ncsu.csc.itrust.action.EditPersonnelAction"%>
<%@page import="edu.ncsu.csc.itrust.action.PatientRoomAssignmentAction"%>


<%
	String patientID = (String) session.getAttribute("pid");
    session.removeAttribute("pid");
    String forwardPatientSearch = request.getParameter("forwardPatientSearch");
	
	
%>

<%@include file="/global.jsp" %>

<%
pageTitle = "iTrust - Manage Wards";
%>

<%@include file="/header.jsp" %>

<%

long personnelMID = loggedInMID.longValue();

WardDAO wardDAO = new WardDAO(prodDAO);
PatientDAO patientDAO = new PatientDAO(prodDAO);

// Get a list of all the hospitals
HospitalsDAO hospitalsDAO = new HospitalsDAO(prodDAO);
List<HospitalBean> hospitals = hospitalsDAO.getAllHospitals();

// Get a list of the hospitals assinged to the current HCP
PersonnelDAO personnelDAO = new PersonnelDAO(prodDAO);
List<HospitalBean> hospitalsForHCP = personnelDAO.getHospitals(personnelMID);

// Get a list of the wards the HCP is assigned to
List<WardBean> wardsForHCP = wardDAO.getAllWardsByHCP(personnelMID);

// Get the submitted parameters to search by
String hospital = request.getParameter("searchbyhospital");
String roomWard = request.getParameter("searchbyroomWard");
String roomStatus = request.getParameter("searchbyroomStatus");

// these are the names of the buttons
String searchWards = request.getParameter("searchWards");
String searchStatus = request.getParameter("searchStatus");
String searchHospitals = request.getParameter("searchHospitals");

// string to know which thing was searched by after clicking a button to update something
String searchedBy = "";
String valueSearchedBy = "";
String parameterName = "";


//Create Patient Room Assignment Action object
PatientRoomAssignmentAction praa = new PatientRoomAssignmentAction(prodDAO);

if(hospital == null && forwardPatientSearch != null){
	WardRoomBean wardRoom = wardDAO.getWardRoom(forwardPatientSearch);
	hospital = wardDAO.getHospitalByWard(forwardPatientSearch).getHospitalID();
	for(WardBean ward : wardDAO.getAllWardsByHospitalID(hospital)){
		for(WardRoomBean room : wardDAO.getAllWardRoomsByWardID(ward.getWardID())){
			if(room.getOccupiedBy() == Long.parseLong(patientID)){
				praa.removePatientFromRoom(room, "Moved Patient");
			}
		}
	}
	praa.assignPatientToRoom(wardRoom, Long.parseLong(patientID));
	loggingAction.logEvent(TransactionType.PATIENT_ASSIGNED_TO_ROOM, loggedInMID, 0, "");
	//hospital = "";
}

String removePatient = request.getParameter("removePatient");
String removePatientFromThisRoom = request.getParameter("removeRoomWard");

String changeStatus = request.getParameter("changeStatus");
// a room id
String roomChangeStatus = request.getParameter("roomChangeStatus");

//a room id
String removePatientRoomID = request.getParameter("removePatientRoomID");

//get hospital bean for the hospital that was selected
HospitalBean currentHospital = null;

if(hospital != null){
	currentHospital = hospitalsDAO.getHospital(hospital);
} 

// Change the status of a WardRoom
if(changeStatus != null && request.getParameter("statusToChange") != null && !request.getParameter("statusToChange").equals("")) {
	WardRoomBean statusUpdate = wardDAO.getWardRoom(roomChangeStatus);
	statusUpdate.setStatus(request.getParameter("statusToChange"));
	wardDAO.updateWardRoom(statusUpdate);
}

// Remove a patient
if(removePatient != null){
	WardRoomBean remove = wardDAO.getWardRoom(removePatientRoomID);
	String reason = request.getParameter("reason");
	praa.removePatientFromRoom(remove,reason);
	loggingAction.logEvent(TransactionType.PATIENT_REMOVED_FROM_ROOM, loggedInMID, 0, "");
}

//Get a list of all the WardRoomBeans that fit the search critera
List<WardRoomBean> listOfRooms = null ;

//for searching by hospital
if(hospital != null && (searchHospitals != null || forwardPatientSearch != null)){
	//get all the wards for the current hcp
	List<WardBean> wards = wardDAO.getAllWardsByHCP(personnelMID);		
	//for each ward all the rooms to the list of rooms
	for(WardBean w: wards){
		if(w.getInHospital() == Long.parseLong(hospital)){
			if(listOfRooms == null){
				listOfRooms = wardDAO.getAllWardRoomsByWardID(w.getWardID());
			} else {
				listOfRooms.addAll(wardDAO.getAllWardRoomsByWardID(w.getWardID()));
			}
		}
		
	}
	valueSearchedBy = hospital;
	searchedBy = "searchHospitals";
	parameterName = "searchbyhospital";
	
} else if(searchStatus != null){
	//get a list of rooms by status
	listOfRooms = wardDAO.getWardRoomsByStatus(roomStatus, personnelMID);
	searchedBy = "searchStatus";
	valueSearchedBy = roomStatus;
	parameterName = "searchbyroomStatus";
	
} else if(searchWards != null && roomWard != null && !roomWard.equals("")){
	List<WardBean> wards = wardDAO.getAllWardsByHCP(personnelMID);
	for(WardBean w: wards){
		if(w.getWardID() == wardDAO.getWardRoom(roomWard).getInWard()){
			if(listOfRooms == null){
				listOfRooms = wardDAO.getAllWardRoomsByWardID(w.getWardID());
			} else {
				listOfRooms.addAll(wardDAO.getAllWardRoomsByWardID(w.getWardID()));
			}
		}
	}
	valueSearchedBy = roomWard;
	searchedBy = "searchWards";
	parameterName = "searchbyroomWard";
}



%>

	<table class="fTable" align="center">
		<tr>
			<th colspan="3">Search for a Room</th>
		</tr>
		<tr class="subHeader">
			<td>Ward</td>
			<td>Status</td>
			<td>Hospital</td>
		</tr>
		<tr>
			<td>
			<form id="mainForm" method="post" action="manageWards.jsp" align="center">
				<select name="searchbyroomWard">
					<option value=""></option>
					<%for(WardBean b: wardsForHCP){%>
						<option value="<%=b.getWardID()%>" name="<%=b.getWardID()%>"><%=b.getRequiredSpecialty()%></option>
					<%}%>
				</select>
			<input type="submit" value="Search by Ward" name="searchWards" />
			</form>
			</td>
			
			<td>
			<form id="mainForm" method="post" action="manageWards.jsp" align="center">
				<select name="searchbyroomStatus">
					<option></option>
					<option value="Clean">Clean</option>
					<option value="Needs Cleaning">Needs Cleaning</option>
					<option value="Out of Service">Out of Service</option>
				</select>
			<input type="submit" value="Search by Status" name="searchStatus" />
			</form>
			</td>
			
			<td>
			<form id="mainForm" method="post" action="manageWards.jsp" align="center">
				<select name="searchbyhospital">
					<option value=""></option>
					<%for(HospitalBean b: hospitalsForHCP){%>
						<option value="<%=b.getHospitalID()%>" name="<%=b.getHospitalName()%>"><%=b.getHospitalName()%></option>
					<%}%>
				</select>
			<input type="submit" value="Search by Hospital" name="searchHospitals" />
			</form>
			</td>
		</tr>
	</table>
	<br>



<%
// Display results
if(searchHospitals != null || searchWards != null || searchStatus != null || forwardPatientSearch != null){
	if(listOfRooms != null && !listOfRooms.isEmpty()){

		// check if these rooms are full and if it is then log it
		boolean vacant = false;
		for(WardRoomBean room: listOfRooms){
			if(room.getOccupiedBy() == null){
				vacant = true;
			}
		}
		
		if(!vacant){
		//log if those rooms were full
		loggingAction.logEvent(TransactionType.ROOMS_FULL, loggedInMID, 0, "");
			
			
		} 
		
	%>
	<hr>
	<table class="fTable" align="center">
		<tr>
			<th colspan="5">Results</th>
			<th colspan="3">Options</th>
		</tr>
		<tr class="subHeader">
			<td>Room Name</td>
			<td>Patient</td>
			<td>Ward</td>
			<td>Status</td>
			<td>Hospital</td>
			<td>Assign Patient</td>
			<td>Remove Patient</td>
			<td>Change Status</td>
		</tr>
		<%for(WardRoomBean room: listOfRooms){ %>
		<tr>
			<td><%=room.getRoomName() %></td>
			<%if(room.getOccupiedBy() == 0 || !patientDAO.checkPatientExists(room.getOccupiedBy())){
				%>
				<td>Unoccupied</td>
				<%
			} else {
			%>
				<td><%=patientDAO.getName(room.getOccupiedBy())%></td>
			<%	
			}
			%>
			<td><%=wardDAO.getWard("" + room.getInWard()).getRequiredSpecialty()%></td>
			<td><%=room.getStatus()%></td>
			<td><%=wardDAO.getHospitalByWard(room.getRoomID() + "").getHospitalName() %></td>
			<td>
				<form id="mainForm" method="post" action=<%="http://localhost:8080/iTrust/auth/getPatientID.jsp?forward=hcp/manageWards.jsp?forwardPatientSearch=" + room.getRoomID() + "&searchHospitals=true"%> >				
					<input type="submit" value="Assign Patient" name="assignPatient" 
					<%if(room.getOccupiedBy() != 0){%>
					disabled 
					<%} %>/>
					<input type="hidden" value=<%=valueSearchedBy%> name=<%=searchedBy %>/>
				</form>
			</td>
			<td align="center"> 
				<form id="mainForm" method="post" action=<%="manageWards.jsp?"+ parameterName +"=" + valueSearchedBy + "&"+searchedBy+"=true"%> >
					<input type="text" name="reason"/>
					<input type="hidden" value="<%=room.getRoomID()%>" name="removePatientRoomID"/>
					<input type="hidden" value=<%=valueSearchedBy%> name=<%=searchedBy %>/>
					<input type="submit" value="Remove Patient" name="removePatient" 
					<%if(room.getOccupiedBy() == 0){%>
					disabled 
					<%} %>/>
				</form>
			</td>
			<td align="center">
				<form id="mainForm" method="post" action=<%="manageWards.jsp?"+ parameterName +"=" + valueSearchedBy + "&"+searchedBy+"=true"%>>
				<select name="statusToChange">
					<option></option>
					<option value="Clean">Clean</option>
					<option value="Needs Cleaning">Needs Cleaning</option>
					<option value="Out of Service">Out of Service</option>
				</select>
				<input type="hidden" value="<%=room.getRoomID()%>" name="roomChangeStatus"/>
				<input type="hidden" value=<%=valueSearchedBy%> name=<%=searchedBy %>/>
				<input type="submit" value="Change Status" name="changeStatus" />
				</form>
			</td>
		</tr>
		<%}%>
	</table>
	
	<% } else {
		%>
		<br>
		<div align=center>No Results</div>
		<%
	}


}%>

<%@include file="/footer.jsp" %>
