package controller.Gamer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.UUID;

import controller.db.connetDB;
import model.Gamer;

public class GamerDAO {
    public static ArrayList<Gamer> getGamerList() {
        ArrayList<Gamer> gamerList = new ArrayList<>();
        try (Connection con = connetDB.getConnection()) {
            PreparedStatement pstmt = con.prepareStatement("SELECT * FROM GAMER");
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                Gamer gvo = new Gamer(rs.getInt("G_NO"), rs.getInt("C_NO"), rs.getString("G_ID"),
                        rs.getString("G_PW"),
                        rs.getString("G_SESSIONID"), rs.getInt("G_ISADMIN"), rs.getInt("G_BALANCE"),
                        rs.getInt("G_GKCOUNT"), rs.getInt("G_DFCOUNT"), rs.getInt("G_MFCOUNT"), rs.getInt("G_FWCOUNT"));
                gamerList.add(gvo);
            }
        } catch (SQLException e) {
            System.out.println("PreParedStatement Error");
        }
        return gamerList;
    }

    public static int register(String data) {
        String[] temp = data.split("\\|");
        int result = 0;
        try (Connection con = connetDB.getConnection()) {
            PreparedStatement pstmt = con.prepareStatement("INSERT INTO GAMER(C_NO, G_ID, G_PW) VALUES(?, ?, ?)");
            pstmt.setInt(1, Integer.parseInt(temp[0]));
            pstmt.setString(2, temp[1]);
            pstmt.setString(3, temp[2]);
            result = pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("회원가입 에러 " + e.getMessage());
        }
        return result;
    }

    public static String login(String data) {
        String[] temp = data.split("\\|");
        String gId = temp[0];
        String gPw = temp[1];
        String sessionId = null;
        int isAdmin = 0;
        try (Connection con = connetDB.getConnection()) {
            PreparedStatement pstmt = con.prepareStatement("SELECT * FROM GAMER WHERE G_ID = ? AND G_PW = ?");
            pstmt.setString(1, gId);
            pstmt.setString(2, gPw);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                isAdmin = rs.getInt("G_ISADMIN");
                sessionId = setSessionId(con, pstmt, rs.getInt("G_NO"));
            }
        } catch (SQLException e) {
            System.out.println("로그인 에러 " + e.getMessage());
        }
        return String.format("%s|%s", sessionId, isAdmin);
    }

    public static String setSessionId(Connection con, PreparedStatement pstmt, int gNo) {
        String sessionId = UUID.randomUUID().toString();
        try {
            pstmt = con.prepareStatement("UPDATE GAMER SET G_SESSIONID = ? WHERE G_NO = ?");
            pstmt.setString(1, sessionId);
            pstmt.setInt(2, gNo);
        } catch (SQLException e) {
            System.out.println("세션정보 에러 " + e.getMessage());
        }
        return sessionId;
    }
}
