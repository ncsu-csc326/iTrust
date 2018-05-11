/*  Patients:
        1   Random Person
        2   Andy Programmer
        5   Baby Programmer
        

    HCPs:
        9000000000  Kelly Doctor
        9000000003  Gandalf Stormcrow
        9000000004  Antonio Medico
        9000000005  Sarah Soulcrusher
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
    ( 101, 2, 9000000000,  '2005-10-10', 'x', '1'),
    ( 102, 2, 9000000003,  '2005-11-10', 'x', '1'),
    ( 103, 5, 9000000000,  '2005-12-10', 'x', '1'),
    ( 104, 1, 9000000000,  '2006-01-10', 'x', '1')
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
    (100, 101, 2, 9000000000, 9000000003, '2007-07-10', 1, 'A', false, false),
    (101, 102, 2, 9000000003, 9000000000, '2007-08-10', 1, 'B', false, false),
    (102, 103, 5, 9000000000, 9000000003, '2007-09-10', 1, 'C', false, false),
    (103, 104, 1, 9000000000, 9000000004, '2007-10-10', 1, 'D', false, false),
    (104, 103, 2, 9000000000, 9000000003, '2007-11-10', 1, 'D', false, false)
ON DUPLICATE KEY UPDATE id = id
;

