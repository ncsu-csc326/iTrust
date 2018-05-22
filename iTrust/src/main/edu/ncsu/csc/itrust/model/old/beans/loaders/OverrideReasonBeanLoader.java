package edu.ncsu.csc.itrust.model.old.beans.loaders;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import edu.ncsu.csc.itrust.model.old.beans.OverrideReasonBean;

/**
 * A loader for MedicationBeans.
 * 
 * Loads in information to/from beans using ResultSets and PreparedStatements. Use the superclass to enforce consistency. 
 * For details on the paradigm for a loader (and what its methods do), see {@link BeanLoader}
 */
public class OverrideReasonBeanLoader implements BeanLoader<OverrideReasonBean> {
	public OverrideReasonBeanLoader() {
	}

	
	@Override
	public List<OverrideReasonBean> loadList(ResultSet rs) throws SQLException {
		ArrayList<OverrideReasonBean> list = new ArrayList<OverrideReasonBean>();
		while (rs.next()) {
			list.add(loadSingle(rs));
		}
		return list;
	}

	@Override
	public OverrideReasonBean loadSingle(ResultSet rs) throws SQLException {
		OverrideReasonBean reason = new OverrideReasonBean();
		reason.setORCode(rs.getString("OverrideCode"));
		return reason;
	}

	@Override
	public PreparedStatement loadParameters(PreparedStatement ps, OverrideReasonBean bean) throws SQLException {
		throw new IllegalStateException("unimplemented!");
	}
}
