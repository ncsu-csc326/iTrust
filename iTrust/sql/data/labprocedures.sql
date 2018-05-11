INSERT INTO labprocedure
(PatientMID,LaboratoryProcedureCode,Rights,Status,Commentary,Results,
 NumericalResults, NumericalResultsUnit, LowerBound, UpperBound,
 OfficeVisitID, UpdatedDate, LabTechID, PriorityCode)
VALUES
(1,'10763-1','ALLOWED','In Transit','','',
 '','','','',
 11,'2008-05-19', 5000000001, 1),
(1,'10666-6','RESTRICTED','Pending','Performed the procedure','',
 '5.0','grams','4.8','5.1',
 11,'2008-05-18', 5000000001, 1),
(1,'10640-1','ALLOWED','Completed','Performed the procedure','Patient checks out ok',
 '88','grams','85','99',
 11,'2007-05-17', 5000000001, 2),
(2,'10640-1','ALLOWED', 'Completed','Performed the procedure','Patient needs more tests',
 '2','grams','1','3',
 952,'2007-05-19', 5000000001, 3),
(2,'10763-1','RESTRICTED', 'In Transit','Performed the procedure','Patient needs more tests',
 '','','','',
 955, '2007-07-20', 5000000002, 2),
(4,'10763-1','ALLOWED','Completed','','',
 '2','grams','1','3',
 955,'2007-07-20', 5000000002, 3),
(4,'10763-1','ALLOWED','Testing','','Patient needs more tests',
 '','','','',
 955,'2007-07-20', 5000000001, 3);
UPDATE labprocedure SET UpdatedDate=(SYSDATE() - INTERVAL 1 DAY) WHERE PatientMID=4;

INSERT INTO officevisits(
	id,
	visitDate,
	HCPID,
	notes,
	HospitalID,
	PatientID
)
VALUES (902,'2005-10-10',9000000000,'Generated for Death for Patient: 1','1',1),
(905,'2005-10-10',9000000000,'Generated for Death for Patient: 1','1',1),
(918,'2005-10-10',9000000000,'Generated for Death for Patient: 1','1',1)
ON DUPLICATE KEY UPDATE ID = ID;

