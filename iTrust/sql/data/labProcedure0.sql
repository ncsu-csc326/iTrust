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
	"12345-6",
	3,
	true,
	1,
	"This is a lo pri lab procedure",
	"Foobar",
	"2016-10-31 11:00:00",
	30,
	60,
	9000000001
);