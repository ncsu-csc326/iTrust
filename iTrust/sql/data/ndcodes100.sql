INSERT INTO ndcodes(Code, Description) VALUES
('483013420','Midichlomaxene'),
('483012382','Midichlominene')
ON DUPLICATE KEY UPDATE Code = Code;