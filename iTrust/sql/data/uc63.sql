/*Inserting Kathryn Evans*/
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
9000000012,
null,
'hcp',
'Evans',
'Kathryn',
'10078 Avent Ferry Road',
'',
'Capitol City',
'NC',
'28700-0458',
'555-877-5100',
'OB/GYN',
'evans@iTrust.org'
)ON DUPLICATE KEY UPDATE MID = MID;

INSERT INTO users(MID, password, role, sQuestion, sAnswer) VALUES(9000000012, '30c952fab122c3f9759f02a6d95c3758b246b4fee239957b2d4fee46e26170c4', 'hcp', 'first letter?', 'a')
ON DUPLICATE KEY UPDATE MID = MID;
/*password: pw*/

INSERT INTO hcpassignedhos(HCPID, HosID) VALUES(9000000012,'4'), (9000000012,'4')
ON DUPLICATE KEY UPDATE HCPID = HCPID;

/*Inserting Harry Potter*/
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
9000000013,
null,
'hcp',
'Potter',
'Harry',
'10078 Imaginary Lane',
'',
'Capitol City',
'NC',
'28700-0458',
'555-877-5100',
'surgeon',
'sorcery@iTrust.org'
)ON DUPLICATE KEY UPDATE MID = MID;

INSERT INTO users(MID, password, role, sQuestion, sAnswer) VALUES(9000000013, '30c952fab122c3f9759f02a6d95c3758b246b4fee239957b2d4fee46e26170c4', 'hcp', 'first letter?', 'a')
ON DUPLICATE KEY UPDATE MID = MID;
/*password: pw*/

/*Inserting patient Daria Griffin*/
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
dateofbirth,
Gender)
VALUES
(400,
'Griffin', 
'Daria', 
'dgrif@gmail.com', 
'1333 Who Cares Road', 
'Suite 102', 
'Raleigh', 
'NC', 
'27606-1234', 
'919-971-0000',
'1993-10-25',
'Female')
 ON DUPLICATE KEY UPDATE MID = MID;

INSERT INTO users(MID, password, role, sQuestion, sAnswer) 
			VALUES (400, '30c952fab122c3f9759f02a6d95c3758b246b4fee239957b2d4fee46e26170c4', 'patient', 'what is your favorite color?', 'blue')
 ON DUPLICATE KEY UPDATE MID = MID;
 /*password: pw*/

/*Inserting patient Brenna Lowery*/
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
dateofbirth,
Gender)
VALUES
(401,
'Lowery', 
'Brenna', 
'lowery@gmail.com', 
'1333 Who Cares Road', 
'Suite 103', 
'Raleigh', 
'NC', 
'27606-1234', 
'919-971-0001',
'1977-03-15',
'Female')
 ON DUPLICATE KEY UPDATE MID = MID;

INSERT INTO users(MID, password, role, sQuestion, sAnswer) 
			VALUES (401, '30c952fab122c3f9759f02a6d95c3758b246b4fee239957b2d4fee46e26170c4', 'patient', 'what is your favorite color?', 'blue')
 ON DUPLICATE KEY UPDATE MID = MID;
 /*password: pw*/
 
 /* Add brena lowery basic health info */
 INSERT INTO `personalhealthinformation` (
 `PatientID`,
 `OfficeVisitID`,
 `Height`,
 `Weight`,
 `HeadCircumference`,
 `Smoker`,
 `SmokingStatus`,
 `HouseholdSmokingStatus`,
 `BloodPressureN`,
 `BloodPressureD`,
 `CholesterolHDL`,
 `CholesterolLDL`,
 `CholesterolTri`,
 `HCPID`,
 `AsOfDate`,
 `OfficeVisitDate`,
 `BMI`
 ) VALUES (
 401,
 99912,
 65,
 125,
 0,
 0,
 9,
 1,
 120,
 80,
 80,
 80,
 200,
 9000000012,
 '2014-01-05 16:14:46',
 '2014-01-05',
 20.8)
 ON DUPLICATE KEY UPDATE PatientID = PatientID;

/*Inserting patient Amelia Davidson*/
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
dateofbirth,
Gender)
VALUES
(402,
'Davidson', 
'Amelia', 
'amelia@gmail.com', 
'1333 Who Cares Road', 
'Suite 104', 
'Raleigh', 
'NC', 
'27606-1234', 
'919-971-0002',
'1985-06-27',
'Female')
 ON DUPLICATE KEY UPDATE MID = MID;

