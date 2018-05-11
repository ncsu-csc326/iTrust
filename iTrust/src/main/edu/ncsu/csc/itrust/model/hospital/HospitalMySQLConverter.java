package edu.ncsu.csc.itrust.model.hospital;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import edu.ncsu.csc.itrust.DBUtil;
import edu.ncsu.csc.itrust.exception.DBException;
import edu.ncsu.csc.itrust.model.SQLLoader;
import edu.ncsu.csc.itrust.model.ValidationFormat;
import edu.ncsu.csc.itrust.model.hospital.Hospital;
@ManagedBean
public class HospitalMySQLConverter implements HospitalData, Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 964736533764578154L;
	private DataSource ds;
	private static SQLLoader<Hospital> hospitalLoader;

	public HospitalMySQLConverter() throws DBException{
		hospitalLoader = new HospitalMySQLLoader();
		try {
			Context ctx = new InitialContext();
			this.ds = ((DataSource) (((Context) ctx.lookup("java:comp/env"))).lookup("jdbc/itrust"));
		} catch (NamingException e) {
			throw new DBException(new SQLException("Context Lookup Naming Exception: "+e.getMessage()));
		}

	}
	
	public HospitalMySQLConverter(DataSource ds){
		hospitalLoader = new HospitalMySQLLoader();
		this.ds = ds;
	}
	
	@Override
	public List<Hospital> getAll() throws DBException{
		List<Hospital> ret = null;
		Connection conn = null;
		PreparedStatement pstring = null;
		ResultSet results = null;
		try{

			conn=ds.getConnection();
			pstring = conn.prepareStatement("SELECT HospitalID, HospitalName,  Address, City, State, Zip FROM hospitals;");
		
			results = pstring.executeQuery();
			ret = hospitalLoader.loadList(results);
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
	public String getHospitalName(String id) throws DBException{
			String hospitalname = null;
			Connection conn = null;
			PreparedStatement pstring = null;
			ResultSet results = null;
			try{

				conn=ds.getConnection();
				pstring = conn.prepareStatement("SELECT HospitalName FROM hospitals WHERE HospitalID = ?;");
			
				pstring.setString(1, id);
			
				results = pstring.executeQuery();
				@SuppressWarnings("unused") //temp is useful for debugging purposes
				boolean temp = results.next();
				
				hospitalname = results.getString("HospitalName");
		
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
			return hospitalname;
	}

	@Override
	@Deprecated
	public Hospital getByID(long id) throws DBException {
		throw new IllegalStateException("HospitalID is NOT a 'long' value");

	}

	@Override
	public boolean add(Hospital hospital) throws DBException {
		boolean retval = false;
		Connection conn = null;
		PreparedStatement pstring = null;
		int results;
		try {
			conn = ds.getConnection();
			pstring = hospitalLoader.loadParameters(conn, pstring, hospital, true);
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
	public boolean update(Hospital hospital) throws DBException {
		boolean retval = false;
		Connection conn = null;
		PreparedStatement pstring = null;
		int results;
		try {
			conn = ds.getConnection();
			pstring = hospitalLoader.loadParameters(conn, pstring, hospital, false);
			results = pstring.executeUpdate();
			retval = (results >0);
		}
		catch(SQLException e){
			throw new DBException(e);
			
		}
		finally{

			DBUtil.closeConnection(conn, pstring);
		}
		return retval;		}

	@Override
	public Hospital getHospitalByID(String locationID) throws DBException {
		Hospital ret = null;
		Connection conn = null;
		PreparedStatement pstring = null;
		ResultSet results = null;
		if(!(ValidationFormat.HOSPITAL_ID.getRegex().matcher(locationID).matches())){
			throw new DBException(new SQLException("Invalid Location ID"));
		}
		try{

			conn=ds.getConnection();
			pstring = conn.prepareStatement("SELECT * FROM hospitals WHERE HospitalID=?;");
			ValidationFormat.HOSPITAL_ID.getRegex().matcher(locationID).matches();
			pstring.setString(1, locationID);
		
			results = pstring.executeQuery();
			results.next();
			ret = hospitalLoader.loadSingle(results);
		
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

}
