package edu.ncsu.csc.itrust.model.old.dao.mysql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import edu.ncsu.csc.itrust.exception.DBException;
import edu.ncsu.csc.itrust.model.old.beans.ApptRequestBean;
import edu.ncsu.csc.itrust.model.old.beans.loaders.ApptRequestBeanLoader;
import edu.ncsu.csc.itrust.model.old.dao.DAOFactory;

/**
 * 
 *
 */
public class ApptRequestDAO {

	private transient final DAOFactory factory;
	private transient final ApptRequestBeanLoader loader;

	public ApptRequestDAO(final DAOFactory factory) {
		this.factory = factory;
		loader = new ApptRequestBeanLoader();
	}

	/**
	 * 
	 * @param hcpid
	 * @return
	 * @throws DBException
	 */
	public List<ApptRequestBean> getApptRequestsFor(final long hcpid) throws SQLException, DBException {
		try (Connection conn = factory.getConnection();
				PreparedStatement stmt = conn
						.prepareStatement("SELECT * FROM appointmentrequests WHERE doctor_id=? ORDER BY sched_date")) {
			stmt.setLong(1, hcpid);

			final ResultSet results = stmt.executeQuery();
			final List<ApptRequestBean> list = loader.loadList(results);
			results.close();
			return list;
		} catch (SQLException e) {
			throw new DBException(e);
		}

	}

	/**
	 * 
	 * @param req
	 * @throws SQLException
	 * @throws DBException
	 */
	public void addApptRequest(final ApptRequestBean req) throws SQLException, DBException {
		try (Connection conn = factory.getConnection();
				PreparedStatement stmt = conn.prepareStatement(
						"INSERT INTO appointmentrequests (appt_type, patient_id, doctor_id, sched_date, "
								+ "comment, pending, accepted) VALUES (?, ?, ?, ?, ?, ?, ?)")) {
			loader.loadParameters(stmt, req);
			stmt.executeUpdate();
		} catch (SQLException e) {
			throw new DBException(e);
		}

	}

	/**
	 * 
	 * @param req
	 * @throws SQLException
	 * @throws DBException
	 */
	public void updateApptRequest(final ApptRequestBean req) throws SQLException, DBException {
		try (Connection conn = factory.getConnection();
				PreparedStatement stmt = conn
						.prepareStatement("UPDATE appointmentrequests SET pending=?, accepted=? WHERE appt_id=?")) {
			stmt.setBoolean(1, req.isPending());
			stmt.setBoolean(2, req.isAccepted());
			stmt.setInt(3, req.getRequestedAppt().getApptID());

			stmt.executeUpdate();
		} catch (SQLException e) {
			throw new DBException(e);
		}
	}

	public ApptRequestBean getApptRequest(final int reqID) throws SQLException, DBException {
		ApptRequestBean bean;
		try (Connection conn = factory.getConnection();
				PreparedStatement stmt = conn.prepareStatement("SELECT * FROM appointmentrequests WHERE appt_id=?")) {
			stmt.setInt(1, reqID);
			final ResultSet results = stmt.executeQuery();
			if (results.next()) {
				bean = loader.loadSingle(results);
			} else {
				bean = null;
			}
			results.close();
		} catch (SQLException e) {
			throw new DBException(e);
		}
		return bean;
	}
}
