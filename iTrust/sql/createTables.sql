/* updated 2014-2-1 */
CREATE TABLE users(
	MID                 BIGINT unsigned,
	Password            VARCHAR(200),
	Salt				VARCHAR(200) DEFAULT '',
	openID              VARCHAR(200),
	Role                enum('patient','admin','hcp','uap','er','tester','pha', 'lt') NOT NULL DEFAULT 'admin',
	sQuestion           VARCHAR(100) DEFAULT '', 
	sAnswer             VARCHAR(30) DEFAULT '',
	isDependent			tinyint(1) unsigned NOT NULL default '0',

	PRIMARY KEY (MID),
	UNIQUE (openID)
	/* Please use the MyISAM backend with no foreign keys.*/
) ENGINE=MyISAM; 

CREATE TABLE hospitals(
	HospitalID   varchar(10),
	HospitalName varchar(30) NOT NULL,
	Address varchar(30),
	City varchar(15),
	State varchar(2),
	Zip varchar(10),
	
	PRIMARY KEY (hospitalID)
) ENGINE=MyISAM;

CREATE TABLE wards(
	wardID BIGINT unsigned AUTO_INCREMENT primary key,
	inHospital varchar(10) NOT NULL,
	requiredSpecialty varchar(128),
	FOREIGN KEY (InHospital) REFERENCES hospitals (HospitalID)
) ENGINE=MyISAM;

CREATE TABLE personnel(
	MID BIGINT unsigned default NULL,
	AMID BIGINT unsigned default NULL,
	role enum('admin','hcp','uap','er','tester','pha', 'lt') NOT NULL default 'admin',
	enabled tinyint(1) unsigned NOT NULL default '0',
	lastName varchar(20) NOT NULL default '',
	firstName varchar(20) NOT NULL default '',
	address1 varchar(30) NOT NULL default '',
	address2 varchar(30) NOT NULL default '',
	city varchar(15) NOT NULL default '',
	state enum('','AK','AL','AR','AZ','CA','CO','CT','DE','DC','FL','GA','HI','IA','ID','IL','IN','KS','KY','LA','MA','MD','ME','MI','MN','MO','MS','MT','NC','ND','NE','NH','NJ','NM','NV','NY','OH','OK','OR','PA','RI','SC','SD','TN','TX','UT','VA','VT','WA','WI','WV','WY') NOT NULL default '',
	zip varchar(10) NOT NULL default '',
	phone varchar(12) NOT NULL default '',
	specialty varchar(40) default NULL,
	email varchar(55)  default '',
	PRIMARY KEY  (MID)
) auto_increment=9000000000 ENGINE=MyISAM;

CREATE TABLE patients(
	MID BIGINT unsigned  auto_increment, 
	lastName varchar(20)  default '', 
	firstName varchar(20)  default '', 
	email varchar(55)  default '', 
	address1 varchar(30)  default '', 
	address2 varchar(30)  default '', 
	city varchar(15)  default '', 
	state enum('AK','AL','AR','AZ','CA','CO','CT','DE','DC','FL','GA','HI','IA','ID','IL','IN','KS','KY','LA','MA','MD','ME','MI','MN','MO','MS','MT','NC','ND','NE','NH','NJ','NM','NV','NY','OH','OK','OR','PA','RI','SC','SD','TN','TX','UT','VA','VT','WA','WI','WV','WY')  default 'AK', 
	zip varchar(10)  default '', 
	phone varchar(12) default '',
	eName varchar(40)  default '', 
	ePhone varchar(12)  default '', 
	iCName varchar(20)  default '', 
	iCAddress1 varchar(30)  default '', 
	iCAddress2 varchar(30)  default '', 
	iCCity varchar(15)  default '', 
	ICState enum('AK','AL','AR','AZ','CA','CO','CT','DE','DC','FL','GA','HI','IA','ID','IL','IN','KS','KY','LA','MA','MD','ME','MI','MN','MO','MS','MT','NC','ND','NE','NH','NJ','NM','NV','NY','OH','OK','OR','PA','RI','SC','SD','TN','TX','UT','VA','VT','WA','WI','WV','WY')  default 'AK', 
	iCZip varchar(10)  default '', 
	iCPhone varchar(12)  default '',
	iCID varchar(20)  default '', 
	DateOfBirth DATE,
	DateOfDeath DATE,
	CauseOfDeath VARCHAR(10) default '',
	MotherMID INTEGER(10) default 0,
	FatherMID INTEGER(10) default 0,
	BloodType VARCHAR(3) default '',
	Ethnicity VARCHAR(20) default '',
	Gender VARCHAR(13) default 'Not Specified',
	TopicalNotes VARCHAR(200) default '',
	CreditCardType VARCHAR(20) default '',
	CreditCardNumber VARCHAR(19) default '',
	DirectionsToHome varchar(512) default '',
	Religion varchar(64) default '',
	Language varchar(32) default '',
	SpiritualPractices varchar(512) default '',
	AlternateName varchar(32) default '',
	DateOfDeactivation DATE default NULL,
	PRIMARY KEY (MID)
) ENGINE=MyISAM;

