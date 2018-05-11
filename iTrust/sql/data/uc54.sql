/***************************USERS**********************************/
/* Weight Test - Weight Percentile EC */
INSERT INTO users(MID, password, role, sQuestion, sAnswer)
	VALUES (111, '30c952fab122c3f9759f02a6d95c3758b246b4fee239957b2d4fee46e26170c4', 'patient', '2+2?', '4')
 ON DUPLICATE KEY UPDATE MID = MID;
 /*password: pw*/
 
/* Height Test - Height/Length Percentile EC */
INSERT INTO users(MID, password, role, sQuestion, sAnswer)
	VALUES (112, '30c952fab122c3f9759f02a6d95c3758b246b4fee239957b2d4fee46e26170c4', 'patient', '2+2?', '4')
 ON DUPLICATE KEY UPDATE MID = MID;
 /*password: pw*/
 
/* HeadCirc Test - Head Circumference Percentile EC */
INSERT INTO users(MID, password, role, sQuestion, sAnswer)
	VALUES (113, '30c952fab122c3f9759f02a6d95c3758b246b4fee239957b2d4fee46e26170c4', 'patient', '2+2?', '4')
 ON DUPLICATE KEY UPDATE MID = MID;
 /*password: pw*/
 
/* BMI Test - BMI Percentile EC */
INSERT INTO users(MID, password, role, sQuestion, sAnswer)
	VALUES (114, '30c952fab122c3f9759f02a6d95c3758b246b4fee239957b2d4fee46e26170c4', 'patient', '2+2?', '4')
 ON DUPLICATE KEY UPDATE MID = MID;
 /*password: pw*/
 
/* Weight Age - Weight Age Bounds */
INSERT INTO users(MID, password, role, sQuestion, sAnswer)
	VALUES (115, '30c952fab122c3f9759f02a6d95c3758b246b4fee239957b2d4fee46e26170c4', 'patient', '2+2?', '4')
 ON DUPLICATE KEY UPDATE MID = MID;
 /*password: pw*/
 
/* Height Age - Height/Length Age Bounds */
INSERT INTO users(MID, password, role, sQuestion, sAnswer)
	VALUES (116, '30c952fab122c3f9759f02a6d95c3758b246b4fee239957b2d4fee46e26170c4', 'patient', '2+2?', '4')
 ON DUPLICATE KEY UPDATE MID = MID;
 /*password: pw*/
 
/* HeadCirc Age - Head Circumference Age Bounds */
INSERT INTO users(MID, password, role, sQuestion, sAnswer)
	VALUES (117, '30c952fab122c3f9759f02a6d95c3758b246b4fee239957b2d4fee46e26170c4', 'patient', '2+2?', '4')
 ON DUPLICATE KEY UPDATE MID = MID;
 /*password: pw*/
 
/* BMI Age - BMI Age Bounds */
INSERT INTO users(MID, password, role, sQuestion, sAnswer)
	VALUES (118, '30c952fab122c3f9759f02a6d95c3758b246b4fee239957b2d4fee46e26170c4', 'patient', '2+2?', '4')
 ON DUPLICATE KEY UPDATE MID = MID;
 /*password: pw*/
 
/*************************PATIENTS*********************************/
/* Weight Test - Weight Percentiles EC */
INSERT INTO patients
(MID, firstName, lastName, email, address1, address2, city, state, zip, phone, eName,
ePhone, iCName, iCAddress1, iCAddress2, iCCity, ICState, iCZip, iCPhone, iCID, DateOfBirth,
DateOfDeath, CauseOfDeath, MotherMID, FatherMID, BloodType, Ethnicity, Gender, TopicalNotes)
VALUES 
(111, 'Weight', 'Test', 'weight.test@gmail.com', '123 Test Dr', '', 'Raleigh', 'NC', '27607', '555-666-7777', '',
'', '', '', '', '', 'NC', '', '', '', '2000-01-01',
NULL, '', NULL, NULL, 'A+', 'Other', 'Female', 'Weight Percentiles EC'
)  ON DUPLICATE KEY UPDATE MID = MID;

/* Height Test - Height/Length Percentiles EC */
INSERT INTO patients
(MID, firstName, lastName, email, address1, address2, city, state, zip, phone, eName,
ePhone, iCName, iCAddress1, iCAddress2, iCCity, ICState, iCZip, iCPhone, iCID, DateOfBirth,
DateOfDeath, CauseOfDeath, MotherMID, FatherMID, BloodType, Ethnicity, Gender, TopicalNotes)
VALUES 
(112, 'Height', 'Test', 'height.test@gmail.com', '123 Test Dr', '', 'Raleigh', 'NC', '27607', '555-666-7777', '',
'', '', '', '', '', 'NC', '', '', '', '2000-01-01',
NULL, '', NULL, NULL, 'A+', 'Other', 'Female', 'Weight Percentiles EC'
)  ON DUPLICATE KEY UPDATE MID = MID;

