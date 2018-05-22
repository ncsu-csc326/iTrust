INSERT INTO ndcodes(Code, Description) VALUES
('67877-1191','Ibuprofen')
ON DUPLICATE KEY UPDATE Code = Code;

INSERT INTO druginteractions(FirstDrug, SecondDrug, Description) VALUES
('678771191', '081096', 'May increase the potential for serious gastrointestinal toxicity.')
ON DUPLICATE KEY UPDATE FirstDrug=FirstDrug;