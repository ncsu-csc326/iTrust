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

