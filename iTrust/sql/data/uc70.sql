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
(336,
'Prentiss', 
'Emily', 
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
			VALUES (336, '30c952fab122c3f9759f02a6d95c3758b246b4fee239957b2d4fee46e26170c4', 'patient', 'how you doin?', 'good');
/*password: pw*/
			
INSERT INTO declaredhcp(PatientID, HCPID) VALUES (336, 9000000071);

INSERT INTO foodEntry(EntryID, DateEaten, MealType, FoodName, Servings, 
	Calories, Fat, Sodium, Carbs, Sugar, Fiber, Protein, PatientID)
		VALUES(5, '2014-03-16', 'Lunch', 'Chocolate Shake', 2, 500, 
		23.5, 259, 66.5, 42.4, 0, 5.9, 336);
		
INSERT INTO designatedNutritionist(PatientID, HCPID) VALUES(336, 9000000071);
		
		
		