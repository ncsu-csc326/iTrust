<%@page contentType="text/css" %>

<%@page import="edu.ncsu.csc.itrust.action.ViewHelperAction"%>

<%@include file="./global.jsp" %>

html {
	height: 100%;
}
body {
	height: 100%
}
label {
	font-size: 13px;
	font-weight: normal;
}
.container-fluid {
	height: 100%;
}
#iTrustPage {
	margin-bottom: 20px;
}
#iTrustSelectedPatient {
	position: fixed;
	bottom: 5px;
	right: 5px;
	z-index: 101;
	padding: 10px;
	background: #D6D6D6;
	border-radius: 8px;
	border: 1px solid #C0C0C0;
	margin-left: 5px;
	transition: opacity .25s ease-in-out;
	-moz-transition: opacity .25s ease-in-out;
	-webkit-transition: opacity .25s ease-in-out
}
#iTrustSelectedPatient:hover {
	opacity: .5;
	transition: opacity .25s ease-in-out;
	-moz-transition: opacity .25s ease-in-out;
	-webkit-transition: opacity .25s ease-in-out;
}
#iTrustFooter {
	position: fixed;
	bottom: 0;
	background-color: rgba(247, 247, 247, 0.59);
	font-size: .7em;
	padding: 5px;
}

#iTrustMenu {
	background-color:rgba(247, 247, 247, 0.59);
	padding:15px;
}
#sampleLoginTable td {
	padding: 3px;
}
.nav span {
	padding-left: 20px;
	font-size: 1.3em;
	font-weight: bold;
	color: rgb(92, 92, 92)
}
.welcome {
	color: rgb(255, 255, 255) !important;
	float: right;
	padding: 12px;
}
.fTable {

}
.fTable tbody {
	background-color: #f1f1f1;
	border: 1px solid #D5D5D5;
	border-radius: 8px;
}
.fTable tr {

}
.fTable th {
	text-align: left;
	background-color: rgb(75, 75, 75);
	color: white;
}
.fTable tbody tr:nth-child(odd) {
   background-color: #E7E7E7;
}
.navbar-brand img {
	max-height: 26px;
	margin-top: -3px;
	margin-left: 6px;
}
.top-border {
	border-top: solid 3px #cc0000 !important;
}

.panel-notification {
	min-height: 230px;
}
.panel-notification ul {
	list-style: none;
	padding: 0px;
}
.panel-notification ul li {
	margin-bottom: 5px;
}
.subHeader {
	font-weight: bold;
}
.userpic {
	max-width: 70px;
	border-radius: 10px;
	border: 1px solid grey;
	float: left;
	margin-right: 16px;
}
.home-row {
	background: url('/iTrust/image/doc1.jpg') no-repeat;
	background-size: cover;
	height: 100%;
}
.panel-heading {
   cursor: pointer;
}

.panel-heading a:hover{
   text-decoration:none !important;
}
.iTrustMenuContents .panel {
	margin-bottom: 5px;
}
#home-content {
	opacity:0;
	background-color: rgba(255, 255, 255, 0.72);
	max-width: 700px;
	margin: auto;
	border-radius: 12px;
	border: 1px solid #C2C2C2;
	padding: 20px;
}
#home-content:after {
 	content: "";
	display: block;
	position: relative;
	bottom: -35px;
	right: -28px;
	width: 0;
	border-width: 15px 15px 0;
	border-style: solid;
	border-color: rgba(255, 255, 255, 0.67) transparent;
}
#home-content h1 {
	margin-top: 17px;
	margin-bottom: -17px;
	text-shadow: 1px 1px white;
	color: grey;
	font-style: italic;
	font-size: 1.2em;
	text-transform: uppercase;
	font-weight: bold;
	text-align: right;
}
#home-content blockquote {
	border-left: 5px solid #C9C9C9;
}
.jenkins-quote {
	font-size: 2em;
	line-height: 1em;
	color: #3A3A3A;
}
.iTrustError {
	color: #cc0000;
}
.patient-navigation {
	padding: 7px;
	border-bottom: 1px solid #DFDFDF;
	line-height:2.5em;
}
.patient-navigation a {
	padding: 8px;
}
.patient-nav-selected {
	padding: 13px;
}
.swipedMenu{
  display:block !important;
  -webkit-transform: translateX(0%) !important;
  -ms-transform: translateX(0%) !important;
  -moz-transform: translateX(0%) !important;
}
.swipedContent{
  position:fixed;
}
.sub-chevron{
  float:right;
  font-size:8pt;
}
.grey-border-container {
	padding: 10px 0 0 19px;
	border: 1px solid #DFDFDF;
	border-radius: 8px;
	margin-bottom: 10px;
	background-color: #FAFAFA;
}
#calendarTable th {
	background-color:#CCCCCC;
}
#calendarTable td {
	border: 1px solid rgb(155, 155, 155);
}

@media (max-width: 600px) {
  #iTrustFooter {
    display: none;
  }
}

