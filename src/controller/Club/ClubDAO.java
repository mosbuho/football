package controller.Club;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import controller.db.connetDB;
import model.Club;
import model.Player;

public class ClubDAO {
    public static ArrayList<Club> getClubList() {
        ArrayList<Club> clubList = new ArrayList<>();
        try (Connection con = connetDB.getConnection()) {
            PreparedStatement pstmt = con.prepareStatement("SELECT * FROM CLUB INNER JOIN PLAYER ON CLUB.C_NO = PLAYER.C_NO");
            ResultSet rs = pstmt.executeQuery();
            Map<Integer, Club> clubMap = new HashMap<>();
            while (rs.next()) {
                int cNo = rs.getInt("C_NO");
                String cName = rs.getString("C_NAME");
                Club club = clubMap.get(cNo);
                if (club == null) {
                    club = new Club(cNo, cName);
                    clubMap.put(cNo, club);
                }
                Player player = new Player(rs.getInt("P_NO"), cNo, rs.getString("P_NAME"),
                        rs.getString("P_UNIFORM_NO"),
                        rs.getString("P_POSITION"), rs.getInt("P_SHO"), rs.getInt("P_PAS"), rs.getInt("P_DEF"),
                        rs.getInt("P_PRICE"));
                club.addPlayer(player);
            }
            clubList.addAll(clubMap.values());
        } catch (SQLException e) {
            System.out.println("팀 정보 가져오기 에러 " + e.getMessage());
        }
        return clubList;
    }
}
