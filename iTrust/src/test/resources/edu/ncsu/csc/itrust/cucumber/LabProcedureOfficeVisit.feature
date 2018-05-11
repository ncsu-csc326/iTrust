#Author: mmackre
# add incorrect fields for New Patients?
#
#
Feature: Lab Procedure Office Visit
As an HCP
I want to be able to document an office visit
So I have a record of the visit for future diagnosis and billing purposes



#fig out what this cr thing is in the prescription notes
Scenario: Document Office Visit All
Given I load uc11.sql
When I login as 9000000000 using pw
And User enters date: 2016-11-01T11:30, location: 9191919191, appointment type: 6, notes: Patient reported issues: hair loss, fast heartbeat, and select send a bill true, weight: 160, height: 69, blood pressure 100/70, ldl: 80, hdl: 60, Triglycerides: 100, house smoke: 1, patient smoke: 4
And I add a new prescription with the following information: Medication: 08109-6 Aspirin, Dosage: 75, Dates: 2016-11-15 to 2017-11-14, Instructions: Number Per day... 1
And For the Lab Procedures associated with the office visit, select LOINC 18106-5, Cardiac echo study procedure with priority 1 and Lab Technician 5000000002
And Diagnosis: S108, Superficial injury neck, Medical Procedures: 99214, Immunizations: 90715, TDAP
And submits record
And After the office visit has been created the Location: 9191919191, notes: Patient reported issues: hair loss, fast heartbeat, sendbill: true
And After the office visit has been created it does include the following basic health metrics: height: 69.0 in, weight: 160.0 lbs, blood pressure: 100/70, LDL: 80, HDL: 60, Triglycerides: 100, Household Smoking Status: 1, Patient Smoking Status: 4
And After the office visit has been created, its prescription info is: drugname: Aspirin, Code: 08109-6, Dosage: 75, Start Date: 2016-11-15, End Date: 2017-11-14, Special instructions: Number Per day... 1
And After the office visit has been created, its Lab Procedures include 18106-5, Cardiac echo study procedure  with priority 1 and Lab Technician 5000000002
And After the office visit has been created, Diagnosis: S108, Skin injury neck, Medical Procedures:  99214, Immunizations: 90715, TDAP


 
Scenario: Edit Office Visit
Given I load uc11.sql
When I login as 9000000000 using pw
And I update notes to Update: Will be visiting Hyderabad, location to 1, add CPT 90714 , Typhoid Vaccine to immunizations, delete S607 from diagnosis
Then After the office visit has been created the Location: 1, notes: Update: Will be visiting Hyderabad, sendbill: true
And After the office visit has been updated the type saved for the office visit is 1
And After the office visit has been updated, it does include the following basic health metrics: height: 69.0 in, weight: 163.0 lbs, blood pressure: 102/60, LDL: 81, HDL: 60, Triglycerides: 110, Household Smoking Status: 1, Patient Smoking Status: 4
And After the office visit has been updated, it does NOT include the following Diagnosis of S607, Multiple superficial injuries of wrist and hand
And After the office visit has been updated, it does include the following Immunization 90714, Typhoid Vaccine


Scenario: AddDeleteCheckPrescrip
Given I load uc11.sql
When I add a new prescription with the following information: Medication: 64764-1512 Aspirin, Dosage: 100, Dates: 2016-11-28 to 2017-11-05, Instructions: take with a lot of water
And verify that it is there code: 64764-1512, dosage 100, start date 2016-11-28, end date 2017-11-05, and special instructions: take with a lot of water
And delete the prescription you just made
Then None should be on record
	
	
Scenario: noProceduresAvailable
Given I load uc11.sql
Then No lab record are present when I check for baby Programmer id: 5
	
	
Scenario: startEndDateBackwards
Given I load uc11.sql
When I add a prescription with backwards dates and the following information: Medication: 64764-1512 Aspirin, Dosage: 100, Dates: 2016-11-28 to 2015-11-05, Instructions: take with a lot of water
Then None should be on record

