

INSERT INTO personnel(
MID,
AMID,
role,
lastName, 
firstName, 
address1,
address2)
VALUES (
9990000000,
null,
'hcp',
'Incomplete',
'Jimmy',
'567 Nowhere St.',
'PO Box 4')
ON DUPLICATE KEY UPDATE mid = mid;

INSERT INTO users(MID, password, role, sQuestion, sAnswer) VALUES(9990000000, '30c952fab122c3f9759f02a6d95c3758b246b4fee239957b2d4fee46e26170c4', 'hcp', 'second letter?', 'b')
ON DUPLICATE KEY UPDATE mid = mid;
/*password: pw*/
INSERT INTO hcpassignedhos(HCPID, HosID) VALUES(9990000000,'9191919191'), (9990000000,'8181818181')
ON DUPLICATE KEY UPDATE HCPID = HCPID;
