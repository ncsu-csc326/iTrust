/*Insert patient Sandy Sky*/
INSERT INTO patients
(MID, 
firstName,
lastName, 
email,
address1,
address2,
city,
state,
zip,
phone,
eName,
ePhone,
iCName,
iCAddress1,
iCAddress2,
iCCity, 
ICState,
iCZip,
iCPhone,
iCID,
DateOfBirth,
DateOfDeath,
CauseOfDeath,
MotherMID,
FatherMID,
BloodType,
Ethnicity,
Gender,
TopicalNotes
)
VALUES (
201,
'Sandy',
'Sky',
'sandy.sky@gmail.com',
'123 Sky Street',
'',
'Raleigh',
'NC',
'27607',
'123-456-7890',
'Susan Sky-Walker',
'444-332-4309',
'IC',
'Street1',
'Street2',
'City',
'PA',
'12345-6789',
'555-555-5555',
'1',
DATE(NOW()-INTERVAL 24 YEAR),
NULL,
'',
0,
0,
'O-',
'Caucasian',
'Male',
'Will save the universe, please protect'
)  ON DUPLICATE KEY UPDATE MID = MID;

INSERT INTO users(MID, password, role, sQuestion, sAnswer) 
			VALUES (201, '30c952fab122c3f9759f02a6d95c3758b246b4fee239957b2d4fee46e26170c4', 'patient', '2+2?', '4')
 ON DUPLICATE KEY UPDATE MID = MID;
 /*password: pw*/
 
 /* Office visits for Sandy Sky */
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
VALUES (201, DATE(NOW()-INTERVAL 1 YEAR), 1, 1, 180, 70, '100/70', 1, 4, 40, 81, 105);

set @ov_year_ago = LAST_INSERT_ID();

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
VALUES (201, DATE(NOW()-INTERVAL 6 MONTH), 1, 1, 178, 70, '105/68', 1, 4, 45, 81, 105);

set @ov_6months_ago = LAST_INSERT_ID();

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
VALUES (201, DATE(NOW()-INTERVAL 1 WEEK), 1, 1, 170, 70, '100/68', 1, 4, 45, 81, 105);

set @ov_week_ago = LAST_INSERT_ID();


/* Actual prescriptions for Sandy Sky */
INSERT INTO prescription(
	patientMID,
	drugCode,
	startDate,
	endDate,
	officeVisitId,
	hcpMID)
VALUES (201, "48301-3420", DATE(NOW()-INTERVAL 6 MONTH), DATE(NOW()-INTERVAL 60 DAY), @ov_6months_ago, 8000000011);

INSERT INTO prescription(
	patientMID,
	drugCode,
	startDate,
	endDate,
	officeVisitId,
	hcpMID)
VALUES (201, "05730-150", DATE(NOW()-INTERVAL 6 MONTH), DATE(NOW()-INTERVAL 5 MONTH), @ov_6months_ago, 8000000011);

INSERT INTO prescription(
	patientMID,
	drugCode,
	startDate,
	endDate,
	officeVisitId,
	hcpMID)
VALUES (201, "63739-291", DATE(NOW()-INTERVAL 1 WEEK), DATE(NOW()+INTERVAL 1 WEEK), @ov_week_ago, 8000000011);

/* ICD codes for Sandy Sky */
INSERT INTO icdCode (code, name, is_chronic)
VALUES ('J45', 'Asthma', 1);
INSERT INTO icdCode (code, name, is_chronic)
VALUES ('J12', 'Viral Pneumonia', 0);
INSERT INTO icdCode (code, name, is_chronic)
VALUES ('J00', 'Acute Nasopharyngitis', 0);

/* Diagnoses for Sandy Sky */

INSERT INTO diagnosis (visitId, icdCode)
VALUES (@ov_year_ago, 'J45'), (@ov_6months_ago, 'J12'), (@ov_week_ago, 'J00');

/* Immunization for Sandy Sky */
INSERT INTO cptCode (Code, name) VALUES ('90715', 'TDAP');
INSERT INTO immunization (visitId, cptCode) VALUES (@ov_year_ago, '90715');

