<%@page errorPage="/auth/exceptionHandler.jsp" %>

<%@page import="java.net.URLEncoder" %>
<%@page import="java.util.List"%>

<%@page import="java.util.ArrayList" %>

<%@page import="edu.ncsu.csc.itrust.*"%>
<%@page import="edu.ncsu.csc.itrust.model.old.dao.DAOFactory"%>
<%@page import="edu.ncsu.csc.itrust.model.old.beans.HospitalBean"%>
<%@page import="edu.ncsu.csc.itrust.action.UpdateHospitalListAction"%>
<%@page import="edu.ncsu.csc.itrust.exception.FormValidationException"%>

<%@page import="edu.ncsu.csc.itrust.model.old.dao.mysql.HospitalsDAO"%>
<%@page import="edu.ncsu.csc.itrust.model.old.beans.WardBean"%>
<%@page import="edu.ncsu.csc.itrust.model.old.beans.WardRoomBean"%>
<%@page import="edu.ncsu.csc.itrust.model.old.dao.mysql.WardDAO"%>
<%@page import="edu.ncsu.csc.itrust.model.old.beans.PersonnelBean"%>
<%@page import="edu.ncsu.csc.itrust.model.old.dao.mysql.PersonnelDAO"%>

<%@page import="edu.ncsu.csc.itrust.model.old.dao.mysql.PatientDAO"%>
<%@page import="edu.ncsu.csc.itrust.model.old.beans.PatientBean"%>

<!-- %@page import="edu.ncsu.csc.itrust.action.UpdateHospitalListAction"%-->



<%@include file="/global.jsp" %>

<%
pageTitle = "iTrust - Manage Wards";
%>

<%@include file="/header.jsp" %>
<% 

// need to do logging stuff???
// loggingAction.logEvent(TransactionType.DEMOGRAPHICS_VIEW, loggedInMID, pid, "");
// ^^^ a way to do it?

String newWard = request.getParameter("ward");
String newRoomName = request.getParameter("room");
String deleteWard = request.getParameter("removeWard");
String deleteRoomWard = request.getParameter("removeRoom");
String roomToRemove = request.getParameter("roomToRemove");
String hosp = request.getParameter("hospitals");
String newRoom	= request.getParameter("addRoom");
String wardToRemoveHCP = request.getParameter("wardToRemoveHCP");
String wardToAddHCP = request.getParameter("wardToAddHCP");

WardDAO wardDAO = new WardDAO(prodDAO);
PatientDAO patientDAO = new PatientDAO(prodDAO);
PersonnelDAO personnelDAO = new PersonnelDAO(prodDAO);

//Get a list of all the hospitals
HospitalsDAO hospitalsDAO = new HospitalsDAO(prodDAO);
List<HospitalBean> hospitals = hospitalsDAO.getAllHospitals();

//get hospital bean for the hospital that was selected
HospitalBean currentHospital = hospitalsDAO.getHospital(hosp);

//Add the new ward 
if( newWard!= null && !newWard.equals("") ){
	//add the ward
	
	long newWardHospitalID = Long.parseLong(currentHospital.getHospitalID());
	WardBean bean = new WardBean(0,newWard,newWardHospitalID);
	
	// need to check if the ward already exists
	wardDAO.addWard(bean);
	
	%>Ward <%=request.getParameter("ward")%> has been added<%

// Delete a selected ward
} else if(deleteWard!= null && !deleteWard.equals("") && request.getParameter("removeWard") != null){
	// check if the ward actually exists?
	
	// get the ward id
	long wardToRemove = Long.parseLong(deleteWard);
			
	// remove the ward from the database
	wardDAO.removeWard(wardToRemove);
			
	%>Ward <%=deleteWard%> has been deleted<%

}

//Add a new room to a specific ward
if(newRoom != null){
	// get the ward id of the ward a room is being added to
	long n = Long.parseLong(newRoom);
	
	WardRoomBean r = new WardRoomBean(0,0,n,newRoomName, "Clean");
	wardDAO.addWardRoom(r);
	
} else if(deleteRoomWard != null){	
	// get the room id of the room being removed
	long r = Long.parseLong(deleteRoomWard);
	
	wardDAO.removeWardRoom(r);
	
}

