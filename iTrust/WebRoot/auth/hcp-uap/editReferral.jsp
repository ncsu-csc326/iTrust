<%@taglib prefix="itrust" uri="/WEB-INF/tags.tld" %>
<%@page errorPage="/auth/exceptionHandler.jsp" %>

<%@page import="java.util.List"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.Date"%>

<%@page import="edu.ncsu.csc.itrust.exception.ITrustException"%>
<%@page import="edu.ncsu.csc.itrust.exception.FormValidationException"%>
<%@page import="edu.ncsu.csc.itrust.action.EditSingleReferralAction"%>
<%@page import="edu.ncsu.csc.itrust.beans.ReferralBean"%>
<%@page import="edu.ncsu.csc.itrust.BeanBuilder"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="edu.ncsu.csc.itrust.validate.ReferralBeanValidator"%>
<%@page import="edu.ncsu.csc.itrust.enums.TransactionType"%>

<%@include file="/global.jsp" %>

<%
	pageTitle = "iTrust - Edit Referral";
%>

<%@include file="/header.jsp" %>

<%!String getParameter(String name, HttpServletRequest request) {
	String r = request.getParameter(name);
	if (r==null) r = "";
	return r;
}

String getParameterOrSessionAttr(String name, HttpServletRequest request, HttpSession session) {
	String r = request.getParameter(name);
    if (r==null) {
    	r = (String)session.getAttribute("referral_"+name);
    	if (r!=null) {
    		session.removeAttribute("referral_"+name);
    	} else {
    		r = "";
    	}
    }
    return r;
}%>


<%
	EditSingleReferralAction action = new EditSingleReferralAction(prodDAO, loggedInMID);

String updateMessage = "";

String pidString = (String)session.getAttribute("pid");

// The URL to redirect to after the edit or creation of the referral is 
// successful.
String returnToURL = getParameterOrSessionAttr("returnToURL", request, session);

// The office visit this referral belongs to.  This value is only used when 
// creating new referrals.  For existing referrals, the office visit is read 
// from the referral itself.  
String ovIDString = getParameterOrSessionAttr("ovID", request, session);

// The referral identifier.  If this is a new referral, this is a special 
// value (-1).  Otherwise, there must be a referral in the database with this 
// id.
String referralIDString = getParameter("referralID", request);

// Was the form submitted?  If so, then process the information, and if there 
// are no errors redirect to another page. 
String submitCancelString = getParameter("submitCancel", request);
String submitEditString = getParameter("submitEdit", request);
String submitDeleteString = getParameter("submitDelete", request);
String confirmDeleteString = getParameter("confirmDelete", request);
String submitCreateString = getParameter("submitCreate", request);

ReferralBeanValidator validator = new ReferralBeanValidator();



// If the form was cancelled, just do the redirect.
if (submitCancelString.length() > 0) {
	response.sendRedirect(returnToURL+"#referrals");
	return;
}

// returnToURL might already have parameters in it depending on how this page was reached
// if it doesn't contain a ?, we need to put that before our additional parameters
// otherwise, we need to use an & instead
String returnToURLDelimeter = "?";
if(returnToURL.contains("?"))
	returnToURLDelimeter = "&";

ReferralBean referralBean = null;
boolean existingReferral = false;
boolean askConfirmDelete = false;

String newPriorityString = "3";
String referralDetails = "";

String errmsg = "";

