package edu.ncsu.csc.itrust.model.old.dao.mysql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Vector;

import edu.ncsu.csc.itrust.DBUtil;
import edu.ncsu.csc.itrust.exception.DBException;
import edu.ncsu.csc.itrust.exception.ITrustException;
import edu.ncsu.csc.itrust.model.old.beans.PatientBean;
import edu.ncsu.csc.itrust.model.old.beans.PersonnelBean;
import edu.ncsu.csc.itrust.model.old.beans.loaders.PatientLoader;
import edu.ncsu.csc.itrust.model.old.beans.loaders.PersonnelLoader;
import edu.ncsu.csc.itrust.model.old.dao.DAOFactory;

/**
 * Used for managing all static information related to a patient. For other
 * information related to all aspects of patient care, see the other DAOs.
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
public class PatientDAO {
	private DAOFactory factory;
	private PatientLoader patientLoader;
	private PersonnelLoader personnelLoader;

	/**
	 * The typical constructor.
	 * 
	 * @param factory
	 *            The {@link DAOFactory} associated with this DAO, which is used
	 *            for obtaining SQL connections, etc.
	 */
	public PatientDAO(DAOFactory factory) {
		this.factory = factory;
		this.patientLoader = new PatientLoader();
		this.personnelLoader = new PersonnelLoader();
	}

	/**
	 * Returns the name for the given MID
	 * 
	 * @param mid
	 *            The MID of the patient in question.
	 * @return A String representing the patient's first name and last name.
	 * @throws ITrustException
	 * @throws DBException
	 */
	public String getName(long mid) throws ITrustException, DBException {
		try (Connection conn = factory.getConnection();
				PreparedStatement ps = conn.prepareStatement("SELECT firstName, lastName FROM patients WHERE MID=?")) {
			ps.setLong(1, mid);
			ResultSet rs;
			rs = ps.executeQuery();
			if (rs.next()) {
				String result = rs.getString("firstName") + " " + rs.getString("lastName");
				rs.close();
				return result;
			} else {
				rs.close();
				throw new ITrustException("User does not exist");
			}
		} catch (SQLException e) {
			throw new DBException(e);
		}
	}

	/**
	 * Returns the role of a particular patient - why is this in PatientDAO? It
	 * should be in AuthDAO
	 * 
	 * @param mid
	 *            The MID of the patient in question.
	 * @param role
	 *            A String representing the role of the patient.
	 * @return A String representing the patient's role.
	 * @throws ITrustException
	 * @throws DBException
	 */
	public String getRole(long mid, String role) throws ITrustException, DBException {
		try (Connection conn = factory.getConnection();
				PreparedStatement ps = conn.prepareStatement("SELECT role FROM users WHERE MID=? AND Role=?")) {
			ps.setLong(1, mid);
			ps.setString(2, role);
			ResultSet rs;
			rs = ps.executeQuery();
			if (rs.next()) {
				String result = rs.getString("role");
				rs.close();
				return result;
			} else {
				rs.close();
				throw new ITrustException("User does not exist with the designated role");
			}
		} catch (SQLException e) {
			throw new DBException(e);
		}
	}

	/**
	 * Adds an empty patient to the table, returns the new MID
	 * 
	 * @return The MID of the patient as a long.
	 * @throws DBException
	 */
	public long addEmptyPatient() throws DBException {
		try (Connection conn = factory.getConnection();
				PreparedStatement ps = conn.prepareStatement("INSERT INTO patients(MID) VALUES(NULL)")) {
			ps.executeUpdate();
			return DBUtil.getLastInsert(conn);
		} catch (SQLException e) {
			throw new DBException(e);
		}
	}

	/**
	 * Returns the patient's information for a given ID
	 * 
	 * @param mid
	 *            The MID of the patient to retrieve.
	 * @return A PatientBean representing the patient.
	 * @throws DBException
	 */
	public PatientBean getPatient(long mid) throws DBException {
		try (Connection conn = factory.getConnection();
				PreparedStatement ps = conn.prepareStatement("SELECT * FROM patients WHERE MID = ?")) {
			ps.setLong(1, mid);
			ResultSet rs = ps.executeQuery();
			PatientBean patient = rs.next() ? patientLoader.loadSingle(rs) : null;
			rs.close();
			return patient;
		} catch (SQLException e) {
			throw new DBException(e);
		}
	}

	/**
	 * Updates a patient's information for the given MID
	 * 
	 * @param p
	 *            The patient bean representing the new information for the
	 *            patient.
	 * @throws DBException
	 */
	public void editPatient(PatientBean p, long hcpid) throws DBException {
		try (Connection conn = factory.getConnection();
				PreparedStatement ps = patientLoader
						.loadParameters(conn.prepareStatement("UPDATE patients SET firstName=?,lastName=?,email=?,"
								+ "address1=?,address2=?,city=?,state=?,zip=?,phone=?,"
								+ "eName=?,ePhone=?,iCName=?,iCAddress1=?,iCAddress2=?,iCCity=?,"
								+ "ICState=?,iCZip=?,iCPhone=?,iCID=?,DateOfBirth=?,"
								+ "DateOfDeath=?,CauseOfDeath=?,MotherMID=?,FatherMID=?,"
								+ "BloodType=?,Ethnicity=?,Gender=?,TopicalNotes=?, CreditCardType=?, CreditCardNumber=?, "
								+ "DirectionsToHome=?, Religion=?, Language=?, SpiritualPractices=?, "
								+ "AlternateName=?, DateOfDeactivation=? WHERE MID=?"), p)) {
			ps.setLong(37, p.getMID());
			ps.executeUpdate();
		} catch (SQLException e) {
			throw new DBException(e);
		}
	}

	/**
	 * Returns whether or not the patient exists
	 * 
	 * @param pid
	 *            The MID of the patient in question.
	 * @return A boolean indicating whether the patient exists.
	 * @throws DBException
	 */
	public boolean checkPatientExists(long pid) throws DBException {
		try (Connection conn = factory.getConnection();
				PreparedStatement ps = conn.prepareStatement("SELECT * FROM patients WHERE MID=?")) {
			ps.setLong(1, pid);
			ResultSet rs = ps.executeQuery();
			boolean exists = rs.next();
			rs.close();
			return exists;
		} catch (SQLException e) {
			throw new DBException(e);
		}
	}

	/**
	 * Returns a list of HCPs who are declared by the given patient
	 * 
	 * @param pid
	 *            The MID of the patient in question.
	 * @return A java.util.List of Personnel Beans.
	 * @throws DBException
	 */
	public List<PersonnelBean> getDeclaredHCPs(long pid) throws DBException {
		if (pid == 0L) {
			throw new DBException(new SQLException("pid cannot be 0"));
		}
		try (Connection conn = factory.getConnection();
				PreparedStatement ps = conn.prepareStatement("SELECT * FROM declaredhcp, personnel "
						+ "WHERE PatientID=? AND personnel.MID=declaredhcp.HCPID")) {
			ps.setLong(1, pid);
			ResultSet rs = ps.executeQuery();
			List<PersonnelBean> loadlist = personnelLoader.loadList(rs);
			rs.close();
			return loadlist;
		} catch (SQLException e) {
			throw new DBException(e);
		}
	}

	/**
	 * Declares an HCP for a particular patient
	 * 
	 * @param pid
	 *            The MID of the patient in question.
	 * @param hcpID
	 *            The HCP's MID.
	 * @return A boolean as to whether the insertion was successful.
	 * @throws DBException
	 * @throws ITrustException
	 */
	public boolean declareHCP(long pid, long hcpID) throws DBException, ITrustException {
		try (Connection conn = factory.getConnection();
				PreparedStatement ps = conn.prepareStatement("INSERT INTO declaredhcp(PatientID, HCPID) VALUES(?,?)")) {
			ps.setLong(1, pid);
			ps.setLong(2, hcpID);
			boolean successfullyAdded = ps.executeUpdate() == 1;
			return successfullyAdded;
		} catch (SQLException e) {
			if (e.getErrorCode() == 1062) {
				throw new ITrustException("HCP " + hcpID + " has already been declared for patient " + pid);
			} else {
				throw new DBException(e);
			}
		}
	}

	/**
	 * Undeclare an HCP for a given patient
	 * 
	 * @param pid
	 *            The MID of the patient in question.
	 * @param hcpID
	 *            The MID of the HCP in question.
	 * @return A boolean indicating whether the action was successful.
	 * @throws DBException
	 */
	public boolean undeclareHCP(long pid, long hcpID) throws DBException {
		try (Connection conn = factory.getConnection();
				PreparedStatement ps = conn.prepareStatement("DELETE FROM declaredhcp WHERE PatientID=? AND HCPID=?")) {
			ps.setLong(1, pid);
			ps.setLong(2, hcpID);
			boolean successfullyDeleted = ps.executeUpdate() == 1;
			return successfullyDeleted;
		} catch (SQLException e) {
			throw new DBException(e);
		}
	}

	/**
	 * Check if a patient has declared the given HCP
	 * 
	 * @param pid
	 *            The MID of the patient in question as a long.
	 * @param hcpid
	 *            The MID of the HCP in question as a long.
	 * @return
	 * @throws DBException
	 */
	public boolean checkDeclaredHCP(long pid, long hcpid) throws DBException {
		try (Connection conn = factory.getConnection();
				PreparedStatement ps = conn
						.prepareStatement("SELECT * FROM declaredhcp WHERE PatientID=? AND HCPID=?")) {
			ps.setLong(1, pid);
			ps.setLong(2, hcpid);
			boolean patientHasDeclaredHCP = (ps.executeQuery().next());
			return patientHasDeclaredHCP;
		} catch (SQLException e) {
			throw new DBException(e);
		}
	}

	/**
	 * Return a list of patients that the given patient represents
	 * 
	 * @param pid
	 *            The MID of the patient in question.
	 * @return A java.util.List of PatientBeans
	 * @throws DBException
	 */
	public List<PatientBean> getRepresented(long pid) throws DBException {
		try (Connection conn = factory.getConnection();
				PreparedStatement ps = conn.prepareStatement("SELECT patients.* FROM representatives, patients "
						+ "WHERE RepresenterMID=? AND RepresenteeMID=patients.MID")) {
			ps.setLong(1, pid);
			ResultSet rs = ps.executeQuery();
			List<PatientBean> loadlist = patientLoader.loadList(rs);
			rs.close();
			return loadlist;
		} catch (SQLException e) {
			throw new DBException(e);
		}
	}

	/**
	 * Return a list of patients that the given patient represents
	 * 
	 * @param pid
	 *            The MID of the patient in question.
	 * @return A java.util.List of PatientBeans
	 * @throws DBException
	 */
	public List<PatientBean> getDependents(long pid) throws DBException {
		try (Connection conn = factory.getConnection();
				PreparedStatement ps = conn.prepareStatement("SELECT patients.* FROM representatives, patients, users "
						+ "WHERE RepresenterMID=? AND RepresenteeMID=patients.MID AND users.MID=patients.MID AND users.isDependent=1")) {
			ps.setLong(1, pid);
			ResultSet rs = ps.executeQuery();
			List<PatientBean> dependentsList = patientLoader.loadList(rs);
			rs.close();
			return dependentsList;
		} catch (SQLException e) {
			throw new DBException(e);
		}
	}

	/**
	 * Return a list of patients that the given patient is represented by
	 * 
	 * @param pid
	 *            The MID of the patient in question.
	 * @return A java.util.List of PatientBeans.
	 * @throws DBException
	 */
	public List<PatientBean> getRepresenting(long pid) throws DBException {
		try (Connection conn = factory.getConnection();
				PreparedStatement ps = conn.prepareStatement("SELECT patients.* FROM representatives, patients "
						+ "WHERE RepresenteeMID=? AND RepresenterMID=patients.MID")) {
			ps.setLong(1, pid);
			ResultSet rs = ps.executeQuery();
			List<PatientBean> representingList = patientLoader.loadList(rs);
			rs.close();
			return representingList;
		} catch (SQLException e) {
			throw new DBException(e);
		}
	}

	/**
	 * Check if the given representer represents the representee
	 * 
	 * @param representer
	 *            The MID of the representer in question.
	 * @param representee
	 *            The MID of the representee in question.
	 * @return A boolean indicating whether represenation is in place.
	 * @throws DBException
	 */
	public boolean represents(long representer, long representee) throws DBException {
		try (Connection conn = factory.getConnection();
				PreparedStatement ps = conn.prepareStatement(
						"SELECT * FROM representatives WHERE RepresenterMID=? AND RepresenteeMID=?")) {
			ps.setLong(1, representer);
			ps.setLong(2, representee);
			ResultSet rs = ps.executeQuery();
			boolean doesRepresent = rs.next();
			rs.close();
			return doesRepresent;
		} catch (SQLException e) {
			throw new DBException(e);
		}
	}

	/**
	 * Assign a representer to the representee
	 * 
	 * @param representer
	 *            The MID of the representer as a long.
	 * @param representee
	 *            The MID of the representee as a long.
	 * @return A boolean as to whether the insertion was correct.
	 * @throws DBException
	 * @throws ITrustException
	 */
	public boolean addRepresentative(long representer, long representee) throws DBException, ITrustException {
		try (Connection conn = factory.getConnection();
				PreparedStatement ps = conn
						.prepareStatement("INSERT INTO representatives(RepresenterMID,RepresenteeMID) VALUES (?,?)")) {
			ps.setLong(1, representer);
			ps.setLong(2, representee);

			boolean successfullyAdded = ps.executeUpdate() == 1;
			return successfullyAdded;
		} catch (SQLException e) {
			if (e.getErrorCode() == 1062) {
				throw new ITrustException("Patient " + representer + " already represents patient " + representee);
			} else {
				throw new DBException(e);
			}
		}
	}

	public boolean checkIfRepresenteeIsActive(long representee) throws DBException {
		if (representee == 0L) {
			throw new DBException(new SQLException("PID cannot be 0"));
		}
		try (Connection conn = factory.getConnection();
				PreparedStatement ps = conn
						.prepareStatement("SELECT * FROM patients WHERE MID=? AND DateOfDeactivation IS NULL")) {
			ps.setLong(1, representee);
			ResultSet rs = ps.executeQuery();
			PatientBean bean = new PatientBean();
			if (rs.next())
				bean = patientLoader.loadSingle(rs);
			rs.close();
			return bean.getMID() == representee;
		} catch (SQLException e) {
			throw new DBException(e);
		}
	}

	public boolean checkIfPatientIsActive(long pid) throws ITrustException {
		if (pid == 0L)
			throw new DBException(new SQLException("PID cannot be 0"));
		try (Connection conn = factory.getConnection();
				PreparedStatement ps = conn
						.prepareStatement("SELECT * FROM patients WHERE MID=? AND DateOfDeactivation IS NULL")) {
			ps.setLong(1, pid);
			ResultSet rs = ps.executeQuery();
			PatientBean bean = rs.next() ? patientLoader.loadSingle(rs) : new PatientBean();
			rs.close();
			return bean.getMID() == pid;
		} catch (SQLException e) {
			throw new DBException(e);
		}
	}

	/**
	 * Unassign the representation
	 * 
	 * @param representer
	 *            The MID of the representer in question.
	 * @param representee
	 *            The MID of the representee in question.
	 * @return A boolean indicating whether the unassignment was sucessful.
	 * @throws DBException
	 */
	public boolean removeRepresentative(long representer, long representee) throws DBException {
		try (Connection conn = factory.getConnection();
				PreparedStatement ps = conn
						.prepareStatement("DELETE FROM representatives WHERE RepresenterMID=? AND RepresenteeMID=?")) {
			ps.setLong(1, representer);
			ps.setLong(2, representee);
			boolean successfullyDeleted = ps.executeUpdate() == 1;
			return successfullyDeleted;
		} catch (SQLException e) {
			throw new DBException(e);
		}
	}

	/**
	 * Removes all dependencies represented by the patient passed in the
	 * parameter
	 * 
	 * @param representerMID
	 *            the mid for the patient to remove all representees for
	 * @throws DBException
	 */
	public void removeAllRepresented(long representerMID) throws DBException {
		try (Connection conn = factory.getConnection();
				PreparedStatement updateStatement = conn.prepareStatement(
						"UPDATE users U, representatives R SET U.isDependent=0 WHERE R.representerMID=? AND "
								+ "R.representeeMID=U.MID AND R.representeeMID NOT IN "
								+ "(SELECT representeeMID FROM representatives WHERE representerMID<>?)");
				PreparedStatement deleteStatement = conn.prepareStatement("DELETE FROM representatives WHERE representerMID=?")) {
			updateStatement.setLong(1, representerMID);
			updateStatement.setLong(2, representerMID);
			updateStatement.executeUpdate();
			
			deleteStatement.setLong(1, representerMID);
			deleteStatement.executeUpdate();
		} catch (SQLException e) {
			throw new DBException(e);
		}
	}

	/**
	 * Removes all dependencies participated by the patient passed in the
	 * parameter
	 * 
	 * @param representerMID
	 *            the mid for the patient to remove all representees for
	 * @throws DBException
	 */
	public void removeAllRepresentee(long representeeMID) throws DBException {
		try (Connection conn = factory.getConnection();
				PreparedStatement deleteStatement = conn.prepareStatement("DELETE FROM representatives WHERE representeeMID=?");
				PreparedStatement updateStatement = conn.prepareStatement("UPDATE users SET isDependent=0 WHERE MID=?")) {
			deleteStatement.setLong(1, representeeMID);
			deleteStatement.executeUpdate();

			updateStatement.setLong(1, representeeMID);
			updateStatement.executeUpdate();
		} catch (SQLException e) {
			throw new DBException(e);
		}
	}

	/**
	 * Lists every patient in the database.
	 * 
	 * @return A java.util.List of PatientBeans representing the patients.
	 * @throws DBException
	 */
	public List<PatientBean> getAllPatients() throws DBException {
		try (Connection conn = factory.getConnection();
				PreparedStatement ps = conn.prepareStatement("SELECT * FROM patients ");
				ResultSet rs = ps.executeQuery()) {
			return patientLoader.loadList(rs);
		} catch (SQLException e) {
			throw new DBException(e);
		}
	}

	/**
	 * Return a list of patients with a special-diagnosis-history who have the
	 * logged in HCP as a DHCP and whose medications are going to expire within
	 * seven days.
	 * 
	 * @param hcpMID
	 *            The MID of the logged in HCP
	 * @return A list of patients satisfying the conditions.
	 * @throws DBException
	 */
	public List<PatientBean> getRenewalNeedsPatients(long hcpMID) throws DBException {
		try (Connection conn = factory.getConnection();
				PreparedStatement ps = conn.prepareStatement("SELECT * FROM ( " +
					"SELECT DISTINCT patients.* FROM patients, declaredhcp, ovdiagnosis, officevisits, ovmedication"
					+ " WHERE"
					+ " declaredhcp.HCPID = ?"
					+ " AND patients.MID = declaredhcp.PatientID"
					+ " AND (ovdiagnosis.VisitID = officevisits.ID"
					+ " AND officevisits.PatientID = declaredhcp.PatientID"
					+ " AND ((ovdiagnosis.ICDCode >= ? AND ovdiagnosis.ICDCode < ?)"
					+ " OR (ovdiagnosis.ICDCode >= ? AND ovdiagnosis.ICDCode < ?)"
					+ " OR (ovdiagnosis.ICDCode >= ? AND ovdiagnosis.ICDCode < ?)))"
					+ " UNION ALL"
					+ " SELECT DISTINCT patients.* From patients, declaredhcp, ovdiagnosis, officevisits, ovmedication "
					+ " WHERE"
					+ " declaredhcp.HCPID = ?"
					+ " AND patients.MID = declaredhcp.PatientID"
					+ " AND (declaredhcp.PatientID = officevisits.PatientID"
					+ " AND officevisits.ID = ovmedication.VisitID"
					+ " AND ovmedication.EndDate BETWEEN CURDATE() AND DATE_ADD(CURDATE(), INTERVAL 7 DAY)))"
					+ " AS final"
					+ " GROUP BY final.MID HAVING COUNT(*) = 2"
					+ " ORDER BY final.lastname ASC, final.firstname ASC")) {
			ps.setLong(1, hcpMID);

			ps.setFloat(2, 250.0f);
			ps.setFloat(3, 251.0f);

			ps.setFloat(4, 493.0f);
			ps.setFloat(5, 494.0f);

			ps.setFloat(6, 390.0f);
			ps.setFloat(7, 460.99f);

			ps.setLong(8, hcpMID);

			ResultSet rs = ps.executeQuery();
			List<PatientBean> loadlist = patientLoader.loadList(rs);
			rs.close();
			return loadlist;
		} catch (SQLException e) {
			throw new DBException(e);
		}
	}

	/**
	 * Returns all patients with names "LIKE" (as in SQL) the passed in
	 * parameters.
	 * 
	 * @param first
	 *            The patient's first name.
	 * @param last
	 *            The patient's last name.
	 * @return A java.util.List of PatientBeans.
	 * @throws DBException
	 */
	public List<PatientBean> searchForPatientsWithName(String first, String last) throws DBException {
		if (first.equals("%") && last.equals("%")) {
			return new Vector<PatientBean>();
		}
		
		try (Connection conn = factory.getConnection();
				PreparedStatement ps = conn.prepareStatement("SELECT * FROM patients WHERE firstName LIKE ? AND lastName LIKE ?")) {
			ps.setString(1, first);
			ps.setString(2, last);
			ResultSet rs = ps.executeQuery();
			List<PatientBean> patientsList = patientLoader.loadList(rs);
			rs.close();
			return patientsList;
		} catch (SQLException e) {
			throw new DBException(e);
		}
	}

	/**
	 * Returns all patients with names "LIKE" with wildcards (as in SQL) the
	 * passed in parameters.
	 * 
	 * @param first
	 *            The patient's first name.
	 * @param last
	 *            The patient's last name.
	 * @return A java.util.List of PatientBeans.
	 * @throws DBException
	 */
	public List<PatientBean> fuzzySearchForPatientsWithName(String first, String last) throws DBException {
		if (first.equals("%") && last.equals("%")) {
			return new Vector<PatientBean>();
		}

		try (Connection conn = factory.getConnection();
				PreparedStatement ps = conn
						.prepareStatement("SELECT * FROM patients WHERE firstName LIKE ? AND lastName LIKE ?")) {
			ps.setString(1, "%" + first + "%");
			ps.setString(2, "%" + last + "%");

			ResultSet rs = ps.executeQuery();
			List<PatientBean> loadlist = patientLoader.loadList(rs);
			rs.close();
			return loadlist;
		} catch (SQLException e) {
			throw new DBException(e);
		}
	}

	/**
	 * Returns all patients with the given MID as a substring in their MID
	 * 
	 * @param MID
	 *            the patients MID
	 * @return list of patients with that MID as a substring
	 * @throws DBException
	 */
	public List<PatientBean> fuzzySearchForPatientsWithMID(long MID) throws DBException {
		try (Connection conn = factory.getConnection();
				PreparedStatement ps = conn.prepareStatement("SELECT * FROM patients WHERE MID LIKE ? ORDER BY MID")) {
			ps.setString(1, "%" + MID + "%");

			ResultSet rs = ps.executeQuery();
			List<PatientBean> loadlist = patientLoader.loadList(rs);
			rs.close();
			return loadlist;
		} catch (SQLException e) {
			throw new DBException(e);
		}
	}

	/**
	 * Allows a patient to add a designated nutritionist. Only the designated
	 * nutritionist will be able to view the patient's nutritional information.
	 */
	public int addDesignatedNutritionist(long patientMID, long HCPID) throws DBException {
		try (Connection conn = factory.getConnection();
				PreparedStatement ps = conn.prepareStatement("INSERT INTO " + "designatedNutritionist(PatientID, HCPID) VALUES(?,?);")) {
			ps.setLong(1, patientMID);
			ps.setLong(2, HCPID);
			int updated = ps.executeUpdate();
			return updated;
		} catch (SQLException e) {
			throw new DBException(e);
		}
	}

	/**
	 * Returns the ID of the designated nutritionist for the patient returns -1
	 * if the patient does not have a designated nutritionist
	 */
	public long getDesignatedNutritionist(long patientMID) throws DBException {
		try (Connection conn = factory.getConnection();
				PreparedStatement ps = conn.prepareStatement("SELECT HCPID FROM " + "designatedNutritionist WHERE PatientID = ?;")) {
			ps.setLong(1, patientMID);
			ResultSet results = ps.executeQuery();
			long nutritionistMID = results.next() ? results.getLong(1) : -1;
			results.close();
			return nutritionistMID;
		} catch (SQLException e) {
			throw new DBException(e);
		}
	}

	/**
	 * Updates the designated nutritionist for this patient. Assumes that the
	 * patient already has a designated nutritionist.
	 */
	public int updateDesignatedNutritionist(long patientMID, long HCPID) throws DBException {
		try (Connection conn = factory.getConnection();
				PreparedStatement ps = conn.prepareStatement("UPDATE designatedNutritionist " + "SET HCPID = ? WHERE PatientID = ?;")) {
			ps.setLong(1, HCPID);
			ps.setLong(2, patientMID);
			int numUpdated = ps.executeUpdate();
			return numUpdated;
		} catch (SQLException e) {
			throw new DBException(e);
		}
	}

	/**
	 * Deletes the designated nutritionist for this patient. Assumes that the
	 * patient already has a designated nutritionist
	 */
	public int deleteDesignatedNutritionist(long patientMID) throws DBException {
		try (Connection conn = factory.getConnection();
				PreparedStatement ps = conn.prepareStatement("DELETE FROM designatedNutritionist " + "WHERE PatientID = ?;")) {
			ps.setLong(1, patientMID);
			int numDeleted = ps.executeUpdate();
			return numDeleted;
		} catch (SQLException e) {
			throw new DBException(e);
		}
	}
}
