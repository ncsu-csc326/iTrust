INSERT INTO hospitals(HospitalID, HospitalName) VALUES
('9191919191','Test Hospital 9191919191'),
('8181818181','Test Hospital 8181818181')
ON DUPLICATE KEY UPDATE HospitalID = HospitalID;

INSERT INTO hospitals(HospitalID, HospitalName, Address, City, State, Zip) VALUES
('1','Health Institute Dr. E','1 Stinson Drive','Raleigh','NC','27607'),
('2','Health Institute Mr. Barry','404 Wade Avenue','Raleigh','NC','27607'),
('3','Health Institute Mr. Donghoon','9001 Sullivan Drive','Raleigh','NC','27607'),
('4','Le Awesome Hospital','1337 Oval Drive','Raleigh','NC','27607'),
('5','Facebook Rehab Center','2 Yarborough Drive','Raleigh','NC','27607'),
('6','Mental Hospital 4 iTrust Devs','1234 Hillsborough Street','Raleigh','NC','27607'),
('7','Ninja Hospital','5 Dan Allen Drive','Raleigh','NC','27607')
ON DUPLICATE KEY UPDATE HospitalID = HospitalID;