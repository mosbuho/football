package controller.Player;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import model.Player;

public class PlayerManager {
    public static void getPlayerList(ObjectOutputStream oos) {
        ArrayList<Player> playerList = PlayerDAO.getPlayerList();
        try {
            oos.writeObject(playerList);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}