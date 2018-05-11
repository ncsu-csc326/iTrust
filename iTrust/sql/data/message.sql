INSERT INTO message(message_id,parent_msg_id,from_id,to_id,sent_date,message,subject,been_read)
VALUES
('1',null,'9000000000','2','2010-01-21 11:32:00','I just updated your office visit.','Office Visit Updated','0'),
('2',null,'2','9000000000 ','2010-01-13 13:46:00','Have my lab results returned yet?','Lab Results','1'),
('3',null,'1','9000000000','2010-01-19 07:58:00','When is my next scheduled appointment?','Appointment','0'),
('4',null,'2','9000000000','2010-01-31 12:12:00','Do I need to schedule another office visit before my prescription can be renewed?','Prescription','0'),
('5',null,'5','9000000000','2010-01-07 09:15:00','How often would you like me to report my physiological measurements?','Remote Monitoring Question','0'),
('6',null,'2','9000000000','2010-02-02 13:03:00','I would like to schedule an appointment before my throat gets any worse. Thanks!','Scratchy Throat','0'),
('7',null,'1','9000000000','2010-01-29 08:01:00','I noticed that there is a medication posted in my last office visit, but you never gave me a prescription!','Office Visit','0'),
('8',null,'9000000000','1','2010-01-28 17:58:00','Hey, I checked on that!','Lab Procedure','1'),
('9',null,'1','9000000003','2010-01-16 11:55:00','Can I reschedule my appointment for next Monday at 2PM?','Appointment Reschedule','0'),
('10',null,'1','9000000000','2010-01-20 16:58:00','Have my lab results have returned? It''s been over 3 weeks!','Lab Results','0'),
('11',null,'1','9000000000','2010-01-31 16:01:00','How often should I update per day?','Telemedicine','0'),
('12',null,'1','9000000000','2010-01-08 14:59:00','Sorry, I had a flat tire on my way to your office. I will call the office ASAP to reschedule!','Missed Appointment','0'),
('13',null,'1','9000000000','2009-12-02 11:15:00','Is it safe to take leftover penicillin from last year when I was sick?','Old Medicine','0'),
('14',null,'1','9000000000','2009-12-29 15:33:00','After taking this aspirin, I feel a little woozy. Should I report this as an adverse event?','Aspirin Side Effects','0'),
('15',null,'1','9000000000','2010-02-01 09:12:00','I checked on that!','Appointment','1'),
('16','8','1','9000000000','2010-01-29 17:58:00','Thank you for checking on this!','RE: Lab Procedure','0'),
('17','15','1','9000000000','2010-02-01 09:12:00','Thank you for checking on this!','RE: Appointment','0');