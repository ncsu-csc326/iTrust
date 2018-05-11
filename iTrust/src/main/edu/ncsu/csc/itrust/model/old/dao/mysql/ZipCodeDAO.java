package edu.ncsu.csc.itrust.model.old.dao.mysql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import edu.ncsu.csc.itrust.exception.DBException;
import edu.ncsu.csc.itrust.model.old.beans.ZipCodeBean;
import edu.ncsu.csc.itrust.model.old.beans.loaders.ZipCodeLoader;
import edu.ncsu.csc.itrust.model.old.dao.DAOFactory;

/**
 * DAO for the Zip Code table
 */
public class ZipCodeDAO {
	private DAOFactory factory;

	public ZipCodeDAO(DAOFactory factory) {
		this.factory = factory;
	}

	/**
	 * Returns the zip code bean for a particular zip code.
	 * 
	 * @param zipCode
	 * @return
	 * @throws DBException
	 */
	public ZipCodeBean getZipCode(String zipCode) throws DBException {
		ZipCodeLoader loader = new ZipCodeLoader();
		try (Connection conn = factory.getConnection();
				PreparedStatement ps = conn.prepareStatement("SELECT * FROM zipcodes WHERE zip=?")) {
			ps.setString(1, zipCode);
			ResultSet rs = ps.executeQuery();
			ZipCodeBean zipCodeBean = rs.next() ? loader.loadSingle(rs) : null;
			rs.close();
			return zipCodeBean;
		} catch (SQLException e) {
			throw new DBException(e);
		}
	}
}
