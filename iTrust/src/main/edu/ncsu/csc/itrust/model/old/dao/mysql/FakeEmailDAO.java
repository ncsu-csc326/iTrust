package edu.ncsu.csc.itrust.model.old.dao.mysql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import edu.ncsu.csc.itrust.exception.DBException;
import edu.ncsu.csc.itrust.model.old.beans.Email;
import edu.ncsu.csc.itrust.model.old.beans.loaders.EmailBeanLoader;
import edu.ncsu.csc.itrust.model.old.dao.DAOFactory;

/**
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
public class FakeEmailDAO {

	private DAOFactory factory;
	private EmailBeanLoader emailBeanLoader = new EmailBeanLoader();

	/**
	 * The typical constructor.
	 * 
	 * @param factory
	 *            The {@link DAOFactory} associated with this DAO, which is used
	 *            for obtaining SQL connections, etc.
	 */
	public FakeEmailDAO(DAOFactory factory) {
		this.factory = factory;
	}

	/**
	 * Return all emails that have been "sent" (inserted into the database)
	 * 
	 * @return A java.util.List of Email objects representing fake e-mails.
	 * @throws DBException
	 */
	public List<Email> getAllEmails() throws DBException {
		try (Connection conn = factory.getConnection();
				PreparedStatement stmt = conn.prepareStatement("SELECT * FROM fakeemail ORDER BY AddedDate DESC");
				ResultSet rs = stmt.executeQuery()) {
			List<Email> loadlist = emailBeanLoader.loadList(rs);
			return loadlist;
		} catch (SQLException e) {
			throw new DBException(e);
		}
	}

	/**
	 * Return all emails that a person has sent
	 * 
	 * @param email
	 *            The "From" email address as a string.
	 * @return A java.util.List of fake emails.
	 * @throws DBException
	 */
	public List<Email> getEmailsByPerson(String email) throws DBException {
		try (Connection conn = factory.getConnection();
				PreparedStatement stmt = conn
						.prepareStatement("SELECT * FROM fakeemail WHERE ToAddr LIKE ? ORDER BY AddedDate DESC");) {
			stmt.setString(1, "%" + email + "%");
			ResultSet rs = stmt.executeQuery();
			List<Email> loadlist = emailBeanLoader.loadList(rs);
			return loadlist;
		} catch (SQLException e) {
			throw new DBException(e);
		}
	}

	/**
	 * "Send" an email, which just inserts it into the database.
	 * 
	 * @param email
	 *            The Email object to insert.
	 * @throws DBException
	 */
	public void sendEmailRecord(Email email) throws DBException {
		try (Connection conn = factory.getConnection();
				PreparedStatement stmt = conn.prepareStatement(
						"INSERT INTO fakeemail (ToAddr, FromAddr, Subject, Body) " + "VALUES (?,?,?,?)")) {
			emailBeanLoader.loadParameters(stmt, email);
			stmt.executeUpdate();
		} catch (SQLException e) {
			throw new DBException(e);
		}
	}

	/**
	 * Returns a list of emails that have the given string as a substring of the
	 * body
	 * 
	 * @param string
	 *            The string to search within the body.
	 * @return A java.util.List of fake emails.
	 * @throws DBException
	 */
	public List<Email> getEmailWithBody(String bodySubstring) throws DBException {
		try (Connection conn = factory.getConnection();
				PreparedStatement stmt = conn.prepareStatement("SELECT * FROM fakeemail WHERE Instr(Body,?)>0 ORDER BY AddedDate DESC")) {
			stmt.setString(1, bodySubstring);
			ResultSet rs = stmt.executeQuery();
			List<Email> loadlist = emailBeanLoader.loadList(rs);
			rs.close();
			return loadlist;
		} catch (SQLException e) {
			throw new DBException(e);
		}
	}

}
