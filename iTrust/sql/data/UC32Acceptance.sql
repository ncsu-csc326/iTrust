DELETE FROM users WHERE MID IN (16, 17, 18);
DELETE FROM patients WHERE MID IN (16, 17, 18);
DELETE FROM declaredhcp WHERE PatientID IN (16, 17, 18);



INSERT INTO patients (MID, firstName,lastName, email, phone) 
VALUES (16, 'Andy', 'Koopa', 'ak@gmail.com', '919-224-3343'),
		(17, 'David', 'Prince', 'prince@gmail.com', '919-212-3433'),
		(18, 'Mark', 'Jackson', 'mj@gmail.com', '919-349-3432');


INSERT INTO declaredhcp(PatientID, HCPID) 
VALUES (16, 9000000003),
		(17, 9000000003),
		(18, 9000000000);
