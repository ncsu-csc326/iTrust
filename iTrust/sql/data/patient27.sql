/*Inserting patient Brody Franco*/
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
dateofbirth,
Gender)
VALUES
(407,
'Franco', 
'Brody', 
'bfranco@gmail.com', 
'1333 Who Cares Road', 
'Suite 102', 
'Raleigh', 
'NC', 
'27606-1234', 
'919-971-0000',
'1993-10-25',
'Male')
 ON DUPLICATE KEY UPDATE MID = MID;

INSERT INTO users(MID, password, role, sQuestion, sAnswer) 
			VALUES (407, '30c952fab122c3f9759f02a6d95c3758b246b4fee239957b2d4fee46e26170c4', 'patient', 'what is your favorite color?', 'blue')
 ON DUPLICATE KEY UPDATE MID = MID;
 /*password: pw*/