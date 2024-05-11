package view;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

import controller.Session;

public class GamerView {
    public static void register(PrintWriter pw, BufferedReader input, BufferedReader br) throws IOException {
        System.out.print("생성할 아이디 : ");
        String gId = input.readLine();
        System.out.print("비밀번호 : ");
        String gPw = input.readLine();
        System.out.print("팀 선택 : ");
        String cNo = input.readLine();
        String data = String.format("%s|%s|%s", cNo, gId, gPw);
        pw.println("register");
        pw.println(data);
        String response = br.readLine();
        System.out.println(response);
    }

    public static void login(Session session, PrintWriter pw, BufferedReader input, BufferedReader br)
            throws IOException {
        System.out.print("아이디 : ");
        String gId = input.readLine();
        System.out.print("비밀번호 : ");
        String gPw = input.readLine();
        String data = String.format("%s|%s", gId, gPw);
        pw.println("login");
        pw.println(data);
        String[] temp = br.readLine().split("\\|");
        String sessionId = temp[0];
        int isAdmin = Integer.parseInt(temp[1]);
        if (sessionId.equals("null")) {
            System.out.println("로그인 실패");
        } else {
            session.setSessionId(sessionId);
            session.setLoggedIn(true);
            if (isAdmin == 1) {
                session.setAdmin(true);
            }
            System.out.println("로그인 성공");
        }
    }
}
