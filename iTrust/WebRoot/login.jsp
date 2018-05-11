<%@page errorPage="/auth/exceptionHandler.jsp"%>
<%@page import="java.util.ResourceBundle"%>
<%@page import="edu.ncsu.csc.itrust.enums.TransactionType"%>
<%@ page import="net.tanesha.recaptcha.ReCaptcha" %>
<%@ page import="net.tanesha.recaptcha.ReCaptchaFactory" %>
<%@ page import="net.tanesha.recaptcha.ReCaptchaImpl" %>
<%@ page import="net.tanesha.recaptcha.ReCaptchaResponse" %>
<%@ page import="org.apache.commons.codec.digest.DigestUtils" %>

<%@include file="/global.jsp" %>

<%
pageTitle = "iTrust - Login";
%>
<%
String remoteAddr = request.getRemoteAddr();
//recaptcha.properties file found in WEB-INF/classes (usually not seen in Eclipse)
ReCaptchaImpl reCaptcha = new ReCaptchaImpl();
//ResourceBundle reCaptchaProps = ResourceBundle.getBundle("recaptcha"); 
reCaptcha.setPrivateKey("6Lcpzb4SAAAAAGbscE39L3UmHQ_ferVd7RyJuo5Y");

String challenge = request.getParameter("recaptcha_challenge_field");
String uresponse = request.getParameter("recaptcha_response_field");

String user = request.getParameter("j_username");
String pass = request.getParameter("j_password");
if(pass!=null){
	String salt = "";
	try {
		long tempID = Long.parseLong(user);
		salt = authDAO.getSalt(tempID);
	} catch (NumberFormatException e){
		salt = "";
	}
	pass = DigestUtils.sha256Hex(pass + salt);
}

if(challenge != null) {
	ReCaptchaResponse reCaptchaResponse = reCaptcha.checkAnswer(remoteAddr, challenge, uresponse);

	if (reCaptchaResponse.isValid()) {
		loginFailureAction.setCaptcha(true);
		validSession = true;
		response.sendRedirect("/iTrust/j_security_check?j_username=" + user + "&j_password=" + pass);
	} else {
		if(request.getParameter("loginError") == null) {
			loginFailureAction.setCaptcha(false);
			long userMID;
			try{
				userMID= Long.parseLong(user);
				loggingAction.logEvent(TransactionType.LOGIN_FAILURE, userMID, userMID, "");
			}catch(NumberFormatException e){
				loggingAction.logEvent(TransactionType.LOGIN_FAILURE, 0, 0, "Username: "+user);
			}
						
			pageContext.forward("/login.jsp?loginError=true");
		}
	}
} else if(loginFailureAction.needsCaptcha() && user != null ) {
	loginFailureAction.setCaptcha(false);
} else if(user != null && !"true".equals(request.getParameter("loginError"))) {
	session.setAttribute("loginFlag", "true");
	response.sendRedirect("/iTrust/j_security_check?j_username=" + user + "&j_password=" + pass);
}

if(request.getParameter("loginError") != null) {
	loginMessage = loginFailureAction.recordLoginFailure();
	long userMID;
	try{
		userMID= Long.parseLong(user);
		loggingAction.logEvent(TransactionType.LOGIN_FAILURE, userMID, userMID, "");
	}catch(NumberFormatException e){
		loggingAction.logEvent(TransactionType.LOGIN_FAILURE, 0, 0, "Username: "+user);
	}
	response.sendRedirect("/iTrust/login.jsp");
}

%>

