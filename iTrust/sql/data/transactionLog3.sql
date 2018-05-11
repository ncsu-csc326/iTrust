DELETE FROM transactionlog where secondaryMID = '1';

INSERT INTO transactionlog(loggedInMID, secondaryMID, transactionCode, timeLogged, addedInfo) 
                    VALUES (9000000000, 1, 1900,'2008-06-19 11:12:00','Viewed prescription report'),
                           (2, 1, 1900,'2008-10-17 14:22:00','Viewed prescription report'),
                           (9000000006, 1, 2101,'2008-11-14 10:04:00','Viewed emergency report'),
                           (9000000003, 1, 1102,'2008-09-14 16:59:00','Updated office visit'),
                           /*(8000000009, 1, 2600,'2008-12-03 12:02:00','Entered/edited laboratory procedures'),*/
                           (9000000003, 1, 1600,'2008-04-05 15:12:00','Identified risk factors of chronic diseases'),
                           (8000000009, 1, 1600,'2008-07-06 08:34:00','Identified risk factors of chronic diseases'),
                           (9000000000, 1, 1600,'2008-06-15 13:15:00','Identified risk factors of chronic diseases'),
                           (9000000000, 1, 1102,'2008-12-01 11:30:00','Updated office visit');

DELETE FROM representatives where representeeMID = '1';
                           
INSERT INTO representatives(representerMID, representeeMID)
					VALUES (2,1);
					
DELETE FROM declaredhcp where PatientID = '1';

INSERT INTO declaredhcp(PatientID, HCPID)
					VALUES(1, 9000000003);


