DELETE FROM cptCode;
DELETE FROM immunization;
ALTER TABLE immunization AUTO_INCREMENT = 0;
DELETE FROM loincCode;
DELETE FROM labProcedure;
ALTER TABLE labProcedure AUTO_INCREMENT = 0;
DELETE FROM officeVisit;
ALTER TABLE officeVisit AUTO_INCREMENT = 0;
DELETE FROM icdCode;
DELETE FROM diagnosis;
ALTER TABLE diagnosis AUTO_INCREMENT = 0;
DELETE FROM declaredhcp; /* Please use DELETE FROM and not TRUNCATE, otherwise the auto_increment start value gets wiped out */; 
DELETE FROM fakeemail;
ALTER TABLE fakeemail AUTO_INCREMENT = 0;
DELETE FROM globalvariables;
DELETE FROM hcpassignedhos;
DELETE FROM hcprelations;
DELETE FROM loginfailures;
DELETE FROM message;
ALTER TABLE message AUTO_INCREMENT = 0;
DELETE FROM reportrequests;
ALTER TABLE reportrequests AUTO_INCREMENT = 0;
DELETE FROM representatives;
DELETE FROM resetpasswordfailures;
DELETE FROM transactionlog;
ALTER TABLE transactionlog AUTO_INCREMENT = 0;
DELETE FROM billing;
ALTER TABLE billing AUTO_INCREMENT = 0;
DELETE FROM reviews;

DELETE FROM personalallergies;
DELETE FROM personalrelations;
DELETE FROM allergies;
ALTER TABLE allergies AUTO_INCREMENT = 0;


DELETE FROM ndcodes;
DELETE FROM druginteractions;
DELETE FROM prescription;


DELETE FROM remotemonitoringdata;
ALTER TABLE remotemonitoringdata AUTO_INCREMENT = 1;
DELETE FROM remotemonitoringlists;

DELETE FROM drugreactionoverridecodes;


DELETE FROM profilephotos;

DELETE FROM appointmentrequests;
ALTER TABLE appointmentrequests AUTO_INCREMENT = 0;
DELETE FROM hcpassignedtoward;
DELETE FROM hcpassignedhos;

DELETE FROM WardRoomCheckout;
DELETE FROM wardrooms;
ALTER TABLE wardrooms AUTO_INCREMENT = 0;
DELETE FROM wards;
ALTER TABLE wards AUTO_INCREMENT = 0;




DELETE FROM recordsrelease;
ALTER TABLE recordsrelease AUTO_INCREMENT = 0;

DELETE FROM hospitals;

DELETE FROM appointment;
ALTER TABLE appointment AUTO_INCREMENT =0;
DELETE FROM appointmenttype;
ALTER TABLE appointmenttype AUTO_INCREMENT = 0;

DELETE FROM flags;
ALTER TABLE flags AUTO_INCREMENT = 0;
DELETE FROM personnel;
ALTER TABLE personnel AUTO_INCREMENT = 9000000000;




DELETE FROM patients;
ALTER TABLE patients AUTO_INCREMENT = 0;
DELETE FROM users;

DELETE FROM medicalProcedure;
ALTER TABLE medicalProcedure AUTO_INCREMENT = 0;

