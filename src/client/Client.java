package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import server.tools.DBConnection;
import server.tools.ExportToCSV;
import server.tools.ExportToXLS;
import server.tools.crawling.Book;
import server.tools.crawling.Driver;
import server.vo.User;

public class Client {
	public static Scanner scan = new Scanner(System.in);
	public static Socket socket;
	public static BufferedReader buf;
	public static PrintWriter write;
	public static User currentUser;
	public static Connection con = null;
	public static PreparedStatement ps = null;
	public static Statement statement = null;
	public static ResultSet rs = null;

	public static void main(String[] args) throws Exception, IOException {
		String ip = "127.0.0.1";
		int port = 9999;
		socket = new Socket(ip, port);
		InputStream is = socket.getInputStream();
		buf = new BufferedReader(new InputStreamReader(is, "utf-8"));
		OutputStream os = socket.getOutputStream();
		write = new PrintWriter(new OutputStreamWriter(os, "utf-8"));
		if (!login()) {
			System.out.println("登录失败，欢迎下次访问");
			System.exit(0);
		}
		int choose = menu();
		while (choose != 8) {
			switch (choose) {
				case 1:
					display();
					break;
				case 2:
					Driver.crawl();
					break;
				case 3:
					query();
					break;
				case 4:
					delete();
					break;
				case 5:
					update();
					break;
				case 6:
					ExportToCSV.to();
					break;
				case 7:
					ExportToXLS.to();
					break;
				default:
					break;
			}
			choose = menu();
		}
		close();
		System.out.println("您已退出程序，欢迎下次使用！");
	}

	public static void close() throws Exception {
		Map<String, Object> map = new HashMap<>();
		map.put("code", 8);
		String sendData = new Gson().toJson(map);
		System.out.println("发送给服务器：" + sendData);
		write.println(sendData);
		write.flush();
		if (write != null)
			write.close();
		if (buf != null)
			buf.close();
		if (socket != null)
			socket.close();
	}

	public static boolean login() throws Exception {
		for (int i = 1; i <= 3; i++) {
			System.out.println("请输入用户名:");
			String userName = scan.nextLine();
			System.out.println("请输入密码:");
			String passsord = scan.nextLine();
			Map<String, Object> map = new HashMap<>();
			map.put("code", 0);
			User user = new User();
			user.setUserName(userName);
			user.setPassword(passsord);
			Gson gson = new Gson();
			map.put("data", gson.toJson(user));
			String sendData = gson.toJson(map);
			System.out.println("发送给服务器：" + sendData);
			String responseData = send(sendData);
			System.out.println("接收到服务器端的响应：" + responseData);
			JsonElement element = JsonParser.parseString(responseData);
			JsonObject obj = element.getAsJsonObject();
			int returnCode = obj.get("code").getAsInt();
			String returnData = obj.get("data").getAsString();
			if (returnCode == 0)
				System.out.println("登录失败：" + returnData);
			else {
				currentUser = gson.fromJson(returnData, User.class);
				return true;
			}
		}
		return false;
	}

	public static int menu() {
		System.out.println("======图书数据助理系统=======");
		System.out.println("当前用户：" + currentUser.getName());
		System.out.println("1.图书信息显示");
		System.out.println("2.当当网站爬取");
		System.out.println("3.查询");
		System.out.println("4.删除");
		System.out.println("5.修改");
		System.out.println("6.导出到csv文件");
		System.out.println("7.导出到xls文件");
		System.out.println("8.退出");
		System.out.println("请输入选项(1-8):");
		int choose = scan.nextInt();
		return choose;
	}

	public static String send(String data) throws Exception {
		write.println(data);
		write.flush();
		String response = buf.readLine();
		return response;
	}

