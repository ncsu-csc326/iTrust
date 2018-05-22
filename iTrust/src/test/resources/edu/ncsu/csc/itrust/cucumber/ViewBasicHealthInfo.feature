Feature: View Basic Health Information
As a patient or HCP
I want to view past basic health information
So I can view get a better understand of the health history

Background:
	Given standard data is loaded
	And UC51 data is loaded
	And UC52 data is loaded

# UC52: TestOfficeVisit4MonthOldViewHealthMetrics
Scenario: Test Office Visit 4 Month Old View Health Metrics
	Given user logs in as Shelly Vang with MID: 8000000011 and Password: pw
	When user navigates to edit basic health info page
	And user enters "101" in Search by name or MID field
	And user selects patient with MID 101
	And user clicks on Click Here to Create a New Office Visit Button
	And user enters "10/01/2013 12:00 PM" as the Office Visit Date
	And user selects "central hospital" with id "9" as the Location
	And user enters "Brynn can start eating rice cereal mixed with breast milk or formula once a day" as Notes
	And user clicks the "Create" button
	And user enters "16.5" as the weight
	And user enters "22.3" as the length
	And user enters "16.1" as the head circumference
	And user selects "1" as the household smoking status
	And user clicks on the Add Record button
	And user navigates to view basic health info page for HCP

	Then the baby records table is visible
	And the above records table (if visible) contains 1 entry(ies) (if visible)
	And the office visit at 10/01/2013 12:00 PM exists in the above records table (if visible)
	And the above office visit (if exists) is the 1-th entry of the above records table (if visible)
	And the above office visit (if exists) contains 16.5 as the weight of the above records table (if visible)
	And the above office visit (if exists) contains 22.3 as the length of the above records table (if visible)
	And the above office visit (if exists) contains 16.1 as the head circumference of the above records table (if visible)
	And the above office visit (if exists) contains 1 as the household smoking the above records table (if visible)

	And the child records table is invisible

	And the adult records table is invisible

# UC52: TestViewHealthMetricsByHCP, TestOfficeVisit5YrOldViewHealthMetrics, TestOfficeVisit20YrOldViewHealthMetrics
Scenario Outline: Test View Health Metrics By HCP
	Given user logs in as Shelly Vang with MID: 8000000011 and Password: pw
	When user navigates to view basic health info page for HCP

	And user enters "<MID>" in Search by name or MID field
	And user selects patient with MID <MID>

	Then the baby records table is <baby_visibility>
	And the above records table (if visible) contains <num_baby_record> entry(ies) (if visible)
	And the office visit at <date> exists in the above records table (if visible)
	And the above office visit (if exists) is the <order>-th entry of the above records table (if visible)
	And the above office visit (if exists) contains <weight> as the weight of the above records table (if visible)
	And the above office visit (if exists) contains <length> as the length of the above records table (if visible)
	And the above office visit (if exists) contains <head_circumference> as the head circumference of the above records table (if visible)
	And the above office visit (if exists) contains <household_smoking_status> as the household smoking the above records table (if visible)

	And the child records table is <child_visibility>
	And the above records table (if visible) contains <num_child_record> entry(ies) (if visible)
	And the office visit at <date> exists in the above records table (if visible)
	And the above office visit (if exists) is the <order>-th entry of the above records table (if visible)
	And the above office visit (if exists) contains <weight> as the weight of the above records table (if visible)
	And the above office visit (if exists) contains <height> as the height of the above records table (if visible)
	And the above office visit (if exists) contains <blood_pressure> as the blood_pressure of the above records table (if visible)
	And the above office visit (if exists) contains <household_smoking_status> as the household smoking the above records table (if visible)

	And the adult records table is <adult_visibility>
	And the above records table (if visible) contains <num_adult_record> entry(ies) (if visible)
	And the office visit at <date> exists in the above records table (if visible)
	And the above office visit (if exists) is the <order>-th entry of the above records table (if visible)
	And the above office visit (if exists) contains <weight> as the weight of the above records table (if visible)
	And the above office visit (if exists) contains <height> as the height of the above records table (if visible)
	And the above office visit (if exists) contains <blood_pressure> as the blood_pressure of the above records table (if visible)
	And the above office visit (if exists) contains <household_smoking_status> as the household smoking the above records table (if visible)
	And the above office visit (if exists) contains <patient_smoking_status> as the household smoking the above records table (if visible)
	And the above office visit (if exists) contains <hdl> as the hdl of the above records table (if visible)
	And the above office visit (if exists) contains <ldl> as the ldl of the above records table (if visible)
	And the above office visit (if exists) contains <triglyceride> as the triglycerides of the above records table (if visible)

	Examples:
		# UC52: TestViewHealthMetricsByHCP
		| MID | date                | order | baby_visibility | num_baby_record | child_visibility | num_child_record | adult_visibility | num_adult_record | weight | length | head_circumference | household_smoking_status | height | blood_pressure | patient_smoking_status | hdl | ldl | triglyceride | 
		| 102 | 10/01/2013 12:00 PM | 1     | visible         | 5               | invisible        | 0                | invisible        | 0                | 30.2   | 34.7   | 19.4               | 3                        | 0      | 0/0            | 0                      | 0   | 0   | 0            |
		| 102 | 02/02/2012 12:00 PM | 2     | visible         | 5               | invisible        | 0                | invisible        | 0                | 15.8   | 25.7   | 17.1               | 3                        | 0      | 0/0            | 0                      | 0   | 0   | 0            |
		| 102 | 12/01/2011 12:00 PM | 3     | visible         | 5               | invisible        | 0                | invisible        | 0                | 12.1   | 22.5   | 16.3               | 3                        | 0      | 0/0            | 0                      | 0   | 0   | 0            |
		| 102 | 11/01/2011 12:00 PM | 4     | visible         | 5               | invisible        | 0                | invisible        | 0                | 10.3   | 21.2   | 15.3               | 3                        | 0      | 0/0            | 0                      | 0   | 0   | 0            |
		| 102 | 10/01/2011 12:00 PM | 5     | visible         | 5               | invisible        | 0                | invisible        | 0                | 8.3    | 19.6   | 14.5               | 3                        | 0      | 0/0            | 0                      | 0   | 0   | 0            |
		# UC52: TestOfficeVisit5YrOldViewHealthMetrics
		| 103 | 10/01/2011 12:00 PM | 1     | visible         | 1               | visible          | 2                | invisible        | 0                | 36.5   | 39.3   | 19.9               | 3                        | 0      | 0/0            | 0                      | 0   | 0   | 0            |
		| 103 | 10/14/2013 12:00 PM | 1     | visible         | 1               | visible          | 2                | invisible        | 0                | 37.9   | 0      | 0                  | 3                        | 42.9   | 95/65          | 0                      | 0   | 0   | 0            |
		| 103 | 10/15/2012 12:00 PM | 2     | visible         | 1               | visible          | 2                | invisible        | 0                | 35.8   | 0      | 0                  | 3                        | 41.3   | 95/65          | 0                      | 0   | 0   | 0            |
		# UC52: TestOfficeVisit20YrOldViewHealthMetrics
		| 104 | 10/25/2013 12:00 PM | 1     | invisible       | 0               | invisible        | 0                | visible          | 3                | 124.3  | 0      | 0                  | 1                        | 62.3   | 100/75         | 3                      | 65  | 102 | 147          |
		| 104 | 10/20/2012 12:00 PM | 2     | invisible       | 0               | invisible        | 0                | visible          | 3                | 120.7  | 0      | 0                  | 3                        | 62.3   | 107/72         | 2                      | 63  | 103 | 145          |
		| 104 | 10/10/2011 12:00 PM | 3     | invisible       | 0               | invisible        | 0                | visible          | 3                | 121.3  | 0      | 0                  | 3                        | 62.3   | 105/73         | 1                      | 64  | 102 | 143          |

