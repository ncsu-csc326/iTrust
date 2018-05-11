INSERT INTO recordsrelease(releaseID, requestDate, pid, releaseHospitalID, recHospitalName, recHospitalAddress, docFirstName, docLastName, docPhone, docEmail, justification, status)
	VALUES (10, "2007-09-27 04:20:00", 1, "9", "Holly Hill", "828 Wake Forest Road, Raleigh, NC 27612", "Meryl", "Stewart", "919-818-2142", "meryl.stewart@hollyhill.org", null, 0),
		(11, "2008-04-30 13:21:45", 1, "9", "Holly Hill", "828 Wake Forest Road, Raleigh, NC 27612", "Robert", "Larenzo", "919-782-4344", "robert.larenzo@hollyhill.org", "Secondary checkup", 0),
		(12, "2008-05-03 07:26:55", 1, "1", "East Health Services", "9002 Asheville Avenue, Cary, NC 27511", "Harold", "McClain", "916-991-4124", "hmcclain@easternhealth.com", "Referred for services", 1),
		(25, "2013-10-18 15:23:19", 102, "1", "Hudson Memorial Hospital", "9128 82nd Street, New York, NY 01829", "Elizabeth", "Synder", "516-712-1229", "synder@hudsonmem.org", null, 2),
		(26, "2013-10-18 17:38:01", 102, "1", "Fairfax Chiropractic", "72 Waywind Street, Hartford, CT 01241", "Michael", "Garrison", "528-912-9103", "mkgarrison@fairfaxchiro.com", "Major back pain referral", 0),
		(27, "2012-08-17 19:04:47", 102, "1", "Hartford Radiology Ltd.", "8941 Hargett Way, Hartford, CT 01243", "Monica", "Brown", "329-818-7734", "monica.brown@hartfordradiology.com", null, 1),
		(28, "2013-02-09 10:32:47", 102, "1", "Hartford Radiology Ltd.", "8941 Hargett Way, Hartford, CT 01243", "Monica", "Brown", "329-818-7734", "monica.brown@hartfordradiology.com", null, 1),
		(65, "2009-06-23 13:45:32", 22, "4", "Pokemon Center", "Palet Town", "Nurse", "Joy", "123-456-7890", "joynurse@pokemon.com", "Revive my pokemon!", 0),
		(66, "2013-11-23 14:32:12", 22, "1", "Rex Hospital", "1829 Lake Boone Trail, Raleigh, NC 27612", "Connor", "DunBar", "919-733-1991", "c.dunbar@rexhospital.org", "Blood test requested from specialist", 0),
		(133, "2010-08-08 20:09:16", 22, "1", "Keller Drive Heart Specialists", "622 Center Wood Avenue, Savannah, GA 42991", "Brian", "McIntyre", "744-239-9117", "mcintyre@kellerheart.com", null, 0);
INSERT INTO hcprelations(HCP, UAP) VALUES(9000000000, 8000000009);