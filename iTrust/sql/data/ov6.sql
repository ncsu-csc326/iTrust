/* for UC11-UC37 accept test scenario 4 */

INSERT INTO officevisits(
	id,
	visitDate,
	HCPID,
	notes,
	HospitalID,
	PatientID
)
VALUES (390,'2011-2-2',9000000004,'Second medical visit in two days','2',2);

INSERT INTO ovmedication(
    VisitID, 
    NDCode, 
    StartDate, 
    EndDate, 
    Dosage, 
    Instructions
) 
VALUES (390, '081096', '2011-2-2', '2011-2-9', 200, 'Take once daily with water');



