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
9000000086,
null,
'hcp',
'Bridges',
'Lamar',
'5451 Presidents Lane',
'',
'Raleigh',
'NC',
'27606',
'999-888-6666',
'Ophthalmologist',
'lbridges@iTrust.org'
) ON DUPLICATE KEY UPDATE mid = mid;

INSERT INTO users(MID, password, role, sQuestion, sAnswer) VALUES(9000000086, '30c952fab122c3f9759f02a6d95c3758b246b4fee239957b2d4fee46e26170c4', 'hcp', 'first letter?', 'a')
ON DUPLICATE KEY UPDATE MID = MID;
/*password: pw*/
INSERT INTO hcpassignedhos(HCPID, HosID) VALUES(9000000086,'9191919191')
ON DUPLICATE KEY UPDATE HCPID = HCPID;