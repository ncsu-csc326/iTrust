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
9000000071,
null,
'hcp',
'Reid',
'Spencer',
'4321 My Road St',
'PO BOX 2',
'New York',
'NY',
'10453',
'999-888-7777',
'surgeon',
'rdoctor@iTrust.org'
)ON DUPLICATE KEY UPDATE MID = MID;

INSERT INTO users(MID, password, role, sQuestion, sAnswer) VALUES(9000000071, '30c952fab122c3f9759f02a6d95c3758b246b4fee239957b2d4fee46e26170c4', 'hcp', 'first letter?', 'a')
ON DUPLICATE KEY UPDATE MID = MID;
/*password: pw*/

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
(335,
'Hotchner', 
'Aaron', 
'nobody@gmail.com', 
'1247 Noname Dr', 
'Suite 106', 
'Raleigh', 
'NC', 
'27607-1234', 
'919-971-0000', 
'Mommy Person', 
'704-532-2117', 
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
'Female',
'')
 ON DUPLICATE KEY UPDATE MID = MID;
 
 INSERT INTO users(MID, password, role, sQuestion, sAnswer) 
			VALUES (335, '30c952fab122c3f9759f02a6d95c3758b246b4fee239957b2d4fee46e26170c4', 'patient', 'how you doin?', 'good');
/*password: pw*/
			
INSERT INTO declaredhcp(PatientID, HCPID) VALUES (335, 9000000071);
	
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
(333,
'Morgan', 
'Derek', 
'nobody@gmail.com', 
'1247 Noname Dr', 
'Suite 106', 
'Raleigh', 
'NC', 
'27607-1234', 
'919-971-0000', 
'Mommy Person', 
'704-532-2117', 
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
'Female',
'')
 ON DUPLICATE KEY UPDATE MID = MID;
 
 INSERT INTO users(MID, password, role, sQuestion, sAnswer) 
			VALUES (333, '30c952fab122c3f9759f02a6d95c3758b246b4fee239957b2d4fee46e26170c4', 'patient', 'how you doin?', 'good');
/*password: pw*/
			
INSERT INTO declaredhcp(PatientID, HCPID) VALUES (333, 9000000071);
			
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
(334,
'Jareau',
'Jennifer', 
'nobody@gmail.com', 
'1247 Noname Dr', 
'Suite 106', 
'Raleigh', 
'NC', 
'27607-1234', 
'919-971-0000', 
'Mommy Person', 
'704-532-2117', 
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
'Female',
'')
 ON DUPLICATE KEY UPDATE MID = MID;
 
 INSERT INTO users(MID, password, role, sQuestion, sAnswer) 
			VALUES (334, '30c952fab122c3f9759f02a6d95c3758b246b4fee239957b2d4fee46e26170c4', 'patient', 'how you doin?', 'good');
/*password: pw*/
			
INSERT INTO declaredhcp(PatientID, HCPID) VALUES (334, 9000000071);
			
			
		
DELETE FROM remotemonitoringdata where PatientID = '1';

INSERT INTO remotemonitoringlists(PatientMID, HCPMID, height, weight, pedometerReading)
					VALUES (342, 9000000000, 1, 1, 1);

INSERT INTO remotemonitoringdata(PatientID, height, weight, timeLogged, ReporterRole, ReporterID) 
                    VALUES (342, 65, 110, CONCAT(date_sub(CURDATE(), INTERVAL 2 DAY), ' 08:19:00'), "self-reported", 1),
                           (342, 65, 112.5, CONCAT(CURDATE(), ' 07:17:00'), "self-reported", 1); 
                           
INSERT INTO remotemonitoringdata(PatientID, height, pedometerReading, timeLogged, ReporterRole, ReporterID) 
                    VALUES (342, 65, 8153, CONCAT(date_sub(CURDATE(), INTERVAL 1 DAY), ' 07:48:00'), "patient representative", 2);		
		