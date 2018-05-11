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
(341,
'Rodriguez', 
'Bender', 
'bender@benderisgreat.com', 
'1024 Byte Ln', 
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
'1975-03-10',
0,
0,
'AB+',
'African American',
'Female',
'')
 ON DUPLICATE KEY UPDATE MID = MID;
 
 INSERT INTO users(MID, password, role, sQuestion, sAnswer) 
			VALUES (341, '30c952fab122c3f9759f02a6d95c3758b246b4fee239957b2d4fee46e26170c4', 'patient', 'how you doin?', 'good');
/*password: pw*/
			
INSERT INTO personalhealthinformation(PatientID, Height, Weight, HCPID)
			VALUES (341, 65, 112.5, 9000000071);
		
INSERT INTO designatedNutritionist(PatientID, HCPID) VALUES(341, 9000000071);
			
INSERT INTO declaredhcp(PatientID, HCPID) VALUES (341, 9000000071);
	
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
(342,
'Farnsworth', 
'Hubert', 
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
'1955-03-10',
0,
0,
'AB+',
'African American',
'Female',
'')
 ON DUPLICATE KEY UPDATE MID = MID;
 
 INSERT INTO users(MID, password, role, sQuestion, sAnswer) 
			VALUES (342, '30c952fab122c3f9759f02a6d95c3758b246b4fee239957b2d4fee46e26170c4', 'patient', 'how you doin?', 'good');
			
INSERT INTO personalhealthinformation(PatientID, Height, Weight, HCPID)
			VALUES (342, 65, 112.5, 9000000071);
		
INSERT INTO designatedNutritionist(PatientID, HCPID) VALUES(342, 9000000071);
/*password: pw*/
			
INSERT INTO declaredhcp(PatientID, HCPID) VALUES (342, 9000000071);

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
(343,
'Wong', 
'Amy', 
'amy@kappakappawong.com', 
'1024 Byte Ln', 
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
'1975-03-10',
0,
0,
'AB+',
'African American',
'Female',
'')
 ON DUPLICATE KEY UPDATE MID = MID;
 
 INSERT INTO users(MID, password, role, sQuestion, sAnswer) 
			VALUES (343, '30c952fab122c3f9759f02a6d95c3758b246b4fee239957b2d4fee46e26170c4', 'patient', 'how you doin?', 'good');
/*password: pw*/
			
INSERT INTO personalhealthinformation(PatientID, Height, Weight, HCPID)
			VALUES (343, 65, 112.5, 9000000071);
		
INSERT INTO designatedNutritionist(PatientID, HCPID) VALUES(343, 9000000071);
			
INSERT INTO declaredhcp(PatientID, HCPID) VALUES (343, 9000000071);
			
INSERT INTO foodEntry(EntryID, DateEaten, MealType, FoodName, Servings, Calories, Fat, Sodium, Carbs, Sugar, Fiber, Protein, PatientID)
		VALUES(6, '2014-05-05', 'Breakfast', 'Cereal', 1, 80, 17, 480, 55, 0, 0, 22, 341);
		
INSERT INTO foodEntry(EntryID, DateEaten, MealType, FoodName, Servings, Calories, Fat, Sodium, Carbs, Sugar, Fiber, Protein, PatientID)
		VALUES(7, '2014-05-05', 'Lunch', 'Hot dog', 2, 80, 8, 480, 25, 0, 0, 9, 341);
		
INSERT INTO foodEntry(EntryID, DateEaten, MealType, FoodName, Servings, Calories, Fat, Sodium, Carbs, Sugar, Fiber, Protein, PatientID)
		VALUES(8, '2014-05-05', 'Dinner', 'Salad', 4, 80, 5, 480, 15, 0, 0, 5, 341);
		
INSERT INTO foodEntry(EntryID, DateEaten, MealType, FoodName, Servings, Calories, Fat, Sodium, Carbs, Sugar, Fiber, Protein, PatientID)
		VALUES(9, '2014-05-06', 'Breakfast', 'Cereal', 1, 80, 16, 480, 53, 0, 0, 21, 341);
		
INSERT INTO foodEntry(EntryID, DateEaten, MealType, FoodName, Servings, Calories, Fat, Sodium, Carbs, Sugar, Fiber, Protein, PatientID)
		VALUES(10, '2014-05-06', 'Lunch', 'Hot dog', 2, 80, 9, 480, 23, 0, 0, 10, 341);
		