CREATE TABLE historypatients(
	ID BIGINT unsigned  auto_increment,
	changeDate DATE NOT NULL,
	changeMID BIGINT unsigned NOT NULL,
	MID BIGINT unsigned NOT NULL, 
	lastName varchar(20)  default '', 
	firstName varchar(20)  default '', 
	email varchar(55)  default '', 
	address1 varchar(30)  default '', 
	address2 varchar(30)  default '', 
	city varchar(15)  default '', 
	state enum('AK','AL','AR','AZ','CA','CO','CT','DE','DC','FL','GA','HI','IA','ID','IL','IN','KS','KY','LA','MA','MD','ME','MI','MN','MO','MS','MT','NC','ND','NE','NH','NJ','NM','NV','NY','OH','OK','OR','PA','RI','SC','SD','TN','TX','UT','VA','VT','WA','WI','WV','WY')  default 'AK', 
	zip varchar(10)  default '', 
	phone varchar(12) default '',
	eName varchar(40)  default '', 
	ePhone varchar(12)  default '', 	
	iCName varchar(20)  default '', 
	iCAddress1 varchar(30)  default '', 
	iCAddress2 varchar(30)  default '', 
	iCCity varchar(15)  default '', 
	ICState enum('AK','AL','AR','AZ','CA','CO','CT','DE','DC','FL','GA','HI','IA','ID','IL','IN','KS','KY','LA','MA','MD','ME','MI','MN','MO','MS','MT','NC','ND','NE','NH','NJ','NM','NV','NY','OH','OK','OR','PA','RI','SC','SD','TN','TX','UT','VA','VT','WA','WI','WV','WY')  default 'AK', 
	iCZip varchar(10)  default '', 
	iCPhone varchar(12)  default '',
	iCID varchar(20)  default '', 
	DateOfBirth DATE,
	DateOfDeath DATE,
	CauseOfDeath VARCHAR(10) default '',
	MotherMID INTEGER(10) default 0,
	FatherMID INTEGER(10) default 0,
	BloodType VARCHAR(3) default '',
	Ethnicity VARCHAR(20) default '',
	Gender VARCHAR(13) default 'Not Specified',
	TopicalNotes VARCHAR(200) default '',
	CreditCardType VARCHAR(20) default '',
	CreditCardNumber VARCHAR(19) default '',
	DirectionsToHome varchar(512) default '',
	Religion varchar(64) default '',
	Language varchar(32) default '',
	SpiritualPractices varchar(512) default '',
	AlternateName varchar(32) default '',
	DateOfDeactivation DATE default NULL,
	PRIMARY KEY (ID)
) ENGINE=MyISAM;

CREATE TABLE ophthalmology(
	MID BIGINT unsigned NOT NULL default '0',
	OID BIGINT unsigned auto_increment,
	dateVisit DATE,
	docLastName varchar(20),
	docFirstName varchar(20),
	vaNumOD int(4),
	vaDenOD int(4),
	vaNumOS int(4),
	vaDenOS int(4),
	sphereOD float,
	sphereOS float,
	cylinderOD float,
	cylinderOS float,
	axisOD int(4),
	axisOS int(4),
	addOD float,
	addOS float,
	PRIMARY KEY (OID)
) ENGINE=MyISAM;

CREATE TABLE ophthalmologySchedule(
	PATIENTMID BIGINT unsigned NOT NULL default '0',
	DOCTORMID BIGINT unsigned NOT NULL default '0',
	OID BIGINT unsigned auto_increment,
	dateTime DATETIME,
	docLastName varchar(20),
	docFirstName varchar(20),
	comments mediumtext,
	pending				BOOLEAN NOT NULL,
	accepted			BOOLEAN NOT NULL,
	PRIMARY KEY (OID)
)  ENGINE=MyISAM;

CREATE TABLE ophthalmologySurgery(
	MID BIGINT unsigned NOT NULL default '0',
	OID BIGINT unsigned auto_increment,
	dateVisit DATE,
	docLastName varchar(20),
	docFirstName varchar(20),
	vaNumOD int(4),
	vaDenOD int(4),
	vaNumOS int(4),
	vaDenOS int(4),
	sphereOD float,
	sphereOS float,
	cylinderOD float,
	cylinderOS float,
	axisOD int(4),
	axisOS int(4),
	addOD float,
	addOS float,
	surgery varchar(40),
	surgeryNotes varchar(400),
	PRIMARY KEY (OID)
) ENGINE=MyISAM;

