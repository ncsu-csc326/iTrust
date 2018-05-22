/* Sets up patients 314159*/

/*User Information*/

INSERT INTO users(MID, password, role, sQuestion, sAnswer) 
VALUES (314159, '30c952fab122c3f9759f02a6d95c3758b246b4fee239957b2d4fee46e26170c4', 'patient', 'what is your favorite color?', 'blue');


/*Deactivated Patients*/
INSERT INTO patients (MID,lastName,firstName,dateOfDeactivation)
VALUES (314159,'Baby','Fake',NOW());

 