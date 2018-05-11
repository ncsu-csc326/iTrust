DELETE FROM remotemonitoringlists where HCPMID = '9000000000';
DELETE FROM remotemonitoringdata where PatientID = '1';

INSERT INTO remotemonitoringlists(PatientMID, HCPMID, height, weight, pedometerReading)
					VALUES (1, 9000000000, 1, 1, 1);

INSERT INTO remotemonitoringdata(PatientID, height, weight, timeLogged, ReporterRole, ReporterID) 
                    VALUES (1, 70, 180, CONCAT(date_sub(CURDATE(), INTERVAL 2 DAY), ' 08:19:00'), "self-reported", 1),
                           (1, 70, 192.5, CONCAT(CURDATE(), ' 07:17:00'), "self-reported", 1); 
                           
INSERT INTO remotemonitoringdata(PatientID, height, pedometerReading, timeLogged, ReporterRole, ReporterID) 
                    VALUES (1, 70, 8153, CONCAT(date_sub(CURDATE(), INTERVAL 1 DAY), ' 07:48:00'), "patient representative", 2);