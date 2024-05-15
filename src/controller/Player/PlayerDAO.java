package controller.Player;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;

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
        try (Connection con = connectDB.getConnection();
                PreparedStatement pstmt = con
                        .prepareStatement(
                                "INSERT INTO PLAYER (C_NO, P_NAME, P_UNIFORM_NO, P_POSITION, P_SHO, P_PAS, P_DEF, P_PRICE) VALUES (?, ?, ?, ?, ?, ?, ?, ?)");) {
            if (cNo == 0) {
                pstmt.setNull(1, Types.INTEGER);
            } else {
                pstmt.setInt(1, cNo);
            }
            pstmt.setString(2, pName);
            pstmt.setInt(3, pUniformNo);
            pstmt.setString(4, pPosition);
            pstmt.setInt(5, pSho);
            pstmt.setInt(6, pPas);
            pstmt.setInt(7, pDef);
            pstmt.setInt(8, pPrice);
            result = pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static int deletePlayer(int pNo) {
        int result = 0;
        try (Connection con = connectDB.getConnection();
                PreparedStatement pstmt = con.prepareStatement("DELETE FROM PLAYER WHERE P_NO = ?")) {
            pstmt.setInt(1, pNo);
            result = pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static int updatePlayer(int pNo, int cNo, String pName, int pUniformNo, String pPosition, int pSho, int pPas,
            int pDef, int pPrice) {
        int result = 0;
        try (Connection con = connectDB.getConnection();
                PreparedStatement pstmt = con
                        .prepareStatement(
                                "UPDATE PLAYER SET C_NO = ?, P_NAME = ?, P_UNIFORM_NO = ?, P_POSITION = ?, P_SHO = ?, P_PAS = ?, P_DEF = ?, P_PRICE = ? WHERE P_NO = ?");) {
            if (cNo == 0) {
                pstmt.setNull(1, Types.INTEGER);
            } else {
                pstmt.setInt(1, cNo);
            }
            pstmt.setString(2, pName);
            pstmt.setInt(3, pUniformNo);
            pstmt.setString(4, pPosition);
            pstmt.setInt(5, pSho);
            pstmt.setInt(6, pPas);
            pstmt.setInt(7, pDef);
            pstmt.setInt(8, pPrice);
            pstmt.setInt(9, pNo);
            result = pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }
}
