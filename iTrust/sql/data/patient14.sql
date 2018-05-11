DELETE FROM users WHERE MID = 14;
DELETE FROM officevisits WHERE PatientID = 14;
DELETE FROM patients WHERE MID = 14;
DELETE FROM declaredhcp WHERE PatientID = 14;
DELETE FROM ovdiagnosis WHERE VisitID = 1066;
DELETE FROM ovmedication WHERE VisitID = 1066;


INSERT INTO users(MID, password, role, sQuestion, sAnswer) 
			VALUES (14, '30c952fab122c3f9759f02a6d95c3758b246b4fee239957b2d4fee46e26170c4', 'patient', 'how you doin?', 'good');
/*password: pw*/
INSERT INTO patients (MID, firstName,lastName, email, phone) 
VALUES (14, 'Zack', 'Arthur', 'k@l.com', '919-555-1234');


INSERT INTO officevisits(id,visitDate,HCPID,notes,HospitalID,PatientID)
VALUES (1066,'2007-6-09',9900000000,'Yet another office visit.','1',14);



INSERT INTO ovmedication(NDCode, VisitID, StartDate,EndDate,Dosage,Instructions)
	VALUES ('647641512', 1066, '2006-10-10', CURDATE(), 5, 'Take twice daily');

INSERT INTO icdcodes (Code, Description) VALUES (459.99, '');

INSERT INTO ovdiagnosis(ICDCode, VisitID) VALUES 
			(459.99, 1066);

INSERT INTO declaredhcp(PatientID, HCPID) VALUES (14, 9900000000);