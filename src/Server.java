import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

import controller.Club.ClubManager;
import controller.Gamer.GamerManager;

public class Server {
    public static final int PORT = 7777;

    public static void main(String[] args) {
        try {
            ServerSocket ss = new ServerSocket(PORT);
            System.out.println("서버 온");
            while (true) {
                Socket cs = ss.accept();
                System.out.println(cs + " 접속");
                new Thread(() -> {
                    csHandler(cs);
                }).start();
            }
        } catch (IOException e) {
            System.out.println("서버 소켓 에러 " + e.getMessage());
        }
    }

    public static void csHandler(Socket cs) {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(cs.getInputStream()))) {
            PrintWriter pw = new PrintWriter(cs.getOutputStream(), true);
            ObjectOutputStream oos = new ObjectOutputStream(cs.getOutputStream());
            String request = null;
            while ((request = br.readLine()) != null) {
                switch (request) {
                    case "register":
                        GamerManager.register(br, pw);
                        break;
                    case "login":
                        GamerManager.login(br, pw);
                        break;
                    case "getClubList":
                        ClubManager.getClubList(oos);
                        break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}