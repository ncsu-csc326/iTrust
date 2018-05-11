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
(42,
'Horse', 
'Bad', 
'badHorse@ele.org', 
'1247 Noname Dr', 
'Suite 106', 
'Raleigh', 
'NC', 
'27606-1234', 
'919-123-4567', 
'Mommy Person', 
'704-123-4567', 
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
'Male',
'Beware his terrible Death Whinny!')
 ON DUPLICATE KEY UPDATE MID = MID;

INSERT INTO users(MID, password, role, sQuestion, sAnswer) 
			VALUES (42, '30c952fab122c3f9759f02a6d95c3758b246b4fee239957b2d4fee46e26170c4', 'patient', 'what is your favorite color?', 'blue')
 ON DUPLICATE KEY UPDATE MID = MID;
 /*password: pw*/

INSERT INTO declaredhcp(PatientID,HCPID) VALUE(1, 9000000000)
 ON DUPLICATE KEY UPDATE PatientID = PatientID;
 
INSERT INTO officevisits(ID, visitDate, HCPID, notes, PatientID, HospitalID)
	VALUES 	(5001, '2010-11-28', 9000000000, 'Voice is a little hoarse.', 42, 1),
			(5002, '2010-11-25', 9000000000, 'Another appointment', 42, 1),
			(5003, '2010-8-13', 9000000000, 'Another appointment', 42, 1),
			(5004, '2010-5-19', 9000000000, 'Another appointment', 42, 1),
			(5005, '2010-2-22', 9000000000, 'Another appointment', 42, 1),
			(5006, '2009-12-29', 9000000000, 'Another appointment', 42, 1),
			(5007, '2009-7-22', 9000000000, 'Another appointment', 42, 1),
			(5008, '2009-5-23', 9000000000, 'Another appointment', 42, 1),
			(5009, '2009-3-15', 9000000000, 'Another appointment', 42, 1),
			(5010, '2008-12-19', 9000000000, 'Another appointment', 42, 1),
			(5011, '2008-8-18', 9000000000, 'Another appointment', 42, 1),
			(5012, '2008-4-10', 9000000000, 'Another appointment', 42, 1),
			(5013, '2008-2-10', 9000000000, 'Another appointment', 42, 1)
	ON DUPLICATE KEY UPDATE ID = ID;
	
DELETE FROM remotemonitoringlists where PatientMID = '42';
DELETE FROM remotemonitoringdata where PatientID = '42';

INSERT INTO remotemonitoringlists(PatientMID, HCPMID, systolicBloodPressure, diastolicBloodPressure)
					VALUES (42, 9000000000, 1, 1);

DELETE FROM personalhealthinformation WHERE PatientID = 42;
INSERT INTO personalhealthinformation(PatientID, OfficeVisitID, Weight, Height, AsOfDate, Smoker, HCPID, OfficeVisitDate, BMI)
	VALUES	(42, 5002, 280, 70, '2010-11-25 05:30:00', 1, 9000000000, '2010-11-25',40.17),
			(42, 5003, 282, 70, '2010-8-13 05:30:00', 1, 9000000000, '2010-8-13',40.46),
			(42, 5004, 281.5, 70, '2010-5-19 05:30:00', 1, 9000000000, '2010-5-19',40.39),
			(42, 5005, 285, 70, '2010-2-22 05:30:00', 1, 9000000000, '2010-2-22',40.89),
			(42, 5006, 279.7, 70, '2009-12-29 05:30:00', 1, 9000000000, '2009-12-29',40.13),
			(42, 5007, 280, 70, '2009-7-22 05:30:00', 1, 9000000000, '2009-7-22',40.17),
			(42, 5008, 280, 70, '2009-5-23 05:30:00', 1, 9000000000, '2009-5-23',40.17),
			(42, 5009, 280, 70, '2009-3-15 05:30:00', 1, 9000000000, '2009-3-15',40.17),
			(42, 5010, 290.3, 70, '2008-12-19 05:30:00', 1, 9000000000, '2008-12-19',41.65),
			(42, 5011, 293.1, 70, '2008-8-18 05:30:00', 1, 9000000000, '2008-8-18',42.05),
			(42, 5012, 296, 70, '2008-4-10 05:30:00', 1, 9000000000, '2008-4-10',42.47),
			(42, 5013, 294.2, 70, '2008-2-10 05:30:00', 1, 9000000000, '2008-2-10',42.21)
on duplicate key update OfficeVisitID = OfficeVisitID;
	
INSERT INTO appointment(doctor_id, patient_id, sched_date, appt_type)
	VALUE	(9000000000, 42, CONCAT(SUBDATE(CURDATE(),1), ' 15:00:00.0'), 'Consultation');
