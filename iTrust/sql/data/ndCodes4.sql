INSERT INTO ndcodes(Code, Description) VALUES
('01864-020','Nexium'),
('00882-219','Lantus'),
('63739-291','Oyster Shell Calcium with Vitamin D'),
('05730-150','Advil')

ON DUPLICATE KEY UPDATE Code = Code;