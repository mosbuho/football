package controller.Club;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import model.Club;

public class ClubManager {
    public static void getClubList(ObjectOutputStream oos) {
        ArrayList<Club> clubList = ClubDAO.getClubList();
        try {
            oos.writeObject(clubList);
        } catch (IOException e) {
            System.out.println("팀 정보 전달 에러 " + e.getMessage());
        }
    }
}