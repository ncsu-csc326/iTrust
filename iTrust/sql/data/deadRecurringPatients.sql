/*
 * NOTE: We're making this for a specific test case, testGetVisitReminders_IgnoreDeadPatients(),
 * So don't use gens for patient 1, 2, or 3 in the same case. It will not harm anything, however,
 * to overwrite them in any other use case.
 * 
 * Ensures patients 1, 2, and 3 are dead, and should be recurring patients
 */
INSERT INTO patients 
(MID,	LastName,	FirstName,	DateOfBirth,					DateOfDeath,					Gender)
VALUES
/* Diabetic who hasn't visited in last year*/
(1,		'Diabetes',	'Andi',		CURDATE() - INTERVAL 40 YEAR,	CURDATE() - INTERVAL 2 YEAR,	'Female'),
/* Patient over 50, hasn't had flu shot this season*/
(2,		'Flu',		'Bernard',	CURDATE() - INTERVAL 70 YEAR,	CURDATE() - INTERVAL 2 YEAR,	'Male'),
/* Female patient under 19 with overdue immunization for HPV. First immunization at 9 years*/
(3,		'HPV',	'Crystal',	CURDATE() - INTERVAL 9 YEAR,	CURDATE() - INTERVAL 2 YEAR,	'Female')
ON DUPLICATE KEY UPDATE MID = MID;

INSERT INTO declaredhcp
(PatientID,	HCPID)
VALUES
(1,			9000000000),
(2,			9000000000),
(3,			9000000000)
ON DUPLICATE KEY UPDATE PatientID = PatientID;

/* Chronic, Diabetes with Ketoacidosis */
INSERT INTO ovdiagnosis
(ID,	VisitID,	ICDCode)
VALUES
(1,		1,			250.10)
ON DUPLICATE KEY UPDATE ID = ID;

/* HPV */
INSERT INTO ovprocedure
(ID,	VisitID,	CPTCode)
VALUES
(1,		2,			'90649')
ON DUPLICATE KEY UPDATE ID = ID;


INSERT INTO officevisits
(ID,	VisitDate,						HCPID,		Notes,		PatientID)
VALUES
(1,		CURDATE() - INTERVAL 3 YEAR,	9000000000,	'Diabetes',	1), /* Chronic patient, diabetes */
(2,		CURDATE() - INTERVAL 3 YEAR,	9000000000,	'HPV',		3) /* HPV */
ON DUPLICATE KEY UPDATE ID = ID;