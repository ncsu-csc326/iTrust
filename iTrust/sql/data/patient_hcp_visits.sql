
INSERT INTO users(MID, password, role, sQuestion, sAnswer)
VALUES
(9000000004, 'pw', 'hcp', 'first letter?', 'a'),
(9000000005, 'pw', 'hcp', 'first letter?', 'a'),
(9010000006, 'pw', 'hcp', 'first letter?', 'a');

INSERT INTO personnel(
	MID,
	AMID,
	role,
	enabled,
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
VALUES 
(9000000004,null,'hcp',0,'Frankenstein','Jason','333 Dark Lane','','Raleigh','NC','27603','333-444-5555','surgeon','jfrankenstein@iTrust.org'),
(9000000005,null,'hcp',0,'Frankenstein','Lauren','333 Dark Lane','','Raleigh','NC','27605','333-444-5555','pediatrician','lfrankenstein@iTrust.org'),
(9010000006,null,'hcp',0,'Shelley','Mary','1313 Cautionary Tale Lane','','Raleigh','NC','27601','333-444-5555','surgeon','mshelly@iTrust.org');

INSERT INTO officevisits(
	id,
	visitDate,
	HCPID,
	notes,
	PatientID,
	HospitalID
)
VALUES 
(7111,'2008-5-01',9000000004,'Get a new doctor',2,1),
(7112,'2008-6-02',9000000005,'Get a competent doctor',2,1),
(7113,'2008-7-03',9010000006,'Get a sane doctor',2,1);
INSERT INTO declaredhcp(PatientID,HCPID) VALUE(2, 9000000004);
