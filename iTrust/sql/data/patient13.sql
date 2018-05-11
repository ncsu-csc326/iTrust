DELETE FROM users WHERE MID = 13;
DELETE FROM patients WHERE MID = 13;
DELETE FROM declaredhcp WHERE PatientID = 13;

INSERT INTO users(MID, password, role, sQuestion, sAnswer) 
			VALUES (13, '30c952fab122c3f9759f02a6d95c3758b246b4fee239957b2d4fee46e26170c4', 'patient', 'how you doin?', 'good');
/*password: pw*/
INSERT INTO patients (MID, firstName,lastName, email, phone) 
VALUES (13, 'Blim', 'Cildron', 'i@j.com', '919-555-9213');


INSERT INTO declaredhcp(PatientID, HCPID) VALUES (13, 9900000000);