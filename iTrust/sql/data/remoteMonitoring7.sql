DELETE FROM remotemonitoringlists where HCPMID = '9000000000';
DELETE FROM remotemonitoringdata where PatientID = '1';

INSERT INTO remotemonitoringlists(PatientMID, HCPMID, height, weight, pedometerReading)
					VALUES (1, 9000000000, 1, 1, 1);