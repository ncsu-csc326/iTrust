


INSERT INTO users(MID, password, role, sQuestion, sAnswer) 
			VALUES (99, '30c952fab122c3f9759f02a6d95c3758b246b4fee239957b2d4fee46e26170c4', 'patient', 'how you doin?', 'good')
		ON DUPLICATE KEY UPDATE mid = mid;
/*password: pw*/
INSERT INTO patients
(MID, 
firstName,
lastName, email, phone) 
VALUES (99, 'Darryl', 'Thompson', 'a@b.com', '919-555-6709')
ON DUPLICATE KEY UPDATE mid = mid;


INSERT INTO officevisits(id,visitDate,HCPID,notes,HospitalID,PatientID)
VALUES (1052,'2007-6-09',9900000000,'Yet another office visit.','1',99),
	   (1053,'2005-10-10',9900000000,'Yet another office visit.','1',99),
	   (1054,'2005-10-10',9900000000,'Yet another office visit.','1',99),
	   (1055,'2007-6-10',9900000000,'Yet another office visit.','1',99),
	   (1056,'2005-10-10',9900000000,'Yet another office visit.','1',99),
	   (1057,'2005-10-10',9000000000,'Yet another office visit.','1',99),
	   (1058,'2005-10-10',9000000000,'Yet another office visit.','1',99),
	   (1059,'2006-10-10',9990000000,'Yet another office visit.','1',99),
	   (1060,'1985-10-10',9000000003,'Yet another office visit.','',99),
		(1061,'2006-10-10',9990000000,'Yet another office visit.','1',99)
		ON DUPLICATE KEY UPDATE id = id;



INSERT INTO ovmedication(NDCode, VisitID, StartDate,EndDate,Dosage,Instructions)
	VALUES ('009042407', 1057, '2006-10-10', '2006-10-11', 5, 'Take twice daily'),
			('009042407', 1055, '2006-10-10', '2006-10-11', 5, 'Take twice daily'),
			('009042407', 1055, '2009-2-16', '2009-2-28', 20, 'Eat like candy'),
			('081096', 1055, '2009-2-16', '2009-3-20', 20, 'Eat like candy'),
			('081096', 1055, '2009-2-16', '2009-3-20', 20, 'Eat like candy'),
			('081096', 1055, '2009-2-13', '2009-2-16', 20, 'Eat like candy'),
			('647641512', 1053, '2006-10-10', DATE_ADD(CURDATE(), INTERVAL 5 DAY), 5, 'Take twice daily'),
			('081096', 1059, '2009-2-13', '2009-2-16', 20, 'Eat like candy'),
			('647641512', 1061, '2006-10-10', DATE_ADD(CURDATE(), INTERVAL 5 DAY), 5, 'Tellem Jimmy sent you')
			ON DUPLICATE KEY UPDATE id = id;


INSERT INTO icdcodes (Code, Description) VALUES (493.00, ''), (390.00, '')
ON DUPLICATE KEY UPDATE Code = Code;

INSERT INTO ovdiagnosis(ICDCode, VisitID) VALUES 
			(493.00, 1055),
			(390.00, 1057)
ON DUPLICATE KEY UPDATE id = id;

INSERT INTO declaredhcp(PatientID, HCPID) VALUES (99, 9900000000)
ON DUPLICATE KEY UPDATE PatientID = PatientID;