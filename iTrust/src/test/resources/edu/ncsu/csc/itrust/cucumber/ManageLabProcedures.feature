#Author: mmackre
# 
#
#
Feature: Manage lab procs 
As an HCP
I want to be able to manage lab procedures
So I can change them as needed 

Scenario: Update Add Lab Procedures HCP
Given UC26sql has been loaded
When I login as 9000000000 using pw
And Delete the Lab procedure with LOINC 40772-6 from 0000000026's office visit on 2015-10-24
And Update the lab procedure with LOINC 34667-6 by setting the Lab Technician to 5000000003
Then When I conclude the update, there is a message displayed at the top of the page: Information Successfully Updated.
And I view the date and it is 2015-10-24 for office visit for patient 26 on date 2015-10-24
And I view the location and it is 9191919191 for office visit for patient 26 on date 2015-10-24
And I view the appointment type and it is 1 for office visit for patient 26 on date 2015-10-24
And I view the notes and it is Odd symptoms. Concerned about environmental hazards at work. for office visit for patient 26 on date 2015-10-24
And I view the sendBill and it is true for office visit for patient 26 on date 2015-10-24
And After the office visit from 2015-10-24 for 26 has been created, it does include the following basic health metrics: height: 69 in, weight: 163 lbs, blood pressure: 110/70, LDL: 81, HDL: 60, Triglycerides: 110, Household Smoking Status: 1 - Non-Smoking Household, Patient Smoking Status: 4 - Never Smoker
And there is not a procedure with LOINC 40772-6, priority 1, Lab Technician 5000000002
And there is a procedure with LOINC 29588-1, priority 2, Lab Technician 5000000003
And there is a procedure with LOINC 34667-6, priority 2, Lab Technician 5000000003


Scenario: View Lab Procedures Patient
Given UC26sql has been loaded
And the data for loinc 12556-7 is updated for this use case
And The office visit for 22 from 2015-10-08 has only 4 lab procedures
And The office visit for 22 from 2015-10-25 has only 1 lab procedures
When I login as 0000000022 using pw
And Examine the lab procedures from the office visit 2015-10-01
And Examine the lab procedures from the office visit 2015-10-08 
And Examine the lab procedures from the office visit 2015-10-24 
Then When I view the office visits, they should include the visit from 2015-10-01
And When I view the office visits, they should include the visit from 2015-10-08 
And When I view the office visits, they should include the visit from 2015-10-24 
And When I view the office visit from 2015-10-01, there should be 2 lab procedure
And When I examine the lab procedures from the office visit 2015-10-01 there is a procedure with LOINC 34667-6, priority 2, Lab Technician 5000000003, status COMPLETED, numerical result: 50, confidence interval: 48-52, commentary This is concerning
And When I view the office visit from 2015-10-08, there should be 4 lab procedure
And When I examine the lab procedures from the office visit 2015-10-08 there is a procedure with LOINC 5583-0, Lab Technician 5000000002, status TESTING, and no other information
And When I examine the lab procedures from the office visit 2015-10-08 there is a procedure with LOINC 5685-3, Lab Technician 5000000002, status TESTING, and no other information
And When I examine the lab procedures from the office visit 2015-10-08 there is a procedure with LOINC 12556-7, Lab Technician 5000000002, status TESTING, and no other information
And When I examine the lab procedures from the office visit 2015-10-08 there is a procedure with LOINC 14807-2, Lab Technician 5000000003, status PENDING, and no other information
And When I view the office visit from 2015-10-08, there should be 4 lab procedure
And When I examine the lab procedures from the office visit 2015-10-25 there is a procedure with LOINC 71950-0, Lab Technician 5000000001, status IN_TRANSIT, and no other information


Scenario: Update Lab Procedures LT
Given UC26sql has been loaded
And the lab proc status' have been changed for this use case
And The office visit for 22 from 2015-10-08 has only 4 lab procedures
And The office visit for 22 from 2015-10-25 has only 1 lab procedures
When I login as 5000000003 using pw 
And Examine the lists of lab procedures assigned and Update the two 2 lab procedures to 3
And Add numerical result: 20 and confidence interval: 19-21 to the procedure whose current status is 4 loinc 5685-3
Then When I first access and examine the lab procedures, the RECEIVED queue should contain the correct lab procedure ID, lab procedure code, status, priority, HCP name, and the date the lab was assigned for the procedures with LOINC 5685-3 and 14807-2
And When I first access and examine the lab procedures, the IN_TRANSIT queue should contain the correct lab procedure ID, lab procedure code, status, priority, HCP name, and the date the lab was assigned for the procedures with LOINC 12556-7 and 71950-0
And The RECEIVED queue should contain the procedure with LOINC 5685-3 first and the procedure with LOINC 14807-2 second
And The IN_TRANSIT queue should contain the procedure with LOINC 12556-7 first and the procedure with LOINC 71950-0 second
And After I update the two intransit lab procedures to received, when I examine the RECEIVED queue the order should be: the procedure with LOINC 5685-3 first, the procedure with LOINC 14807-2 second, the procedure with LOINC 12556-7 third, and the procedure with LOINC 71950-0 fourth
And After I add results to the lab procedure with LOINC 5685-3 and update the procedure, its status should change to pending and it should no longer appear in the lab technician's queue
And After the lab procedure with LOINC 5685-3 has been updated, the procedure with LOINC 14807-2 should now have status testing and be first in the received queue
And The RECEIVED queue should contain the procedure with LOINC 12556-7 first and the procedure with LOINC 71950-0 second

Scenario: Lab Pending To Completed
Given UC26sql has been loaded
When I login as 5000000003 using pw 
And Examine the lab procedures from the office visit 2015-10-01
And When I examine the lab procedures from the office visit 2015-10-01 there is a procedure with LOINC 34667-6, priority 2, Lab Technician 5000000003, status COMPLETED, numerical result: 50, confidence interval: 48-52, commentary This is concerning
And Add commentary to the lab procedure This seems high from date 2015-10-01
Then When I view the office visits, they should include the visit from 2015-10-01
And When I view the office visits, they should include the visit from 2015-10-08
And When I view the office visits, they should include the visit from 2015-10-24
And When I view the office visit from 2015-10-01, there should be 2 lab procedure
And 34667-6 from 2015-10-01's status should change to completed



Scenario: black box test hcp fields left blank when creating lab procedure
Given UC26sql has been loaded
When I create a new lab procedure with LOINC 29588-1 Component: Heavy Metals Panel, System: Blood, priority 2, lab technicial 5000000003
And the lab procedure is not created

Scenario: black box patient view but no records shown
Given UC26sql has been loaded
And The office visit for 22 from 2015-10-09 has only 0 lab procedures
When I login as 0000000022 using pw
Then When I view the office visit from 2015-10-09, there should be 0 lab procedure



Scenario: black box Update Lab Procedures LT invalid data
Given UC26sql has been loaded
And The office visit for 22 from 2015-10-08 has only 4 lab procedures
And The office visit for 22 from 2015-10-25 has only 1 lab procedures
When I login as 5000000003 using pw
And Add numerical result: abcd and confidence interval: 19-.. to the procedure whose current status is testing loinc 5685-3
Then After I add an invalid intervals to the lab procedure with LOINC 5685-3 and attempt to update the procedure, it should not successfully update.


