INSERT INTO officevisits(ID, visitDate, HCPID, notes, PatientID, HospitalID)
VALUES
(218, CONCAT(YEAR(NOW()), '-11-02'), 9000000000, 'Diagnose Malaria', 1, '1'),
(219, CONCAT(YEAR(NOW()), '-11-02'), 9000000000, 'Diagnose Malaria', 2, '1'),
(220, CONCAT(YEAR(NOW()), '-11-02'), 9000000000, 'Diagnose Malaria', 3, '1'),
(221, CONCAT(YEAR(NOW()), '-11-02'), 9000000000, 'Diagnose Malaria', 4, '1'),
(222, CONCAT(YEAR(NOW()), '-11-02'), 9000000000, 'Diagnose Malaria', 5, '1'),
(223, CONCAT(YEAR(NOW()), '-11-09'), 9000000000, 'Diagnose Malaria', 6, '1'),
(224, CONCAT(YEAR(NOW()), '-11-09'), 9000000000, 'Diagnose Malaria', 7, '1'),
(225, CONCAT(YEAR(NOW()), '-11-09'), 9000000000, 'Diagnose Malaria', 8, '1'),
(226, CONCAT(YEAR(NOW()), '-11-09'), 9000000000, 'Diagnose Malaria', 10, '1'),
(227, CONCAT(YEAR(NOW()), '-11-09'), 9000000000, 'Diagnose Malaria', 20, '1'),
(228, CONCAT(YEAR(NOW()), '-11-09'), 9000000000, 'Diagnose Malaria', 21, '1'),

(229, CONCAT(YEAR(NOW())-1, '-11-02'), 9000000000, 'Diagnose Malaria', 1, '1'),
(230, CONCAT(YEAR(NOW())-2, '-11-02'), 9000000000, 'Diagnose Malaria', 2, '1'),
(231, CONCAT(YEAR(NOW())-3, '-11-02'), 9000000000, 'Diagnose Malaria', 3, '1'),
(232, CONCAT(YEAR(NOW())-4, '-11-02'), 9000000000, 'Diagnose Malaria', 4, '1'),
(233, CONCAT(YEAR(NOW())-5, '-11-02'), 9000000000, 'Diagnose Malaria', 5, '1'),
(234, CONCAT(YEAR(NOW())-1, '-11-09'), 9000000000, 'Diagnose Malaria', 6, '1'),
(235, CONCAT(YEAR(NOW())-2, '-11-09'), 9000000000, 'Diagnose Malaria', 7, '1'),
(236, CONCAT(YEAR(NOW())-3, '-11-09'), 9000000000, 'Diagnose Malaria', 8, '1'),
(237, CONCAT(YEAR(NOW())-4, '-11-09'), 9000000000, 'Diagnose Malaria', 10, '1'),
(238, CONCAT(YEAR(NOW())-5, '-11-09'), 9000000000, 'Diagnose Malaria', 20, '1')


ON DUPLICATE KEY UPDATE id = id;

INSERT INTO ovdiagnosis(ID, VisitID, ICDCode)
VALUES
(218, 218, 84.50),
(219, 219, 84.50),
(220, 220, 84.50),
(221, 221, 84.50),
(222, 222, 84.50),
(223, 223, 84.50),
(224, 224, 84.50),
(225, 225, 84.50),
(226, 226, 84.50),
(227, 227, 84.50),
(228, 228, 84.50),

(229, 229, 84.50),
(230, 230, 84.50),
(231, 231, 84.50),
(232, 232, 84.50),
(233, 233, 84.50),
(234, 234, 84.50),
(235, 235, 84.50),
(236, 236, 84.50),
(237, 237, 84.50),
(238, 238, 84.50),
(239, 239, 84.50)
ON DUPLICATE KEY UPDATE VisitID = VALUES(VisitID), ICDCode = VALUES(ICDCode);
