<%@page import="java.net.URLEncoder" %>
<%@page import="java.net.URLDecoder" %>
<%@page import="java.util.List"%>

<%@page import="edu.ncsu.csc.itrust.beans.MedicationBean"%>
<%@page import="edu.ncsu.csc.itrust.beans.DrugInteractionBean"%>
<%@page import="edu.ncsu.csc.itrust.action.UpdateNDCodeListAction"%>
<%@page import="edu.ncsu.csc.itrust.action.DrugInteractionAction"%>
<%@page import="edu.ncsu.csc.itrust.exception.FormValidationException"%>
<%@page import="edu.ncsu.csc.itrust.exception.ITrustException"%>

<%@include file="/global.jsp" %>

<%
	pageTitle = "iTrust - Maintain ND Codes";
%>

<%@include file="/header.jsp" %>

<%
	UpdateNDCodeListAction ndUpdater = new UpdateNDCodeListAction(prodDAO, loggedInMID.longValue());
	DrugInteractionAction interactionAction = new DrugInteractionAction(prodDAO);
	
	String headerMessage = "Viewing Current ND Codes";
	String code1 = request.getParameter("code1") != null
	? request.getParameter("code1").trim()
	: "";
	String code2 = request.getParameter("code2") != null
	? request.getParameter("code2").trim()
	: "";
	String code = code1 + code2;
	
	if (request.getParameter("add") != null || request.getParameter("update") != null ||
	request.getParameter("delete") != null || request.getParameter("deleteND") != null ||
	request.getParameter("import") != null) {
		try {
	if(request.getParameter("add") != null || request.getParameter("update") != null) {
		MedicationBean med =
			new MedicationBean(code, request.getParameter("description"));
		headerMessage = (request.getParameter("add") != null)
				? ndUpdater.addNDCode(med)
				: ndUpdater.updateInformation(med);
		if(!headerMessage.contains("Error")) {
			if(request.getParameter("add") != null)
				loggingAction.logEvent(TransactionType.DRUG_CODE_ADD, loggedInMID, 0, code);
			else if(request.getParameter("update") != null)
				loggingAction.logEvent(TransactionType.DRUG_CODE_EDIT, loggedInMID, 0, code);
		}
	} else {
		if(request.getParameter("delete") != null) { //was codeToDelete
			interactionAction.deleteInteraction(code, request.getParameter("codeToDelete").trim());
			headerMessage = "Interaction deleted successfully";
			loggingAction.logEvent(TransactionType.DRUG_INTERACTION_DELETE, loggedInMID, 0, 
					"\nDrug: " + code + "\nDrug: " + request.getParameter("codeToDelete"));
		}
		else if (request.getParameter("deleteND") != null) {
			MedicationBean med =
					new MedicationBean(code, request.getParameter("description"));
			
			headerMessage = (request.getParameter("deleteND") != null)
					? ndUpdater.removeNDCode(med)
					: ndUpdater.updateInformation(med);
			if(!headerMessage.contains("Error")) {
				if(request.getParameter("deleteND") != null)
					loggingAction.logEvent(TransactionType.DRUG_CODE_REMOVE, loggedInMID, 0, code);
			}
		}
		else if (request.getParameter("import") != null) {
			response.sendRedirect("importNDCodes.jsp?forward=editNDCodes.jsp");
		}
		else {
			headerMessage = "Interaction does not exist";
		}
			
		
	}
		} catch(FormValidationException e) {
%>
			<div align=center>
				<span class="iTrustError"><%=StringEscapeUtils.escapeHtml(e.getMessage())%></span>
			</div>
<%
	headerMessage = "Validation Errors";
		} catch(ITrustException e) {
%>
			<div align=center>
				<span class="iTrustError"><%=StringEscapeUtils.escapeHtml(e.getMessage()) %></span>
			</div>
<%
			headerMessage = "Validation Errors";
		}
		
	} else {
		loggingAction.logEvent(TransactionType.DRUG_CODE_VIEW, loggedInMID, 0, "");
	}
			
	String headerColor = (headerMessage.indexOf("Error") > -1)
			? "#ffcccc"
			: "#00CCCC";
