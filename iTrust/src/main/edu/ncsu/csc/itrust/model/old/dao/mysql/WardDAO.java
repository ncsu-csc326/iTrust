package edu.ncsu.csc.itrust.model.old.dao.mysql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import edu.ncsu.csc.itrust.exception.DBException;
import edu.ncsu.csc.itrust.exception.ITrustException;
import edu.ncsu.csc.itrust.model.old.beans.HospitalBean;
import edu.ncsu.csc.itrust.model.old.beans.PersonnelBean;
import edu.ncsu.csc.itrust.model.old.beans.WardBean;
import edu.ncsu.csc.itrust.model.old.beans.WardRoomBean;
import edu.ncsu.csc.itrust.model.old.beans.loaders.HospitalBeanLoader;
import edu.ncsu.csc.itrust.model.old.beans.loaders.PersonnelLoader;
import edu.ncsu.csc.itrust.model.old.beans.loaders.WardBeanLoader;
import edu.ncsu.csc.itrust.model.old.beans.loaders.WardRoomBeanLoader;
import edu.ncsu.csc.itrust.model.old.dao.DAOFactory;

/**
 * Used for managing Wards
 * 
 * DAO stands for Database Access Object. All DAOs are intended to be
 * reflections of the database, that is, one DAO per table in the database (most
 * of the time). For more complex sets of queries, extra DAOs are added. DAOs
 * can assume that all data has been validated and is correct.
 * 
 * DAOs should never have setters or any other parameter to the constructor than
 * a factory. All DAOs should be accessed by DAOFactory (@see
 * {@link DAOFactory}) and every DAO should have a factory - for obtaining JDBC
 * connections and/or accessing other DAOs.
 */
public class WardDAO {

	private DAOFactory factory;
	private WardBeanLoader wardLoader = new WardBeanLoader();
	private WardRoomBeanLoader wardRoomLoader = new WardRoomBeanLoader();
	private PersonnelLoader personnelLoader = new PersonnelLoader();
	private HospitalBeanLoader hospitalLoader = new HospitalBeanLoader();

	/**
	 * The typical constructor.
	 * 
	 * @param factory
	 *            The {@link DAOFactory} associated with this DAO, which is used
	 *            for obtaining SQL connections, etc.
	 */
	public WardDAO(DAOFactory factory) {
		this.factory = factory;
	}

	/**
	 * Returns a list of all wards under a hospital sorted alphabetically
	 * 
	 * @param id
	 *            The ID of the hospital to get wards from
	 * @return A java.util.List of WardBeans.
	 * @throws DBException
	 */
	public List<WardBean> getAllWardsByHospitalID(String id) throws DBException {
		try (Connection conn = factory.getConnection();
				PreparedStatement ps = conn
						.prepareStatement("SELECT * FROM WARDS WHERE InHospital = ? ORDER BY RequiredSpecialty")) {
			ps.setString(1, id);
			ResultSet rs = ps.executeQuery();
			List<WardBean> wards = wardLoader.loadList(rs);
			rs.close();
			return wards;
		} catch (SQLException e) {
			throw new DBException(e);
		}
	}

	/**
	 * Adds a Ward
	 * 
	 * @param ward
	 *            The WardBean object to insert.
	 * @return A boolean indicating whether the insertion was successful.
	 * @throws DBException
	 * @throws ITrustException
	 */
	public boolean addWard(WardBean ward) throws DBException, ITrustException {
		try (Connection conn = factory.getConnection();
				PreparedStatement ps = conn
						.prepareStatement("INSERT INTO wards (requiredSpecialty, inHospital) " + "VALUES (?,?)")) {
			ps.setString(1, ward.getRequiredSpecialty());
			ps.setLong(2, ward.getInHospital());
			boolean added = ps.executeUpdate() == 1;
			return added;
		} catch (SQLException e) {
			if (e.getErrorCode() == 1062) {
				throw new ITrustException("Error: Ward already exists.");
			} else {
				throw new DBException(e);
			}
		}
	}

	/**
	 * Updates a particular ward's information. Returns the number of rows
	 * affected (should be 1)
	 * 
	 * @param ward
	 *            The WardBean to update.
	 * @return An int indicating the number of affected rows.
	 * @throws DBException
	 */
	public int updateWard(WardBean ward) throws DBException {
		try (Connection conn = factory.getConnection();
				PreparedStatement ps = conn
						.prepareStatement("UPDATE wards SET requiredSpecialty=?, InHospital=? " + "WHERE WardID = ?")) {
			ps.setString(1, ward.getRequiredSpecialty());
			ps.setLong(2, ward.getInHospital());
			ps.setLong(3, ward.getWardID());
			int result = ps.executeUpdate();
			return result;
		} catch (SQLException e) {
			throw new DBException(e);
		}
	}

