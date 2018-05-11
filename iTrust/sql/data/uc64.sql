/*Inserting initial record for Daria*/
INSERT INTO obstetrics
(MID,
pregId,
LMP,
EDD,
dateVisit,
weeksPregnant,
pregnancyStatus)
VALUES
(400,
0,
'2014-07-01',
'2015-04-07',
'2014-09-23',
'12-0',
'Initial');

/*Inserting initial record for Brenna*/
INSERT INTO obstetrics
(MID,
pregId,
LMP,
EDD,
dateVisit,
weeksPregnant,
pregnancyStatus)
VALUES
(401,
1,
'2014-07-26',
'2015-05-02',
'2014-10-14',
'11-3',
'Initial');

/*Inserting initial record for Mary*/
INSERT INTO obstetrics
(MID,
pregId,
LMP,
EDD,
dateVisit,
weeksPregnant,
pregnancyStatus)
VALUES
(404,
0,
'2013-03-14',
'2013-12-18',
'2013-03-22',
'1-1',
'Initial');

/*Inserting flags for Daria*/
INSERT INTO flags
(FID,
MID,
pregId,
flagType)
VALUES
(0,
400,
0,
'Twins');