INSERT INTO foodEntry(EntryID, DateEaten, MealType, FoodName, Servings, Calories, Fat, Sodium, Carbs, Sugar, Fiber, Protein, PatientID)
		VALUES(11, '2014-05-06', 'Dinner', 'Salad', 4, 80, 5, 480, 15, 0, 0, 6, 341);
		
INSERT INTO foodEntry(EntryID, DateEaten, MealType, FoodName, Servings, Calories, Fat, Sodium, Carbs, Sugar, Fiber, Protein, PatientID)
		VALUES(12, '2014-05-07', 'Breakfast', 'Cereal', 1, 80, 15, 480, 58, 0, 0, 20, 341);
		
INSERT INTO foodEntry(EntryID, DateEaten, MealType, FoodName, Servings, Calories, Fat, Sodium, Carbs, Sugar, Fiber, Protein, PatientID)
		VALUES(13, '2014-05-07', 'Lunch', 'Hot dog', 2, 80, 9, 480, 22, 0, 0, 8, 341);
		
INSERT INTO foodEntry(EntryID, DateEaten, MealType, FoodName, Servings, Calories, Fat, Sodium, Carbs, Sugar, Fiber, Protein, PatientID)
		VALUES(14, '2014-05-07', 'Dinner', 'Salad', 4, 80, 6, 480, 15, 0, 0, 7, 341);
		
INSERT INTO foodEntry(EntryID, DateEaten, MealType, FoodName, Servings, Calories, Fat, Sodium, Carbs, Sugar, Fiber, Protein, PatientID)
		VALUES(15, '2014-05-08', 'Breakfast', 'Cereal', 1, 80, 16, 480, 48, 0, 0, 25, 341);
		
INSERT INTO foodEntry(EntryID, DateEaten, MealType, FoodName, Servings, Calories, Fat, Sodium, Carbs, Sugar, Fiber, Protein, PatientID)
		VALUES(16, '2014-05-08', 'Lunch', 'Hot dog', 2, 80, 8, 480, 21, 0, 0, 11, 341);
		
INSERT INTO foodEntry(EntryID, DateEaten, MealType, FoodName, Servings, Calories, Fat, Sodium, Carbs, Sugar, Fiber, Protein, PatientID)
		VALUES(17, '2014-05-08', 'Dinner', 'Salad', 4, 80, 5, 480, 17, 0, 0, 6, 341);
		
INSERT INTO foodEntry(EntryID, DateEaten, MealType, FoodName, Servings, Calories, Fat, Sodium, Carbs, Sugar, Fiber, Protein, PatientID)
		VALUES(18, '2014-05-09', 'Breakfast', 'Cereal', 1, 80, 16, 480, 52, 0, 0, 26, 341);
		
INSERT INTO foodEntry(EntryID, DateEaten, MealType, FoodName, Servings, Calories, Fat, Sodium, Carbs, Sugar, Fiber, Protein, PatientID)
		VALUES(19, '2014-05-09', 'Lunch', 'Hot dog', 2, 80, 9, 480, 28, 0, 0, 10, 341);
		
INSERT INTO foodEntry(EntryID, DateEaten, MealType, FoodName, Servings, Calories, Fat, Sodium, Carbs, Sugar, Fiber, Protein, PatientID)
		VALUES(20, '2014-05-09', 'Dinner', 'Salad', 6, 80, 5, 480, 13, 0, 0, 6, 341);


INSERT INTO foodEntry(EntryID, DateEaten, MealType, FoodName, Servings, Calories, Fat, Sodium, Carbs, Sugar, Fiber, Protein, PatientID)
		VALUES(21, '2014-05-05', 'Breakfast', 'Cereal', 1, 80, 25, 480, 74, 0, 0, 40, 342);
		
INSERT INTO foodEntry(EntryID, DateEaten, MealType, FoodName, Servings, Calories, Fat, Sodium, Carbs, Sugar, Fiber, Protein, PatientID)
		VALUES(22, '2014-05-05', 'Lunch', 'Hot dog', 2, 80, 12, 480, 14, 0, 0, 38, 342);
		
