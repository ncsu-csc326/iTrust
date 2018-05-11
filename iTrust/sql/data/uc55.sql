/********************* REQUIRED IMMUNIZATIONS ***********/
/* DTP */
INSERT INTO requiredprocedures (cptCode, description, ageGroup, attribute)
	VALUES ('90696', 'Diphtheria, Tetanus, Pertussis', 0, 'immunization'),
		   ('90696', 'Diphtheria, Tetanus, Pertussis', 1, 'immunization'),
		   ('90696', 'Diphtheria, Tetanus, Pertussis', 2, 'immunization');
	
/* Polio */
INSERT INTO requiredprocedures (cptCode, description, ageGroup, attribute, ageMax)
	VALUES	('90712', 'Poliovirus', 0, 'immunization', 216),
			('90712', 'Poliovirus', 1, 'immunization', 216),
			('90712', 'Poliovirus', 2, 'immunization', 216);
			
/* MMR */
INSERT INTO requiredprocedures (cptCode, description, ageGroup, attribute)
	VALUES	('90707', 'Measles, Mumps, Rubella', 0, 'immunization'),
			('90707', 'Measles, Mumps, Rubella', 1, 'immunization'),
			('90707', 'Measles, Mumps, Rubella', 2, 'immunization');

/* Haemophilus Influenzae */
INSERT INTO requiredprocedures (cptCode, description, ageGroup, attribute, ageMax)
	VALUES 	('90645', 'Haemophilus Influenzae', 0, 'immunization', 60);
	
/* Hepatitis B */
INSERT INTO requiredprocedures (cptCode, description, ageGroup, attribute)
	VALUES	('90371', 'Hepatitis B', 0, 'immunization'),
			('90371', 'Hepatitis B', 1, 'immunization'),
			('90371', 'Hepatitis B', 2, 'immunization');
	
/* Varicella */
INSERT INTO requiredprocedures (cptCode, description, ageGroup, attribute)
	VALUES 	('90396', 'Varicella', 0, 'immunization'),
			('90396', 'Varicella', 1, 'immunization');

/********************* ICD CODES ************************/
/* Chicken Pox */
INSERT INTO icdcodes (Code, Description, Chronic, URL)
	VALUES (35.00, 'Chicken Pox', 'no', '')
	ON DUPLICATE KEY UPDATE Code = Code;

/********************** USERS ***************************/
/* Adam Sandler */
INSERT INTO users (MID, Password, Role, sQuestion, sAnswer)
	VALUES (300, '30c952fab122c3f9759f02a6d95c3758b246b4fee239957b2d4fee46e26170c4', 'patient', '2+2?', '4')
	ON DUPLICATE KEY UPDATE MID = MID;
	
/* Natalie Portman */
INSERT INTO users (MID, Password, Role, sQuestion, sAnswer)
	VALUES (301, '30c952fab122c3f9759f02a6d95c3758b246b4fee239957b2d4fee46e26170c4', 'patient', '2+2?', '4')
	ON DUPLICATE KEY UPDATE MID = MID;
	
/* Will Smith */
INSERT INTO users (MID, Password, Role, sQuestion, sAnswer)
	VALUES (302, '30c952fab122c3f9759f02a6d95c3758b246b4fee239957b2d4fee46e26170c4', 'patient', '2+2?', '4')
	ON DUPLICATE KEY UPDATE MID = MID;
	
/* Julia Roberts */
INSERT INTO users (MID, Password, Role, sQuestion, sAnswer)
	VALUES (303, '30c952fab122c3f9759f02a6d95c3758b246b4fee239957b2d4fee46e26170c4', 'patient', '2+2?', '4')
	ON DUPLICATE KEY UPDATE MID = MID;
	
/* Christina Aguillera */
INSERT INTO users (MID, Password, Role, sQuestion, sAnswer)
	VALUES (305, '30c952fab122c3f9759f02a6d95c3758b246b4fee239957b2d4fee46e26170c4', 'patient', '2+2?', '4')
	ON DUPLICATE KEY UPDATE MID = MID;
	
/* Charlie Chaplin */
INSERT INTO users (MID, Password, Role, sQuestion, sAnswer)
	VALUES (308, '30c952fab122c3f9759f02a6d95c3758b246b4fee239957b2d4fee46e26170c4', 'patient', '2+2?', '4')
	ON DUPLICATE KEY UPDATE MID = MID;
	
