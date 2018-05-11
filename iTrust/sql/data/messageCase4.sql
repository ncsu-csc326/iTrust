INSERT INTO message(message_id,parent_msg_id,from_id,to_id,sent_date,message,subject,been_read)
VALUES
('1',null,'9000000000','2','2009-03-25 16:22:00','The flu vaccine does not have any side effects.','RE: Influenza Vaccine','0'),
('2',null,'9000000000','2','2009-12-03 8:26:00','Getting a flu shot is a great idea, especially at your age.','RE: Flu Season','0'),
('3',null,'9000000000','2','2010-03-25 16:39:00','Either way, the flu shot should be free','RE: Influenza Vaccine','0'),
('4',null,'9000000000','2','2010-02-12 9:22:00','I''ll see you then! Thanks','RE: appointment','0'),
('5',null,'9000000000','2','2008-02-02 9:22:00','I just scheduled an appointment with you next week. Thanks for asking!','RE: Bad coughing again','0'),
('6',null,'9000000000','2','2010-01-21 20:22:00','you do not need to schedule an appointment for a flu shot. Visit the local health department.','RE: Vaccines','0');
/*UPDATE patients SET MessageFilter='Kelly Doctor,,flu,side effects,01/05/2010,04/01/2010' WHERE MID='2';*/