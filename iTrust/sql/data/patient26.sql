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
26,
'Philip',
'Fry',
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
'1986-2-15',
0,
0,
'O-',
'Caucasian',
'Male',
"Crispy"
)  ON DUPLICATE KEY UPDATE MID = MID;

INSERT INTO users(MID, password, role, sQuestion, sAnswer) 
			VALUES (26, '30c952fab122c3f9759f02a6d95c3758b246b4fee239957b2d4fee46e26170c4', 'patient', 'how you doin?', 'good')
 ON DUPLICATE KEY UPDATE MID = MID;
 /*password: pw*/

INSERT INTO declaredhcp(PatientID,HCPID) VALUE(26, 9000000010)
 ON DUPLICATE KEY UPDATE PatientID = PatientID;

INSERT INTO transactionlog(loggedInMID, secondaryMID, transactionCode, timeLogged, addedInfo) 
                    VALUES (9000000000, 24, 410, '2007-06-23 06:54:59','ASDF'),
                           (9000000000, 24, 410, '2007-06-23 06:54:59','ASDF'),
                           (9000000000, 24, 1900,'2007-06-23 06:55:59','Viewed patient records'),
                           (9000000007, 24, 1900,'2007-06-23 06:54:59','Viewed patient records'),
                           (9000000007, 24, 1900,'2007-06-25 06:54:59','Viewed patient records'),
                           (9000000009, 24, 1900,'2007-06-24 06:54:59','Viewed patient records'),
                           (9000000000, 24, 3400,'2011-06-24 06:54:59','Patient added to monitoring list'),
                           (9000000009, 24, 1900,'2007-06-22 06:54:59','Viewed patient records');
