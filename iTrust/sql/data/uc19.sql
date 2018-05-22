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
2,
'Andy',
'Programmer',
'andy.programmer@gmail.com',
'344 Bob Street',
'',
'Raleigh',
'NC',
'27607',
'555-555-5555',
'Mr Emergency',
'555-555-5551',
'IC',
'Street1',
'Street2',
'City',
'PA',
'19003-2715',
'555-555-5555',
'1',
'1984-05-19',
'2005-03-10',
'250.10',
1,
0,
'O-',
'Caucasian',
'Male',
'This person is absolutely crazy. Do not touch them.'
)  ON DUPLICATE KEY UPDATE MID = MID;

INSERT INTO users(MID, password, role, sQuestion, sAnswer) 
			VALUES (2, '30c952fab122c3f9759f02a6d95c3758b246b4fee239957b2d4fee46e26170c4', 'patient', 'how you doin?', 'good')
 ON DUPLICATE KEY UPDATE MID = MID;
 /*password: pw*/
 
 
/*insert office visit for patient 2*/
INSERT INTO officevisit (
	patientMID, 
	visitDate, 
	locationID, 
	apptTypeID, 
	notes,
	weight, 
	height,
	blood_pressure,
	household_smoking_status,
	patient_smoking_status,
	hdl,
	ldl,
	triglyceride) 
VALUES (2, "2016-10-05 00:00:00", 9191919191, 6, "soon to be travelling abroad", 180, 73, '110/67', 1, 4, 40, 81, 105); 

set @ov_id = LAST_INSERT_ID();

INSERT INTO ndcodes(Code, Description) VALUES
('05730-150','Advil')
ON DUPLICATE KEY UPDATE code = code;

INSERT INTO prescription(
	patientMID,
	drugCode,
	startDate,
	endDate,
	instructions,
	officeVisitId,
	hcpMID)
VALUES (2, "05730-150", "2016-10-05", "2016-11-05", 'Take as needed', @ov_id, 8000000011);

INSERT INTO officevisit (
	patientMID, 
	visitDate, 
	locationID, 
	apptTypeID, 
	notes,
	weight, 
	height,
	blood_pressure,
	household_smoking_status,
	patient_smoking_status,
	hdl,
	ldl,
	triglyceride) 
VALUES (2, "2016-10-20 00:00:00", 9191919191, 1, "All good! needs prescription refills", 185, 73, '112/68', 1, 4, 40, 81, 105);

set @ov_id = LAST_INSERT_ID();

INSERT INTO ndcodes(Code, Description) VALUES
('63739-291','Oyster Shell Calcium with Vitamin D')
ON DUPLICATE KEY UPDATE code = code;
INSERT INTO ndcodes(Code, Description) VALUES
('00882-219','Lantus')
ON DUPLICATE KEY UPDATE code = code;
INSERT INTO ndcodes(Code, Description) VALUES
('48301-3420','Midichlomaxene')
ON DUPLICATE KEY UPDATE code = code;
 

INSERT INTO prescription(
	patientMID,
	drugCode,
	startDate,
	endDate,
	instructions,
	officeVisitId,
	hcpMID)
VALUES (2, "48301-3420", "2016-10-20", "2017-10-20", 'fill in one week if needed', @ov_id, 8000000011);

INSERT INTO prescription(
	patientMID,
	drugCode,
	startDate,
	endDate,
	instructions,
	officeVisitId,
	hcpMID)
VALUES (2, "63739-291", "2016-10-20", "2017-10-20", 'It cant hurt', @ov_id, 8000000011);

INSERT INTO prescription(
	patientMID,
	drugCode,
	startDate,
	endDate,
	instructions,
	officeVisitId,
	hcpMID)
VALUES (2, "00882-219", "2016-10-20", "2017-10-20", 'for treating diabetes', @ov_id, 8000000011);
 
