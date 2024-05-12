package controller.Club;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import model.Club;
import model.Player;

public class ClubManager {
    public static void getClubList(ObjectOutputStream oos) {
        ArrayList<Club> clubList = ClubDAO.getClubList();
        try {
            oos.writeObject(clubList);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void addPlayerToClub(Club club, ResultSet rs) {
        Player player;
        try {
            player = new Player(rs.getInt("P_NO"), club.getcNo(), club.getcName(), rs.getString("P_NAME"),
                    rs.getString("P_UNIFORM_NO"),
                    rs.getString("P_POSITION"), rs.getInt("P_SHO"), rs.getInt("P_PAS"), rs.getInt("P_DEF"),
                    rs.getInt("P_PRICE"));
            club.addPlayer(player);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}