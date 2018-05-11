INSERT INTO cptcodes(Code, Description, Attribute) 
VALUES
('1270F','Injection procedure', NULL),
('87','Diagnostic Radiology', NULL),
('90655', 'Influenza virus vaccine, split', 'immunization'),
('90656', 'Influenza virus vaccine, split', 'immunization'),
('90657', 'Influenza virus vaccine, split', 'immunization'),
('90658', 'Influenza virus vaccine, split', 'immunization'),
('90660', 'Influenza virus vaccine, live', 'immunization'),
('90371', 'Hepatitis B', 'immunization'),
('90681', 'Rotavirus', 'immunization'),
('90696', 'Diphtheria, Tetanus, Pertussis', 'immunization'),
('90645', 'Haemophilus influenzae', 'immunization'),
('90669', 'Pneumococcal', 'immunization'),
('90712', 'Poliovirus', 'immunization'),
('90707', 'Measles, Mumps, Rubella', 'immunization'),
('90396', 'Varicella', 'immunization'),
('90633', 'Hepatitis A', 'immunization'),
('90649', 'Human Papillomavirus', 'immunization')
ON DUPLICATE KEY UPDATE Code = Code; 
