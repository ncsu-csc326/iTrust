package edu.ncsu.csc.itrust.model.old.dao.mysql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import edu.ncsu.csc.itrust.exception.DBException;
import edu.ncsu.csc.itrust.model.old.beans.RecordsReleaseBean;
import edu.ncsu.csc.itrust.model.old.beans.loaders.RecordsReleaseBeanLoader;
import edu.ncsu.csc.itrust.model.old.dao.DAOFactory;

public class RecordsReleaseDAO {
	DAOFactory factory;
	RecordsReleaseBeanLoader loader = new RecordsReleaseBeanLoader();

	public RecordsReleaseDAO(DAOFactory factory) {
		this.factory = factory;
	}

	public boolean addRecordsRelease(RecordsReleaseBean bean) throws DBException {
		try (Connection conn = factory.getConnection();
				PreparedStatement ps = loader.loadParameters(conn.prepareStatement(
						"INSERT INTO recordsrelease(requestDate,pid,releaseHospitalID,recHospitalName,"
								+ "recHospitalAddress,docFirstName,docLastName,docPhone,docEmail,justification,status) "
								+ "VALUES(?,?,?,?,?,?,?,?,?,?,?)"),
						bean)) {
			int numInserted = ps.executeUpdate();
			return numInserted == 1;
		} catch (SQLException e) {
			throw new DBException(e);
		}
	}

	public boolean updateRecordsRelease(RecordsReleaseBean bean) throws DBException {
		try (Connection conn = factory.getConnection();
				PreparedStatement ps = loader.loadParameters(
						conn.prepareStatement("UPDATE recordsrelease SET requestDate=?, pid=?, releaseHospitalID=?, "
								+ "recHospitalName=?, recHospitalAddress=?, docFirstName=?, docLastName=?, docPhone=?, "
								+ "docEmail=?, justification=?, status=? WHERE releaseID=?"),
						bean)) {
			ps.setLong(12, bean.getReleaseID());
			int numUpdated = ps.executeUpdate();
			return numUpdated == 1;
		} catch (SQLException e) {
			throw new DBException(e);
		}
	}

	public RecordsReleaseBean getRecordsReleaseByID(long releaseID) throws DBException {
		try (Connection conn = factory.getConnection();
				PreparedStatement ps = conn.prepareStatement("SELECT * FROM recordsrelease WHERE releaseID=?")) {
			ps.setLong(1, releaseID);
			ResultSet rs;
			rs = ps.executeQuery();
			// Get the first and only records release bean
			List<RecordsReleaseBean> records = loader.loadList(rs);
			rs.close();
			return (records.size() > 0) ? records.get(0) : null;
		} catch (SQLException e) {
			throw new DBException(e);
		}

	}

	public List<RecordsReleaseBean> getAllRecordsReleasesByHospital(String hospitalID) throws DBException {
		try (Connection conn = factory.getConnection();
				PreparedStatement ps = conn.prepareStatement(
						"SELECT * FROM recordsrelease " + "WHERE releaseHospitalID=? ORDER BY requestDate DESC")) {
			ps.setString(1, hospitalID);
			ResultSet rs = ps.executeQuery();
			List<RecordsReleaseBean> releases = loader.loadList(rs);
			rs.close();
			return releases;
		} catch (SQLException e) {
			throw new DBException(e);
		}

	}

	public List<RecordsReleaseBean> getAllRecordsReleasesByPid(long pid) throws DBException {
		try (Connection conn = factory.getConnection();
				PreparedStatement ps = conn
						.prepareStatement("SELECT * FROM recordsrelease " + "WHERE pid=? ORDER BY requestDate DESC")) {
			ps.setLong(1, pid);
			ResultSet rs = ps.executeQuery();
			List<RecordsReleaseBean> releases = loader.loadList(rs);
			rs.close();
			return releases;
		} catch (SQLException e) {
			throw new DBException(e);
		}
	}
}
