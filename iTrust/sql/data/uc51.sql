/* Insert Central Hospital*/
INSERT INTO hospitals(HospitalID, HospitalName, Address, City, State, Zip) VALUES
('9','Central Hospital','12345 Central Pkwy','Greensboro','NC','27401')
ON DUPLICATE KEY UPDATE HospitalID = HospitalID;

/*Insert HCP Shelly Vang*/
INSERT INTO personnel(
MID,
AMID,
role,
lastName, 
firstName, 
address1,
address2,
city,
state,
zip,
phone,
specialty,
email)
VALUES (
8000000011,
null,
'hcp',
'Vang',
'Shelly',
'1234 Awesome St',
'Apt C',
'Greensboro',
'NC',
'27401',
'999-000-0000',
'general physician',
'svang@iTrust.org'
)ON DUPLICATE KEY UPDATE MID = MID;

INSERT INTO users(MID, password, role, sQuestion, sAnswer) VALUES(8000000011, '30c952fab122c3f9759f02a6d95c3758b246b4fee239957b2d4fee46e26170c4', 'hcp', '2+2?', '4')
ON DUPLICATE KEY UPDATE MID = MID;
/*password: pw*/

INSERT INTO hcpassignedhos(HCPID, HosID) VALUES(8000000011,'9')
ON DUPLICATE KEY UPDATE HCPID = HCPID;

/*Insert Streptococcal sore throat diagnosis*/
INSERT INTO icdcodes(Code, Description, Chronic, URL) VALUES
('034.0', 'Streptococcal sore throat', 'no','')  
ON DUPLICATE KEY UPDATE Code = Code;

/*Insert patient Brynn McClain*/
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
101,
'Brynn',
'McClain',
'brynn.mcclain@gmail.com',
'123 Brynn Street',
'',
'Raleigh',
'NC',
'27607',
'123-456-7890',
'Baby Brynn',
'123-456-7891',
'IC',
'Street1',
'Street2',
'City',
'PA',
'12345-6789',
'555-555-5555',
'1',
'2013-05-01',
NULL,
'',
NULL,
NULL,
'O-',
'Caucasian',
'Female',
'The future queen of england. Treat with care.'
)  ON DUPLICATE KEY UPDATE MID = MID;

INSERT INTO users(MID, password, role, sQuestion, sAnswer) 
			VALUES (101, '30c952fab122c3f9759f02a6d95c3758b246b4fee239957b2d4fee46e26170c4', 'patient', '2+2?', '4')
 ON DUPLICATE KEY UPDATE MID = MID;
 /*password: pw*/

/*Insert patient Caldwell Hudson*/
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
102,
'Caldwell',
'Hudson',
'caldwell.hudson@gmail.com',
'123 Caldwell Street',
'',
'Cary',
'NC',
'27513',
'111-111-1111',
'Baby Caldwell',
'111-111-1112',
'IC',
'Street1',
'Street2',
'City',
'PA',
'12345-6789',
'555-555-5555',
'1',
'2011-09-29',
NULL,
'',
NULL,
NULL,
'B+',
'Caucasian',
'Male',
'Future olympian. Treat with care.'
)  ON DUPLICATE KEY UPDATE MID = MID;

INSERT INTO users(MID, password, role, sQuestion, sAnswer) 
			VALUES (102, '30c952fab122c3f9759f02a6d95c3758b246b4fee239957b2d4fee46e26170c4', 'patient', '2+2?', '4')
 ON DUPLICATE KEY UPDATE MID = MID;
 /*password: pw*/

/*Insert patient Fulton Gray*/
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
103,
'Fulton',
'Gray',
'fulton.gray@gmail.com',
'123 85th Street',
'',
'New York City',
'NY',
'11223',
'222-222-2222',
'Kid Gray',
'222-222-2221',
'IC',
'Street1',
'Street2',
'City',
'PA',
'12345-6789',
'555-555-5555',
'1',
'2008-10-10',
NULL,
'',
NULL,
NULL,
'O+',
'Caucasian',
'Male',
'Just a normal kid from Brooklyn.'
)  ON DUPLICATE KEY UPDATE MID = MID;

INSERT INTO users(MID, password, role, sQuestion, sAnswer) 
			VALUES (103, '30c952fab122c3f9759f02a6d95c3758b246b4fee239957b2d4fee46e26170c4', 'patient', '2+2?', '4')
 ON DUPLICATE KEY UPDATE MID = MID;
 /*password: pw*/

/*Insert patient Daria Griffin*/
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
104,
'Daria',
'Griffin',
'daria.griffin@gmail.com',
'123 Dunder Street',
'',
'Scranton',
'PA',
'18503',
'333-333-3333',
'Miss Daria',
'333-333-3331',
'IC',
'Street1',
'Street2',
'City',
'PA',
'12345-6789',
'555-555-5555',
'1',
'1993-10-25',
NULL,
'',
NULL,
NULL,
'A+',
'Caucasian',
'Female',
'Related to Peter.'
)  ON DUPLICATE KEY UPDATE MID = MID;

INSERT INTO users(MID, password, role, sQuestion, sAnswer) 
			VALUES (104, '30c952fab122c3f9759f02a6d95c3758b246b4fee239957b2d4fee46e26170c4', 'patient', '2+2?', '4')
 ON DUPLICATE KEY UPDATE MID = MID;
 /*password: pw*/

/*Insert patient Thane Ross*/
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
105,
'Thane',
'Ross',
'thane.ross@gmail.com',
'123 Ross Ct',
'',
'Asheville',
'NC',
'28803',
'888-888-8888',
'Mister Ross',
'888-888-8881',
'IC',
'Street1',
'Street2',
'City',
'PA',
'12345-6789',
'555-555-5555',
'1',
'1989-01-03',
NULL,
'',
NULL,
NULL,
'O-',
'Caucasian',
'Male',
'This guy is a hippy.'
)  ON DUPLICATE KEY UPDATE MID = MID;

INSERT INTO users(MID, password, role, sQuestion, sAnswer) 
			VALUES (105, '30c952fab122c3f9759f02a6d95c3758b246b4fee239957b2d4fee46e26170c4', 'patient', '2+2?', '4')
 ON DUPLICATE KEY UPDATE MID = MID;
 /*password: pw*/
