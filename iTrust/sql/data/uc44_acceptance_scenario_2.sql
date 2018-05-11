INSERT INTO officevisits(
	id,
	visitDate,
	HCPID,
	notes,
	HospitalID,
	PatientID
)
VALUES (44100,'2011-1-28',9000000004,'Has flu','2',1);

INSERT INTO patientspecificinstructions (id, VisitID, Modified, Name, URL, Comment)
VALUES (
    44100, 
    44100, 
    '2011-1-28 12:00:00', 
    'Flu Diet', 
    'http://www.webmd.com/cold-and-flu/flu-guide/what-to-eat-when-you-have-the-flu', 
    'Eat a healthy diet to help you get over the flu faster! Take your vitamins and drink lots of fluids'
);
