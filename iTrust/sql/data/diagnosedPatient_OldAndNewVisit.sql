INSERT INTO patients 
(MID,	LastName,	FirstName,		DateOfBirth,					Gender)
VALUES
/* Diabetic who hasn't visited in last year*/
(1,		'Brimley',	'Wilford',		CURDATE() - INTERVAL 40 YEAR,	'Male')
ON DUPLICATE KEY UPDATE MID = MID;

INSERT INTO declaredhcp
(PatientID,	HCPID)
VALUES
(1,			9000000000)
ON DUPLICATE KEY UPDATE PatientID = PatientID;

/* Chronic visitor, Diabetes with Ketoacidosis */
INSERT INTO ovdiagnosis
(ID,	VisitID,	ICDCode)
VALUES
(1,		1,			250.10)
ON DUPLICATE KEY UPDATE ID = ID;

INSERT INTO officevisits
(ID,	VisitDate,						HCPID,		Notes,		PatientID)
VALUES
(1,		CURDATE() - INTERVAL 3 YEAR,	9000000000,	'Diabeetus',	  1), /* Chronic patient, diabetes */
(2,		CURDATE() - INTERVAL 3 MONTH,	9000000000, 'Liberty Mutual', 1)
ON DUPLICATE KEY UPDATE ID = ID;