package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import server.vo.User;

public class Client {

	// 多个函数共享的数据可以定义为全局的
	public static Scanner scan = new Scanner(System.in); // 控制台输入
	public static Socket socket; // 建立连接后的套接字对象
	public static BufferedReader buf; // 字符输入流
	public static PrintWriter write; // 字符输出流
	public static User currentUser; // 当前登录用户

	// 主方法
	public static void main(String[] args) throws Exception, IOException {
		String ip = "127.0.0.1"; // 服务器IP
		int port = 9999; // 服务器程序的端口
		// 1.建立与服务器的连接
		socket = new Socket(ip, port);
		// 获取输入流
		InputStream is = socket.getInputStream();
		buf = new BufferedReader(new InputStreamReader(is, "utf-8"));
		// 获取输出流
		OutputStream os = socket.getOutputStream();
		write = new PrintWriter(new OutputStreamWriter(os, "utf-8"));
		// 2.用户登录
		if (!login()) { // 登录失败，退出程序
			System.out.println("登录失败，欢迎下次访问");
			System.exit(0);
		}
		// 登录成功，显示程序主界面
		int choose = menu();
		while (choose != 8) { // 不是退出
			switch (choose) {
				case 1: // 修改密码
					updatepassword();
					break;
				case 2:// 修改密码

					break;
				case 3:// 增加数据：显示二级菜单

					break;
				default:
					break;
			}
			choose = menu(); // 执行完毕，重新显示主菜单，并选择
		}
		// 输入选择=8，退出
		close();
		System.out.println("您已退出程序，欢迎下次使用！");
	}

	// 选择退出时，先向服务器发送“退出请求：code=8”，然后关闭连接
	public static void close() throws Exception {
		Map<String, Object> map = new HashMap<>();
		map.put("code", 8);
		String sendData = new Gson().toJson(map); // 将java的map对象直接转换成json字符串
		System.out.println("发送给服务器：" + sendData);
		write.println(sendData);
		write.flush();
		if (write != null) {
			write.close();
		}
		if (buf != null) {
			buf.close();
		}
		if (socket != null) {
			socket.close();
		}
	}

	// 绘制流程图，按流程图书写
	public static boolean login() throws Exception {
		for (int i = 1; i <= 3; i++) {
			// 1.输入用户名，密码：所有输入均应该先给输入提示
			System.out.println("请输入用户名:");
			String userName = scan.nextLine();
			System.out.println("请输入密码:");
			String passsord = scan.nextLine();
			// 可以使用GSON包非常方便的将java类型的数据转成json字符串，或者从json字符串中获取相关的数据,见教案p129
			// 封装发送的数据：如登录请求的数据格式可以为"{code:0,data:{userName:***,password:***}}"
			Map<String, Object> map = new HashMap<>();
			map.put("code", 0);
			User user = new User();
			user.setUserName(userName);
			user.setPassword(passsord);
			Gson gson = new Gson();
			map.put("data", gson.toJson(user)); // toJson方法可以将java的vo对象直接转换成json字符串
			String sendData = gson.toJson(map); // 将java的map对象直接转换成json字符串
			System.out.println("发送给服务器：" + sendData);
			/**
			 * 2.发送数据并接收服务器返回的数据（返回数据也有格式，如可以是以下格式:
			 * code:0或1，data:****
			 * code=0表示登录失败，data中存放失败原因
			 * code=1表示登录成功，data中放当前用户相关的信息（用户名，姓名，角色等）
			 */
			String responseData = send(sendData);
			System.out.println("接收到服务器端的响应：" + responseData);
			// 4.解析返回的字符串
			JsonElement element = JsonParser.parseString(responseData); // 使用parseString方法将json字符串转换为JsonElement对象
			JsonObject obj = element.getAsJsonObject(); // 将JsonElement对象转换为JsonObject对象
			int returnCode = obj.get("code").getAsInt(); // 获取code属性
			String returnData = obj.get("data").getAsString(); // 获取data属性
			if (returnCode == 0) { // 登录失败
				System.out.println("登录失败：" + returnData);
			} else { // 登录成功，returnData中存放的是当前用户的json字符串
				currentUser = gson.fromJson(returnData, User.class); // 使用fromJson方法可以将json字符串直接转换为指定类型的对象
				return true;
			}

		}

		return false;

	}

	// 显示操作界面，返回用户的选择1，2，.....
	public static int menu() {
		System.out.println("======新闻管理系统=======");
		System.out.println("作者:****&******");
		System.out.println("当前用户：" + currentUser.getName());
		System.out.println("1.密码修改");
		System.out.println("2.增加数据"); // 需要增加二级菜单：【键盘录入，网页爬取，txt文件导入，xls文件导入】
		System.out.println("3.修改数据"); // 按key修改其他字段
		System.out.println("4.删除数据"); // 按key删除记录
		System.out.println("5.查询数据"); // 需要增加二级菜单，【自动设计查询条件】
		System.out.println("6.统计数据"); // 需要增加二级菜单，【自行设计统计条件】
		System.out.println("7.导出数据至文件"); // 需要增加二级菜单：【导出至txt，导出至xls】
		System.out.println("8.退出");

		System.out.println("请输入选项(1-8):");
		int choose = scan.nextInt();

		return choose;
	}

	// 将指定的字符串发送给服务器，并接收服务器返回的数据
	public static String send(String data) throws Exception {
		// 发送数据
		write.println(data);
		write.flush();
		// 接收数据
		String response = buf.readLine();
		return response;
	}

	// 修改密码
	public static void updatepassword() {

	}

	// 从键盘输入
	public static void inputFromKeyboard() {

	}

	// 将电子表格文件编码为base64形式的字符串进行传输
	public static void importFromXLS() {

	}
}
