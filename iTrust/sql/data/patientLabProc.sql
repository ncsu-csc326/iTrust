INSERT INTO labprocedure
(PatientMID,LaboratoryProcedureCode,Rights,Status,Commentary,Results,
 NumericalResults, NumericalResultsUnit, LowerBound, UpperBound,
 OfficeVisitID, UpdatedDate, LabTechID, PriorityCode)
VALUES
(2,'10763-1','ALLOWED','Completed','Its all done','',
 '85','grams','78','92',
 1521,'2011-11-20', 5000000001, 1),
(1,'13495-7','ALLOWED','Completed','','Patient needs more tests',
 '79','ml','25','50',
 1522,'2011-10-20', 5000000001, 2),
(21,'10666-6','ALLOWED','Completed','All over with','',
 '33','grams','21','36',
  1523,'2011-9-20',5000000001,2),
 (21,'10666-6','ALLOWED','Completed','All over with','',
 '22','grams','21','36',
  1524,'2011-3-20',5000000001,2),
  (21,'10666-6','ALLOWED','Completed','All over with','',
 '28','grams','21','36',
  1525,'2010-11-16',5000000001,2);

INSERT INTO officevisits(
	id,
	visitDate,
	HCPID,
	notes,
	HospitalID,
	PatientID
)
VALUES (1521,'2011-11-20',9000000000,'He is not feeling to well','1',2),
(1522,'2011-10-20',9000000000,'Excessively sick','1',1),
(1523,'2011-9-20',9000000000,'Dying','1',21),
(1524,'2011-3-20',9000000000,'Close to death','1',21),
(1525,'2010-11-16',9000000000,'Really sick','1',21);

