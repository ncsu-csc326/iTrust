#Author: seelder
# add incorrect fields for New Patients?


Feature: Office Visit
As an HCP
I want to be able to document an office visit
So I have a record of the visit for future diagnosis and billing purposes

Scenario Outline: Required Fields Only
Given I have already selected <patient>
When I enter date: <visitDate>, location: <hospitalLocation>, appointment type: <appointmentType>, and no other information
Then the patient's office visit for <visitDate>, <hospitalLocation>, and <appointmentType> is successfully documented

#Note that visitDate must be in the format YYYY-MM-dd HH:mm (e.g. 2016-01-24 15:00)
Examples:
| patient   | visitDate        | hospitalLocation | appointmentType |
| 000000001 | 2016-07-27 11:30 | Central Hospital | Physical        |

Scenario Outline: All fields
Given I have already selected <patient>
When I enter date: <visitDate>, location: <hospitalLocation>, appointment type: <appointmentType>, notes: <notes>, and select send a bill <send>
Then the patient's entire office visit for <visitDate>, <hospitalLocation>, <appointmentType>, <notes>, and send bill <send> is successfully documented

#Note that visitDate must be in the format YYYY-MM-dd HH:mm (e.g. 2016-01-24 15:00)
Examples:
| patient   | visitDate        | hospitalLocation | appointmentType | notes | send  |
| 000000001 | 2016-07-27 11:30 | Central Hospital | Physical        | ab;   | false |

Scenario Outline: Valid update
Given <patient> already has a visit with date: <oldVisitDate>, location: <oldHospitalLocation>, appointment type: <oldAppointmentType>, notes: <oldNotes>, and select send a bill <oldSend>
And I have already selected <patient>
When I update the office visit information to date: <visitDate>, location: <hospitalLocation>, appointment type: <appointmentType>, notes: <notes>, and send a bill: <send>
Then the office visit information is - date: <visitDate>, location: <hospitalLocation>, appointment type: <appointmentType>, notes: <notes>, and send a bill: <send>

Examples:
| patient   | oldVisitDate        | oldHospitalLocation | oldAppointmentType | oldNotes | oldSend  | visitDate        | hospitalLocation | appointmentType | notes        | send  |
| 000000001 | 2016-08-11 11:30    | Central Hospital    | Physical           | ab;      | false    | 2016-07-27 11:30 | Central Hospital | Consultation    | another note | false |




