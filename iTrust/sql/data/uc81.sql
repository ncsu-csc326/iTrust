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
9000000081,
null,
'hcp',
'Ivanlyft',
'Duyu',
'4321 My Road St',
'PO BOX 2',
'Woodbridge',
'CA',
'95258',
'360-420-6969',
'trainer',
'broflex@iTrust.org'
)ON DUPLICATE KEY UPDATE MID = MID;

INSERT INTO users(MID, password, role, sQuestion, sAnswer) VALUES(9000000081, '30c952fab122c3f9759f02a6d95c3758b246b4fee239957b2d4fee46e26170c4', 'hcp', 'first letter?', 'a')
ON DUPLICATE KEY UPDATE MID = MID;
-- password: pw
INSERT INTO hcpassignedhos(HCPID, HosID) VALUES(9000000081,'9191919191'), (9000000081,'8181818181')
ON DUPLICATE KEY UPDATE HCPID = HCPID;

INSERT INTO exerciseEntry(EntryID, Date, ExerciseType, Name, Calories, Hours, Sets, Reps, PatientID)
		VALUES(81, '2012-12-14', 'Weight Training', 'Bench Press', 50, 0.5, 3, 10, 1);
INSERT INTO exerciseEntry(EntryID, Date, ExerciseType, Name, Calories, Hours, PatientID)
		VALUES(82, '2012-12-12', 'Cardio', 'Running', 100, 1.0, 1);
		