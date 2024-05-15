package view;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.PrintWriter;

public class AdminView {
    public static void adminMenu(PrintWriter pw, BufferedReader br, BufferedReader input, ObjectInputStream ois,
            String sessionId, boolean isAdmin) throws IOException {
        if (!isAdmin) {
            System.out.println("관리자 권한 없음");
        } else {
            int menu = 0;
            while (true) {
                menu = 0;
                PrintView.printAdminMenu();
                try {
                    menu = Integer.parseInt(input.readLine());
                } catch (NumberFormatException | IOException e) {
                }
                switch (menu) {
                    case 1:
                        ClubView.createClub(input, pw, ois, br, sessionId);
                        break;
                    case 2:
                        ClubView.updateClub(input, pw, ois, br, sessionId);
                        break;
                    case 3:
                        ClubView.deleteClub(input, pw, ois, br, sessionId);
                        break;
                    case 4:
                        PlayerView.createPlayer(input, pw, ois, br, sessionId);
                        break;
                    case 5:
                        PlayerView.updatePlayer(input, pw, ois, br, sessionId);
                        break;
                    case 6:
                        PlayerView.deletePlayer(input, pw, ois, br, sessionId);
                        break;
                    case 7:
                        return;
                    default:
                        System.out.println("올바른 값 입력");
                        break;
                }
            }
        }
    }
}
