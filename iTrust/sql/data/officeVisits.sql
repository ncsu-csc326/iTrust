
INSERT INTO officevisits(id,visitDate,HCPID,notes,HospitalID,PatientID)
VALUES (20000,CONCAT(YEAR(NOW()), '-',  MONTH(NOW()), '-10'),9000000000,'Terrible cough.','1',2),
	  (20001,CONCAT(YEAR(NOW()), '-',  MONTH(NOW()), '-21'),9000000003,'Cannot stop yawning.','1',2)
		 ON DUPLICATE KEY UPDATE id = id;

INSERT INTO ovdiagnosis(ID, ICDCode, VisitID) 
	VALUES  (1000, 487.00, 20000)
	ON DUPLICATE KEY UPDATE id = id;

		   
INSERT INTO ovprocedure(ID, VisitID, CPTCode) 
	VALUES (20010, 20000, '1270F'),
			(20011, 20000, '90657'),
			(20012, 20001, '1270F');
			
INSERT INTO ovmedication(NDCode, VisitID, StartDate,EndDate,Dosage,Instructions)
	VALUES ('664662530', 20001, CONCAT(YEAR(NOW()), '-',  MONTH(NOW()), '-21'), CONCAT(ADDDATE(CURDATE(),60)), 250, 'Administer every 6 hours after meals.'	);
	