
<%@page import="edu.ncsu.csc.itrust.action.EditPatientInstructionsAction"%>
<%@page import="edu.ncsu.csc.itrust.beans.PatientInstructionsBean"%>
<%@page import="java.text.SimpleDateFormat"%>

<% { %>

<%

String updateMessage = "";

String lastPatientInstructionsID = "";
String lastName = "";
String lastUrl = "";
String lastComment = "";

String errorMsg = "";

boolean editingExistingInstructions = false;
boolean cancelledEditingInstructions = false;

if (null != request.getParameter("cancelPatientInstructionsButton") &&
   !"".equals(request.getParameter("cancelPatientInstructionsButton"))) {
	cancelledEditingInstructions = true;
}

// The "editPatientInstructionsForm" form is submitted if the user has chosen 
// to edit or remove an entry.
if ("editPatientInstructionsForm".equals(submittedFormName)) {
	EditPatientInstructionsAction pinstructions = ovaction.patientInstructions();
    
    String editID = request.getParameter("editPatientInstructionsID");
    PatientInstructionsBean bean = new PatientInstructionsBean();
    bean.setId(Long.parseLong(editID));
    if ("remove".equals(request.getParameter("editPatientInstructionsAction"))) {
    	pinstructions.deletePatientInstructions(bean);
        ovaction.logOfficeVisitEvent(TransactionType.PATIENT_INSTRUCTIONS_DELETE);
        updateMessage = "Patient-Specific Instructions information successfully updated.";
    } else {
    	List<PatientInstructionsBean> beans = pinstructions.getPatientInstructions();
    	boolean found = false;
    	for (PatientInstructionsBean pibean: beans) {
    		if (pibean.getId() == bean.getId()) {
    			bean = pibean;
    			found = true;
    			break;
    		}
    	}
    	if (found) {
            lastPatientInstructionsID = Long.toString(bean.getId());
            lastName = bean.getName();
            lastUrl = bean.getUrl();
            lastComment = bean.getComment();
            editingExistingInstructions = true;
    	} else {
    		errorMsg = "Error while trying to edit patient specific instructions.";
    	}
    }
}

// The "patientInstructionsForm" form is submitted when the user has submitted 
// a new or a changed entry.  
if (!cancelledEditingInstructions && "patientInstructionsForm".equals(submittedFormName)) {
	EditPatientInstructionsAction pinstructions = ovaction.patientInstructions();
	
	if (null != request.getParameter("patientInstructionsID") && 
	   !("".equals(request.getParameter("patientInstructionsID")))) {
		editingExistingInstructions = true;
	}
    
	PatientInstructionsBean bean = new BeanBuilder<PatientInstructionsBean>().build(request.getParameterMap(), new PatientInstructionsBean());
	bean.setModified(new Date());
	bean.setVisitID(ovaction.getOvID());
    try {
    	pinstructions.validate(bean);
    	if (editingExistingInstructions==true) {
    		long id = Long.parseLong(request.getParameter("patientInstructionsID"));
    		bean.setId(id);
    		pinstructions.editPatientInstructions(bean);
            ovaction.logOfficeVisitEvent(TransactionType.PATIENT_INSTRUCTIONS_EDIT);
    		editingExistingInstructions = false; // now that it has been updated, clear the flag
    	} else {
    	    pinstructions.addPatientInstructions(bean);
            ovaction.logOfficeVisitEvent(TransactionType.PATIENT_INSTRUCTIONS_ADD);
    	}
	    //ovaction.logOfficeVisitEvent(TransactionType.OFFICE_VISIT_EDIT);
	    //ovaction.logOfficeVisitEvent(TransactionType.DIAGNOSIS_ADD);
	    updateMessage = "Patient-Specific Instructions information successfully updated.";
    } catch (FormValidationException e) {
        errorMsg = e.printHTMLasString();
        lastPatientInstructionsID = request.getParameter("patientInstructionsID");
        lastName = request.getParameter("name");
        lastUrl = request.getParameter("url");
        lastComment = request.getParameter("comment");
    }
}
%>
<script type="text/javascript">
    function removePatientInstructionsID(value) {
        document.getElementById("editPatientInstructionsID").value = value;
        document.getElementById("editPatientInstructionsAction").value = "remove";
        document.forms["editPatientInstructionsForm"].submit();
    }
    function editPatientInstructionsID(value) {
        document.getElementById("editPatientInstructionsID").value = value;
        document.getElementById("editPatientInstructionsAction").value = "edit";
        document.forms["editPatientInstructionsForm"].submit();
    }
</script>


<a name="patientInstructionsAnchor"></a>
<div align=center>
<%
if (!"".equals(updateMessage)) {
    %>  <span class="iTrustMessage"><%= updateMessage %></span>  <%
}

%>

<form action="editOfficeVisit.jsp#patientInstructionsAnchor" method="post" id="editPatientInstructionsForm">
<input type="hidden" name="formName" value="editPatientInstructionsForm" />
<input type="hidden" id="editPatientInstructionsID" name="editPatientInstructionsID" value="" />
<input type="hidden" id="editPatientInstructionsAction" name="editPatientInstructionsAction" value="" />
<input type="hidden" name="ovID" value="<%= StringEscapeUtils.escapeHtml("" + (ovaction.getOvID())) %>" />
</form>

<% if (!"".equals(errorMsg)) { %>
    <div style="background-color:yellow;color:black" align="center">
        <%= errorMsg %>
    </div>
<% } %>

<form action="editOfficeVisit.jsp#patientInstructionsAnchor" method="post" id="patientInstructionsForm">

<input type="hidden" name="formIsFilled" value="true" />
<input type="hidden" name="formName" value="patientInstructionsForm" />
<input type="hidden" name="ovID" value="<%= StringEscapeUtils.escapeHtml("" + (ovaction.getOvID())) %>" />
<% if (editingExistingInstructions==true) { %>
    <input type="hidden" name="patientInstructionsID" value="<%= lastPatientInstructionsID %>" />
<% } %>


<table class="fTable" align="center" id="patientInstructionsTable">
    <tr>
        <th colspan="4"><a href="#" class="topLink">[Top]</a>Patient Specific Instructions</th>
    </tr>
    <tr class="subHeader">
        <td>Name</td>
        <td>Comments</td>
        <td>Modified</td>
        <td style="width: 60px;">Action</td>
    </tr>

    <%if(ovaction.patientInstructions().getPatientInstructions().size()==0) { %>
    <tr>
        <td  colspan="4" style="text-align: center;">No Patient Instructions on record</td>
    </tr>
    <%} else {
    	    SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
            SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm a");
            for(PatientInstructionsBean d : ovaction.patientInstructions().getPatientInstructions()) { %>
    <tr>
        <td style="min-width: 10em;">
            <a href="<%= StringEscapeUtils.escapeHtml("" + (d.getUrl())) %>">
            <%= StringEscapeUtils.escapeHtml("" + (d.getName())) %>
            </a>
        </td>
        <td style="max-width: 60%; ">
            <%= StringEscapeUtils.escapeHtml("" + (d.getComment())) %>
        </td>
        <td style="min-width: 5em; text-align: center;">
            <%= StringEscapeUtils.escapeHtml("" + (dateFormat.format(d.getModified()))) %> <br>
            <%= StringEscapeUtils.escapeHtml("" + (timeFormat.format(d.getModified()))) %>
        </td>
        <td style="min-width: 4em; text-align: center;">
            <a href="javascript:editPatientInstructionsID('<%= StringEscapeUtils.escapeHtml("" + (d.getId())) %>');">Edit</a>
            <br>
            <a href="javascript:removePatientInstructionsID('<%= StringEscapeUtils.escapeHtml("" + (d.getId())) %>');">Remove</a>
        </td>
    </tr>
    <%      } // end of for-loop
    } // end of if..else statements %>
    <tr>
        <th colspan="4" style="text-align: center;">New</th>
    </tr>
    <tr>
        <td vertical-align: top;>Name:</td>
        <td colspan="3">
            <input type="text" name="name" id="name" 
                   style="width: 40em; min-width: 30em;" 
                   value="<%= StringEscapeUtils.escapeHtml(lastName) %>"
                   <%= disableSubformsString %> >
        </td>
    </tr>
        <td vertical-align: top;>URL:</td>
        <td colspan="3">
            <input type="text" name="url" id="url" 
                   style="width: 40em; min-width: 30em;" 
                   value="<%= StringEscapeUtils.escapeHtml(lastUrl) %>"
                   <%= disableSubformsString %> >
        </td>
    <tr>
        <td vertical-align: top;>Comments:</td>
        <td colspan="3" style="">
            <textarea name="comment" id="comment" 
                      style="width: 40em; height: 5em; min-width: 30em; font-family: sans-serif;"
                      <%= disableSubformsString %> 
                      ><%= StringEscapeUtils.escapeHtml(lastComment) %></textarea>
        </td>
    </tr>
    <tr>
        <td colspan="4" style="text-align: center;">
            <% if (editingExistingInstructions == true) { %>
                <input type="submit" name="updatePatientInstructionsButton" 
                       id="updatePatientInstructionsButton"
                       value="Edit Patient Instructions" 
                       <%= disableSubformsString %> >
                <input type="submit" name="cancelPatientInstructionsButton" 
                       value="Cancel" 
                       <%= disableSubformsString %> >
            <% } else { %>
                <input type="submit" id="addPatientInstructionsButton" 
                       value="Add Patient Instructions" 
                       <%= disableSubformsString %> >
            <% } %>
        </td>
    </tr>
</table>

</form>

</div>


<% } %>

