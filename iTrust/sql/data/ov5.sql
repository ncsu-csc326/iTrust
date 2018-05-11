/* for UC11-UC37 accept test scenario 3 */

INSERT INTO officevisits(
	id,
	visitDate,
	HCPID,
	notes,
	HospitalID,
	PatientID
)
VALUES (380,ADDDATE(CURDATE(),-1),9000000003,'Hates getting shots','3',5);

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
    NumericalResultsUnit,
    LowerBound,
    UpperBound
)
VALUES (1380, 5, '13495-7', 'ALLOWED','Completed','','',380, CONCAT(ADDDATE(CURDATE(),-1), ' 12:00:00.0'),
        '10', 'ml', '9','11');

INSERT INTO ovprocedure(
    id,
    visitid,
    cptcode,
    hcpid
)
VALUES 
(1380, 380, '90707', '9000000003'),
(1381, 380, '90371', '9000000003'),
(1382, 380, '90712', '9000000003');
