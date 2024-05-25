package controller.Gamer;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;

import controller.Club.ClubManager;
import controller.db.connectDB;
import model.Club;
import model.Gamer;

public class GamerDAO {
    public static ArrayList<Gamer> getGamerList() {
        ArrayList<Gamer> gamerList = new ArrayList<>();
        try (Connection con = connectDB.getConnection()) {
            PreparedStatement pstmt = con.prepareStatement(
                    "SELECT G.G_ID, C.C_NAME, G.G_POINT FROM GAMER G LEFT JOIN CLUB C ON G.C_NO = C.C_NO ORDER BY G_POINT DESC");
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
        try (Connection con = connectDB.getConnection()) {
            CallableStatement cstmt = con.prepareCall("{CALL REGISTER(?, ?, ?)}");
            cstmt.setInt(1, cNo);
            cstmt.setString(2, gId);
            cstmt.setString(3, gPw);
            result = cstmt.executeUpdate();
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
        try (Connection con = connectDB.getConnection()) {
            CallableStatement cstmt = con.prepareCall("{CALL LOGIN(?, ?, ?, ?)}");
            cstmt.setString(1, gId);
            cstmt.setString(2, gPw);
            cstmt.registerOutParameter(3, Types.VARCHAR);
            cstmt.registerOutParameter(4, Types.INTEGER);
            cstmt.executeQuery();
            sessionId = cstmt.getString(3);
            isAdmin = cstmt.getInt(4);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return String.format("%s|%s", sessionId, isAdmin);
    }

    public static int checkIsAdmin(String sessionId) {
        int isAdmin = 0;
        try (Connection con = connectDB.getConnection()) {
            PreparedStatement pstmt = con.prepareStatement("SELECT G_ISADMIN FROM GAMER WHERE G_SESSIONID = ?");
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
        try (Connection con = connectDB.getConnection()) {
            CallableStatement cstmt = con.prepareCall("{CALL LOGOUT(?)}");
            cstmt.setString(1, data);
            cstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static Club getMyClubInfo(String sessionId) {
        Club club = null;
        try (Connection con = connectDB.getConnection()) {
            PreparedStatement pstmt = con.prepareStatement(
                    "SELECT G.G_NO, G.G_BALANCE, C.C_NO, NVL(C.C_NAME, '무소속') AS C_NAME, P.P_NO, P.P_NAME, P.P_UNIFORM_NO, P.P_POSITION, P.P_SHO, P.P_PAS, P.P_DEF, P.P_PRICE "
                            + "FROM GAMER G LEFT JOIN CLUB C ON G.C_NO = C.C_NO INNER JOIN OWNER O ON G.G_NO = O.G_NO INNER JOIN PLAYER P ON O.P_NO = P.P_NO WHERE G_SESSIONID = ?");
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

    public static void updatePoint(String winnerSessionId) {
        try (Connection con = connectDB.getConnection()) {
            CallableStatement cstmt = con.prepareCall("{CALL POINTUP(?)}");
            cstmt.setString(1, winnerSessionId);
            cstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}