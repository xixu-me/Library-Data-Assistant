package server.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import server.tools.DBConnection;
import server.vo.User;

public class UserDAO {
	public static User get(String userName) throws Exception {
		String sql = "SELECT * FROM user WHERE userName=?";
		try (Connection con = DBConnection.getConnection();
				PreparedStatement pst = con.prepareStatement(sql)) {
			pst.setString(1, userName);
			try (ResultSet rs = pst.executeQuery()) {
				if (rs.next())
					return createUserFromResultSet(rs);
			}
		}
		return null;
	}

	public static ArrayList<User> query(User userCondition) throws Exception {
		ArrayList<User> list = new ArrayList<>();
		String sql = "SELECT * FROM user"; // 示例SQL，实际应根据userCondition调整
		try (Connection con = DBConnection.getConnection();
				PreparedStatement pst = con.prepareStatement(sql);
				ResultSet rs = pst.executeQuery()) {
			while (rs.next())
				list.add(createUserFromResultSet(rs));
		}
		return list;
	}

	private static User createUserFromResultSet(ResultSet rs) throws Exception {
		User user = new User();
		user.setUserName(rs.getString("userName"));
		user.setPassword(rs.getString("password"));
		user.setName(rs.getString("name"));
		user.setRole(rs.getString("role"));
		return user;
	}
}
