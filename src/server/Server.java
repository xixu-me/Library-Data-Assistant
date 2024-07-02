package server;

import java.net.ServerSocket;
import java.net.Socket;

import server.thread.HandleThread;

public class Server {
	private static final int PORT = 9999;

	public static void main(String[] args) {
		try (ServerSocket server = new ServerSocket(PORT)) {
			System.out.println("Server listening on port " + PORT);
			while (true) {
				Socket clientSocket = server.accept();
				System.out.println("Client connected: " + clientSocket.getInetAddress().getHostAddress());
				new Thread(new HandleThread(clientSocket)).start();
			}
		} catch (Exception e) {
			System.err.println("Server exception: " + e.getMessage());
		}
	}
}
