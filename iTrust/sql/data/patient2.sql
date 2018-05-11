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

DELETE FROM allergies WHERE PatientID = 2;
INSERT INTO allergies(PatientID,Code,Description, FirstFound) 
	VALUES (2, '', 'Pollen', '2007-06-05 20:33:58'),
	       (2, '664662530', 'Penicillin', '2007-06-04 20:33:58');
	       
INSERT INTO ndcodes(Code, Description) VALUES
('664662530','Penicillin')
ON DUPLICATE KEY UPDATE Code = Code;

DELETE FROM personalhealthinformation WHERE PatientID = 2;
INSERT INTO personalhealthinformation
(PatientID,OfficeVisitID,Height,Weight,Smoker,SmokingStatus,BloodPressureN,BloodPressureD,CholesterolHDL,CholesterolLDL,CholesterolTri,HCPID, AsOfDate,OfficeVisitDate,BMI)
VALUES ( 2,  961, 60,   200,   0, 9,     190,          100,           500,             239,         290,          9000000000, '2007-06-07 20:33:58','2007-06-07',39.06),
	   ( 2,  962, 62,   210,   1, 5,     195,          250,             36,             215,           280,          9000000000, '2007-06-07 20:34:58','2007-06-07',38.41)
	   on duplicate key update OfficeVisitID = OfficeVisitID;


INSERT INTO officevisits(id,visitDate,HCPID,notes,HospitalID,PatientID)
VALUES (952,'2007-6-09',9000000000,'Yet another office visit.','1',2),
	   (953,'2005-10-10',9000000000,'Yet another office visit.','1',2),
	   (954,'2005-10-10',9000000000,'Yet another office visit.','1',2),
	   (955,'2007-6-10',9000000000,'Yet another office visit.','1',2),
	   (956,'2005-10-10',9000000000,'Yet another office visit.','1',2),
	   (957,'2005-10-10',9000000000,'Yet another office visit.','1',2),
	   (958,'2005-10-10',9000000000,'Yet another office visit.','1',2),
	   (959,'2006-10-10',9000000000,'Yet another office visit.','1',2),
	   (960,'1985-10-10',9000000000,'Yet another office visit.','',2),
	   (961,'2007-06-07',9000000000,'Yet another office visit.','',2),
	   (962,'2007-06-07',9000000000,'Yet another office visit.','',2)
		 ON DUPLICATE KEY UPDATE id = id;

INSERT INTO ovdiagnosis(ID, ICDCode, VisitID) 
	VALUES  (100, 250.1, 955),
			(101, 79.30, 960),
			(102, 250.1, 960),
			(103, 250.1, 960),
			(104, 11.4, 960),
			(105, 15.00, 960)
			 ON DUPLICATE KEY UPDATE id = id;

DELETE FROM ovmedication WHERE VisitID = 955;
INSERT INTO ovmedication(NDCode, VisitID, StartDate,EndDate,Dosage,Instructions)
	VALUES ('009042407', 955, '2006-10-10', '2006-10-11', 5, 'Take twice daily'),
		   ('009042407', 955, '2006-10-10', '2006-10-11', 5, 'Take twice daily'),
		   ('647641512', 955, '2006-10-10', '2020-10-11', 5, 'Take twice daily');


DELETE FROM ovprocedure WHERE VisitID = 955;
INSERT INTO ovprocedure(ID, VisitID, CPTCode) VALUES (955, 955, '1270F');

INSERT INTO declaredhcp(PatientID,HCPID) VALUE(2, 9000000003)
 ON DUPLICATE KEY UPDATE PatientID = PatientID;

INSERT INTO representatives(RepresenterMID, RepresenteeMID) VALUES(2,1)
 ON DUPLICATE KEY UPDATE RepresenterMID = RepresenterMID;

INSERT INTO ovsurvey (VisitID, SurveyDate, WaitingRoomMinutes, ExamRoomMinutes, VisitSatisfaction, TreatmentSatisfaction)
	VALUES (952, '2008-03-01 12:00:00', null, 20, 1, 5),
		   (954, '2008-03-02 13:00:00', 25, 55, 2, 4)
			 ON DUPLICATE KEY UPDATE VisitID = VisitID;
