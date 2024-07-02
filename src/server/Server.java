package server;

import java.net.ServerSocket;
import java.net.Socket;

import server.thread.HandleThread;

public class Server {
	public static void main(String[] args) throws Exception {
		int port = 9999;
		ServerSocket server = new ServerSocket(port);
		System.out.println("The server is listening for client connection requests on port " + port + ".");
		while (true) {
			Socket socket = server.accept();
			System.out.println("Client " + socket.getInetAddress().getHostAddress() + " successfully connected!");
			new HandleThread(socket).start();
		}
	}
}
