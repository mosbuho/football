package controller;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;

import controller.db.connectDB;
import model.Player;

public class OwnerDAO {
    public static int sellPlayer(String data) {
        String[] temp = data.split("\\|");
        int pNo = Integer.parseInt(temp[0]);
        String sessionId = temp[1];
        int result = 0;
        try (Connection con = connectDB.getConnection()) {
            CallableStatement cstmt = con.prepareCall("{CALL SELLPLAYER(?, ?, ?)}");
            cstmt.setString(1, sessionId);
            cstmt.setInt(2, pNo);
            cstmt.registerOutParameter(3, Types.INTEGER);
            cstmt.executeUpdate();
            result = cstmt.getInt(3);
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
        try (Connection con = connectDB.getConnection()) {
            CallableStatement cstmt = con.prepareCall("{CALL BUYPLAYER(?, ?)}");
            cstmt.setString(1, sessionId);
            cstmt.setInt(2, pNo);
            result = cstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static ArrayList<Player> getGamerPlayers(String data) {
        String[] temp = data.split("\\|");
        ArrayList<Player> playerList = new ArrayList<>();
        try (Connection con = connectDB.getConnection()) {
            PreparedStatement pstmt = con.prepareStatement(
                    "SELECT P.P_NO, P_NAME, P_UNIFORM_NO, P_POSITION, P_SHO, P_PAS, P_DEF FROM OWNER O INNER JOIN PLAYER P ON O.P_NO = P.P_NO "
                            + "WHERE G_NO = (SELECT G_NO FROM GAMER WHERE G_SESSIONID = ?) AND O.P_NO IN (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
            pstmt.setString(1, temp[0]);
            pstmt.setInt(2, Integer.parseInt(temp[1]));
            pstmt.setInt(3, Integer.parseInt(temp[2]));
            pstmt.setInt(4, Integer.parseInt(temp[3]));
            pstmt.setInt(5, Integer.parseInt(temp[4]));
            pstmt.setInt(6, Integer.parseInt(temp[5]));
            pstmt.setInt(7, Integer.parseInt(temp[6]));
            pstmt.setInt(8, Integer.parseInt(temp[7]));
            pstmt.setInt(9, Integer.parseInt(temp[8]));
            pstmt.setInt(10, Integer.parseInt(temp[9]));
            pstmt.setInt(11, Integer.parseInt(temp[10]));
            pstmt.setInt(12, Integer.parseInt(temp[11]));
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                Player player = new Player(rs.getInt("P_NO"), rs.getString("P_NAME"), rs.getString("P_UNIFORM_NO"),
                        rs.getString("P_POSITION"), rs.getInt("P_SHO"), rs.getInt("P_PAS"), rs.getInt("P_DEF"));
                playerList.add(player);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return playerList;
    }
}
