<%@page import="java.util.Date"%>
<%@page import="edu.ncsu.csc.itrust.model.old.beans.ReviewsBean"%>
<%@page import="org.jfree.ui.Align"%>
<%@page import="edu.ncsu.csc.itrust.action.ReviewsAction"%>
<%@page import="edu.ncsu.csc.itrust.action.ZipCodeAction"%>
<%@page errorPage="/auth/exceptionHandler.jsp" %>
<%@page import="java.net.URLEncoder" %>
<%@page import="java.util.List"%>
<%@page import="java.util.ArrayList"%>
<%@page import="edu.ncsu.csc.itrust.model.old.dao.DAOFactory"%>
<%@page import="edu.ncsu.csc.itrust.action.FindExpertAction"%>
<%@page import="edu.ncsu.csc.itrust.model.old.beans.PatientBean"%>
<%@page import="edu.ncsu.csc.itrust.model.old.beans.HospitalBean"%>
<%@page import="edu.ncsu.csc.itrust.model.old.beans.PersonnelBean"%>
<%@page import="edu.ncsu.csc.itrust.model.old.dao.mysql.PatientDAO"%>
<%@page import="java.util.HashMap"%>

<%@include file="/global.jsp" %>

<% pageTitle = "iTrust - Reviews Page"; %>

<%@include file="/header.jsp" %>



<%
	String mid = request.getParameter("expertID");
	String rating = null;
	long expertID = -1;
	if(mid != null)
	{
		expertID = Long.parseLong(mid);
		session.setAttribute("expertID", mid);
		loggingAction.logEvent(TransactionType.VIEW_REVIEWS, loggedInMID, expertID, "");
		response.sendRedirect("/iTrust/auth/patient/reviewsPage.jsp");
		return;
	}
	if(session.getAttribute("expertID") != null){
		try {
			expertID = Long.parseLong((String)session.getAttribute("expertID"));
		} catch (NumberFormatException e){
			%> <h1>User does not exist!</h1> <%
			return;
		}
	}
	
	ReviewsAction reviewsAction = new ReviewsAction(prodDAO, loggedInMID.longValue()); 
		String reviewTitle = request.getParameter("title");
		String reviewRating = request.getParameter("rating");
		String description = request.getParameter("description");
	
		if(reviewTitle != null && reviewRating != null && description != null)
		{
			loggingAction.logEvent(TransactionType.SUBMIT_REVIEW, loggedInMID, expertID, "");
			ReviewsBean review = new ReviewsBean();
			review.setDescriptiveReview(description);
			review.setRating(Integer.parseInt(reviewRating));
			review.setTitle(reviewTitle);
			review.setMID(loggedInMID.longValue());
			review.setPID(expertID);
			review.setDateOfReview(new Date());
			
			reviewsAction.addReview(review);
			
		}
	
	if(expertID != -1)
	{
		List<ReviewsBean> reviews = reviewsAction.getReviews(expertID);
		PersonnelBean physician = reviewsAction.getPhysician(expertID);
		%><h1>Reviews for <%=physician.getFullName()%></h1>
		<br>
		<%
		if(reviews.size() == 0)
		{
			%><p><i> <%=physician.getFullName() %> has not been reviewed yet.</i></p><%
		}
		for(ReviewsBean reviewBean : reviews )
		{ %> 
			<div class="grey-border-container">
				<p> <b><%= reviewBean.getTitle()%> </b> <span style="margin-right:10px"></span>
				
					<%
					for(int i = 0 ; i < 5 ; i++)
					{ 
						if(i < reviewBean.getRating())
						{
							%> <span class="glyphicon glyphicon-star" style="color:red;"></span><% 
						}
						else
						{
							%> <span class="glyphicon glyphicon-star-empty"></span><% 
						}
						
					}
					
					
					%>
				    </p>
				<p><%= reviewBean.getDescriptiveReview() %> </p>
				<p><%= reviewBean.getDateOfReview()%></p>
			</div>	
		
	  <%}
	  
	  
	  		if(reviewsAction.isAbleToRate(expertID))
	  		{
	  %>
	  	<a href="#addModal" role="button" class="btn btn-primary" data-toggle="modal">Add a Review</a>
 
		
				<div id="addModal" class="modal fade" tabindex="-1" role="dialog" aria-labelledby="addReview" aria-hidden="true">
					<div class="modal-dialog">
					<div class="modal-content">
					
						<div class="modal-header" >
						<button type="button" class="close" data-dismiss="modal" aria-hidden="true" >x</button>
						<h3 class="modal-title" id="addReview">Add a Review</h3>
					 </div>
					 <div class="modal-body">
					 	<form class="form-horizontal" role="form" method="post" id="mainForm" name="mainForm">
					 	<div class="form-group">
					 	<p> 
					 		<b>Title: </b> <input class="form-control" type="text" width="1" name="title">
					 	</p>
					 	 </div>
					 	<br>
					 	<div class="form-group">
					 	<b>Rating (out of 5): </b>
						<select class="form-control" name="rating">
						<option value="1">1</option>
						<option value= "2">2</option>
						<option value="3">3</option>			
						<option value="4">4</option>
						<option value="5">5</option>
						</select>
						</div>
						<br>
						<br>
						<div class="form-group">
						<p>
							<b>Describe your experience: </b> <textarea style="margin-top:5px;width:100%;" rows="4" cols="80" name="description" class="form-control"></textarea> 
						</p>
						</div>
					 <div class="modal-footer">
   					 <button class="btn" data-dismiss="modal" aria-hidden="true">Close</button>
    				<button type="submit" name="addReview" value="Add review" class="btn btn-primary">Add review</button>
    				</div>
    				</form>
    				
  					</div>
  					</div>
  					</div>
				</div>

				<%
				}
				 %>

<% }
	else
	{
	%> <h1>User does not exist!</h1> <% 
	}
	
 %>
	


<%@include file="/footer.jsp"%>
