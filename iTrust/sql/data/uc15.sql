INSERT INTO cptCode(code, name) VALUES
('90467','Hepatitis C Vaccine')
ON DUPLICATE KEY UPDATE code = code;

INSERT INTO icdCode(code, name, is_chronic) VALUES
('U21','Makena Syndrome', 0)
ON DUPLICATE KEY UPDATE code = code;

INSERT INTO ndcodes(Code, Description) VALUES
('60810-9688','Aspirin')
ON DUPLICATE KEY UPDATE code = code;

INSERT INTO loincCode (code, component, kind_of_property, time_aspect, system, scale_type, method_type) VALUES
('66554-4', 'Adamantine', 'PrThr', 'Pt', 'hair', 'Ord', 'Screen');