package controller.Club;

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
        try (Connection con = connectDB.getConnection();
                PreparedStatement pstmt = con
                        .prepareStatement("SELECT * FROM CLUB C INNER JOIN PLAYER P ON C.C_NO = P.C_NO");
                ResultSet rs = pstmt.executeQuery();) {
            Map<Integer, Club> clubMap = new HashMap<>();
            Club club = null;
            while (rs.next()) {
                int cNo = rs.getInt("C_NO");
                club = clubMap.get(cNo);
                if (club == null) {
                    club = new Club(cNo, rs.getString("C_NAME"));
                    clubMap.put(cNo, club);
                }
                ClubManager.addPlayerToClub(club, rs);
            }
            clubList.addAll(clubMap.values());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return clubList;
    }
}
