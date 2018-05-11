INSERT INTO ndcodes(Code, Description) VALUES
('619580501','Adefovir'),
('081096','Aspirin')

ON DUPLICATE KEY UPDATE Code = Code;
