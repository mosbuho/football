package controller.Club;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import controller.db.connectDB;
import model.Club;

public class ClubDAO {
    public static ArrayList<Club> getClubList() {
        ArrayList<Club> clubList = new ArrayList<>();
        try (Connection con = connectDB.getConnection()) {
            PreparedStatement pstmt = con
                    .prepareStatement("SELECT * FROM CLUB C LEFT JOIN PLAYER P ON C.C_NO = P.C_NO");
            ResultSet rs = pstmt.executeQuery();
            Map<Integer, Club> clubMap = new HashMap<>();
            Club club = null;
            int cNo = 0;
            int pNo = 0;
            while (rs.next()) {
                cNo = rs.getInt("C_NO");
                pNo = rs.getInt("P_NO");
                club = clubMap.get(cNo);
                if (club == null) {
                    club = new Club(cNo, rs.getString("C_NAME"));
                    clubMap.put(cNo, club);
                }
                if (pNo != 0) {
                    ClubManager.addPlayerToClub(club, rs);
                }
            }
            clubList.addAll(clubMap.values());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return clubList;
    }

    public static int createClub(String cName) {
        int result = 0;
        try (Connection con = connectDB.getConnection()) {
            CallableStatement cstmt = con.prepareCall("{CALL INSERTCLUB(?)}");
            cstmt.setString(1, cName);
            result = cstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static int deleteClub(int cNo) {
        int result = 0;
        try (Connection con = connectDB.getConnection()) {
            CallableStatement cstmt = con.prepareCall("{CALL DELETECLUB(?)}");
            cstmt.setInt(1, cNo);
            result = cstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static int updateClub(int cNo, String cName) {
        int result = 0;
        try (Connection con = connectDB.getConnection()) {
            PreparedStatement cstmt = con.prepareStatement("{CALL UPDATECLUB(?, ?)}");
            cstmt.setString(1, cName);
            cstmt.setInt(2, cNo);
            result = cstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }
}
