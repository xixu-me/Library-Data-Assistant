package server.thread;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import server.dao.UserDAO;
import server.vo.User;

public class HandleThread extends Thread {
	private Socket socket;

	public HandleThread(Socket socket) {
		this.socket = socket;
	}

	@Override
	public void run() {
		try (BufferedReader buf = new BufferedReader(new InputStreamReader(socket.getInputStream(), "utf-8"));
				PrintWriter write = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), "utf-8"))) {
			String receiveStr;
			while ((receiveStr = buf.readLine()) != null) {
				System.out.println("Receive the client's request data: " + receiveStr);
				JsonObject obj = new Gson().fromJson(receiveStr, JsonObject.class);
				int code = obj.get("code").getAsInt();
				if (code == 8)
					break;
				String data = obj.get("data").getAsString();
				String response = handleRequest(code, data);
				write.println(response);
				write.flush();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			System.out.println("Client exited!");
			closeResources();
		}
	}

	private String handleRequest(int code, String data) throws Exception {
		switch (code) {
			case 0:
				return loginHandle(data);
			case 1:
				// Handle other cases
				return "";
			case 2:
				// Handle other cases
				return "";
			default:
				System.out.println("Invalid command!");
				return "";
		}
	}

	private String loginHandle(String str) throws Exception {
		User loginUser = new Gson().fromJson(str, User.class);
		User user = UserDAO.get(loginUser.getUserName());
		Map<String, Object> map = new HashMap<>();
		if (user == null) {
			map.put("code", 0);
			map.put("data", "Username does not exist!");
		} else if (loginUser.getPassword().equals(user.getPassword())) {
			map.put("code", 1);
			map.put("data", new Gson().toJson(user));
		} else {
			map.put("code", 0);
			map.put("data", "Wrong password!");
		}
		return new Gson().toJson(map);
	}

	private void closeResources() {
		try {
			if (socket != null)
				socket.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
