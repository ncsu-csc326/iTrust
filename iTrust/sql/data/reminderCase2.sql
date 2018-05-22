INSERT INTO appointment(doctor_id,patient_id,sched_date,appt_type,comment)
VALUES
('9000000000', '2', CONCAT(ADDDATE(CURDATE(),1), ' 14:00:00'), 'General Checkup', 'monthly checkup'),
('9000000003', '2', CONCAT(ADDDATE(CURDATE(),2), ' 10:30:00'), 'Physical', 'yearly physical'),
('9000000003', '1', CONCAT(ADDDATE(CURDATE(),3), ' 7:45:00'), 'Colonoscopy', NULL),
('9000000000', '5', CONCAT(ADDDATE(CURDATE(),1), ' 11:30:00'), 'General Checkup', NULL),
('9000000003', '5', CONCAT(ADDDATE(CURDATE(),6), ' 14:45:00'), 'General Checkup', 'Follow-up for new medication');