/********************* PATIENTS *************************/
/* Adam Sandler */
INSERT INTO patients 	(MID, lastName, firstName, email, address1, address2,
						city, state, zip, phone, eName, ePhone,
						DateOfBirth, BloodType, Ethnicity, Gender, TopicalNotes)
	VALUES	(300, 'Sandler', 'Adam', 'adam.sandler@gmail.com', '123 Broad St.', '',
			'Raleigh', 'NC', '27607', '555-666-7777', '', '',
			'2009-01-01', 'AB+', 'Caucasian', 'Male', 'A renowned software engineer.')
	ON DUPLICATE KEY UPDATE MID = MID;
	
/* Natalie Portman */
INSERT INTO patients 	(MID, lastName, firstName, email, address1, address2,
						city, state, zip, phone, eName, ePhone,
						DateOfBirth, BloodType, Ethnicity, Gender, TopicalNotes)
	VALUES	(301, 'Portman', 'Natalie', 'natalie.portman@gmail.com', '123 Broad St.', '',
			'Raleigh', 'NC', '27607', '555-666-7777', '', '',
			'2002-01-01', 'AB+', 'Caucasian', 'Female', 'Professional ice skater.')
	ON DUPLICATE KEY UPDATE MID = MID;
	
/* Will Smith */
INSERT INTO patients 	(MID, lastName, firstName, email, address1, address2,
						city, state, zip, phone, eName, ePhone,
						DateOfBirth, BloodType, Ethnicity, Gender, TopicalNotes)
	VALUES	(302, 'Smith', 'Will', 'will.smith@gmail.com', '123 Broad St.', '',
			'Raleigh', 'NC', '27607', '555-666-7777', '', '',
			'1995-01-01', 'AB+', 'African American', 'Male', 'Technically a donkey.')
	ON DUPLICATE KEY UPDATE MID = MID;
	
/* Julia Roberts */
INSERT INTO patients 	(MID, lastName, firstName, email, address1, address2,
						city, state, zip, phone, eName, ePhone,
						DateOfBirth, BloodType, Ethnicity, Gender, TopicalNotes)
	VALUES	(303, 'Roberts', 'Julia', 'julia.roberts@gmail.com', '123 Broad St.', '',
			'Raleigh', 'NC', '27607', '555-666-7777', '', '',
			'2010-01-01', 'AB+', 'Caucasian', 'Female', 'The hills are alive.')
	ON DUPLICATE KEY UPDATE MID = MID;
	
/* Christina Aguillera */
INSERT INTO patients 	(MID, lastName, firstName, email, address1, address2,
						city, state, zip, phone, eName, ePhone,
						DateOfBirth, BloodType, Ethnicity, Gender, TopicalNotes)
	VALUES	(305, 'Aguillera', 'Christina', 'christina.aguillera@gmail.com', '123 Broad St.', '',
			'Raleigh', 'NC', '27607', '555-666-7777', '', '',
			'2009-01-01', 'AB+', 'Caucasian', 'Female', 'Has a fondness for sweets.')
	ON DUPLICATE KEY UPDATE MID = MID;
	
/* Charlie Chaplin */
INSERT INTO patients	(MID, lastName, firstName, email, address1, address2,
						city, state, zip, phone, eName, ePhone,
						DateOfBirth, BloodType, Ethnicity, Gender, TopicalNotes)
	VALUES	(308, 'Chaplin', 'Charlie', 'charlie.chaplin@gmail.com', '123 Broad St.', '',
			'Raleigh', 'NC', '27607', '555-666-7777', '', '',
			'1988-01-01', 'AB+', 'Caucasian', 'Male', 'No color at all.')
	ON DUPLICATE KEY UPDATE MID = MID;
	
/******************** DIAGNOSES *************************/
	
/* Christina Aguillera */
INSERT INTO ovdiagnosis (ID, VisitID, ICDCode, URL)
	VALUES (500, 753, 35.00, '')
	ON DUPLICATE KEY UPDATE ID = ID;

	
/********************* OFFICE VISITS ********************/
/* Adam Sandler */
INSERT INTO officevisits (ID, visitDate, HCPID, notes, PatientID, HospitalID, IsERIncident)
	VALUES 	(739, '2012-12-31', 9000000000, 'DTP vaccine', 300, '1', 0),
			(740, '2013-01-01', 9000000000, 'MMR and Polio vaccines', 300, '1', 0),
			(741, '2013-01-02', 9000000000, 'Hepatitis B vaccine', 300, '1', 0),
			(742, '2013-01-03', 9000000000, 'Rotavirus, Hepatitis A, and Varicella vaccines', 300, '1', 0),
			(743, '2013-01-04', 9000000000, 'Influenza vaccine', 300, '1', 0)
	ON DUPLICATE KEY UPDATE ID = ID;
	
