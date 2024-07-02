package server;

import java.net.ServerSocket;
import java.net.Socket;

import server.thread.HandleThread;

public class Server {
	public static void main(String[] args) throws Exception {
		int port = 9999;
		ServerSocket server = new ServerSocket(port);
		System.out.println("服务器正在 9999 端口侦听客户端请求");
		while (true) {
			Socket socket = server.accept();
			System.out.println("客户端" + socket.getInetAddress().getHostAddress() + "成功连接");
			new HandleThread(socket).start();
		}
	}
}