// Assign HCP to a ward
if(request.getParameter("addHCP") != null && request.getParameterValues("HCPtoAdd") != null){
	// get the list of HCPs to be added
	String[] HCPtoAdd = request.getParameterValues("HCPtoAdd");
	long wardID = Long.parseLong(wardToAddHCP);
	for(int i = 0; i < HCPtoAdd.length; i++){
		long hcpID = Long.parseLong(HCPtoAdd[i]);
	   	wardDAO.assignHCPToWard(hcpID, wardID);
	}
// Remove HCP from a ward
} else if(request.getParameter("removeHCP") != null && request.getParameterValues("HCPtoRemove") != null){
	// get the list of HCPs to be removed
	String[] HCPtoRemove = request.getParameterValues("HCPtoRemove");
	long wardID = Long.parseLong(wardToRemoveHCP);
	for(int i = 0; i < HCPtoRemove.length; i++){
		long hcpID = Long.parseLong(HCPtoRemove[i]);
    	wardDAO.removeWard(hcpID, wardID);
	}
}
%>

<div align=center>
	<form id="mainForm" method="post" action="manageWards.jsp">
	Select a Hospital:
	<select name="hospitals">
		<option value="<%=hosp %>"></option>
	<%for(HospitalBean b: hospitals){%>
		<option value="<%=b.getHospitalID()%>" name="<%=b.getHospitalName()%>"><%=b.getHospitalName()%></option>
		
	
	<%}%>
	</select>		
		<input type="submit" value="Select" name="selectHospital" />
	</form>
</div>
<br>
<br>

<% 
//Get a list of all the wards for the hospital that was picked
//Then create a table for each ward

