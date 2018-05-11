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

