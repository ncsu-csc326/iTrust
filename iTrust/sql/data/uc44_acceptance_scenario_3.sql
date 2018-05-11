INSERT INTO officevisits(
	id,
	visitDate,
	HCPID,
	notes,
	HospitalID,
	PatientID
)
VALUES (44200,'2011-1-28',9000000003,'Hates getting shots','2',5);

INSERT INTO labprocedure(
    laboratoryprocedureid,
    patientmid,
    laboratoryprocedurecode,
    rights,
    status,
    commentary,
    results,
    officevisitid,
    updateddate,
    NumericalResults,
    LowerBound,
    UpperBound
)
VALUES (1384, 5, '13495-7', 'ALLOWED','Completed','','',44200, CONCAT(ADDDATE(CURDATE(),-1), ' 12:00:00.0'),
        '10','9','11');

INSERT INTO ovprocedure(
    id,
    visitid,
    cptcode,
    hcpid
)
VALUES 
(1383, 44200, '90707', '9000000003'),
(1381, 44200, '90371', '9000000003'),
(1382, 44200, '90712', '9000000003');

INSERT INTO ovmedication(
    VisitID, 
    NDCode, 
    StartDate, 
    EndDate, 
    Dosage, 
    Instructions
) 
VALUES (44200, '081096', '2011-1-28', '2011-2-28', 150, 'Take once daily with water');