/* Allergies for Sandy Sky */
INSERT INTO allergies(PatientID,Code,Description, FirstFound) 
	VALUES (201, '891671548', 'Pollen', '2016-05-05'),
	       (201, '664662530', 'Penicillin', '2016-06-04');

	       
/*insert sarah sky*/
INSERT INTO patients
(MID, 
firstName,
lastName, 
email,
address1,
address2,
city,
state,
zip,
phone,
eName,
ePhone,
iCName,
iCAddress1,
iCAddress2,
iCCity, 
ICState,
iCZip,
iCPhone,
iCID,
DateOfBirth,
DateOfDeath,
CauseOfDeath,
MotherMID,
FatherMID,
BloodType,
Ethnicity,
Gender,
TopicalNotes
)
VALUES (
202,
'Sarah',
'Sky',
'sarah.sky@gmail.com',
'123 Sky Street',
'',
'Raleigh',
'NC',
'27607',
'123-456-7890',
'Susan Sky-Walker',
'444-332-4309',
'IC',
'Street1',
'Street2',
'City',
'PA',
'12345-6789',
'555-555-5555',
'1',
DATE(NOW()-INTERVAL 1 MONTH),
NULL,
'',
0,
0,
'O+',
'Caucasian',
'Female',
'Will save the universe, please protect'
)  ON DUPLICATE KEY UPDATE MID = MID;

INSERT INTO users(MID, password, role, sQuestion, sAnswer) 
			VALUES (202, '30c952fab122c3f9759f02a6d95c3758b246b4fee239957b2d4fee46e26170c4', 'patient', '2+2?', '4')
 ON DUPLICATE KEY UPDATE MID = MID;

/*office visit for sarah sky*/
INSERT INTO officevisit (
	patientMID,
	visitDate,
	locationID,
	apptTypeID,
	notes,
	length,
	weight,
	household_smoking_status)
VALUES (202, DATE(NOW()-INTERVAL 1 WEEK), 9191919191, 6, "Patient had trouble breathing", 20, 10, 1);

set @ov_id2 = LAST_INSERT_ID();

/*diagnosis visit for sarah sky*/
INSERT INTO diagnosis (visitId, icdCode)
VALUES (@ov_id2, 'J00');

/* prescriptions for Sarah Sky */
INSERT INTO prescription(
	patientMID,
	drugCode,
	startDate,
	endDate,
	officeVisitId,
	hcpMID)
VALUES (202, "63739-291", DATE(NOW()-INTERVAL 1 WEEK), DATE(NOW()+INTERVAL 1 WEEK), @ov_id2, 8000000011);


/*Insert patient Thane Ross*/
INSERT INTO patients
(MID, 
firstName,
lastName, 
email,
address1,
address2,
city,
state,
zip,
phone,
eName,
ePhone,
iCName,
iCAddress1,
iCAddress2,
iCCity, 
ICState,
iCZip,
iCPhone,
iCID,
DateOfBirth,
DateOfDeath,
CauseOfDeath,
MotherMID,
FatherMID,
BloodType,
Ethnicity,
Gender,
TopicalNotes
)
VALUES (
105,
'Thane',
'Ross',
'thane.ross@gmail.com',
'123 Ross Ct',
'',
'Asheville',
'NC',
'28803',
'888-888-8888',
'Mister Ross',
'888-888-8881',
'IC',
'Street1',
'Street2',
'City',
'PA',
'12345-6789',
'555-555-5555',
'1',
'1989-01-03',
NULL,
'',
NULL,
NULL,
'O-',
'Caucasian',
'Male',
'This guy is a hippy.'
)  ON DUPLICATE KEY UPDATE MID = MID;

INSERT INTO users(MID, password, role, sQuestion, sAnswer) 
			VALUES (105, '30c952fab122c3f9759f02a6d95c3758b246b4fee239957b2d4fee46e26170c4', 'patient', '2+2?', '4')
 ON DUPLICATE KEY UPDATE MID = MID;
 
 
 
 INSERT INTO ndcodes (Code, Description)
 VALUES ('48301-3420', 'Midichlomaxene')
  ON DUPLICATE KEY UPDATE Code = Code;
 
 
 

