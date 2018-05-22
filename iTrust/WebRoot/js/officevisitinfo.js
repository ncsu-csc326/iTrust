$(function() {
	var currentTabId = window.sessionStorage.getItem("currentTabId");

	if(!!currentTabId && currentTabId !== $("input[name='ovtabs'][checked='checked']").attr("id")) {
		$("#" + currentTabId)[0].click();
	}
	
	$("input[name='ovtabs']").click(function(evt) {
		window.sessionStorage.setItem("currentTabId", $(evt.target).attr("id"));
	})
	
	$("input[type='submit']").click(function(){
		$("#officevisitinfo-message").fadeTo(0,1)
		$("#officevisitinfo-message").text("Processing...");
		$('html, body').animate({ scrollTop: 0 }, 'fast');
	})
});