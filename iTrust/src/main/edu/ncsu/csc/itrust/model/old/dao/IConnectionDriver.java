package edu.ncsu.csc.itrust.model.old.dao;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Used by DAOFactory to abstract away different ways of getting our JDBC connection
 * 
 *  
 * 
 */
public interface IConnectionDriver {
	public Connection getConnection() throws SQLException;
}
