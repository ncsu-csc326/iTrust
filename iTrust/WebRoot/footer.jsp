				</div>
		</div>

	
		<div id="iTrustFooter" class="col-sm-8 col-sm-offset-4 col-md-9 col-md-offset-3">
			<div style="float: left; width: 48%;" class="adminLinks">
<%
			if( ! "true".equals(System.getProperty("itrust.production") ) ) { 
%>
				  <a class="iTrustTestNavlink" href="/iTrust/util/transactionLog.jsp">Transaction Log</a>
				| <a class="iTrustTestNavlink" href="/iTrust/util/displayDatabase.jsp">Display Database</a>
				| <a class="iTrustTestNavlink" href="/iTrust/util/blackbox/blackbox.jsp">Black Box Test Plan</a>
				| <a class="iTrustTestNavlink" href="/iTrust/util/showFakeEmails.jsp">Show Fake Emails</a>
<%
			}
%>
			</div>
			<div style="float: right; width: 48%; margin-right: 10px;text-align:right;">
				  <a class="iTrustNavlink" href="mailto:nobody@itrust.com">Contact</a>
				| <a class="iTrustNavlink" href="/iTrust/privacyPolicy.jsp">Privacy Policy</a>
				| <a class="iTrustNavlink" href="http://agile.csc.ncsu.edu/iTrust/">iTrust v21.0</a>
			</div>
		</div>
</div>
    <script src="/iTrust/js/bootstrap.js"></script>
	</body>
</html>
