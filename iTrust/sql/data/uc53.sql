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

/***********************OFFICE VISITS******************************/
/* Tom Nook - Underweight EC */
INSERT INTO officevisits (id, visitDate, HCPID, notes, HospitalID, PatientID)
	VALUES 	(701, '2013-01-01', 9000000000, 'Underweight EC', '1', 106),
			(702, '2013-01-02', 9000000000, 'Underweight BL Low', '1', 106),
			(703, '2013-01-03', 9000000000, 'Underweight BL High', '1', 106)
	ON DUPLICATE KEY UPDATE ID = ID;

/* Hannah Montana - Normal EC */
INSERT INTO officevisits (id, visitDate, HCPID, notes, HospitalID, PatientID)
	VALUES	(704, '2013-01-01', 9000000000, 'Normal EC', '1', 107),
			(705, '2013-01-02', 9000000000, 'Normal BL Low', '1', 107),
			(706, '2013-01-03', 9000000000, 'Normal BL High', '1', 107)
	ON DUPLICATE KEY UPDATE ID = ID;
	
/* Hank Hill - Overweight EC */
INSERT INTO officevisits (id, visitDate, HCPID, notes, HospitalID, PatientID)
	VALUES	(707, '2013-01-01', 9000000000, 'Overweight EC', '1', 108),
			(708, '2013-01-02', 9000000000, 'Overweight BL Low', '1', 108),
			(709, '2013-01-03', 9000000000, 'Overweight BL High', '1', 108)
	ON DUPLICATE KEY UPDATE ID = ID;

/* Snorlax - Obese EC */
INSERT INTO officevisits (id, visitDate, HCPID, notes, HospitalID, PatientID)
	VALUES 	(710, '2013-01-01', 9000000000, 'Obese EC', '1', 109),
			(711, '2013-01-02', 9000000000, 'Obese BL Low', '1', 109)
	ON DUPLICATE KEY UPDATE ID = ID;
	
/* Bart Simpson - Other Cases */
INSERT INTO officevisits (id, visitDate, HCPID, notes, HospitalID, PatientID)
	VALUES	(712, '2003-01-01', 9000000000, 'Under 20 yrs EC', '1', 110),
			(713, '2009-12-31', 9000000000, 'Under 20 yrs BL High', '1', 110),
			(714, '2010-01-01', 9000000000, 'Over 20 yrs BL Low', '1', 110)
	ON DUPLICATE KEY UPDATE ID = ID;
	
/***********************HEALTH METRICS*****************************/
/* Tom Nook - Underweight EC */
insert into personalhealthinformation(PatientID,OfficeVisitID,HCPID,HeadCircumference,Weight,Height,BloodPressureN,BloodPressureD,Smoker,SmokingStatus,HouseholdSmokingStatus,CholesterolHDL,CholesterolLDL,CholesterolTri,AsOfDate,OfficeVisitDate,BMI)
	values	(106,701,9000000000,0,46.09,60,0,0,0,0,3,0,0,0,'2013-01-01 15:03:11','2013-01-01',9.00),
			(106,702,9000000000,0,0.5,60,0,0,0,0,3,0,0,0,'2013-01-02 09:48:53','2013-01-02',0.10),
			(106,703,9000000000,0,94.2,60,0,0,0,0,3,0,0,0,'2013-01-03 10:20:48','2013-01-03',12.54)
	on duplicate key update OfficeVisitID = OfficeVisitID;
	
/* Hannah Montana - Normal EC */
insert into personalhealthinformation(PatientID,OfficeVisitID,HCPID,HeadCircumference,Weight,Height,BloodPressureN,BloodPressureD,Smoker,SmokingStatus,HouseholdSmokingStatus,CholesterolHDL,CholesterolLDL,CholesterolTri,AsOfDate,OfficeVisitDate,BMI)
	values	(107,704,9000000000,0,77.35,50,0,0,0,0,3,0,0,0,'2013-01-01 15:03:11','2013-01-01',21.75),
			(107,705,9000000000,0,95.7,60.3,0,0,0,0,3,0,0,0,'2013-01-02 09:48:53','2013-01-02',18.69),
			(107,706,9000000000,0,127.5,60,0,0,0,0,3,0,0,0,'2013-01-03 10:20:48','2013-01-03',24.90)
	on duplicate key update OfficeVisitID = OfficeVisitID;

/* Hank Hill - Overweight EC */
insert into personalhealthinformation(PatientID,OfficeVisitID,HCPID,HeadCircumference,Weight,Height,BloodPressureN,BloodPressureD,Smoker,SmokingStatus,HouseholdSmokingStatus,CholesterolHDL,CholesterolLDL,CholesterolTri,AsOfDate,OfficeVisitDate,BMI)
	values	(108,707,9000000000,0,191.7,70,0,0,0,0,3,0,0,0,'2013-01-01 15:03:11','2013-01-01',27.50),
			(108,708,9000000000,0,128,60,0,0,0,0,3,0,0,0,'2013-01-02 09:48:53','2013-01-02',25.00),
			(108,709,9000000000,0,153.1,60,0,0,0,0,3,0,0,0,'2013-01-03 10:20:48','2013-01-03',29.90)
	on duplicate key update OfficeVisitID = OfficeVisitID;
	
/* Snorlax - Obese EC */
insert into personalhealthinformation(PatientID,OfficeVisitID,HCPID,HeadCircumference,Weight,Height,BloodPressureN,BloodPressureD,Smoker,SmokingStatus,HouseholdSmokingStatus,CholesterolHDL,CholesterolLDL,CholesterolTri,AsOfDate,OfficeVisitDate,BMI)
	values	(109,710,9000000000,0,455.2,80,0,0,0,0,3,0,0,0,'2013-01-01 15:03:11','2013-01-01',50.00),
			(109,711,9000000000,0,209.1,70,0,0,0,0,3,0,0,0,'2013-01-02 09:48:53','2013-01-02',30.00)
	on duplicate key update OfficeVisitID = OfficeVisitID;
	
/* Bart Simpson - Other Cases */
insert into personalhealthinformation(PatientID,OfficeVisitID,HCPID,HeadCircumference,Weight,Height,BloodPressureN,BloodPressureD,Smoker,SmokingStatus,HouseholdSmokingStatus,CholesterolHDL,CholesterolLDL,CholesterolTri,AsOfDate,OfficeVisitDate,BMI)
	values	(110,712,9000000000,0,77.4,50,0,0,0,0,3,0,0,0,'2003-01-01 15:03:11','2003-01-01',21.76),
			(110,713,9000000000,0,77.4,50,0,0,0,0,3,0,0,0,'2009-12-31 09:48:53','2009-12-31',21.76),
			(110,714,9000000000,0,77.4,50,0,0,0,0,3,0,0,0,'2010-01-01 10:20:48','2010-01-01',21.76)
	on duplicate key update OfficeVisitID = OfficeVisitID;