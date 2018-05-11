DELETE FROM appointment;

INSERT INTO appointment(doctor_id,patient_id,sched_date,appt_type,comment)
VALUES
('9000000000', '1', CONCAT(ADDDATE(CURDATE(),1), ' 10:30:00'), 'General Checkup', 'General Checkup after your knee surgery.'),
('9000000000', '2', ADDDate(CONCAT(YEAR(NOW()), '-',  MONTH(NOW()), '-15 07:30:00'), INTERVAL 1 MONTH), 'Physical', NULL),
('9000000003', '1', CONCAT(ADDDATE(CURDATE(),7), ' 01:35:00'), 'Consultation', 'This is your surgery consulation.');