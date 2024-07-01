package server.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import server.tools.DBConnection;
import server.vo.User;

public class UserDAO {
	// 按关键字进行查询，返回查询结果（vo对象，因为查询结果最多一条），没有查询到，则返回null
	public static User get(String userName) throws Exception {
		User user = null;
		// 获取连接
		Connection con = DBConnection.getConnection();
		// 操作对应sql语句，支持参数
		String sql = "select * from user where userName=?";
		// 创建语句对象
		PreparedStatement pst = con.prepareStatement(sql);
		// 对sql语句的？参数进行赋值
		pst.setString(1, userName);
		// 执行select查询语句，如果sql语句是insert，update或delete，则需调用executeUpdate进行执行
		ResultSet rs = pst.executeQuery();
		// 将记录集转换为vo对象
		while (rs.next()) {
			user = new User();
			user.setUserName(rs.getString("userName"));
			user.setPassword(rs.getString("password"));
			user.setName(rs.getString("name"));
			user.setRole(rs.getString("role"));
		}
		// 关闭连接对象
		DBConnection.close(con, pst);
		return user;
	}

	// 按任意条件组合查询，将查询结果存放在集合中【因为可能有多条】
	public static ArrayList<User> query(User userCondition) throws Exception {
		ArrayList<User> list = new ArrayList<User>();
		// 获取连接
		Connection con = DBConnection.getConnection();
		// 操作对应sql语句
		String sql = "*******";
		// 创建语句对象
		PreparedStatement pst = con.prepareStatement(sql);
		// 执行select查询语句，如果sql语句是insert，update或delete，则需调用executeUpdate进行执行
		ResultSet rs = pst.executeQuery();
		// 将记录集转换为vo对象
		while (rs.next()) {
			User user = new User();
			user.setUserName(rs.getString("userName"));
			user.setPassword(rs.getString("password"));
			user.setName(rs.getString("name"));
			user.setRole(rs.getString("role"));
			list.add(user); // 将对象增加到集合中
		}
		// 关闭连接对象
		DBConnection.close(con, pst);

		return list;
	}

}
