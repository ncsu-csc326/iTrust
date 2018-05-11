/*Inserting Meredith Palmers*/
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
9000000011,
null,
'hcp',
'Palmers',
'Meredith',
'6789 S Street',
'PO BOX 5',
'New York',
'NY',
'10453',
'999-888-5555',
NULL,
'mpalmers@iTrust.org'
) ON DUPLICATE KEY UPDATE MID = MID;

INSERT INTO users(MID, 
password, 
role, 
sQuestion, 
sAnswer) 
VALUES(9000000011, 
'30c952fab122c3f9759f02a6d95c3758b246b4fee239957b2d4fee46e26170c4', 
'hcp', 
'first letter?', 
'z')
ON DUPLICATE KEY UPDATE MID = MID;
/*password: pw*/
INSERT INTO hcpassignedhos(HCPID, HosID) VALUES(9000000011,'9191919191')
ON DUPLICATE KEY UPDATE HCPID = HCPID;
/*Done with Meredith Palmers*/

/*Inserting UAP Mike Jones*/INSERT INTO personnel(
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
'admin',
'Jones',
'Mike',
'538 N Circle',
'PO BOX 3',
'Albany',
'NY',
'10453',
'999-555-4561',
NULL,
'mikeJ@iTrust.org'
) ON DUPLICATE KEY UPDATE MID = MID;

INSERT INTO users(MID, 
password, 
role, 
sQuestion, 
sAnswer) 
VALUES(9000000012, 
'30c952fab122c3f9759f02a6d95c3758b246b4fee239957b2d4fee46e26170c4', 
'uap', 
'first letter?', 
'z')
ON DUPLICATE KEY UPDATE MID = MID;
/*password: pw*/
/*Done with Mike Jones*/

/*Inserting UAP Daniel Williams*/
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
'admin',
'Williams',
'Daniel',
'123 Fake St.',
'PO BOX 4',
'London',
'OH',
'43140',
'900-555-1589',
NULL,
'dwill@iTrust.org'
) ON DUPLICATE KEY UPDATE MID = MID;

INSERT INTO users(MID, 
password, 
role, 
sQuestion, 
sAnswer) 
VALUES(9000000013, 
'30c952fab122c3f9759f02a6d95c3758b246b4fee239957b2d4fee46e26170c4', 
'uap', 
'first letter?', 
'z')
ON DUPLICATE KEY UPDATE MID = MID;
/*password: pw*/
/*Done with Daniel Williams*/

/*Inserting UAP Jane Smith*/
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
9000000014,
null,
'admin',
'Smith',
'Jane',
'222 S Sleep',
'PO BOX 2',
'Cloud',
'MN',
'56304',
'999-555-5621',
NULL,
'jjSmith@iTrust.org'
) ON DUPLICATE KEY UPDATE MID = MID;

INSERT INTO users(MID, 
password, 
role, 
sQuestion, 
sAnswer) 
VALUES(9000000014, 
'30c952fab122c3f9759f02a6d95c3758b246b4fee239957b2d4fee46e26170c4', 
'uap', 
'first letter?', 
'z')
ON DUPLICATE KEY UPDATE MID = MID;
/*password: pw*/
/*Done with Jane Smith*/

/*Inserting UAP Roger King*/
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
9000000015,
null,
'admin',
'King',
'Roger',
'987 Queen Ct',
'PO BOX 5',
'Raleigh',
'NC',
'27606',
'999-555-4561',
NULL,
'kingoque@iTrust.org'
) ON DUPLICATE KEY UPDATE MID = MID;

INSERT INTO users(MID, 
password, 
role, 
sQuestion, 
sAnswer) 
VALUES(9000000015, 
'30c952fab122c3f9759f02a6d95c3758b246b4fee239957b2d4fee46e26170c4', 
'uap', 
'first letter?', 
'z')
ON DUPLICATE KEY UPDATE MID = MID;
/*password: pw*/
/*Done with Roger King*/

/* Rob Peterson - patient */
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
(309,
'Peterson', 
'Rob', 
'rob@gmail.com', 
'1247 Deptford Dr', 
'Apt 1', 
'Raleigh', 
'NC', 
'27606-1234', 
'919-971-0000', 
'Mommy Peterson', 
'609-555-2222', 
'Aetna', 
'1234 Aetna Blvd', 
'Suite 602', 
'Charlotte',
'NC', 
'28215', 
'704-555-1234', 
'ChetumNHowe', 
'1980-06-01',
0,
0,
'O+',
'African American',
'Male',
'')
 ON DUPLICATE KEY UPDATE MID = MID;

