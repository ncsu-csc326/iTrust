package edu.ncsu.csc.itrust.model.old.beans.loaders;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import edu.ncsu.csc.itrust.model.old.beans.WardRoomBean;

public class WardRoomBeanLoader implements BeanLoader<WardRoomBean> {
	@Override
	public List<WardRoomBean> loadList(ResultSet rs) throws SQLException {
		ArrayList<WardRoomBean> list = new ArrayList<WardRoomBean>();
		while (rs.next()) {
			list.add(loadSingle(rs));
		}
		return list;
	}

	@Override
	public WardRoomBean loadSingle(ResultSet rs) throws SQLException {
		WardRoomBean wardRoom = new WardRoomBean(rs.getLong("RoomID"), rs.getLong("OccupiedBy"), rs.getLong("InWard"), rs.getString("roomName"), rs.getString("Status"));
		return wardRoom;
	}

	@Override
	public PreparedStatement loadParameters(PreparedStatement ps, WardRoomBean bean) throws SQLException {
		throw new IllegalStateException("unimplemented!");
	}
}
