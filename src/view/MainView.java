package view;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.PrintWriter;

public class MainView {
    public static void printMainMenu() {
        System.out.println("-------------------------");
        System.out.println("|\tMINIFOOTBALL\t|");
        System.out.println("-------------------------");
        System.out.println("|\t1. 회원가입\t|");
        System.out.println("|\t2. 로그인\t|");
        System.out.println("|\t3. 종료\t\t|");
        System.out.println("-------------------------");
        System.out.print("메뉴 입력 : ");
    }

    public static void printLoggedInMainMenu() {
        System.out.println("-------------------------");
        System.out.println("|\tMINIFOOTBALL\t|");
        System.out.println("-------------------------");
        System.out.println("|\t1. 팀 목록\t|");
        System.out.println("|\t2. 선수 목록\t|");
        System.out.println("|\t3. 순위\t\t|");
        System.out.println("|\t4. 내 팀 관리\t|");
        System.out.println("|\t5. 플레이\t|");
        System.out.println("|\t6. 종료\t\t|");
        System.out.println("-------------------------");
        System.out.print("메뉴 입력 : ");
    }

    public static void printMyClubMenu() {
        System.out.println("-------------------------");
        System.out.println("|\tMINIFOOTBALL\t|");
        System.out.println("-------------------------");
        System.out.println("|\t1. 내 팀 정보\t|");
        System.out.println("|\t2. 선수 방출\t|");
        System.out.println("|\t3. 선수 판매\t|");
        System.out.println("|\t4. 선수 영입\t|");
        System.out.println("|\t5. 플레이\t|");
        System.out.println("|\t6. 메인 메뉴\t|");
        System.out.println("-------------------------");
        System.out.print("메뉴 입력 : ");
    }

    public static void myClubMenu(PrintWriter pw, BufferedReader br, BufferedReader input, ObjectInputStream ois, String sessionId) {
        int menu = 0;
        while (true) {
            menu = 0;
            MainView.printMyClubMenu();
            try {menu = Integer.parseInt(input.readLine());} 
            catch (NumberFormatException | IOException e) {}
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
                    // 플레이
                    break;
                case 6:
                    return;
                default:
                    System.out.println("올바른 값 입력");
                    break;
            }
        }
    }
}