<%@include file="/header.jsp" %>
<script type="text/javascript">
	$( document ).ready(function(){
		$('#home-content').delay(1000).animate({opacity:1},3000);
		var sayings = "Now is the time to try something new.;If winter comes, can spring be far behind?;A stranger, is a friend you have not spoken to yet.;Conquer your fears or they will conquer you.;You learn from your mistakes, you will learn a lot today.;You only need look to your own reflection for inspiration. Because you are Beautiful!;You are not judged by your efforts you put in, you are judged on your performance.;Rivers need springs.;Good news from afar may bring you a welcome visitor.;When all else seems to fail, smile for today and just love someone.;When you look down, all you see is dirt, so keep looking up.;If you are afraid to shake the dice, you will never throw a six.;A single conversation with a wise man is better than ten years of study.;Happiness is often a rebound from hard work.;The world may be your oyster, but that doesn't mean you'll get it's pearl.;Your life will be filled with magical moments.;You're true love will show himself to you under the moonlight.;Do not follow where the path may lead. Go where there is no path...and leave a trail.;Do not fear what you don't know.;The object of your desire comes closer.;You have a flair for adding a fanciful dimension to any story.;Do not be covered in sadness or be fooled in happiness they both must exist.;You will have unexpected great good luck.;You will have a pleasant surprise.;All progress occurs because people dare to be different.;Your ability for accomplishment will be followed by success.;We can't help everyone. But everyone can help someone.;Express yourself: Don't hold back!;You have a deep appreciation of the arts and music.;Intelligence is the door to freedom and alert attention is the mother of intelligence.;Back away from individuals who are impulsive.;Enjoyed the meal? Buy one to go too.;You believe in the goodness of mankind.;A big fortune will descend upon you this year.;Now these three remain, faith, hope, and love. The greatest of these is love.;For success today look first to yourself.;Determination is the wake-up call to the human will.;There are no limitations to the mind except those we aknowledge.;A merry heart does good like a medicine.;Whenever possible, keep it simple.;Your dearest wish will come true.;Poverty is no disgrace.;If you don’t do it excellently, don’t do it at all.;You have an unusual equipment for success, use it properly.;Emotion is energy in motion.;You will soon be honored by someone you respect.;Punctuality is the politeness of kings and the duty of gentle people everywhere.;Your happiness is intertwined with your outlook on life.;Elegant surroundings will soon be yours.;If you feel you are right, stand firmly by your convictions.;Your smile brings happiness to everyone you meet.;Instead of worrying and agonizing, move ahead constructively.;Do you believe?;Endurance and persistence will be rewarded.;Never underestimate the power of the human touch.;Hold on to the past but eventually, let the times go and keep the memories into the present.;Truth is an unpopular subject. Because it is unquestionably correct.;The most important thing in communication is to hear what isn’t being said.;Be broad minded and socially active.;Your present question marks are going to succeed.;You have a fine capacity for the enjoyment of life.;You will live long and enjoy life.;A wish is what makes life happen when you dream of rose petals.;Love can turn a cottage into a golden palace.;Lend your money and lose your freind.;If you never give up on love, It will never give up on you.;Unleash your life force.;There is a prospect of a thrilling time ahead for you.;No distance is too far, if two hearts are tied together.;Land is always in the mind of the flying birds.;Try? No! Do or do not, there is no try.;It's about time you asked that special someone on a date.;You create your own stage ... the audience is waiting.;It is never too late. Just as it is never too early.;Discover the power within yourself.;Good things take time.;Stop thinking about the road not taken and pave over the one you took.;Put your unhappiness aside. Life is beautiful, be happy.;You can still love what you can not have in life.;Make a wise choice everyday.;Circumstance does not make the man, it reveals him to himself.;The man who waits till tomorrow, misses the opportunities of today.;Life does not get better by chance. It gets better by change.;If you never expect anything you can never be disappointed.;True wisdom is found in happiness.;Ones always regrets what could have done. Remember for next time.;Every exit is an entrance to new experiences.;Your doctors are guessing.;About time I got out of that cookie.;I've never seen anyone fall from a banana peel.;Friendship is the greatest treasure of all.;Follow your bliss and the Universe will open doors here there were once only walls.;Find a peaceful place where you can make plans for the future.;All the water in the world can't sink a ship unless it gets inside.;When you squeeze an orange, orange juice comes out - because that's what's inside.;If at first you don't succeed, lower your standards.;If your mood ring is tie-dye, you may need counseling.;Don't judge a book by the words in it.;Wow, somebody needs a happy meal.;\"Stressed\" is \"desserts\" spelled backwards.;Brocolli is a vegetable. So is pizza.;I love you. I love you. I love you, I do.  But don't get excited, I love monkeys too.;Life doesn't come with instructions.;Burritos are sleeping bags for ground beef.;I would imagine that if you could understand morse code, a tap dancer would drive you crazy.;Pompous male bees are often known to drone on about themselves.;At 6 foot 7, he was good at basketball and everyone looked up to him.;Cut the red wire.  Unless there is a blue one or a green one.;Build failed, please try again.;It's like smelling dirty underwear, you never know what you're going to get.;Pigs transform food into bacon.".split(';');
		var index = Math.floor((Math.random()*sayings.length)+1);
		$('.jenkins-quote').html(sayings[index]);
		//$('.jenkins-quote').delay(1000).animate({opacity:1},4000);
		
	});
</script>
<div id="home-content">
	<blockquote><span class="jenkins-quote">Cut the red wire.  Unless there is a blue one or a green one.</span></blockquote>
	<h1>- quotes by Dr. Jenkins</h1>
	<!-- patient-centered -->
</div>
<%
	if(!loginFailureAction.needsCaptcha()) {
%>
<%
	} else {
%>


<%
	}
%>

<%@include file="/footer.jsp" %>

