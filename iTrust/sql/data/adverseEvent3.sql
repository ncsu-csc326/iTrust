INSERT INTO ovprocedure(ID, VisitID, CPTCode, HCPID )
                    VALUES (998, 998, '90371', 9000000000),
                    (555, 555, '90371', 9000000000),
                    (556, 556, '90371', 9000000000),
                    (557, 557, '90371', 9000000000),
                    (558, 558, '90371', 9000000000),
                    (559, 559, '90371', 9000000000);
                    
INSERT INTO officevisits(
	id,
	visitDate,
	HCPID,
	notes,
	HospitalID,
	PatientID
)
VALUES (998,'2009-05-19',9000000000,'The adEvent3 test visit','1',2),
(555,'2009-02-19',9000000000,'The adEvent3 test visit','1',1),
(556,'2009-05-07',9000000000,'The adEvent3 test visit','1',10),
(557,'2008-12-31',9000000000,'The adEvent3 test visit','1',3),
(558,'2009-01-01',9000000000,'The adEvent3 test visit','1',13),
(559,'2009-01-18',9000000000,'The adEvent3 test visit','1',4);

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
VALUES (0, 'Active', 2, 'Hepatitis B', '90371', 'A rash began spreading outward from the injection spot', 
	9000000000, '2009-05-19 08:34:00'),
	(0, 'Active', 1, 'Hepatitis B', '90371', 'I have a rash around the shot area', 
	9000000000, '2009-02-19 06:01:00'),
	(0, 'Active', 10, 'Hepatitis B', '90371', 'I have skin irritation from the immunization', 
	9000000000, '2009-05-07 14:37:00'),
	(0, 'Active', 3, 'Hepatitis B', '90371', 'I am in pain', 9000000000, '2008-12-31 22:00:00'),
	(0, 'Active', 13, 'Hepatitis B', '90371', 'A rash a rash a rash', 9000000000, '2009-01-01 08:16:00'),
	(0, 'Active', 4, 'Hepatitis B', '90371', 'Ouch', 9000000000, '2009-01-18 13:51:00');