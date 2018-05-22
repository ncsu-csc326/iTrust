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

INSERT INTO officevisit (
	patientMID, 
	visitDate, 
	locationID, 
	apptTypeID, 
	weight, 
	height,
	blood_pressure,
	household_smoking_status,
	patient_smoking_status,
	hdl,
	ldl,
	triglyceride) 
VALUES (106, "2013-01-01 00:00:00", 1, 1, 45, 60, '160/100', 1, 4, 37, 141, 162);

INSERT INTO officevisit (
	patientMID, 
	visitDate, 
	locationID, 
	apptTypeID, 
	weight, 
	height,
	blood_pressure,
	household_smoking_status,
	patient_smoking_status,
	hdl,
	ldl,
	triglyceride) 
VALUES (106, "2013-01-02 00:00:00", 1, 1, 0.5, 60, '160/100', 1, 4, 37, 141, 162);

INSERT INTO officevisit (
	patientMID, 
	visitDate, 
	locationID, 
	apptTypeID, 
	weight, 
	height,
	blood_pressure,
	household_smoking_status,
	patient_smoking_status,
	hdl,
	ldl,
	triglyceride) 
VALUES (106, "2013-01-03 00:00:00", 1, 1, 92, 60, '160/100', 1, 4, 37, 141, 162);

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

INSERT INTO officevisit (
	patientMID, 
	visitDate, 
	locationID, 
	apptTypeID, 
	weight, 
	height,
	blood_pressure,
	household_smoking_status,
	patient_smoking_status,
	hdl,
	ldl,
	triglyceride) 
VALUES (107, "2013-01-01 00:00:00", 1, 1, 108.7, 60, '160/100', 1, 4, 37, 141, 162);

INSERT INTO officevisit (
	patientMID, 
	visitDate, 
	locationID, 
	apptTypeID, 
	weight, 
	height,
	blood_pressure,
	household_smoking_status,
	patient_smoking_status,
	hdl,
	ldl,
	triglyceride) 
VALUES (107, "2013-01-02 00:00:00", 1, 1, 92.5, 60, '160/100', 1, 4, 37, 141, 162);

INSERT INTO officevisit (
	patientMID, 
	visitDate, 
	locationID, 
	apptTypeID, 
	weight, 
	height,
	blood_pressure,
	household_smoking_status,
	patient_smoking_status,
	hdl,
	ldl,
	triglyceride) 
VALUES (107, "2013-01-03 00:00:00", 1, 1, 124.5, 60, '160/100', 1, 4, 37, 141, 162);

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

INSERT INTO officevisit (
	patientMID, 
	visitDate, 
	locationID, 
	apptTypeID, 
	weight, 
	height,
	blood_pressure,
	household_smoking_status,
	patient_smoking_status,
	hdl,
	ldl,
	triglyceride) 
VALUES (108, "2013-01-01 00:00:00", 1, 1, 137.5, 60, '160/100', 1, 4, 37, 141, 162);

INSERT INTO officevisit (
	patientMID, 
	visitDate, 
	locationID, 
	apptTypeID, 
	weight, 
	height,
	blood_pressure,
	household_smoking_status,
	patient_smoking_status,
	hdl,
	ldl,
	triglyceride) 
VALUES (108, "2013-01-02 00:00:00", 1, 1, 125, 60, '160/100', 1, 4, 37, 141, 162);

INSERT INTO officevisit (
	patientMID, 
	visitDate, 
	locationID, 
	apptTypeID, 
	weight, 
	height,
	blood_pressure,
	household_smoking_status,
	patient_smoking_status,
	hdl,
	ldl,
	triglyceride) 
VALUES (108, "2013-01-03 00:00:00", 1, 1, 149.5, 60, '160/100', 1, 4, 37, 141, 162);

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

INSERT INTO officevisit (
	patientMID, 
	visitDate, 
	locationID, 
	apptTypeID, 
	weight, 
	height,
	blood_pressure,
	household_smoking_status,
	patient_smoking_status,
	hdl,
	ldl,
	triglyceride) 
VALUES (109, "2013-01-01 00:00:00", 1, 1, 250, 60, '160/100', 1, 4, 37, 141, 162);

INSERT INTO officevisit (
	patientMID, 
	visitDate, 
	locationID, 
	apptTypeID, 
	weight, 
	height,
	blood_pressure,
	household_smoking_status,
	patient_smoking_status,
	hdl,
	ldl,
	triglyceride) 
VALUES (109, "2013-01-02 00:00:00", 1, 1, 150, 60, '160/100', 1, 4, 37, 141, 162);

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

INSERT INTO officevisit (
	patientMID, 
	visitDate, 
	locationID, 
	apptTypeID, 
	weight, 
	height,
	blood_pressure,
	household_smoking_status,
	patient_smoking_status,
	hdl,
	ldl,
	triglyceride) 
VALUES (110, "2003-01-01 00:00:00", 1, 1, NULL, 60, '160/100', 1, 4, 37, 141, 162);

INSERT INTO officevisit (
	patientMID, 
	visitDate, 
	locationID, 
	apptTypeID, 
	weight, 
	height,
	blood_pressure,
	household_smoking_status,
	patient_smoking_status,
	hdl,
	ldl,
	triglyceride) 
VALUES (110, "2009-12-31 00:00:00", 1, 1, 125, NULL, '160/100', 1, 4, 37, 141, 162);

INSERT INTO officevisit (
	patientMID, 
	visitDate, 
	locationID, 
	apptTypeID, 
	weight, 
	height,
	blood_pressure,
	household_smoking_status,
	patient_smoking_status,
	hdl,
	ldl,
	triglyceride) 
VALUES (110, "2010-01-01 00:00:00", 1, 1, 108.8, 60, '160/100', 1, 4, 37, 141, 162);

