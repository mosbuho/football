package controller.Player;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;

import controller.Gamer.GamerDAO;
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

    public static void createPlayer(BufferedReader br, PrintWriter pw) throws IOException {
        String data = br.readLine();
        String[] temp = data.split("\\|");
        String sessionId = temp[0];
        int cNo = Integer.parseInt(temp[1]);
        String pName = temp[2];
        int pUniformNo = Integer.parseInt(temp[3]);
        String pPosition = temp[4];
        int pSho = Integer.parseInt(temp[5]);
        int pPas = Integer.parseInt(temp[6]);
        int pDef = Integer.parseInt(temp[7]);
        int pPrice = Integer.parseInt(temp[8]);
        int isAdmin = GamerDAO.checkIsAdmin(sessionId);
        if (isAdmin == 1) {
            int result = PlayerDAO.createPlayer(cNo, pName, pUniformNo, pPosition, pSho, pPas, pDef, pPrice);
            if (result == 1) {
                pw.println("선수 생성 완료");
            } else {
                pw.println("선수 생성 실패");
            }
        } else {
            pw.println("선수 생성 실패");
        }
    }

    public static void deletePlayer(BufferedReader br, PrintWriter pw) throws IOException {
        String data = br.readLine();
        String[] temp = data.split("\\|");
        String sessionId = temp[0];
        int pNo = Integer.parseInt(temp[1]);
        int isAdmin = GamerDAO.checkIsAdmin(sessionId);
        if (isAdmin == 1) {
            int result = PlayerDAO.deletePlayer(pNo);
            if (result == 1) {
                pw.println("선수 삭제 완료");
            } else {
                pw.println("선수 삭제 실패");
            }
        } else {
            pw.println("선수 삭제 실패");
        }
    }

    public static void updatePlayer(BufferedReader br, PrintWriter pw) throws IOException {
        String data = br.readLine();
        String[] temp = data.split("\\|");
        String sessionId = temp[0];
        int pNo = Integer.parseInt(temp[1]);
        int cNo = Integer.parseInt(temp[2]);
        String pName = temp[3];
        int pUniformNo = Integer.parseInt(temp[4]);
        String pPosition = temp[5];
        int pSho = Integer.parseInt(temp[6]);
        int pPas = Integer.parseInt(temp[7]);
        int pDef = Integer.parseInt(temp[8]);
        int pPrice = Integer.parseInt(temp[9]);
        int isAdmin = GamerDAO.checkIsAdmin(sessionId);
        if (isAdmin == 1) {
            int result = PlayerDAO.updatePlayer(pNo, cNo, pName, pUniformNo, pPosition, pSho, pPas, pDef, pPrice);
            if (result == 1) {
                pw.println("선수 변경 완료");
            } else {
                pw.println("선수 변경 실패");
            }
        } else {
            pw.println("선수 변경 실패");
        }
    }
}