	public static void display() {
		con = DBConnection.getConnection();
		String sql = "select * from book limit 10";
		try {
			statement = con.createStatement();
			rs = statement.executeQuery(sql);
			while (rs.next()) {
				Book book = new Book();
				book.setTitle(rs.getString(1));
				book.setAuthor(rs.getString(2));
				book.setPublisher(rs.getString(3));
				book.setOldprice(rs.getDouble(4));
				book.setNewprice(rs.getDouble(5));
				book.setHref(rs.getString(6));
				System.out.println(book.toString());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (rs != null)
			try {
				rs.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		if (statement != null)
			try {
				statement.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		DBConnection.close(con, ps);
	}

	public static void query() {
		String sql = null;
		System.out.println("======查询图书=======");
		System.out.println("1.按名称查询");
		System.out.println("2.按作者查询");
		System.out.println("3.按出版社查询");
		System.out.println("4.按价格查询");
		System.out.println("5.退出");
		System.out.println("请输入选项(1-5):");
		int n = 0;
		n = scan.nextInt();
		switch (n) {
			case 1:
				sql = "select * from book where title like ?";
				System.out.println("请输入名称:");
				outputQuery(sql);
				break;
			case 2:
				sql = "select * from book where author like ?";
				System.out.println("请输入作者:");
				outputQuery(sql);
				break;
			case 3:
				sql = "select * from book where publisher like ?";
				System.out.println("请输入出版社:");
				outputQuery(sql);
				break;
			case 4:
				sql = "select * from book where newprice like ?";
				System.out.println("请输入价格:");
				outputQuery(sql);
				break;
			default:
				break;
		}
	}

	public static void outputQuery(String sql) {
		con = DBConnection.getConnection();
		String title, author, pubilsher, oldprice, newprice, url;
		String inquire = scan.next();
		try {
			ps = con.prepareStatement(sql);
			ps.setString(1, '%' + inquire + '%');
			rs = ps.executeQuery();
			while (rs.next()) {
				title = rs.getString(1);
				author = rs.getString(2);
				pubilsher = rs.getString(3);
				oldprice = rs.getString(4);
				newprice = rs.getString(5);
				url = rs.getString(6);
				System.out.println("书名:" + title);
				System.out.println("作者:" + author);
				System.out.println("出版社:" + pubilsher);
				System.out.println("原价格:" + oldprice);
				System.out.println("折后价格:" + newprice);
				System.out.println("详情地址:" + url);
			}
			rs.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		DBConnection.close(con, ps);
	}

	public static void delete() {
		con = DBConnection.getConnection();
		System.out.println("请输入书名");
		String bookname = scan.next();
		String sql = "delete from book where title like ?";
		try {
			ps = con.prepareStatement(sql);
			ps.setString(1, '%' + bookname + '%');
			ps.executeUpdate();
			System.out.println("删除成功");
		} catch (Exception e) {
			System.err.println("删除失败");
		}
		DBConnection.close(con, ps);
	}

	public static void update() {
		con = DBConnection.getConnection();
		String sql = null;
		System.out.println("======修改图书=======");
		System.out.println("1.修改作者姓名");
		System.out.println("2.修改出版社");
		System.out.println("3.修改价格");
		System.out.println("4.退出");
		System.out.println("请输入需要修改的图书的名称");
		String title = scan.next();
		System.out.println("请输入选项(1-4):");
		int n = 0;
		n = scan.nextInt();
		String name = null;
		switch (n) {
			case 1:
				System.out.println("请输入修改后的作责姓名");
				name = scan.next();
				sql = "update book set author = ? where title = ?";
				try {
					ps = con.prepareStatement(sql);
					ps.setString(1, name);
					ps.setString(2, title);
					ps.executeUpdate();
				} catch (Exception e) {
					e.printStackTrace();
				}
				break;
			case 2:
				System.out.println("请输入修改后的出版社名");
				name = scan.next();
				sql = "update book set publisher = ? where title = ?";
				try {
					ps = con.prepareStatement(sql);
					ps.setString(1, name);
					ps.setString(2, title);
					ps.executeUpdate();
				} catch (Exception e) {
					e.printStackTrace();
				}
				break;
			case 3:
				System.out.println("请输入修改后的价格");
				name = scan.next();
				sql = "update book set oldprice = newprice where title = ?";
				String sql2 = "update book set newprice = ? where title = ?";
				try {
					ps = con.prepareStatement(sql);
					ps.setString(1, title);
					ps.executeUpdate();
					ps = con.prepareStatement(sql2);
					ps.setString(1, name);
					ps.setString(2, title);
					ps.executeUpdate();
				} catch (Exception e) {
					e.printStackTrace();
				}
				break;
			default:
				break;
		}
		DBConnection.close(con, ps);
	}
}
