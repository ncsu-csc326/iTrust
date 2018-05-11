<%@page errorPage="/auth/exceptionHandler.jsp"%>

<%@page import="java.util.List"%>
<%@page import="java.util.ArrayList"%>
<%@page import="edu.ncsu.csc.itrust.action.ViewFoodEntryAction"%>
<%@page import="edu.ncsu.csc.itrust.beans.PersonnelBean"%>
<%@page import="java.lang.Long"%>
<%@page import="java.lang.NumberFormatException"%>
<%@page import="edu.ncsu.csc.itrust.action.DesignateNutritionistAction"%>
<%@page import="edu.ncsu.csc.itrust.exception.ITrustException"%>
<%@page import="java.lang.IllegalArgumentException"%>
<%@page import="edu.ncsu.csc.itrust.exception.FormValidationException"%>


<%@include file="/global.jsp"%>

<%
	pageTitle = "iTrust - Designate a Nutritionist";
%>

<%@include file="/header.jsp"%>

<div align=center>
	<h2>My Designated Nutritionist</h2>
	
<%
	DesignateNutritionistAction desAction = new DesignateNutritionistAction(prodDAO, loggedInMID.longValue());
	List<PersonnelBean> nutritionists = desAction.getAllNutritionists(); //used for select option
	int updated = -5; //-5 is not an option, so if it's -5, do nothing
	String message = ""; //message used for letting user know result of operation
	
	/* Now take care of updating information */
	boolean formIsFilled = request.getParameter("formIsFilled") != null
			&& request.getParameter("formIsFilled").equals("true");
			
	if (formIsFilled) {
		try {
			//get the mid of the hcp we want to change to
			String nutrMIDStr = request.getParameter("nutritionist");
			if (nutrMIDStr != null && !nutrMIDStr.equals("")) {
				long nutrMID = Long.parseLong(nutrMIDStr);
				//now that we have the MID, update it
				updated = desAction.updateNutritionist(nutrMID);
				//the new nutritionist (so we can print out his name)
				PersonnelBean nutr = desAction.getDesignatedNutritionist();
				String nutrName = "";
				if (nutr != null) {
					nutrName = nutr.getFirstName() + " " + nutr.getLastName();
				}
				if (updated == -2) {
					message = "Sorry, but " + nutrMID 
							+ " is not a Nutritionist.";
				} else if (updated == -1) {
					message = "" + nutrMID + " is already your Nutritionist";
				} else if (updated == 0) {
					if (nutrMID == -1) {
						/* desAction will return 0 when trying to delete a 
						* Nutritionist when you already don't have one
						*/
						message = "Congratulations! You no longer have " + 
								"a designated Nutritionist";
					} else {
						message = "Sorry, but we could not process your command";
					}
				} else if (updated == 1) {
					if (nutrMID == -1) {//they deleted their nutritionist
						message = "Congratulations! You no longer have " + 
							"a designated Nutritionist";
					} else {
						message = "Congratulations! You switched your " + 
							"designated Nutritionist to " + nutrName;
					}
					loggingAction.logEvent(TransactionType.EDIT_DESIGNATED_NUTRITIONIST, 
						loggedInMID.longValue(), loggedInMID.longValue(), "");
				} else {
					message = "Sorry, I do not know what you want";
				}
			}
%>
			<div align=center>
				<span><%=StringEscapeUtils.escapeHtml(message)%></span>
			</div>
			</br>
<%
		} catch (NumberFormatException d) {
%>
		<div align=center>
			<span class="iTrustError"><%=StringEscapeUtils.escapeHtml(d.getMessage())%></span>
		</div>
<%
		}
	}
%>
	<form id="desNutrition" action="designatedNutritionist.jsp" method="post">
		<input type="hidden" name=formIsFilled value="true">
		<select name="nutritionist">
			<option value="-1">None</option>
<% 				for (PersonnelBean nutr: nutritionists) {
%>
						<option value="<%=
							StringEscapeUtils.escapeHtml("" + nutr.getMID()) %>"
							<%if (desAction.getDesignatedNutritionist() != null &&
							desAction.getDesignatedNutritionist().getMID() == nutr.getMID()) {%>
								selected <%}%>>
						<%= StringEscapeUtils.escapeHtml(nutr.getFirstName() 
								+ " " + nutr.getLastName()) %> </option>
<%
					}
%>
					
				</select>
			</br>
			</br>
			</br>
		<div align="center">
			<input type="submit" style="font-weight:bold;"
				value="Edit your Designated Nutritionist"> 
		</div>
	</form>
	<br />
	
</div>

<%@include file="/footer.jsp"%>