%>

<br />
<div align=center>
<form name="mainForm" method="post">
<input type="hidden" id="codeToDelete" name="codeToDelete" value="">
<input type="hidden" id="updateID" name="updateID" value="">
<input type="hidden" id="oldDescrip" name="oldDescrip" value="">
<script type="text/javascript">
	function fillUpdate(code) {
		document.getElementById("code1").value = code.substring(0,5);
		document.getElementById("code2").value = code.substring(5);
		document.getElementById("description").value
			= unescape(document.getElementById("UPD" + code).value);
		document.getElementById("oldDescrip").value
			= unescape(document.getElementById("UPD" + code).value);
		document.getElementById("interactions").innerHTML = "";
		document.getElementById("intDesc").innerHTML = "";
		drugs = document.getElementById("INTDRUG" + code).value.split("\n");
		descs = document.getElementById("INTDESC" + code).value.split("\n");
		for(d in drugs) {
			if(d == drugs.length - 1) break;			
			intLink = document.createElement("a");
			intLink.href = "javascript:void(0)";
			intLink.id = "drugInteraction" + d; 
			
			if(d == 0) {
				addIntDesc(drugs[d], descs[d]);
			}
			
			if(document.all) {
				intLink.attachEvent("onclick", addIntDescIE);
				//Strip newline off end of drug code
				drugs[d] = drugs[d].substring(0, drugs[d].length-1);
			} else {
				intLink.setAttribute("onclick", "addIntDesc('" + drugs[d] + "', '" + descs[d] + "');");
			}

			var intText = drugs[d];	
			if(document.getElementById("UPD" + intText).value != "" && document.getElementById("UPD" + intText).value != "undefined") {
				intText = intText + " " + unescape(document.getElementById("UPD" + drugs[d]).value);
			}
			intText = intText.substring(0,5) + "-" + intText.substring(5);
			
			intLinkText = document.createTextNode(intText);
			intLink.appendChild(intLinkText);
			document.getElementById("interactions").appendChild(intLink);
			document.getElementById("interactions").appendChild(document.createElement('br'));
			
		}
		
		document.getElementById("interactions").appendChild(document.createElement('br'));
		document.getElementById("interactions").appendChild(document.createElement('br'));
		
		if(drugs.length <= 1) {
			document.getElementById("interactions").innerHTML = "No Interactions";
			if(document.getElementById("delete").style != null) {
				document.getElementById("delete").style.visibility = "hidden";
				document.getElementById("intDesc").style.visibility = "hidden";
			}
		}
	}

	function addIntDescIE() {
		linkID = event.srcElement.id;
		linkID = linkID.substring(15); //Get number after "drugInteraction"
		linkNumber = eval(linkID);
		addIntDesc(drugs[linkNumber], descs[linkNumber]);
	}

	function addIntDesc(code, desc) {
		document.getElementById("intDesc").innerHTML = desc;
		document.getElementById("delete").style.visibility = "";
		document.getElementById("intDesc").style.visibility = "";
		document.getElementById("codeToDelete").value = code;
	}
</script>


<span class="iTrustMessage"><%= StringEscapeUtils.escapeHtml("" + (headerMessage )) %></span>

