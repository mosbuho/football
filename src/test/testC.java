package test;

import java.io.*;
import java.net.*;

public class testC {
	private static final String SERVER_IP = "localhost";
	private static final int SERVER_PORT = 1234;

	public static void main(String[] args) throws InterruptedException {
		try {
			Socket socket = new Socket(SERVER_IP, SERVER_PORT);
			BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
			BufferedReader input = new BufferedReader(new InputStreamReader(System.in));

			System.out.println("서버: " + in.readLine());
			String userName = input.readLine();
			out.println(userName);

			while (true) {
				String serverMessage = in.readLine();
				System.out.println("서버: " + serverMessage);

				if (serverMessage.equals("당신의 차례입니다. 'ROLL'을 입력하세요.")) {
					String roll = input.readLine();
					out.println(roll);
				}
				Thread.sleep(1500);
			}
		} catch (IOException e) {
			System.out.println("클라이언트 에러: " + e.getMessage());
		}
	}
}