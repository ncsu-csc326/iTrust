INSERT INTO ndcodes(Code, Description) VALUES
('009042407','Tetracycline'),
('548680955','Isotretinoin')

ON DUPLICATE KEY UPDATE Code = Code;
