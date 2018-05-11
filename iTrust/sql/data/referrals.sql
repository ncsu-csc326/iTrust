INSERT INTO referrals(
id,
PatientID,
SenderID,
ReceiverID, 
ReferralDetails, 
OVID,
viewed_by_patient,
viewed_by_HCP,
TimeStamp,
PriorityCode)
VALUES (
1,
2,
9000000000,
9000000003,
'Gandalf will make sure that the virus does not get past your immune system',
955,
false,
false,
'2007-7-15',
1
)ON DUPLICATE KEY UPDATE id = id;

INSERT INTO referrals(
id,
PatientID,
SenderID,
ReceiverID, 
ReferralDetails, 
OVID,
viewed_by_patient,
viewed_by_HCP,
TimeStamp,
PriorityCode)
VALUES (
2,
2,
9000000000,
9000000003,
'Gandalf will take care of you.',
955,
true,
false,
'2007-7-09',
2
)ON DUPLICATE KEY UPDATE id = id;


INSERT INTO referrals(
id,
PatientID,
SenderID,
ReceiverID, 
ReferralDetails, 
OVID,
viewed_by_patient,
viewed_by_HCP,
TimeStamp,
PriorityCode)
VALUES (
3,
2,
9000000000,
9000000003,
'Gandalf will help you defeat the orcs!',
955,
false,
true,
'2007-7-14',
1
)ON DUPLICATE KEY UPDATE id = id;


INSERT INTO referrals(
id,
PatientID,
SenderID,
ReceiverID, 
ReferralDetails, 
OVID,
viewed_by_patient,
viewed_by_HCP,
TimeStamp,
PriorityCode)
VALUES (
4,
2,
9000000000,
9000000003,
'Gandalf is the best doctor ever!!',
955,
true,
true,
'2007-7-10',
3
)ON DUPLICATE KEY UPDATE id = id;


