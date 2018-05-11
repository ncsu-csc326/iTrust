/* Bob Ross */
INSERT INTO users (MID, Password, Role, sQuestion, sAnswer, isDependent)
	VALUES	(750, '30c952fab122c3f9759f02a6d95c3758b246b4fee239957b2d4fee46e26170c4', 'patient', '2+2?', '4', 0)
	ON DUPLICATE KEY UPDATE MID = MID;
	
INSERT INTO patients (MID, lastName, firstName, DateOfBirth, TopicalNotes)
	VALUES	(750, 'Ross', 'Bob', '1990-01-01', 'Happy trees abound!')
	ON DUPLICATE KEY UPDATE MID = MID;
	
/* Billy Ross */
INSERT INTO users (MID, Password, Role, sQuestion, sAnswer, isDependent)
	VALUES	(751, '30c952fab122c3f9759f02a6d95c3758b246b4fee239957b2d4fee46e26170c4', 'patient', '2+2?', '4', 1)
	ON DUPLICATE KEY UPDATE MID = MID;
	
INSERT INTO patients (MID, lastName, firstName, DateOfBirth, TopicalNotes)
	VALUES	(751, 'Ross', 'Billy', '2000-01-01', 'Happy tree jr.')
	ON DUPLICATE KEY UPDATE MID = MID;
	
INSERT INTO personalhealthinformation(PatientID,OfficeVisitID,HCPID,HeadCircumference,Weight,Height,BloodPressureN,BloodPressureD,Smoker,SmokingStatus,HouseholdSmokingStatus,CholesterolHDL,CholesterolLDL,CholesterolTri,AsOfDate,OfficeVisitDate,BMI)
	VALUES	(751,770,9000000000,14.5,8.3,19.6,0,0,0,0,3,0,0,0,'2011-10-01 15:03:11','2011-10-01', 15.19)
	on duplicate key update OfficeVisitID = OfficeVisitID;	
	
INSERT INTO officevisits (ID, visitDate, HCPID, notes, PatientID, HospitalID, IsERIncident)
	VALUES	(770, '2013-01-01', 9000000000, 'Consumed too much happy tree in one sitting', 751, '1', 0)
	ON DUPLICATE KEY UPDATE ID = ID;
	
INSERT INTO recordsrelease (releaseID, requestDate, pid, releaseHospitalID, recHospitalName, recHospitalAddress, docFirstName, docLastName, docPhone, docEmail, justification, status)
	VALUES	(70, '2013-02-02 10:30:00', 751, '1', 'Rex Hospital', '123 Broad St.', 'Holy', 'Cannoli', '555-666-7777', 'a@b.com', 'Moving', 1)
	ON DUPLICATE KEY UPDATE releaseID = releaseID;
	
INSERT INTO recordsrelease (releaseID, requestDate, pid, releaseHospitalID, recHospitalName, recHospitalAddress, docFirstName, docLastName, docPhone, docEmail, justification, status)
	VALUES	(71, '2013-03-03 10:30:00', 751, '1', 'Raptor Hospital', '123 Elf St.', 'Doctor', 'Unicorn', '666-777-8888', 'unicorn.power@rainbowcare.com', 'Second opinion', 0)
	ON DUPLICATE KEY UPDATE releaseID = releaseID;
	
/* Bob Ross Represents Billy Ross */
INSERT INTO representatives (representerMID, representeeMID)
	VALUES	(750, 751);