/*insert patient 1 so she can be a health care representative*/
INSERT INTO patients
(MID, 
lastName, 
firstName,
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
dateofbirth,
mothermid,
fathermid,
bloodtype,
ethnicity,
gender, 
topicalnotes)
VALUES
(1,
'Person', 
'Random', 
'nobody@gmail.com', 
'1247 Noname Dr', 
'Suite 106', 
'Raleigh', 
'NC', 
'27606-1234', 
'919-971-0000', 
'Mommy Person', 
'704-532-2117', 
'Aetna', 
'1234 Aetna Blvd', 
'Suite 602', 
'Charlotte',
'NC', 
'28215', 
'704-555-1234', 
'ChetumNHowe', 
'1950-05-10',
0,
0,
'AB+',
'African American',
'Female',
'')
 ON DUPLICATE KEY UPDATE MID = MID;

INSERT INTO users(MID, password, role, sQuestion, sAnswer) 
			VALUES (1, '30c952fab122c3f9759f02a6d95c3758b246b4fee239957b2d4fee46e26170c4', 'patient', 'what is your favorite color?', 'blue')
 ON DUPLICATE KEY UPDATE MID = MID;
 /*password: pw*/ 
	 
INSERT INTO representatives(RepresenterMID, RepresenteeMID) VALUES
(2,1),
(2,201) 
ON DUPLICATE KEY UPDATE RepresenterMID = RepresenterMID;

 
 
 
 
 
 
 /*office visits for other patients so we can test representative prescription viewing*/
 INSERT INTO officevisit (
	patientMID, 
	visitDate, 
	locationID, 
	apptTypeID, 
	notes,
	weight, 
	height,
	blood_pressure,
	household_smoking_status,
	patient_smoking_status,
	hdl,
	ldl,
	triglyceride) 
VALUES (1, "2016-10-22 00:00:00", 9191919191, 1, "All good! needs prescription refills", 185, 73, '112/68', 1, 4, 40, 81, 105);

set @ov_id = LAST_INSERT_ID();

INSERT INTO prescription(
	patientMID,
	drugCode,
	startDate,
	endDate,
	officeVisitId,
	hcpMID)
VALUES (1, "48301-3420", "2016-10-20", "2017-10-20", @ov_id, 8000000011);


INSERT INTO officevisit (
	patientMID, 
	visitDate, 
	locationID, 
	apptTypeID, 
	notes,
	weight, 
	height,
	blood_pressure,
	household_smoking_status,
	patient_smoking_status,
	hdl,
	ldl,
	triglyceride) 
VALUES (3, "2016-10-22 00:00:00", 9191919191, 1, "All good! needs prescription refills", 185, 73, '112/68', 1, 4, 40, 81, 105);


set @ov_id = LAST_INSERT_ID();

INSERT INTO prescription(
	patientMID,
	drugCode,
	startDate,
	endDate,
	officeVisitId,
	hcpMID)
VALUES (3, "00882-219", "2016-10-20", "2017-10-20", @ov_id, 9000000000);


INSERT INTO officevisit (
	patientMID, 
	visitDate, 
	locationID, 
	apptTypeID, 
	notes,
	weight, 
	height,
	blood_pressure,
	household_smoking_status,
	patient_smoking_status,
	hdl,
	ldl,
	triglyceride) 
VALUES (5, "2016-10-22 00:00:00", 9191919191, 1, "All good! needs prescription refills", 185, 73, '112/68', 1, 4, 40, 81, 105);

set @ov_id = LAST_INSERT_ID();

INSERT INTO prescription(
	patientMID,
	drugCode,
	startDate,
	endDate,
	officeVisitId,
	hcpMID)
VALUES (5, "48301-3420", "2016-10-20", "2017-10-20", @ov_id, 9000000000);

set @ov_id = LAST_INSERT_ID();

INSERT INTO prescription(
	patientMID,
	drugCode,
	startDate,
	endDate,
	officeVisitId,
	hcpMID)
VALUES (5, "00882-219", "2016-10-21", "2017-10-20", @ov_id, 9000000000);


