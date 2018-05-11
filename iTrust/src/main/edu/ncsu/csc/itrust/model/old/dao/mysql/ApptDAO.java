package edu.ncsu.csc.itrust.model.old.dao.mysql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import edu.ncsu.csc.itrust.exception.DBException;
import edu.ncsu.csc.itrust.model.old.beans.ApptBean;
import edu.ncsu.csc.itrust.model.old.beans.loaders.ApptBeanLoader;
import edu.ncsu.csc.itrust.model.old.dao.DAOFactory;

public class ApptDAO {

	private transient final DAOFactory factory;
	private transient final ApptBeanLoader abloader;
	private transient final ApptTypeDAO apptTypeDAO;

	private static final int MIN_MID = 999999999;

	public ApptDAO(final DAOFactory factory) {
		this.factory = factory;
		this.apptTypeDAO = factory.getApptTypeDAO();
		this.abloader = new ApptBeanLoader();
	}

	public List<ApptBean> getAppt(final int apptID) throws SQLException, DBException {
		ResultSet results = null;
		try (Connection conn = factory.getConnection();
				PreparedStatement stmt = conn.prepareStatement("SELECT * FROM appointment WHERE appt_id=?")) {
			stmt.setInt(1, apptID);
			results = stmt.executeQuery();
			final List<ApptBean> abList = this.abloader.loadList(results);
			return abList;
		} catch (SQLException e) {
			throw new DBException(e);
		}
	}

	public List<ApptBean> getApptsFor(final long mid) throws SQLException, DBException {
		try (Connection conn = factory.getConnection();
				PreparedStatement stmt = (mid >= MIN_MID)
						? conn.prepareStatement(
								"SELECT * FROM appointment WHERE doctor_id=? AND sched_date > NOW() ORDER BY sched_date;")
						: conn.prepareStatement(
								"SELECT * FROM appointment WHERE patient_id=? AND sched_date > NOW() ORDER BY sched_date;")) {
			stmt.setLong(1, mid);

			ResultSet results = stmt.executeQuery();
			List<ApptBean> abList = this.abloader.loadList(results);
			results.close();
			return abList;
		} catch (SQLException e) {
			throw new DBException(e);
		}
	}

	public List<ApptBean> getAllApptsFor(long mid) throws SQLException, DBException {
		try (Connection conn = factory.getConnection();
				PreparedStatement stmt = (mid >= MIN_MID)
						? conn.prepareStatement("SELECT * FROM appointment WHERE doctor_id=? ORDER BY sched_date;")
						: conn.prepareStatement("SELECT * FROM appointment WHERE patient_id=? ORDER BY sched_date;")) {
			stmt.setLong(1, mid);

			final ResultSet results = stmt.executeQuery();
			final List<ApptBean> abList = this.abloader.loadList(results);
			results.close();
			return abList;
		} catch (SQLException e) {
			throw new DBException(e);
		}

	}

	public void scheduleAppt(final ApptBean appt) throws SQLException, DBException {
		try (Connection conn = factory.getConnection();
				PreparedStatement stmt = this.abloader.loadParameters(conn.prepareStatement(
						"INSERT INTO appointment (appt_type, patient_id, doctor_id, sched_date, comment) "
								+ "VALUES (?, ?, ?, ?, ?)"),
						appt)) {
			stmt.executeUpdate();
		} catch (SQLException e) {
			throw new DBException(e);
		}
	}

	public void editAppt(final ApptBean appt) throws SQLException, DBException {
		try (Connection conn = factory.getConnection();
				PreparedStatement stmt = conn
						.prepareStatement("UPDATE appointment SET appt_type=?, sched_date=?, comment=? WHERE appt_id=?")) {
			stmt.setString(1, appt.getApptType());
			stmt.setTimestamp(2, appt.getDate());
			stmt.setString(3, appt.getComment());
			stmt.setInt(4, appt.getApptID());

			stmt.executeUpdate();
		} catch (SQLException e) {
			throw new DBException(e);
		}
	}

	public void removeAppt(final ApptBean appt) throws SQLException, DBException {
		try (Connection conn = factory.getConnection();
				PreparedStatement stmt = conn.prepareStatement("DELETE FROM appointment WHERE appt_id=?")) {
			stmt.setInt(1, appt.getApptID());

			stmt.executeUpdate();
			stmt.close();
		} catch (SQLException e) {
			throw new DBException(e);
		}
	}

	public List<ApptBean> getAllHCPConflictsForAppt(final long mid, final ApptBean appt)
			throws SQLException, DBException {

		final int duration = apptTypeDAO.getApptType(appt.getApptType()).getDuration();

		try (Connection conn = factory.getConnection();
				PreparedStatement stmt = conn.prepareStatement("SELECT * " 
						+ "FROM appointment a, appointmenttype type " // and the corresponding types 
						+ "WHERE a.appt_type=type.appt_type AND " // match them with types
						+ "((DATE_ADD(a.sched_date, INTERVAL type.duration MINUTE)>? AND " // a1 ends after a2 starts AND
						+ "a.sched_date<=?) OR " // a1 starts before a2 OR
						+ "(DATE_ADD(?, INTERVAL ? MINUTE)>a.sched_date AND " // a2 ends after a1 starts AND
						+ "?<=a.sched_date)) AND " // a2 starts before a1 starts
						+ "a.doctor_id=? AND a.appt_id!=?;")
			) {
			stmt.setTimestamp(1, appt.getDate());
			stmt.setTimestamp(2, appt.getDate());
			stmt.setTimestamp(3, appt.getDate());
			stmt.setInt(4, duration);
			stmt.setTimestamp(5, appt.getDate());
			stmt.setLong(6, mid);
			stmt.setInt(7, appt.getApptID());

			final ResultSet results = stmt.executeQuery();

			final List<ApptBean> conflictList = this.abloader.loadList(results);
			results.close();
			return conflictList;
		} catch (SQLException e) {
			throw new DBException(e);
		}
	}