	/**
	 * Removes a ward from a hospital. Returns whether or not any changes were
	 * made
	 * 
	 * @param id
	 *            The WardId of the Ward to remove.
	 * @return A boolean indicating success.
	 * @throws DBException
	 */
	public boolean removeWard(long id) throws DBException {
		try (Connection conn = factory.getConnection();
				PreparedStatement ps = conn.prepareStatement("DELETE FROM wards WHERE wardID = ?")) {
			ps.setLong(1, id);
			boolean deleted = ps.executeUpdate() == 1;
			return deleted;
		} catch (SQLException e) {
			throw new DBException(e);
		}
	}

	/**
	 * Returns a list of all wardrooms under a ward sorted alphabetically
	 * 
	 * @param id
	 *            The id of the ward to get all rooms for
	 * @return A java.util.List of all WardRoomBeans in a ward.
	 * @throws DBException
	 */
	public List<WardRoomBean> getAllWardRoomsByWardID(long id) throws DBException {
		try (Connection conn = factory.getConnection();
				PreparedStatement ps = conn
						.prepareStatement("SELECT * FROM wardrooms WHERE inWard = ? ORDER BY roomName")) {
			ps.setLong(1, id);
			ResultSet rs = ps.executeQuery();
			List<WardRoomBean> wards = wardRoomLoader.loadList(rs);
			rs.close();
			return wards;
		} catch (SQLException e) {
			throw new DBException(e);
		}
	}

	/**
	 * Adds a WardRoom
	 * 
	 * @param wardRoom
	 *            The WardRoomBean object to insert.
	 * @return A boolean indicating whether the insertion was successful.
	 * @throws DBException
	 * @throws ITrustException
	 */
	public boolean addWardRoom(WardRoomBean wardRoom) throws DBException, ITrustException {
		try (Connection conn = factory.getConnection();
				PreparedStatement ps = conn
						.prepareStatement("INSERT INTO wardrooms (InWard, RoomName, Status) " + "VALUES (?,?,?)")) {
			ps.setLong(1, wardRoom.getInWard());
			ps.setString(2, wardRoom.getRoomName());
			ps.setString(3, wardRoom.getStatus());
			boolean added = ps.executeUpdate() == 1;
			return added;
		} catch (SQLException e) {
			throw e.getErrorCode() == 1062 ? new ITrustException("Error: WardRoom already exists.")
					: new DBException(e);
		}
	}

	/**
	 * Updates a particular wardroom's information. Returns the number of rows
	 * affected (should be 1)
	 * 
	 * @param wardRoom
	 *            The WardRoomBean to update.
	 * @return An int indicating the number of affected rows.
	 * @throws DBException
	 */
	public int updateWardRoom(WardRoomBean wardRoom) throws DBException {
		try (Connection conn = factory.getConnection();
				PreparedStatement ps = conn.prepareStatement(
						"UPDATE wardrooms SET InWard=?, RoomName=?, Status=? " + "WHERE RoomID = ?")) {
			ps.setLong(1, wardRoom.getInWard());
			ps.setString(2, wardRoom.getRoomName());
			ps.setString(3, wardRoom.getStatus());
			ps.setLong(4, wardRoom.getRoomID());
			int result = ps.executeUpdate();
			return result;
		} catch (SQLException e) {
			throw new DBException(e);
		}
	}

	/**
	 * Removes a room from a ward. Returns whether or not any changes were made
	 * 
	 * @param id
	 *            The RoomId of the Room to remove.
	 * @return A boolean indicating success.
	 * @throws DBException
	 */
	public boolean removeWardRoom(long id) throws DBException {
		try (Connection conn = factory.getConnection();
				PreparedStatement ps = conn.prepareStatement("DELETE FROM WardRooms WHERE RoomID = ?")) {
			ps.setLong(1, id);
			boolean removed = ps.executeUpdate() == 1;
			return removed;
		} catch (SQLException e) {
			throw new DBException(e);
		}
	}

