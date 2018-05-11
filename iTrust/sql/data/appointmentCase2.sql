INSERT INTO appointment(doctor_id,patient_id,sched_date,appt_type,comment)
VALUES
('9000000000', '1', CONCAT(ADDDATE(CURDATE(),7), ' 09:10:00'), 'Consultation', NULL),
('9000000003', '2', CONCAT(ADDDATE(CURDATE(),7), ' 09:30:00'), 'General Checkup', NULL),
('9000000000', '5', CONCAT(ADDDATE(CURDATE(),7), ' 09:30:00'), 'General Checkup', 'Scheduled booster shots'),
('9000000000', '1', CONCAT(ADDDATE(CURDATE(),14), ' 13:30:00'), 'Ultrasound', NULL),
('9000000000', '2', CONCAT(ADDDATE(CURDATE(),14), ' 13:45:00'), 'General Checkup', NULL),
('9000000000', '5', CONCAT(ADDDATE(CURDATE(),10), ' 16:00:00'), 'General Checkup', 'Follow-up for the immunizations.');