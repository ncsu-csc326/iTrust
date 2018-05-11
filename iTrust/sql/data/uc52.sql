/*Create office visits for Caldwell Hudson (PID 102)*/ 
insert into officevisits(id,visitDate,HCPID,notes,HospitalID,PatientID)
	values	(84,'2011-10-01',8000000011,'Office visit recording.','1',102),
			(85,'2011-11-01',8000000011,'Office visit recording.','1',102),
			(86,'2011-12-01',8000000011,'Office visit recording.','1',102),
			(87,'2012-02-02',8000000011,'Office visit recording.','1',102),
			(88,'2013-10-28',8000000011,'Office visit recording.','1',102)
	on duplicate key update id = id;

/*Create office visits for Fulton Gray (PID 103)*/
insert into officevisits(id,visitDate,HCPID,notes,HospitalID,PatientID)
	values	(94,'2011-10-01',8000000011,'Office visit recording.','1',103),
			(95,'2012-10-15',8000000011,'Office visit recording.','1',103),
			(96,'2013-10-14',8000000011,'Office visit recording.','1',103)
	on duplicate key update id = id;

/*Create office visits for Daria Griffin (PID 104)*/
insert into officevisits(id,visitDate,HCPID,notes,HospitalID,PatientID)
	values	(604,'2011-10-10',8000000011,'Office visit recording.','1',104),
			(605,'2012-10-20',8000000011,'Office visit recording.','1',104),
			(606,'2013-10-25',8000000011,'Office visit recording.','1',104)
	on duplicate key update id = id;

/*Create office visit for Thane Ross (PID 105)*/
insert into officevisits(id,visitDate,HCPID,notes,HospitalID,PatientID)
	values (614,'2013-10-25',8000000011,'Office visit recording.','1',105)
	on duplicate key update id = id;

/*Create health metrics for Caldwell Hudson (PID 102)*/
insert into personalhealthinformation(PatientID,OfficeVisitID,HCPID,HeadCircumference,Weight,Height,BloodPressureN,BloodPressureD,Smoker,SmokingStatus,HouseholdSmokingStatus,CholesterolHDL,CholesterolLDL,CholesterolTri,AsOfDate,OfficeVisitDate,BMI)
	values	(102,84,8000000011,14.5,8.3,19.6,0,0,0,0,3,0,0,0,'2011-10-01 15:03:11','2011-10-01', 15.19),
			(102,85,8000000011,15.3,10.3,21.1,0,0,0,0,3,0,0,0,'2011-11-01 09:48:53','2011-11-01', 16.26),
			(102,86,8000000011,16.3,12.1,22.5,0,0,0,0,3,0,0,0,'2011-12-01 10:20:48','2011-12-01', 16.80),
			(102,87,8000000011,17.1,15.8,25.7,0,0,0,0,3,0,0,0,'2012-02-02 14:13:48','2012-02-02',16.82),
			(102,88,8000000011,19.4,30.2,34.7,0,0,0,0,3,0,0,0,'2013-10-28 08:34:21','2013-10-28',17.63)
	on duplicate key update OfficeVisitID = OfficeVisitID;

/*Create health metrics for Fulton Gray (PID 103)*/
insert into personalhealthinformation(PatientID,OfficeVisitID,HCPID,HeadCircumference,Weight,Height,BloodPressureN,BloodPressureD,Smoker,SmokingStatus,HouseholdSmokingStatus,CholesterolHDL,CholesterolLDL,CholesterolTri,AsOfDate,OfficeVisitDate,BMI)
	values	(103,94,8000000011,19.9,36.5,39.3,0,0,0,0,3,0,0,0,'2011-10-01 16:10:23','2011-10-01',16.61),
			(103,95,8000000011,0,35.8,41.3,95,65,0,0,3,0,0,0,'2012-10-15 10:45:19','2012-10-15',14.75),
			(103,96,8000000011,0,37.9,42.9,95,65,0,0,2,0,0,0,'2013-10-14 11:32:39','2013-10-14',14.48)
	on duplicate key update OfficeVisitID = OfficeVisitID;

/*Create health metrics for Daria Griffin (PID 104)*/
insert into personalhealthinformation(PatientID,OfficeVisitID,HCPID,HeadCircumference,Weight,Height,BloodPressureN,BloodPressureD,Smoker,SmokingStatus,HouseholdSmokingStatus,CholesterolHDL,CholesterolLDL,CholesterolTri,AsOfDate,OfficeVisitDate,BMI)
	values	(104,604,8000000011,0,121.3,62.3,105,73,1,1,3,64,102,143,'2011-10-10 13:23:45','2011-10-10',21.97),
			(104,605,8000000011,0,120.7,62.3,107,72,1,2,3,63,103,145,'2012-10-20 17:18:42','2012-10-20',21.86),
			(104,606,8000000011,0,124.3,62.3,100,75,0,3,1,65,102,147,'2013-10-25 14:35:56','2013-10-25',22.51)
	on duplicate key update OfficeVisitID = OfficeVisitID;

/*Create health metrics for Thane Ross (PID 105)*/
insert into personalhealthinformation(PatientID,OfficeVisitID,HCPID,HeadCircumference,Weight,Height,BloodPressureN,BloodPressureD,Smoker,SmokingStatus,HouseholdSmokingStatus,CholesterolHDL,CholesterolLDL,CholesterolTri,AsOfDate,OfficeVisitDate,BMI)
	values (105,614,8000000011,0,210.1,73.1,160,100,0,0,1,37,141,162,'2013-10-25 07:58:02','2013-10-25',27.64)
	on duplicate key update OfficeVisitID = OfficeVisitID;


