DELETE FROM users WHERE MID = 100;
/*For every insert, first clear the database of those entries.*/
INSERT INTO users(MID, password, role, sQuestion, sAnswer) 
			VALUES (100, '30c952fab122c3f9759f02a6d95c3758b246b4fee239957b2d4fee46e26170c4', 'patient', 'Is it broken?', 'I can fix it.');
/*password: pw*/
DELETE FROM patients WHERE MID = 100;
INSERT INTO patients (MID, firstName, lastName, email, phone) 
VALUES (100, 'Anakin', 'Skywalker', 'thechosenone@itrust.com', '919-419-5555');

INSERT INTO allergies(PatientID, Description, FirstFound, Code)
	VALUES (100,'Lantus','2012-2-03', '00882219'); /* Allergy for Lantus */
			
DELETE FROM declaredhcp WHERE PatientID = 100;
INSERT INTO declaredhcp(PatientID, HCPID) VALUES (100, 9000000000);