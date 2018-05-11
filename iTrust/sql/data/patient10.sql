DELETE FROM users WHERE MID = 10;
DELETE FROM officevisits WHERE PatientID = 10;
DELETE FROM patients WHERE MID = 10;
DELETE FROM declaredhcp WHERE PatientID = 10;
DELETE FROM ovdiagnosis WHERE VisitID = 1062;
DELETE FROM ovmedication WHERE VisitID = 1062;


INSERT INTO users(MID, password, role, sQuestion, sAnswer) 
			VALUES (10, '30c952fab122c3f9759f02a6d95c3758b246b4fee239957b2d4fee46e26170c4', 'patient', 'how you doin?', 'good');
/*password: pw*/
INSERT INTO patients (MID, firstName,lastName, email, phone) 
VALUES (10, 'Zappic', 'Clith', 'c@d.com', '919-555-9213');


INSERT INTO officevisits(id,visitDate,HCPID,notes,HospitalID,PatientID)
VALUES (1062,'2007-6-09',9900000000,'Yet another office visit.','1',10);



INSERT INTO ovmedication(NDCode, VisitID, StartDate,EndDate,Dosage,Instructions)
	VALUES ('647641512', 1062, '2006-10-10', DATE_ADD(CURDATE(), INTERVAL 10 DAY), 5, 'Take twice daily');


INSERT INTO ovdiagnosis(ICDCode, VisitID) VALUES 
			(493.00, 1062);

INSERT INTO declaredhcp(PatientID, HCPID) VALUES (10, 9900000000);