INSERT INTO officevisit (
	patientMID, 
	visitDate, 
	locationID, 
	apptTypeID, 
	notes,
	weight, 
	height,
	blood_pressure,
	household_smoking_status,
	patient_smoking_status,
	hdl,
	ldl,
	triglyceride) 
VALUES (6, "2016-10-22 00:00:00", 9191919191, 1, "All good! needs prescription refills", 185, 73, '112/68', 1, 4, 40, 81, 105);

set @ov_id = LAST_INSERT_ID();

INSERT INTO prescription(
	patientMID,
	drugCode,
	startDate,
	endDate,
	officeVisitId,
	hcpMID)
VALUES (6, "63739-291", "2016-10-21", "2017-10-20", @ov_id, 9000000000);

INSERT INTO prescription(
	patientMID,
	drugCode,
	startDate,
	endDate,
	officeVisitId,
	hcpMID)
VALUES (6, "48301-3420", "2016-10-20", "2017-10-20", @ov_id, 9000000001);

INSERT INTO officevisit (
	patientMID, 
	visitDate, 
	locationID, 
	apptTypeID, 
	notes,
	weight, 
	height,
	blood_pressure,
	household_smoking_status,
	patient_smoking_status,
	hdl,
	ldl,
	triglyceride) 
VALUES (8, "2016-10-22 00:00:00", 9191919191, 1, "All good! needs prescription refills", 185, 73, '112/68', 1, 4, 40, 81, 105);
	

set @ov_id = LAST_INSERT_ID();

INSERT INTO prescription(
	patientMID,
	drugCode,
	startDate,
	endDate,
	officeVisitId,
	hcpMID)
VALUES (8, "63739-291", "2016-10-21", "2017-10-20", @ov_id, 9000000000);

INSERT INTO prescription(
	patientMID,
	drugCode,
	startDate,
	endDate,
	officeVisitId,
	hcpMID)
VALUES (8, "48301-3420", "2016-10-20", "2017-10-20", @ov_id, 9000000001);
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
2,
'Andy',
'Programmer',
'andy.programmer@gmail.com',
'344 Bob Street',
'',
'Raleigh',
'NC',
'27607',
'555-555-5555',
'Mr Emergency',
'555-555-5551',
'IC',
'Street1',
'Street2',
'City',
'PA',
'19003-2715',
'555-555-5555',
'1',
'1984-05-19',
'2005-03-10',
'250.10',
1,
0,
'O-',
'Caucasian',
'Male',
'This person is absolutely crazy. Do not touch them.'
)  ON DUPLICATE KEY UPDATE MID = MID;

INSERT INTO users(MID, password, role, sQuestion, sAnswer) 
			VALUES (2, '30c952fab122c3f9759f02a6d95c3758b246b4fee239957b2d4fee46e26170c4', 'patient', 'how you doin?', 'good')
 ON DUPLICATE KEY UPDATE MID = MID;
 /*password: pw*/
 
 
/*insert office visit for patient 2*/
INSERT INTO officevisit (
	patientMID, 
	visitDate, 
	locationID, 
	apptTypeID, 
	notes,
	weight, 
	height,
	blood_pressure,
	household_smoking_status,
	patient_smoking_status,
	hdl,
	ldl,
	triglyceride) 
VALUES (2, "2016-10-05 00:00:00", 9191919191, 6, "soon to be travelling abroad", 180, 73, '110/67', 1, 4, 40, 81, 105); 

set @ov_id = LAST_INSERT_ID();

INSERT INTO ndcodes(Code, Description) VALUES
('05730-150','Advil')
ON DUPLICATE KEY UPDATE code = code;

 /*insert notes later I guess Take as needed*/
INSERT INTO prescription(
	patientMID,
	drugCode,
	startDate,
	endDate,
	officeVisitId,
	hcpMID)
VALUES (2, "05730-150", "2016-10-05", "2016-11-05", @ov_id, 8000000011);

