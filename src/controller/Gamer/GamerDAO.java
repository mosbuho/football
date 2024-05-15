package controller.Gamer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.UUID;

import controller.OwnerDAO;
import controller.Club.ClubManager;
import controller.db.connectDB;
import model.Club;
import model.Gamer;

public class GamerDAO {
    public static ArrayList<Gamer> getGamerList() {
        ArrayList<Gamer> gamerList = new ArrayList<>();
        try (Connection con = connectDB.getConnection();
                PreparedStatement pstmt = con.prepareStatement(
                        "SELECT G.G_ID, C.C_NAME, G.G_POINT FROM GAMER G LEFT JOIN CLUB C ON G.C_NO = C.C_NO ORDER BY G_POINT");) {
            ResultSet rs = pstmt.executeQuery();
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
                PreparedStatement pstmt = con.prepareStatement("INSERT INTO GAMER(C_NO, G_ID, G_PW) VALUES(?, ?, ?)",
                        new String[] { "G_NO" })) {
            pstmt.setInt(1, cNo);
            pstmt.setString(2, gId);
            pstmt.setString(3, gPw);
            result = pstmt.executeUpdate();
            ResultSet rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                int gNo = rs.getInt(1);
                OwnerDAO.initPlayer(con, gNo, cNo);
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
                String gSessionId = rs.getString("G_SESSIONID");
                sessionId = setSessionId(con, pstmt, rs.getInt("G_NO"), gSessionId);
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

    public static int checkIsAdmin(String sessionId) {
        int isAdmin = 0;
        try (Connection con = connectDB.getConnection();
                PreparedStatement pstmt = con.prepareStatement("SELECT G_ISADMIN FROM GAMER WHERE G_SESSIONID = ?")) {
            pstmt.setString(1, sessionId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                isAdmin = rs.getInt("G_ISADMIN");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return isAdmin;
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

    public static int sellPlayer(String data) {
        String[] temp = data.split("\\|");
        int pNo = Integer.parseInt(temp[0]);
        String sessionId = temp[1];
        int result = 0;
        Connection con = connectDB.getConnection();
        try (PreparedStatement pstmt = con.prepareStatement(
                "UPDATE GAMER SET G_BALANCE = G_BALANCE + (SELECT P_PRICE FROM PLAYER WHERE P_NO = ?) WHERE G_SESSIONID = ?")) {
            pstmt.setInt(1, pNo);
            pstmt.setString(2, sessionId);
            result = pstmt.executeUpdate();
            OwnerDAO.dropPlayer(data);
        } catch (SQLException e) {
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
        try (PreparedStatement pstmt = con.prepareStatement(
                "UPDATE GAMER SET G_BALANCE = G_BALANCE - (SELECT P_PRICE FROM PLAYER WHERE P_NO = ?) WHERE G_SESSIONID = ?")) {
            pstmt.setInt(1, pNo);
            pstmt.setString(2, sessionId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                OwnerDAO.insertPlayer(data);
                result = pstmt.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static Club getMyClubInfo(String sessionId) {
        Club club = null;
        try (Connection con = connectDB.getConnection();
                PreparedStatement pstmt = con.prepareStatement(
                        "SELECT G.G_NO, G.G_BALANCE, C.C_NO, NVL(C.C_NAME, '무소속') AS C_NAME, P.P_NO, P.P_NAME, P.P_UNIFORM_NO, P.P_POSITION, P.P_SHO, P.P_PAS, P.P_DEF, P.P_PRICE "
                                + "FROM GAMER G LEFT JOIN CLUB C ON G.C_NO = C.C_NO INNER JOIN OWNER O ON G.G_NO = O.G_NO INNER JOIN PLAYER P ON O.P_NO = P.P_NO WHERE G_SESSIONID = ?")) {
            pstmt.setString(1, sessionId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                if (club == null) {
                    club = new Club(rs.getInt("C_NO"), rs.getString("C_NAME"), rs.getInt("G_BALANCE"));
                }
                ClubManager.addPlayerToClub(club, rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return club;
    }
}