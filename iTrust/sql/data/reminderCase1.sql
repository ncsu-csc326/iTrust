INSERT INTO appointment(doctor_id,patient_id,sched_date,appt_type,comment)
VALUES
('9000000003', '2', CONCAT(ADDDATE(CURDATE(),1), ' 16:00:00'), 'General Checkup', NULL),
('9000000003', '1', CONCAT(ADDDATE(CURDATE(),2), ' 9:00:00'), 'Physical', NULL),
('9000000000', '2', CONCAT(ADDDATE(CURDATE(),3), ' 8:00:00'), 'Colonoscopy', NULL),
('9000000000', '5', CONCAT(ADDDATE(CURDATE(),1), ' 9:15:00'), 'General Checkup', 'Follow-up for immunization');