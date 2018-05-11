package edu.ncsu.csc.itrust.model.user;

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
import edu.ncsu.csc.itrust.model.old.enums.Role;
@ManagedBean
@RequestScoped
public class UserMySQLConverter implements DataBean<User>, Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -8056934124263634466L;
	@Resource(name="jdbc/itrust")
	private static DAOFactory factory;
	private SQLLoader<User> loader;
	private DataSource ds;
	//private transient final ProductionConnectionDriver driver = new ProductionConnectionDriver();


	/**
	 * @throws DBException
	 */
	public UserMySQLConverter() throws DBException{
		loader = new UserSQLConvLoader();
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
	public UserMySQLConverter(DataSource ds) throws DBException{
		this.ds = ds;

			loader = new UserSQLConvLoader();

	}
	
	@Override
	public List<User> getAll() throws DBException {
		List<User> list = new ArrayList<User>();
		Connection conn = null;
		PreparedStatement pstring = null;
		ResultSet results = null;
		try {
			conn = ds.getConnection();
			pstring = conn.prepareStatement("SELECT * FROM users");
		
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
	public User getByID(long id) throws DBException {
		User ret = null;
		Connection conn = null;
		PreparedStatement pstring = null;
		ResultSet results = null;
		String stmt = "";
		Role userRole = getUserRole(id);
		switch (userRole) {
		case HCP:
		case PHA:
		case ADMIN:
		case UAP:
		case ER:
		case LT:
			stmt = "SELECT users.MID AS MID, users.Role AS Role, personnel.firstName AS firstName, personnel.lastName AS lastName FROM users INNER JOIN personnel ON users.MID = personnel.MID WHERE users.MID=?;";
			break;
//ToDo: add back in when needed
		case PATIENT:
			stmt = "SELECT users.MID AS MID, users.Role AS Role, patients.firstName AS firstName, patients.lastName AS lastName FROM users INNER JOIN patients ON users.MID = patients.MID WHERE users.MID=?;";
//			throw new IllegalStateException("unimplemented");
			break;
		case TESTER:
			stmt = "SELECT MID AS MID, Role, '' AS firstName, MID AS lastName from Users WHERE MID=?;";
			break;
		default:
			throw new DBException(new SQLException("Role " + userRole + " not supported"));
		}
		try {
			List<User> list = null;
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
	public boolean add(User addObj) throws DBException {
		throw new IllegalStateException("unimplemented");
		// TODO implement as needed

	}
	@Override
	public boolean update(User updateObj) throws DBException {
		// TODO Implement as needed
		throw new IllegalStateException("unimplemented");
	}
	
	private Role getUserRole(long mid) throws DBException{
		Role userRole = null;
		Connection conn = null;
		PreparedStatement pstring = null;
		ResultSet results = null;
		try {
			conn=ds.getConnection();
			pstring = conn.prepareStatement("SELECT Role FROM users WHERE MID = ?;");
			pstring.setString(1, Long.toString(mid));
			results = pstring.executeQuery();
			results.next();
			String roleName = results.getString("Role");
			userRole = Role.parse(roleName);

			return userRole;
		} catch (SQLException e) {
			throw new DBException(e);
		} 
		finally{
			try{
				if(results != null){
					results.close();
				}
			} catch (SQLException e) {
				throw new DBException(e);
			} 
			
			
			finally{
				DBUtil.closeConnection(conn, pstring);
			}
		}

		
		
	}



}
