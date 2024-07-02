package server.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import server.tools.DBConnection;
import server.vo.User;

public class UserDAO {
	public static User get(String userName) throws Exception {
		User user = null;
		Connection con = DBConnection.getConnection();
		String sql = "select * from user where userName=?";
		PreparedStatement pst = con.prepareStatement(sql);
		pst.setString(1, userName);
		ResultSet rs = pst.executeQuery();
		while (rs.next()) {
			user = new User();
			user.setUserName(rs.getString("userName"));
			user.setPassword(rs.getString("password"));
			user.setName(rs.getString("name"));
			user.setRole(rs.getString("role"));
		}
		DBConnection.close(con, pst);
		return user;
	}

	public static ArrayList<User> query(User userCondition) throws Exception {
		ArrayList<User> list = new ArrayList<User>();
		Connection con = DBConnection.getConnection();
		String sql = "*******";
		PreparedStatement pst = con.prepareStatement(sql);
		ResultSet rs = pst.executeQuery();
		while (rs.next()) {
			User user = new User();
			user.setUserName(rs.getString("userName"));
			user.setPassword(rs.getString("password"));
			user.setName(rs.getString("name"));
			user.setRole(rs.getString("role"));
			list.add(user);
		}
		DBConnection.close(con, pst);
		return list;
	}
}
