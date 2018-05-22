INSERT INTO `hospitals` (`HospitalID`, `HospitalName`, `Address`, `City`, `State`, `Zip`) VALUES ('471', 'Hunt Hospital', '1070 PARTNERS WAY', 'Raleigh', 'NC', '27606');
INSERT INTO `hospitals` (`HospitalID`, `HospitalName`, `Address`, `City`, `State`, `Zip`) VALUES ('472', 'DH Hill Hospital', '2 BROUGHTON DR', 'Raleigh', 'NC', '27695');
INSERT INTO `hospitals` (`HospitalID`, `HospitalName`, `Address`, `City`, `State`, `Zip`) VALUES ('473', 'Tar Heel Hospital', '209 South Rd', 'Chapel Hill', 'NC', '27599');
INSERT INTO `hospitals` (`HospitalID`, `HospitalName`, `Address`, `City`, `State`, `Zip`) VALUES ('474', 'Duke Hospital', '905 W Main St', 'Durham', 'NC', '27701');
INSERT INTO `hospitals` (`HospitalID`, `HospitalName`, `Address`, `City`, `State`, `Zip`) VALUES ('475', 'Charlotte Hospital', '9201 University City Blvd', 'Charlotte', 'NC', '28223');

INSERT INTO `personnel` (`MID`, `role`, `lastName`, `firstName`, `specialty`, `email`) VALUES ('4700000001', 'hcp', 'McDoctor', 'Yolanda', 'General Physician', 'yolanda@mcdoctor.com');
INSERT INTO `personnel` (`MID`, `role`, `lastName`, `firstName`, `specialty`, `email`) VALUES ('4700000002', 'hcp', 'McDoctor', 'Laura', 'Pediatrician', 'laura@mcdoctor.com');
INSERT INTO `personnel` (`MID`, `role`, `lastName`, `firstName`, `specialty`, `email`) VALUES ('4700000003', 'hcp', 'McDoctor', 'Kevin', 'Heart Surgeon', 'kevin@mcdoctor.com');
INSERT INTO `personnel` (`MID`, `role`, `lastName`, `firstName`, `specialty`, `email`) VALUES ('4700000004', 'hcp', 'McDoctor', 'Kyle', 'General Physician', 'kyle@mcdoctor.com');
INSERT INTO `personnel` (`MID`, `role`, `lastName`, `firstName`, `specialty`, `email`) VALUES ('4700000005', 'hcp', 'McDoctor', 'Amanda', 'General Physician', 'amanda@mcdoctor.com');
INSERT INTO `personnel` (`MID`, `role`, `lastName`, `firstName`, `specialty`, `email`) VALUES ('4700000006', 'hcp', 'McDoctor', 'Bob', 'General Physician', 'bob@mcdoctor.com');
INSERT INTO `personnel` (`MID`, `role`, `lastName`, `firstName`, `specialty`, `email`) VALUES ('4700000007', 'hcp', 'McDoctor', 'Marty', 'OB/GYN', 'marty@mcdoctor.com');
INSERT INTO `personnel` (`MID`, `role`, `lastName`, `firstName`, `specialty`, `email`) VALUES ('4700000008', 'hcp', 'McDoctor', 'Linda', 'Pediatrician', 'linda@mcdoctor.com');
INSERT INTO `personnel` (`MID`, `role`, `lastName`, `firstName`, `specialty`, `email`) VALUES ('4700000009', 'hcp', 'McDoctor', 'David', 'Surgeon', 'david@mcdoctor.com');

INSERT INTO `hcpassignedhos` (`hosID`, `HCPID`) VALUES ('471', '4700000001');
INSERT INTO `hcpassignedhos` (`hosID`, `HCPID`) VALUES ('471', '4700000002');
INSERT INTO `hcpassignedhos` (`hosID`, `HCPID`) VALUES ('472', '4700000003');
INSERT INTO `hcpassignedhos` (`hosID`, `HCPID`) VALUES ('472', '4700000004');
INSERT INTO `hcpassignedhos` (`hosID`, `HCPID`) VALUES ('473', '4700000005');
INSERT INTO `hcpassignedhos` (`hosID`, `HCPID`) VALUES ('473', '4700000006');
INSERT INTO `hcpassignedhos` (`hosID`, `HCPID`) VALUES ('474', '4700000007');
INSERT INTO `hcpassignedhos` (`hosID`, `HCPID`) VALUES ('474', '4700000008');
INSERT INTO `hcpassignedhos` (`hosID`, `HCPID`) VALUES ('475', '4700000009');