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
9000000008,
null,
'hcp',
'George',
'Curious',
'Monkey Street',
'',
'San Francisco',
'CA',
'94102',
'999-888-7777',
'Pediatrician',
'meepmeep@meep.org'
)ON DUPLICATE KEY UPDATE MID = MID;

INSERT INTO users(MID, password, role, sQuestion, sAnswer) VALUES(9000000008, '30c952fab122c3f9759f02a6d95c3758b246b4fee239957b2d4fee46e26170c4', 'hcp', 'first letter?', 'a')
ON DUPLICATE KEY UPDATE MID = MID;
/*password: pw*/
INSERT INTO hcpassignedhos(HCPID, HosID) VALUES(9000000008,'4'), (9000000008,'4')
ON DUPLICATE KEY UPDATE HCPID = HCPID;
