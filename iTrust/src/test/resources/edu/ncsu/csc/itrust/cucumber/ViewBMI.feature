Feature: View BMI
As a patient of HCP
I want to be able to view BMI
So I can determine whether if a dietary treatment is necessary

# testUnderweightEC, testNormalWeightEC, testOverweightEC, testObeseEC, testNullWeightOrHeight
Scenario Outline: Test BMI
	Given user logs in as Kelly Doctor with MID: 9000000000 and Password: pw
	When user navigates to view basic health info page for HCP

	And user enters "<MID>" in Search by name or MID field
	And user selects patient with MID <MID>

	Then the adult records table is visible
	And the office visit at <date> exists in the above records table (if visible)
	And the above office visit (if exists) contains <BMI> as the BMI of the above records table (if visible)
	
	Examples:
		| MID | date                | BMI    |
		#testUnderweightEC
		| 106 | 01/01/2013 12:00 PM | 9.0    |
		| 106 | 01/02/2013 12:00 PM | 0.1    |
		| 106 | 01/03/2013 12:00 PM | 18.4   |
		#testNormalWeightEC
		| 107 | 01/01/2013 12:00 PM | 21.74  |
		| 107 | 01/02/2013 12:00 PM | 18.5   |
		| 107 | 01/03/2013 12:00 PM | 24.9   |
		#testOverweightEC
		| 108 | 01/01/2013 12:00 PM | 27.5   |
		| 108 | 01/02/2013 12:00 PM | 25.0   |
		| 108 | 01/03/2013 12:00 PM | 29.9   |
		#testObeseEC
		| 109 | 01/01/2013 12:00 PM | 50.0   |
		| 109 | 01/02/2013 12:00 PM | 30.0   |
		#testNullWeightOrHeight
		| 110 | 01/01/2013 12:00 PM | 21.76  |
		| 110 | 01/02/2013 12:00 PM | N/A    |
		| 110 | 01/03/2013 12:00 PM | N/A    |