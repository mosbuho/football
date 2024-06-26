import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import model.Session;
import view.AdminView;
import view.ClubView;
import view.GamePlayView;
import view.GamerView;
import view.PrintView;
import view.MyMenuView;
import view.PlayerView;

public class Client {
    public static final int PORT = 7777;

    public static void main(String[] args) {
        Session session = null;
        try (Socket cs = new Socket("localhost", PORT);
                BufferedReader br = new BufferedReader(new InputStreamReader(cs.getInputStream()));
                ObjectInputStream ois = new ObjectInputStream(cs.getInputStream());
                BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
                PrintWriter pw = new PrintWriter(cs.getOutputStream(), true);) {
            session = new Session();
            int menu = 0;
            while (true) {
                menu = 0;
                if (!session.isLoggedIn()) {
                    PrintView.printMainMenu();
                    try {
                        menu = Integer.parseInt(input.readLine());
                    } catch (NumberFormatException | IOException e) {
                    }
                    switch (menu) {
                        case 1:
                            GamerView.register(pw, input, br, ois);
                            break;
                        case 2:
                            GamerView.login(session, pw, input, br);
                            break;
                        case 3:
                            GamerView.logout(pw, session.getSessionId());
                            return;
                        default:
                            System.out.println("올바른 값 입력");
                            break;
                    }
                } else {
                    PrintView.printLoggedInMainMenu();
                    try {
                        menu = Integer.parseInt(input.readLine());
                    } catch (NumberFormatException | IOException e) {
                    }
                    switch (menu) {
                        case 1:
                            ClubView.getClubList(pw, ois);
                            break;
                        case 2:
                            PlayerView.getPlayerList(pw, ois);
                            break;
                        case 3:
                            GamerView.getGamerList(pw, ois);
                            break;
                        case 4:
                            MyMenuView.myClubMenu(pw, br, input, ois, session.getSessionId());
                            break;
                        case 5:
                            GamePlayView.ready(pw, br, input, ois, session.getSessionId());
                            break;
                        case 6:
                            GamePlayView.play(pw, br, input, ois, session.getSessionId());
                            break;
                        case 7:
                            AdminView.adminMenu(pw, br, input, ois, session.getSessionId(), session.isAdmin());
                            break;
                        case 8:
                            GamerView.logout(pw, session.getSessionId());
                            return;
                        default:
                            System.out.println("올바른 값 입력");
                            break;
                    }
                }
            }
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}