/* Natalie Portman */
INSERT INTO officevisits (ID, visitDate, HCPID, notes, PatientID, HospitalID, IsERIncident)
	VALUES	(744, '2013-01-01', 9000000000, 'DTP vaccine', 301, '1', 0),
			(745, '2013-01-02', 9000000000, 'Polio and MMR vaccines', 301, '1', 0),
			(746, '2013-01-03', 9000000000, 'Hepatitis B vaccine', 301, '1', 0),
			(747, '2013-01-04', 9000000000, 'Rotavirus, Hep A, and Varicella vaccines', 301, '1', 0),
			(748, '2013-01-05', 9000000000, 'Influenza vaccine', 301, '1', 0)
	ON DUPLICATE KEY UPDATE ID = ID;

/* Will Smith */
INSERT INTO officevisits (ID, visitDate, HCPID, notes, PatientID, HospitalID, IsERIncident)
	VALUES	(749, '2013-01-01', 9000000000, 'Vaccines', 302, '1', 0)
	ON DUPLICATE KEY UPDATE ID = ID;
	
/* Julia Roberts */
INSERT INTO officevisits (ID, visitDate, HCPID, notes, PatientID, HospitalID, IsERIncident)
	VALUES	(750, '2013-01-01', 9000000000, 'Vaccines', 303, '1', 0),
			(751, '2013-01-02', 9000000000, 'Hep B vaccine', 303, '1', 0)
	ON DUPLICATE KEY UPDATE ID = ID;
	
/* Christina Aguillera */
INSERT INTO officevisits (ID, visitDate, HCPID, notes, PatientID, HospitalID, IsERIncident)
	VALUES 	(753, '2013-01-01', 9000000000, 'Vaccinations & diagnosis', 305, '1', 0)
	ON DUPLICATE KEY UPDATE ID = ID;
			
/********************* PROCEDURES ************************/
/* Adam Sandler */
INSERT INTO ovprocedure (ID, VisitID, CPTCode, HCPID)
	VALUES	(5000, 739, '90696', '9000000000'),
			(5001, 740, '90712', '9000000000'),
			(5002, 740, '90707', '9000000000'),
			(5003, 741, '90371', '9000000000'),
			(5004, 742, '90396', '9000000000'),
			(5005, 742, '90681', '9000000000'),
			(5006, 742, '90633', '9000000000'),
			(5007, 743, '90645', '9000000000')
	ON DUPLICATE KEY UPDATE ID = ID;
	
/* Natalie Portman */
INSERT INTO ovprocedure (ID, VisitID, CPTCode, HCPID)
	VALUES	(5008, 744, '90696', '9000000000'),
			(5009, 745, '90712', '9000000000'),
			(5010, 745, '90707', '9000000000'),
			(5011, 746, '90371', '9000000000'),
			(5012, 747, '90681', '9000000000'),
			(5013, 747, '90633', '9000000000'),
			(5014, 747, '90396', '9000000000'),
			(5015, 748, '90645', '9000000000')
	ON DUPLICATE KEY UPDATE ID = ID;
	
/* Will Smith */
INSERT INTO ovprocedure (ID, VisitID, CPTCode, HCPID)
	VALUES	(5016, 749, '90696', '9000000000'),
			(5017, 749, '90707', '9000000000'),
			(5018, 749, '90371', '9000000000')
	ON DUPLICATE KEY UPDATE ID = ID;
	
/* Julia Roberts */
INSERT INTO ovprocedure (ID, VisitID, CPTCode, HCPID)
	VALUES	(5019, 750, '90681', '9000000000'),
			(5020, 751, '90371', '9000000000')
	ON DUPLICATE KEY UPDATE ID = ID;
	
/* Christina Aguillera */
INSERT INTO ovprocedure (ID, VisitID, CPTCode, HCPID)
	VALUES	(5027, 753, '90681', '9000000000'),
			(5028, 753, '90371', '9000000000'),
			(5029, 753, '90696', '9000000000'),
			(5030, 753, '90712', '9000000000'),
			(5031, 753, '90707', '9000000000'),
			(5032, 753, '90645', '9000000000')
	ON DUPLICATE KEY UPDATE ID = ID;