CREATE TABLE obstetrics(
	MID BIGINT unsigned NOT NULL default '0',
	OID BIGINT unsigned auto_increment,  
	pregId BIGINT unsigned NOT NULL default '0',
	LMP DATE, 
	EDD DATE, 
	weeksPregnant varchar(4)  default '', 
	dateVisit DATE,
	yearConception int(4) default 0,
	hoursInLabor float default 0,
	deliveryType enum('Vaginal Delivery','Caesarean Section','Miscarriage')  default 'Vaginal Delivery', 
	pregnancyStatus enum('Initial', 'Office Visit', 'Complete') default 'Initial',
	weight float default 0,
	bloodPressureS int default 0,
	bloodPressureD int default 0,
	FHR int default 0,
	FHU float default 0,
	PRIMARY KEY (OID)
) ENGINE=MyISAM;

CREATE TABLE flags(
	FID BIGINT unsigned auto_increment,
	MID BIGINT unsigned NOT NULL default '0',
	pregId BIGINT unsigned NOT NULL default '0',
	flagType enum('High Blood Pressure', 'Advanced Maternal Age', 'Maternal Allergies', 'Low-Lying Placenta',
		'Genetic Miscarriage', 'Abnormal FHR', 'Twins', 'Abnormal Weight Change', 'Negative Blood Type', 'Pregnancy relevant pre-existing conditions'),
	PRIMARY KEY (FID)
) ENGINE=MyISAM;

CREATE TABLE loginfailures(
	ipaddress varchar(128) NOT NULL, 
	failureCount int NOT NULL default 0, 
	lastFailure TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
	PRIMARY KEY (ipaddress)
) ENGINE=MyISAM;

CREATE TABLE resetpasswordfailures(
	ipaddress varchar(128) NOT NULL, 
	failureCount int NOT NULL default 0, 
	lastFailure TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
	PRIMARY KEY (ipaddress)
) ENGINE=MyISAM;

CREATE TABLE icdcodes (
  Code decimal(5,2) NOT NULL,
  Description TEXT NOT NULL,
  Chronic enum('no','yes') NOT NULL default 'no',
  Ophthalmology enum('no','yes') NOT NULL default 'no',
  URL varchar(512) NOT NULL default '',
  PRIMARY KEY (Code)
) ENGINE=MyISAM;

CREATE TABLE cptcodes(
	Code varchar(5) NOT NULL COMMENT 'Actual CPT Code', 
	Description varchar(30) NOT NULL COMMENT 'Description of the CPT Codes', 
	Attribute varchar(30),
	PRIMARY KEY (Code)
) ENGINE=MyISAM;

CREATE TABLE drugreactionoverridecodes(
	Code varchar(5) NOT NULL COMMENT 'Identifier for override reason',
	Description varchar(80) NOT NULL COMMENT 'Description of override reason',
	PRIMARY KEY (Code)
) ENGINE=MyISAM;
	
CREATE TABLE ndcodes(
	Code varchar(10) NOT NULL, 
	Description varchar(100) NOT NULL, 
	PRIMARY KEY  (Code)
) ENGINE=MyISAM;

CREATE TABLE druginteractions(
	FirstDrug varchar(9) NOT NULL,
	SecondDrug varchar(9) NOT NULL,
	Description varchar(100) NOT NULL,
	PRIMARY KEY  (FirstDrug,SecondDrug)
) ENGINE=MyISAM;

CREATE TABLE transactionlog(
	transactionID int(10) unsigned NOT NULL auto_increment, 
	loggedInMID BIGINT unsigned NOT NULL DEFAULT '0', 
	secondaryMID BIGINT unsigned NOT NULL DEFAULT '0', 
	transactionCode int(10) UNSIGNED NOT NULL default '0', 
	timeLogged timestamp NOT NULL default CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP, 
	addedInfo VARCHAR(255) default '',
	PRIMARY KEY (transactionID)
) ENGINE=MyISAM;

CREATE TABLE hcprelations(
	HCP BIGINT unsigned NOT NULL default '0', 
	UAP BIGINT unsigned NOT NULL default '0',
	PRIMARY KEY (HCP, UAP)
) ENGINE=MyISAM;

CREATE TABLE personalrelations(
	PatientID BIGINT unsigned NOT NULL COMMENT 'MID of the patient',
	RelativeID BIGINT unsigned NOT NULL COMMENT 'MID of the Relative',
	RelativeType VARCHAR( 35 ) NOT NULL COMMENT 'Relation Type'
) ENGINE=MyISAM;