/* HeadCirc Test - Head Circumference Percentiles EC */
INSERT INTO patients
(MID, firstName, lastName, email, address1, address2, city, state, zip, phone, eName,
ePhone, iCName, iCAddress1, iCAddress2, iCCity, ICState, iCZip, iCPhone, iCID, DateOfBirth,
DateOfDeath, CauseOfDeath, MotherMID, FatherMID, BloodType, Ethnicity, Gender, TopicalNotes)
VALUES 
(113, 'HeadCirc', 'Test', 'headcirc.test@gmail.com', '123 Test Dr', '', 'Raleigh', 'NC', '27607', '555-666-7777', '',
'', '', '', '', '', 'NC', '', '', '', '2000-01-01',
NULL, '', NULL, NULL, 'A+', 'Other', 'Female', 'Weight Percentiles EC'
)  ON DUPLICATE KEY UPDATE MID = MID;

/* BMI Test - BMI Percentiles EC */
INSERT INTO patients
(MID, firstName, lastName, email, address1, address2, city, state, zip, phone, eName,
ePhone, iCName, iCAddress1, iCAddress2, iCCity, ICState, iCZip, iCPhone, iCID, DateOfBirth,
DateOfDeath, CauseOfDeath, MotherMID, FatherMID, BloodType, Ethnicity, Gender, TopicalNotes)
VALUES 
(114, 'BMI', 'Test', 'bmi.test@gmail.com', '123 Test Dr', '', 'Raleigh', 'NC', '27607', '555-666-7777', '',
'', '', '', '', '', 'NC', '', '', '', '2000-01-01',
NULL, '', NULL, NULL, 'A+', 'Other', 'Female', 'Weight Percentiles EC'
)  ON DUPLICATE KEY UPDATE MID = MID;

/* Weight Age - Weight Age Bounds */
INSERT INTO patients
(MID, firstName, lastName, email, address1, address2, city, state, zip, phone, eName,
ePhone, iCName, iCAddress1, iCAddress2, iCCity, ICState, iCZip, iCPhone, iCID, DateOfBirth,
DateOfDeath, CauseOfDeath, MotherMID, FatherMID, BloodType, Ethnicity, Gender, TopicalNotes)
VALUES 
(115, 'Weight', 'Age', 'weight.age@gmail.com', '123 Test Dr', '', 'Raleigh', 'NC', '27607', '555-666-7777', '',
'', '', '', '', '', 'NC', '', '', '', '2000-01-01',
NULL, '', NULL, NULL, 'A+', 'Other', 'Female', 'Weight Percentiles EC'
)  ON DUPLICATE KEY UPDATE MID = MID;

/* Height Age - Height/Length Age Bounds */
INSERT INTO patients
(MID, firstName, lastName, email, address1, address2, city, state, zip, phone, eName,
ePhone, iCName, iCAddress1, iCAddress2, iCCity, ICState, iCZip, iCPhone, iCID, DateOfBirth,
DateOfDeath, CauseOfDeath, MotherMID, FatherMID, BloodType, Ethnicity, Gender, TopicalNotes)
VALUES 
(116, 'Height', 'Age', 'height.age@gmail.com', '123 Test Dr', '', 'Raleigh', 'NC', '27607', '555-666-7777', '',
'', '', '', '', '', 'NC', '', '', '', '2000-01-01',
NULL, '', NULL, NULL, 'A+', 'Other', 'Female', 'Weight Percentiles EC'
)  ON DUPLICATE KEY UPDATE MID = MID;

/* HeadCirc Age - Head Circumference Age Bounds */
INSERT INTO patients
(MID, firstName, lastName, email, address1, address2, city, state, zip, phone, eName,
ePhone, iCName, iCAddress1, iCAddress2, iCCity, ICState, iCZip, iCPhone, iCID, DateOfBirth,
DateOfDeath, CauseOfDeath, MotherMID, FatherMID, BloodType, Ethnicity, Gender, TopicalNotes)
VALUES 
(117, 'HeadCirc', 'Age', 'headcirc.age@gmail.com', '123 Test Dr', '', 'Raleigh', 'NC', '27607', '555-666-7777', '',
'', '', '', '', '', 'NC', '', '', '', '2000-01-01',
NULL, '', NULL, NULL, 'A+', 'Other', 'Female', 'Weight Percentiles EC'
)  ON DUPLICATE KEY UPDATE MID = MID;

/* BMI Age - BMI Age Bounds */
INSERT INTO patients
(MID, firstName, lastName, email, address1, address2, city, state, zip, phone, eName,
ePhone, iCName, iCAddress1, iCAddress2, iCCity, ICState, iCZip, iCPhone, iCID, DateOfBirth,
DateOfDeath, CauseOfDeath, MotherMID, FatherMID, BloodType, Ethnicity, Gender, TopicalNotes)
VALUES 
(118, 'BMI', 'Age', 'bmi.age@gmail.com', '123 Test Dr', '', 'Raleigh', 'NC', '27607', '555-666-7777', '',
'', '', '', '', '', 'NC', '', '', '', '2000-01-01',
NULL, '', NULL, NULL, 'A+', 'Other', 'Female', 'Weight Percentiles EC'
)  ON DUPLICATE KEY UPDATE MID = MID;