	public List<ApptBean> getAllPatientConflictsForAppt(final long mid, final ApptBean appt)
			throws SQLException, DBException {
		final int duration = apptTypeDAO.getApptType(appt.getApptType()).getDuration();
		try (Connection conn = factory.getConnection();
				PreparedStatement stmt = conn.prepareStatement("SELECT * " 
						+ "FROM appointment a, appointmenttype type " // and the corresponding types
						+ "WHERE a.appt_type=type.appt_type AND " // match them with types
						+ "((DATE_ADD(a.sched_date, INTERVAL type.duration MINUTE)>? AND "// a1 ends after a2 starts AND
						+ "a.sched_date<=?) OR " // a1 starts before a2 OR
						+ "(DATE_ADD(?, INTERVAL ? MINUTE)>a.sched_date AND " // a2 ends after a1 starts AND
						+ "?<=a.sched_date)) AND " // a2 starts before a1 starts
						+ "a.patient_id=? AND a.appt_id!=?;")
			) {
			stmt.setTimestamp(1, appt.getDate());
			stmt.setTimestamp(2, appt.getDate());
			stmt.setTimestamp(3, appt.getDate());
			stmt.setInt(4, duration);
			stmt.setTimestamp(5, appt.getDate());
			stmt.setLong(6, mid);
			stmt.setInt(7, appt.getApptID());

			final ResultSet results = stmt.executeQuery();

			final List<ApptBean> conflictList = this.abloader.loadList(results);
			results.close();
			return conflictList;
		} catch (SQLException e) {
			throw new DBException(e);
		}

	}

	/**
	 * Returns all past and future appointment conflicts for the doctor with the
	 * given MID
	 * 
	 * @param mid
	 * @throws SQLException
	 */
	public List<ApptBean> getAllConflictsForDoctor(final long mid) throws SQLException, DBException {
		try (Connection conn = factory.getConnection();
				PreparedStatement stmt = conn.prepareStatement("SELECT a1.* "
						+ "FROM appointment a1, appointment a2, " // all possible sets of 2 appts
						+ "appointmenttype type1,appointmenttype type2 " // and the corresponding types
						+ "WHERE a1.appt_id!=a2.appt_id AND " // exclude itself
						+ "a1.appt_type=type1.appt_type AND a2.appt_type=type2.appt_type AND " // match then with types
						+ "((DATE_ADD(a1.sched_date, INTERVAL type1.duration MINUTE)>a2.sched_date AND " // a1 ends after a2 starts AND
						+ "a1.sched_date<=a2.sched_date) OR" // a1 starts before a2 OR
						+ "(DATE_ADD(a2.sched_date, INTERVAL type2.duration MINUTE)>a1.sched_date AND " // a2 ends after a1 starts AND
						+ "a2.sched_date<=a1.sched_date)) AND " // a2 starts before a1 starts
						+ "a1.doctor_id=? AND a2.doctor_id=?;")
			) {
			stmt.setLong(1, mid);
			stmt.setLong(2, mid);

			final ResultSet results = stmt.executeQuery();

			final List<ApptBean> conflictList = this.abloader.loadList(results);
			results.close();
			return conflictList;
		} catch (SQLException e) {
			throw new DBException(e);
		}

	}

	/**
	 * Returns all past and future appointment conflicts for the patient with
	 * the given MID
	 * 
	 * @param mid
	 * @throws SQLException
	 * @throws DBException
	 */
	public List<ApptBean> getAllConflictsForPatient(final long mid) throws SQLException, DBException {
		try (Connection conn = factory.getConnection();
				PreparedStatement stmt = conn.prepareStatement("SELECT a1.* "
						+ "FROM appointment a1, appointment a2, " // all possible sets of 2 appts
						+ "appointmenttype type1,appointmenttype type2 " // and the corresponding types
						+ "WHERE a1.appt_id!=a2.appt_id AND " // exclude itself
						+ "a1.appt_type=type1.appt_type AND a2.appt_type=type2.appt_type AND " // match them with types
						+ "((DATE_ADD(a1.sched_date, INTERVAL type1.duration MINUTE)>a2.sched_date AND " // a1 ends after a2 starts AND
						+ "a1.sched_date<=a2.sched_date) OR" // a1 starts before a2 OR
						+ "(DATE_ADD(a2.sched_date, INTERVAL type2.duration MINUTE)>a1.sched_date AND " // a2 ends after a1 starts AND
						+ "a2.sched_date<=a1.sched_date)) AND " // a2 starts before a1 starts
						+ "a1.patient_id=? AND a2.patient_id=?;")
			) {
			stmt.setLong(1, mid);
			stmt.setLong(2, mid);

			final ResultSet results = stmt.executeQuery();

			final List<ApptBean> conflictList = this.abloader.loadList(results);
			results.close();
			return conflictList;
		} catch (SQLException e) {
			throw new DBException(e);
		}

	}
}
