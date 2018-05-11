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
 
DELETE FROM remotemonitoringlists where PatientMID = '42';
DELETE FROM remotemonitoringdata where PatientID = '42';

INSERT INTO remotemonitoringlists(PatientMID, HCPMID, systolicBloodPressure, diastolicBloodPressure)
					VALUES (42, 9000000000, 1, 1);

INSERT INTO appointment(doctor_id, patient_id, sched_date, appt_type)
	VALUE	(9000000000, 42, CONCAT(SUBDATE(CURDATE(),1), ' 15:00:00.0'), 'Consultation');
