/* Bob Marley (Dependent) */
INSERT INTO users (MID, Password, Role, sQuestion, sAnswer, isDependent)
	VALUES (580, '30c952fab122c3f9759f02a6d95c3758b246b4fee239957b2d4fee46e26170c4', 'patient', '2+2?', '4', 1)
	ON DUPLICATE KEY UPDATE MID = MID;
	
INSERT INTO patients 	(MID, lastName, firstName, email, address1, address2,
						city, state, zip, phone, eName, ePhone,
						DateOfBirth, BloodType, Ethnicity, Gender, TopicalNotes)
	VALUES	(580, 'Marley', 'Bob', 'bob.marley@gmail.com', '123 Broad St.', '',
			'Raleigh', 'NC', '27607', '555-666-7777', '', '',
			'1945-02-06', 'AB+', 'African American', 'Male', 'Has excellent people skills.')
	ON DUPLICATE KEY UPDATE MID = MID;
	
/* Morgan Freeman (Representative) */
INSERT INTO users (MID, Password, Role, sQuestion, sAnswer, isDependent)
	VALUES (581, '30c952fab122c3f9759f02a6d95c3758b246b4fee239957b2d4fee46e26170c4', 'patient', '2+2?', '4', 0)
	ON DUPLICATE KEY UPDATE MID = MID;
	
INSERT INTO patients 	(MID, lastName, firstName, email, address1, address2,
						city, state, zip, phone, eName, ePhone,
						DateOfBirth, BloodType, Ethnicity, Gender, TopicalNotes)
	VALUES	(581, 'Freeman', 'Morgan', 'morgan.freeman@gmail.com', '123 Broad St.', '',
			'Raleigh', 'NC', '27607', '555-666-7777', '', '',
			'1937-06-01', 'AB+', 'African American', 'Male', 'Every morning Morgan Freeman narrates the sunrise.')
	ON DUPLICATE KEY UPDATE MID = MID;
	
/* Morgan Freeman represents Bob Marley */
INSERT INTO representatives (representerMID, representeeMID)
	VALUES	(581, 580);