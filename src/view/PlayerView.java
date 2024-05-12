package view;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.PrintWriter;
import java.util.ArrayList;

import model.Player;

public class PlayerView {

    public static void getPlayerList(PrintWriter pw, ObjectInputStream ois) {
        pw.println("getPlayerList");
        try {
            ArrayList<Player> playerList = (ArrayList<Player>) ois.readObject();
            System.out.println(playerList);
        } catch (ClassNotFoundException | IOException e) {
            System.out.println("선수 목록 수신 중 에러 " + e.getMessage());
        }
    }
}
