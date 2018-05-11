

INSERT INTO officevisits(ID, visitDate, HCPID, notes, PatientID, HospitalID)
VALUES
(107, '2007-03-09', 9000000004, 'Diagnose Echovirus', 2, '1'),
(108, '2007-03-10', 9000000004, 'Diagnose Echovirus', 3, '1'),
(109, '2008-01-21', 9000000005, 'Diagnose Echovirus', 4, '1'),
(110, '2001-01-01', 9000000000, 'Diagnose Echovirus', 2, '1'),
(112, '2008-02-21', 9000000004, 'Diagnose Echovirus', 2, '1'),
(113, '2008-05-05', 9000000000, 'Diagnose Echovirus', 1, '1'),

(114, '2011-08-30', 9000000000, 'Diagnose Influenza', 2, '1'),
(115, '2011-09-04', 9000000000, 'Diagnose Influenza', 3, '1'),
(116, '2011-07-15', 9000000004, 'Diagnose Influenza', 1, '1'),
(117, '2011-09-02', 9000000008, 'Diagnose Influenza', 25, '1'),

(118, '2011-09-24', 9000000000, 'Diagnose Malaria', 2, '1'),
(119, '2011-07-17', 9000000000, 'Diagnose Malaria', 3, '1'),
(120, '2011-07-22', 9000000004, 'Diagnose Malaria', 1, '1'),
(121, '2011-08-02', 9000000000, 'Diagnose Malaria', 25, '1'),

(122, '2011-08-25', 9000000000, 'Diagnose Mumps', 2, '1'),
(123, '2011-09-09', 9000000000, 'Diagnose Mumps', 3, '1'),
(124, '2011-09-12', 9000000000, 'Diagnose Mumps', 25, '1')

ON DUPLICATE KEY UPDATE id = id;

INSERT INTO ovdiagnosis(ID, VisitID, ICDCode)
VALUES
(107, 107, 79.10),
(108, 108, 79.10),
(109, 109, 79.10),
(110, 110, 79.10),
(112, 112, 79.10),
(113, 113, 79.10),

(114, 114, 487.00),
(115, 115, 487.00),
(116, 116, 487.00),
(117, 117, 487.00),

(118, 118, 84.50),
(119, 119, 84.50),
(120, 120, 84.50),
(121, 121, 84.50),

(122, 122, 72.00),
(123, 123, 72.00),
(124, 124, 72.00)
ON DUPLICATE KEY UPDATE VisitID = VALUES(VisitID), ICDCode = VALUES(ICDCode);

INSERT INTO labprocedure (PatientMID, LaboratoryProcedureCode, Rights, Status, Commentary, Results, OfficeVisitID, UpdatedDate,
                          NumericalResults, NumericalResultsUnit, LowerBound, UpperBound)
VALUES
(4, '10640-1','ALLOWED','Completed','','',109,'2008-01-22 20:20:45.0', '10', 'g', '9', '11');

INSERT INTO ovmedication(ID, VisitID, NDCode, StartDate, EndDate, Dosage, Instructions)
VALUES
(4, 107, '009042407', '2007-03-10', '2007-04-10', 5, 'Take twice daily'),
(5, 108, '009042407', '2007-03-11', '2007-04-11', 5, 'Take twice daily')
ON DUPLICATE KEY UPDATE id = id;

INSERT INTO ovsurvey(VisitID, SurveyDate, WaitingRoomMinutes, ExamRoomMinutes, VisitSatisfaction, TreatmentSatisfaction)
VALUES
(107, '2008-09-25 02:00:00.0', 15, 10, 2, 4),
(108, '2008-09-25 03:00:00.0', 17, 25, 4, 4)
ON DUPLICATE KEY UPDATE VisitID = VisitID;
