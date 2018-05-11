/*Inserting dependent Brittany Franco*/
INSERT INTO patients
(MID, 
lastName, 
firstName,
email,
address1,
address2,
city,
state,
phone,
dateofbirth,
Gender)
VALUES
(409,
'Franco', 
'Brittany', 
'brfranco@gmail.com', 
'1333 Who Cares Road', 
'Suite 102', 
'Raleigh', 
'NC', 
'919-971-0000',
'2006-10-25',
'Female')
 ON DUPLICATE KEY UPDATE MID = MID;

INSERT INTO users(MID, password, role, sQuestion, sAnswer, isDependent) 
			VALUES (409, '30c952fab122c3f9759f02a6d95c3758b246b4fee239957b2d4fee46e26170c4', 'patient', 'what is your favorite color?', 'blue', 1)
 ON DUPLICATE KEY UPDATE MID = MID;
 /*password: pw*/
 
 /* Brody Franco Represents Brittany Franco */
INSERT INTO representatives (representerMID, representeeMID)
	VALUES	(407, 409);
	