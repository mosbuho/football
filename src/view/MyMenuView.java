package view;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.PrintWriter;

public class MyMenuView {
    public static void myClubMenu(PrintWriter pw, BufferedReader br, BufferedReader input, ObjectInputStream ois,
            String sessionId) {
        int menu = 0;
        while (true) {
            menu = 0;
            PrintView.printMyClubMenu();
            try {
                menu = Integer.parseInt(input.readLine());
            } catch (NumberFormatException | IOException e) {
            }
            switch (menu) {
                case 1:
                    GamerView.getMyClubInfo(input, sessionId, pw, br, ois);
                    break;
                case 2:
                    GamerView.dropPlayer(input, sessionId, pw, br, ois);
                    break;
                case 3:
                    GamerView.sellPlayer(input, sessionId, pw, br, ois);
                    break;
                case 4:
                    GamerView.buyPlayer(input, sessionId, pw, br, ois);
                    break;
                case 5:
                    return;
                default:
                    System.out.println("올바른 값 입력");
                    break;
            }
        }
    }
}
