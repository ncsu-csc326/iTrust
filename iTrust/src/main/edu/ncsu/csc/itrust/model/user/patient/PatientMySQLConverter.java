package edu.ncsu.csc.itrust.model.user.patient;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import edu.ncsu.csc.itrust.DBUtil;
import edu.ncsu.csc.itrust.exception.DBException;
import edu.ncsu.csc.itrust.model.DataBean;
import edu.ncsu.csc.itrust.model.SQLLoader;
import edu.ncsu.csc.itrust.model.old.dao.DAOFactory;
@ManagedBean
@RequestScoped
public class PatientMySQLConverter implements DataBean<Patient>, Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -8056934124263634466L;
	@Resource(name="jdbc/itrust")
	private static DAOFactory factory;
	private SQLLoader<Patient> loader;
	private DataSource ds;
	//private transient final ProductionConnectionDriver driver = new ProductionConnectionDriver();


	/**
	 * @throws DBException
	 */
	public PatientMySQLConverter() throws DBException{
		loader = new PatientSQLConvLoader();
		try {
			Context ctx = new InitialContext();
				ds = ((DataSource) (((Context) ctx.lookup("java:comp/env"))).lookup("jdbc/itrust"));
		} catch (NamingException e) {
			throw new DBException(new SQLException("Context Lookup Naming Exception: "+e.getMessage()));
		}


	}
	
	/**Constructor used for unit testing
	 * @param ds
	 * @throws DBException
	 */
	public PatientMySQLConverter(DataSource ds) throws DBException{
		this.ds = ds;

			loader = new PatientSQLConvLoader();

	}
	
	@Override
	public List<Patient> getAll() throws DBException {
		List<Patient> list = new ArrayList<Patient>();
		Connection conn = null;
		PreparedStatement pstring = null;
		ResultSet results = null;
		try {
			conn = ds.getConnection();
			pstring = conn.prepareStatement("SELECT users.MID AS MID, users.Role AS Role, patients.firstName AS firstName, patients.lastName AS lastName FROM users INNER JOIN patients ON users.MID = patients.MID");
		
			results = pstring.executeQuery();
			list = loader.loadList(results);
			return list;
		} catch (SQLException e) {
			throw new DBException(e);
		} finally {
			try{
				if(results !=null){
					results.close();
				}
			} catch (SQLException e) {
				throw new DBException(e);
			} finally {
				DBUtil.closeConnection(conn, pstring);
			}
		}

	}
	@Override
	public Patient getByID(long id) throws DBException {
		Patient ret = null;
		Connection conn = null;
		PreparedStatement pstring = null;
		ResultSet results = null;
		String stmt = "";
		stmt = "SELECT users.MID AS MID, users.Role AS Role, patients.firstName AS firstName, patients.lastName AS lastName FROM users INNER JOIN patients ON users.MID = patients.MID WHERE users.MID=?;";
		try {
			List<Patient> list = null;
			conn=ds.getConnection();
			pstring = conn.prepareStatement(stmt);
			pstring.setString(1, Long.toString(id));
			results = pstring.executeQuery();
			list = loader.loadList(results);
			if(!list.isEmpty()){
				ret = list.get(0);
			}
			return ret;
		} catch (SQLException e) {
			throw new DBException(e);
		} 
		finally {
		
			try{
				if(results !=null){
					results.close();
				}
			} catch (SQLException e) {
				throw new DBException(e);
			} finally {
				DBUtil.closeConnection(conn, pstring);
			}
		}

	}
	@Override
	public boolean add(Patient addObj) throws DBException {
		throw new IllegalStateException("unimplemented");
		// TODO implement as needed

	}
	@Override
	public boolean update(Patient updateObj) throws DBException {
		// TODO Implement as needed
		throw new IllegalStateException("unimplemented");
	}
	




}
