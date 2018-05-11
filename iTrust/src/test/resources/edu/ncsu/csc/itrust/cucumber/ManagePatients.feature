#Author: seelder
# add incorrect fields for New Patients?


Feature:Add and Remove Patients

Scenario Outline: Valid new Patient
Given I login as <HCPMID> using <Password>
When I enter a <validPatientFirstName>, <validPatientLastName>, and <validPatientEmail> for a new patient and submit the information
Then the new patient is assigned an MID
And <validPatientFirstName> <validPatientLastName> is created

Examples:
|HCPMID    |Password|validPatientFirstName|validPatientLastName|validPatientEmail     |
|9000000000|pw      |Jack                 |Johnson             |JJohnson001@gmail.com |
|9000000000|pw      |Xena                 |Jackson             |XLJackson@yahoo.com   |

Scenario: Default values for a new patient
Given I login as 9000000000 using pw
And a Patient has been created
Then all initial values except MID default to null
