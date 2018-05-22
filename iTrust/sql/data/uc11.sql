
 /*insert office visit for patient 26*/
INSERT INTO officevisit (
	patientMID, 
	visitDate, 
	locationID, 
	apptTypeID, 
	notes,
	weight, 
	height,
	blood_pressure,
	household_smoking_status,
	patient_smoking_status,
	hdl,
	ldl,
	triglyceride) 
VALUES (26, "2015-11-13 00:00:00", 9191919191, 1, "Overall Good!", 163, 69, '102/60', 1, 4, 60, 81, 110);

set @ov_id = LAST_INSERT_ID();

INSERT INTO icdCode (code, name, is_chronic)
VALUES ('S607','Ouch on toe', 0)
ON DUPLICATE KEY UPDATE code = code;

INSERT INTO cptCode(code, name) 
VALUES ('99214', ''), ('90717', 'Typhoid Vaccine')
ON DUPLICATE KEY UPDATE code = code;

INSERT INTO icdCode(code, name, is_chronic) VALUES
('S108','Skin injury neck', 0)
ON DUPLICATE KEY UPDATE code = code;

INSERT INTO icdCode(code, name, is_chronic) VALUES
('S107','Skin injury hand', 0)
ON DUPLICATE KEY UPDATE code = code;

INSERT INTO loincCode (code, component, kind_of_property, time_aspect, system, scale_type, method_type) VALUES
('18106-5', 'procedure', 'Prid', 'Pt', 'Cardiac Echo Study', 'Nom', '*');

INSERT INTO ndcodes (Code, Description) VALUES ('1234', 'good meds');

