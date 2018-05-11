INSERT INTO patients
(MID, 
firstName,
lastName, 
email,
address1,
address2,
city,
state,
zip,
phone,
eName,
ePhone,
iCName,
iCAddress1,
iCAddress2,
iCCity, 
ICState,
iCZip,
iCPhone,
iCID,
DateOfBirth,
MotherMID,
FatherMID,
BloodType,
Ethnicity,
Gender,
TopicalNotes
)
VALUES (
22,
'Fozzie',
'Bear',
'wakka.wakka@wakka.com',
'344 Henson Street',
'',
'New York',
'NY',
'10001',
'555-555-5555',
'Kermit the Frog',
'555-555-5551',
'IC',
'Street1',
'Street2',
'City',
'PA',
'19003-2715',
'555-555-5555',
'1',
'1976-04-25',
0,
0,
'O-',
'Ursine',
'Male',
"Fozzie has a low tolerance for pain--he simply can\'t bear it.  Wakka, Wakka, Wakka."
)  ON DUPLICATE KEY UPDATE MID = MID;

INSERT INTO users(MID, password, role, sQuestion, sAnswer) 
			VALUES (22, '30c952fab122c3f9759f02a6d95c3758b246b4fee239957b2d4fee46e26170c4', 'patient', 'how you doin?', 'good')
 ON DUPLICATE KEY UPDATE MID = MID;
/*password: pw*/

INSERT INTO officevisits(id,visitDate,HCPID,notes,HospitalID,PatientID)
VALUES (1952,'2010-6-09',9000000007,'Meep meep meep?','1',22),
	   (1953,'2010-10-31',9000000000,'If you say Wakka, Wakka, Wakka one more time...','1',22),
	   (1954,'2010-12-11',9000000003,'Wakka Wakka this!','1',22),
	   (1955,'2011-3-31',9000000007,'MEEP. Meep Meep.','1',22)
		 ON DUPLICATE KEY UPDATE id = id;

INSERT INTO declaredhcp(PatientID,HCPID) VALUE(22, 9000000007)
 ON DUPLICATE KEY UPDATE PatientID = PatientID;


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
    (19520, 1952, 22, 9000000007, 9000000000, '2010-06-10', 2, 'Meep meep meep.', false, false),
    (19521, 1952, 22, 9000000007, 9000000003, '2010-06-12', 3, 'Meep meep meep!', false, false),
    (19522, 1952, 22, 9000000007, 9000000000, '2010-06-14', 3, 'Meep Meep Meep.', false, false),
    (19523, 1952, 22, 9000000007, 9000000003, '2010-06-16', 2, 'Meep MEEP Meep...', false, false),
    (19524, 1952, 22, 9000000007, 9000000000, '2010-06-18', 1, 'meep meep meep', false, false),
    (19525, 1952, 22, 9000000007, 9000000003, '2010-06-20', 1, 'MEEP MEEP MEEP...', false, false),
    (19550, 1955, 22, 9000000007, 9000000000, '2010-06-22', 3, 'Meep meep... Meep.', false, false),
    (19551, 1955, 22, 9000000007, 9000000000, '2010-06-13', 2, 'Meep meep, meep.', false, false)
ON DUPLICATE KEY UPDATE id = id
;

INSERT INTO labprocedure
(PatientMID,LaboratoryProcedureCode,Rights,Status,Commentary,Results,
 NumericalResults, NumericalResultsUnit, LowerBound, UpperBound,
 OfficeVisitID, UpdatedDate, LabTechID, PriorityCode)
VALUES
(22,'10763-1','ALLOWED','In Transit','','',
 '','','','',
 1952,'2010-06-09', 5000000001, 3),
 
(22,'10666-6','ALLOWED','In Transit','','',
 '','','','',
 1952,'2010-06-13', 5000000001, 1),
 
(22,'10640-1','ALLOWED','In Transit','','',
 '','','','',
 1952,'2010-06-17', 5000000002, 2),
 

(22,'10640-1','ALLOWED', 'Received','','',
 '','','','',
 1952,'2010-06-10', 5000000001, 1),
 
(22,'10763-1','ALLOWED', 'Received','','',
 '','','','',
 1952, '2010-06-14', 5000000002, 3),
 
(22,'10763-1','ALLOWED','Received','','',
 '','','','',
 1952,'2010-06-18', 5000000002, 2),
 

(22,'10763-1','ALLOWED','Pending','','',
 '5','g','4','6',
 1952,'2010-06-11', 5000000001, 2),
 
(22,'10763-1','ALLOWED','Pending','','',
 '13','g','11','15',
 1952,'2010-06-15', 5000000001, 1),
 
(22,'10763-1','ALLOWED','Pending','','',
 '3','g','2','4',
 1952,'2010-06-19', 5000000002, 3),
 

(22,'10763-1','ALLOWED','Completed','Meep meep meep.','',
 '7','g','6','8',
 1952,'2010-06-12', 5000000001, 2),
 
(22,'10763-1','ALLOWED','Completed','MEEP MEEP MEEP!','',
 '5.23','g','4.92','6.03',
 1952,'2010-06-16', 5000000001, 3),
 
(22,'10763-1','ALLOWED','Completed','Meep, meep. MEEP!!!','',
 '18','g','10','17256',
 1952,'2010-06-20', 5000000002, 1);



