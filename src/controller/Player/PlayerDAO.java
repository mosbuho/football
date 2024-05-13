package controller.Player;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import controller.db.connectDB;
import model.Player;

public class PlayerDAO {

    public static ArrayList<Player> getPlayerList() {
        ArrayList<Player> playerList = new ArrayList<>();
        try (Connection con = connectDB.getConnection();
                PreparedStatement pstmt = con
                        .prepareStatement("SELECT * FROM CLUB C INNER JOIN PLAYER P ON C.C_NO = P.C_NO");
                ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                Player player = new Player(rs.getInt("P_NO"), rs.getInt("C_NO"), rs.getString("C_NAME"),
                        rs.getString("P_NAME"),
                        rs.getString("P_UNIFORM_NO"), rs.getString("P_POSITION"), rs.getInt("P_SHO"),
                        rs.getInt("P_PAS"), rs.getInt("P_DEF"), rs.getInt("P_PRICE"));
                playerList.add(player);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return playerList;
    }
}
