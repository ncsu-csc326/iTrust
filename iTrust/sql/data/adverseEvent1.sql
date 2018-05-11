INSERT INTO ovmedication(VisitID, NDCode, StartDate, EndDate, Dosage, Instructions) 
                    VALUES (999, 54868-4985, '2007-06-15', '2007-12-15', 10, 'Take once daily with water');
                    
INSERT INTO officevisits(
	id,
	visitDate,
	HCPID,
	notes,
	HospitalID,
	PatientID
)
VALUES (999,'2007-05-10',9000000000,'The adEvent test visit','1',2);

INSERT INTO ndcodes(Code, Description) VALUES
('548684985','Citalopram Hydrobromide')

ON DUPLICATE KEY UPDATE Code = Code;

INSERT INTO adverseevents(
	id,
	Status,
	PatientMID,
	PresImmu,
	Code,
	Comment,
	Prescriber,
	TimeLogged
)
VALUES (0, 'Active', 2, 'Citalopram Hydrobromide', '548684985', 'Stomach cramps and migraine headaches after taking this drug', 
	9000000000, '2007-08-12 15:10:00');