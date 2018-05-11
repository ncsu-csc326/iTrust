DELETE FROM remotemonitoringlists where HCPMID = '9000000000';

INSERT INTO remotemonitoringlists(PatientMID, HCPMID, GlucoseLevel, Weight)
					VALUES (1, 9000000000, 1, 1);