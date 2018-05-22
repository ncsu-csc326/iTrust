Feature: Edit Basic Health Information

# Student written functional blackbox test
Scenario: Test Negative Weight
Given Shelly Vang is an HCP with MID: 8000000011
And Brynn McClain is a patient with MID 101 who is born on 05/01/2013
When Shelly Vang logs in with MID: 8000000011
And Chooses to document an office visit
And selects patient Brynn McClain with MID 101
And Choose appointment type General checkup with id: 1
And Chooses the date to be 10/1/2013
And Select Central Hospital with id: 9 for location
And enters Note “Brynn can start eating rice cereal mixed with breast milk or formula once a day”
And Enters -1 for weight
And Enters 10 for length
And Enters 16.1 for Head Circumference
And selects 1 for non-smoking for household smoking status
And submits record
Then the record is not successfully added

Scenario: Test Invalid Triglycerides
Given Shelly Vang is an HCP with MID: 8000000011
And Brynn McClain is a patient with MID 101 who is born on 05/01/2013
When Shelly Vang logs in with MID: 8000000011
And Chooses to document an office visit
And selects patient Brynn McClain with MID 101
And Choose appointment type General checkup with id: 1
And Chooses the date to be 10/1/2026
And Select Central Hospital with id: 9 for location
And enters Note “Brynn is an adult now! Or at least older than 12.”
And Enters 180 for weight
And Enters 65 for height
And Enters 140/110 for blood pressure
And selects 1 for non-smoking for household smoking status
And selects 2 for former smoker for patient smoking status
And enters 89 for HDL
And enters 500 for LDL
And enters 50 for Triglycerides 
And submits record
Then the record is not successfully added

Scenario: Test Invalid Length
Given Shelly Vang is an HCP with MID: 8000000011
And Brynn McClain is a patient with MID 101 who is born on 05/01/2013
When Shelly Vang logs in with MID: 8000000011
And Chooses to document an office visit
And selects patient Brynn McClain with MID 101
And Choose appointment type General checkup with id: 1
And Chooses the date to be 10/1/2013
And Select Central Hospital with id: 9 for location
And enters Note “Brynn can start eating rice cereal mixed with breast milk or formula once a day”
And Enters 16.5 for weight
And Enters -1 for length
And Enters 16.1 for Head Circumference
And selects 1 for non-smoking for household smoking status
And submits record
Then the record is not successfully added

Scenario: Length With Two Decimal Places
Given Shelly Vang is an HCP with MID: 8000000011
And Brynn McClain is a patient with MID 101 who is born on 05/01/2013
When Shelly Vang logs in with MID: 8000000011
And Chooses to document an office visit
And selects patient Brynn McClain with MID 101
And Choose appointment type General checkup with id: 1
And Chooses the date to be 5/1/2013
And Select Central Hospital with id: 9 for location
And enters Note “Brynn was born today!”
And Enters 10 for weight
And Enters 8.22 for length
And Enters 16 for Head Circumference
And selects 1 for non-smoking for household smoking status
And submits record
Then the record is not successfully added

Scenario: Invalid LDL Cholesterol In Adult
Given Shelly Vang is an HCP with MID: 8000000011
And Brynn McClain is a patient with MID 101 who is born on 05/01/2013
When Shelly Vang logs in with MID: 8000000011
And Chooses to document an office visit
And selects patient Brynn McClain with MID 101
And Choose appointment type General checkup with id: 1
And Chooses the date to be 10/1/2026
And Select Central Hospital with id: 9 for location
And enters Note “Brynn is an adult now! Or at least older than 12.”
And Enters 180 for weight
And Enters 65 for height
And Enters 140/110 for blood pressure
And selects 1 for non-smoking for household smoking status
And selects 2 for former smoker for patient smoking status
And enters 89 for HDL
And enters 601 for LDL
And enters 599 for Triglycerides 
And submits record
Then the record is not successfully added

# Acceptance Tests Provided by Teaching Staffs 
Scenario: Four Month Old
Given Shelly Vang is an HCP with MID: 8000000011
And Brynn McClain is a patient with MID 101 who is born on 05/01/2013
When Shelly Vang logs in with MID: 8000000011
And Chooses to document an office visit
And selects patient Brynn McClain with MID 101
And Choose appointment type General checkup with id: 1
And Chooses the date to be 10/1/2013
And Select Central Hospital with id: 9 for location
And enters Note “Brynn can start eating rice cereal mixed with breast milk or formula once a day”
And Enters 16.5 for weight
And Enters 22.3 for length
And Enters 16.1 for Head Circumference
And selects 1 for non-smoking for household smoking status
And submits record
Then the record is saved successfully
 
 
Scenario:  Two Years Old
Given Shelly Vang is an HCP with MID: 8000000011
And Caldwell Hudson is a patient with MID 102 who is born on 09/29/2011
When Shelly Vang logs in with MID: 8000000011
And Chooses to document an office visit
And selects patient Caldwell Hudson with MID 102
And Choose appointment type General checkup with id: 1
And Select Central Hospital with id: 9 for location
And Chooses the date to be 10/28/2013
And enters Note “Diagnosed with strep throat.  Avoid contact with others for the first 24 hours of antibiotics”
And Enters 30.2 for weight
And Enters 34.7 for length
And Enters 19.4 for Head Circumference
And selects 3 for indoors smokers for household smoking status
And submits record
Then the record is saved successfully
 
 
Scenario:  Five Years Old
Given Shelly Vang is an HCP with MID: 8000000011
And Fulton Gray is a patient with MID 103 who is born on 10/10/2008
When Shelly Vang logs in with MID: 8000000011
And Chooses to document an office visit
And selects patient Fulton Gray with MID 103
And Choose appointment type General checkup with id: 1
And Select Central Hospital with id: 9 for location
And Chooses the date to be 10/14/2013
And enters Note “Fulton has all required immunizations to start kindergarten next year.”
And Enters 37.9 for weight
And Enters 42.9 for height
And Enters 95/65 for blood pressure
And selects 2 for outdoor smokers for household smoking status
And submits record
Then the record is saved successfully


