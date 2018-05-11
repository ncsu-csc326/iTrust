package edu.ncsu.csc.itrust.model.apptType;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import edu.ncsu.csc.itrust.DBUtil;
import edu.ncsu.csc.itrust.exception.DBException;

@ManagedBean(name="appt_type")
@SessionScoped
public class ApptTypeMySQLConverter  implements Serializable, ApptTypeData{
	/**
	 * 
	 */
	private static final long serialVersionUID = 4035629854880598008L;
	@Resource(name="jdbc/itrust")
	private DataSource ds;
	private ApptTypeMySQLLoader apptTypeLoader;
	/**
	 * @throws DBException 
	 * 
	 */

	public ApptTypeMySQLConverter() throws DBException{
			apptTypeLoader = new ApptTypeMySQLLoader();
			try {
				Context ctx = new InitialContext();
					ds = ((DataSource) (((Context) ctx.lookup("java:comp/env"))).lookup("jdbc/itrust"));
			} catch (NamingException e) {
				throw new DBException(new SQLException("Context Lookup Naming Exception: "+e.getMessage()));
			}

	}
	
	public ApptTypeMySQLConverter(DataSource ds){
		apptTypeLoader = new ApptTypeMySQLLoader();
		this.ds = ds;
	}
	
	@Override
	public Map<Long, ApptType> getApptTypeIDs(String name) throws DBException{
		Map<Long, ApptType> apptRef = null;
		Connection conn = null;
		PreparedStatement pstring = null;
		ResultSet results = null;
		try{
			apptRef = new HashMap<Long, ApptType>();
			conn=ds.getConnection();
			pstring = conn.prepareStatement("SELECT * FROM appointmenttype WHERE appt_type=?");
		
			pstring.setString(1, name);
		
			results = pstring.executeQuery();
			apptRef = apptTypeLoader.loadRefList(results);
	
		}
		catch(SQLException e){
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
		return apptRef;
		
	}

	@Override
	public String getApptTypeName(Long id) throws DBException{
		String apptname = null;
		Connection conn = null;
		PreparedStatement pstring = null;
		ResultSet results = null;
		try{

			conn=ds.getConnection();
			pstring = conn.prepareStatement("SELECT appt_type FROM appointmenttype WHERE apptType_id=?;");
		
			pstring.setLong(1, id);
		
			results = pstring.executeQuery();
			@SuppressWarnings("unused") // Check is useful for debugging purposes
			boolean check = results.next();
			apptname = results.getString("appt_type");
	
		}
		catch(SQLException e){
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
		return apptname;
	}

	@Override
	public List<ApptType> getAll() throws DBException {
		List<ApptType> ret = null;
		Connection conn = null;
		PreparedStatement pstring = null;
		ResultSet results = null;
		try{
			conn=ds.getConnection();
			pstring = conn.prepareStatement("SELECT * FROM appointmenttype;");
		
			results = pstring.executeQuery();
			ret = apptTypeLoader.loadList(results);
	
		}
		catch(SQLException e){
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
		return ret;
	}

	@Override
	public ApptType getByID(long id) throws DBException {
		ApptType ret = null;
		Connection conn = null;
		PreparedStatement pstring = null;
		ResultSet results = null;
		try{

			conn=ds.getConnection();
			pstring = conn.prepareStatement("SELECT * FROM appointmenttype WHERE apptType_id=?;");
		
			pstring.setLong(1, id);
		
			results = pstring.executeQuery();
			results.next();
			ret = apptTypeLoader.loadSingle(results);
		
		}
		catch(SQLException e){
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
		return ret;
	}

	@Override
	public boolean add(ApptType at) throws DBException {
		boolean retval = false;
		Connection conn = null;
		PreparedStatement pstring = null;
		int results;
		try {
			conn = ds.getConnection();
			pstring = apptTypeLoader.loadParameters(conn, pstring, at, true);
			results = pstring.executeUpdate();
			retval = (results >0);
		}
		catch(SQLException e){
			throw new DBException(e);
			
		}
		finally{

			DBUtil.closeConnection(conn, pstring);
		}
		return retval;
	}

	@Override
	public boolean update(ApptType at) throws DBException {
		boolean retval = false;
		Connection conn = null;
		PreparedStatement pstring = null;

		int results;
		try {
			conn = ds.getConnection();
			pstring = apptTypeLoader.loadParameters(conn, pstring, at, false);
			results = pstring.executeUpdate();
			retval = (results >0);
		}
		catch(SQLException e){
			throw new DBException(e);
			
		}
		finally{

			DBUtil.closeConnection(conn, pstring);
		}
		return retval;
	}



}
