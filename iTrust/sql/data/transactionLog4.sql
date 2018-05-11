DELETE FROM representatives where representeeMID = '2';
  
INSERT INTO transactionlog(loggedInMID, secondaryMID, transactionCode, timeLogged, addedInfo) 
						VALUES (1, 2, 1900,'2008-05-12 14:11:00','Viewed prescription report');