// Unknown referral.  We must be creating a new referral.
if (referralIDString.length() == 0 || referralIDString.equals("-1")) {
	// ensure we have the parameters we expect to have
	if (ovIDString.length() == 0 || ovIDString.equals("-1")) {
		throw new ITrustException("When the referral ID is not given, the office visit ID is required.");
	}
    if (pidString == null || pidString.length() < 1 || pidString.equals("-1") || pidString.equals("null")) {
        throw new ITrustException("When the referral ID is not given, the patient ID is required.");
    }
       
	referralIDString = "-1";
	// Form was submitted.  This form is being displayed in response to a submit 
    // button.  We must be creating or cancelling a referral.
	if (submitCreateString.length() > 0) {
		referralBean = new ReferralBean();
		referralBean.setSenderID(loggedInMID);
        referralBean.setReceiverID(Long.parseLong(request.getParameter("receiverID")));        
        referralBean.setPatientID(Long.parseLong(request.getParameter("patientID")));
        referralBean.setOvid(Long.parseLong(ovIDString));
        referralBean.setReferralDetails(request.getParameter("referralDetails"));
        referralBean.setPriority(Integer.parseInt(request.getParameter("priority")));
        referralBean.setViewedByHCP(false);
        referralBean.setViewedByPatient(false);
        
        // validate
        try {
        	validator.validate(referralBean);
            action.addReferral(referralBean);            
            loggingAction.logEvent(TransactionType.CONSULTATION_REFERRAL_CREATE,
            		               loggedInMID.longValue(), 
            		               Long.parseLong(request.getParameter("patientID")), 
            		               "");
            response.sendRedirect(returnToURL+returnToURLDelimeter+"r=added#referrals");
            return;
            
        } catch (FormValidationException e) {
        	errmsg = e.printHTMLasString();
        	referralDetails = referralBean.getReferralDetails();
        } catch (NumberFormatException e) {
        	//patientID is null
        }
	}
	// Form was not submitted.  This form is being displayed for the first time.
	else {
		// If no mid (receiving HCP) is present, we have to let the user look 
		// it up.  Normally, this would lose all our HTTP POST Parameters.  So, 
		// to save those parameters, we write them to the session.  We delete 
		// them as soon as we return to this page. (The getParameterOrSessionAttr 
        // method above retrieves the session attributes and deletes them.)
		String session_receivingHCP = (String)session.getAttribute("mid");
		if (session_receivingHCP==null || session_receivingHCP.length()==0) {
	session.setAttribute("referral_returnToURL", returnToURL);
            session.setAttribute("referral_ovID", ovIDString);
	response.sendRedirect("/iTrust/auth/getPersonnelID.jsp?forward=hcp-uap/editReferral.jsp");
	return;
		} else {
	session.removeAttribute("referral_returnToURL");
            session.removeAttribute("referral_ovID");
            session.removeAttribute("mid");
		}
        referralBean = new ReferralBean();
        referralBean.setOvid(Long.parseLong(ovIDString));
        referralBean.setPatientID(Long.parseLong(pidString));
        referralBean.setSenderID(loggedInMID);
        referralBean.setReceiverID(Long.parseLong(session_receivingHCP));
	}
// Known referral.  We must be editing an existing referral.
} else {
    existingReferral = true;
    referralBean = action.getReferral(Long.parseLong(referralIDString));

    // Form was submitted.  This form is being displayed in response to a submit 
    // button.  We are editing an existing referral.
	if (submitEditString.length() > 0) {
        referralBean.setReferralDetails(request.getParameter("referralDetails"));
        referralBean.setPriority(Integer.parseInt(request.getParameter("priority")));
        
        // validate
        try {
            validator.validate(referralBean);
            action.editReferral(referralBean);            
            loggingAction.logEvent(TransactionType.CONSULTATION_REFERRAL_EDIT,
			               loggedInMID.longValue(), 
			               Long.parseLong(request.getParameter("patientID")), 
			               "");
            response.sendRedirect(returnToURL+returnToURLDelimeter+"r=edited#referrals");
            return;
            
        } catch (FormValidationException e) {
            errmsg = e.printHTMLasString();
            referralDetails = referralBean.getReferralDetails();
        } catch (NumberFormatException e) {
        	//pidString is null but that's okay because the Referral ID is not null
        }
	}
    // Form was submitted.  This form is being displayed in response to a submit 
    // button.  We are deleting an existing referral, and the delete was 
    // confirmed.
	else if (submitDeleteString.length() > 0 && confirmDeleteString.length() > 0) {
        action.deleteReferral(referralBean);
        loggingAction.logEvent(TransactionType.CONSULTATION_REFERRAL_CANCEL,
        		               loggedInMID.longValue(), 
        		               Long.parseLong(request.getParameter("patientID")), 
        		               "");
        response.sendRedirect(returnToURL+returnToURLDelimeter+"r=deleted#referrals");
        return;
	}
    // Form was submitted.  Confirm the delete befroe actually deleting the 
    // referral.
	else if (submitDeleteString.length() > 0) {
		referralIDString = Long.toString(referralBean.getId());
        ovIDString = Long.toString(referralBean.getOvid());
        askConfirmDelete = true;
	}
	// Form was not submitted.  This form is being displayed for the first time.
	else {
        referralIDString = Long.toString(referralBean.getId());
        ovIDString = Long.toString(referralBean.getOvid());
	}
}

String specialty = action.getReceivingHCPSpecialty(referralBean);
if (specialty.equals("")) {
	specialty = "no specialty";
}


if (existingReferral) {
	newPriorityString = "" + referralBean.getPriority();
	referralDetails = referralBean.getReferralDetails();
}


String disabledAttr = "";
if (referralBean.isViewedByHCP() || askConfirmDelete) {
	disabledAttr = "disabled=\"true\"";
}
%>

