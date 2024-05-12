package view;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.PrintWriter;
import java.util.ArrayList;

import model.Club;

public class ClubView {
    public static void getClubList(PrintWriter pw, ObjectInputStream ois) {
        pw.println("getClubList");
        try {
            ArrayList<Club> clubList = (ArrayList<Club>) ois.readObject();
            System.out.println(clubList);
        } catch (ClassNotFoundException | IOException e) {
            e.printStackTrace();
        }
    }
}
