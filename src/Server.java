import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

import controller.Club.ClubManager;
import controller.GamePlay.GamePlayManager;
import controller.Gamer.GamerManager;
import controller.Player.PlayerManager;

public class Server {
    public static final int PORT = 7777;

    public static void main(String[] args) {
        try (ServerSocket ss = new ServerSocket(PORT)) {
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
        try (cs;
                BufferedReader br = new BufferedReader(new InputStreamReader(cs.getInputStream()));
                PrintWriter pw = new PrintWriter(cs.getOutputStream(), true);
                ObjectOutputStream oos = new ObjectOutputStream(cs.getOutputStream());) {
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
                    case "getPlayerList":
                        PlayerManager.getPlayerList(oos);
                        break;
                    case "getGamerList":
                        GamerManager.getGamerList(oos);
                        break;
                    case "getMyClubInfo":
                        GamerManager.getMyClubInfo(br, pw, oos);
                        break;
                    case "sellPlayer":
                        GamerManager.sellPlayer(br, pw);
                        break;
                    case "buyPlayer":
                        GamerManager.buyPlayer(br, pw);
                        break;
                    case "ready":
                        GamePlayManager.ready(br, pw, cs);
                        break;
                    case "play":
                        GamePlayManager.play(br, pw);
                        break;
                    case "createClub":
                        ClubManager.createClub(br, pw);
                        break;
                    case "deleteClub":
                        ClubManager.deleteClub(br, pw);
                        break;
                    case "updateClub":
                        ClubManager.updateClub(br, pw);
                        break;
                    case "createPlayer":
                        PlayerManager.createPlayer(br, pw);
                        break;
                    case "updatePlayer":
                        PlayerManager.updatePlayer(br, pw);
                        break;
                    case "deletePlayer":
                        PlayerManager.deletePlayer(br, pw);
                        break;
                    case "logout":
                        GamerManager.logout(br, pw);
                        break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}