DELETE FROM remotemonitoringlists where HCPMID = '8000000009';
DELETE FROM remotemonitoringdata where PatientID = '2';

INSERT INTO remotemonitoringlists(PatientMID, HCPMID)
					VALUES (2, 8000000009);



