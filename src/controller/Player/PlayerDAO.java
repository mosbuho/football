package controller.Player;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.concurrent.Callable;

import controller.db.connectDB;
import model.Player;

public class PlayerDAO {

    public static ArrayList<Player> getPlayerList() {
        ArrayList<Player> playerList = new ArrayList<>();
        try (Connection con = connectDB.getConnection();
                PreparedStatement pstmt = con
                        .prepareStatement("SELECT * FROM PLAYER P LEFT JOIN CLUB C ON C.C_NO = P.C_NO");) {
            ResultSet rs = pstmt.executeQuery();
            String cName = null;
            while (rs.next()) {
                cName = rs.getString("C_NAME");
                if (cName == null) {
                    cName = "무소속";
                }
                Player player = new Player(rs.getInt("P_NO"), rs.getInt("C_NO"), cName,
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

    public static int createPlayer(int cNo, String pName, int pUniformNo, String pPosition, int pSho, int pPas,
            int pDef, int pPrice) {
        int result = 0;
        try (Connection con = connectDB.getConnection()) {
            CallableStatement cstmt = con.prepareCall("{CALL INSERTPLAYER(?, ?, ?, ?, ?, ?, ?, ?)}");
            if (cNo == 0) {
                cstmt.setNull(1, Types.INTEGER);
            } else {
                cstmt.setInt(1, cNo);
            }
            cstmt.setString(2, pName);
            cstmt.setInt(3, pUniformNo);
            cstmt.setString(4, pPosition);
            cstmt.setInt(5, pSho);
            cstmt.setInt(6, pPas);
            cstmt.setInt(7, pDef);
            cstmt.setInt(8, pPrice);
            result = cstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static int deletePlayer(int pNo) {
        int result = 0;
        try (Connection con = connectDB.getConnection()) {
            CallableStatement cstmt = con.prepareCall("{CALL DELETEPLAYER(?)}");
            cstmt.setInt(1, pNo);
            result = cstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static int updatePlayer(int pNo, int cNo, String pName, int pUniformNo, String pPosition, int pSho, int pPas,
            int pDef, int pPrice) {
        int result = 0;
        try (Connection con = connectDB.getConnection()) {
            CallableStatement cstmt = con.prepareCall("{CALL UPDATEPLAYER(?, ?, ?, ?, ?, ?, ?, ?, ?)}");
            if (cNo == 0) {
                cstmt.setNull(1, Types.INTEGER);
            } else {
                cstmt.setInt(1, cNo);
            }
            cstmt.setString(2, pName);
            cstmt.setInt(3, pUniformNo);
            cstmt.setString(4, pPosition);
            cstmt.setInt(5, pSho);
            cstmt.setInt(6, pPas);
            cstmt.setInt(7, pDef);
            cstmt.setInt(8, pPrice);
            cstmt.setInt(9, pNo);
            result = cstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }
}
