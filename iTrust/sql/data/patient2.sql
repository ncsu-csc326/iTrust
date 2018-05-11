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

INSERT INTO declaredhcp(PatientID,HCPID) VALUE(2, 9000000003)
 ON DUPLICATE KEY UPDATE PatientID = PatientID;

INSERT INTO representatives(RepresenterMID, RepresenteeMID) VALUES(2,1)
 ON DUPLICATE KEY UPDATE RepresenterMID = RepresenterMID;
