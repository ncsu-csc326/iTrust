INSERT INTO message(message_id,parent_msg_id,from_id,to_id,sent_date,message,subject,been_read)
VALUES
('2',null,'2','9000000000 ','2010-01-13 13:46:00','Have my lab results returned yet?','Lab Results','1'),
('3',null,'1','9000000000','2010-01-19 07:58:00','When is my next scheduled appointment?','Appointment','0'),
('4',null,'2','9000000000','2010-01-31 12:12:00','Do I need to schedule another office visit before my prescription can be renewed?','Prescription','0'),
('5',null,'5','9000000000','2010-01-07 09:15:00','How often would you like me to report my physiological measurements?','Remote Monitoring Question','0'),
('6',null,'2','9000000000','2010-02-02 13:03:00','I would like to schedule an appointment before my throat gets any worse. Thanks!','Scratchy Throat','0'),
('7',null,'1','9000000000','2010-01-29 08:01:00','I noticed that there is a medication posted in my last office visit, but you never gave me a prescription!','Office Visit','0'),
('8',null,'9000000000','1','2010-01-28 17:58:00','Hey, I checked on that!','Lab Procedure','1'),
('16','8','1','9000000000','2010-01-29 17:58:00','Thank you for checking on this!','RE: Lab Procedure','0');