Scenario:  20 Years Old
Given Shelly Vang is an HCP with MID: 8000000011
And Daria Griffin is a patient with MID 104 who is born on 10/25/1993
When Shelly Vang logs in with MID: 8000000011
And Chooses to document an office visit
And selects patient Fulton Gray with MID 104
And Choose appointment type General checkup with id: 1
And Select Central Hospital with id: 9 for location
And Chooses the date to be 10/25/2013
And enters Note “Patient is healthy.”
And Enters 124.3 for weight
And Enters 62.3 for height
And Enters 110/75 for blood pressure
And selects 1 for non-smoking for household smoking status
And selects 2 for former smoker for patient smoking status
And enters 65 for HDL
And enters 102 for LDL
And enters 147 for Triglycerides 
And submits record
Then the record is saved successfully


Scenario:  24 Years Old
Given Shelly Vang is an HCP with MID: 8000000011
And Thane Ross is a patient with MID 105 who is born on 01/03/1989
When Shelly Vang logs in with MID: 8000000011
And Chooses to document an office visit
And Chooses the date to be 10/25/2013
And selects patient Thane Ross with MID 105
And Choose appointment type General checkup with id: 1
And Select Central Hospital with id: 9 for location
And enters Note “Thane should consider modifying diet and exercise to avoid future heart disease”
And Enters 210.1 for weight
And Enters 73.1 for height
And Enters 160/110 for blood pressure
And selects 1 for non-smoking for household smoking status
And selects 4 for never smoked for patient smoking status
And enters 37 for HDL
And enters 141 for LDL
And enters 162 for Triglycerides 
And submits record
Then the record is saved successfully

Scenario:  No Smoking Status
Given Kelly Doctor is an HCP with MID: 9000000000
And Random Person is a patient with MID 1 who is born on 05/10/1950
When Kelly Doctor logs in with MID: 9000000000
And Chooses to document an office visit
And Chooses the date to be 12/25/2013
And selects patient Random Person with MID 1
And enters Note “Came in complaining of splinters.”
And Enters 500 for weight
And Enters 48.0 for height
And Enters 180/110 for blood pressure
And selects 1 for non-smoking for household smoking status
And enters 40 for HDL
And enters 190 for LDL
And enters 500 for Triglycerides 
And submits record
Then the record is not successfully added


Scenario:  No High Blood Pressure 
Given Kelly Doctor is an HCP with MID: 9000000000
And Random Person is a patient with MID 1 who is born on 05/10/1950
When Kelly Doctor logs in with MID: 9000000000
And Chooses to document an office visit
And Chooses the date to be 12/01/2013
And selects patient Random Person with MID 1
And enters Note “It doesn't matter”
And Enters 160 for weight
And Enters 55.5 for height
And Enters /79 for blood pressure
And selects 1 for non-smoking for household smoking status
And selects 4 for new smoked for patient smoking status 
And enters 50 for HDL
And enters 100 for LDL
And enters 345 for Triglycerides 
And submits record
Then the record is not successfully added


Scenario:  Invalid Characters for HDL
Given Kelly Doctor is an HCP with MID: 9000000000
And Random Person is a patient with MID 1 who is born on 05/10/1950
When Kelly Doctor logs in with MID: 9000000000
And Chooses to document an office visit
And Chooses the date to be 10/31/2013
And selects patient Random Person with MID 1
And enters Note “Testing invalid input”
And Enters 150 for weight
And Enters 55.5 for height
And Enters 120/79 for blood pressure
And selects 1 for non-smoking for household smoking status
And selects 4 for new smoked for patient smoking status 
And enters AA for HDL
And enters 101 for LDL
And enters 222 for Triglycerides 
And submits record
Then the record is not successfully added


Scenario:  Zero Head Circumference for Under 3 Year Old
Given Kelly Doctor is an HCP with MID: 9000000000
And Brynn McClain is a patient with MID 101 who is born on 05/01/2013
When Kelly Doctor logs in with MID: 9000000000
And Chooses to document an office visit
And Chooses the date to be 10/26/2013
And selects patient Random Person with MID 1
And enters Note “Brynn is a very healthy baby”
And Enters 90 for weight
And Enters 36.0 for height
And Enters 0 for Head Circumference
And selects 1 for non-smoking for household smoking status
And submits record
Then the record is not successfully added
