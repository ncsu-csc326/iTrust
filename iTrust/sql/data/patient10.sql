DELETE FROM users WHERE MID = 10;
DELETE FROM patients WHERE MID = 10;
DELETE FROM declaredhcp WHERE PatientID = 10;


INSERT INTO users(MID, password, role, sQuestion, sAnswer) 
			VALUES (10, '30c952fab122c3f9759f02a6d95c3758b246b4fee239957b2d4fee46e26170c4', 'patient', 'how you doin?', 'good');
/*password: pw*/
INSERT INTO patients (MID, firstName,lastName, email, phone) 
VALUES (10, 'Zappic', 'Clith', 'c@d.com', '919-555-9213');

INSERT INTO declaredhcp(PatientID, HCPID) VALUES (10, 9900000000);