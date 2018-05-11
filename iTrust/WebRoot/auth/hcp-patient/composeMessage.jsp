
<%@page import="edu.ncsu.csc.itrust.beans.HCPVisitBean"%>
<% List<HCPVisitBean> hcplist = vHcpAction.getVisitedHCPs(); %>
<h4>CC </h4>
<table id="hcp_table" class="fTable" style="text-align: center;">			
<%	
int i = 0; 
for (HCPVisitBean vb: hcplist) {
	if ( vb.getHCPMID() != ignoreMID) {
%>
		<tr>
			<td><%= StringEscapeUtils.escapeHtml("" + (vb.getHCPName())) %></td>
			<td>
				<input name="cc" value="<%= StringEscapeUtils.escapeHtml("" + (vb.getHCPMID())) %>" 
						type="checkbox" <%= StringEscapeUtils.escapeHtml("") %> />
			</td>
		</tr> 
<%
	i++;
	}
}
%>
</table>
<!-- end cc checkbox -->

<br />

<p><b>Subject:</b> <input width="50%" class="form-control"   type="text" name="subject" value="<%= StringEscapeUtils.escapeHtml(subject) %>"/></p>
<br>
<br>
<p><b>Message:</b> </p>
<textarea class="form-control" rows="3" name="messageBody"></textarea><br />
<br />
<input class="btn btn-default" type="submit" value="Send" name="sendMessage"/>


