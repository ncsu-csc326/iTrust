<%@page import="org.apache.jasper.tagplugins.jstl.core.ForEach"%>
<%@page import="org.jfree.ui.Align"%>
<%@page import="edu.ncsu.csc.itrust.action.ReviewsAction"%>
<%@page import="edu.ncsu.csc.itrust.action.ZipCodeAction"%>
<%@page errorPage="/auth/exceptionHandler.jsp" %>
<%@page import="java.util.Collections" %>
<%@page import="java.net.URLEncoder" %>
<%@page import="java.util.List"%>
<%@page import="java.util.ArrayList"%>
<%@page import="edu.ncsu.csc.itrust.dao.DAOFactory"%>
<%@page import="edu.ncsu.csc.itrust.action.FindExpertAction"%>
<%@page import="edu.ncsu.csc.itrust.beans.PatientBean"%>
<%@page import="edu.ncsu.csc.itrust.beans.DistanceComparator"%>
<%@page import="edu.ncsu.csc.itrust.beans.RatingComparator"%>
<%@page import="edu.ncsu.csc.itrust.beans.HospitalBean"%>
<%@page import="edu.ncsu.csc.itrust.beans.PersonnelBean"%>
<%@page import="edu.ncsu.csc.itrust.dao.mysql.PatientDAO"%>
<%@page import="java.util.HashMap"%>

<%@include file="/global.jsp" %>

<% pageTitle = "iTrust - Find an Expert"; %>

<%@include file="/header.jsp" %>

<h1 class="page-header">Find an Expert </h1>
<div class="col-md-4">
<%
	//Create PatientBean object to get patient's zipcode
	PatientBean patient = new PatientDAO(prodDAO).getPatient(loggedInMID.longValue());
	FindExpertAction findExpertAction = new FindExpertAction(prodDAO);
	String specialty = "All";
	String zipCode = patient.getZip().substring(0, 5);
	String range = "All";
	String sortby = "Distance";
	if(request.getParameter("specialty") != null) {
		specialty = request.getParameter("specialty");
	}
	if(request.getParameter("zipCode") != null) {
		zipCode = request.getParameter("zipCode");
	}
	if(request.getParameter("range") != null) {
		range = request.getParameter("range");
	}
	if(request.getParameter("sortby") != null)
	{
		sortby = request.getParameter("sortby");
	}
	
%>
<form class="form-horizontal" role="form" align="left" action="findExpert.jsp" method="post" id="mainForm" name="mainForm">
	<div class="form-group">
		<label style="margin-left: 10px;" for="specialty">Specialty:</label>
		<select name="specialty" class="form-control">
			<option value="OB/GYN" <%if(specialty.equals("OB/GYN")) {%> selected = 'selected' <%}%>>OB/GYN</option>
			<option value= "Surgeon" <%if(specialty.equals("Surgeon")) {%> selected = 'selected' <%}%>>Surgeon</option>
			<option value="Heart Surgeon" <%if(specialty.equals("Heart Surgeon")) {%> selected = 'selected' <%}%>>Heart Surgeon</option>			
			<option value="Pediatrician" <%if(specialty.equals("Pediatrician")) {%> selected = 'selected' <%}%>>Pediatrician</option>
			<option value="General Physician" <%if(specialty.equals("General Physician")) {%> selected = 'selected' <%}%>>General Physician</option>
			<option value="All" <%if(specialty.equals("All")) {%> selected = 'selected' <%}%>>All Doctors</option>
		</select>
	</div><br>
	<div class="form-group">
		<label style="margin-left: 10px;" for="zipCode">ZIP Code:</label>
		<input class="form-control" type="text" name="zipCode" value=<%=zipCode%>>
	</div><br>
	<div class="form-group">
		<label style="margin-left: 10px;" for="range">Distance From ZIP Code: </label>
		<select class="form-control" name="range">
			<option value="All" <%if(range.equals("All")) {%> selected = 'selected' <%}%>>All</option>
			<option value="1500" <%if(range.equals("1500")) {%> selected = 'selected' <%}%>>1500 Miles</option>
			<option value="1000" <%if(range.equals("1000")) {%> selected = 'selected' <%}%>>1000 Miles</option>			
			<option value="500" <%if(range.equals("500")) {%> selected = 'selected' <%}%>>500 Miles</option>
			<option value="250" <%if(range.equals("250")) {%> selected = 'selected' <%}%>>250 Miles</option>
		</select>
	</div><br>
	<div class="form-group">
		<label style="margin-left: 10px;" for="sortby">Sort by: </label>
		<select class="form-control" name="sortby">
			<option value="Distance" <%if(sortby.equals("Distance")){ %> selected='selected' <%} %>>Distance</option>
			<option value="Rating" <%if(sortby.equals("Rating")){ %> selected='selected' <%} %>>Rating</option>	
		</select>
		<br>
		<br>
		<button class="btn btn-primary" type="submit" name="findExpert">Find Expert</button>
		<br>
		<br>
	</div>
	
	
</form>
</div>
<div class="col-md-7 col-md-offset-1">


 
		

