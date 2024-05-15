package view;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.PrintWriter;
import java.util.ArrayList;

import model.Player;

public class PlayerView {
    public static void getPlayerList(PrintWriter pw, ObjectInputStream ois) {
        pw.println("getPlayerList");
        try {
            ArrayList<Player> playerList = (ArrayList<Player>) ois.readObject();
            System.out.println(playerList);
        } catch (ClassNotFoundException | IOException e) {
            System.out.println("선수 목록 수신 중 에러 " + e.getMessage());
        }
    }

    public static void createPlayer(BufferedReader input, PrintWriter pw, ObjectInputStream ois, BufferedReader br,
            String sessionId) throws IOException {
        ClubView.getClubList(pw, ois);
        System.out.print("생성할 선수 소속팀 번호 (0 = 무소속) : ");
        int cNo = Integer.parseInt(input.readLine());
        System.out.print("생성할 선수 이름 : ");
        String pName = input.readLine();
        System.out.print("생성할 선수 등번호 (0-99) : ");
        int pUniformNo = Integer.parseInt(input.readLine());
        System.out.print("생성할 선수 포지션 (gk | df | mf | fw) : ");
        String pPosition = input.readLine();
        System.out.print("생성할 선수 슈팅 능력 (0-99) : ");
        int pSho = Integer.parseInt(input.readLine());
        System.out.print("생성할 선수 패스 능력 (0-99) : ");
        int pPas = Integer.parseInt(input.readLine());
        System.out.print("생성할 선수 수비 능력 (0-99) : ");
        int pDef = Integer.parseInt(input.readLine());
        System.out.print("생성할 선수 가격 : ");
        int pPrice = Integer.parseInt(input.readLine());
        String data = String.format("%s|%d|%s|%d|%s|%d|%d|%d|%d", sessionId, cNo, pName, pUniformNo, pPosition, pSho,
                pPas,
                pDef, pPrice);
        pw.println("createPlayer");
        pw.println(data);
        String response = br.readLine();
        System.out.println(response);
    }

    public static void updatePlayer(BufferedReader input, PrintWriter pw, ObjectInputStream ois, BufferedReader br,
            String sessionId) throws NumberFormatException, IOException {
        PlayerView.getPlayerList(pw, ois);
        System.out.print("변경할 선수 번호 : ");
        int pNo = Integer.parseInt(input.readLine());
        ClubView.getClubList(pw, ois);
        System.out.print("변경할 선수 소속팀 (0 = 무소속) : ");
        int cNo = Integer.parseInt(input.readLine());
        System.out.print("변경할 선수 이름 : ");
        String pName = input.readLine();
        System.out.print("변경할 선수 등번호 (0-99) : ");
        int pUniformNo = Integer.parseInt(input.readLine());
        System.out.print("변경할 선수 포지션 (gk | df | mf | fw) : ");
        String pPosition = input.readLine();
        System.out.print("변경할 선수 슈팅 능력 (0-99) : ");
        int pSho = Integer.parseInt(input.readLine());
        System.out.print("변경할 선수 패스 능력 (0-99) : ");
        int pPas = Integer.parseInt(input.readLine());
        System.out.print("변경할 선수 수비 능력 (0-99) : ");
        int pDef = Integer.parseInt(input.readLine());
        System.out.print("변경할 선수 가격 : ");
        int pPrice = Integer.parseInt(input.readLine());
        String data = String.format("%s|%d|%d|%s|%d|%s|%d|%d|%d|%d", sessionId, pNo, cNo, pName, pUniformNo, pPosition, pSho,
                pPas,
                pDef, pPrice);
        pw.println("updatePlayer");
        pw.println(data);
        String response = br.readLine();
        System.out.println(response);
    }

    public static void deletePlayer(BufferedReader input, PrintWriter pw, ObjectInputStream ois, BufferedReader br,
            String sessionId) throws IOException {
        PlayerView.getPlayerList(pw, ois);
        System.out.print("삭제할 선수 번호 : ");
        String pNo = input.readLine();
        String data = String.format("%s|%s", sessionId, pNo);
        pw.println("deletePlayer");
        pw.println(data);
        String response = br.readLine();
        System.out.println(response);
    }
}
