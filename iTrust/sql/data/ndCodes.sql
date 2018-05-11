INSERT INTO ndcodes(Code, Description) VALUES
('009042407','Tetracycline'),
('081096','Aspirin'),
('647641512','Prioglitazone'),
('548684985', 'Citalopram Hydrobromide'),
('00060431', 'Benzoyl Peroxide')

ON DUPLICATE KEY UPDATE Code = Code;
