INSERT INTO personnel(
MID,
AMID,
role,
lastName, 
firstName, 
address1,
address2,
city,
state,
zip,
phone,
specialty,
email)
VALUES (
9000000071,
null,
'hcp',
'Reid',
'Spencer',
'4321 My Road St',
'PO BOX 2',
'New York',
'NY',
'10453',
'999-888-7777',
'nutritionist',
'kdoctor@iTrust.org'
)ON DUPLICATE KEY UPDATE MID = MID;

INSERT INTO users(MID, password, role, sQuestion, sAnswer) VALUES(9000000071, '30c952fab122c3f9759f02a6d95c3758b246b4fee239957b2d4fee46e26170c4', 'hcp', 'first letter?', 'a')
ON DUPLICATE KEY UPDATE MID = MID;
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
(335,
'Hotchner', 
'Aaron', 
'nobody@gmail.com', 
'1247 Noname Dr', 
'Suite 106', 
'Raleigh', 
'NC', 
'27607-1234', 
'919-971-0000', 
'Mommy Person', 
'704-532-2117', 
'Aetna', 
'1234 Aetna Blvd', 
'Suite 602', 
'Charlotte',
'NC', 
'28215', 
'704-555-1234', 
'ChetumNHowe', 
'1950-05-10',
0,
0,
'AB+',
'African American',
'Female',
'')
 ON DUPLICATE KEY UPDATE MID = MID;
 
 INSERT INTO users(MID, password, role, sQuestion, sAnswer) 
			VALUES (335, '30c952fab122c3f9759f02a6d95c3758b246b4fee239957b2d4fee46e26170c4', 'patient', 'how you doin?', 'good');
/*password: pw*/
			
INSERT INTO declaredhcp(PatientID, HCPID) VALUES (335, 9000000071);
	
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
(333,
'Morgan', 
'Derek', 
'nobody@gmail.com', 
'1247 Noname Dr', 
'Suite 106', 
'Raleigh', 
'NC', 
'27607-1234', 
'919-971-0000', 
'Mommy Person', 
'704-532-2117', 
'Aetna', 
'1234 Aetna Blvd', 
'Suite 602', 
'Charlotte',
'NC', 
'28215', 
'704-555-1234', 
'ChetumNHowe', 
'1950-05-10',
0,
0,
'AB+',
'African American',
'Female',
'')
 ON DUPLICATE KEY UPDATE MID = MID;
 
 INSERT INTO users(MID, password, role, sQuestion, sAnswer) 
			VALUES (333, '30c952fab122c3f9759f02a6d95c3758b246b4fee239957b2d4fee46e26170c4', 'patient', 'how you doin?', 'good');
/*password: pw*/
			
INSERT INTO declaredhcp(PatientID, HCPID) VALUES (333, 9000000071);
			
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
(334,
'Jareau',
'Jennifer', 
'nobody@gmail.com', 
'1247 Noname Dr', 
'Suite 106', 
'Raleigh', 
'NC', 
'27607-1234', 
'919-971-0000', 
'Mommy Person', 
'704-532-2117', 
'Aetna', 
'1234 Aetna Blvd', 
'Suite 602', 
'Charlotte',
'NC', 
'28215', 
'704-555-1234', 
'ChetumNHowe', 
'1950-05-10',
0,
0,
'AB+',
'African American',
'Female',
'')
 ON DUPLICATE KEY UPDATE MID = MID;
 
 INSERT INTO users(MID, password, role, sQuestion, sAnswer) 
			VALUES (334, '30c952fab122c3f9759f02a6d95c3758b246b4fee239957b2d4fee46e26170c4', 'patient', 'how you doin?', 'good');
/*password: pw*/
			
INSERT INTO declaredhcp(PatientID, HCPID) VALUES (334, 9000000071);
			
			
INSERT INTO foodEntry(EntryID, DateEaten, MealType, FoodName, Servings, Calories, Fat, Sodium, Carbs, Sugar, Fiber, Protein, PatientID)
		VALUES(1, '2012-09-30', 'Breakfast', 'Hot dog', 4, 80, 5, 480, 2, 0, 0, 5, 334);
	
INSERT INTO foodEntry(EntryID, DateEaten, MealType, FoodName, Servings, Calories, Fat, Sodium, Carbs, Sugar, Fiber, Protein, PatientID)
		VALUES(2, '2012-09-30', 'Lunch', 'Mango Passionfruit Juice', 1.2, 130, 0, 25, 32, 29, 0, 1, 334);
		
		
INSERT INTO foodEntry(EntryID, DateEaten, MealType, FoodName, Servings, Calories, Fat, Sodium, Carbs, Sugar, Fiber, Protein, PatientID)
		VALUES(3, '2014-04-13', 'Snack', 'Oreos', 53, 140, 7, 90, 21, 13, 1, 0, 335);
	
INSERT INTO foodEntry(EntryID, DateEaten, MealType, FoodName, Servings, Calories, Fat, Sodium, Carbs, Sugar, Fiber, Protein, PatientID)
		VALUES(4, '2013-05-21', 'Breakfast', 'Cheese and Bean Dip', .75, 45, 2, 230, 5, 0, 2, 2, 335);	
		
/* give them all the designated nutritionist of spencer reid */
INSERT INTO designatedNutritionist(PatientID, HCPID) VALUES(333, 9000000071);
INSERT INTO designatedNutritionist(PatientID, HCPID) VALUES(334, 9000000071);
INSERT INTO designatedNutritionist(PatientID, HCPID) VALUES(335, 9000000071);	
		
		
		