<%@taglib uri="/WEB-INF/tags.tld" prefix="itrust"%>
<%@page errorPage="/auth/exceptionHandler.jsp"%>

<%@page import="java.util.List"%>
<%@page import="edu.ncsu.csc.itrust.action.RequestRecordsReleaseAction"%>
<%@page import="edu.ncsu.csc.itrust.beans.HospitalBean"%>
<%@page import="edu.ncsu.csc.itrust.beans.RecordsReleaseBean"%>


<%@include file="/global.jsp" %>

<%
pageTitle = "iTrust - Records Release Request";

//Require a Patient ID first
String pidString =  loggedInMID.toString();
String loggedInName = authDAO.getUserName(loggedInMID.longValue());

String isRepresentee = request.getParameter("isRepresentee");
String currentMID = request.getParameter("currentMID");

RequestRecordsReleaseAction releaseAction = new RequestRecordsReleaseAction(prodDAO, Long.parseLong(currentMID));
List<HospitalBean> hospitalList = releaseAction.getAllPatientHospitals();

session.setAttribute("loggedInName", loggedInName);
session.setAttribute("releaseAction", releaseAction);
session.setAttribute("loggingAction", loggingAction);
session.setAttribute("patMID", pidString);

String firstName = request.getParameter("recFirstName");
if(firstName == null)
	firstName = "";

String lastName = request.getParameter("recLastName");
if(lastName == null)
	lastName = "";

String phone = request.getParameter("recPhone");
if(phone == null)
	phone = "";

String email = request.getParameter("recEmail");
if(email == null)
	email = "";

String hosName = request.getParameter("recHospitalName");
if(hosName == null)
	hosName = "";

String hosAddress1 = request.getParameter("recHospitalAddress1");
if(hosAddress1 == null)
	hosAddress1 = "";

String hosAddress2 = request.getParameter("recHospitalAddress2");
if(hosAddress2 == null)
	hosAddress2 = "";

String hosZip = request.getParameter("recHospitalZip");
if(hosZip == null)
	hosZip = "";

String hosState = request.getParameter("recHospitalState");
if(hosState == null)
	hosState = "";

String hosCity = request.getParameter("recHospitalCity");
if(hosCity == null)
	hosCity = "";



String releaseJustification = request.getParameter("releaseJustification");
if(releaseJustification == null)
	releaseJustification = "";

String releaseHos = request.getParameter("releaseHospital");
if(releaseHos == null)
	releaseHos = "";

String errorMsg = (String)request.getAttribute("failure");
%>

<%@include file="/header.jsp" %>

<h2>Medical records release form:</h2>

<hr align="left" width="50%"><br />

<%
if(errorMsg != null){ %>
<span style="color: red; font-size: 12pt"><b><i><%=errorMsg%></i></b></span><br /><br />
<%}%>

    <%if(isRepresentee.equals("false")){ %>
    	<i>Complete the form below to authorize the release of personal medical health records.</i><br /><br />    
    <%} 
    else if(isRepresentee.equals("true")){%>
    	<i>Complete the form below to authorize the release of <%=releaseAction.getPatientName()%>'s medical health records.</i><br /><br />
    <%}%>

 <form action="RecordsReleaseServlet" method="post" id="mainForm">
 <b>Release hospital: </b><select autofocus required name="releaseHospital">
 	<option disabled value="">Select release hospital</option>
 	<%
 	for(int i = 0; i < hospitalList.size(); i++){
 	%>
 		<option value=<%=hospitalList.get(i).getHospitalID()%> <%=releaseHos.equals(hospitalList.get(i).getHospitalID()) ? "selected" : ""%>><%=hospitalList.get(i).getHospitalName()%></option>
 	<% 		
 	}
 	%>

