/***************************USERS**********************************/
/* Tom Nook - Underweight EC */
INSERT INTO users(MID, password, role, sQuestion, sAnswer) 
	VALUES (106, '30c952fab122c3f9759f02a6d95c3758b246b4fee239957b2d4fee46e26170c4', 'patient', '2+2?', '4')
 ON DUPLICATE KEY UPDATE MID = MID;
 /*password: pw*/
 
 /* Hannah Montana - Normal EC */
 INSERT INTO users(MID, password, role, sQuestion, sAnswer) 
	VALUES (107, '30c952fab122c3f9759f02a6d95c3758b246b4fee239957b2d4fee46e26170c4', 'patient', '2+2?', '4')
 ON DUPLICATE KEY UPDATE MID = MID;
 /*password: pw*/
 
 /* Hank Hill - Overweight EC */
 INSERT INTO users(MID, password, role, sQuestion, sAnswer) 
	VALUES (108, '30c952fab122c3f9759f02a6d95c3758b246b4fee239957b2d4fee46e26170c4', 'patient', '2+2?', '4')
 ON DUPLICATE KEY UPDATE MID = MID;
 /*password: pw*/
 
 /* Snorlax - Obese EC */
 INSERT INTO users(MID, password, role, sQuestion, sAnswer) 
	VALUES (109, '30c952fab122c3f9759f02a6d95c3758b246b4fee239957b2d4fee46e26170c4', 'patient', '2+2?', '4')
 ON DUPLICATE KEY UPDATE MID = MID;
 /*password: pw*/
 
 /* Bart Simpson - Other Cases */
 INSERT INTO users(MID, password, role, sQuestion, sAnswer) 
	VALUES (110, '30c952fab122c3f9759f02a6d95c3758b246b4fee239957b2d4fee46e26170c4', 'patient', '2+2?', '4')
 ON DUPLICATE KEY UPDATE MID = MID;
 /*password: pw*/

/*************************PATIENTS*********************************/
/* Tom Nook - Underweight EC */
INSERT INTO patients
(MID, firstName, lastName, email, address1, address2, city, state, zip, phone, eName,
ePhone, iCName, iCAddress1, iCAddress2, iCCity, ICState, iCZip, iCPhone, iCID, DateOfBirth,
DateOfDeath, CauseOfDeath, MotherMID, FatherMID, BloodType, Ethnicity, Gender, TopicalNotes)
VALUES 
(106, 'Tom', 'Nook', 'tom.nook@gmail.com', '123 New Leaf Dr.', '', 'Raleigh', 'NC', '27607', '123-456-7890', '',
'', '', '', '', '', 'PA', '', '', '', '1980-01-01',
NULL, '', NULL, NULL, 'A+', 'Other', 'Male', 'A reputable businessman.'
)  ON DUPLICATE KEY UPDATE MID = MID;

/* Hannah Montana - Normal EC */
INSERT INTO patients
(MID, firstName, lastName, email, address1, address2, city, state, zip, phone, eName,
ePhone, iCName, iCAddress1, iCAddress2, iCCity, ICState, iCZip, iCPhone, iCID, DateOfBirth,
DateOfDeath, CauseOfDeath, MotherMID, FatherMID, BloodType, Ethnicity, Gender, TopicalNotes)
VALUES
(107, 'Hannah', 'Montana', 'hannah.montana@gmail.com', '456 Montana Pl.', '', 'Montana', 'MT', '59710', '', '',
'', '', '', '', '', 'PA', '', '', '', '1980-01-01',
NULL, '', NULL, NULL, 'A-', 'Caucasian', 'Female', 'By day a normal high school student... by night a rock star!'
) ON DUPLICATE KEY UPDATE MID = MID;

/* HANK HILL - Overweight EC */
INSERT INTO patients
(MID, firstName, lastName, email, address1, address2, city, state, zip, phone, eName,
ePhone, iCName, iCAddress1, iCAddress2, iCCity, ICState, iCZip, iCPhone, iCID, DateOfBirth,
DateOfDeath, CauseOfDeath, MotherMID, FatherMID, BloodType, Ethnicity, Gender, TopicalNotes)
VALUES
(108, 'Hank', 'Hill', 'hank.hill@gmail.com', '789 Hill St.', '', 'Arlen', 'TX', '77777', '', '',
'', '', '', '', '', 'PA', '', '', '', '1980-01-01',
NULL, '', NULL, NULL, 'B+', 'Caucasian', 'Male', 'Sells propane and propane accessories.'
) ON DUPLICATE KEY UPDATE MID = MID;

/* SNORLAX - Obese EC */
INSERT INTO patients
(MID, firstName, lastName, email, address1, address2, city, state, zip, phone, eName,
ePhone, iCName, iCAddress1, iCAddress2, iCCity, ICState, iCZip, iCPhone, iCID, DateOfBirth,
DateOfDeath, CauseOfDeath, MotherMID, FatherMID, BloodType, Ethnicity, Gender, TopicalNotes)
VALUES
(109, 'Snorlax', 'Nolastname', 'snorlax@gmail.com', '234 Pokemon Pl.', '', 'Kanto', 'NC', '86759', '', '',
'', '', '', '', '', 'PA', '', '', '', '1980-01-01',
NULL, '', NULL, NULL, 'B-', 'Other', 'Male', 'Not in shape, but makes an excellent pillow.'
) ON DUPLICATE KEY UPDATE MID = MID;

/* BART SIMPSON - Underage EC */
INSERT INTO patients
(MID, firstName, lastName, email, address1, address2, city, state, zip, phone, eName,
ePhone, iCName, iCAddress1, iCAddress2, iCCity, ICState, iCZip, iCPhone, iCID, DateOfBirth,
DateOfDeath, CauseOfDeath, MotherMID, FatherMID, BloodType, Ethnicity, Gender, TopicalNotes)
VALUES
(110, 'Bartholomew', 'Simpson', 'eatmyshorts@gmail.com', '', '', 'Springfield', 'NJ', '55555', '', '',
'', '', '', '', '', 'PA', '', '', '', '1990-01-01',
NULL, '', NULL, NULL, 'O+', 'Unknown', 'Male', 'Perfectly normal human being.'
) ON DUPLICATE KEY UPDATE MID = MID;