INSERT INTO foodEntry(EntryID, DateEaten, MealType, FoodName, Servings, Calories, Fat, Sodium, Carbs, Sugar, Fiber, Protein, PatientID)
		VALUES(23, '2014-05-05', 'Dinner', 'Salad', 4, 80, 12, 480, 20, 0, 0, 12, 342);
		
INSERT INTO foodEntry(EntryID, DateEaten, MealType, FoodName, Servings, Calories, Fat, Sodium, Carbs, Sugar, Fiber, Protein, PatientID)
		VALUES(24, '2014-05-06', 'Breakfast', 'Cereal', 1, 80, 10, 480, 105, 0, 0, 85, 342);
		
INSERT INTO foodEntry(EntryID, DateEaten, MealType, FoodName, Servings, Calories, Fat, Sodium, Carbs, Sugar, Fiber, Protein, PatientID)
		VALUES(25, '2014-05-06', 'Lunch', 'Hot dog', 2, 80, 4, 480, 205, 0, 0, 15, 342);
		
INSERT INTO foodEntry(EntryID, DateEaten, MealType, FoodName, Servings, Calories, Fat, Sodium, Carbs, Sugar, Fiber, Protein, PatientID)
		VALUES(26, '2014-05-06', 'Dinner', 'Salad', 4, 80, 10, 480, 20, 0, 0, 30, 342);
		
INSERT INTO foodEntry(EntryID, DateEaten, MealType, FoodName, Servings, Calories, Fat, Sodium, Carbs, Sugar, Fiber, Protein, PatientID)
		VALUES(27, '2014-05-07', 'Breakfast', 'Cereal', 1, 80, 105, 480, 25, 0, 0, 52, 342);
		
INSERT INTO foodEntry(EntryID, DateEaten, MealType, FoodName, Servings, Calories, Fat, Sodium, Carbs, Sugar, Fiber, Protein, PatientID)
		VALUES(28, '2014-05-07', 'Lunch', 'Hot dog', 2, 80, 9, 480, 27, 0, 0, 7, 342);
		
INSERT INTO foodEntry(EntryID, DateEaten, MealType, FoodName, Servings, Calories, Fat, Sodium, Carbs, Sugar, Fiber, Protein, PatientID)
		VALUES(29, '2014-05-07', 'Dinner', 'Salad', 4, 80, 6, 480, 25, 0, 0, 5, 342);
		
INSERT INTO foodEntry(EntryID, DateEaten, MealType, FoodName, Servings, Calories, Fat, Sodium, Carbs, Sugar, Fiber, Protein, PatientID)
		VALUES(30, '2014-05-08', 'Breakfast', 'Cereal', 1, 80, 17, 480, 55, 0, 0, 22, 342);
		
INSERT INTO foodEntry(EntryID, DateEaten, MealType, FoodName, Servings, Calories, Fat, Sodium, Carbs, Sugar, Fiber, Protein, PatientID)
		VALUES(31, '2014-05-08', 'Lunch', 'Hot dog', 2, 80, 10, 480, 28, 0, 0, 12, 342);
		
INSERT INTO foodEntry(EntryID, DateEaten, MealType, FoodName, Servings, Calories, Fat, Sodium, Carbs, Sugar, Fiber, Protein, PatientID)
		VALUES(32, '2014-05-08', 'Dinner', 'Salad', 4, 80, 7, 480, 25, 0, 0, 6, 342);
		
INSERT INTO foodEntry(EntryID, DateEaten, MealType, FoodName, Servings, Calories, Fat, Sodium, Carbs, Sugar, Fiber, Protein, PatientID)
		VALUES(33, '2014-05-09', 'Breakfast', 'Cereal', 1, 80, 27, 480, 22, 0, 0, 42, 342);
		
INSERT INTO foodEntry(EntryID, DateEaten, MealType, FoodName, Servings, Calories, Fat, Sodium, Carbs, Sugar, Fiber, Protein, PatientID)
		VALUES(34, '2014-05-09', 'Lunch', 'Hot dog', 2, 80, 10, 480, 25, 0, 0, 7, 342);
		
INSERT INTO foodEntry(EntryID, DateEaten, MealType, FoodName, Servings, Calories, Fat, Sodium, Carbs, Sugar, Fiber, Protein, PatientID)
		VALUES(35, '2014-05-09', 'Dinner', 'Salad', 4, 80, 3, 480, 25, 0, 0, 7, 342);
		
		
		
		