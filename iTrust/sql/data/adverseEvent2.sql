INSERT INTO ovprocedure(ID, VisitID, CPTCode, HCPID )
                    VALUES (998, 998, '90371', 9000000000);
                    
INSERT INTO officevisits(
	id,
	visitDate,
	HCPID,
	notes,
	HospitalID,
	PatientID
)
VALUES (998,'2009-03-22',9000000000,'The adEvent2 test visit','1',1);

INSERT INTO cptcodes(Code, Description) VALUES
('90371','Hepatitis B')

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
VALUES (0, 'Active', 1, 'Hepatitis B', '90371', 'A rash began spreading outward from the injection spot', 
	9000000000, '2009-05-19 08:34:00');