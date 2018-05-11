
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
9900000000,
null,
'hcp',
'Arehart',
'Tester',
'2110 Thanem Circle',
'Apt. 302',
'Raleigh',
'NC',
'27614',
'999-888-7777',
'Neurologist',
'tarehart@iTrust.org'
) ON DUPLICATE KEY UPDATE mid = mid;

INSERT INTO users(MID, password, role, sQuestion, sAnswer) VALUES(9900000000, '30c952fab122c3f9759f02a6d95c3758b246b4fee239957b2d4fee46e26170c4', 'hcp', 'second letter?', 'b')
ON DUPLICATE KEY UPDATE mid = mid;
/*password: pw*/
INSERT INTO hcpassignedhos(HCPID, HosID) VALUES(9900000000,'9191919191'), (9900000000,'8181818181')
ON DUPLICATE KEY UPDATE HCPID = HCPID;
