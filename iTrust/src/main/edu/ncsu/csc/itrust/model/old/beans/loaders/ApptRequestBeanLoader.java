package edu.ncsu.csc.itrust.model.old.beans.loaders;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import edu.ncsu.csc.itrust.model.old.beans.ApptBean;
import edu.ncsu.csc.itrust.model.old.beans.ApptRequestBean;

/**
 * 
 *
 */
public class ApptRequestBeanLoader implements BeanLoader<ApptRequestBean> {

	private ApptBeanLoader loader = new ApptBeanLoader();

	/**
	 * 
	 */
	@Override
	public List<ApptRequestBean> loadList(ResultSet rs) throws SQLException {
		List<ApptRequestBean> list = new ArrayList<ApptRequestBean>();
		while (rs.next()) {
			list.add(loadSingle(rs));
		}
		return list;
	}

	/**
	 * 
	 */
	@Override
	public ApptRequestBean loadSingle(ResultSet rs) throws SQLException {
		ApptRequestBean bean = new ApptRequestBean();
		ApptBean appt = loader.loadSingle(rs);
		bean.setRequestedAppt(appt);
		bean.setPending(rs.getBoolean("pending"));
		bean.setAccepted(rs.getBoolean("accepted"));
		return bean;
	}

	/**
	 * 
	 */
	@Override
	public PreparedStatement loadParameters(PreparedStatement ps, ApptRequestBean bean) throws SQLException {
		PreparedStatement ps2 = loader.loadParameters(ps, bean.getRequestedAppt());
		ps2.setBoolean(6, bean.isPending());
		ps2.setBoolean(7, bean.isAccepted());
		return ps2;
	}

}