INSERT INTO users(MID, password, role, sQuestion, sAnswer) 
			VALUES (309, '30c952fab122c3f9759f02a6d95c3758b246b4fee239957b2d4fee46e26170c4', 'patient', 'what is your favorite color?', 'red')
 ON DUPLICATE KEY UPDATE MID = MID;
 /*password: pw*/
 
 /* Theresa Clark - patient */
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
(310,
'Clark', 
'Theresa', 
'terry@gmail.com', 
'3 Elm St', 
'', 
'Raleigh', 
'NC', 
'27606-1234', 
'919-555-1111', 
'Mommy Clark', 
'856-555-2222', 
'Aetna', 
'1234 Aetna Blvd', 
'Suite 602', 
'Charlotte',
'NC', 
'28215', 
'704-555-1234', 
'ChetumNHowe', 
'1981-06-01',
0,
0,
'O+',
'Caucasian',
'Female',
'')
 ON DUPLICATE KEY UPDATE MID = MID;

INSERT INTO users(MID, password, role, sQuestion, sAnswer) 
			VALUES (310, '30c952fab122c3f9759f02a6d95c3758b246b4fee239957b2d4fee46e26170c4', 'patient', 'what is your favorite color?', 'red')
 ON DUPLICATE KEY UPDATE MID = MID;
 /*password: pw*/
 
 /* Sean Ford - patient */
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
(311,
'Ford', 
'Sean', 
'macdaddy@gmail.com', 
'1247 Nightmare Dr', 
'', 
'Raleigh', 
'NC', 
'27606-1234', 
'919-971-2222', 
'Mommy Ford', 
'609-555-2222', 
'Aetna', 
'1234 Aetna Blvd', 
'Suite 602', 
'Charlotte',
'NC', 
'28215', 
'704-555-1234', 
'ChetumNHowe', 
'1982-06-01',
0,
0,
'AB+',
'Asian American',
'Male',
'')
 ON DUPLICATE KEY UPDATE MID = MID;

INSERT INTO users(MID, password, role, sQuestion, sAnswer) 
			VALUES (311, '30c952fab122c3f9759f02a6d95c3758b246b4fee239957b2d4fee46e26170c4', 'patient', 'what is your favorite color?', 'red')
 ON DUPLICATE KEY UPDATE MID = MID;
 /*password: pw*/

/* Louise Turner - patient */
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
(312,
'Turner', 
'Louise', 
'bigL@gmail.com', 
'33 Deptford Dr', 
'Apt 33', 
'Raleigh', 
'NC', 
'27606-1234', 
'919-971-3333', 
'Mommy Turner', 
'609-555-2222', 
'Aetna', 
'1234 Aetna Blvd', 
'Suite 602', 
'Charlotte',
'NC', 
'28215', 
'704-555-1234', 
'ChetumNHowe', 
'1984-06-01',
0,
0,
'O+',
'Asian American',
'Female',
'')
 ON DUPLICATE KEY UPDATE MID = MID;

INSERT INTO users(MID, password, role, sQuestion, sAnswer) 
VALUES (312, '30c952fab122c3f9759f02a6d95c3758b246b4fee239957b2d4fee46e26170c4', 'patient', 'what is your favorite color?', 'red')
 		ON DUPLICATE KEY UPDATE MID = MID;
 /*password: pw*/
 
 /* John Smith - patient */
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
(313,
'Smith', 
'John', 
'jhneaph@gmail.com', 
'5209 Youvben Crt', 
'Apt 3', 
'Raleigh', 
'NC', 
'27606-1234', 
'919-555-8008', 
'Jane Smith', 
'252-555-2222', 
'Blue Cross', 
'1234 Blue Cross Blvd', 
'Suite 602', 
'Fayetteville',
'NC', 
'28301', 
'252-555-1234', 
'A39023G', 
'1968-02-21',
0,
0,
'AB+',
'Caucasian',
'Male',
'')
 ON DUPLICATE KEY UPDATE MID = MID;

INSERT INTO users(MID, password, role, sQuestion, sAnswer) 
			VALUES (313, '30c952fab122c3f9759f02a6d95c3758b246b4fee239957b2d4fee46e26170c4', 'patient', 'what is time of the year?', 'summer')
 ON DUPLICATE KEY UPDATE MID = MID;
 /*password: pw*/

/* Maria Lopez - patient */
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
(314,
'Lopez', 
'Maria', 
'mLopez@gmail.com', 
'328 CountyLine St', 
'Apt 3', 
'Apex', 
'NC', 
'27502-1234', 
'910-803-9134', 
'Mario Lopez', 
'452-555-3627', 
'Cigna', 
'9820 Proprietary Ct', 
'Suite 46', 
'Raleigh',
'NC', 
'27606', 
'919-555-1234', 
'A329485X', 
'1980-10-07',
0,
0,
'B-',
'Hispanic',
'Female',
'')
 ON DUPLICATE KEY UPDATE MID = MID;
 
 INSERT INTO users(MID, password, role, sQuestion, sAnswer) 
			VALUES (314, '30c952fab122c3f9759f02a6d95c3758b246b4fee239957b2d4fee46e26170c4', 'patient', 'what is time of the day?', 'noon')
 ON DUPLICATE KEY UPDATE MID = MID;
 /*password: pw*/
 
 /* Juan Carlos - patient */
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
(315,
'Carlos', 
'Juan', 
'jCarl@juanny.com', 
'489 Funny Land St', 
'Apt A', 
'Morrisville', 
'NC', 
'27612-1234', 
'910-803-9134', 
'Luis Carlos', 
'769-555-1542', 
'Blue Cross', 
'1234 Blue Cross Blvd', 
'Suite 602', 
'Fayetteville',
'NC', 
'28301', 
'252-555-1234', 
'BSGU43234N', 
'1973-07-11',
0,
0,
'A+',
'Hispanic',
'Male',
'')
 ON DUPLICATE KEY UPDATE MID = MID;

