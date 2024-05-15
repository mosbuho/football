package controller.Gamer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;

import controller.OwnerDAO;
import model.Club;
import model.Gamer;

public class GamerManager {
    public static void getGamerList(ObjectOutputStream oos) {
        ArrayList<Gamer> gamerList = GamerDAO.getGamerList();
        try {
            oos.writeObject(gamerList);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void register(BufferedReader br, PrintWriter pw) throws IOException {
        String data = br.readLine();
        int result = GamerDAO.register(data);
        String response = (result != 0) ? "회원가입 성공" : "회원가입 실패";
        pw.println(response);
    }

    public static void login(BufferedReader br, PrintWriter pw) throws IOException {
        String data = br.readLine();
        String result = GamerDAO.login(data);
        pw.println(result);
    }

    public static void logout(BufferedReader br, PrintWriter pw) {
        try {
            String data = br.readLine();
            GamerDAO.logout(data);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void dropPlayer(BufferedReader br, PrintWriter pw) {
        try {
            String data = br.readLine();
            int result = OwnerDAO.dropPlayer(data);
            String response = (result != 0) ? "방출 성공" : "방출 실패";
            pw.println(response);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void sellPlayer(BufferedReader br, PrintWriter pw) {
        try {
            String data = br.readLine();
            int result = GamerDAO.sellPlayer(data);
            String response = (result != 0) ? "판매 성공" : "판매 실패";
            pw.println(response);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void buyPlayer(BufferedReader br, PrintWriter pw) {
        try {
            String data = br.readLine();
            int result = GamerDAO.buyPlayer(data);
            String response = (result != 0) ? "영입 성공" : "영입 실패";
            pw.println(response);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void getMyClubInfo(BufferedReader br, PrintWriter pw, ObjectOutputStream oos) {
        try {
            String sessionId = br.readLine();
            Club club = GamerDAO.getMyClubInfo(sessionId);
            oos.writeObject(club);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}