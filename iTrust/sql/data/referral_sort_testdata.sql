/*  Patients:
        1   Random Person
        2   Andy Programmer
        5   Baby Programmer
       22   Fozzie Bear
        

    HCPs:
        9000000000  Kelly Doctor
        9000000003  Gandalf Stormcrow
        9000000004  Antonio Medico
        9000000005  Sarah Soulcrusher
        9000000007  Beaker Beaker
*/

INSERT INTO officevisits(
	id,
    PatientID,
    HCPID,
	visitDate,
	notes,
	HospitalID
)
VALUES 
    ( 101, 1, 9000000003,  '2008-10-10', 'x', '1'),
    ( 102, 2, 9000000003,  '2009-09-14', 'x', '1'),
    ( 122, 22, 9000000003,  '2010-08-18', 'x', '1')
ON DUPLICATE KEY UPDATE id = id
;

INSERT INTO referrals (
    id,
    OVID,
    PatientID,
    SenderID,
    ReceiverID, 
    TimeStamp,
    PriorityCode,
    ReferralDetails, 
    viewed_by_patient,
    viewed_by_HCP
) 
VALUES 
    (101, 101, 1,  9000000003, 9000000000, '2008-10-10', 3, 'Refer this.', false, false),
    (102, 101, 1,  9000000003, 9000000004, '2009-11-30', 2, 'Refer that.', false, false),
    (103, 101, 1,  9000000003, 9000000005, '2010-12-01', 1, 'Refer reefer.', false, false),
    (104, 102, 2,  9000000003, 9000000007, '2009-09-10', 1, 'Dont fear the refer.', false, false),
    (105, 102, 2,  9000000003, 9000000004, '2010-11-10', 2, 'After refer D comes referee.', false, false),
    (106, 102, 2,  9000000003, 9000000005, '2011-12-10', 3, 'My referrals are better than yours.', false, false),
    (107, 122, 22, 9000000003, 9000000004, '2010-08-10', 1, 'Says who and what army?', false, false),
    (108, 122, 22, 9000000003, 9000000000, '2010-10-13', 3, 'Thats what she said', false, false),
    (109, 122, 22, 9000000003, 9000000007, '2011-12-22', 2, 'You are so juvenile.', false, false)
ON DUPLICATE KEY UPDATE id = id
;

