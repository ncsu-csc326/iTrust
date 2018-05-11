package edu.ncsu.csc.itrust.model.old.dao.mysql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import edu.ncsu.csc.itrust.exception.DBException;
import edu.ncsu.csc.itrust.exception.ITrustException;
import edu.ncsu.csc.itrust.model.old.beans.HospitalBean;
import edu.ncsu.csc.itrust.model.old.beans.loaders.HospitalBeanLoader;
import edu.ncsu.csc.itrust.model.old.dao.DAOFactory;

/**
 * Used for managing hospitals
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
public class HospitalsDAO {
	
	private DAOFactory factory;
	private HospitalBeanLoader hospitalLoader = new HospitalBeanLoader();

	/**
	 * The typical constructor.
	 * 
	 * @param factory
	 *            The {@link DAOFactory} associated with this DAO, which is used
	 *            for obtaining SQL connections, etc.
	 */
	public HospitalsDAO(DAOFactory factory) {
		this.factory = factory;
	}

	/**
	 * Returns a list of all hospitals sorted alphabetically
	 * 
	 * @return A java.util.List of HospitalBeans.
	 * @throws DBException
	 */
	public List<HospitalBean> getAllHospitals() throws DBException {
		try (Connection conn = factory.getConnection();
				PreparedStatement stmt = conn.prepareStatement("SELECT * FROM hospitals ORDER BY HospitalName");
				ResultSet rs = stmt.executeQuery()) {
			List<HospitalBean> loadlist = hospitalLoader.loadList(rs);
			return loadlist;
		} catch (SQLException e) {
			throw new DBException(e);
		}
	}

	/**
	 * Returns a particular hospital given its ID
	 * 
	 * @param id
	 *            The String ID of the hospital.
	 * @return A HospitalBean representing this hospital.
	 * @throws DBException
	 */
	public HospitalBean getHospital(String id) throws DBException {
		try (Connection conn = factory.getConnection();
				PreparedStatement stmt = conn.prepareStatement("SELECT * FROM hospitals WHERE HospitalID = ?")) {
			stmt.setString(1, id);
			ResultSet rs = stmt.executeQuery();
			if (rs.next()) {
				HospitalBean loaded = hospitalLoader.loadSingle(rs);
				rs.close();
				return loaded;
			}
			rs.close();
			return null;
		} catch (SQLException e) {
			throw new DBException(e);
		}
	}

	/**
	 * Adds a hospital
	 * 
	 * @param hosp
	 *            The HospitalBean object to insert.
	 * @return A boolean indicating whether the insertion was successful.
	 * @throws DBException
	 * @throws ITrustException
	 */
	public boolean addHospital(HospitalBean hosp) throws DBException, ITrustException {
		try (Connection conn = factory.getConnection();
				PreparedStatement stmt = conn.prepareStatement("INSERT INTO hospitals (HospitalID, HospitalName, Address, City, State, Zip) "
						+ "VALUES (?,?,?,?,?,?)")) {
			stmt.setString(1, hosp.getHospitalID());
			stmt.setString(2, hosp.getHospitalName());
			stmt.setString(3, hosp.getHospitalAddress());
			stmt.setString(4, hosp.getHospitalCity());
			stmt.setString(5, hosp.getHospitalState());
			stmt.setString(6, hosp.getHospitalZip());
			return stmt.executeUpdate() == 1;
		} catch (SQLException e) {
			if (e.getErrorCode() == 1062) {
				throw new ITrustException("Error: Hospital already exists.");
			} else {
				throw new DBException(e);
			}
		}
	}

	/**
	 * Updates a particular hospital's description. Returns the number of rows
	 * affected (should be 1)
	 * 
	 * @param hosp
	 *            The HospitalBean to update.
	 * @return An int indicating the number of affected rows.
	 * @throws DBException
	 */
	public int updateHospital(HospitalBean hosp) throws DBException {
		try (Connection conn = factory.getConnection();
				PreparedStatement stmt = conn.prepareStatement(
						"UPDATE hospitals SET HospitalName=?,Address=?,City=?,State=?,Zip=?" + "WHERE HospitalID = ?");) {
			stmt.setString(1, hosp.getHospitalName());
			stmt.setString(2, hosp.getHospitalAddress());
			stmt.setString(3, hosp.getHospitalCity());
			stmt.setString(4, hosp.getHospitalState());
			stmt.setString(5, hosp.getHospitalZip());
			stmt.setString(6, hosp.getHospitalID());
			int rowsUpdated = stmt.executeUpdate();
			return rowsUpdated;
		} catch (SQLException e) {
			throw new DBException(e);
		}
	}

	/**
	 * Assign an HCP to a hospital. If they have already been assigned to that
	 * hospital, then an iTrustException is thrown.
	 * 
	 * @param hcpID
	 *            The HCP's MID to assign to the hospital.
	 * @param hospitalID
	 *            The ID of the hospital to assign them to.
	 * @return A boolean indicating whether the assignment was a success.
	 * @throws DBException
	 * @throws ITrustException
	 */
	public boolean assignHospital(long hcpID, String hospitalID) throws DBException, ITrustException {
		try (Connection conn = factory.getConnection();
				PreparedStatement stmt = conn.prepareStatement("INSERT INTO hcpassignedhos (HCPID, HosID) VALUES (?,?)");) {
			stmt.setLong(1, hcpID);
			stmt.setString(2, hospitalID);
			boolean successfullyAdded = stmt.executeUpdate() == 1;
			return successfullyAdded;
		} catch (SQLException e) {
			if (1062 == e.getErrorCode()) {
				throw new ITrustException("HCP " + hcpID + " already assigned to hospital " + hospitalID);
			} else {
				throw new DBException(e);
			}
		}
	}

	/**
	 * Unassigns an HCP to a hospital. Returns whether or not any changes were
	 * made
	 * 
	 * @param hcpID
	 *            The MID of the HCP to remove.
	 * @param hospitalID
	 *            The ID of the hospital being removed from.
	 * @return A boolean indicating success.
	 * @throws DBException
	 */
	public boolean removeHospitalAssignment(long hcpID, String hospitalID) throws DBException {
		try (Connection conn = factory.getConnection();
				PreparedStatement stmt = conn.prepareStatement("DELETE FROM hcpassignedhos WHERE HCPID = ? AND HosID = ?");) {
			stmt.setLong(1, hcpID);
			stmt.setString(2, hospitalID);
			boolean successfullyRemoved = stmt.executeUpdate() == 1;
			return successfullyRemoved;
		} catch (SQLException e) {
			throw new DBException(e);
		}
	}

	/**
	 * Removes all hospital assignments for a particular HCP. Returns the number
	 * of rows affected.
	 * 
	 * @param hcpID
	 *            The MID of the HCP.
	 * @return An int representing the number of hospital assignments removed.
	 * @throws DBException
	 */
	public int removeAllHospitalAssignmentsFrom(long hcpID) throws DBException {
		try (Connection conn = factory.getConnection();
				PreparedStatement stmt = conn.prepareStatement("DELETE FROM hcpassignedhos WHERE HCPID = ?");) {
			stmt.setLong(1, hcpID);
			int numRemoved = stmt.executeUpdate();
			return numRemoved;
		} catch (SQLException e) {
			throw new DBException(e);
		}
	}

	/**
	 * Checks to see if the LT has an assigned hospital
	 * 
	 * @param hcpID
	 *            The MID of the LT.
	 * @return true If the LT has an assigned hospital to them, false if not
	 * @throws DBException
	 */
	public boolean checkLTHasHospital(long hcpID) throws DBException {
		try (Connection conn = factory.getConnection();
				PreparedStatement stmt = conn.prepareStatement("SELECT * FROM hcpassignedhos WHERE HCPID = ?")) {
			stmt.setLong(1, hcpID);
			ResultSet rs = stmt.executeQuery();
			boolean ltHasAssignedHospitals = rs.next();
			rs.close();
			return ltHasAssignedHospitals;
		} catch (SQLException e) {
			throw new DBException(e);
		}
	}

	public List<HospitalBean> getHospitalsAssignedToPhysician(long pid) throws DBException {
		List<String> hospitalIDs = new ArrayList<String>();
		List<HospitalBean> hospitalNames = new ArrayList<HospitalBean>();
		HospitalBeanLoader loader = new HospitalBeanLoader();

		try (Connection conn = factory.getConnection();
				PreparedStatement hcpAssignedHosStmt = conn.prepareStatement("SELECT hosID FROM hcpassignedhos WHERE HCPID=?");
				PreparedStatement hospitalsStmt = conn.prepareStatement("SELECT * FROM hospitals WHERE hospitals.HospitalID=?")) {
			hcpAssignedHosStmt.setString(1, String.valueOf(pid));
			ResultSet rs = hcpAssignedHosStmt.executeQuery();
			while (rs.next()) {
				hospitalIDs.add(rs.getString("hosID"));
			}
			if (hospitalIDs.size() == 0) {
				return null;
			}
			for (String hID : hospitalIDs) {
				hospitalsStmt.setString(1, String.valueOf(hID));
				rs = hospitalsStmt.executeQuery();
				while (rs.next()) {
					hospitalNames.add(loader.loadSingle(rs));
				}
			}
			rs.close();
		} catch (SQLException e) {
			throw new DBException(e);
		}
		return hospitalNames;
	}
}