CREATE TABLE representatives(
	representerMID BIGINT unsigned default 0, 
	representeeMID BIGINT unsigned default 0,
	PRIMARY KEY  (representerMID,representeeMID)
) ENGINE=MyISAM;

CREATE TABLE hcpassignedhos(
	hosID VARCHAR(10) NOT NULL, 
	HCPID BIGINT unsigned NOT NULL, 
	PRIMARY KEY (hosID,HCPID)
) ENGINE=MyISAM;

CREATE TABLE declaredhcp(
	PatientID BIGINT unsigned NOT NULL default '0', 
	HCPID BIGINT unsigned NOT NULL default '0', 
	PRIMARY KEY  (PatientID,HCPID)
) ENGINE=MyISAM;

CREATE TABLE officevisits(
	ID int(10) unsigned auto_increment,
	visitDate date default '0000-00-00', 
	appt_type           VARCHAR(30) NOT NULL default 'General Checkup',  /* UC60 */
	HCPID BIGINT unsigned default '0', 
	notes mediumtext, 
	PatientID BIGINT unsigned default '0', 
	HospitalID VARCHAR(10) default '',
	IsERIncident BOOLEAN default false,
	IsBilled BOOLEAN default false, /* UC60 */
	PRIMARY KEY  (ID)
) ENGINE=MyISAM;

CREATE TABLE billing( /* UC60 */
	billID       int(10) unsigned auto_increment,
	appt_id      INT UNSIGNED,
	PatientID    BIGINT unsigned NOT NULL default '0',
	HCPID        BIGINT unsigned default '0', 
	billTimeS    DATE,
	amt          int,
	status       VARCHAR(20) default '',
	ccHolderName VARCHAR(30),
	billingAddress VARCHAR(120),
	ccType		 VARCHAR(20),
	ccNumber	VARCHAR(40),
	cvv			VARCHAR(4),
	insHolderName VARCHAR(30),
	insID		VARCHAR(20),
	insProviderName	VARCHAR(20),
	insAddress1	VARCHAR(30),
	insAddress2	VARCHAR(30),
	insCity		VARCHAR(20),
	insState	enum('AK','AL','AR','AZ','CA','CO','CT','DE','DC','FL','GA','HI','IA','ID','IL','IN','KS','KY','LA','MA','MD','ME','MI','MN','MO','MS','MT','NC','ND','NE','NH','NJ','NM','NV','NY','OH','OK','OR','PA','RI','SC','SD','TN','TX','UT','VA','VT','WA','WI','WV','WY'),
	insZip		VARCHAR(10),
	insPhone	VARCHAR(12),
	submissions int default 0,
	subTime     datetime,
	isInsurance BOOLEAN default false,
	PRIMARY KEY  (billID),
	FOREIGN KEY  (appt_id) REFERENCES appointment (appt_id),
	FOREIGN KEY  (PatientID) REFERENCES patients (MID),
	FOREIGN KEY  (HCPID) REFERENCES personnel (MID)
) ENGINE=MyISAM;

CREATE TABLE personalhealthinformation (
	PatientID BIGINT unsigned NOT NULL default '0',
	OfficeVisitID BIGINT unsigned NOT NULL default '0',
	Height float default '0',  
	Weight float default '0',  
	HeadCircumference float default '0',
	Smoker tinyint(1) NOT NULL default '0' COMMENT 'Is the person a smoker',
	SmokingStatus int(1) NOT NULL default '9' COMMENT 'Smoking Status Code',
	HouseholdSmokingStatus int(1) NOT NULL default '1' COMMENT 'Household Smoking Status Code',
	BloodPressureN int(11) default '0',  
	BloodPressureD int(11) default '0',  
	CholesterolHDL int(11) default '0' COMMENT 'HDL Cholesterol',  
	CholesterolLDL int(11) default '0' COMMENT 'LDL Ccholesterol',  
	CholesterolTri int(11) default '0' COMMENT 'Cholesterol Triglyceride',  
	HCPID BIGINT unsigned default NULL,  
	AsOfDate timestamp NOT NULL default CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP,
	OfficeVisitDate date NOT NULL default '0000-00-00',
	BMI float default '-1'
	/*PRIMARY KEY (OfficeVisitID)*/
) ENGINE=MyISAM;

CREATE TABLE personalallergies(
	PatientID BIGINT unsigned NOT NULL COMMENT 'MID of the Patient',
	Allergy VARCHAR( 50 ) NOT NULL COMMENT 'Description of the allergy'
) ENGINE=MyISAM;


