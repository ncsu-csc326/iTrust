INSERT INTO ndcodes(Code, Description) VALUES
('01864020','Nexium'),
('00882219','Lantus'),
('63739291','Oyster Shell Calcium with Vitamin D'),
('05730150','Advil')

ON DUPLICATE KEY UPDATE Code = Code;