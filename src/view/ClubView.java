package view;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.PrintWriter;
import java.util.ArrayList;

import model.Club;

public class ClubView {
    public static void getClubList(PrintWriter pw, ObjectInputStream ois) {
        pw.println("getClubList");
        try {
            ArrayList<Club> clubList = (ArrayList<Club>) ois.readObject();
            System.out.println(clubList);
        } catch (ClassNotFoundException | IOException e) {
            e.printStackTrace();
        }
    }

    public static void createClub(BufferedReader input, PrintWriter pw, ObjectInputStream ois, BufferedReader br,
            String sessionId) throws IOException {
        System.out.print("생성할 팀 이름 : ");
        String cName = input.readLine();
        String data = String.format("%s|%s", sessionId, cName);
        pw.println("createClub");
        pw.println(data);
        String response = br.readLine();
        System.out.println(response);
    }

    public static void updateClub(BufferedReader input, PrintWriter pw, ObjectInputStream ois, BufferedReader br,
            String sessionId) throws IOException {
        ClubView.getClubList(pw, ois);
        System.out.print("변경할 팀 번호 : ");
        String cNo = input.readLine();
        System.out.print("변경할 팀 이름 : ");
        String cName = input.readLine();
        String data = String.format("%s|%s|%s", sessionId, cNo, cName);
        pw.println("updateClub");
        pw.println(data);
        String response = br.readLine();
        System.out.println(response);
    }

    public static void deleteClub(BufferedReader input, PrintWriter pw, ObjectInputStream ois, BufferedReader br,
            String sessionId) throws IOException {
        ClubView.getClubList(pw, ois);
        System.out.print("삭제할 팀 번호 : ");
        String cNo = input.readLine();
        String data = String.format("%s|%s", sessionId, cNo);
        pw.println("deleteClub");
        pw.println(data);
        String response = br.readLine();
        System.out.println(response);
    }
}