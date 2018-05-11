#Author: Mike MacKrell
# 
#
#
Feature: View ER Reports



Scenario: ViewEmergencyReportER
Given I load uc21.sql
When I login as 9000000006 using pw
And Go to the Emergency Patient Report.
Then mid: 201 name: Sandy Sky, age: 24, gender: Male, emergency contact: Susan Sky-Walker, phone: 444-332-4309, allergies: [Penicillin, Pollen], blood type: O-, diagnoses: J00, and J45, prescriptions: 63739-291, and 48301-3420, immunizaitons: 90715


Scenario: ViewEmergencyReportHCP
Given I load uc21.sql
When I login as 9000000000 using pw
And Go to the Emergency Patient Report.
Then mid: 201, name: Sandy Sky, age: 24, gender: Male, emergency contact: Susan Sky-Walker, phone: 444-332-4309, allergies: [Penicillin, Pollen], blood type: O-, diagnoses: J00, and J45, prescriptions: 63739-291, and 48301-3420, immunizaitons: 90715


Scenario: ViewEmergencyReportInfantER
Given I load uc21.sql
When I login as 9000000006 using pw
And Go to the Emergency Patient Report.
Then mid: 202, name: Sarah Sky, age: 0, gender: Female, emergency contact: Susan Sky-Walker, phone: 444-332-4309, no allergies, blood type: O+, diagnosis: J00, prescriptions: 63739-291, no immunizations

Scenario: ViewEmergencyReportERNoPatient
Given I load uc21.sql
When I login as 9000000006 using pw
And Go to the Emergency Patient Report.
Then Error: The patient 2002 does not exist


Scenario: ViewEmergencyReportHCPNoPatient
Given I load uc21.sql
When I login as 9000000000 using pw
And Go to the Emergency Patient Report.
Then Error: The patient 2002 does not exist


Scenario: ViewEmergencyReportNoOfficeVisit
Given I load uc21.sql
And No office visits exist for patient 105
When I login as 9000000000 using pw
And Go to the Emergency Patient Report.
Then Office visit info missing for 105 so no prescriptions, diagnoses, allergies, or immunizations

