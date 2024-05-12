package controller.Gamer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.UUID;

import controller.OwnerDAO;
import controller.Club.ClubManager;
import controller.Player.PlayerDAO;
import controller.db.connectDB;
import model.Club;
import model.Gamer;

public class GamerDAO {
    public static ArrayList<Gamer> getGamerList() {
        ArrayList<Gamer> gamerList = new ArrayList<>();
        try (Connection con = connectDB.getConnection();
                PreparedStatement pstmt = con.prepareStatement(
                        "SELECT G.G_ID, C.C_NAME, G.G_POINT FROM GAMER G INNER JOIN CLUB C ON G.C_NO = C.C_NO ORDER BY G_POINT");
                ResultSet rs = pstmt.executeQuery();) {
            while (rs.next()) {
                Gamer gamer = new Gamer(rs.getString("G_ID"), rs.getString("C_NAME"), rs.getInt("G_POINT"));
                gamerList.add(gamer);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return gamerList;
    }

    public static int register(String data) {
        String[] temp = data.split("\\|");
        int result = 0;
        int cNo = Integer.parseInt(temp[0]);
        String gId = temp[1];
        String gPw = temp[2];
        try (Connection con = connectDB.getConnection();
                PreparedStatement pstmt = con.prepareStatement("INSERT INTO GAMER(C_NO, G_ID, G_PW) VALUES(?, ?, ?)");
                PreparedStatement pstmt2 = con.prepareStatement("SELECT G_SEQ.CURRVAL FROM DUAL");) {
            pstmt.setInt(1, cNo);
            pstmt.setString(2, gId);
            pstmt.setString(3, gPw);
            result = pstmt.executeUpdate();
            ResultSet rs = pstmt2.executeQuery();
            if (rs.next()) {
                OwnerDAO.initPlayer(con, rs, cNo);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static String login(String data) {
        String[] temp = data.split("\\|");
        String gId = temp[0];
        String gPw = temp[1];
        String sessionId = null;
        int isAdmin = 0;
        try (Connection con = connectDB.getConnection();
                PreparedStatement pstmt = con.prepareStatement(
                        "SELECT G_NO, G_ISADMIN, G_SESSIONID FROM GAMER WHERE G_ID = ? AND G_PW = ?");) {
            pstmt.setString(1, gId);
            pstmt.setString(2, gPw);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                isAdmin = rs.getInt("G_ISADMIN");
                sessionId = rs.getString("G_SESSIONID");
                sessionId = setSessionId(con, pstmt, rs.getInt("G_NO"), sessionId);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return String.format("%s|%s", sessionId, isAdmin);
    }

    public static String setSessionId(Connection con, PreparedStatement pstmt, int gNo, String sessionId) {
        String newSessionId = UUID.randomUUID().toString();
        try {
            pstmt = con.prepareStatement("UPDATE GAMER SET G_SESSIONID = ? WHERE G_NO = ?");
            pstmt.setString(1, newSessionId);
            pstmt.setInt(2, gNo);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return newSessionId;
    }

    public static void logout(String data) {
        try (Connection con = connectDB.getConnection();
                PreparedStatement pstmt = con
                        .prepareStatement("UPDATE GAMER SET G_SESSIONID = NULL WHERE G_SESSIONID = ?")) {
            pstmt.setString(1, data);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static int dropPlayer(String data) {
        String[] temp = data.split("\\|");
        int pNo = Integer.parseInt(temp[0]);
        String sessionId = temp[1];
        int result = 0;
        try (Connection con = connectDB.getConnection();
                PreparedStatement pstmt = con.prepareStatement("SELECT G_NO FROM GAMER WHERE G_SESSIONID = ?")) {
            con.setAutoCommit(false);
            pstmt.setString(1, sessionId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                int gNo = rs.getInt("G_NO");
                result = OwnerDAO.dropPlayer(gNo, pNo, con);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static int sellPlayer(String data) {
        String[] temp = data.split("\\|");
        int pNo = Integer.parseInt(temp[0]);
        String sessionId = temp[1];
        int result = 0;
        Connection con = connectDB.getConnection();
        try (PreparedStatement pstmt = con.prepareStatement("SELECT G_NO, G_BALANCE FROM GAMER WHERE G_SESSIONID = ?");
                PreparedStatement pstmt2 = con.prepareStatement("UPDATE GAMER SET G_BALANCE = ? WHERE G_NO = ?");) {
            con.setAutoCommit(false);
            pstmt.setString(1, sessionId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                int gNo = rs.getInt("G_NO");
                int gBalance = rs.getInt("G_BALANCE");
                int pPrice = PlayerDAO.getPlayerPrice(pNo, con);
                OwnerDAO.dropPlayer(gNo, pNo, con);
                pstmt2.setInt(1, gBalance + pPrice);
                pstmt2.setInt(2, gNo);
                result = pstmt2.executeUpdate();
            }
            con.commit();
        } catch (SQLException e) {
            try {
                con.rollback();
                con.close();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
            e.printStackTrace();
        }
        return result;
    }

    public static int buyPlayer(String data) {
        String[] temp = data.split("\\|");
        int pNo = Integer.parseInt(temp[0]);
        String sessionId = temp[1];
        int result = 0;
        Connection con = connectDB.getConnection();
        try (PreparedStatement pstmt = con.prepareStatement("SELECT G_NO, G_BALANCE FROM GAMER WHERE G_SESSIONID = ?");
                PreparedStatement pstmt3 = con.prepareStatement("UPDATE GAMER SET G_BALANCE = ? WHERE G_NO = ?");) {
            con.setAutoCommit(false);
            pstmt.setString(1, sessionId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                int gNo = rs.getInt("G_NO");
                int gBalance = rs.getInt("G_BALANCE");
                OwnerDAO.insertPlayer(gNo, pNo, con);
                int pPrice = PlayerDAO.getPlayerPrice(pNo, con);
                pstmt3.setInt(1, gBalance - pPrice);
                pstmt3.setInt(2, gNo);
                result = pstmt3.executeUpdate();
            }
            con.commit();
        } catch (SQLException e) {
            try {
                con.rollback();
                con.close();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
            e.printStackTrace();
        }
        return result;
    }

    public static Club getMyClubInfo(String sessionId) {
        Club club = null;
        try (Connection con = connectDB.getConnection();
                PreparedStatement pstmt = con.prepareStatement(
                        "SELECT G.G_NO, G_BALANCE, C.C_NO, C.C_NAME FROM GAMER G INNER JOIN CLUB C ON G.C_NO = C.C_NO WHERE G_SESSIONID = ?");
                PreparedStatement pstmt2 = con.prepareStatement(
                        "SELECT * FROM OWNER O INNER JOIN PLAYER P ON O.P_NO = P.P_NO WHERE O.G_NO = ?")) {
            pstmt.setString(1, sessionId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                int cNo = rs.getInt("C_NO");
                String cName = rs.getString("C_NAME");
                int gBalance = rs.getInt("G_BALANCE");
                club = new Club(cNo, cName, gBalance);
                pstmt2.setInt(1, rs.getInt("G_NO"));
                ResultSet rs2 = pstmt2.executeQuery();
                while (rs2.next()) {
                    ClubManager.addPlayerToClub(club, rs2);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return club;
    }
}