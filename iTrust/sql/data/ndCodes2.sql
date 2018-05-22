INSERT INTO ndcodes(Code, Description) VALUES
('00904-2407','Tetracycline'),
('54868-0955','Isotretinoin')

ON DUPLICATE KEY UPDATE Code = Code;
