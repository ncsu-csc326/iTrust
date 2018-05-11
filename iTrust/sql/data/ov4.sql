INSERT INTO officevisits(
	id,
	visitDate,
	HCPID,
	notes,
	HospitalID,
	PatientID
)
VALUES (375,'2009-9-15',9000000000,'Test office visit','1',2);

DELETE FROM allergies WHERE PatientID = 2;
INSERT INTO allergies(PatientID,Code, Description,FirstFound) 
	VALUES (2, '081096','Aspirin','2008-12-10 20:33:58'),	/*aspirin*/
	       (2, '664662530','Penicillin', '2007-06-04 20:33:58');	/*penicillin*/