	/**
	 * Returns a list of all wards assigned to a HCP sorted alphabetically
	 * 
	 * @param id
	 *            The id of the HCP to get wards for
	 * @return A java.util.List of all WardBeans.
	 * @throws DBException
	 */
	public List<WardBean> getAllWardsByHCP(long id) throws DBException {
		try (Connection conn = factory.getConnection();
				PreparedStatement ps = conn.prepareStatement(
						"SELECT * FROM HCPAssignedToWard haw INNER JOIN Wards w WHERE HCP = ? AND haw.ward = w.wardid ORDER BY RequiredSpecialty")) {
			ps.setLong(1, id);
			ResultSet rs = ps.executeQuery();

			List<WardBean> wards = wardLoader.loadList(rs);
			rs.close();
			return wards;
		} catch (SQLException e) {
			throw new DBException(e);
		}
	}

	/**
	 * Returns a list of all HCPs assigned to a ward sorted alphabetically
	 * 
	 * @param id
	 *            The id of the ward to get HCPs for
	 * @return A java.util.List of PersonnelBean that represent the HCPs
	 *         assigned to a ward.
	 * @throws DBException
	 */
	public List<PersonnelBean> getAllHCPsAssignedToWard(long id) throws DBException {
		try (Connection conn = factory.getConnection();
				PreparedStatement ps = conn.prepareStatement(
						"SELECT * FROM HCPAssignedToWard haw INNER JOIN Personnel p WHERE haw.HCP = p.MID AND WARD = ? ORDER BY lastName")) {
			ps.setLong(1, id);
			ResultSet rs = ps.executeQuery();
			List<PersonnelBean> assignedHCPs = personnelLoader.loadList(rs);
			rs.close();
			return assignedHCPs;
		} catch (SQLException e) {
			throw new DBException(e);
		}
	}

	/**
	 * Assigns an HCP to a the specified ward.
	 * 
	 * @param hcpID
	 *            The id of the HCP to assign
	 * @param wardID
	 *            The ward to assign the HCP to.
	 * @return A boolean whether or not the insert worked correctly.
	 * @throws ITrustException
	 */
	public boolean assignHCPToWard(long hcpID, long wardID) throws ITrustException {
		try (Connection conn = factory.getConnection();
				PreparedStatement ps = conn.prepareStatement("INSERT INTO HCPAssignedToWard (HCP, WARD) Values(?,?)")) {
			ps.setLong(1, hcpID);
			ps.setLong(2, wardID);

			boolean assigned = ps.executeUpdate() == 1;
			return assigned;
		} catch (SQLException e) {
			if (e.getErrorCode() == 1062) {
				throw new ITrustException("Error: HCP or WARD ID don't exist!");
			} else {
				throw new DBException(e);
			}
		}
	}

	/**
	 * Removes a HCP and Ward association
	 * 
	 * @param wardID
	 *            The Ward ID of the Ward associated to the HCP.
	 * @param hcpID
	 *            The HCP ID of the HCP associated with the Ward.
	 * @return A boolean indicating success.
	 * @throws DBException
	 */
	public boolean removeWard(long hcpID, long wardID) throws DBException {
		try (Connection conn = factory.getConnection();
				PreparedStatement ps = conn
						.prepareStatement("DELETE FROM HCPAssignedToWard WHERE Ward = ? and hcp = ?")) {
			ps.setLong(1, wardID);
			ps.setLong(2, hcpID);
			boolean removed = ps.executeUpdate() == 1;
			return removed;
		} catch (SQLException e) {
			throw new DBException(e);
		}
	}

	/**
	 * Updates a particular wardroom's occupiedBy information. Returns the
	 * number of rows affected (should be 1)
	 * 
	 * @param wardRoom
	 *            The WardRoomBean to update.
	 * @return An int indicating the number of affected rows.
	 * @throws DBException
	 */
	public int updateWardRoomOccupant(WardRoomBean wardRoom) throws DBException {
		try (Connection conn = factory.getConnection();
				PreparedStatement ps = conn
						.prepareStatement("UPDATE wardRooms SET OccupiedBy=? " + "WHERE RoomID = ?")) {
			if (wardRoom.getOccupiedBy() == null) {
				ps.setNull(1, java.sql.Types.BIGINT);
			} else {
				ps.setLong(1, wardRoom.getOccupiedBy());
			}
			ps.setLong(2, wardRoom.getRoomID());
			int result = ps.executeUpdate();
			return result;
		} catch (SQLException e) {
			throw new DBException(e);
		}
	}

