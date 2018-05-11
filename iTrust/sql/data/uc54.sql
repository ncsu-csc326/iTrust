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

/***********************OFFICE VISITS******************************/
/* Weight Test - Weight Percentile EC */
INSERT INTO officevisits (id, visitDate, HCPID, notes, HospitalID, PatientID)
	VALUES 	(715, '2013-01-01', 9000000000, 'Weight Percentile EC', '1', 111),
			(716, '2013-01-02', 9000000000, 'Weight Percentile Bound (Low)', '1', 111),
			(717, '2013-01-03', 9000000000, 'Weight Percentile Bound (High)', '1', 111)
	ON DUPLICATE KEY UPDATE ID = ID;
	
/* Height Test - Height/Length Percentile EC */
INSERT INTO officevisits (id, visitDate, HCPID, notes, HospitalID, PatientID)
	VALUES 	(718, '2013-01-01', 9000000000, 'Height Percentile EC', '1', 112),
			(719, '2013-01-02', 9000000000, 'Height Percentile Bound (Low)', '1', 112),
			(720, '2013-01-03', 9000000000, 'Height Percentile Bound (High)', '1', 112)
	ON DUPLICATE KEY UPDATE ID = ID;
	
/* HeadCirc Test - Head Circumference Percentile EC */
INSERT INTO officevisits (id, visitDate, HCPID, notes, HospitalID, PatientID)
	VALUES 	(721, '2002-01-01', 9000000000, 'Head Circumference Percentile EC', '1', 113),
			(722, '2002-01-02', 9000000000, 'Head Circumference Percentile Bound (Low)', '1', 113),
			(723, '2002-01-03', 9000000000, 'Head Circumference Percentile Bound (High)', '1', 113)
	ON DUPLICATE KEY UPDATE ID = ID;
	
/* BMI Test - BMI Percentile EC */
INSERT INTO officevisits (id, visitDate, HCPID, notes, HospitalID, PatientID)
	VALUES 	(724, '2013-01-01', 9000000000, 'BMI Percentile EC', '1', 114),
			(725, '2013-01-02', 9000000000, 'BMI Percentile Bound (Low)', '1', 114),
			(726, '2013-01-03', 9000000000, 'BMI Percentile Bound (High)', '1', 114)
	ON DUPLICATE KEY UPDATE ID = ID;
	
/* Weight Age - Weight Age Bounds */
INSERT INTO officevisits (id, visitDate, HCPID, notes, HospitalID, PatientID)
	VALUES 	(727, '2000-01-02', 9000000000, 'Weight Age Bound (Low)', '1', 115),
			(728, '2019-12-31', 9000000000, 'Weight Age Bound (High)', '1', 115),
			(729, '2020-01-01', 9000000000, 'Weight Age Bound (Exceeded)', '1', 115)
	ON DUPLICATE KEY UPDATE ID = ID;
	
/* Height Age - Height/Length Age Bounds */
INSERT INTO officevisits (id, visitDate, HCPID, notes, HospitalID, PatientID)
	VALUES 	(730, '2000-01-02', 9000000000, 'Length Age Bound (Low)', '1', 116),
			(731, '2019-12-31', 9000000000, 'Height Age Bound (High)', '1', 116),
			(732, '2020-01-01', 9000000000, 'Height Age Bound (Exceeded)', '1', 116)
	ON DUPLICATE KEY UPDATE ID = ID;
	
/* HeadCirc Age - Head Circumference Age Bounds */
INSERT INTO officevisits (id, visitDate, HCPID, notes, HospitalID, PatientID)
	VALUES 	(733, '2000-01-02', 9000000000, 'Head Circumference Age Bound (Low)', '1', 117),
			(734, '2002-12-31', 9000000000, 'Head Circumference Age Bound (High)', '1', 117)
	ON DUPLICATE KEY UPDATE ID = ID;
	
