/*Insert patient 26*/
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
26,
'Fry',
'Philip',
'fryp@fakencsu.edu',
'2870 Gorgon Drive',
'',
'Raleigh',
'NC',
'27603',
'525-455-5654',
'Jumbo the Clown',
'515-551-5551',
'IC2',
'Street3',
'Street4',
'Downtown',
'GA',
'19023-2735',
'559-595-5995',
'4',
DATE(NOW()-INTERVAL 24 YEAR),
NULL,
'',
0,
0,
'O-',
'Caucasian',
'Male',
'Crispy'
)  ON DUPLICATE KEY UPDATE MID = MID;

INSERT INTO users(MID, password, role, sQuestion, sAnswer) 
			VALUES (26, '30c952fab122c3f9759f02a6d95c3758b246b4fee239957b2d4fee46e26170c4', 'patient', '2+2?', '4')
 ON DUPLICATE KEY UPDATE MID = MID;
 /*password: pw*/
 
/*insert office visit for patient 26*/
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
VALUES (26, "2015-10-24 00:00:00", 9191919191, 1, "Odd symptoms. Concerned about environmental hazards at work.", 163, 69, '110/70', 1, 4, 60, 81, 110);

set @ov_id = LAST_INSERT_ID();

/*Insert lab procs for above office visit*/
INSERT INTO labProcedure (
	labTechnicianID,
	officeVisitID,
	labProcedureCode,
	priority,
	isRestricted,
	status,
	commentary,
	results,
	updatedDate,
	confidenceIntervalLower,
	confidenceIntervalUpper,
	hcpMID
) VALUES (
	5000000002,
	@ov_id,
	"34667-6",
	2,
	true,
	2,
	"In transit status",
	"No results yet",
	"2014-10-24 0:00:00",
	91,
	92,
	9000000001
);

INSERT INTO labProcedure (
	labTechnicianID,
	officeVisitID,
	labProcedureCode,
	priority,
	isRestricted,
	status,
	commentary,
	results,
	updatedDate,
	confidenceIntervalLower,
	confidenceIntervalUpper,
	hcpMID
) VALUES (
	5000000002,
	@ov_id,
	"40772-6",
	1,
	true,
	2,
	"In testing status",
	"No results yet",
	"2014-10-24 0:00:00",
	91,
	92,
	9000000001
), (5000000003, @ov_id, "29588-1", 2, true, 5, "status", "5", "2014-10-24 0:00:00", 25, 28, 9000000001);

/*insert diagnosis for above office visit*/
INSERT INTO diagnosis (visitId, icdCode)
VALUES (@ov_id, 'S60.7');
	

/*Insert patient 22*/
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
22,
'Bear',
'Fozzie',
'wakka.wakka@wakka.com',
'344 Henson Street',
'',
'New York',
'NY',
'10001',
'555-555-5555',
'Kermit the Frog',
'555-555-5551',
'IC',
'Street1',
'Street2',
'City',
'PA',
'19003-2715',
'555-555-5555',
'1',
DATE(NOW()-INTERVAL 34 YEAR),
NULL,
'',
0,
0,
'O-',
'Ursine',
'Male',
'Fozzie has a low tolerance for pain--he simply cant bear it.  Wakka, Wakka, Wakka.'
)  ON DUPLICATE KEY UPDATE MID = MID;

INSERT INTO users(MID, password, role, sQuestion, sAnswer) 
			VALUES (22, '30c952fab122c3f9759f02a6d95c3758b246b4fee239957b2d4fee46e26170c4', 'patient', '2+2?', '4')
 ON DUPLICATE KEY UPDATE MID = MID;
 /*password: pw*/


/*office visit for patient 22*/
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
VALUES (22, "2015-10-01 00:00:00", 1, 1, "foo", 100, 63, '102/72', 1, 4, 40, 81, 105);

set @ov_id2 = LAST_INSERT_ID();
/*Insert lab procs for above office visit*/
INSERT INTO labProcedure (labTechnicianID, officeVisitID, labProcedureCode,	priority, isRestricted,	status,	commentary,	results, updatedDate,
	confidenceIntervalLower,
	confidenceIntervalUpper,
	hcpMID
) VALUES (5000000003, @ov_id2, "34667-6", 2, true, 5, "This is concerning", "50", "2015-10-01 0:00:00", 48, 52, 9000000001),
(5000000001, @ov_id2, "34667-6", 2, true, 1, "", "50", "2015-10-01 0:00:00", 48, 52, 9000000001);





/*Second office visit for patient 22*/
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
VALUES (22, "2015-10-08 00:00:00", 1, 6, "bar", 102, 63, '104/76', 1, 4, 45, 81, 105);

set @ov_id3 = LAST_INSERT_ID();

/*insert the 4 lab procedures for patient 22*/
INSERT INTO labProcedure (labTechnicianID, officeVisitID, labProcedureCode,	priority, isRestricted,	status, hcpMID)
VALUES (5000000002, @ov_id3, "5583-0", 1, true, 4, 9000000001),
(5000000003, @ov_id3, "5685-3", 1, true, 4, 9000000001), 
(5000000003, @ov_id3, "14807-2", 1, true, 4, 9000000001), 
(5000000003, @ov_id3, "12556-7", 2, true, 2, 9000000001);

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
VALUES (22, "2015-10-25 00:00:00", 1, 6, "I think Fozzie needs to use a different shampoo", 101, 63, '108/72', 1, 4, 50, 81, 105);

set @ov_id4 = LAST_INSERT_ID();


INSERT INTO labProcedure (labTechnicianID, officeVisitID, labProcedureCode,	priority, isRestricted,	status, hcpMID)
VALUES /*marker for the broken things*/
(5000000003, @ov_id4, "71950-0", 1, true, 2, 9000000001);


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
VALUES (22, "2015-10-09 00:00:00", 1, 6, "bar", 102, 63, '104/76', 1, 4, 45, 81, 105);

INSERT INTO loincCode (code, component, kind_of_property, time_aspect, system, scale_type, method_type)
values ('00000-1', 'blood test', 'fluid', 'urgent', NULL, NULL, NULL),
('00000-2', 'urine test', 'fluid', 'not urgent', NULL, NULL, NULL),
('29588-1', 'Heavy Metals Panel', '-', 'pt', 'Blood', NULL, NULL),
('34667-6', 'Heavy Metals Panel', '-', 'pt', 'Hair', NULL, NULL),
('5583-0', 'Arsenic', 'MCnc', 'pt', 'Blood', 'Qn', NULL),
('5685-3', 'Mercury', 'MCnc', 'pt', 'Blood', 'Qn', NULL),
('12556-7', 'Copper', 'MCnc', 'pt', 'Blood', 'Qn', NULL),
('14807-2', 'Lead', 'SCnc', 'pt', 'Blood', 'Qn', NULL),
('71950-0', 'Health Assessment Questionnaire', '-', 'pt', 'Patient', '-', 'HAQ'),
('40772-6', 'Heavy Metals Panel', '-', 'pt', 'Urine', NULL, NULL);



