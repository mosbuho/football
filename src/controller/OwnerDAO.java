package controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import controller.db.connectDB;

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
}