INSERT INTO users(MID, password, role, sQuestion, sAnswer) 
			VALUES (402, '30c952fab122c3f9759f02a6d95c3758b246b4fee239957b2d4fee46e26170c4', 'patient', 'what is your favorite color?', 'blue')
 ON DUPLICATE KEY UPDATE MID = MID;
 /*password: pw*/

/*Inserting patient Thane Ross*/
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
dateofbirth,
Gender)
VALUES
(403,
'Ross', 
'Thane', 
'ross@gmail.com', 
'1333 Who Cares Road', 
'Suite 105', 
'Raleigh', 
'NC', 
'27606-1234', 
'919-971-0003',
'1985-06-27',
'Male')
 ON DUPLICATE KEY UPDATE MID = MID;

INSERT INTO users(MID, password, role, sQuestion, sAnswer) 
			VALUES (403, '30c952fab122c3f9759f02a6d95c3758b246b4fee239957b2d4fee46e26170c4', 'patient', 'what is your favorite color?', 'blue')
 ON DUPLICATE KEY UPDATE MID = MID;
 /*password: pw*/

/*Inserting patient Mary Hadalamb*/
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
dateofbirth,
Gender)
VALUES
(404,
'Hadalamb', 
'Mary', 
'snowlamb@gmail.com', 
'1333 Who Cares Road', 
'Suite 106', 
'Raleigh', 
'NC', 
'27606-1234', 
'919-971-0004',
'1985-06-27',
'Female')
 ON DUPLICATE KEY UPDATE MID = MID;

INSERT INTO users(MID, password, role, sQuestion, sAnswer) 
			VALUES (404, '30c952fab122c3f9759f02a6d95c3758b246b4fee239957b2d4fee46e26170c4', 'patient', 'what is your favorite color?', 'blue')
 ON DUPLICATE KEY UPDATE MID = MID;
 /*password: pw*/

/*Inserting patient Rock Solid*/
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
dateofbirth,
Gender)
VALUES
(405,
'Solid', 
'Rock', 
'androll@gmail.com', 
'1333 Who Cares Road', 
'Suite 107', 
'Raleigh', 
'NC', 
'27606-1234', 
'919-971-0005',
'1985-06-27',
'Female')
 ON DUPLICATE KEY UPDATE MID = MID;

INSERT INTO users(MID, password, role, sQuestion, sAnswer) 
			VALUES (405, '30c952fab122c3f9759f02a6d95c3758b246b4fee239957b2d4fee46e26170c4', 'patient', 'what is your favorite color?', 'blue')
 ON DUPLICATE KEY UPDATE MID = MID;
 /*password: pw*/

/*Inserting patient Sing Along*/
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
dateofbirth,
Gender)
VALUES
(406,
'Along', 
'Sing', 
'song@gmail.com', 
'1333 Who Cares Road', 
'Suite 108', 
'Raleigh', 
'NC', 
'27606-1234', 
'919-971-0006',
'1985-06-27',
'Female')
 ON DUPLICATE KEY UPDATE MID = MID;

INSERT INTO users(MID, password, role, sQuestion, sAnswer) 
			VALUES (406, '30c952fab122c3f9759f02a6d95c3758b246b4fee239957b2d4fee46e26170c4', 'patient', 'what is your favorite color?', 'blue')
 ON DUPLICATE KEY UPDATE MID = MID;
 /*password: pw*/

/*Inserting pregnancy for Brenna*/
INSERT INTO obstetrics
(MID,
weeksPregnant, 
dateVisit,
hoursInLabor,
deliveryType,
yearConception,
pregnancyStatus)
VALUES
(401,
'39-5',
'2010-01-01',
'27',  
'Vaginal Delivery',
'2010',
'Complete')
 ON DUPLICATE KEY UPDATE MID = MID;

/*Inserting pregnancy for Brenna*/
INSERT INTO obstetrics
(MID,
weeksPregnant,
dateVisit,
hoursInLabor,
deliveryType,
yearConception,
pregnancyStatus)
VALUES
(401,
'8-2',  
'2012-01-01',
'0', 
'Miscarriage',
'2012',
'Complete')
 ON DUPLICATE KEY UPDATE MID = MID;
