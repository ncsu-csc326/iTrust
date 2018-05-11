INSERT INTO appointment(doctor_id, patient_id, sched_date, appt_type, comment)
			VALUES(9000000000,100,CONCAT(ADDDATE(CURDATE(),7), ' 09:30:00'), 'General Checkup', 'Always seven days from now');

INSERT INTO appointment(doctor_id, patient_id, sched_date, appt_type, comment)
			VALUES(9000000000,100,CONCAT(ADDDATE(CURDATE(),7), ' 09:45:00'), 'General Checkup', 'This conflicts with previous appt');
			
INSERT INTO appointment(doctor_id, patient_id, sched_date, appt_type, comment)
			VALUES(9000000000,1, CONCAT(ADDDATE(CURDATE(),12), ' 09:45:00'), 'General Checkup', NULL);
INSERT INTO appointment(doctor_id, patient_id, sched_date, appt_type, comment)
			VALUES(9000000000,1, CONCAT(ADDDATE(CURDATE(),7), ' 13:30:00'), 'Ultrasound', NULL);
			
INSERT INTO appointment(doctor_id, patient_id, sched_date, appt_type, comment)
			VALUES(9000000000,42, '2010-01-01 13:30:00', 'Ultrasound', NULL);
			
			
INSERT INTO appointment(doctor_id, patient_id, sched_date, appt_type, comment)
			VALUES(9000000000,100, '2012-07-01 13:45:00', 'Ultrasound', NULL);
			
