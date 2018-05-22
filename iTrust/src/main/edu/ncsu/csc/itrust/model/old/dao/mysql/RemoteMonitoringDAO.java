package edu.ncsu.csc.itrust.model.old.dao.mysql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import edu.ncsu.csc.itrust.exception.DBException;
import edu.ncsu.csc.itrust.exception.ITrustException;
import edu.ncsu.csc.itrust.model.old.beans.PersonnelBean;
import edu.ncsu.csc.itrust.model.old.beans.RemoteMonitoringDataBean;
import edu.ncsu.csc.itrust.model.old.beans.TelemedicineBean;
import edu.ncsu.csc.itrust.model.old.beans.loaders.PersonnelLoader;
import edu.ncsu.csc.itrust.model.old.beans.loaders.RemoteMonitoringDataBeanLoader;
import edu.ncsu.csc.itrust.model.old.beans.loaders.RemoteMonitoringListsBeanLoader;
import edu.ncsu.csc.itrust.model.old.dao.DAOFactory;

/**
 * Used for the keeping track of remote monitoring data.
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
public class RemoteMonitoringDAO {

	private transient final DAOFactory factory;
	private transient final RemoteMonitoringDataBeanLoader loader = new RemoteMonitoringDataBeanLoader();
	private transient final RemoteMonitoringListsBeanLoader rmListLoader = new RemoteMonitoringListsBeanLoader();
	private transient final PersonnelLoader personnelLoader = new PersonnelLoader();

	/**
	 * The typical constructor.
	 * 
	 * @param factory
	 *            The {@link DAOFactory} associated with this DAO, which is used
	 *            for obtaining SQL connections, etc.
	 */
	public RemoteMonitoringDAO(final DAOFactory factory) {
		this.factory = factory;
	}

	/**
	 * Return remote monitoring list data for a given patient.
	 * 
	 * @param patientMID
	 *            Patient to retrieve data for.
	 * @return List of TelemedicineBeans
	 * @throws DBException
	 */
	public List<TelemedicineBean> getTelemedicineBean(final long patientMID) throws DBException {
		try (Connection conn = factory.getConnection();
				PreparedStatement stmt = conn
						.prepareStatement("SELECT * FROM remotemonitoringlists WHERE PatientMID=?")) {
			stmt.setLong(1, patientMID);
			final ResultSet results = stmt.executeQuery();

			final List<TelemedicineBean> telemedicine = rmListLoader.loadList(results);
			results.close();
			return telemedicine;
		} catch (SQLException e) {
			throw new DBException(e);
		}
	}

	/**
	 * Returns patient data for a given HCP
	 * 
	 * @param loggedInMID
	 *            loggedInMID
	 * @return dataList
	 * @throws DBException
	 */
	public List<RemoteMonitoringDataBean> getPatientsData(final long loggedInMID) throws DBException {
		try (Connection conn = factory.getConnection();
				PreparedStatement stmt = conn
						.prepareStatement("SELECT * FROM remotemonitoringlists WHERE HCPMID=? ORDER BY PatientMID");
				PreparedStatement pstmt1 = conn.prepareStatement(
						"SELECT * FROM remotemonitoringdata WHERE timelogged >= CURRENT_DATE ORDER BY PatientID, timeLogged DESC");
				final ResultSet dataRS = pstmt1.executeQuery()) {
			stmt.setLong(1, loggedInMID);
			final ResultSet patientRS = stmt.executeQuery();

			final List<String> patientList = new ArrayList<String>();
			while (patientRS.next()) {
				patientList.add(Long.valueOf(patientRS.getLong("PatientMID")).toString());
			}
			final List<RemoteMonitoringDataBean> dataList = loader.loadList(dataRS);

			int idx1;
			int idx2;
			// Go through all patients and remove any that aren't monitored by
			// this HCP
			for (idx1 = 0; idx1 < dataList.size(); idx1++) {
				if (!patientList.contains(Long.valueOf(dataList.get(idx1).getPatientMID()).toString())) {
					dataList.remove(idx1);
					idx1--;
				}
			}

			// Add values in patient list with no data for today to list.
			boolean itsThere;
			for (idx1 = 0; idx1 < patientList.size(); idx1++) {
				itsThere = false;
				for (idx2 = 0; idx2 < dataList.size(); idx2++) {
					if (dataList.get(idx2).getPatientMID() == Long.parseLong(patientList.get(idx1))) {
						itsThere = true;
						break;
					}
				}
				if (!itsThere) {
					dataList.add(new RemoteMonitoringDataBean(Long.parseLong(patientList.get(idx1))));
				}
			}

			return dataList;
		} catch (SQLException e) {
			throw new DBException(e);
		}
	}

	/**
	 * getPatientDataByDate
	 * 
	 * @param patientMID
	 *            patientMID
	 * @param lower
	 *            lower
	 * @param upper
	 *            upeer
	 * @return dataList
	 * @throws DBException
	 */
	public List<RemoteMonitoringDataBean> getPatientDataByDate(final long patientMID, final Date lower,
			final Date upper) throws DBException {
		try (Connection conn = factory.getConnection();
				PreparedStatement stmt = conn.prepareStatement(
						"SELECT * FROM remotemonitoringdata WHERE PatientID=? AND timeLogged >= ? AND timeLogged <= ? ORDER BY timeLogged DESC")) {
			stmt.setLong(1, patientMID);
			stmt.setTimestamp(2, new Timestamp(lower.getTime()));
			// add 1 day's worth to include the upper
			stmt.setTimestamp(3, new Timestamp(upper.getTime() + 1000L * 60L * 60 * 24L));
			final ResultSet results = stmt.executeQuery();
			final List<RemoteMonitoringDataBean> dataList = loader.loadList(results);
			results.close();
			return dataList;
		} catch (SQLException e) {
			throw new DBException(e);
		}
	}

	/**
	 * Get the requested type of data for the specified patient.
	 * 
	 * @param patientMID
	 *            The MID of the patient
	 * @param dataType
	 *            The type of telemedicine data to return
	 * @return A list of beans which all contain information of the requested
	 *         type
	 * @throws DBException
	 */
	public List<RemoteMonitoringDataBean> getPatientDataByType(final long patientMID, final String dataType)
			throws DBException {
		try (Connection conn = factory.getConnection();
				PreparedStatement stmt = conn
						.prepareStatement("SELECT * FROM remotemonitoringdata WHERE PatientID=? AND " + dataType
								+ " != -1 ORDER BY timeLogged ASC")) {
			stmt.setLong(1, patientMID);

			final ResultSet results = stmt.executeQuery();
			final List<RemoteMonitoringDataBean> dataList = loader.loadList(results);
			results.close();
			return dataList;
		} catch (SQLException e) {
			throw new DBException(e);
		}
	}

	/**
	 * Store pedometer reading and height/weight data for a given patient in the
	 * RemoteMonitoringData table
	 * 
	 * @param patientMID
	 *            The MID of the patient
	 * @param bean
	 *            bean
	 * @param height
	 *            The height of the patient
	 * @param weight
	 *            The weight of the patient
	 * @param reporterRole
	 *            The role of the person that reported these monitoring stats
	 * @param reporterMID
	 *            The MID of the person that reported these monitoring stats
	 * @throws ITrustException
	 */
	public void storePatientData(final long patientMID, final RemoteMonitoringDataBean bean, final String reporterRole,
			final long reporterMID) throws ITrustException {
		float height = bean.getHeight();
		float weight = bean.getWeight();
		int pedometer = bean.getPedometerReading();
		int sbp = bean.getSystolicBloodPressure();
		int dbp = bean.getDiastolicBloodPressure();
		int glucose = bean.getGlucoseLevel();

		if (height == 0) {
			height = -1;
		}
		if (weight == 0) {
			weight = -1;
		}
		if (pedometer == 0) {
			pedometer = -1;
		}
		if (sbp == 0) {
			sbp = -1;
		}
		if (dbp == 0) {
			dbp = -1;
		}
		if (glucose == 0) {
			glucose = -1;
		}

		if (getNumberOfDailyEntries(patientMID, "height") >= 1 && height != -1) {
			throw new ITrustException("Patient height entries for today cannot exceed 1.");
		}
		if (getNumberOfDailyEntries(patientMID, "weight") >= 1 && weight != -1) {
			throw new ITrustException("Patient weight entries for today cannot exceed 1.");
		}
		if (getNumberOfDailyEntries(patientMID, "pedometerReading") >= 1 && pedometer != -1) {
			throw new ITrustException("Patient pedometer reading entries for today cannot exceed 1.");
		}
		if (getNumberOfDailyEntries(patientMID, "glucoseLevel") >= 10 && glucose != -1) {
			throw new ITrustException("Patient glucose level entries for today cannot exceed 10.");
		}
		if (getNumberOfDailyEntries(patientMID, "systolicBloodPressure") >= 10 && sbp != -1) {
			throw new ITrustException("Patient systolic blood pressure entries for today cannot exceed 10.");
		}
		if (getNumberOfDailyEntries(patientMID, "diastolicBloodPressure") >= 10 && dbp != -1) {
			throw new ITrustException("Patient diastolic blood pressure entries for today cannot exceed 10.");

		}
		if ("patient representative".equals(reporterRole)) {
			validatePR(reporterMID, patientMID);
		}
		try (Connection conn = factory.getConnection();
				PreparedStatement pstmt = conn
						.prepareStatement("INSERT INTO remotemonitoringdata(PatientID, height, weight, "
								+ "pedometerReading, systolicBloodPressure, diastolicBloodPressure, glucoseLevel, ReporterRole, ReporterID) VALUES(?,?,?,?,?,?,?,?,?)")) {
			pstmt.setLong(1, patientMID);
			pstmt.setFloat(2, height);
			pstmt.setFloat(3, weight);
			pstmt.setInt(4, pedometer);
			pstmt.setInt(5, sbp);
			pstmt.setInt(6, dbp);
			pstmt.setInt(7, glucose);
			pstmt.setString(8, reporterRole);
			pstmt.setLong(9, reporterMID);

			pstmt.executeUpdate();
		} catch (SQLException e) {
			throw new DBException(e);
		}
	}

	/**
	 * Private method to get the number of entries for a certain patientID and a
	 * certain data type for today.
	 * 
	 * @param patientMID
	 * @param dataType
	 * @return the number of entries
	 * @throws DBException
	 */
	private int getNumberOfDailyEntries(final long patientMID, final String dataType) throws DBException {
		try (Connection conn = factory.getConnection();
				PreparedStatement stmt = conn
						.prepareStatement("SELECT * FROM remotemonitoringdata WHERE PatientID=? AND " + dataType
								+ "!=? AND DATE(timeLogged)=CURRENT_DATE")) {
			stmt.setLong(1, patientMID);
			stmt.setInt(2, -1);
			final ResultSet results = stmt.executeQuery();
			final List<RemoteMonitoringDataBean> patients = loader.loadList(results);
			results.close();
			return patients.size();
		} catch (SQLException e) {
			throw new DBException(e);
		}
	}

	/**
	 * validatePR
	 * 
	 * @param representativeMID
	 *            representativeMID
	 * @param patientMID
	 *            patientMID
	 * @throws ITrustException
	 */
	public void validatePR(final long representativeMID, final long patientMID) throws ITrustException {
		try (Connection conn = factory.getConnection();
				PreparedStatement stmt = conn.prepareStatement(
						"SELECT * FROM representatives WHERE RepresenterMID=? AND RepresenteeMID=?")) {
			stmt.setLong(1, representativeMID);
			stmt.setLong(2, patientMID);
			final ResultSet results = stmt.executeQuery();
			boolean exists = results.next();
			results.close();
			if (!exists) {
				throw new ITrustException("Representer is not valid for patient " + patientMID);
			}
		} catch (SQLException e) {
			throw new DBException(e);
		}
	}

	/**
	 * Show the list of HCPs monitoring this patient
	 * 
	 * @param patientMID
	 *            The MID of the patient
	 * @return list of HCPs monitoring the provided patient
	 */
	public List<PersonnelBean> getMonitoringHCPs(final long patientMID) throws DBException {
		try (Connection conn = factory.getConnection();
				PreparedStatement stmt = conn.prepareStatement("SELECT * FROM personnel, remotemonitoringlists "
						+ "WHERE remotemonitoringlists.PatientMID=? AND remotemonitoringlists.HCPMID=personnel.MID")) {
			stmt.setLong(1, patientMID);
			final ResultSet results = stmt.executeQuery();
			final List<PersonnelBean> personnel = personnelLoader.loadList(results);
			results.close();

			return personnel;
		} catch (SQLException e) {
			throw new DBException(e);
		}
	}

	/**
	 * Add a patient to the list of HCPs' monitoring lists of Patients
	 * 
	 * @param patientMID
	 *            The MID of the patient
	 * @param HCPMID
	 *            The MID of the HCP
	 * @param tBean
	 *            The TelemedicineBean indicating what telemedicine data the
	 *            patient is allowed to enter.
	 * @return true if added successfully, false if already in list
	 */
	public boolean addPatientToList(final long patientMID, final long HCPMID, final TelemedicineBean tBean)
			throws DBException {
		String permissionPS = "SystolicBloodPressure, DiastolicBloodPressure, GlucoseLevel, Height, Weight, PedometerReading";
		try (Connection conn = factory.getConnection();
				PreparedStatement stmt = conn
						.prepareStatement("SELECT * FROM remotemonitoringlists WHERE PatientMID = ? AND HCPMID = ?");
				PreparedStatement stmt2 = conn.prepareStatement("INSERT INTO remotemonitoringlists(PatientMID, HCPMID, "
						+ permissionPS + ") VALUES(?,?,?,?,?,?,?,?)")) {
			stmt.setLong(1, patientMID);
			stmt.setLong(2, HCPMID);
			final ResultSet results = stmt.executeQuery();
			boolean hasResults = results.next();
			results.close();

			if (hasResults) {
				return false;
			}

			// TODO: somebody should probably make this use a loader of some kind
			stmt2.setLong(1, patientMID);
			stmt2.setLong(2, HCPMID);
			stmt2.setBoolean(3, tBean.isSystolicBloodPressureAllowed());
			stmt2.setBoolean(4, tBean.isDiastolicBloodPressureAllowed());
			stmt2.setBoolean(5, tBean.isGlucoseLevelAllowed());
			stmt2.setBoolean(6, tBean.isHeightAllowed());
			stmt2.setBoolean(7, tBean.isWeightAllowed());
			stmt2.setBoolean(8, tBean.isPedometerReadingAllowed());
			stmt2.executeUpdate();

			return true;
		} catch (SQLException e) {
			throw new DBException(e);
		}
	}

	/**
	 * Remove a patient from the list of HCPs' monitoring lists of Patients
	 * 
	 * @param patientMID
	 *            The MID of the patient
	 * @param HCPMID
	 *            The MID of the HCP
	 * @return true if removed successfully, false if not in list
	 */
	public boolean removePatientFromList(final long patientMID, final long HCPMID) throws DBException {
		try (Connection conn = factory.getConnection();
				PreparedStatement stmt = conn
						.prepareStatement("DELETE FROM remotemonitoringlists WHERE PatientMID = ? AND HCPMID = ?")) {
			stmt.setLong(1, patientMID);
			stmt.setLong(2, HCPMID);
			boolean removed = stmt.executeUpdate() != 0;
			return removed;
		} catch (SQLException e) {
			throw new DBException(e);
		}
	}
}
