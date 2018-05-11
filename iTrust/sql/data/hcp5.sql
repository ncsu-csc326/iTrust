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
9000000005,
null,
'hcp',
'Soulcrusher',
'Sarah',
'4321 My Road St',
'PO BOX 2',
'Woodbridge',
'CA',
'95258',
'999-888-7777',
'surgeon',
'ssoulcrusher@iTrust.org'
)ON DUPLICATE KEY UPDATE MID = MID;

INSERT INTO users(MID, password, role, sQuestion, sAnswer) VALUES(9000000005, '30c952fab122c3f9759f02a6d95c3758b246b4fee239957b2d4fee46e26170c4', 'hcp', 'first letter?', 'a')
ON DUPLICATE KEY UPDATE MID = MID;
-- password: pw
INSERT INTO hcpassignedhos(HCPID, HosID) VALUES(9000000005,'9191919191'), (9000000005,'8181818181')
ON DUPLICATE KEY UPDATE HCPID = HCPID;
