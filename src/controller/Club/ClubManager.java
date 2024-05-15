package controller.Club;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import controller.Gamer.GamerDAO;
import model.Club;
import model.Player;

public class ClubManager {
    public static void getClubList(ObjectOutputStream oos) {
        ArrayList<Club> clubList = ClubDAO.getClubList();
        try {
            oos.writeObject(clubList);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void addPlayerToClub(Club club, ResultSet rs) {
        try {
            Player player = new Player(rs.getInt("P_NO"), club.getcNo(), club.getcName(), rs.getString("P_NAME"),
                    rs.getString("P_UNIFORM_NO"),
                    rs.getString("P_POSITION"), rs.getInt("P_SHO"), rs.getInt("P_PAS"), rs.getInt("P_DEF"),
                    rs.getInt("P_PRICE"));
            club.addPlayer(player);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void createClub(BufferedReader br, PrintWriter pw) throws IOException {
        String data = br.readLine();
        String[] temp = data.split("\\|");
        String sessionId = temp[0];
        String cName = temp[1];
        int isAdmin = GamerDAO.checkIsAdmin(sessionId);
        if (isAdmin == 1) {
            int result = ClubDAO.createClub(cName);
            if (result == 1) {
                pw.println("팀 생성 완료");
            } else {
                pw.println("팀 생성 실패");
            }
        } else {
            pw.println("팀 생성 실패");
        }
    }

    public static void updateClub(BufferedReader br, PrintWriter pw) throws IOException {
        String data = br.readLine();
        String[] temp = data.split("\\|");
        String sessionId = temp[0];
        int cNo = Integer.parseInt(temp[1]);
        String cName = temp[2];
        int isAdmin = GamerDAO.checkIsAdmin(sessionId);
        if (isAdmin == 1) {
            int result = ClubDAO.updateClub(cNo, cName);
            if (result == 1) {
                pw.println("팀 정보 변경 완료");
            } else {
                pw.println("팀 정보 변경 실패");
            }
        } else {
            pw.println("팀 정보 변경 실패");
        }
    }

    public static void deleteClub(BufferedReader br, PrintWriter pw) throws IOException {
        String data = br.readLine();
        String[] temp = data.split("\\|");
        String sessionId = temp[0];
        int cNo = Integer.parseInt(temp[1]);
        int isAdmin = GamerDAO.checkIsAdmin(sessionId);
        if (isAdmin == 1) {
            int result = ClubDAO.deleteClub(cNo);
            if (result == 1) {
                pw.println("팀 삭제 완료");
            } else {
                pw.println("팀 삭제 실패");
            }
        } else {
            pw.println("팀 삭제 실패");
        }
    }
}