INSERT INTO ovprocedure(ID, VisitID, CPTCode, HCPID )
                    VALUES (998, 998, '90371', 9000000000),
                    (555, 555, '90371', 9000000000),
                    (556, 556, '90712', 9000000000),
                    (557, 557, '90707', 9000000000),
                    (558, 558, '90371', 9000000000),
                    (559, 559, '90707', 9000000000),
                    (560, 560, '90712', 9000000000),
                    (570, 570, '90707', 9000000000);
                    
INSERT INTO ovmedication(VisitID, NDCode, StartDate, EndDate, Dosage, Instructions) 
                    VALUES (122, '548684985', '2009-06-15', '2009-12-15', 10, 'Take once daily with water'),
                    (112, '009042407', '2008-12-15', '2009-12-18', 500, 'Take once daily'),
                    (113, '081096', '2009-04-06', '2010-04-06', 100, 'Take when you get headaches');
                    
INSERT INTO ndcodes(Code, Description) VALUES
('548684985','Citalopram Hydrobromide'),
('009042407','Tetracycline'),
('081096','Aspirin');

INSERT INTO officevisits(
	id,
	visitDate,
	HCPID,
	notes,
	HospitalID,
	PatientID
)
VALUES (998,'2009-05-19',9000000000,'The adEvent test visit','1',2),
(555,'2009-02-19',9000000000,'The adEvent test visit','1',1),
(556,'2009-05-07',9000000000,'The adEvent test visit','1',10),
(557,'2008-12-31',9000000000,'The adEvent test visit','1',3),
(558,'2009-01-01',9000000000,'The adEvent test visit','1',13),
(559,'2009-01-18',9000000000,'The adEvent test visit','1',4),
(570,'2009-11-18',9000000000,'The adEvent test visit','1',2),
(122,'2009-06-15',9000000000,'The adEvent test visit','1',2),
(112,'2008-12-15',9000000000,'The adEvent test visit','1',2),
(113,'2009-04-06',9000000000,'The adEvent test visit','1',2),
(560,'2009-09-27',9000000000,'The adEvent test visit','1',2);

INSERT INTO cptcodes(Code, Description, Attribute) VALUES
('90371','Hepatitis B','immunization'),
('90707','Measles, Mumps, Rubella','immunization'),
('90712','Poliovirus','immunization')

ON DUPLICATE KEY UPDATE Code = Code;

INSERT INTO adverseevents(
	Status,
	PatientMID,
	PresImmu,
	Code,
	Comment,
	Prescriber,
	TimeLogged
)
VALUES ('', 1, 'Hepatitis B', '90371', 'A rash began spreading outward from the injection spot', 
	9000000000, '2009-05-19 08:34:00'),
	('', 1, 'Hepatitis B', '90371', 'I have a rash around the shot area', 
	9000000000, '2009-02-19 06:01:00'),
	('', 10, 'Poliovirus', '90712', 'I have skin irritation from the immunization', 
	9000000000, '2009-05-07 14:37:00'),
	('', 3, 'Measles, Mumps, Rubella', '90707', 'I am in pain', 9000000000, '2008-12-31 22:00:00'),
	('', 13, 'Hepatitis B', '90371', 'A rash a rash a rash', 9000000000, '2009-01-01 08:16:00'),
	('', 4, 'Measles, Mumps, Rubella', '90707', 'Ouch', 9000000000, '2009-01-18 13:51:00'),
	('', 13, 'Citalopram Hydrobromide', '548684985', 'Stomach cramps and migraine headaches after taking this drug', 
	9000000000, '2009-10-12 15:10:00'),
	('', 13, 'Tetracycline', '009042407', 'Blah', 9000000000, '2009-06-12 08:10:00'),
	('', 10, 'Tetracycline', '009042407', 'More blah', 9000000000, '2009-09-28 08:59:00');