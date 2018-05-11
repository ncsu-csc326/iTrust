DELETE FROM allergies WHERE PatientID = 2;
INSERT INTO allergies(PatientID,Code,Description, FirstFound) 
	VALUES (2, '081096', 'Aspirin','1999-03-14 20:00:00'); /*aspirin*/

INSERT INTO officevisits(
	id,
	visitDate,
	HCPID,
	notes,
	HospitalID,
	PatientID
)
VALUES (381,'2011-2-11',9000000001,'Hates getting shots','3',2);
