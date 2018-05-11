/* for UC11-UC37 accept test scenario 5 */

INSERT INTO officevisits(
	id,
	visitDate,
	HCPID,
	notes,
	HospitalID,
	PatientID
)
VALUES (395,'2011-1-26',9000000005,'Seems fine to me','4',1);

INSERT INTO ovmedication(
    VisitID, 
    NDCode, 
    StartDate, 
    EndDate, 
    Dosage, 
    Instructions
) 
VALUES (395, 678771191, '2011-1-26', '2011-2-5', 200, 'Take once daily with water');

INSERT INTO ovprocedure(ID, VisitID, CPTCode, HCPID )
VALUES (395, 395, '1270F', 9000000005);


INSERT INTO ovdiagnosis(ID, VisitID, ICDCode)
VALUES
(395, 395, 11.40)
ON DUPLICATE KEY UPDATE VisitID = VALUES(VisitID), ICDCode = VALUES(ICDCode);


INSERT INTO labprocedure (PatientMID, LaboratoryProcedureCode, Rights, Status, Commentary, Results, OfficeVisitID, UpdatedDate,
                          NumericalResults, NumericalResultsUnit, LowerBound, UpperBound)
VALUES
(1, '45335-7','ALLOWED','Completed','','',395,'2011-01-26 20:20:45.0','10','mg','9','11');