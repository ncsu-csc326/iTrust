<%@page import="java.net.URLEncoder" %>
<%@page import="java.net.URLDecoder" %>
<%@page import="java.util.List"%>

<%@page import="edu.ncsu.csc.itrust.model.old.beans.OverrideReasonBean"%>
<%@page import="edu.ncsu.csc.itrust.action.UpdateReasonCodeListAction"%>
<%@page import="edu.ncsu.csc.itrust.exception.FormValidationException"%>
<%@page import="edu.ncsu.csc.itrust.exception.ITrustException"%>

<%@include file="/global.jsp" %>

<%
pageTitle = "iTrust - Maintain Override Reason Codes";
%>

<%@include file="/header.jsp" %>

<%
	UpdateReasonCodeListAction orcUpdater = new UpdateReasonCodeListAction(prodDAO, loggedInMID.longValue());
	
	String headerMessage = "Viewing Current Override Reason Codes";
	String code = request.getParameter("code") != null
			? request.getParameter("code").trim()
			: "";
	
	if (request.getParameter("add") != null || request.getParameter("update") != null || request.getParameter("delete") != null) {
		try {
			if(request.getParameter("add") != null || request.getParameter("update") != null) {
				OverrideReasonBean orc =
					new OverrideReasonBean(code, request.getParameter("description"));
				headerMessage = (request.getParameter("add") != null)
						? orcUpdater.addORCode(orc)
						: orcUpdater.updateInformation(orc);
				
				if(!headerMessage.contains("Error")) {
					if(request.getParameter("add") != null) {
						loggingAction.logEvent(TransactionType.OVERRIDE_CODE_ADD, loggedInMID, 0, request.getParameter("code"));
						
					}
					if(request.getParameter("update") != null) {
						loggingAction.logEvent(TransactionType.OVERRIDE_CODE_EDIT, loggedInMID, 0, request.getParameter("code"));
					}
				}
			}
		} catch(FormValidationException e) {
%>
			<div align=center>
				<span class="iTrustError"><%=StringEscapeUtils.escapeHtml(e.getMessage()) %></span>
			</div>
<%
			headerMessage = "Validation Errors";
		}
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
		document.getElementById("code").value = code.substring(0,5);
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
		if(drugs.length <= 1) {
			document.getElementById("interactions").innerHTML = "No Interactions";
			if(document.getElementById("delete").style != null) {
				document.getElementById("delete").style.visibility = "hidden";
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
		document.getElementById("codeToDelete").value = code;
	}
</script>


<span class="iTrustMessage"><%= StringEscapeUtils.escapeHtml("" + (headerMessage )) %></span>

<br />
<br />

<table class="fTable" align="center">
	<tr>
		<th colspan="3">Update Override Reaction Code List</th>
	</tr>
	<tr class="subHeader">
		<th>Code</th>
		<th>Description</th>
	</tr>
	<tr>
		<td style="padding-right: 10px;">
			<input  type="text"
					id="code"
					name="code"
					size="5"
					maxlength="5"
			/>
		</td>
		<td>
			<input  type="text"
					id="description"
					name="description"
					size="40"
					maxlength="50" />
		</td>
	</tr>
</table>
<br />
<input type="submit" name="add" value="Add Code" />
<input type="submit" name="update" value="Update Code" />

<br />
<br />

<table class="fTable" align="center">
	<tr>
		<th colspan="2">Current Override Reason Codes</th>
	</tr>
	<tr class="subHeader">
		<th>Code</th>
		<th>Description</th>
	</tr>
	<%
		List<OverrideReasonBean> orcList = prodDAO.getORCodesDAO().getAllORCodes();
		String tempCode = "";
		String tempDescrip = "";
		String escapedDescrip = "";
		String intDrugsString = "";
		String intDescsString = "";
		
		for (OverrideReasonBean orcEntry : orcList) {
			tempCode = orcEntry.getORCode();
			tempDescrip = orcEntry.getDescription();
			escapedDescrip = URLEncoder.encode(tempDescrip, "UTF-8").replaceAll("\\+", "%20");
	%>
		<tr>
			<td><%=5 > tempCode.length() ? tempCode : tempCode.substring(0, 5)
				%><%=5 > tempCode.length() ? "" : tempCode.substring(5) %>
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
</form>
<br />
<br />
</div>
<br />

<%@include file="/footer.jsp" %>