<%

	zipCode = request.getParameter("zipCode");
	range = request.getParameter("range");
	specialty = request.getParameter("specialty");

	if (zipCode != null && !"null".equals(zipCode) &&
			range != null && !"null".equals(range) &&
			specialty != null && !"null".equals(specialty)) {

		List<PersonnelBean> beans = new ArrayList<PersonnelBean>();
		FindExpertAction fea = new FindExpertAction(DAOFactory.getProductionInstance());
		ZipCodeAction zipCodeAction = new ZipCodeAction(DAOFactory.getProductionInstance(), loggedInMID.longValue());
		ReviewsAction reviewsAction = new ReviewsAction(DAOFactory.getProductionInstance(), loggedInMID.longValue());
		//Get the zipcode and range
		zipCode = request.getParameter("zipCode");
		range = request.getParameter("range");
		//Check to see that entered zipcode contains only 5 digits
		if (zipCode.length() == 5 && zipCode.matches("[0-9]+")) {
			
			//Find experts belonging to hospitals in the user's zipcode
			loggingAction.logEvent(TransactionType.FIND_EXPERT, loggedInMID.longValue(), loggedInMID.longValue(), "");
			beans = zipCodeAction.getExperts(specialty, zipCode, range);
			
			if(sortby.equals("Distance"))
			{				
				Collections.sort(beans, new DistanceComparator(zipCodeAction, zipCode));
			}
			
				if(sortby.equals("Rating"))
			{				
				Collections.sort(beans, new RatingComparator(reviewsAction));
			}
			
				
			if (beans.size() > 0) {
		 		
				String expertName = "";
				String expertSpecialty;
				String distanceFromYou = "";
				String currentExpertName = null;
				double rating;
				
				for (PersonnelBean expert : beans) {
					currentExpertName = expertName;
					
	%><div id="<%=String.valueOf(expert.getMID())%>Modal" class="modal fade" tabindex="-1" role="dialog" aria-labelledby="viewPhysician" aria-hidden="true">
	<div class="modal-dialog">
	<div class="modal-content">			
	<div class="modal-header" >
	<button type="button" class="close" data-dismiss="modal" aria-hidden="true" >x</button>
	<h3 class="modal-title" id="viewPhysician"><%= expert.getFullName() %></h3>
	</div>
	<div class="modal-body">
	<p><b>About me:</b> </p>
	<p><i>Specialty: </i> <%=expert.getSpecialty()%> </p>
	<p><i>Phone: </i> <%= expert.getPhone() %></p>
	<p><i>Email: </i> <%= expert.getEmail() %></p>
	<br>
	<p> <b>Assigned Hospitals:</b></p>
	<%
		
		// Distance from the user to the closest hospital that the expert
		// practices at. Initial value of negative one is an invalid
		// distance. Distance is not displayed if no hospitals can
		// be calculated.
		long closestHospital = -1;
	
		List<HospitalBean> hospitals = findExpertAction.findHospitalsAssignedToHCP(expert.getMID());
	    int i = 0;
		for(HospitalBean hospital : hospitals)
		{	
			// Calculate the distance to the hospital
			long dist = zipCodeAction.calcDistance(hospital.getHospitalZip(), zipCode);
			
			// Only set closest if distance is not Integer.MAX_VALUE. Max distances
			// are returned when zip code is null.
			if (dist != Integer.MAX_VALUE) {
				
				// If there is not yet a closest hospital (closestHospital < 0) or
				// the current distance is less than the previous minimum, set current
				// distance as the minimum.
				if (closestHospital < 0 || dist < closestHospital) {
					closestHospital = dist;
				}
			}
	 %>
		<p><i>Hospital Name: </i> <%=hospital.getHospitalName()%></p>
		<p><i>Address: </i> <%=hospital.getHospitalAddress()%></p>
		<p><%= hospital.getHospitalCity() + ", " + hospital.getHospitalState() + "  " + hospital.getHospitalZip()%></p>
		<br>
	<%  } %>
	</div>
	<div class="modal-footer">
   	<button class="btn" data-dismiss="modal" aria-hidden="true">Close</button>
  	</div>
  	</div>
  	</div>
	</div><%
					expertName = expert.getFirstName() + " " + expert.getLastName();
					expertSpecialty = expert.getSpecialty();
					distanceFromYou = (closestHospital < 0) ? "Unknown" : Long.toString(closestHospital);
					rating = reviewsAction.getAverageRating(expert.getMID());
					if(!currentExpertName.equals(expertName))
					{ 
						i++;
						%>
							<div class="grey-border-container">
								<p><b>Physician Name:</b> <a href="#<%=expert.getMID()%>Modal" role="button" class="btn" data-toggle="modal"> <%=expertName%></a></p>
								<p><b>Distance From You:</b> <%= StringEscapeUtils.escapeHtml(distanceFromYou) + " miles" %></p>
								<p><b>Specialty:</b> <%= StringEscapeUtils.escapeHtml(specialty) %></p>
								<p><b>Rating:</b> <%= StringEscapeUtils.escapeHtml(rating != 0.0 ? Double.toString(rating) + " (based on " + reviewsAction.getReviews(expert.getMID()).size() + " reviews)" : expertName + " hasn't been reviewed yet.")%> <a href="reviewsPage.jsp?expertID=<%=expert.getMID()%>" id="<%= i %>" ><span class="font1">View</span></a>	</p>	
							</div>									
						<% 
					}
				}
			} else {
			%>
				<br>
				<i><b>No Results!</b></i>
			<%
			}
		} else {
			//Log zipcode error if user entered zip code with incorrect formatting
			loggingAction.logEvent(TransactionType.FIND_EXPERT_ZIP_ERROR, loggedInMID.longValue(), loggedInMID.longValue(), "");
		%>
			<i><b>ZIP Code must be 5 digits!</b></i>
		<%
		}
	}
%>
</div>

<%@include file="/footer.jsp" %>
