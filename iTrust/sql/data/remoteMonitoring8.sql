DELETE FROM remotemonitoringlists where HCPMID = '9000000000';
DELETE FROM remotemonitoringlists where PatientMID = '2';
DELETE FROM remotemonitoringdata where PatientID = '2';

INSERT INTO remotemonitoringlists(PatientMID, HCPMID, systolicBloodPressure, diastolicBloodPressure)
					VALUES (2, 9000000000, 1, 1);