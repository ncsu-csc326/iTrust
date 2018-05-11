INSERT INTO remotemonitoringlists(PatientMID, HCPMID)
					VALUES (1, 9000000000),
						   (5, 9000000000),
						   (1, 8000000009);

INSERT INTO remotemonitoringdata(PatientID, systolicBloodPressure, diastolicBloodPressure, glucoseLevel, timeLogged, ReporterRole, ReporterID) 
                    VALUES (1, 90, 60, 80, CONCAT(CURDATE(), ' 05:30:00'), "self-reported", 1),
                           (1, 160, 110, 60, CONCAT(CURDATE(), ' 08:00:00'), "patient representative", 2),
                           (1, 100, 70, 90, CONCAT(CURDATE(), ' 07:15:00'), "case manager", 8000000009),
                           (1, 100, 75, 100, CONCAT(SUBDATE(CURDATE(), 1), ' 13:15:00'), "self-reported", 1),
                           (1, 100, 80, 110, CONCAT(SUBDATE(CURDATE(), 2), ' 17:15:00'), "patient representative", 2),
                           (1, 95, 65, 95, CONCAT(SUBDATE(CURDATE(), 4), ' 02:15:00'), "case manager", 8000000009),
                           (5, 100, 75, 100, CONCAT(SUBDATE(CURDATE(), 3), ' 11:47:00'), "self-reported", 5);