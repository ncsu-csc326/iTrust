DELETE FROM personalhealthinformation WHERE PatientID = 2;

insert into officevisits(id,visitDate,HCPID,notes,HospitalID,PatientID)
	values	(700, CONCAT(YEAR(NOW())-2, '-08-12 08:34:58'),9000000000,'Office visit recording.','',2),
			(701, CONCAT(YEAR(NOW())-2, '-10-30 10:54:22'),9000000000,'Office visit recording.','',2),
			(702, CONCAT(YEAR(NOW())-2, '-11-30 14:14:22'),9000000000,'Office visit recording.','',2),
			(703, CONCAT(YEAR(NOW())-1, '-01-04 12:54:48'),9000000000,'Office visit recording.','',2),
			(704, CONCAT(YEAR(NOW())-1, '-11-19 12:32:48'),9000000000,'Office visit recording.','',2),
			(705, '2007-08-12 08:34:58',9000000000,'Office visit recording.','',2),
			(706, '2007-10-30 10:54:22',9000000000,'Office visit recording.','',2)
	on duplicate key update id = id;
	
INSERT INTO personalhealthinformation 
	   (PatientID,OfficeVisitID,Height,Weight,Smoker,BloodPressureN,BloodPressureD,CholesterolHDL,CholesterolLDL,CholesterolTri,HCPID, AsOfDate, OfficeVisitDate)
VALUES ( 2, 700, 62.2,   205.5,   1,      165,          220,           40,             255,         230,          9000000000, CONCAT(YEAR(NOW())-2, '-08-12 08:34:58') , CONCAT(YEAR(NOW())-2, '-08-12')),
	   ( 2, 701, 62.0,   209.1,   1,      170,          200,           70,             200,         290,          9000000000, CONCAT(YEAR(NOW())-2, '-10-30 10:54:22'),  CONCAT(YEAR(NOW())-2, '-10-30')),
	   ( 2,  702, 62.0,   220.5,   1,      190,          300,           35,             219,         160,          9000000000, CONCAT(YEAR(NOW())-2, '-11-30 14:14:22'), CONCAT(YEAR(NOW())-2, '-11-30')),
	   ( 2,  703, 62.0,   218.5,   0,      165,          220,           30,             255,         220,          9000000000, CONCAT(YEAR(NOW())-1, '-01-04 12:54:48'), CONCAT(YEAR(NOW())-1, '-01-04')),
	   ( 2,  704, 62.4,    0.0,     0,      165,          220,           30,             255,         220,          9000000000, CONCAT(YEAR(NOW())-1, '-11-19 12:32:48'), CONCAT(YEAR(NOW())-1, '-11-19')),
	   ( 2,  705, 62.2,   205.5,   1,      165,          220,           40,             255,         230,          9000000000, '2007-08-12 08:34:58', '2007-08-12'),
	   ( 2,  706, 62.0,   209.1,   1,      170,          200,           70,             200,         290,          9000000000, '2007-10-30 10:54:22', '2007-10-30')
on duplicate key update OfficeVisitID = OfficeVisitID;