/* BMI Age - BMI Age Bounds */
INSERT INTO officevisits (id, visitDate, HCPID, notes, HospitalID, PatientID)
	VALUES 	(735, '2001-12-31', 9000000000, 'BMI Age Bound (Under)', '1', 118),
			(736, '2002-01-01', 9000000000, 'BMI Age Bound (Low)', '1', 118),
			(737, '2019-12-31', 9000000000, 'BMI Age Bound (High)', '1', 118),
			(738, '2020-01-01', 9000000000, 'BMI Age Bound (Exceeded)', '1', 118)
	ON DUPLICATE KEY UPDATE ID = ID;
	
/***********************HEALTH METRICS*****************************/
/* Weight Test - Weight Percentile EC */
insert into personalhealthinformation(PatientID,OfficeVisitID,HCPID,HeadCircumference,Weight,Height,BloodPressureN,BloodPressureD,Smoker,SmokingStatus,HouseholdSmokingStatus,CholesterolHDL,CholesterolLDL,CholesterolTri,AsOfDate,OfficeVisitDate,BMI)
	values	(111,715,9000000000,0,90,0,0,0,0,0,3,0,0,0,'2013-01-01 15:03:11','2013-01-01',-1),
			(111,716,9000000000,0,1,0,0,0,0,0,3,0,0,0,'2013-01-02 09:48:53','2013-01-02',-1),
			(111,717,9000000000,0,1000,0,0,0,0,0,3,0,0,0,'2013-01-03 10:20:48','2013-01-03',-1)
	on duplicate key update OfficeVisitID = OfficeVisitID;
	
/* Height Test - Height/Length Percentile EC */
insert into personalhealthinformation(PatientID,OfficeVisitID,HCPID,HeadCircumference,Weight,Height,BloodPressureN,BloodPressureD,Smoker,SmokingStatus,HouseholdSmokingStatus,CholesterolHDL,CholesterolLDL,CholesterolTri,AsOfDate,OfficeVisitDate,BMI)
	values	(112,718,9000000000,0,0,60,0,0,0,0,3,0,0,0,'2013-01-01 15:03:11','2013-01-01',-1),
			(112,719,9000000000,0,0,1,0,0,0,0,3,0,0,0,'2013-01-02 09:48:53','2013-01-02',-1),
			(112,720,9000000000,0,0,1000,0,0,0,0,3,0,0,0,'2013-01-03 10:20:48','2013-01-03',-1)
	on duplicate key update OfficeVisitID = OfficeVisitID;
	
/* HeadCirc Test - Head Circumference Percentile EC */
insert into personalhealthinformation(PatientID,OfficeVisitID,HCPID,HeadCircumference,Weight,Height,BloodPressureN,BloodPressureD,Smoker,SmokingStatus,HouseholdSmokingStatus,CholesterolHDL,CholesterolLDL,CholesterolTri,AsOfDate,OfficeVisitDate,BMI)
	values	(113,721,9000000000,17,0,0,0,0,0,0,3,0,0,0,'2002-01-01 15:03:11','2002-01-01',-1),
			(113,722,9000000000,1,0,0,0,0,0,0,3,0,0,0,'2002-01-02 09:48:53','2002-01-02',-1),
			(113,723,9000000000,1000,0,0,0,0,0,0,3,0,0,0,'2002-01-03 10:20:48','2002-01-03',-1)
	on duplicate key update OfficeVisitID = OfficeVisitID;
	
/* BMI Test - BMI Percentile EC */
insert into personalhealthinformation(PatientID,OfficeVisitID,HCPID,HeadCircumference,Weight,Height,BloodPressureN,BloodPressureD,Smoker,SmokingStatus,HouseholdSmokingStatus,CholesterolHDL,CholesterolLDL,CholesterolTri,AsOfDate,OfficeVisitDate,BMI)
	values	(114,724,9000000000,0,90,60,0,0,0,0,3,0,0,0,'2013-01-01 15:03:11','2013-01-01',17.58),
			(114,725,9000000000,0,1,200,0,0,0,0,3,0,0,0,'2013-01-02 09:48:53','2013-01-02',0.02),
			(114,726,9000000000,0,200,1,0,0,0,0,3,0,0,0,'2013-01-03 10:20:48','2013-01-03',140600)
	on duplicate key update OfficeVisitID = OfficeVisitID;
	
