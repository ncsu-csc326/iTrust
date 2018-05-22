INSERT INTO ndcodes(Code, Description) VALUES
('48301-3420','Midichlomaxene'),
('48301-2382','Midichlominene')
ON DUPLICATE KEY UPDATE Code = Code;