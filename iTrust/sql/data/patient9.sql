


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

INSERT INTO declaredhcp(PatientID, HCPID) VALUES (99, 9900000000)
ON DUPLICATE KEY UPDATE PatientID = PatientID;