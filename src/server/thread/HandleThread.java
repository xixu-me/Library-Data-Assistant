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
import server.vo.User;

public class HandleThread extends Thread {
	private Socket socket;
	private BufferedReader buf;
	private PrintWriter write;

	public HandleThread(Socket socket) {
		super();
		this.socket = socket;
		try {

			InputStream is = socket.getInputStream();
			buf = new BufferedReader(new InputStreamReader(is, "utf-8"));

			OutputStream os = socket.getOutputStream();
			write = new PrintWriter(new OutputStreamWriter(os, "utf-8"));
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	public void run() {
		while (true) {
			try {

				String receiveStr = buf.readLine();
				System.out.println("接收到客户端的请求数据:" + receiveStr);

				JsonElement element = JsonParser.parseString(receiveStr);
				JsonObject obj = element.getAsJsonObject();
				int code = obj.get("code").getAsInt();
				if (code == 8) {
					close();
					break;
				}
				String data = obj.get("data").getAsString();
				String response = "";
				switch (code) {
					case 0:
						response = loginHandle(data);
						break;
					case 1:

						break;
					case 2:
						break;
					default:
						System.out.println("命令无效！");
						break;
				}

				write.println(response);
				write.flush();

			} catch (Exception e) {
				e.printStackTrace();
			}

		}

		System.out.println("客户端退出！");

	}

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

		User loginUser = new Gson().fromJson(str, User.class);

		User user = UserDAO.get(loginUser.getUserName());
		/**
		 * 根据dao执行结果，给出相应的响应，需要设计响应字符串的协议格式 如可以这样设计：*{code:0
		 * 1，data:****}
		 * code=0表示登录失败，data中存放失败原因 code=
		 * 表示登录成功，data中放当前用户相关的信息（用户名，姓名，角色等）
		 */
		String responseData = "";
		if (user == null) {
			Map<String, Object> map = new HashMap<>();
			map.put("code", 0);
			map.put("data", "用户名不存在");
			responseData = new Gson().toJson(map);
		} else {
			if (loginUser.getPassword().equals(user.getPassword())) {
				Map<String, Object> map = new HashMap<>();
				map.put("code", 1);
				map.put("data", new Gson().toJson(user));
				responseData = new Gson().toJson(map);
			} else {
				Map<String, Object> map = new HashMap<>();
				map.put("code", 0);
				map.put("data", "密码错误");
				responseData = new Gson().toJson(map);
			}
		}
		return responseData;
	}

}
