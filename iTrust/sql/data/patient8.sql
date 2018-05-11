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
(8,
'C',
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
'Male',
'')
;

INSERT INTO users(MID, password, role, sQuestion, sAnswer) 
			VALUES (8, '30c952fab122c3f9759f02a6d95c3758b246b4fee239957b2d4fee46e26170c4', 'patient', 'opposite of yin?', 'yang');
/*password: pw*/
INSERT INTO representatives(RepresenterMID, RepresenteeMID) VALUES(2, 8);

INSERT INTO declaredhcp(patientid, hcpid) VALUES (8, 9000000000);

INSERT INTO officevisits(id, visitDate, HCPID, notes, PatientID, HospitalID)
VALUES
	(4000, '1995-05-10', 9000000000, 'Hep B Immunization 1', '8', null),
	(4003, '1995-06-22', 9000000000, 'Rotavirus Immunization 1', '8', null),
	(4004, '1995-09-10', 9000000000, 'Rotavirus Immunization 2', '8', null),
	(4005, '1995-11-10', 9000000000, 'Rotavirus Immunization 3', '8', null),
	(4012, '1995-06-22', 9000000000, 'Haemophilus influenzae Immunization 1', '8', null),
	(4013, '1995-09-10', 9000000000, 'Haemophilus influenzae Immunization 2', '8', null),
	(4014, '1996-05-10', 9000000000, 'Haemophilus influenzae Immunization 3', '8', null),
	(4015, '1995-06-22', 9000000000, 'Pneumococcal Immunization 1', '8', null),
	(4016, '1995-09-10', 9000000000, 'Pneumococcal Immunization 2', '8', null),
	(4017, '1995-11-10', 9000000000, 'Pneumococcal Immunization 3', '8', null),
	(4018, '1996-05-10', 9000000000, 'Pneumococcal Immunization 4', '8', null),
	(4019, '1995-06-22', 9000000000, 'Poliovirus 1', '8', null),
	(4020, '1995-09-10', 9000000000, 'Poliovirus 2', '8', null),
	(4021, '1995-11-10', 9000000000, 'Poliovirus 3', '8', null),
	(4022, '1996-05-10', 9000000000, 'Measles, Mumps, Rubella Immunization 1', '8', null),
	(4023, '1999-05-10', 9000000000, 'Measles, Mumps, Rubella Immunization 2', '8', null),
	(4026, '1996-05-10', 9000000000, 'Hep A Immunization 1', '8', null);


INSERT INTO ovprocedure(id, visitid, cptcode)
VALUES
	(4000, 4000, "90371"),
	(4003, 4003, "90681"),
	(4004, 4004, "90681"),
	(4005, 4005, "90681"),
	(4012, 4012, "90645"),
	(4013, 4013, "90645"),
	(4014, 4014, "90645"),
	(4015, 4015, "90669"),
	(4016, 4016, "90669"),
	(4017, 4017, "90669"),
	(4018, 4018, "90669"),
	(4019, 4019, "90712"),
	(4020, 4020, "90712"),
	(4021, 4021, "90712"),
	(4022, 4022, "90707"),
	(4023, 4023, "90707"),
	(4026, 4026, "90633");

