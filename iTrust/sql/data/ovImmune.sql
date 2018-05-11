INSERT INTO officevisits(
	id,
	visitDate,
	HCPID,
	notes,
	HospitalID,
	PatientID
)
VALUES (4029,CURDATE(),9000000000,'immunization','1',1);

INSERT INTO ovprocedure(ID, VisitID, CPTCode, HCPID )
VALUES (4029, 4029, 90660, 9000000000);