INSERT INTO users(MID, password, role, sQuestion, sAnswer) 
			VALUES (315, '30c952fab122c3f9759f02a6d95c3758b246b4fee239957b2d4fee46e26170c4', 'patient', 'what is your favorite show?', 'smallville')
 ON DUPLICATE KEY UPDATE MID = MID;
 /*password: pw*/
 
 /* Alex Paul - patient */
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
(316,
'Paul', 
'Alex', 
'paulster@MS.com', 
'489 Side Seven St', 
'Dvsn 3', 
'Mount Pilot', 
'NC', 
'27520-1234', 
'252-555-1586', 
'Mrs. Paul', 
'919-555-5668', 
'Blue Cross', 
'1234 Blue Cross Blvd', 
'Suite 602', 
'Fayetteville',
'NC', 
'28301', 
'252-555-1234', 
'YE2903AS86', 
'1990-06-10',
0,
0,
'A+',
'African American',
'Male',
'')
 ON DUPLICATE KEY UPDATE MID = MID;

INSERT INTO users(MID, password, role, sQuestion, sAnswer) 
			VALUES (316, '30c952fab122c3f9759f02a6d95c3758b246b4fee239957b2d4fee46e26170c4', 'patient', 'what is your favorite time?', 'mealtime')
 ON DUPLICATE KEY UPDATE MID = MID;
 /*password: pw*/
 
 /* UAP: Mike Jones*/
 
/*Scenario 3 - Sean Ford's submitted and unsubmitted bill */

INSERT INTO billing(appt_id, billTimeS, PatientID, HCPID, amt, status) VALUES
((Select appt_id from itrust.appointment WHERE patient_id = 311 AND sched_date > '2012-03-08' ORDER BY sched_date ASC LIMIT 1), '2012-03-08', 311,9000000000,40,'Submitted');
 
INSERT INTO billing(appt_id, billTimeS, PatientID, HCPID, amt, status) VALUES
((Select appt_id from itrust.appointment WHERE patient_id = 311 AND sched_date > '2012-12-02' ORDER BY sched_date ASC LIMIT 1), '2013-12-02', 311,'9000000011','40','Unsubmitted');
 
/* Scenario 5 - Louise Turner pays by credit card  */
INSERT INTO billing(appt_id, billTimeS, PatientID, HCPID, amt, status) VALUES
((Select appt_id from itrust.appointment WHERE patient_id = 312 AND sched_date > '2012-02-04' ORDER BY sched_date ASC LIMIT 1), '2012-02-04', 312,'9000000011','60','Unsubmitted');

/*Bill for John Smith*/
INSERT INTO billing(appt_id, billTimeS, PatientID, HCPID, amt, status) VALUES
((Select appt_id from itrust.appointment WHERE patient_id = 313 AND sched_date > '2014-01-10' ORDER BY sched_date ASC LIMIT 1), '2014-01-10', 313, 8000000011, 150, 'Unsubmitted');

/*Bill for Maria Lopez*/
INSERT INTO billing(appt_id, billTimeS, PatientID, HCPID, amt, status) VALUES
((Select appt_id from itrust.appointment WHERE patient_id = 314 AND sched_date > '2014-02-17' ORDER BY sched_date ASC LIMIT 1), '2014-02-17', 314, 9000000000, 250, 'Unsubmitted');

/*Bill for Juan Carlos*/
INSERT INTO billing(appt_id, billTimeS, PatientID, HCPID, amt, status) VALUES
((Select appt_id from itrust.appointment WHERE patient_id = 315 AND sched_date > '2014-02-07' ORDER BY sched_date ASC LIMIT 1), '2014-02-07', 315, 8000000011, 350, 'Unsubmitted');

/*Bill for Alex Paul*/
INSERT INTO billing(appt_id, billTimeS, PatientID, HCPID, amt, status) VALUES
((Select appt_id from itrust.appointment WHERE patient_id = 316 AND sched_date > '2014-01-25' ORDER BY sched_date ASC LIMIT 1), '2014-01-25', 316, 9000000000, 250, 'Unsubmitted');

