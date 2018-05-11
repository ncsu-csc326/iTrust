INSERT INTO users(MID, password, role, sQuestion, sAnswer) 
			VALUES (5, '30c952fab122c3f9759f02a6d95c3758b246b4fee239957b2d4fee46e26170c4', 'patient', 'opposite of yin?', 'yang');
/*password: pw*/

INSERT INTO patients
(MID,
lastName, 
firstName,
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
dateofbirth,
mothermid,
fathermid,
bloodtype,
ethnicity,
gender,
topicalnotes)
VALUES
(5,
'Programmer',
'Baby',
'fake@email.com',
'1247 Noname Dr',
'Suite 106',
'Raleigh', 
'NC',
'27606-1234',
'919-971-0000',
'Mum',
'704-532-2117',
'Aetna', 
'1234 Aetna Blvd', 
'Suite 602',
'Charlotte',
'NC',
'28215',
'704-555-1234', 
'ChetumNHowe', 
'1995-05-10',
0,
2,
'AB+',
'African American',
'Female',
'')
;



INSERT INTO representatives(RepresenterMID, RepresenteeMID) VALUES(2, 5);

INSERT INTO declaredhcp(patientid, hcpid) VALUES (5, 9000000000);

INSERT INTO officevisits(id, visitDate, HCPID, notes, PatientID, HospitalID)
VALUES
	(1000, '1995-05-10', 9000000000, 'Hep B Immunization 1', '5', null),
	(1001, '1995-06-10', 9000000000, 'Hep B Immunization 2', '5', null),
	(1002, '1995-11-10', 9000000000, 'Hep B Immunization 3', '5', null),
	(1003, '1995-06-22', 9000000000, 'Rotavirus Immunization 1', '5', null),
	(1004, '1995-09-10', 9000000000, 'Rotavirus Immunization 2', '5', null),
	(1005, '1995-11-10', 9000000000, 'Rotavirus Immunization 3', '5', null),
	(1006, '1995-06-22', 9000000000, 'Diphtheria, Tetanus, Pertussis Immunization 1', '5', null),
	(1007, '1995-09-10', 9000000000, 'Diphtheria, Tetanus, Pertussis Immunization 2', '5', null),
	(1008, '1995-11-10', 9000000000, 'Diphtheria, Tetanus, Pertussis Immunization 3', '5', null),
	(1009, '1996-08-10', 9000000000, 'Diphtheria, Tetanus, Pertussis Immunization 4', '5', null),
	(1010, '1999-05-10', 9000000000, 'Diphtheria, Tetanus, Pertussis Immunization 5', '5', null),
	(1011, '2006-05-10', 9000000000, 'Diphtheria, Tetanus, Pertussis Immunization 6', '5', null),
	(1012, '1995-06-22', 9000000000, 'Haemophilus influenzae Immunization 1', '5', null),
	(1013, '1995-09-10', 9000000000, 'Haemophilus influenzae Immunization 2', '5', null),
	(1014, '1996-05-10', 9000000000, 'Haemophilus influenzae Immunization 3', '5', null),
	(1015, '1995-06-22', 9000000000, 'Pneumococcal Immunization 1', '5', null),
	(1016, '1995-09-10', 9000000000, 'Pneumococcal Immunization 2', '5', null),
	(1017, '1995-11-10', 9000000000, 'Pneumococcal Immunization 3', '5', null),
	(1018, '1996-05-10', 9000000000, 'Pneumococcal Immunization 4', '5', null),
	(1019, '1995-06-22', 9000000000, 'Poliovirus 1', '5', null),
	(1020, '1995-09-10', 9000000000, 'Poliovirus 2', '5', null),
	(1021, '1995-11-10', 9000000000, 'Poliovirus 3', '5', null),
	(1022, '1996-05-10', 9000000000, 'Measles, Mumps, Rubella Immunization 1', '5', null),
	(1023, '1999-05-10', 9000000000, 'Measles, Mumps, Rubella Immunization 2', '5', null),
	(1024, '1996-05-10', 9000000000, 'Varicella Immunization 1', '5', null),
	(1025, '1999-05-10', 9000000000, 'Varicella Immunization 2', '5', null),
	(1026, '1996-05-10', 9000000000, 'Hep A Immunization 1', '5', null),
	(1027, '1996-11-10', 9000000000, 'Hep A Immunization 2', '5', null),
	(1028, '2004-05-10', 9000000000, 'Human Papillomavirus Immunization 1', '5', null),
	(1029, '2004-07-10', 9000000000, 'Human Papillomavirus Immunization 2', '5', null),
	(1030, '2004-11-10', 9000000000, 'Human Papillomavirus Immunization 3', '5', null),
	(1031, '2007-06-07', 9000000000, 'Extra addition', '5', null)
	on duplicate key update id = id;

INSERT INTO ovprocedure(id, visitid, cptcode)
VALUES
	(1000, 1000, "90371"),
	(1001, 1001, "90371"),
	(1002, 1002, "90371"),
	(1003, 1003, "90681"),
	(1004, 1004, "90681"),
	(1005, 1005, "90681"),
	(1006, 1006, "90696"),
	(1007, 1007, "90696"),
	(1008, 1008, "90696"),
	(1009, 1009, "90696"),
	(1010, 1010, "90696"),
	(1011, 1011, "90696"),
	(1012, 1012, "90645"),
	(1013, 1013, "90645"),
	(1014, 1014, "90645"),
	(1015, 1015, "90669"),
	(1016, 1016, "90669"),
	(1017, 1017, "90669"),
	(1018, 1018, "90669"),
	(1019, 1019, "90712"),
	(1020, 1020, "90712"),
	(1021, 1021, "90712"),
	(1022, 1022, "90707"),
	(1023, 1023, "90707"),
	(1024, 1024, "90396"),
	(1025, 1025, "90396"),
	(1026, 1026, "90633"),
	(1027, 1027, "90633"),
	(1028, 1028, "90649"),
	(1029, 1029, "90649"),
	(1030, 1030, "90649");
	
INSERT INTO personalhealthinformation
(PatientID,OfficeVisitID,Height,Weight,Smoker,SmokingStatus,BloodPressureN,BloodPressureD,CholesterolHDL,CholesterolLDL,CholesterolTri,HCPID,AsOfDate,OfficeVisitDate,BMI)
VALUES ( 5,  1031, 60,   200,   0, 9,     190,          100,           500,             239,         290,          9000000000, '2007-06-07 20:33:58','2007-06-07',39.06)
on duplicate key update OfficeVisitID = OfficeVisitID;