</select> 
    <P>
    <b>Recipient doctor information:</b> <br />
    <DL>
    <DD><LABEL for="recFirstName">First name: </LABEL>
              <INPUT required type="text" id="recFirstName" name="recFirstName" value=<%= firstName.replace("\'", "&#39;") %>><BR></DD>
    <DD><LABEL for="recLastName">Last name: </LABEL>
              <INPUT required type="text" id="recLastName" name="recLastName" value=<%= lastName.replace("\'", "&#39;") %>><BR></DD>
    <DD><LABEL for="recPhone">Phone number: </LABEL>
              <INPUT required type="text" id="recPhone" name="recPhone" value=<%= phone.replace("\'", "&#39;") %>><BR></DD>
    <DD><LABEL for="recEmail">Email address: </LABEL>
              <INPUT required type="text" id="recEmail" name="recEmail" value=<%= email.replace("\'", "&#39;") %>><BR></DD>
    </DL>             
    <b>Recipient hospital information:</b> <br />
    <DL>
    <DD><LABEL for="recHospitalName">Hospital: </LABEL>
              <INPUT required type="text" id="recHospitalName" name="recHospitalName" value=<%=hosName.replace("\'", "&#39;")%>><BR></DD>
    <DD><LABEL for="recHospitalAddress">Hospital address1: </LABEL>
              <INPUT required type="text" id="recHospitalAddress1" name="recHospitalAddress1" value=<%=hosAddress1.replace("\'", "&#39;")%>><BR></DD>
    <DD><LABEL for="recHospitalAddress">Hospital address2: </LABEL>
              <INPUT required type="text" id="recHospitalAddress2" name="recHospitalAddress2" value=<%=hosAddress2.replace("\'", "&#39;")%>><BR></DD>
    <DD><LABEL for="recHospitalCity">Hospital city: </LABEL>
              <INPUT required type="text" id="recHospitalCity" name="recHospitalCity" value=<%=hosCity.replace("\'", "&#39;")%>><BR></DD>
    <DD><LABEL for="recHospitalState">Hospital state: </LABEL>
              <INPUT required type="text" id="recHospitalState" name="recHospitalState" value=<%=hosState.replace("\'", "&#39;")%>><BR></DD>
    <DD><LABEL for="recHospitalZip">Hospital zip: </LABEL>
              <INPUT required type="text" id="recHospitalZip" name="recHospitalZip" value=<%=hosZip.replace("\'", "&#39;")%>><BR></DD>
    </DL>
    <LABEL for="releaseJustification">Request justification:</LABEL><br />
    		  <textarea id="releaseJustification" name="releaseJustification" cols="40" rows="5"><%=releaseJustification.replace("\'", "&#39;")%></textarea>

    <br /><br />
    
   <b>Please check and sign your name below.</b> <br />
   
   <%if(isRepresentee.equals("false")){ %>     
   		<i>"I hereby authorize the release of my confidential health records to the doctor and hospital indicated above. I understand and assume all legal responsibility for the release of these records."</i> <br /><br />
   <%}
   else if(isRepresentee.equals("true")){%>
   		<i>"I hereby authorize the release of my dependent's confidential health records to the doctor and hospital indicated above. I understand and assume all legal responsibility for the release of my dependent's records."</i><br /><br />
   <%}%>    
    <INPUT required type="checkbox" id="verifyForm" name="verifyForm" value="true"><i>I agree</i><br />
    <%if(isRepresentee.equals("false")){ %>
    	Signature: <INPUT required type="text" id="digitalSig" name="digitalSig"><br /><br />    
    <%} 
    else if(isRepresentee.equals("true")){%>
    	Representative signature: <INPUT required type="text" id="digitalSig" name="digitalSig"><br /><br />
    <%}%>
	<input type="hidden" value=<%=currentMID%> id="currentMID" name="currentMID"></input>
	<input type="hidden" value=<%=isRepresentee%> id="isRepresentee" name="isRepresentee"></input>
	<input type="hidden" value=<%=loggedInName%> id="loggedInName" name="loggedInName"></input>
    <INPUT type="submit" value="Submit" id="submit"> <INPUT type="reset">
    </P>
 </form>
 
<script type="text/javascript"> 
 function InitList(selectList)
{
  // if the list is already populated, nothing left to do
  if (selectList.optons.length > 1) {
    return;
  }

  selectList.optons.length = 0;
  var currentSelection = selectList.value;

  // sample code for population a list of 10 elements
  for (var i = 0; i < 10; i++) {
      selectList.options[i] = new Option(i, i, false, i == currentSelection);
  }
}
 </script>
 



<%@include file="/footer.jsp"%>