import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import controller.Session;
import view.ClubView;
import view.GamerView;
import view.MenuViewer;

public class Client {
    public static final int PORT = 7777;

    public static void main(String[] args) {
        try (Socket cs = new Socket("localhost", PORT)) {
            Session session = new Session();
            BufferedReader br = new BufferedReader(new InputStreamReader(cs.getInputStream()));
            BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
            PrintWriter pw = new PrintWriter(cs.getOutputStream(), true);
            ObjectInputStream ois = new ObjectInputStream(cs.getInputStream());
            int menu = 0;

            while (true) {
                if (!session.isLoggedIn()) {
                    MenuViewer.mainMenu();
                    menu = Integer.parseInt(input.readLine());
                    switch (menu) {
                        case 1:
                            GamerView.register(pw, input, br);
                            break;
                        case 2:
                            GamerView.login(session, pw, input, br);
                            break;
                        case 3:
                            System.out.println("종료");
                            return;
                        default:
                            System.out.println("올바른 값 입력");
                            break;
                    }
                } else {
                    MenuViewer.loggedInMainMenu();
                    menu = Integer.parseInt(input.readLine());
                    switch (menu) {
                        case 1:
                            ClubView.getClubList(pw, ois);
                            break;
                        case 2:
                            break;
                        case 3:
                            System.out.println("종료");
                            return;
                        default:
                            System.out.println("올바른 값 입력");
                            break;
                    }
                }
            }
        } catch (UnknownHostException e) {
            System.out.println("소켓 에러 " + e.getMessage());
        } catch (IOException e) {
            System.out.println("소켓 에러 " + e.getMessage());
        }
    }
}