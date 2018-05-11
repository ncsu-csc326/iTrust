
<%@page import="edu.ncsu.csc.itrust.action.EditLabProceduresAction"%>
<%@page import="edu.ncsu.csc.itrust.beans.LabProcedureBean"%>
<%@page import="edu.ncsu.csc.itrust.dao.mysql.PersonnelDAO"%>
<%@page import="edu.ncsu.csc.itrust.exception.ErrorList"%>
<%@page import="edu.ncsu.csc.itrust.exception.FormValidationException"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.HashMap"%>

<% { %>

<%

String updateMessage = "";

boolean labProcedureError = false;
String labProcedureErrorMessage = "";
String fieldErrorMessage = "";

if ("true".equals(request.getParameter("labProcDeleted"))) {
	updateMessage = "Lab Procedure information successfully updated.";
}

if ("labProcedureForm".equals(submittedFormName)) {


	try {
		EditLabProceduresAction labProcedures = ovaction.labProcedures();
	    Map<String, String[]> map = request.getParameterMap();
	    HashMap<String, String[]> hashmap = new HashMap(map); 
	    hashmap.remove("ovID");
		LabProcedureBean bean = new BeanBuilder<LabProcedureBean>().build(hashmap, new LabProcedureBean());
		
		// Assign lab tech id, or a default if none given.
		String labtech = request.getParameter("labTech");
		
		//Previously, if no lab tech was selected, one would be selected automatically (see commented out line)
		//This is contrary to the requirements document, so if no lab tech has been selected we throw an exception
		// TODO: throw a similar exception when there is no LOINC
		// TODO: move all of this error handling to LabProcedureValidator.java
		if (labtech==null || labtech.equals("")) {
			ErrorList errorList = new ErrorList();
			errorList.addIfNotNull("A lab tech must be selected before adding a laboratory procedure.");
			throw new FormValidationException(errorList);
		} else {
			bean.setLTID(Long.parseLong(labtech));
		}
		
		// Assign priority, or default priority.
		int priority = 3;
		try {
			priority = Integer.parseInt(request.getParameter("labProcPriority"));
		} catch (NumberFormatException e) {
		}
		bean.setPriorityCode(priority);
		
	    bean.setOvID(ovaction.getOvID());
	    bean.setPid(ovaction.getPid());
	    labProcedures.addLabProcedure(bean);
    	ovaction.logOfficeVisitEvent(TransactionType.OFFICE_VISIT_EDIT);
    	ovaction.logOfficeVisitEvent(TransactionType.LAB_PROCEDURE_ADD);
	    updateMessage = "Lab Procedure information successfully updated.";

	} catch(FormValidationException e)
	{
        fieldErrorMessage = e.printHTMLasString();
        labProcedureError = true;
	}
}
if (!"".equals(updateMessage)) {
    %>  <div align="center"><span class="iTrustMessage"><%= updateMessage %></span></div>  <%
}

%>

<script type="text/javascript">
    function removeLabProcID(value) {
        document.getElementById("removeLabProcID").value = value;
        document.forms["removeLabProcedureForm"].submit();
    }
    function editLabProcCommentary(procID) {
        document.getElementById("labProcID").value = procID;
        document.forms["editLabProcCommentary"].submit();
    }
    function reassignLabProc(procID) {
        document.getElementById("labProcID_Reassign").value = procID;
        document.forms["editLabProcReassign"].submit();
    }
</script>

<div align=center>

<form action="editOVLabProcedureConfirmDelete.jsp" method="post" id="removeLabProcedureForm">
<input type="hidden" name="formName" value="removeLabProcedureForm" />
<input type="hidden" id="removeLabProcID" name="labProcID" value="" />
<input type="hidden" name="ovID" value="<%= StringEscapeUtils.escapeHtml("" + (ovaction.getOvID())) %>" />
</form>

<form action="editOVLabProcedureCommentary.jsp" method="post" id="editLabProcCommentary">
<input type="hidden" name="labProcID" id="labProcID" value="" />
<input type="hidden" name="ovID" value="<%= StringEscapeUtils.escapeHtml("" + (ovaction.getOvID())) %>" />
</form>

<form action="editOVLabProcedureReassign.jsp" method="post" id="editLabProcReassign">
<input type="hidden" name="labProcID" id="labProcID_Reassign" value="" />
<input type="hidden" name="ovID" value="<%= StringEscapeUtils.escapeHtml("" + (ovaction.getOvID())) %>" />
</form>

<form action="editOfficeVisit.jsp" method="post" id="labProcedureForm">

<input type="hidden" name="formIsFilled" value="true" />
<input type="hidden" name="formName" value="labProcedureForm" />
<input type="hidden" name="ovID" value="<%= StringEscapeUtils.escapeHtml("" + (ovaction.getOvID())) %>" />

<% if (!"".equals(fieldErrorMessage)) { %>
    <div style="background-color:yellow;color:black" align="center">
        <%= fieldErrorMessage %>
    </div>
<% } %>

<table class="fTable" align="center" id="labProceduresTable">
    <tr>
        <th colspan="11"><a href="#" class="topLink">[Top]</a>Laboratory Procedures</th>
    </tr>
    <tr class="subHeader">
        <td>LOINC<br/>Code</td>
        <td>Lab Tech</td>
        <td>Status</td>
        <td>Rights</td>
        <td>Priority</td>
        <td>Commentary</td>
        <td>Numerical<br/>Results</td>
        <td colspan="2">Confidence<br/>Interval</td>
        <td style="width: 60px;">Updated Date</td>
        <td>Action</td>
    </tr>
    <%if(ovaction.labProcedures().getLabProcedures().size()==0){ %>
    <tr>
        <td colspan="11" style="text-align: center;">No Laboratory Procedures on record</td>
    </tr>
    <%} else { %>
    <%for(LabProcedureBean labproc : ovaction.labProcedures().getLabProcedures()){ %>
    <tr>
        <td align=center><%= StringEscapeUtils.escapeHtml("" + (labproc.getLoinc())) %></td>
        <td align=center><%= StringEscapeUtils.escapeHtml("" + (ovaction.labProcedures().getLabTechName(labproc.getLTID()))) %></td>
        <td align=center><%= StringEscapeUtils.escapeHtml("" + (labproc.getStatus())) %></td>
        <td align=center style="font-size: 0.7em;"><%= StringEscapeUtils.escapeHtml("" + (labproc.getRights())) %></td>
        <td align=center><%= StringEscapeUtils.escapeHtml("" + (labproc.getPriorityCode())) %></td>
        <td align=center><%= StringEscapeUtils.escapeHtml("" + (labproc.getCommentary())) %></td>
        <td align=center><%= StringEscapeUtils.escapeHtml("" + (labproc.getNumericalResult())) %></td>
        <td align=center><%= StringEscapeUtils.escapeHtml("" + (labproc.getLowerBound())) %></td>
        <td align=center><%= StringEscapeUtils.escapeHtml("" + (labproc.getUpperBound())) %></td>
        <td align=center><%= StringEscapeUtils.escapeHtml("" + (labproc.getTimestamp())) %></td>
        <td >
            <% if (labproc.getStatus().equals(LabProcedureBean.In_Transit) || 
            	   labproc.getStatus().equals(LabProcedureBean.Received)) { %>
                <a href="javascript:removeLabProcID('<%= StringEscapeUtils.escapeHtml("" + (labproc.getProcedureID())) %>');">Remove</a>
                <br/>
                <a href="javascript:reassignLabProc('<%= StringEscapeUtils.escapeHtml("" + (labproc.getProcedureID())) %>');">Reassign</a>
            <% } %>
            <% if (labproc.getStatus().equals(LabProcedureBean.Pending) || 
            		(labproc.getStatus().equals(LabProcedureBean.Completed) && !labproc.isViewedByPatient())
            		) { %>
                <a
                href="javascript:editLabProcCommentary('<%= StringEscapeUtils.escapeHtml("" + (labproc.getProcedureID())) %>');">Edit&nbsp;Commentary</a>
            <% } %>
        </td>
    </tr>
    <%} %>
    <%} %>
    <tr>
        <th colspan="11" style="text-align: center;">New</th>
    </tr>
    <tr style="border-bottom: none;">
        <td style="border-bottom: none; border-right: none; text-align: right;">Procedure:</td>
        <td colspan="10" style="border-bottom: none; border-left: none;">
            <select name="loinc" <%= disableSubformsString %> >
                <option value="">-- Please Select a Procedure --</option>
                    <% for(LOINCbean loinc : ovaction.labProcedures().getLabProcedureCodes()) { %>
                <option value="<%=loinc.getLabProcedureCode()%>"> <%= StringEscapeUtils.escapeHtml("" + (loinc.getLabProcedureCode())) %>
                    - <%= StringEscapeUtils.escapeHtml("" + (loinc.getComponent())) %> - <%= StringEscapeUtils.escapeHtml("" + (loinc.getKindOfProperty())) %> - <%= StringEscapeUtils.escapeHtml("" + (loinc.getTimeAspect())) %>
                    - <%= StringEscapeUtils.escapeHtml("" + (loinc.getSystem())) %> - <%= StringEscapeUtils.escapeHtml("" + (loinc.getScaleType())) %> 
                    - <%= StringEscapeUtils.escapeHtml("" + (loinc.getMethodType())) %></option>
                    <% } %>
            </select>
        </td>
    </tr>
    <tr style="border-bottom: none; border-top: none;">
        <td colspan="1" style="border-bottom: none; border-top: none; border-right: none; text-align: right;">
            Lab Technician:
        </td>
        <td colspan="3" style="border: none;">
            <select name="labTech" <%= disableSubformsString %> >
                <option value="">-- Please Assign to a Lab Tech--</option>
                
                <%  for (PersonnelBean pb : ovaction.labProcedures().getLabTechs()) { 
                    	 int[] sizes = ovaction.labProcedures().getLabTechQueueSizeByPriority(pb.getMID());%>
                    <option value="<%= pb.getMID() %>" >
                        <%= StringEscapeUtils.escapeHtml("" + (pb.getFullName())) %>
                        -- <%= StringEscapeUtils.escapeHtml("" + (pb.getSpecialty())) %>
                        -- <%for (int i=1; i<=3; i++) { out.print(i +": " + sizes[i] + "; "); } %>
                    </option>
                <% } %>
            </select>
        </td>
        <td colspan="1" style="border: none; text-align: right;">
            Priority:
        </td>
        <td colspan="6" style="border-bottom: none; border-top: none; border-left: none">
            <select name="labProcPriority">
                <option value="1">1</option>
                <option value="2">2</option>
                <option value="3" selected="true">3</option>
            </select>
        </td>
    </tr>
    <tr>
        <td colspan="11" align="center" style="border-top: none;">
            <input  type="submit" id="add_labProcedure" name="addLP" value="Add Lab Procedure" <%= disableSubformsString %> >
        </td>
    </tr>
</table>


</form>

</div>


<% } %>
