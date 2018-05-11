INSERT INTO appointment (doctor_id, patient_id, sched_date, appt_type) VALUES 
(9000000000, 1, CONCAT(ADDDATE(CURDATE(),7), ' 13:00:00'), 'General Checkup'),
(9000000000, 1, CONCAT(ADDDATE(CURDATE(),7), ' 14:00:00'), 'General Checkup'),
(9000000000, 1, CONCAT(ADDDATE(CURDATE(),7), ' 14:45:00'), 'General Checkup'),
(9000000000, 1, CONCAT(ADDDATE(CURDATE(),7), ' 16:45:00'), 'General Checkup');

INSERT INTO appointmentrequests (doctor_id, patient_id, sched_date, appt_type, comment, pending, accepted) VALUES
(9000000010, 1, CONCAT(ADDDATE(CURDATE(),7), ' 14:45:00'), 'General Checkup', NULL, true, false),
(9000000003, 1, CONCAT(ADDDATE(CURDATE(),7), ' 16:45:00'), 'General Checkup', NULL, true, false),
(9000000000, 2, CONCAT(ADDDATE(CURDATE(),7), ' 12:45:00'), 'Colonoscopy', NULL, true, false);