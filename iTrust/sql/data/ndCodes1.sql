INSERT INTO ndcodes(Code, Description) VALUES
('61958-0501','Adefovir'),
('08109-6','Aspirin')

ON DUPLICATE KEY UPDATE Code = Code;
