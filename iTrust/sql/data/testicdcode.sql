INSERT INTO icdcode (code, name, is_chronic) VALUES
('T000', 'Test code 0', 0),
('T001', 'Test code 1', 1)
ON duplicate key update code=code;