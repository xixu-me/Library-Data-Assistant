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
				System.out.println("Receive the client's request data: " + receiveStr);
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
						System.out.println("Invalid command!");
						break;
				}
				write.println(response);
				write.flush();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		System.out.println("Client exited!");
	}

	public void close() throws Exception {
		if (write != null)
			write.close();
		if (buf != null)
			buf.close();
		if (socket != null)
			socket.close();
	}

	public String loginHandle(String str) throws Exception {
		User loginUser = new Gson().fromJson(str, User.class);
		User user = UserDAO.get(loginUser.getUserName());
		String responseData = "";
		if (user == null) {
			Map<String, Object> map = new HashMap<>();
			map.put("code", 0);
			map.put("data", "Username does not exist!");
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
				map.put("data", "Wrong password!");
				responseData = new Gson().toJson(map);
			}
		}
		return responseData;
	}
}
