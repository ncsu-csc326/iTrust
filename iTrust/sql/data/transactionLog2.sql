DELETE FROM transactionlog where secondaryMID = '2';

INSERT INTO transactionlog(loggedInMID, secondaryMID, transactionCode, timeLogged, addedInfo) 
                    VALUES (9000000000, 2, 1600,'2008-03-04 10:15:00','Identified risk factors of chronic diseases'),
                           (9000000000, 2, 1102,'2008-09-07 16:30:00','Updated office visit'),
                           (9000000003, 2, 1900,'2008-07-15 13:13:00','Viewed prescription report'),
                           (9000000006, 2, 2101,'2008-11-14 09:32:00','Viewed emergency report ');


