package edu.ncsu.csc.itrust.model.old.beans.loaders;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import edu.ncsu.csc.itrust.model.old.beans.ReportRequestBean;


/**
 * A loader for ReportRequestBeans.
 * 
 * Loads in information to/from beans using ResultSets and PreparedStatements. Use the superclass to enforce consistency. 
 * For details on the paradigm for a loader (and what its methods do), see {@link BeanLoader}
 */
public class ReportRequestBeanLoader implements BeanLoader<ReportRequestBean> {

	@Override
	public List<ReportRequestBean> loadList(ResultSet rs) throws SQLException {
		ArrayList<ReportRequestBean> list = new ArrayList<ReportRequestBean>();
		while (rs.next()) {
			list.add(loadSingle(rs));
		}
		return list;
	}

	@Override
	public ReportRequestBean loadSingle(ResultSet rs) throws SQLException {
		ReportRequestBean b = new ReportRequestBean();
		b.setID(rs.getLong("ID"));
		b.setRequesterMID(rs.getLong("RequesterMID"));
		b.setPatientMID(rs.getLong("PatientMID"));
		b.setRequestedDate(rs.getTimestamp("RequestedDate"));
		b.setViewedDate(rs.getTimestamp("ViewedDate"));
		b.setStatus(rs.getString("Status"));
		return b;
	}

	@Override
	public PreparedStatement loadParameters(PreparedStatement ps, ReportRequestBean bean) throws SQLException {
		throw new IllegalStateException("unimplemented!");
	}


}