<br />
<br />
<table class="fTable"  align="left">
	<tr>
		<th colspan="2">Current Drug ND Codes</th>
	</tr>
	<tr class="subHeader">
		<th>Code</th>
		<th>Description</th>
	</tr>
	<%
		List<MedicationBean> medList = prodDAO.getNDCodesDAO().getAllNDCodes();
		List<DrugInteractionBean> interactionList;
		String tempCode = "";
		String tempDescrip = "";
		String escapedDescrip = "";
		String intDrugsString = "";
		String intDescsString = "";
		
		for (MedicationBean medEntry : medList) {
			tempCode = medEntry.getNDCode();
			tempDescrip = medEntry.getDescription();
			escapedDescrip = URLEncoder.encode(tempDescrip, "UTF-8").replaceAll("\\+", "%20");
			interactionList = interactionAction.getInteractions(tempCode);
			intDrugsString = "";
			intDescsString = "";
			for(DrugInteractionBean b : interactionList) {
				if(tempCode.equals(b.getFirstDrug())) intDrugsString += b.getSecondDrug() + "\n";
				if(tempCode.equals(b.getSecondDrug())) intDrugsString += b.getFirstDrug() + "\n";
				intDescsString += b.getDescription() + "\n";
			}
			//intDrugsString = URLEncoder.encode(intDrugsString, "UTF-8").replaceAll("\\+", "%20");
			//intDescsString = URLEncoder.encode(intDescsString, "UTF-8").replaceAll("\\+", "%20");
	%>
		<tr>
			<td><%=5 > tempCode.length() ? tempCode : tempCode.substring(0, 5)
				%>-<%=5 > tempCode.length() ? "" : tempCode.substring(5) %>
			</td>
			<td><a href="javascript:void(0)"
					onclick="fillUpdate('<%= StringEscapeUtils.escapeHtml("" + (tempCode )) %>')"
						><%= StringEscapeUtils.escapeHtml("" + (tempDescrip )) %></a>
				<input type="hidden"
						id="UPD<%= StringEscapeUtils.escapeHtml("" + (tempCode )) %>"
						name="UPD<%= StringEscapeUtils.escapeHtml("" + (tempCode )) %>"
						value="<%= StringEscapeUtils.escapeHtml("" + (escapedDescrip )) %>">
				<input type="hidden"
						id="INTDRUG<%= StringEscapeUtils.escapeHtml("" + (tempCode )) %>"
						name="INTDRUG<%= StringEscapeUtils.escapeHtml("" + (tempCode )) %>"
						value="<%= StringEscapeUtils.escapeHtml("" + (intDrugsString )) %>">
				<input type="hidden"
						id="INTDESC<%= StringEscapeUtils.escapeHtml("" + (tempCode )) %>"
						name="INTDESC<%= StringEscapeUtils.escapeHtml("" + (tempCode )) %>"
						value="<%= StringEscapeUtils.escapeHtml("" + (intDescsString )) %>">
			</td>
		</tr>
	<% } %>
</table>

<table class="fTable" align="center" width="500">
	<tr>
		<th colspan="3">Update or Add ND Code</th>
	</tr>
	<tr class="subHeader">
		<th>Code</th>
		<th>Description</th>
	</tr>
	<tr>
		<td style="padding-right: 10px;">
			<input  type="text"
					id="code1"
					name="code1"
					size="5"
					maxlength="5"
			/>-<input type="text"
					id="code2"
					name="code2"
					size="4"
					maxlength="4"/>
		</td>
		<td>
			<input  type="text"
					id="description"
					name="description"
					size="40"
					maxlength="50" />
		</td>
	</tr>
	<tr class="subHeader">
		<th>Interaction</th>
		<th>Description</th>
	</tr>
	<tr>
		<td style="padding-right: 10px;vertical-align:top" id="interactions">
			No Interactions
		</td>
		<td>
			<div id="intDesc" style="height:100px;width:200px;overflow-y:scroll;visibility:hidden"></div>
			
		</td>	
	</tr>
	<tr>
		<td>
			<input type="button" id="editInt" name="editInt" value="Add Interaction" onclick="location.href='editNDCInteractions.jsp'" />
		</td>
		<td>
			<input type="submit" value="Delete Interaction" name="delete" id="delete" style="visibility: hidden" />
		</td>
	</tr>
</table>
<br />
<div style="padding-left:60px;">
<input type="submit" name="add" value="Add Drug" />
<input type="submit" name="update" value="Update Drug" />
<input type="submit" name="deleteND" value="Delete Drug" />
<input type="submit" name="import" id="import" value="Import Drugs" />
</div>

</form>
<br />
<br />
</div>
<br />

<%@include file="/footer.jsp" %>
