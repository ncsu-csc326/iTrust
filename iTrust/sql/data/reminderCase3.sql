INSERT INTO appointment(doctor_id,patient_id,sched_date,appt_type,comment)
VALUES
('9000000000', '2', CONCAT(ADDDATE(CURDATE(),1), ' 14:00:00'), 'General Checkup', 'monthly checkup');