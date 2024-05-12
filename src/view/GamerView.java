package view;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.PrintWriter;
import java.util.ArrayList;

import model.Club;
import model.Gamer;
import model.Session;

public class GamerView {
    public static void register(PrintWriter pw, BufferedReader input, BufferedReader br, ObjectInputStream ois)
            throws IOException {
        System.out.print("생성할 아이디 : ");
        String gId = input.readLine();
        System.out.print("비밀번호 : ");
        String gPw = input.readLine();
        ClubView.getClubList(pw, ois);
        System.out.print("팀 선택 : ");
        String cNo = input.readLine();
        String data = String.format("%s|%s|%s", cNo, gId, gPw);
        pw.println("register");
        pw.println(data);
        String response = br.readLine();
        System.out.println(response);
    }

    public static void login(Session session, PrintWriter pw, BufferedReader input, BufferedReader br)
            throws IOException {
        System.out.print("아이디 : ");
        String gId = input.readLine();
        System.out.print("비밀번호 : ");
        String gPw = input.readLine();
        String data = String.format("%s|%s", gId, gPw);
        pw.println("login");
        pw.println(data);
        String[] temp = br.readLine().split("\\|");
        String sessionId = temp[0];
        int isAdmin = Integer.parseInt(temp[1]);
        if (sessionId.equals("null")) {
            System.out.println("로그인 실패");
        } else {
            session.setSessionId(sessionId);
            session.setLoggedIn(true);
            if (isAdmin == 1) {
                session.setAdmin(true);
            }
            System.out.println("로그인 성공");
        }
    }

    public static void getGamerList(PrintWriter pw, ObjectInputStream ois) {
        pw.println("getGamerList");
        ArrayList<Gamer> gamerList = new ArrayList<>();
        try {
            gamerList = (ArrayList<Gamer>) ois.readObject();
            System.out.println(gamerList);
        } catch (ClassNotFoundException | IOException e) {
            e.printStackTrace();
        }
    }

    public static void logout(PrintWriter pw, String sessionId) {
        pw.println("logout");
        pw.println(sessionId);
    }

    public static void dropPlayer(BufferedReader input, String sessionId, PrintWriter pw, BufferedReader br,
            ObjectInputStream ois) {
        getMyClubInfo(input, sessionId, pw, br, ois);
        System.out.print("방출할 선수 번호 : ");
        try {
            int pNo = Integer.parseInt(input.readLine());
            String data = String.format("%d|%s", pNo, sessionId);
            pw.println("dropPlayer");
            pw.println(data);
            String response = br.readLine();
            System.out.println(response);
        } catch (NumberFormatException | IOException e) {
            System.out.println("올바른 값 입력");
        }
    }

    public static void sellPlayer(BufferedReader input, String sessionId, PrintWriter pw, BufferedReader br,
            ObjectInputStream ois) {
        getMyClubInfo(input, sessionId, pw, br, ois);
        System.out.print("판매할 선수 번호 : ");
        try {
            int pNo = Integer.parseInt(input.readLine());
            String data = String.format("%d|%s", pNo, sessionId);
            pw.println("sellPlayer");
            pw.println(data);
            String response = br.readLine();
            System.out.println(response);
        } catch (NumberFormatException | IOException e) {
            System.out.println("올바른 값 입력");
        }
    }

    public static void buyPlayer(BufferedReader input, String sessionId, PrintWriter pw, BufferedReader br,
            ObjectInputStream ois) {
        PlayerView.getPlayerList(pw, ois);
        System.out.print("영입할 선수 번호 : ");
        try {
            int pNo = Integer.parseInt(input.readLine());
            String data = String.format("%s|%s", pNo, sessionId);
            pw.println("buyPlayer");
            pw.println(data);
            String response = br.readLine();
            System.out.println(response);
        } catch (NumberFormatException | IOException e) {
            System.out.println("올바른 값 입력");
        }
    }

    public static void getMyClubInfo(BufferedReader input, String sessionId, PrintWriter pw, BufferedReader br,
            ObjectInputStream ois) {
        pw.println("getMyClubInfo");
        pw.println(sessionId);
        try {
            Club club = (Club) ois.readObject();
            System.out.println(club.toStringWithBalance());
        } catch (ClassNotFoundException | IOException e) {
            e.printStackTrace();
        }
    }
}
