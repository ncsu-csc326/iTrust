INSERT INTO hospitals(HospitalID, HospitalName, Address, City, State, Zip) VALUES('11','NY Public Hospital', '1245 Main Street', 'New York', 'NY', '10453' );
INSERT INTO hospitals(HospitalID, HospitalName, Address, City, State, Zip) VALUES('12','LA Central', '1245 Main Street', 'Los Angeles', 'CA', '90004' );

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
9000012345,
null,
'hcp',
'Warren',
'TJ',
'4321 My Road St',
'PO BOX 2',
'New York',
'NY',
'10453',
'999-888-7777',
'Heart Surgeon',
'tjWarren@iTrust.org'
) ON DUPLICATE KEY UPDATE MID = MID;

INSERT INTO users(MID, password, role, sQuestion, sAnswer) VALUES(9000012345, '30c952fab122c3f9759f02a6d95c3758b246b4fee239957b2d4fee46e26170c4', 'hcp', 'first letter?', 'a')
ON DUPLICATE KEY UPDATE MID = MID;
/*password: pw*/
INSERT INTO hcpassignedhos(HCPID, HosID) VALUES(9000012345,'11')
ON DUPLICATE KEY UPDATE HCPID = HCPID;

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
9000012347,
null,
'hcp',
'Lewis',
'Tyler',
'4321 My Road St',
'PO BOX 2',
'Los Angeles',
'CA',
'90004',
'999-888-7777',
'Heart Surgeon',
'tjWarren@iTrust.org'
) ON DUPLICATE KEY UPDATE MID = MID;


INSERT INTO users(MID, password, role, sQuestion, sAnswer) VALUES(9000012347, '30c952fab122c3f9759f02a6d95c3758b246b4fee239957b2d4fee46e26170c4', 'hcp', 'first letter?', 'a')
ON DUPLICATE KEY UPDATE MID = MID;
/*password: pw*/
INSERT INTO hcpassignedhos(HCPID, HosID) VALUES(9000012347,'12')
ON DUPLICATE KEY UPDATE HCPID = HCPID;

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
9000012346,
null,
'hcp',
'Newton',
'Cam',
'4321 My Road St',
'PO BOX 2',
'New York',
'NY',
'10453',
'999-888-7777',
'General Physician',
'cn@iTrust.org'
) ON DUPLICATE KEY UPDATE MID = MID;

INSERT INTO users(MID, password, role, sQuestion, sAnswer) VALUES(9000012346, '30c952fab122c3f9759f02a6d95c3758b246b4fee239957b2d4fee46e26170c4', 'hcp', 'first letter?', 'a')
ON DUPLICATE KEY UPDATE MID = MID;
/*password: pw*/
INSERT INTO hcpassignedhos(HCPID, HosID) VALUES(9000012346,'11')
ON DUPLICATE KEY UPDATE HCPID = HCPID;


