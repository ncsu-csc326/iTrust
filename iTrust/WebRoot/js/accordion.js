// number of accordions
var numberOfAccordions = 6;
// accordion height
var heightAccordion = 26;// $("#Accordion1Content").height();
// height in pixels
var heightInPixels = 450;//$("#AccordionContainer").height();
// can change to the length of the page so the whole side bar is taken up
var ContentHeight = heightInPixels - (numberOfAccordions * heightAccordion);
// milliseconds to full open
var TimeToSlide = 300.0; //250.0;

var openAccordion = '';

function popup(){
	alert("hi");
}

function runAccordion(index)
{

  var nID = "menu" + index + "Item";
  if(openAccordion == nID)
    nID = '';

  setTimeout("animate(" + new Date().getTime() + "," + TimeToSlide + ",'" 
      + openAccordion + "','" + nID + "')", 33);

  openAccordion = nID;
}

function animate(lastTick, timeLeft, closingId, openingId)
{  
  var curTick = new Date().getTime();
  var elapsedTicks = curTick - lastTick;

  var opening = (openingId == '') ? null : document.getElementById(openingId);
  var closing = (closingId == '') ? null : document.getElementById(closingId);

  if(timeLeft <= elapsedTicks)
  {
    if(opening != null)
      opening.style.height = ContentHeight + 'px';

    if(closing != null)
    {
      closing.style.display = 'none';
      closing.style.height = '0px';
    }
    return;
  }

  timeLeft -= elapsedTicks;
  var newClosedHeight = Math.round((timeLeft/TimeToSlide) * ContentHeight);

  if(opening != null)
  {
    if(opening.style.display != 'block')
      opening.style.display = 'block';
    opening.style.height = (ContentHeight - newClosedHeight) + 'px';
  }

  if(closing != null)
    closing.style.height = newClosedHeight + 'px';

  setTimeout("animate(" + curTick + "," + timeLeft + ",'" 
      + closingId + "','" + openingId + "')", 33);
}