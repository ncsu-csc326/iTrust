package edu.ncsu.csc.itrust.model.old.beans.loaders;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import edu.ncsu.csc.itrust.model.old.beans.RemoteMonitoringDataBean;

/**
 * A loader for RemoteMonitoringDataBeans.
 * 
 * Loads in information to/from beans using ResultSets and PreparedStatements. Use the superclass to enforce consistency. 
 * For details on the paradigm for a loader (and what its methods do), see {@link BeanLoader}
 */
public class RemoteMonitoringDataBeanLoader implements BeanLoader<RemoteMonitoringDataBean> {

	@Override
	public List<RemoteMonitoringDataBean> loadList(ResultSet rs) throws SQLException {
		List<RemoteMonitoringDataBean> list = new ArrayList<RemoteMonitoringDataBean>();
		while (rs.next()) {
			list.add(loadSingle(rs));
		}
		return list;
	}

	@Override
	public PreparedStatement loadParameters(PreparedStatement ps, RemoteMonitoringDataBean bean) throws SQLException {
		throw new IllegalStateException("unimplemented!");
	}

	@Override
	public RemoteMonitoringDataBean loadSingle(ResultSet rs) throws SQLException {
		RemoteMonitoringDataBean d = new RemoteMonitoringDataBean();
		d.setLoggedInMID(rs.getLong("PatientID"));
		d.setSystolicBloodPressure(rs.getInt("systolicBloodPressure"));
		d.setDiastolicBloodPressure(rs.getInt("diastolicBloodPressure"));
		d.setGlucoseLevel(rs.getInt("glucoseLevel"));
		d.setHeight(rs.getFloat("height"));
		d.setWeight(rs.getFloat("weight"));
		d.setPedometerReading(rs.getInt("pedometerReading"));
		d.setTime(rs.getTimestamp("timeLogged"));
		d.setReporterRole(rs.getString("ReporterRole"));
		d.setReporterMID(rs.getLong("ReporterID"));
		return d;
	}
}