CREATE TABLE allergies(
	ID INT(10) unsigned auto_increment primary key,
	PatientID BIGINT unsigned NOT NULL COMMENT 'MID of the Patient',
	Description VARCHAR( 50 ) NOT NULL COMMENT 'Description of the allergy',
	FirstFound TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
	Code varchar(10) COMMENT 'NDCode of drug that patient is allergic to'
	/*NEW, Added Code, so that we could pass the NDCode of the drug when adding allergy.*/
) ENGINE=MyISAM;

CREATE TABLE ovprocedure(
	ID INT(10) auto_increment primary key,
	VisitID INT( 10 ) unsigned NOT NULL COMMENT 'ID of the Office Visit',
	CPTCode VARCHAR( 5 ) NOT NULL COMMENT 'CPTCode of the procedure',
	HCPID VARCHAR( 10 ) NOT NULL DEFAULT ''
) ENGINE=MyISAM;

CREATE TABLE ovmedication (
    ID INT(10)  auto_increment primary key,
	VisitID INT( 10 ) unsigned NOT NULL COMMENT 'ID of the Office Visit',
	NDCode VARCHAR( 9 ) NOT NULL COMMENT 'NDCode for the medication',
	StartDate DATE,
	EndDate DATE,
	Dosage INT DEFAULT 0 COMMENT 'Always in mg - this could certainly be changed later',
	Instructions VARCHAR(500) DEFAULT '',
	OverrideOther VARCHAR(255) DEFAULT '' COMMENT 'Provided if user chooses other reason'
) ENGINE=MyISAM;

CREATE TABLE ovreactionoverride (
	ID INT(10)  auto_increment primary key,
	OVMedicationID INT(10) NOT NULL COMMENT 'Must correspond to an ID in OVMedication table',
	OverrideCode VARCHAR(5) COMMENT 'Code identifier of the override reason',
	FOREIGN KEY (OVMedicationID) REFERENCES OVMedication (ID)
) ENGINE=MyISAM;

CREATE TABLE ovdiagnosis (
    ID INT(10) auto_increment primary key,
	VisitID INT( 10 ) unsigned NOT NULL COMMENT 'ID of the Office Visit',
	ICDCode DECIMAL( 5, 2 ) NOT NULL COMMENT 'Code for the Diagnosis',
    URL VARCHAR(512) COMMENT 'URL for information'
) ENGINE=MyISAM;

CREATE TABLE globalvariables (
	Name VARCHAR(20) primary key,
	Value VARCHAR(20)
) ENGINE=MyISAM;

INSERT INTO globalvariables(Name,Value) VALUES ('Timeout', '20');

CREATE TABLE fakeemail(
	ID INT(10) auto_increment primary key,
	ToAddr VARCHAR(100),
	FromAddr VARCHAR(100),
	Subject VARCHAR(500),
	Body VARCHAR(2000),
	AddedDate timestamp NOT NULL default CURRENT_TIMESTAMP
) ENGINE=MyISAM;

CREATE TABLE reportrequests (
	ID INT(10) auto_increment primary key,
    RequesterMID BIGINT unsigned,
    PatientMID BIGINT unsigned,
    ApproverMID BIGINT unsigned,
    RequestedDate datetime,
    ApprovedDate datetime,
    ViewedDate datetime,
    Status varchar(30),
	Comment TEXT
) ENGINE=MyISAM;

CREATE TABLE ovsurvey (
	VisitID int(10) unsigned primary key COMMENT 'ID of the Office Visit',
	SurveyDate datetime not null COMMENT 'Date the survey was completed',
	WaitingRoomMinutes int(3) COMMENT 'How many minutes did you wait in the waiting room?',
	ExamRoomMinutes int(3) COMMENT 'How many minutes did you wait in the examination room before seeing your physician?',
    VisitSatisfaction int(1) COMMENT 'How satisfied were you with your office visit?',
	TreatmentSatisfaction int(1) COMMENT 'How satisfied were you with the treatment or information you received?'
) ENGINE=MyISAM;

CREATE TABLE loinc (
	LaboratoryProcedureCode VARCHAR (7), 
	Component VARCHAR(100),
	KindOfProperty VARCHAR(100),
	TimeAspect VARCHAR(100),
	System VARCHAR(100),
	ScaleType VARCHAR(100),
	MethodType VARCHAR(100)
) ENGINE=MyISAM;

CREATE TABLE labprocedure (
	LaboratoryProcedureID BIGINT(10) auto_increment primary key,
	PatientMID BIGINT unsigned, 
	LaboratoryProcedureCode VARCHAR (7), 
	Rights VARCHAR(10),
	Status VARCHAR(20),
	Commentary MEDIUMTEXT,
	Results MEDIUMTEXT,
	NumericalResults VARCHAR(20),
	NumericalResultsUnit VARCHAR(20),
	UpperBound VARCHAR(20),
	LowerBound VARCHAR(20),	
	OfficeVisitID INT unsigned,
	LabTechID LONG,
	PriorityCode INT unsigned,
	ViewedByPatient BOOLEAN NOT NULL default FALSE,
	UpdatedDate timestamp NOT NULL default CURRENT_TIMESTAMP
) ENGINE=MyISAM;

