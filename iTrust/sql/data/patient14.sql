DELETE FROM users WHERE MID = 14;
DELETE FROM patients WHERE MID = 14;
DELETE FROM declaredhcp WHERE PatientID = 14;

INSERT INTO users(MID, password, role, sQuestion, sAnswer) 
			VALUES (14, '30c952fab122c3f9759f02a6d95c3758b246b4fee239957b2d4fee46e26170c4', 'patient', 'how you doin?', 'good');
/*password: pw*/
INSERT INTO patients (MID, firstName,lastName, email, phone) 
VALUES (14, 'Zack', 'Arthur', 'k@l.com', '919-555-1234');


INSERT INTO declaredhcp(PatientID, HCPID) VALUES (14, 9900000000);