INSERT INTO officevisit (
	patientMID, 
	visitDate, 
	locationID, 
	apptTypeID, 
	notes,
	weight, 
	height,
	blood_pressure,
	household_smoking_status,
	patient_smoking_status,
	hdl,
	ldl,
	triglyceride) 
VALUES (2, "2016-10-20 00:00:00", 9191919191, 1, "All good! needs prescription refills", 185, 73, '112/68', 1, 4, 40, 81, 105);

set @ov_id = LAST_INSERT_ID();

INSERT INTO ndcodes(Code, Description) VALUES
('63739-291','Oyster Shell Calcium with Vitamin D')
ON DUPLICATE KEY UPDATE code = code;
INSERT INTO ndcodes(Code, Description) VALUES
('00882-219','Lantus')
ON DUPLICATE KEY UPDATE code = code;
INSERT INTO ndcodes(Code, Description) VALUES
('48301-3420','Midichlomaxene')
ON DUPLICATE KEY UPDATE code = code;
 
 /*insert notes later I guess fill in one week if needed*/
INSERT INTO prescription(
	patientMID,
	drugCode,
	startDate,
	endDate,
	officeVisitId,
	hcpMID)
VALUES (2, "48301-3420", "2016-10-20", "2017-10-20", @ov_id, 8000000011);

 /*insert notes later I guess It can't hurt*/
INSERT INTO prescription(
	patientMID,
	drugCode,
	startDate,
	endDate,
	officeVisitId,
	hcpMID)
VALUES (2, "63739-291", "2016-10-20", "2017-10-20", @ov_id, 8000000011);

 /*insert notes later I guess for treating diabetes*/
INSERT INTO prescription(
	patientMID,
	drugCode,
	startDate,
	endDate,
	officeVisitId,
	hcpMID)
VALUES (2, "00882-219", "2016-10-20", "2017-10-20", @ov_id, 8000000011);
 
/*insert patient 1 so she can be a health care representative*/
INSERT INTO patients
(MID, 
lastName, 
firstName,
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
dateofbirth,
mothermid,
fathermid,
bloodtype,
ethnicity,
gender, 
topicalnotes)
VALUES
(1,
'Person', 
'Random', 
'nobody@gmail.com', 
'1247 Noname Dr', 
'Suite 106', 
'Raleigh', 
'NC', 
'27606-1234', 
'919-971-0000', 
'Mommy Person', 
'704-532-2117', 
'Aetna', 
'1234 Aetna Blvd', 
'Suite 602', 
'Charlotte',
'NC', 
'28215', 
'704-555-1234', 
'ChetumNHowe', 
'1950-05-10',
0,
0,
'AB+',
'African American',
'Female',
'')
 ON DUPLICATE KEY UPDATE MID = MID;

INSERT INTO users(MID, password, role, sQuestion, sAnswer) 
			VALUES (1, '30c952fab122c3f9759f02a6d95c3758b246b4fee239957b2d4fee46e26170c4', 'patient', 'what is your favorite color?', 'blue')
 ON DUPLICATE KEY UPDATE MID = MID;
 /*password: pw*/ 
	 
INSERT INTO representatives(RepresenterMID, RepresenteeMID) VALUES
(2,1),
(2,201) 
ON DUPLICATE KEY UPDATE RepresenterMID = RepresenterMID;

 
 
 
 
 
 
 /*office visits for other patients so we can test representative prescription viewing*/
 INSERT INTO officevisit (
	patientMID, 
	visitDate, 
	locationID, 
	apptTypeID, 
	notes,
	weight, 
	height,
	blood_pressure,
	household_smoking_status,
	patient_smoking_status,
	hdl,
	ldl,
	triglyceride) 
VALUES (1, "2016-10-22 00:00:00", 9191919191, 1, "All good! needs prescription refills", 185, 73, '112/68', 1, 4, 40, 81, 105);

set @ov_id = LAST_INSERT_ID();

INSERT INTO prescription(
	patientMID,
	drugCode,
	startDate,
	endDate,
	officeVisitId,
	hcpMID)
