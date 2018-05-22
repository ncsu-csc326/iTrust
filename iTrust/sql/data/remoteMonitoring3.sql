DELETE FROM remotemonitoringlists where HCPMID = '9000000000';
DELETE FROM remotemonitoringdata where PatientID = '1';

INSERT INTO remotemonitoringlists(PatientMID, HCPMID)
					VALUES (1, 9000000000),
						   (5, 9000000000);

INSERT INTO remotemonitoringdata(PatientID, systolicBloodPressure, diastolicBloodPressure, glucoseLevel, timeLogged, ReporterRole, ReporterID) 
                    VALUES (1, 90, 60, 80, CONCAT(CURDATE(), ' 05:30:00'), "self-reported", 1),
                           (1, 160, 110, 60, CONCAT(CURDATE(), ' 08:00:00'), "patient representative", 2),
                           (1, 100, 70, 90, CONCAT(CURDATE(), ' 07:15:00'), "case manager", 8000000009);