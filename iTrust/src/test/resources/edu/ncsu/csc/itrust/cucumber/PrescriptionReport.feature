#Author: mmackre
#
Feature: View Prescription Report
As an HCP or patient
I want to view prescription reports
So I can know what the prescrips are 


Scenario: BlackBox HCPViewNoPrescrips
Given UC19.sql has not been loaded
When I login as 900000000 using pw
Then When I view the prescrip report for 2, it should be empty

Scenario: BlackBox PatientViewNoPrescrips
Given UC19.sql has not been loaded
When I login as 2 using pw
Then When I view the prescrip report for 2, it should be empty


Scenario: BlackBox PersonalRepViewRepresenteesPrescriptions
Given UC19.sql has been loaded
When I login as 1 using pw
Then When I view the prescrip report for 2 it should have report title first: Andy Programmer, prescription Advil, 2016-10-05, 2016-10-05, 2016-11-05, Kelly Doctor
And  When I view the prescrip report for 2 it should have report title second: Andy Programmer, prescription Oyster Shell Calcium with Vitamin D, 2016-10-20, 2016-10-20, 2017-10-20, Kelly Doctor
And  When I view the prescrip report for 2 it should have report title third: Andy Programmer, prescription Lantus, 2016-10-20, 2016-10-20, 2017-10-20, Kelly Doctor
And  When I view the prescrip report for 2 it should have report title fourth: Andy Programmer, prescription Midichlomaxene, 2016-10-20, 2016-10-20, 2017-10-20, Kelly Doctor

Scenario: ViewPrescriptionReportPatient
Given UC19.sql has been loaded
When I login as 2 using pw
Then When I view the prescrip report for 2 it should have report title first: Andy Programmer, prescription Advil, 2016-10-05, 2016-10-05, 2016-11-05, Kelly Doctor
And  When I view the prescrip report for 2 it should have report title second: Andy Programmer, prescription Oyster Shell Calcium with Vitamin D, 2016-10-20, 2016-10-20, 2017-10-20, Kelly Doctor
And  When I view the prescrip report for 2 it should have report title third: Andy Programmer, prescription Lantus, 2016-10-20, 2016-10-20, 2017-10-20, Kelly Doctor
And  When I view the prescrip report for 2 it should have report title fourth: Andy Programmer, prescription Midichlomaxene, 2016-10-20, 2016-10-20, 2017-10-20, Kelly Doctor

Scenario: ViewPrescriptionReportHCP
Given UC19.sql has been loaded
When I login as 900000000 using pw
Then When I view the prescrip report for 2 it should have report title first: Andy Programmer, prescription Advil, 2016-10-05, 2016-10-05, 2016-11-05, Kelly Doctor
And  When I view the prescrip report for 2 it should have report title second: Andy Programmer, prescription Oyster Shell Calcium with Vitamin D, 2016-10-20, 2016-10-20, 2017-10-20, Kelly Doctor
And  When I view the prescrip report for 2 it should have report title third: Andy Programmer, prescription Lantus, 2016-10-20, 2016-10-20, 2017-10-20, Kelly Doctor
And  When I view the prescrip report for 2 it should have report title fourth: Andy Programmer, prescription Midichlomaxene, 2016-10-20, 2016-10-20, 2017-10-20, Kelly Doctor





