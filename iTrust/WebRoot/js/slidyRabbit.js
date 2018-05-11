//slidyRabbit.js WAS the utility javascript file for sliding the menu into the screen.
//Now it just houses the code for the sliding accordian menu items.
$(document).ready(function(e){
	
	$('.panel-heading a').on('click',function(e){
	    if($($(this).attr("href")).hasClass('in')){
	        e.stopPropagation();
	        e.preventDefault();
	    }
	});
	
	var updateIcons = function(isIn){
		$this = $("#toggleMenu");
		if(!isIn){
			$this.children().each(function(){
				$(this).removeClass("glyphicon-chevron-left");
				$(this).addClass("glyphicon-chevron-right");
			});
		} else {
			$this.children().each(function(){
				$(this).removeClass("glyphicon-chevron-right");
				$(this).addClass("glyphicon-chevron-left");
			});
		}
	}
	
	var hideMenu = function() {
		$("#iTrustMenu").removeClass("swipedMenu");
		$("#iTrustContent").removeClass("swipedContent");
		updateIcons(false);
	}
	
	var showMenu = function(){
		$("#iTrustMenu").addClass("swipedMenu");
		$("#iTrustContent").addClass("swipedContent");
		updateIcons(true);
	}
	
	var page = new SwipeableElem("iTrustPage");
	var menu = new SwipeableElem("iTrustMenu");
	
	page.onSwipeRight(showMenu);
	menu.onSwipeLeft(hideMenu)
	
	$("#toggleMenu").click(function(){
		if($("#iTrustMenu").hasClass("swipedMenu")){
			$("#iTrustMenu").removeClass("swipedMenu");
			$("#iTrustContent").removeClass("swipedContent");
			updateIcons(false);
		} else {
			$("#iTrustMenu").addClass("swipedMenu");
			$("#iTrustContent").addClass("swipedContent");
			updateIcons(true);
		}
	});
	//End side menu slide stuff
	
	
	
	
	
	
	
	//Begin chevron stuff
	var slide = "";
	var $down = null;
	//My bootstrapy function that allows you to generalize the collapse animation.
	//This is used mostly for the menus.
	$("[anim-type='collapse']").each(function(){
		$this = $(this);
		var $chev = $("<span>");
		$chev.addClass("glyphicon");
		$chev.addClass("glyphicon-chevron-down");
		$chev.addClass("sub-chevron");
		$this.children("h2.panel-title").append($chev);
		var sel = $this.attr("anim-target");
		$(sel).hide();
		$this.on('click', function(){
			if(slide === this.getAttribute("anim-target")){
				$(slide).slideUp(300);
				if($down){
					var chev = $down.children("h2.panel-title").children();
					chev.removeClass("glyphicon-chevron-up");
					chev.addClass("glyphicon-chevron-down");
				}
				$down = null;
				slide = "";
				return;
			}
			$(slide).slideUp(300);
			doChevron($(this));
			$down = $(this);
			slide = this.getAttribute("anim-target");
			$(slide).slideDown(300);
		});
	});
	
	var doChevron = function($div){
		var chev;
		if($down){
			chev = $down.children("h2.panel-title").children();
			chev.removeClass("glyphicon-chevron-up");
			chev.addClass("glyphicon-chevron-down");
		}
		chev = $div.children("h2.panel-title").children();
		chev.removeClass("glyphicon-chevron-down");
		chev.addClass("glyphicon-chevron-up");
	}
	//End chevron stuff
});