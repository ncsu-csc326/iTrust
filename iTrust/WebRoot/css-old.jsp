<%@page contentType="text/css" %>

<%@page import="edu.ncsu.csc.itrust.action.ViewHelperAction"%>

<%@include file="./global.jsp" %>

<%
String primaryColor = "C7C7C7"; //prefsBean.getThemeColor(); //"A1BFFF"; //
String secondaryColor = "FFFFFF";

//Calculate the inverse of the secondary color
int r = 255 - Integer.parseInt(secondaryColor.substring(0,2), 16);
int g = 255 - Integer.parseInt(secondaryColor.substring(2,4), 16);
int b = 255 - Integer.parseInt(secondaryColor.substring(4,6), 16);
String rStr = Integer.toHexString(r);
String gStr = Integer.toHexString(r);
String bStr = Integer.toHexString(r);
rStr = rStr.length() == 1 ? "0" + rStr : rStr;
gStr = gStr.length() == 1 ? "0" + gStr : gStr;
bStr = bStr.length() == 1 ? "0" + bStr : bStr;
String secondaryColorInv = rStr + gStr + bStr;

String backgroundColor = "#FFFFFF"; //ViewHelperAction.calculateColor(primaryColor, secondaryColor, 0.75);
String linkColor = ViewHelperAction.calculateColor(primaryColor, secondaryColorInv, 0.5);
String borderColor = ViewHelperAction.calculateColor(primaryColor, secondaryColor, 0.5);
String calendarHeadingColor = ViewHelperAction.calculateColor(primaryColor, secondaryColor, 0.1);
String calendarBgColor = ViewHelperAction.calculateColor(primaryColor, secondaryColor, 0.875);
String calendarTodayColor = ViewHelperAction.calculateColor(primaryColor, secondaryColor, 0.425);
String formTitleColor = ViewHelperAction.calculateColor(primaryColor, secondaryColorInv, 0.42);
String formBackgroundColor = ViewHelperAction.calculateColor(primaryColor, secondaryColor, 0.9);
String formHeadingBgColor = ViewHelperAction.calculateColor(primaryColor, secondaryColor, 0.4);
String formHeadingBgColor2 = ViewHelperAction.calculateColor(primaryColor, secondaryColor, 0.6);
%>

/* General Content classes */
html {
	height: 100%;
}

body {
	margin: 0px;
	padding: 0px;
	background-color: <%= backgroundColor %>;
	font-size: 13px;
	color: #2C2C2C;
	line-height: 1.6em;
	font-family: "Helvetica Neue", "Helvetica", Arial, Geneva, sans-serif;
	height: 100%;
}

a {
	color: #<%= linkColor %>;
	font-size: 14px;
}

table{
	border-collapse: collapse;
}

input:focus {
	border: 2px solid #CB9949;
}

fieldset {
	background-color: #<%= backgroundColor %>;
	border: 1px solid #<%= borderColor %>;
}

legend {
	letter-spacing: 0.5pt;
	color: #<%= formTitleColor %>;
	font-size: 140%;
}

.zebraOn {
	padding:5px;
}

.zebraOff {
	background-color:  #<%= borderColor %>;
	padding:5px;
}

.contentBlock {
	font-family: verdana;
	font-size: 0.8em;
	padding-left: 10px;
}
.contentBlock a{
	font-family: verdana;
	font-size: 0.9em;
	/*padding-left: 10px;*/
	color: #<%= linkColor %>;
}

.contentBlock img.icon {
    padding-right: 10px;
}

.contentBlock ul {
    list-style-type: none;
    padding-left: 1em;
}

.contentBlock ul > li {
    
}

.subheading {
	font-family: verdana;
	font-size: 0.8em;
	font-weight: bold;
	padding-bottom: 10px;
	padding-left: 5px;
}

.menuItem {
	
	margin: 0em;
	width: 100%;
	/*border-bottom: 1px solid #fff;*/
}

.menuItem:hover{
	/*border-bottom: 1px solid #bd0000;*/
	background: none repeat scroll 0 0 #CFCFCF;
}

.menu_category .menuItem .highlighted{
	border: 0;
	width: 14px;
	visibility: hidden;
	padding: 0 0 0 4px;
	float: left;
}

.menu_category .menuItem:hover .highlighted{
	visibility: visible;
}

.menuItem:hover {
	/*background-color: #E5E5E5;*/
}

