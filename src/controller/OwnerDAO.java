package controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class OwnerDAO {

    public static void initPlayer(Connection con, ResultSet rs, int cNo) {
        try (PreparedStatement pstmt = con
                .prepareStatement("INSERT INTO OWNER(G_NO, P_NO) SELECT ?, P_NO FROM PLAYER WHERE C_NO = ?")) {
            int gNo = rs.getInt(1);
            pstmt.setInt(1, gNo);
            pstmt.setInt(2, cNo);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static int dropPlayer(int gNo, int pNo, Connection con) {
        int result = 0;
        try (PreparedStatement pstmt = con.prepareStatement("DELETE FROM OWNER WHERE G_NO = ? AND P_NO = ?")) {
            pstmt.setInt(1, gNo);
            pstmt.setInt(2, pNo);
            result = pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static int insertPlayer(int gNo, int pNo, Connection con) {
        int result = 0;
        try (PreparedStatement pstmt = con.prepareStatement("INSERT INTO OWNER VALUES(?, ?)")) {
            pstmt.setInt(1, gNo);
            pstmt.setInt(2, pNo);
            result = pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }
}
