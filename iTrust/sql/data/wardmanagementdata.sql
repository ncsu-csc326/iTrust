
INSERT INTO `wards` (`InHospital`, `RequiredSpecialty`) VALUES('1','Pediatrics');
INSERT INTO `wards` (`InHospital`, `RequiredSpecialty`) VALUES('1','Elderly');
INSERT INTO `wards` (`InHospital`, `RequiredSpecialty`) VALUES('1','Cardiology');
INSERT INTO `wards` (`InHospital`, `RequiredSpecialty`) VALUES('1','Maternity');

INSERT INTO `hcpassignedtoward`(`HCP`,`Ward`) VALUES('9000000000','001');
INSERT INTO `hcpassignedtoward`(`HCP`,`Ward`) VALUES('9900000000','002');
INSERT INTO `hcpassignedtoward`(`HCP`,`Ward`) VALUES('9990000000','003');
INSERT INTO `hcpassignedtoward`(`HCP`,`Ward`) VALUES('9000000003','004');

INSERT INTO `hcpassignedhos`(`hosid`,`hcpid`) VALUES('1','9000000000');
INSERT INTO `hcpassignedhos`(`hosid`,`hcpid`) VALUES('1','9000000002');
INSERT INTO `hcpassignedhos`(`hosid`,`hcpid`) VALUES('1','9000000003');
INSERT INTO `hcpassignedhos`(`hosid`,`hcpid`) VALUES('1','9000000007');





INSERT INTO `wardrooms`(`RoomName`,`OccupiedBy`,`InWard`, `Status`) VALUES('Lolita','1', '001','Clean');
INSERT INTO `wardrooms`(`RoomName`,`OccupiedBy`,`InWard`, `Status`) VALUES('Boneyard','100', '002','Clean');
INSERT INTO `wardrooms`(`RoomName`,`OccupiedBy`,`InWard`, `Status`) VALUES('Heartless','10', '003','Clean');
INSERT INTO `wardrooms`(`RoomName`,`OccupiedBy`,`InWard`, `Status`) VALUES('Berbeh','11', '004','Clean');