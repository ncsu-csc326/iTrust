INSERT INTO hospitals(HospitalID, HospitalName, Address, City, State, Zip) VALUES
('8','Wal-Mart Medical Center','10000 Ballantyne Commons Pkwy','Charlotte','NC','28277')
ON DUPLICATE KEY UPDATE HospitalID = HospitalID;