/* Weight Age - Weight Age Bounds */
insert into personalhealthinformation(PatientID,OfficeVisitID,HCPID,HeadCircumference,Weight,Height,BloodPressureN,BloodPressureD,Smoker,SmokingStatus,HouseholdSmokingStatus,CholesterolHDL,CholesterolLDL,CholesterolTri,AsOfDate,OfficeVisitDate,BMI)
	values	(115,727,9000000000,0,90,0,0,0,0,0,3,0,0,0,'2000-01-02 15:03:11','2000-01-02',-1),
			(115,728,9000000000,0,90,0,0,0,0,0,3,0,0,0,'2019-12-31 09:48:53','2019-12-31',-1),
			(115,729,9000000000,0,90,0,0,0,0,0,3,0,0,0,'2020-01-01 10:20:48','2020-01-01',-1)
	on duplicate key update OfficeVisitID = OfficeVisitID;
	
/* Height Age - Height Age Bounds */
insert into personalhealthinformation(PatientID,OfficeVisitID,HCPID,HeadCircumference,Weight,Height,BloodPressureN,BloodPressureD,Smoker,SmokingStatus,HouseholdSmokingStatus,CholesterolHDL,CholesterolLDL,CholesterolTri,AsOfDate,OfficeVisitDate,BMI)
	values	(116,730,9000000000,0,0,60,0,0,0,0,3,0,0,0,'2000-01-02 15:03:11','2000-01-02',-1),
			(116,731,9000000000,0,0,60,0,0,0,0,3,0,0,0,'2019-12-31 09:48:53','2019-12-31',-1),
			(116,732,9000000000,0,0,60,0,0,0,0,3,0,0,0,'2020-01-01 10:20:48','2020-01-01',-1)
	on duplicate key update OfficeVisitID = OfficeVisitID;
	
/* HeadCirc Age - Head Circumference Age Bounds */
insert into personalhealthinformation(PatientID,OfficeVisitID,HCPID,HeadCircumference,Weight,Height,BloodPressureN,BloodPressureD,Smoker,SmokingStatus,HouseholdSmokingStatus,CholesterolHDL,CholesterolLDL,CholesterolTri,AsOfDate,OfficeVisitDate,BMI)
	values	(117,733,9000000000,17,0,0,0,0,0,0,3,0,0,0,'2000-01-02 15:03:11','2000-01-02',-1),
			(117,734,9000000000,17,0,0,0,0,0,0,3,0,0,0,'2002-12-31 09:48:53','2002-12-31',-1)
	on duplicate key update OfficeVisitID = OfficeVisitID;
	
/* BMI Age - BMI Age Bounds */
insert into personalhealthinformation(PatientID,OfficeVisitID,HCPID,HeadCircumference,Weight,Height,BloodPressureN,BloodPressureD,Smoker,SmokingStatus,HouseholdSmokingStatus,CholesterolHDL,CholesterolLDL,CholesterolTri,AsOfDate,OfficeVisitDate,BMI)
	values	(118,735,9000000000,0,90,60,0,0,0,0,3,0,0,0,'2001-12-31 15:03:11','2001-12-31',17.580),
			(118,736,9000000000,0,90,60,0,0,0,0,3,0,0,0,'2002-01-01 09:48:53','2002-01-01',17.58),
			(118,737,9000000000,0,90,60,0,0,0,0,3,0,0,0,'2019-12-31 10:20:48','2019-12-31',17.58),
			(118,738,9000000000,0,90,60,0,0,0,0,3,0,0,0,'2020-01-01 10:20:48','2020-01-01',17.58)
	on duplicate key update OfficeVisitID = OfficeVisitID;