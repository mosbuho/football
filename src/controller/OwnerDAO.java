package controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import controller.Club.ClubManager;
import controller.db.connectDB;
import model.Club;

public class OwnerDAO {

    public static void initPlayer(Connection con, int gNo, int cNo) {
        try (PreparedStatement pstmt = con
                .prepareStatement("INSERT INTO OWNER(G_NO, P_NO) SELECT ?, P_NO FROM PLAYER WHERE C_NO = ?")) {
            pstmt.setInt(1, gNo);
            pstmt.setInt(2, cNo);
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
                PreparedStatement pstmt = con.prepareStatement(
                        "DELETE FROM OWNER WHERE G_NO = (SELECT G_NO FROM GAMER WHERE G_SESSIONID = ?) AND P_NO = ?")) {
            pstmt.setString(1, sessionId);
            pstmt.setInt(2, pNo);
            result = pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static int insertPlayer(String data) {
        String[] temp = data.split("\\|");
        int pNo = Integer.parseInt(temp[0]);
        String sessionId = temp[1];
        int result = 0;
        try (Connection con = connectDB.getConnection();
                PreparedStatement pstmt = con.prepareStatement(
                        "INSERT INTO OWNER VALUES((SELECT G_NO FROM GAMER WHERE G_SESSIONID = ?), ?)")) {
            pstmt.setString(1, sessionId);
            pstmt.setInt(2, pNo);
            result = pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static Club getMyClubInfo(String sessionId) {
        Club club = null;
        try (Connection con = connectDB.getConnection();
                PreparedStatement pstmt = con.prepareStatement(
                        "SELECT G.G_NO, G.G_BALANCE, C.C_NO, C.C_NAME, P.P_NO, P.P_NAME, P.P_UNIFORM_NO, P.P_POSITION, P.P_SHO, P.P_PAS, P.P_DEF, P.P_PRICE "
                                + "FROM GAMER G INNER JOIN CLUB C ON G.C_NO = C.C_NO INNER JOIN OWNER O ON G.G_NO = O.G_NO INNER JOIN PLAYER P ON O.P_NO = P.P_NO WHERE G_SESSIONID = ?")) {
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
