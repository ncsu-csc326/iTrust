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
9000000010,
null,
'hcp',
'Zoidberg',
'John',
'6789 S Street',
'PO BOX 4',
'New York',
'NY',
'10453',
'999-888-7777',
NULL,
'jzoidberg@iTrust.org'
) ON DUPLICATE KEY UPDATE MID = MID;

INSERT INTO users(MID, 
password, 
role, 
sQuestion, 
sAnswer) 
VALUES(9000000010, 
'30c952fab122c3f9759f02a6d95c3758b246b4fee239957b2d4fee46e26170c4', 
'hcp', 
'first letter?', 
'z')
ON DUPLICATE KEY UPDATE MID = MID;
/*password: pw*/
INSERT INTO hcpassignedhos(HCPID, HosID) VALUES(9000000010,'9191919191')
ON DUPLICATE KEY UPDATE HCPID = HCPID;

INSERT INTO appointment(doctor_id, patient_id, sched_date, appt_type)
VALUES(9000000010, 2, '2012-08-22 13:30:00', 'General Checkup');

INSERT INTO appointmentrequests(doctor_id, patient_id, sched_date, appt_type,pending, accepted)
VALUES(9000000010, 26, '2012-02-24 09:00:00', 'General Checkup',1,0);