CREATE TABLE message (
	message_id          INT UNSIGNED AUTO_INCREMENT,
	parent_msg_id       INT UNSIGNED,
	original_msg_id     INT UNSIGNED,
	from_id             BIGINT UNSIGNED NOT NULL,
	to_id               BIGINT UNSIGNED NOT NULL,
	sent_date           DATETIME NOT NULL,
	message             TEXT,
	subject				TEXT,
	been_read			INT UNSIGNED,
	PRIMARY KEY (message_id)
) ENGINE=MyISAM;

CREATE TABLE appointment (
	appt_id				INT UNSIGNED AUTO_INCREMENT primary key,
	doctor_id           BIGINT UNSIGNED NOT NULL,
	patient_id          BIGINT UNSIGNED NOT NULL,
	sched_date          DATETIME NOT NULL,
	appt_type           VARCHAR(30) NOT NULL,
	comment				TEXT
) ENGINE=MyISAM;

CREATE TABLE appointmenttype (
	apptType_id			INT UNSIGNED AUTO_INCREMENT primary key,
	appt_type           VARCHAR(30) NOT NULL,
	duration			INT UNSIGNED NOT NULL,
	price				INT UNSIGNED NOT NULL default '0' /* UC60 */
) ENGINE=MyISAM;

CREATE TABLE referrals (
	id          INT UNSIGNED AUTO_INCREMENT,
	PatientID          BIGINT UNSIGNED NOT NULL,
	SenderID               BIGINT UNSIGNED NOT NULL,
	ReceiverID           BIGINT UNSIGNED NOT NULL,
	ReferralDetails             TEXT,
	OVID		BIGINT UNSIGNED NOT NULL,
	viewed_by_patient 	boolean NOT NULL,
	viewed_by_HCP 	boolean NOT NULL,
	TimeStamp DATETIME NOT NULL,
	PriorityCode INT unsigned,
	PRIMARY KEY (id)
) AUTO_INCREMENT=1 ENGINE=MyISAM;

CREATE TABLE remotemonitoringdata (
	id          INT UNSIGNED AUTO_INCREMENT,
	PatientID          BIGINT UNSIGNED NOT NULL,
	systolicBloodPressure int(10) SIGNED default -1,
	diastolicBloodPressure int(10) SIGNED default -1,
	glucoseLevel int(10) SIGNED default -1,
	height float default -1,
	weight float default -1,
	pedometerReading int(10) SIGNED default -1,
	timeLogged timestamp NOT NULL default CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP,
	ReporterRole		TEXT,
	ReporterID          BIGINT UNSIGNED NOT NULL,
	PRIMARY KEY (id)
) AUTO_INCREMENT=1 ENGINE=MyISAM;

CREATE TABLE remotemonitoringlists (
	PatientMID BIGINT unsigned default 0, 
	HCPMID BIGINT unsigned default 0,
	SystolicBloodPressure BOOLEAN default true,
	DiastolicBloodPressure BOOLEAN default true,
	GlucoseLevel BOOLEAN default true,
	Height BOOLEAN default true,
	Weight BOOLEAN default true,
	PedometerReading BOOLEAN default true,
	PRIMARY KEY  (PatientMID,HCPMID)
) ENGINE=MyISAM;

CREATE TABLE adverseevents (
	id INT UNSIGNED AUTO_INCREMENT primary key,
	Status VARCHAR(10) default "Active",
	PatientMID BIGINT unsigned default 0,
	PresImmu VARCHAR(50),
	Code VARCHAR(20),
	Comment VARCHAR(2000),
	Prescriber BIGINT unsigned default 0,
	TimeLogged timestamp NOT NULL default CURRENT_TIMESTAMP
) ENGINE=MyISAM;

CREATE TABLE profilephotos (
	MID BIGINT (10) primary key,
	Photo LONGBLOB,
	UpdatedDate timestamp NOT NULL default CURRENT_TIMESTAMP
) ENGINE=MyISAM;

CREATE TABLE patientspecificinstructions (
    id BIGINT unsigned AUTO_INCREMENT primary key,
    VisitID BIGINT unsigned,
    Modified TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    Name VARCHAR(100),
    URL VARCHAR(250),
    Comment VARCHAR(500)
) ENGINE=MyISAM;