	/**
	 * Returns a list of all wards with the status specified that the hcp has
	 * access to
	 * 
	 * @param status
	 *            The Status to search on
	 * @param hcpID
	 *            The id of the HCP to get wards for
	 * @return A java.util.List of WardRoomBeans that the specified hcp has
	 *         access too.
	 * @throws DBException
	 */
	public List<WardRoomBean> getWardRoomsByStatus(String status, Long hcpID) throws DBException {
		try (Connection conn = factory.getConnection();
				PreparedStatement ps = conn.prepareStatement(
						"SELECT * FROM wardrooms wr inner join hcpassignedtoward hw where wr.status = ? and wr.inward = hw.ward and hw.hcp = ?")) {
			ps.setString(1, status);
			ps.setLong(2, hcpID);
			ResultSet rs = ps.executeQuery();
			List<WardRoomBean> wards = wardRoomLoader.loadList(rs);
			rs.close();
			return wards;
		} catch (SQLException e) {
			throw new DBException(e);
		}
	}

	/**
	 * Returns a WardRoom specified by the id
	 * 
	 * @param wardRoomID
	 *            The id of the ward room to get
	 * @return A WardRoomBean with the ID specified
	 * @throws DBException
	 */
	public WardRoomBean getWardRoom(String wardRoomID) throws DBException {
		try (Connection conn = factory.getConnection();
				PreparedStatement ps = conn.prepareStatement("SELECT * FROM wardrooms where RoomID = ?")) {
			ps.setString(1, wardRoomID);
			ResultSet rs = ps.executeQuery();
			WardRoomBean loaded = rs.next() ? wardRoomLoader.loadSingle(rs) : null;
			rs.close();
			return loaded;
		} catch (SQLException e) {
			throw new DBException(e);
		}
	}

	/**
	 * Returns a Ward specified by the id
	 * 
	 * @param wardID
	 *            The id of the ward to get
	 * @return A WardBean with the ID specified
	 * @throws DBException
	 */
	public WardBean getWard(String wardID) throws DBException {
		try (Connection conn = factory.getConnection();
				PreparedStatement ps = conn.prepareStatement("SELECT * FROM wards where wardid = ?")) {
			ps.setString(1, wardID);
			ResultSet rs = ps.executeQuery();
			WardBean ward = rs.next() ? wardLoader.loadSingle(rs) : null;
			rs.close();
			return ward;
		} catch (SQLException e) {
			throw new DBException(e);
		}
	}

	/**
	 * Returns the hospital that the specified ward room is located in
	 * 
	 * @param wardRoomID
	 *            The id of the ward room to get the hospital for
	 * @return The HospitalBean that the specified ward room is located in.
	 * @throws DBException
	 */
	public HospitalBean getHospitalByWard(String wardRoomID) throws DBException {
		try (Connection conn = factory.getConnection();
				PreparedStatement ps = conn.prepareStatement(
						"SELECT * FROM hospitals h inner join wards ward inner join wardrooms room where room.RoomID = ? and room.inward = ward.wardid and ward.inhospital = h.hospitalid")) {
			ps.setString(1, wardRoomID);
			ResultSet rs = ps.executeQuery();
			HospitalBean hospital = rs.next() ? hospitalLoader.loadSingle(rs) : null;
			rs.close();
			return hospital;
		} catch (SQLException e) {
			throw new DBException(e);
		}
	}

	/**
	 * Logs the checkout reason for a patient
	 * 
	 * @param mid
	 *            The mid of the Patient checking out
	 * @param reason
	 *            The reason the patient is being checked out.
	 * @return Whether 1 patient was inserted
	 * @throws ITrustException
	 */
	public boolean checkOutPatientReason(long mid, String reason) throws DBException {
		try (Connection conn = factory.getConnection();
				PreparedStatement ps = conn
						.prepareStatement("INSERT INTO WardRoomCheckout (PID, Reason) Values(?,?)")) {
			ps.setLong(1, mid);
			ps.setString(2, reason);
			boolean logged = ps.executeUpdate() == 1;
			return logged;
		} catch (SQLException e) {
			throw new DBException(e);
		}
	}

	/**
	 * Returns the hospital that the specified user is located in
	 * 
	 * @param pid
	 *            The id of the user to get the hospital for
	 * @return The HospitalBean that the specified patient is located in.
	 * @throws DBException
	 */
	public HospitalBean getHospitalByPatientID(long pid) throws DBException {
		try (Connection conn = factory.getConnection();
				PreparedStatement ps = conn.prepareStatement(
						"SELECT * FROM hospitals h inner join wards ward inner join wardrooms room where room.OccupiedBy = ? and room.inward = ward.wardid and ward.inhospital = h.hospitalid")) {
			ps.setLong(1, pid);
			ResultSet rs = ps.executeQuery();
			HospitalBean hospital = rs.next() ? hospitalLoader.loadSingle(rs) : null;
			rs.close();
			return hospital;
		} catch (SQLException e) {
			throw new DBException(e);
		}
	}
}
