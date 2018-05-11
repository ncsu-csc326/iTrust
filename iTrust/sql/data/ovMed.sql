INSERT INTO ovmedication(VisitID, NDCode, StartDate, EndDate, Dosage, Instructions) 
                    VALUES (900, 64764-1512, '2006-10-10', '2020-10-11', 5, 'The Big Test');
                    
INSERT INTO officevisits(
	id,
	visitDate,
	HCPID,
	notes,
	HospitalID,
	PatientID
)
VALUES (900,'2005-10-10',9000000000,'The ovMed test visit','1',2);

INSERT INTO ndcodes(Code, Description) VALUES
('63252','Prioglitazone')

ON DUPLICATE KEY UPDATE Code = Code;