INSERT INTO personnel (MID, lastName, firstName, address1, address2, city, state, zip, specialty)
	VALUES (9100000000, 'Doctor', 'Good', 'Street 1', 'Street 2', 'Raleigh', 'NC', '27613', 'None'),
		   (9100000001, 'Doctor', 'Bad', 'Avenue 1', 'Avenue 2', 'Raleigh', 'NC', '27613', 'Heart Specialist');

INSERT INTO hcpassignedhos (HCPID, HosID) 
	VALUES (9100000000,'9191919191'), 
		   (9100000000,'8181818181'),
		   (9100000001,'8181818181');

INSERT INTO officevisits (id,visitDate,HCPID,notes,HospitalID,PatientID)
	VALUES (900,'2007-6-09',9100000000,'Good has 10 visits and 5 surveys','1',2),
		   (901,'2007-6-09',9100000000,'Good has 10 visits and 5 surveys','1',2),
		   (902,'2007-6-09',9100000000,'Good has 10 visits and 5 surveys','1',2),
		   (903,'2007-6-09',9100000000,'Good has 10 visits and 5 surveys','1',2),
		   (904,'2007-6-09',9100000000,'Good has 10 visits and 5 surveys','1',2),
		   (905,'2007-6-09',9100000000,'Good has 10 visits and 5 surveys','1',2),
		   (906,'2007-6-09',9100000000,'Good has 10 visits and 5 surveys','1',2),
		   (907,'2007-6-09',9100000000,'Good has 10 visits and 5 surveys','1',2),
		   (908,'2007-6-09',9100000000,'Good has 10 visits and 5 surveys','1',2),
		   (909,'2007-6-09',9100000000,'Good has 10 visits and 5 surveys','1',2),
		   (910,'2007-6-09',9100000001,'Bad has 8 visits and 6 surveys','1',2),
		   (911,'2007-6-09',9100000001,'Bad has 8 visits and 6 surveys','1',2),
		   (912,'2007-6-09',9100000001,'Bad has 8 visits and 6 surveys','1',2),
		   (913,'2007-6-09',9100000001,'Bad has 8 visits and 6 surveys','1',2),
		   (914,'2007-6-09',9100000001,'Bad has 8 visits and 6 surveys','1',2),
		   (915,'2007-6-09',9100000001,'Bad has 8 visits and 6 surveys','1',2),
		   (916,'2007-6-09',9100000001,'Bad has 8 visits and 6 surveys','1',2),
		   (917,'2007-6-09',9100000001,'Bad has 8 visits and 6 surveys','1',2)
		ON DUPLICATE KEY UPDATE HCPID = VALUES(HCPID);

INSERT INTO ovsurvey (VisitID, SurveyDate, WaitingRoomMinutes, ExamRoomMinutes, VisitSatisfaction, TreatmentSatisfaction)
	VALUES (900, '2008-03-01 12:00:00', null, 10, 5, 5),
		   (902, '2008-03-01 12:00:00', null, null, 5, 5),
		   (904, '2008-03-01 12:00:00', 10, null, null, 5),
		   (906, '2008-03-01 12:00:00', 10, 10, null, 5),
		   (908, '2008-03-01 12:00:00', 10, 10, 4, null),
		   (911, '2008-03-01 12:00:00', 20, 30, 1, 1),
		   (912, '2008-03-01 12:00:00', 20, 30, 1, 1),
		   (913, '2008-03-01 12:00:00', 20, 30, 1, 1),
		   (914, '2008-03-01 12:00:00', 20, 30, 1, 3),
		   (915, '2008-03-01 12:00:00', 20, 30, 1, 3),
		   (916, '2008-03-01 12:00:00', 20, 30, 1, 3)
				ON DUPLICATE KEY UPDATE VisitSatisfaction = VALUES(VisitSatisfaction),
										TreatmentSatisfaction = Values(TreatmentSatisfaction);