/* iTrust Header */
#title {
	font-size: 25pt;
	float: left;
	margin-right: 1em;
}

#iTrustHeader {
	width: 100%;
	height: 112px;
	padding: 0em;
	vertical-align: bottom;
	border-bottom: 4px solid #BD0000;
	position: relative;
}
#iTrustHeader2 {
	background-color: grey;
	height: 60px;
	border-bottom: 1px solid dimgray;
	position: fixed;
	top: 0;
	left: 0;
	z-index: 99;
	width: 100%;
}

/* Main navigation formatting */
#iTrustLogo {
	float: left;
    padding-top: 6px;
}

#iTrustUserInfo {
	float: right;
	text-align: right;
	
}

#iTrustUserInfo div{
	padding:10px;
}

#iTrustUserInfoInner {
	width: 200px;
	position: absolute;
	top: 0px;
	right: 0px;
	background: grey;
	float: right;
	text-align: right;
	vertical-align: bottom;
	margin-right: 20px;
	color: white;
}

#iTrustUserInfo div a{
	font-size: 12px;
	color:#BD0000;
	font-weight:bold;
}

#iTrustSelectedPatient {
	
	clear:both;
	text-align: right;
	background: #3E3E3E;
	position: fixed;
	right:0;
	bottom:0;
	z-index:99;
	/**border-top: 2px solid black;**/
}

.selectedPatient {
	color: #fff;
	margin: 0 16px;
}

#container{
	height: 100%;
}
#container2 {
	height: 100%;	
}
.selectedPatient a{
	color:#fff;
}

#iTrustNavLink {
	color: <%= linkColor %>;
	font-size: 11pt;
	text-decoration: none;
	vertical-align:bottom;
}

/* iTrust Menu */
#iTrustMenu {
	margin: 0em;
	float: left;
	position: static;
	width: 29%;
	/* margin: 65px 0 0 0; */
	height: 100%;
	/* top: 34px; */
	/* left: 0; */
	background-color: whitesmoke;
	padding: 0 10px;
		
}

#notificationArea:first-child{
	width: 25% !important;
}

#notificationArea{
	width: 74% !important;
}

#areaContainer{
	padding: 10px;
}

#notificationArea h2{
	background-color: #3E3E3E;
    border-bottom: 2px solid #BD0000;
    color:#fff;
    padding: 10px;
    margin-bottom: 0;
}

#notificationArea.rightArea{
	border:1px solid #dedede;
}

#notificationArea.rightArea h2{
	margin: 0;
}

#notificationArea ul{
	list-style: none;
	margin: 0 10px 0 0;
	padding: 0;
}

#iTrustMenu a {
}


.iTrustMenuContents {
}

#iTrustMain {
}



/* Content Page */

#iTrustPage {
}

#iTrustContent {
}

#iTrustFooter {
	position: fixed;
	bottom: 0;
}

.mainTable {
	width: 90%;
}


/* Formated table stuff */
.fTable {
	background-color: #<%= formBackgroundColor %>;
	border: 1px solid black;
	border-collapse: collapse;
}

.fTable tbody tr td{
    background: none repeat scroll 0 0 #EDEDED;
}

.fTable tbody tr:hover td{
    background: none repeat scroll 0 0 #CFCFCF;
}

.fTable tr {
	/*border: 1px solid black;*/
	 
}

.fTable tr th {
	/*border: 1px solid black;*/
	background-color: #3e3e3e;
	font-size: 16px;
	padding: 10px;
	white-space: nowrap;
	color: #fff;
	border-bottom: 2px solid #bd0000;
	text-align:left;
	font-weight: bold;
}

.fTable tr th a{
	color: #fff;
}

.fTable tr td {
	/*border: 1px solid black;*/
	font-size: 14px;
	padding: 10px;
}

.fTable .subHeader td {
	color: #<%= formTitleColor %>;
	background: #D6D6D6;
	font-weight: bold;
	text-align: center;
	white-space: nowrap;
}

.fTable .subHeader th {
    background: none repeat scroll 0 0 #888888;
    border-bottom: 1px solid #919191;
    color: #F7F7F7;
    font-size: 14px;
    font-weight: bold;
    text-align: left;
    white-space: nowrap;
}

.fTable .subHeaderVertical {
	background: #<%= formHeadingBgColor2%>;
	font-weight: bold;
	text-align: left;
	font-size: 14px;
}

