INSERT INTO labProcedure (
	labTechnicianID,
	officeVisitID,
	labProcedureCode,
	priority,
	isRestricted,
	status,
	commentary,
	results,
	updatedDate,
	confidenceIntervalLower,
	confidenceIntervalUpper,
	hcpMID
) VALUES (
	5000000001,
	4,
	"23456-7",
	1,
	true,
	2,
	"A hi pri lab procedure",
	"Results are important",
	"2016-10-31 09:30:00",
	80,
	85,
	9000000001
);