DELETE FROM declaredhcp /* Please use DELETE FROM and not TRUNCATE, otherwise the auto_increment start value gets wiped out */; 
DELETE FROM fakeemail;
DELETE FROM globalvariables;
DELETE FROM hcpassignedhos;
DELETE FROM hcprelations;
DELETE FROM labprocedure;
DELETE FROM loginfailures;
DELETE FROM loinc;
DELETE FROM message;
DELETE FROM referrals;
DELETE FROM ovdiagnosis;
DELETE FROM ovmedication;
DELETE FROM ovprocedure;
DELETE FROM ovsurvey;
DELETE FROM officevisits;
DELETE FROM reportrequests;
DELETE FROM representatives;
DELETE FROM resetpasswordfailures;
DELETE FROM transactionlog;
DELETE FROM adverseevents;
DELETE FROM billing;
DELETE FROM reviews;
DELETE FROM appointment;
DELETE FROM appointmenttype;

DELETE FROM personalallergies;
DELETE FROM personalhealthinformation;
DELETE FROM personalrelations;
DELETE FROM allergies;
DELETE FROM icdcodes;

DELETE FROM obstetrics;
ALTER TABLE obstetrics AUTO_INCREMENT = 0;
DELETE FROM flags;
DELETE FROM personnel;
DELETE FROM hospitals;
DELETE FROM ndcodes;
DELETE FROM druginteractions;

DELETE FROM cptcodes;
DELETE FROM patients;
DELETE FROM historypatients;
DELETE FROM users;

DELETE FROM remotemonitoringdata;
DELETE FROM remotemonitoringlists;

DELETE FROM drugreactionoverridecodes;
DELETE FROM ovreactionoverride;

DELETE FROM profilephotos;
DELETE FROM ovprocedure;

DELETE FROM patientspecificinstructions;

DELETE FROM appointmentrequests;

DELETE FROM wards;
ALTER TABLE wards AUTO_INCREMENT = 0;
DELETE FROM hcpassignedtoward;
DELETE FROM hcpassignedhos;
DELETE FROM wardrooms;
ALTER TABLE wardrooms AUTO_INCREMENT = 0;

DELETE FROM cdcweightstats;
DELETE FROM cdcheightstats;
DELETE FROM cdcheadcircumferencestats;
DELETE FROM cdcbmistats;
DELETE FROM normaltable;

DELETE FROM requiredprocedures;
DELETE FROM recordsrelease;
ALTER TABLE recordsrelease AUTO_INCREMENT = 0;

DELETE FROM foodEntry;
DELETE FROM sleepEntry;
DELETE FROM exerciseEntry;
DELETE FROM labels;
DELETE FROM designatedNutritionist;

DELETE FROM ophthalmology;
ALTER TABLE ophthalmology AUTO_INCREMENT = 0;
DELETE FROM opdiagnosis;
ALTER TABLE opdiagnosis AUTO_INCREMENT = 0;
DELETE FROM ophthalmologySurgery;
ALTER TABLE ophthalmologySurgery AUTO_INCREMENT = 0;
DELETE FROM ophthalmologyschedule;
ALTER TABLE ophthalmologyschedule AUTO_INCREMENT = 0;
