
INSERT INTO officevisits(
	id,
	visitDate,
	HCPID,
	notes,
	HospitalID,
	PatientID
)
VALUES  (44510,'2007-6-10',9000000005,'What a cromulent visit.','2',2),
        (44520,'2005-10-10',9000000005,'One word: kwyjibo.','2',1),
        (44530,'2007-6-9',9000000000,'I was saying "Boo-urns!"','2',2);


INSERT INTO patientspecificinstructions (id, VisitID, Modified, Name, URL, Comment)
VALUES (
    44510, 
    44510, 
    '2007-6-14 14:56:00', 
    'Heartburn Information', 
    'http://www.mayoclinic.com/health/heartburn-gerd/DS00095/DSECTION=lifestyle-and-home-remedies', 
    'That means no more chili fries!'
),(
    44520, 
    44520, 
    '2005-10-12 10:35:00', 
    'Glucose Testing Information', 
    'http://www.mayoclinic.com/health/glucose-tolerance-test/MY00145', 
    'This should provide you more information about your upcoming test.'
),(
    44530, 
    44530, 
    '2007-6-10 13:11:00', 
    'Vein Procedure Resource', 
    'http://www.mayoclinic.com/health/sclerotherapy/MY01302', 
    'Here is some reference information about the issue we talked about during your office visit.'
);