if(hosp != null && !hosp.equals("") && !hosp.equals("null")){
	
%>

	You selected <%= currentHospital.getHospitalName() %>
	
	
<div align=center>
<form id="mainForm" method="post" action="manageWards.jsp">
	Ward Specialty:
	<input type="hidden" name="hospitals" value="<%=hosp%>"/>
	<input type="text" name="ward" />
	<input type="submit" name="addWard" value="Add Ward"/>
</form>
</div>

<hr>

<% 
	//get a list of all the ward beans for the hospital
	
	List<WardBean> wards = wardDAO.getAllWardsByHospitalID(currentHospital.getHospitalID());
	for(WardBean w: wards){
		
		// get the HCPs assigned to the ward
		List<PersonnelBean> HCP = wardDAO.getAllHCPsAssignedToWard(w.getWardID());
		
		// list of all the HCP not assigned to the ward
		List<PersonnelBean> notAssignedHCP = new ArrayList<PersonnelBean>();
		// all the personnel for the current hospital
		List<PersonnelBean> allPersonnelForCurrentHospital = personnelDAO.getPersonnelFromHospital(currentHospital.getHospitalID());
		
		// make the list of all the people not assigned to this ward
		for(PersonnelBean person: allPersonnelForCurrentHospital){
			// is the right way to do this???
			if(!HCP.contains(person)){
				notAssignedHCP.add(person);
			}
		}
%>

<div>
	<div align=center>
	<!-- <table class="fTable" style="float:center" align="left"> -->
	<table class="fTable" id="wardTable" align="left" height=200 style="overflow: auto;">
		<tr height=40>
			<th colspan="3"><%=w.getRequiredSpecialty()%></th>
		</tr>
		<tr class="subHeader" height=40>
			<td style="text-align: left;">Rooms</td>
			<td>Patient</td>
			<td></td>
		</tr>
		
		<% 
		//get a list of all the WardRooms for the ward
		
		List<WardRoomBean> rooms = wardDAO.getAllWardRoomsByWardID(w.getWardID());
		
		int numberOfRooms = rooms.size();
				
		for(WardRoomBean room: rooms){
		%>
		
		<tr>
			<td><%=room.getRoomName() %></td>
			<%if(room.getOccupiedBy() == 0){%>
			<td>NONE</td>
			<%  
			
			} else {
				if(patientDAO.checkPatientExists(room.getOccupiedBy())){
			%>
			<td><%=patientDAO.getName(room.getOccupiedBy()) %></td>
			<% 	} else { %>
				<td>NONE</td>
			<% 	}
			}%>
			<form id="mainForm" method="post" action="manageWards.jsp">
			<td><input type="submit" name="removeRoomButton" value="Remove Room"/></td>
				<input type="hidden" name="roomToRemove" value="<%=room.getRoomID()%>"/>
				<input type="hidden" name="hospitals" value="<%=hosp%>"/>
				<input type="hidden" name="removeRoom" value="<%=room.getRoomID()%>"/>
			</form>
		
		</tr>
		
		<%} %>
		<tr height=40>
			<form id="mainForm" method="post" action="manageWards.jsp">
			<td><input type="text" name="room" />
				<input type="submit" name="addRoomButton" value="Add Room"/></td>
				<input type="hidden" name="hospitals" value="<%=hosp%>"/>
				<input type="hidden" name="addRoom" value="<%=w.getWardID()%>"/>
			</form>
			<td></td>
			<td></td>
		</tr>
	</table>
	</div>
	
	<div align="" style="background: none repeat scroll 0 0 #EDEDED">
	<table class="fTable" height=200>
		<tr height=40>
			<th colspan="1">Assigned HCP</th>
			<th colspan="1" style="width: 100%;">Unassigned HCP</th>
		</tr>
		<tr>
			<%//numberOfRooms = 0; %>
			<td>
			<%if(HCP.isEmpty()){
				%>No HCP Assigned<%
			} else {%>
				<form method="post" action="manageWards.jsp">
				<select size="<%=HCP.size() %>" name="HCPtoRemove" multiple="yes" style="float: center"> 
				<%
				int i = 0;
				for(PersonnelBean h: HCP){ %>
					<%//if(personnelDAO.checkPersonnelExists(h.getMID())){ %>
					<!-- input type="hidden" name="HCPtoAssign" value=<%=h.getMID() %>/ -->
  					<option value=<%=h.getMID()%> ><%=h.getFullName()%> </option>
  					<%//} %>
				<%} 
				%></select>
				<br>
					<input type="hidden" name="hospitals" value="<%=hosp%>"/>
					<input type="hidden" name="wardToRemoveHCP" value="<%=w.getWardID()%>"/>
					<input type="submit" name="removeHCP" value="Remove Selected"/>
				</form>
			<%}%>
			
			</td>
			<td>
			<%if(notAssignedHCP.isEmpty()){
				%>No HCP To Assign<%
			} else {%>
				<form id="mainForm" method="post" action="manageWards.jsp">
				<select size="<%=notAssignedHCP.size() %>" name="HCPtoAdd" multiple="yes" style="float: center"> 
				<%for(PersonnelBean h: notAssignedHCP){ %>
					<%//if(personnelDAO.checkPersonnelExists(h.getMID())){ %>
  						<option value=<%=h.getMID() %> ><%=h.getFullName()%> </option>
  					<%//} 
  				}%>
				</select>
				<br>		
					<input type="hidden" name="hospitals" value="<%=hosp%>"/>
					<input type="hidden" name="wardToAddHCP" value="<%=w.getWardID()%>"/>
					<input type="submit" name="addHCP" value="Add Selected"/>
				</form>
			<%}%>
			</td>
		</tr>
	</table>
	<br clear="all" />
	</div>
	
<form id="mainForm" method="post" action="manageWards.jsp">
	<input type="hidden" name="hospitals" value="<%=hosp%>"/>
	<input type="hidden" name="removeWard" value="<%=w.getWardID()%>"/>
	<input type="submit" name="removeWardButton" value="Remove Ward"/>
</form>

<br>
<br>
</div>
<% 
	}
%>


<% 
}
%>


<%@include file="/footer.jsp" %>
