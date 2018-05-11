DELETE FROM representatives where representerMID = '2';
DELETE FROM remotemonitoringdata where PatientID = '1';
DELETE FROM remotemonitoringlists where PatientMID = '1';

INSERT INTO representatives(representerMID, representeeMID)
					VALUES (2, 1);
					
INSERT INTO remotemonitoringlists(PatientMID, HCPMID, GlucoseLevel)
					VALUES (1, 9000000000, 1);