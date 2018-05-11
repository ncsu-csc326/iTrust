INSERT INTO drugreactionoverridecodes(Code, Description) VALUES
('00001','Alerted interaction not clincally significant'),
('00002','Patient currently tolerates the medication or combination'),
('00003','Benefit of drug outweighs disadvantages'),
('00004', 'Patient has tolerated medication or combination in the past'),
('00005', 'Medication list out of date'),
('00006', 'Limited course of treatment'),
('00007', 'No good alternatives available to alerted medication'),
('00008', 'Allergy information inaccurate in patient''s record')


ON DUPLICATE KEY UPDATE Code = Code;
