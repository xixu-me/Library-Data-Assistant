package server.tools;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class DBConnection {
	private String driverName;
	private String url;
	private String user;
	private String password;

	// 加载驱动
	static {

	}

	public static Connection getConnection() {

		return null;
	}

	public static void close(Connection con, PreparedStatement pst) {

	}

}
