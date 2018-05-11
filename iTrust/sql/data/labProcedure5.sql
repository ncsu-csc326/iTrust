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
	5000000002,
	4,
	"12312-3",
	3,
	true,
	1,
	"This is for lab tech 5000000002",
	"Foobar",
	"2013-10-31 11:00:00",
	4,
	20, /* blaze it */
	9000000001
);