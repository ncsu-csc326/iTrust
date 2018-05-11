INSERT INTO appointment(doctor_id,patient_id,sched_date,appt_type,comment)
VALUES
('9000000000', '2', CONCAT(YEAR(NOW())+1, '-06-04 10:30:00'), 'Consultation', 'Consultation for your upcoming surgery.'),
('9000000000', '2', CONCAT(YEAR(NOW())+1, '-10-14 08:00:00'), 'Colonoscopy', NULL),
('9000000003', '1', CONCAT(YEAR(NOW())+1, '-04-03 15:00:00'), 'Physical', 'This is your yearly physical.'),
('9000000000', '2', CONCAT(ADDDATE(CURDATE(),14), ' 10:30:00'), 'General Checkup', 'Checkup to remove your stitches'),
('9000000000', '1', CONCAT(ADDDATE(CURDATE(),21), ' 13:45:00'), 'General Checkup', NULL);