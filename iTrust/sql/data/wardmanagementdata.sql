
INSERT INTO `wards` (`InHospital`, `RequiredSpecialty`) VALUES('1','Pediatrics');
INSERT INTO `wards` (`InHospital`, `RequiredSpecialty`) VALUES('1','Elderly');
INSERT INTO `wards` (`InHospital`, `RequiredSpecialty`) VALUES('1','Cardiology');
INSERT INTO `wards` (`InHospital`, `RequiredSpecialty`) VALUES('1','Maternity');

INSERT INTO `hcpassignedtoward`(`HCP`,`Ward`) VALUES('9000000000',(Select wardID FROM wards WHERE requiredSpecialty = 'Pediatrics' ORDER BY wardID ASC LIMIT 1));
INSERT INTO `hcpassignedtoward`(`HCP`,`Ward`) VALUES('9900000000',(Select wardID FROM wards WHERE requiredSpecialty = 'Elderly' ORDER BY wardID ASC LIMIT 1));
INSERT INTO `hcpassignedtoward`(`HCP`,`Ward`) VALUES('9990000000',(Select wardID FROM wards WHERE requiredSpecialty = 'Cardiology' ORDER BY wardID ASC LIMIT 1));
INSERT INTO `hcpassignedtoward`(`HCP`,`Ward`) VALUES('9000000003',(Select wardID FROM wards WHERE requiredSpecialty = 'Maternity' ORDER BY wardID ASC LIMIT 1));

INSERT INTO `hcpassignedhos`(`hosid`,`hcpid`) VALUES('1','9000000000');
INSERT INTO `hcpassignedhos`(`hosid`,`hcpid`) VALUES('1','9000000002');
INSERT INTO `hcpassignedhos`(`hosid`,`hcpid`) VALUES('1','9000000003');
INSERT INTO `hcpassignedhos`(`hosid`,`hcpid`) VALUES('1','9000000007');





INSERT INTO `wardrooms`(`RoomName`,`OccupiedBy`,`InWard`, `Status`) VALUES('Lolita','1', (Select wardID FROM wards WHERE requiredSpecialty = 'Pediatrics' ORDER BY wardID ASC LIMIT 1),'Clean');
INSERT INTO `wardrooms`(`RoomName`,`OccupiedBy`,`InWard`, `Status`) VALUES('Boneyard','100', (Select wardID FROM wards WHERE requiredSpecialty = 'Elderly' ORDER BY wardID ASC LIMIT 1),'Clean');
INSERT INTO `wardrooms`(`RoomName`,`OccupiedBy`,`InWard`, `Status`) VALUES('Heartless','10', (Select wardID FROM wards WHERE requiredSpecialty = 'Cardiology' ORDER BY wardID ASC LIMIT 1),'Clean');
INSERT INTO `wardrooms`(`RoomName`,`OccupiedBy`,`InWard`, `Status`) VALUES('Berbeh','11', (Select wardID FROM wards WHERE requiredSpecialty = 'Maternity' ORDER BY wardID ASC LIMIT 1),'Clean');