# UC52: TestOfficeVisit24YrOldViewHealthMetrics
Scenario: Test View Health Metrics By Patient 
	Given user logs in as Thane Ross with MID: 105 and Password: pw
	When user navigates to view basic health info page for Patient

	Then the baby records table is invisible
	And the child records table is invisible

	And the adult records table is visible
	And the above records table (if visible) contains 1 entry(ies) (if visible)
	And the office visit at 10/25/2013 12:00 PM exists in the above records table (if visible)
	And the above office visit (if exists) is the 1-th entry of the above records table (if visible)
	And the above office visit (if exists) contains 210.1 as the weight of the above records table (if visible)
	And the above office visit (if exists) contains 73.1 as the height of the above records table (if visible)
	And the above office visit (if exists) contains 160/100 as the blood_pressure of the above records table (if visible)
	And the above office visit (if exists) contains 1 as the household smoking the above records table (if visible)
	And the above office visit (if exists) contains 4 as the household smoking the above records table (if visible)
	And the above office visit (if exists) contains 37 as the hdl of the above records table (if visible)
	And the above office visit (if exists) contains 141 as the ldl of the above records table (if visible)
	And the above office visit (if exists) contains 162 as the triglycerides of the above records table (if visible)

# UC52: TestNoHealthRecordsExistByHCP
Scenario: Test No Health Records Exist By HCP
	Given user logs in as Shelly Vang with MID: 8000000011 and Password: pw
	When user navigates to view basic health info page for HCP
	And user enters "101" in Search by name or MID field
	And user selects patient with MID 101
	Then the page displays message "No health records available."

# UC52: TestNoHealthRecordsExistByPatient
Scenario: Test No Health Records Exist By Patient
	Given user logs in as Thane Ross with MID: 105 and Password: pw
	When user navigates to view basic health info page for Patient
	Then the page displays message "No health records available."

# UC52: TestOfficeVisitDateIsBirthDate
Scenario: Test Office Visit Date Is Birth Date
	Given user logs in as Shelly Vang with MID: 8000000011 and Password: pw
	When user navigates to edit basic health info page
	And user enters "101" in Search by name or MID field
	And user selects patient with MID 101
	And user clicks on Click Here to Create a New Office Visit Button
	And user enters "05/01/2016 12:00 PM" as the Office Visit Date
	And user selects "central hospital" with id "9" as the Location
	And user enters "Brynn is growing into a beautiful sunflower" as Notes
	And user clicks the "Create" button
	And user enters "42.8" as the weight
	And user enters "18.9" as the height
	And user enters "100/80" as the blood pressure
	And user selects "1" as the household smoking status
	And user clicks on the Add Record button
	And user navigates to view basic health info page for HCP

	Then the child records table is visible
	And the above records table (if visible) contains 1 entry(ies) (if visible)
	And the office visit at 05/01/2016 12:00 PM exists in the above records table (if visible)
	And the above office visit (if exists) is the 1-th entry of the above records table (if visible)
	And the above office visit (if exists) contains 42.8 as the weight of the above records table (if visible)
	And the above office visit (if exists) contains 18.9 as the height of the above records table (if visible)
	And the above office visit (if exists) contains 100/80 as the blood_pressure of the above records table (if visible)
	And the above office visit (if exists) contains 1 as the household smoking the above records table (if visible)

	And the baby records table is invisible

	And the adult records table is invisible

