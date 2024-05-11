package controller.Gamer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import model.Gamer;

public class GamerManager {
    public static void getGamerList() {
        ArrayList<Gamer> gamerList = GamerDAO.getGamerList();
    }

    public static void register(BufferedReader br, PrintWriter pw) throws IOException {
        String data = br.readLine();
        int result = GamerDAO.register(data);
        String response = (result != 0) ? "회원가입 성공" : "회원가입 실패";
        pw.println(response);
    }

    public static void login(BufferedReader br, PrintWriter pw) throws IOException {
        String data = br.readLine();
        String result = GamerDAO.login(data);
        pw.println(result);
    }
}