.fTable .topLink {
    float: right;
}

.iTrustError {
	color: #FF0000;
	font-weight: bold;
}

.iTrustMessage {
	color: #00AA88;
	font-weight: bold;
}

.filterEdit {
	border: 2px solid #292929;
	padding: 10px;
	background-color: silver;	
}

/* Calendar Formatting */
#calendarTable {
	font-family:"Trebuchet MS", Arial, Helvetica, sans-serif;
	border-collapse:collapse;
}

#calendarTable td, #calendarTable th {
	font-size:1em;
	background-color: #<%= "000000"%>;
	border:1px solid #<%= calendarHeadingColor%>;
	padding:3px 7px 2px 7px;
}

#calendarTable tr th {
	font-family:"Trebuchet MS", Arial, Helvetica, sans-serif;
	background-color: #<%= calendarHeadingColor%>;
	color: #<%= secondaryColor%>;
}

#calendarTable tr td {
	height: 100px; 
	min-width: 120px;
	width: 150px;
	background-color: #<%= calendarBgColor%>;
}

#calendarTable tr td div.cell {
	height: 100px;
	min-width: 120px;
	width: 99%;
	vertical-align: top;
	overflow: auto;
}

#calendarTable tr td.today {
	background-color: #<%= calendarTodayColor%>;
}

#calendarTable .calendarEntry {
	padding: 5px 0px 0px 5px;
	font-size: 14px;
}

#calendarTable .conflict {
	font-weight: bold;
}

#calendarTable .calendarEntry a {
	font-size: 12px;
}
/* Transaction Page */

.transactionForm .right{
	text-align: right;
}

.transactionForm .left{
	text-align: left;
}

.transactionForm .center{
	text-align: center;
}

#transactionTable {
	margin-top: 5px;	
}

.transactionChart .yaxis {
	vertical-align: 100px;	
}

#fancyTable {
	font-family:"Trebuchet MS", Arial, Helvetica, sans-serif;
	width:99%;
	border-collapse:collapse;
}

.fancyTable td, .fancyTable th {
	font-size:1em;
	background-color: #<%= calendarBgColor%>;
	border:1px solid #<%= calendarHeadingColor%>;
	padding:3px 7px 2px 7px;
}

.fancyTable th {
	font-size:1.1em;
	text-align:left;
	padding-top:5px;
	padding-bottom:4px;
	background-color:#<%= calendarHeadingColor%>;
	color:#<%= secondaryColorInv%>;
}

.fancyTable tr.alt td {
	color:#<%= formTitleColor%>;
	background-color:#<%= calendarTodayColor%>;
}

/* Menu formatting */
.menu_category {
	padding-top: 1em;
	padding-bottom: 0.5em;
	padding-left: 1em; 
}

#loginMenu h2{
	background: none repeat scroll 0 0 #474747;
    border-bottom: 2px solid #BD0000;
    color: #FFFFFF;
    padding: 10px;
    margin: 0;
}

#sampleLoginsHead{
background: none repeat scroll 0 0 #474747;
    border-bottom: 2px solid #BD0000;
    color: #FFFFFF;
    padding: 4px 10px;
	margin: 0;
	font-size: 14px;
}

#sampleLoginTable{
	border: 1px solid #a0a0a0;
}

#sampleLoginTable td{
	padding: 2px 10px;
}

#sampleLoginTable td:hover{
	background: #e5e5e5;
}

#loginMenu form{
	padding: 10px;
	border: 1px solid #a0a0a0;
}
			
.menu_category table td:hover{
	
}
			
.menu_category a {
	font-size: 14px;
	color: black;
	text-decoration: none;
	display: table;
}

.menu_category a:hover {	
	text-decoration: underline;
}

.menu_category span {
	font-size: 16px;
	font-weight: bold;
	color: white;
	background: #3E3E3E;
    border-bottom-right-radius: 40px;
    border-top-right-radius: 10px;
    padding: 5px;
}


.menu_category span:first-child{
	border-bottom: 2px solid #BD0000;
	width: 100%;
	display: inline-table;
}

.filter_fieldset {
	float: left; 
	border: 0px;
	margin-left: 2em;
}

.clear_button {
	display: block;
	clear: both;
}

/* These links are only for testing purposes and are indicated such */
a.iTrustTestNavlink {
	color: black;
	text-decoration: none;
}

#UserSearch {
	padding:10px;
}
