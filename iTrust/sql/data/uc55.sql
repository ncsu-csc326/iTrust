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
	
