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
MotherMID,
FatherMID,
BloodType,
Ethnicity,
Gender,
TopicalNotes
)
VALUES (
23,
'Dare',
'Devil',
'dare@devil.com',
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
'1976-04-25',
0,
0,
'O-',
'Caucasian',
'Male',
"Blind"
)  ON DUPLICATE KEY UPDATE MID = MID;

INSERT INTO users(MID, password, role, sQuestion, sAnswer) 
			VALUES (23, '30c952fab122c3f9759f02a6d95c3758b246b4fee239957b2d4fee46e26170c4', 'patient', 'how you doin?', 'good')
 ON DUPLICATE KEY UPDATE MID = MID;
/*password: pw*/

INSERT INTO officevisits(id,visitDate,HCPID,notes,HospitalID,PatientID)
VALUES (2952,'2010-6-09',9000000007,'Meep meep meep?','1',23),
	   (2953,'2010-10-31',9000000000,'If you say Wakka, Wakka, Wakka one more time...','1',23),
	   (2954,'2010-12-11',9000000003,'Wakka Wakka this!','1',23),
	   (2955,'2011-3-31',9000000007,'MEEP. Meep Meep.','1',23)
		 ON DUPLICATE KEY UPDATE id = id;

INSERT INTO declaredhcp(PatientID,HCPID) VALUE(23, 9000000007)
 ON DUPLICATE KEY UPDATE PatientID = PatientID;


INSERT INTO referrals (
    id,
    OVID,
    PatientID,
    SenderID,
    ReceiverID, 
    TimeStamp,
    PriorityCode,
    ReferralDetails, 
    viewed_by_patient,
    viewed_by_HCP
) 
VALUES 
    (29520, 2952, 23, 9000000007, 9000000000, '2010-06-10', 2, 'Meep meep meep.', false, false),
    (29521, 2952, 23, 9000000007, 9000000003, '2010-06-12', 3, 'Meep meep meep!', false, false),
    (29523, 2952, 23, 9000000007, 9000000000, '2010-06-14', 3, 'Meep Meep Meep.', false, false),
    (29523, 2953, 23, 9000000000, 9000000003, '2010-06-16', 2, 'Meep MEEP Meep...', false, false),
    (29524, 2953, 23, 9000000000, 9000000000, '2010-06-18', 1, 'meep meep meep', false, false),
    (29525, 2953, 23, 9000000000, 9000000003, '2010-06-20', 1, 'MEEP MEEP MEEP...', false, false),
    (29550, 2953, 23, 9000000000, 9000000000, '2010-06-23', 3, 'Meep meep... Meep.', false, false),
    (29551, 2953, 23, 9000000000, 9000000000, '2010-06-13', 2, 'Meep meep, meep.', false, false)
ON DUPLICATE KEY UPDATE id = id;

INSERT INTO transactionlog(loggedInMID, secondaryMID, transactionCode, timeLogged, addedInfo) 
                    VALUES (9000000000, 23, 400, '2007-06-23 06:54:59','Viewed patient demographics'),
                           (9000000000, 23, 410, '2007-06-23 06:54:59','Edited patient demographics'),
                           (9000000000, 23, 1900,'2007-06-23 06:55:59','Viewed patient records'),
                           (9000000007, 23, 400,'2007-06-23 06:54:59','Viewed patient records'),
                           (9000000007, 23, 410,'2007-06-25 06:54:59','Viewed patient records'),
                           (9000000007, 23, 1900,'2007-06-24 06:54:59','Viewed patient records'),
                           (9000000007, 23, 1900,'2007-06-22 06:54:59','Viewed patient records');
