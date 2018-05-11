DELETE FROM remotemonitoringlists where HCPMID = '9000000000';
DELETE FROM remotemonitoringdata where PatientID = '1';

INSERT INTO remotemonitoringlists(PatientMID, HCPMID)
					VALUES (1, 9000000000);

INSERT INTO remotemonitoringdata(PatientID, weight, timeLogged, ReporterRole, ReporterID) 
                    VALUES (1, 180, CONCAT(date_sub(CURDATE(), INTERVAL 2 DAY), ' 08:19:00'), "self-reported", 1),
                           (1, 186.5, CONCAT(CURDATE(), ' 07:17:00'), "self-reported", 1); 
                           
INSERT INTO remotemonitoringdata(PatientID, weight, pedometerReading, timeLogged, ReporterRole, ReporterID) 
                    VALUES (1, 177, 8153, CONCAT(date_sub(CURDATE(), INTERVAL 1 DAY), ' 07:48:00'), "patient representative", 2);