VALUES (1, "48301-3420", "2016-10-20", "2017-10-20", @ov_id, 8000000011);


INSERT INTO officevisit (
	patientMID, 
	visitDate, 
	locationID, 
	apptTypeID, 
	notes,
	weight, 
	height,
	blood_pressure,
	household_smoking_status,
	patient_smoking_status,
	hdl,
	ldl,
	triglyceride) 
VALUES (3, "2016-10-22 00:00:00", 9191919191, 1, "All good! needs prescription refills", 185, 73, '112/68', 1, 4, 40, 81, 105);


set @ov_id = LAST_INSERT_ID();

INSERT INTO prescription(
	patientMID,
	drugCode,
	startDate,
	endDate,
	officeVisitId,
	hcpMID)
VALUES (3, "00882-219", "2016-10-20", "2017-10-20", @ov_id, 9000000000);


INSERT INTO officevisit (
	patientMID, 
	visitDate, 
	locationID, 
	apptTypeID, 
	notes,
	weight, 
	height,
	blood_pressure,
	household_smoking_status,
	patient_smoking_status,
	hdl,
	ldl,
	triglyceride) 
VALUES (5, "2016-10-22 00:00:00", 9191919191, 1, "All good! needs prescription refills", 185, 73, '112/68', 1, 4, 40, 81, 105);

set @ov_id = LAST_INSERT_ID();

INSERT INTO prescription(
	patientMID,
	drugCode,
	startDate,
	endDate,
	officeVisitId,
	hcpMID)
VALUES (5, "48301-3420", "2016-10-20", "2017-10-20", @ov_id, 9000000000);

set @ov_id = LAST_INSERT_ID();

INSERT INTO prescription(
	patientMID,
	drugCode,
	startDate,
	endDate,
	officeVisitId,
	hcpMID)
VALUES (5, "00882-219", "2016-10-21", "2017-10-20", @ov_id, 9000000000);


INSERT INTO officevisit (
	patientMID, 
	visitDate, 
	locationID, 
	apptTypeID, 
	notes,
	weight, 
	height,
	blood_pressure,
	household_smoking_status,
	patient_smoking_status,
	hdl,
	ldl,
	triglyceride) 
VALUES (6, "2016-10-22 00:00:00", 9191919191, 1, "All good! needs prescription refills", 185, 73, '112/68', 1, 4, 40, 81, 105);

set @ov_id = LAST_INSERT_ID();

INSERT INTO prescription(
	patientMID,
	drugCode,
	startDate,
	endDate,
	officeVisitId,
	hcpMID)
VALUES (6, "63739-291", "2016-10-21", "2017-10-20", @ov_id, 9000000000);

INSERT INTO prescription(
	patientMID,
	drugCode,
	startDate,
	endDate,
	officeVisitId,
	hcpMID)
VALUES (6, "48301-3420", "2016-10-20", "2017-10-20", @ov_id, 9000000001);

INSERT INTO officevisit (
	patientMID, 
	visitDate, 
	locationID, 
	apptTypeID, 
	notes,
	weight, 
	height,
	blood_pressure,
	household_smoking_status,
	patient_smoking_status,
	hdl,
	ldl,
	triglyceride) 
VALUES (8, "2016-10-22 00:00:00", 9191919191, 1, "All good! needs prescription refills", 185, 73, '112/68', 1, 4, 40, 81, 105);
	

set @ov_id = LAST_INSERT_ID();

INSERT INTO prescription(
	patientMID,
	drugCode,
	startDate,
	endDate,
	officeVisitId,
	hcpMID)
VALUES (8, "63739-291", "2016-10-21", "2017-10-20", @ov_id, 9000000000);

INSERT INTO prescription(
	patientMID,
	drugCode,
	startDate,
	endDate,
	officeVisitId,
	hcpMID)
VALUES (8, "48301-3420", "2016-10-20", "2017-10-20", @ov_id, 9000000001);
 