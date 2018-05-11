DELETE FROM remotemonitoringlists where HCPMID = '9000000000';
DELETE FROM remotemonitoringdata where PatientID = '2';

INSERT INTO remotemonitoringlists(PatientMID, HCPMID)
					VALUES (2, 9000000000);


