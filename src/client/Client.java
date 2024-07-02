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
			System.out.println("Login failed, welcome to visit next time!");
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
		System.out.println("You have exited the program. Welcome to use it next time!");
	}

	public static void close() throws Exception {
		Map<String, Object> map = new HashMap<>();
		map.put("code", 8);
		String sendData = new Gson().toJson(map);
		System.out.println("Send to server: " + sendData);
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
			System.out.println("Please enter your user name:");
			String userName = scan.nextLine();
			System.out.println("Please enter your password:");
			String passsord = scan.nextLine();
			Map<String, Object> map = new HashMap<>();
			map.put("code", 0);
			User user = new User();
			user.setUserName(userName);
			user.setPassword(passsord);
			Gson gson = new Gson();
			map.put("data", gson.toJson(user));
			String sendData = gson.toJson(map);
			System.out.println("Send to server: " + sendData);
			String responseData = send(sendData);
			System.out.println("Received the response from the server: " + responseData);
			JsonElement element = JsonParser.parseString(responseData);
			JsonObject obj = element.getAsJsonObject();
			int returnCode = obj.get("code").getAsInt();
			String returnData = obj.get("data").getAsString();
			if (returnCode == 0)
				System.out.println("Login failed: " + returnData);
			else {
				currentUser = gson.fromJson(returnData, User.class);
				return true;
			}
		}
		return false;
	}

	public static int menu() {
		System.out.println("======== Library Data Assistant ========");
		System.out.println("Current user: " + currentUser.getName());
		System.out.println("1. Display library data;");
		System.out.println("2. Crawling library data;");
		System.out.println("3. Querying library data;");
		System.out.println("4. Deleting library data;");
		System.out.println("5. Modifying library data;");
		System.out.println("6. Exporting library data to a CSV file;");
		System.out.println("7. Exporting library data to an XLS file;");
		System.out.println("8. Exiting Assistant.");
		System.out.println("Please enter options (1-8):");
		int choose = scan.nextInt();
		scan.nextLine();
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
		System.out.println("====== Querying library data =======");
		System.out.println("1. Querying by title;");
		System.out.println("2. Querying by author;");
		System.out.println("3. Querying by publisher;");
		System.out.println("4. Querying by price;");
		System.out.println("5. Return to previous menu.");
		System.out.println("Please enter options (1-5):");
		int n = 0;
		n = scan.nextInt();
		scan.nextLine();
		switch (n) {
			case 1:
				sql = "select * from book where title = ?";
				System.out.println("Please enter the title:");
				outputQuery(sql);
				break;
			case 2:
				sql = "select * from book where author = ?";
				System.out.println("Please enter the author:");
				outputQuery(sql);
				break;
			case 3:
				sql = "select * from book where publisher = ?";
				System.out.println("Please enter the publisher:");
				outputQuery(sql);
				break;
			case 4:
				sql = "select * from book where newprice = ?";
				System.out.println("Please enter the price:");
				outputQuery(sql);
				break;
			default:
				break;
		}
	}

	public static void outputQuery(String sql) {
		con = DBConnection.getConnection();
		String title, author, pubilsher, oldprice, newprice, url;
		String inquire = scan.nextLine();
		try {
			ps = con.prepareStatement(sql);
			ps.setString(1, inquire);
			rs = ps.executeQuery();
			while (rs.next()) {
				title = rs.getString(1);
				author = rs.getString(2);
				pubilsher = rs.getString(3);
				oldprice = rs.getString(4);
				newprice = rs.getString(5);
				url = rs.getString(6);
				System.out.println("Title: " + title + ", Author: " + author + ", Publisher: " + pubilsher
						+ ", Original Price: " + oldprice + ", Discounted Price: " + newprice + ", URL: " + url);
			}
			rs.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		DBConnection.close(con, ps);
	}

	public static void delete() {
		con = DBConnection.getConnection();
		System.out.println("Please enter the title:");
		String title = scan.nextLine();
		String sql = "delete from book where title = ?";
		try {
			ps = con.prepareStatement(sql);
			ps.setString(1, title);
			ps.executeUpdate();
			System.out.println("Delete successfully!");
		} catch (Exception e) {
			System.err.println("Delete failed!");
		}
		DBConnection.close(con, ps);
	}

	public static void update() {
		con = DBConnection.getConnection();
		String sql = null;
		System.out.println("Please enter the title:");
		String title = scan.nextLine();
		System.out.println("====== Modifying library data =======");
		System.out.println("1. Modifying author;");
		System.out.println("2. Modifying publisher;");
		System.out.println("3. Modifying price;");
		System.out.println("4. Return to previous menu.");
		System.out.println("Please enter options (1-4):");
		int n = 0;
		n = scan.nextInt();
		scan.nextLine();
		String inquery = null;
		switch (n) {
			case 1:
				System.out.println("Please enter the modified author:");
				inquery = scan.nextLine();
				sql = "update book set author = ? where title = ?";
				try {
					ps = con.prepareStatement(sql);
					ps.setString(1, inquery);
					ps.setString(2, title);
					ps.executeUpdate();
				} catch (Exception e) {
					e.printStackTrace();
				}
				break;
			case 2:
				System.out.println("Please enter the modified publisher:");
				inquery = scan.nextLine();
				sql = "update book set publisher = ? where title = ?";
				try {
					ps = con.prepareStatement(sql);
					ps.setString(1, inquery);
					ps.setString(2, title);
					ps.executeUpdate();
				} catch (Exception e) {
					e.printStackTrace();
				}
				break;
			case 3:
				System.out.println("Please enter the modified price:");
				inquery = scan.nextLine();
				sql = "update book set oldprice = newprice where title = ?";
				String sql2 = "update book set newprice = ? where title = ?";
				try {
					ps = con.prepareStatement(sql);
					ps.setString(1, title);
					ps.executeUpdate();
					ps = con.prepareStatement(sql2);
					ps.setString(1, inquery);
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