CREATE TABLE referralmessage(
	messageID  INT unsigned NOT NULL, 
	referralID INT unsigned NOT NULL, 
	PRIMARY KEY (messageID,referralID)
) ENGINE=MyISAM;

CREATE TABLE appointmentrequests(
	appt_id				INT UNSIGNED AUTO_INCREMENT primary key,
	doctor_id           BIGINT UNSIGNED NOT NULL,
	patient_id          BIGINT UNSIGNED NOT NULL,
	sched_date          DATETIME NOT NULL,
	appt_type           VARCHAR(30) NOT NULL,
	comment				TEXT,
	pending				BOOLEAN NOT NULL,
	accepted			BOOLEAN NOT NULL
) ENGINE=MyISAM;

CREATE TABLE WardRooms(
	RoomID BIGINT unsigned AUTO_INCREMENT primary key,
	OccupiedBy BIGINT unsigned default NULL,
	InWard BIGINT unsigned NOT NULL,
	RoomName varchar(128),
	Status varchar(128),
	FOREIGN KEY (InWard) REFERENCES wards (WardID),
	FOREIGN KEY (OccupiedBy) REFERENCES patients (MID)
) ENGINE=MyISAM;

CREATE TABLE HCPAssignedToWard(
	HCP BIGINT unsigned,
	WARD BIGINT unsigned,
	FOREIGN KEY (WARD) REFERENCES wards (WardID),
	FOREIGN KEY (HCP) REFERENCES personnel (MID)
) ENGINE=MyISAM;

CREATE TABLE WardRoomCheckout(
	PID BIGINT unsigned default NULL,
	Reason VARCHAR(120),
	FOREIGN KEY (PID) REFERENCES patients (MID)
) ENGINE=MyISAM;

/*Table for storing weight based on sex and age */
CREATE TABLE cdcweightstats(
	sex INT NOT NULL,
	age FLOAT NOT NULL,
	L	DOUBLE NOT NULL,
	M	DOUBLE NOT NULL,
	S	DOUBLE NOT NULL,
	PRIMARY KEY(sex, age)
) ENGINE=innoDB;

/*Table for storing height based on sex and age */
CREATE TABLE cdcheightstats(
	sex INT NOT NULL,
	age FLOAT NOT NULL,
	L	DOUBLE NOT NULL,
	M	DOUBLE NOT NULL,
	S	DOUBLE NOT NULL,
	PRIMARY KEY(sex, age)
) ENGINE=innoDB;

/*Table for storing head circumference statistics based on sex and age */
CREATE TABLE cdcheadcircumferencestats(
	sex INT NOT NULL,
	age FLOAT NOT NULL,
	L	DOUBLE NOT NULL,
	M	DOUBLE NOT NULL,
	S	DOUBLE NOT NULL,
	PRIMARY KEY(sex, age)
) ENGINE=innoDB;

/*Table for storing bmi statistics based on sex and age */
CREATE TABLE cdcbmistats(
	sex INT NOT NULL,
	age FLOAT NOT NULL,
	L	DOUBLE NOT NULL,
	M	DOUBLE NOT NULL,
	S	DOUBLE NOT NULL,
	PRIMARY KEY(sex, age)
) ENGINE=innoDB;

/*Table for storing z-score values and probabilities*/
CREATE TABLE normaltable(
	z	DOUBLE NOT NULL,
	_00 DOUBLE NOT NULL,
	_01 DOUBLE NOT NULL,
	_02 DOUBLE NOT NULL,
	_03 DOUBLE NOT NULL,
	_04 DOUBLE NOT NULL,
	_05 DOUBLE NOT NULL,
	_06 DOUBLE NOT NULL,
	_07 DOUBLE NOT NULL,
	_08 DOUBLE NOT NULL,
	_09 DOUBLE NOT NULL,
	PRIMARY KEY(z)
) ENGINE=innoDB;

/*Table for maintaining records release information*/
CREATE TABLE recordsrelease(
	releaseID BIGINT UNSIGNED AUTO_INCREMENT,
	requestDate DATETIME NOT NULL,
	pid	BIGINT UNSIGNED NOT NULL,
	releaseHospitalID VARCHAR(30) NOT NULL,
	recHospitalName VARCHAR(30) NOT NULL,
	recHospitalAddress VARCHAR(120) NOT NULL,
	docFirstName VARCHAR(20) NOT NULL,
	docLastName VARCHAR(20) NOT NULL,
	docPhone VARCHAR(12) NOT NULL,
	docEmail VARCHAR(100) NOT NULL,
	justification VARCHAR(120),
	status INT NOT NULL,
	PRIMARY KEY(releaseID)
) ENGINE=innoDB;

