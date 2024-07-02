package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
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

	public static void main(String[] args) {
		try {
			setupConnection();
			if (!login()) {
				System.out.println("Login failed. Welcome to visit next time!");
				System.exit(0);
			}
			handleUserChoices();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			close();
		}
	}

	private static void setupConnection() throws IOException {
		String ip = "localhost";
		int port = 9999;
		socket = new Socket(ip, port);
		buf = new BufferedReader(new InputStreamReader(socket.getInputStream(), "utf-8"));
		write = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), "utf-8"), true);
	}

	private static void handleUserChoices() throws Exception {
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
					ExportToCSV.to("output.csv", "select * from book");
					break;
				case 7:
					ExportToXLS.to("select * from book", "output.xls");
					break;
				default:
					break;
			}
			choose = menu();
		}
		System.out.println("You have exited the assistant. Welcome to visit next time!");
	}

	public static void close() {
		try {
			Map<String, Object> map = new HashMap<>();
			map.put("code", 8);
			write.println(new Gson().toJson(map));
			if (socket != null)
				socket.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static boolean login() throws Exception {
		for (int i = 1; i <= 3; i++) {
			User user = getUserCredentials();
			Map<String, Object> map = new HashMap<>();
			map.put("code", 0);
			map.put("data", new Gson().toJson(user));
			String sendData = new Gson().toJson(map);
			String responseData = send(sendData);
			JsonObject obj = JsonParser.parseString(responseData).getAsJsonObject();
			if (obj.get("code").getAsInt() == 0)
				System.out.println("Login failed: " + obj.get("data").getAsString());
			else {
				currentUser = new Gson().fromJson(obj.get("data").getAsString(), User.class);
				return true;
			}
		}
		return false;
	}

	private static User getUserCredentials() {
		System.out.println("Please enter your username:");
		String userName = scan.nextLine();
		System.out.println("Please enter your password:");
		String password = scan.nextLine();
		return new User(userName, password);
	}

	public static String send(String data) throws Exception {
		write.println(data);
		return buf.readLine();
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

	private static void openConnection() {
		con = DBConnection.getConnection();
	}

	private static void closeResources() {
		try {
			if (rs != null)
				rs.close();
			if (ps != null)
				ps.close();
			if (statement != null)
				statement.close();
			DBConnection.close(con);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void display() {
		openConnection();
		String sql = "SELECT * FROM book LIMIT 10";
		try {
			statement = con.createStatement();
			rs = statement.executeQuery(sql);
			while (rs.next()) {
				Book book = new Book(rs.getString(1), rs.getString(2), rs.getString(3), rs.getDouble(4),
						rs.getDouble(5), rs.getString(6));
				System.out.println(book);
			}
			System.out.println("Displaying ended!");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeResources();
		}
	}

	public static void query() {
		openConnection();
		try {
			System.out.println("====== Querying library data =======");
			System.out.println("1. Querying by title;");
			System.out.println("2. Querying by author;");
			System.out.println("3. Querying by publisher;");
			System.out.println("4. Querying by original price;");
			System.out.println("5. Return to previous menu.");
			System.out.println("Please enter options (1-5):");
			int n = scan.nextInt();
			scan.nextLine();
			String sql = "";
			switch (n) {
				case 1:
					sql = "SELECT * FROM book WHERE title = ?";
					System.out.println("Please enter the title:");
					break;
				case 2:
					sql = "SELECT * FROM book WHERE author = ?";
					System.out.println("Please enter the author:");
					break;
				case 3:
					sql = "SELECT * FROM book WHERE publisher = ?";
					System.out.println("Please enter the publisher:");
					break;
				case 4:
					sql = "SELECT * FROM book WHERE oldprice = ?";
					System.out.println("Please enter the price:");
					break;
				default:
					System.out.println("Returning to previous menu.");
					break;
			}
			if (!sql.isEmpty()) {
				ps = con.prepareStatement(sql);
				ps.setString(1, scan.nextLine());
				rs = ps.executeQuery();
				while (rs.next()) {
					String title = rs.getString(1);
					String author = rs.getString(2);
					String pubilsher = rs.getString(3);
					String oldprice = rs.getString(4);
					String newprice = rs.getString(5);
					String url = rs.getString(6);
					System.out.println("Title: " + title + ", Author: " + author + ", Publisher: " + pubilsher
							+ ", Original Price: " + oldprice + ", Discounted Price: " + newprice + ", URL: " + url);
				}
				System.out.println("Querying ended!");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeResources();
		}
	}

	public static void delete() {
		openConnection();
		System.out.println("Please enter the title:");
		String sql = "delete from book where title = ?";
		try {
			ps = con.prepareStatement(sql);
			ps.setString(1, scan.nextLine());
			int affectedRows = ps.executeUpdate();
			if (affectedRows > 0) {
				System.out.println("Delete successfully!");
			} else {
				System.out.println("No record found to delete!");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeResources();
		}
	}

	public static void update() {
		try {
			openConnection();
			System.out.println("Please enter the title:");
			String title = scan.nextLine();
			System.out.println("====== Modifying library data =======");
			System.out.println("1. Modifying author;");
			System.out.println("2. Modifying publisher;");
			System.out.println("3. Modifying price;");
			System.out.println("4. Return to previous menu.");
			System.out.println("Please enter options (1-4):");
			int choice = scan.nextInt();
			scan.nextLine();
			switch (choice) {
				case 1:
					updateField("author", title);
					break;
				case 2:
					updateField("publisher", title);
					break;
				case 3:
					updatePrice(title);
					break;
				default:
					System.out.println("Returning to previous menu.");
					break;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeResources();
		}
	}

	private static void updateField(String field, String title) throws Exception {
		System.out.println("Please enter the modified " + field + ":");
		String newValue = scan.nextLine();
		String sql = "UPDATE book SET " + field + " = ? WHERE title = ?";
		ps = con.prepareStatement(sql);
		ps.setString(1, newValue);
		ps.setString(2, title);
		int affectedRows = ps.executeUpdate();
		if (affectedRows > 0)
			System.out.println("Update successful!");
		else
			System.out.println("No record found to update.");
	}

	private static void updatePrice(String title) throws Exception {
		System.out.println("Please enter the modified price:");
		String newPrice = scan.nextLine();
		String sqlUpdateOldPrice = "UPDATE book SET oldprice = newprice WHERE title = ?";
		ps = con.prepareStatement(sqlUpdateOldPrice);
		ps.setString(1, title);
		ps.executeUpdate();
		String sqlUpdateNewPrice = "UPDATE book SET newprice = ? WHERE title = ?";
		ps = con.prepareStatement(sqlUpdateNewPrice);
		ps.setString(1, newPrice);
		ps.setString(2, title);
		int affectedRows = ps.executeUpdate();
		if (affectedRows > 0) {
			System.out.println("Price update successful!");
		} else {
			System.out.println("No record found to update price!");
		}
	}
}
