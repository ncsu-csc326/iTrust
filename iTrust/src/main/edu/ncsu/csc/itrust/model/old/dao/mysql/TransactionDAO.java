package edu.ncsu.csc.itrust.model.old.dao.mysql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

import edu.ncsu.csc.itrust.exception.DBException;
import edu.ncsu.csc.itrust.logger.TransactionLogger;
import edu.ncsu.csc.itrust.model.old.beans.OperationalProfile;
import edu.ncsu.csc.itrust.model.old.beans.TransactionBean;
import edu.ncsu.csc.itrust.model.old.beans.loaders.OperationalProfileLoader;
import edu.ncsu.csc.itrust.model.old.beans.loaders.TransactionBeanLoader;
import edu.ncsu.csc.itrust.model.old.dao.DAOFactory;
import edu.ncsu.csc.itrust.model.old.enums.TransactionType;

/**
 * Used for the logging mechanism.
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
public class TransactionDAO {

	private DAOFactory factory;
	private TransactionBeanLoader loader = new TransactionBeanLoader();
	private OperationalProfileLoader operationalProfileLoader = new OperationalProfileLoader();

	/**
	 * The typical constructor.
	 * 
	 * @param factory
	 *            The {@link DAOFactory} associated with this DAO, which is used
	 *            for obtaining SQL connections, etc.
	 */
	public TransactionDAO(DAOFactory factory) {
		this.factory = factory;
	}

	/**
	 * Returns the whole transaction log
	 * 
	 * @return
	 * @throws DBException
	 */
	public List<TransactionBean> getAllTransactions() throws DBException {
		try (Connection conn = factory.getConnection();
				PreparedStatement ps = conn.prepareStatement("SELECT * FROM transactionlog ORDER BY timeLogged DESC");
				ResultSet rs = ps.executeQuery()) {
			List<TransactionBean> loadlist = loader.loadList(rs);
			return loadlist;
		} catch (SQLException e) {
			throw new DBException(e);
		}
	}

	/**
	 * Log a transaction, with all of the info. The meaning of secondaryMID and
	 * addedInfo changes depending on the transaction type.
	 * 
	 * @param type
	 *            The {@link TransactionType} enum representing the type this
	 *            transaction is.
	 * @param loggedInMID
	 *            The MID of the user who is logged in.
	 * @param secondaryMID
	 *            Typically, the MID of the user who is being acted upon.
	 * @param addedInfo
	 *            A note about a subtransaction, or specifics of this
	 *            transaction (for posterity).
	 * @throws DBException
	 */
	public void logTransaction(TransactionType type, Long loggedInMID, Long secondaryMID, String addedInfo)
			throws DBException {

		// Use 0 as the default secondaryMID
		if (secondaryMID == null) {
			secondaryMID = 0L;
		}

		try (Connection conn = factory.getConnection();
				PreparedStatement ps = conn.prepareStatement("INSERT INTO transactionlog(loggedInMID, secondaryMID, "
						+ "transactionCode, addedInfo) VALUES(?,?,?,?)")) {
			ps.setLong(1, loggedInMID);
			ps.setLong(2, secondaryMID);
			ps.setInt(3, type.getCode());
			ps.setString(4, addedInfo);
			ps.executeUpdate();
		} catch (SQLException e) {
			throw new DBException(e);
		}
	}

	/**
	 * Return a list of all transactions in which an HCP accessed the given
	 * patient's record
	 * 
	 * @param patientID
	 *            The MID of the patient in question.
	 * @return A java.util.List of transactions.
	 * @throws DBException
	 */
	public List<TransactionBean> getAllRecordAccesses(long patientID, long dlhcpID, boolean getByRole)
			throws DBException {
		try (Connection conn = factory.getConnection();
				PreparedStatement ps = conn
						.prepareStatement("SELECT * FROM transactionlog WHERE secondaryMID=? AND transactionCode "
								+ "IN(" + TransactionType.patientViewableStr
								+ ") AND loggedInMID!=? ORDER BY timeLogged DESC")) {
			ps.setLong(1, patientID);
			ps.setLong(2, dlhcpID);
			ResultSet rs = ps.executeQuery();
			List<TransactionBean> transactionList = loader.loadList(rs);

			transactionList = addAndSortRoles(transactionList, patientID, getByRole);

			rs.close();
			return transactionList;
		} catch (SQLException e) {
			throw new DBException(e);
		}
	}

	/**
	 * The Most Thorough Fetch
	 * 
	 * @param mid
	 *            MID of the logged in user
	 * @param dlhcpID
	 *            MID of the user's DLHCP
	 * @param start
	 *            Index to start pulling entries from
	 * @param range
	 *            Number of entries to retrieve
	 * @return List of <range> TransactionBeans affecting the user starting from
	 *         the <start>th entry
	 * @throws DBException
	 */
	public List<TransactionBean> getTransactionsAffecting(long mid, long dlhcpID, java.util.Date start, int range)
			throws DBException {
		try (Connection conn = factory.getConnection();
				PreparedStatement ps = conn.prepareStatement("SELECT * FROM transactionlog WHERE ((timeLogged <= ?) "
						+ "AND  (secondaryMID=? AND transactionCode " + "IN (" + TransactionType.patientViewableStr
						+ ")) " + "OR (loggedInMID=? AND transactionCode=?) ) "
						+ "AND NOT (loggedInMID=? AND transactionCode IN (" + // exclude
																				// if
																				// DLHCP
																				// as
																				// specified
																				// in
																				// UC43
						TransactionType.dlhcpHiddenStr + ")) " + "ORDER BY timeLogged DESC LIMIT 0,?")) {
			ps.setString(2, mid + "");
			ps.setString(3, mid + "");
			ps.setInt(4, TransactionType.LOGIN_SUCCESS.getCode());
			ps.setTimestamp(1, new Timestamp(start.getTime()));
			ps.setLong(5, dlhcpID);
			ps.setInt(6, range);
			ResultSet rs = ps.executeQuery();
			List<TransactionBean> transactionList = loader.loadList(rs);
			rs.close();
			return transactionList;
		} catch (SQLException e) {
			throw new DBException(e);
		}
	}

	/**
	 * Return a list of all transactions in which an HCP accessed the given
	 * patient's record, within the dates
	 * 
	 * @param patientID
	 *            The MID of the patient in question.
	 * @param lower
	 *            The starting date as a java.util.Date
	 * @param upper
	 *            The ending date as a java.util.Date
	 * @return A java.util.List of transactions.
	 * @throws DBException
	 */
	public List<TransactionBean> getRecordAccesses(long patientID, long dlhcpID, java.util.Date lower,
			java.util.Date upper, boolean getByRole) throws DBException {
		try (Connection conn = factory.getConnection();
				PreparedStatement ps = conn
						.prepareStatement("SELECT * FROM transactionlog WHERE secondaryMID=? AND transactionCode IN ("
								+ TransactionType.patientViewableStr + ") " + "AND timeLogged >= ? AND timeLogged <= ? "
								+ "AND loggedInMID!=? " + "ORDER BY timeLogged DESC")) {
			ps.setLong(1, patientID);
			ps.setTimestamp(2, new Timestamp(lower.getTime()));
			// add 1 day's worth to include the upper
			ps.setTimestamp(3, new Timestamp(upper.getTime() + 1000L * 60L * 60 * 24L));
			ps.setLong(4, dlhcpID);
			ResultSet rs = ps.executeQuery();
			List<TransactionBean> transactionList = loader.loadList(rs);

			transactionList = addAndSortRoles(transactionList, patientID, getByRole);
			rs.close();
			return transactionList;
		} catch (SQLException e) {
			throw new DBException(e);
		}
	}
	
	/**
	 * Returns the operation profile
	 * 
	 * @return The OperationalProfile as a bean.
	 * @throws DBException
	 */
	public OperationalProfile getOperationalProfile() throws DBException {
		return getOperationalProfile(0L);
	}

	/**
	 * Returns the operation profile
	 * 
	 * @return The OperationalProfile as a bean.
	 * @throws DBException
	 */
	public OperationalProfile getOperationalProfile(long loggedInMID) throws DBException {
		try (Connection conn = factory.getConnection();
				PreparedStatement ps = conn
						.prepareStatement("SELECT TransactionCode, count(transactionID) as TotalCount, "
								+ "count(if(loggedInMID<9000000000, transactionID, null)) as PatientCount, "
								+ "count(if(loggedInMID>=9000000000, transactionID, null)) as PersonnelCount "
								+ "FROM transactionlog GROUP BY transactionCode ORDER BY transactionCode ASC");
				ResultSet rs = ps.executeQuery()) {
			TransactionLogger.getInstance().logTransaction(TransactionType.OPERATIONAL_PROFILE_VIEW, loggedInMID, 0L, "");
			OperationalProfile result = operationalProfileLoader.loadSingle(rs);
			return result;
		} catch (SQLException e) {
			throw new DBException(e);
		}
	}

	/**
	 * 
	 * @param transactionList
	 * @param patientID
	 * @param sortByRole
	 * @return
	 * @throws DBException
	 */
	private List<TransactionBean> addAndSortRoles(List<TransactionBean> transactionList, long patientID,
			boolean sortByRole) throws DBException {
		for (TransactionBean t : transactionList) {
			String rawRole = getRoleStringForMID(t.getLoggedInMID());
			String formattedRole;
			switch (rawRole) {
			case "er":
				formattedRole = "Emergency Responder";
				break;
			case "uap":
				formattedRole = "UAP";
				break;
			case "hcp":
				formattedRole = getHCPRole(patientID, t.getLoggedInMID());
				break;
			case "patient":
				formattedRole = getPatientRole(patientID, t.getLoggedInMID());
				break;
			default:
				formattedRole = "";
				break;
			}
			t.setRole(formattedRole);
		}

		if (sortByRole) {
			TransactionBean[] array = new TransactionBean[transactionList.size()];
			array[0] = transactionList.get(0);
			TransactionBean t;
			for (int i = 1; i < transactionList.size(); i++) {
				t = transactionList.get(i);
				String role = t.getRole();
				int j = 0;
				while (array[j] != null && role.compareToIgnoreCase(array[j].getRole()) >= 0)
					j++;
				for (int k = i; k > j; k--) {
					array[k] = array[k - 1];
				}
				array[j] = t;
			}
			int size = transactionList.size();
			for (int i = 0; i < size; i++)
				transactionList.set(i, array[i]);
		}

		return transactionList;
	}

	/**
	 * Returns the role for the given MID, or empty string if no user exists for
	 * the given MID.
	 */
	private String getRoleStringForMID(Long mid) throws DBException {
		try (Connection conn = factory.getConnection();
				PreparedStatement roleQuery = conn.prepareStatement("SELECT Role FROM users WHERE MID=?")) {
			roleQuery.setLong(1, mid);
			ResultSet rs = roleQuery.executeQuery();

			String role = rs.next() ? rs.getString("Role") : "";
			rs.close();
			return role;
		} catch (SQLException e) {
			throw new DBException(e);
		}
	}

	/**
	 * Returns the HCP role (LHCP or DLHCP) for the given patient and HCP MIDs.
	 */
	private String getHCPRole(Long patientID, Long hcpID) throws DBException {
		try (Connection conn = factory.getConnection();
				PreparedStatement ps = conn.prepareStatement("SELECT PatientID FROM declaredhcp WHERE HCPID=?")) {
			ps.setLong(1, hcpID);
			ResultSet rs = ps.executeQuery();
			String role = "LHCP";
			while (rs.next()) {
				if (rs.getLong("PatientID") == patientID) {
					role = "DLHCP";
					break;
				}
			}
			rs.close();
			return role;
		} catch (SQLException e) {
			throw new DBException(e);
		}
	}

	/**
	 * Returns the patient role (Patient or Personal Health Representative) for
	 * the given patient and HCP MIDs.
	 */
	private String getPatientRole(Long patientID, Long mid) throws DBException {
		try (Connection conn = factory.getConnection();
				PreparedStatement ps = conn
						.prepareStatement("SELECT representeeMID FROM representatives WHERE representerMID=?")) {
			ps.setLong(1, mid);
			ResultSet rs = ps.executeQuery();
			String role = "Patient";
			while (rs.next()) {
				if (rs.getLong("representeeMID") == patientID) {
					role = "Personal Health Representative";
					break;
				}
			}
			rs.close();
			return role;
		} catch (SQLException e) {
			throw new DBException(e);
		}
	}
}