CREATE TABLE requiredprocedures(
	cptCode VARCHAR(10) NOT NULL,
	description VARCHAR(30) NOT NULL,
	ageGroup INT NOT NULL,
	attribute VARCHAR(30) NOT NULL,
	ageMax BIGINT unsigned default NULL
)ENGINE=innoDB;
    
CREATE TABLE reviews(
    mid  BIGINT UNSIGNED NOT NULL,
    pid  BIGINT UNSIGNED NOT NULL,
    reviewdate DATETIME NOT NULL,
    descriptivereview VARCHAR(500),
    rating INT UNSIGNED NOT NULL,
    title VARCHAR(200)
)ENGINE=innoDB;

CREATE TABLE IF NOT EXISTS obstetricsconditions (
  `cid` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  `mid` BIGINT NOT NULL,
  `condition` VARCHAR(64) NOT NULL,
  PRIMARY KEY (`cid`),
  UNIQUE INDEX `cid_UNIQUE` (`cid` ASC)
)ENGINE=innoDB;

CREATE TABLE IF NOT EXISTS zipcodes (
  `zip` varchar(5) NOT NULL default '',
  `state` char(2) NOT NULL default '',
  `latitude` varchar(10) NOT NULL default '',
  `longitude` varchar(10) NOT NULL default '',
  `city` varchar(50) default NULL,
  `full_state` varchar(50) default NULL,
  PRIMARY KEY `zip` (`zip`)
) ENGINE=innoDB;

CREATE TABLE IF NOT EXISTS foodEntry (
	EntryID BIGINT UNSIGNED,
	DateEaten DATE NOT NULL,
	MealType enum('Breakfast', 'Lunch', 'Dinner', 'Snack'),
	FoodName varchar(50) NOT NULL,
	Servings DOUBLE UNSIGNED NOT NULL,
	Calories DOUBLE UNSIGNED NOT NULL,
	Fat DOUBLE UNSIGNED NOT NULL,
	Sodium DOUBLE UNSIGNED NOT NULL,
	Carbs DOUBLE UNSIGNED NOT NULL,
	Sugar DOUBLE UNSIGNED NOT NULL,
	Fiber DOUBLE UNSIGNED NOT NULL,
	Protein DOUBLE UNSIGNED NOT NULL,
	PatientID BIGINT UNSIGNED,
	LabelID BIGINT default NULL,
	PRIMARY KEY (EntryID),
	FOREIGN KEY (LabelID) REFERENCES labels (EntryID)
	) ENGINE = MyISAM;

CREATE TABLE IF NOT EXISTS sleepEntry (
	EntryID BIGINT UNSIGNED,
	Date DATE NOT NULL,
	SleepType enum('Nightly', 'Nap'),
	Hours DOUBLE UNSIGNED NOT NULL,
	PatientID BIGINT UNSIGNED,
	LabelID BIGINT default NULL,
	PRIMARY KEY (EntryID),
	FOREIGN KEY (LabelID) REFERENCES labels (EntryID)
	) ENGINE = MyISAM;

CREATE TABLE IF NOT EXISTS exerciseEntry (
	EntryID BIGINT UNSIGNED,
	Date DATE NOT NULL,
	ExerciseType enum('Cardio', 'Weight Training'),
	Name varchar(50) NOT NULL,
	Hours DOUBLE UNSIGNED NOT NULL,
	Calories INT UNSIGNED NOT NULL,
	Sets INT UNSIGNED,
	Reps INT UNSIGNED,
	PatientID BIGINT UNSIGNED,
	LabelID BIGINT default NULL,
	PRIMARY KEY (EntryID),
	FOREIGN KEY (LabelID) REFERENCES labels (EntryID)
) ENGINE = MyISAM;
	
CREATE TABLE IF NOT EXISTS labels (
	EntryID BIGINT UNSIGNED AUTO_INCREMENT,
	PatientID BIGINT UNSIGNED,
	LabelName varchar(50) NOT NULL,
	LabelColor varchar(10),
	PRIMARY KEY(EntryID)
) ENGINE = MyISAM;
	
CREATE TABLE IF NOT EXISTS designatedNutritionist(
	PatientID BIGINT unsigned NOT NULL default '0', 
	HCPID BIGINT unsigned NOT NULL default '0', 
	PRIMARY KEY  (PatientID)
) ENGINE=MyISAM;

CREATE TABLE IF NOT EXISTS opdiagnosis(
    ID INT(10) auto_increment primary key,
	VisitID INT( 10 ) unsigned NOT NULL COMMENT 'ID of the Ophthalmology Visit',
	ICDCode DECIMAL( 5, 2 ) NOT NULL COMMENT 'Code for the Diagnosis',
    URL VARCHAR(512) COMMENT 'URL for information'
) ENGINE=MyISAM;
