package server.thread;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import server.dao.UserDAO;
import server.tools.Encoding;
import server.vo.User;

public class HandleThread extends Thread {
	private Socket socket;
	private BufferedReader buf; // 字符输入流
	private PrintWriter write; // 字符输出流

	// 构造方法
	public HandleThread(Socket socket) {
		super();
		this.socket = socket;
		try {
			// 获取输入流
			InputStream is = socket.getInputStream();
			buf = new BufferedReader(new InputStreamReader(is, "utf-8"));
			// 获取输出流
			OutputStream os = socket.getOutputStream();
			write = new PrintWriter(new OutputStreamWriter(os, "utf-8"));
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	// 具体的请求处理：线程执行start方法时自动调用
	@Override
	public void run() {
		while (true) { // 循环接收客户端发送过来的数据
			try {
				// 接收客户端发送的json字符串
				String receiveStr = buf.readLine();
				System.out.println("接收到客户端的请求数据:" + receiveStr);
				// 按协议解析字符串，如：code = 0，表示的是登录请求
				JsonElement element = JsonParser.parseString(receiveStr); // 使用parseString方法将json字符串转换为JsonElement对象
				JsonObject obj = element.getAsJsonObject(); // 将JsonElement对象转换为JsonObject对象
				int code = obj.get("code").getAsInt(); // 获取code属性
				if (code == 8) { // 客户端退出
					close();
					break;
				}
				String data = obj.get("data").getAsString(); // 获取data属性
				String response = "";
				switch (code) {
					case 0: // 登录请求
						response = loginHandle(data); // 登录请求处理，data中存放的是输入的用户名和密码
						break;
					case 1:

						break;
					case 2:
						break;
					default:
						System.out.println("命令无效！");
						break;
				}
				// 向客户端发送响应数据
				write.println(response);
				write.flush();

			} catch (Exception e) {
				e.printStackTrace();
			}

		}

		System.out.println("客户端退出！");

	}

	// 客户端选择退出时，关闭连接
	public void close() throws Exception {
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

	public String loginHandle(String str) throws Exception {
		// 解析json字符串，str中的存放的是输入的用户名，密码信息的json格式字符串,使用Gson包将其直接转换为User对象
		User loginUser = new Gson().fromJson(str, User.class);
		// 调用DAO，按用户名查询数据库，返回查询结果
		User user = UserDAO.get(loginUser.getName());
		/**
		 * 根据dao执行结果，给出相应的响应，需要设计响应字符串的协议格式 如可以这样设计：*{code:0
		 * 1，data:****}
		 * code=0表示登录失败，data中存放失败原因 code=
		 * 表示登录成功，data中放当前用户相关的信息（用户名，姓名，角色等）
		 */
		String responseData = "";
		if (user == null) { // 登录失败，原因：表示的用户不存在
			Map<String, Object> map = new HashMap<>();
			map.put("code", 0);
			map.put("data", "用户名不存在");
			responseData = new Gson().toJson(map); // 将java的map对象直接转换成json字符串
		} else { // 有该用户，进一步密码是否正确
			if (Encoding.md5(loginUser.getPassword()).equals(user.getPassword())) { // 用户名密码正确
				Map<String, Object> map = new HashMap<>();
				map.put("code", 1);
				map.put("data", new Gson().toJson(user)); // 使用toJson方法可以将java的vo对象直接转换成json字符串
				responseData = new Gson().toJson(map); // 将java的map对象直接转换成json字符串
			} else { // 密码不正确
				Map<String, Object> map = new HashMap<>();
				map.put("code", 0);
				map.put("data", "密码错误");
				responseData = new Gson().toJson(map); // 将java的map对象直接转换成json字符串
			}
		}
		return responseData;
	}

}