<% if (!"".equals(errmsg)) { %>
    <div style="background-color:yellow;color:black" align="center">
        <%= errmsg %>
    </div>
<% } %>



<form action="editReferral.jsp" id="editReferralForm" method="post">
    <input type="hidden" id="ovID" name="ovID" value="<%= ovIDString %>" />
    <input type="hidden" id="id" name="id" value="<%= referralIDString %>" />
    <input type="hidden" id="returnToURL" name="returnToURL" value="<%= returnToURL %>" />
    <input type="hidden" id="receiverID" name="receiverID" value="<%= Long.toString(referralBean.getReceiverID()) %>" />
	<input type="hidden" id="patientID" name="patientID" value="<%= Long.toString(referralBean.getPatientID()) %>" />


<table class="fTable" align="center" id="referralTable">
    <tr>
        <th colspan="2" style="text-align: center;">
            Referral
        </th>
    </tr>
    <tr>
        <td>Patient: </td>
        <td><%= StringEscapeUtils.escapeHtml(action.getPatientName(referralBean))%></td>
    </tr>
    <tr>
        <td>Receiving HCP: </td>
        <td><%= StringEscapeUtils.escapeHtml(action.getReceivingHCPName(referralBean)) %>
            (<%= StringEscapeUtils.escapeHtml(specialty) %>)
            <%= referralBean.isViewedByHCP() ? "<em>viewed</em>" : "<em>not yet viewed</em>" %>
        </td>
    </tr>
    <tr>
        <td>Time Generated: </td>
        <td><%= StringEscapeUtils.escapeHtml(referralBean.getTimeStamp()) %></td>
    </tr>
    <tr>
        <td>Office Visit Date: </td>
        <td><%= StringEscapeUtils.escapeHtml(action.getOfficeVisitDate(referralBean)) %></td>
    </tr>
    <tr>
        <td>Notes: </td>
        <td><textarea name="referralDetails" 
                      style="width: 40em; height: 5em; font-family: sans-serif;"
                      <%= disabledAttr %>
                      ><%= StringEscapeUtils.escapeHtml(referralDetails) %></textarea>
        </td>
    </tr>
    <tr>
        <td>Priority: </td>
        <td>
            <select name="priority" <%= disabledAttr %> >
                <option value="1" <%= "1".equals(newPriorityString) ? "selected=\"true\"" : "" %>>1</option>
                <option value="2" <%= "2".equals(newPriorityString) ? "selected=\"true\"" : "" %>>2</option>
                <option value="3" <%= "3".equals(newPriorityString) ? "selected=\"true\"" : "" %>>3</option>
            </select>
        </td>
    </tr>
	    
<% if (askConfirmDelete) { %>

        <tr>
            <td colspan="2" style="text-align: center; font-weight: bold; color: rgb(0,0,0); background-color: rgb(255,160,0);">
                Are you sure you want to delete this referral?
            </td>
        </tr>
        <tr>
            <td colspan="2" style="text-align: center;">
                <input type="hidden" id="referralID" name="referralID" value="<%= referralIDString %>">
                <input type="hidden" id="confirmDelete" name="confirmDelete" value="true">
                <input type="submit" id="submitCreate" name="submitDelete" value="Delete Referral">
                <input type="submit" id="submitCancel" name="submitCancel" value="Cancel">
            </td>
        </tr>
    
<% } else { %>
	
	    <tr>
	        <td colspan="2" style="text-align: center;">
		        <% if (!existingReferral) { %>
		            <input type="submit" id="submitCreate" name="submitCreate" value="Create Referral">
	                <input type="submit" id="submitCancel" name="submitCancel" value="Cancel">
		        <% } else if (existingReferral && !referralBean.isViewedByHCP()) { %>
		            <input type="hidden" id="referralID" name="referralID" value="<%= referralIDString %>">
	                <input type="submit" id="submitEdit" name="submitEdit" value="Save Changes">
	                <input type="submit" id="submitCancel" name="submitCancel" value="Cancel Changes">
	                <input type="submit" id="submitDelete" name="submitDelete" value="Delete Referral">
		        <% }  else { %>
                    <input type="hidden" id="referralID" name="referralID" value="<%= referralIDString %>">
	                <input type="submit" id="submitDelete" name="submitDelete" value="Delete Referral">
	                <input type="submit" id="submitCancel" name="submitCancel" value="Cancel Changes">
		        <% } %>
	        </td>
	    </tr>

<% }  %>
	
</table>

</form>

<br /><br /><br />
<itrust:patientNav />
<br />

<form></form>

<%@include file="/footer.jsp" %>
