package view;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import model.Club;
import model.Player;

public class GamePlayView {

    public static void ready(PrintWriter pw, BufferedReader br, BufferedReader input, ObjectInputStream ois,
            String sessionId) throws IOException {
        Club club = GamerView.getMyPlayerList(input, sessionId, pw, br, ois);
        ArrayList<Player> allPlayers = club.getPlayerList();
        HashSet<Integer> selectedPlayers = new HashSet<>();
        HashMap<String, ArrayList<Player>> playerMap = new HashMap<>();
        if (allPlayers.size() < 11) {
            System.out.println("선수 부족");
        } else {
            ArrayList<Player> players = null;
            String pPosition = null;
            for (Player player : allPlayers) {
                pPosition = player.getpPosition();
                players = playerMap.get(pPosition);
                if (players == null) {
                    players = new ArrayList<>();
                    playerMap.put(pPosition, players);
                }
                players.add(player);
            }
            StringBuilder sb = new StringBuilder();
            sb.append(sessionId).append("|");
            String[] positions = { "gk", "df", "df", "df", "df", "mf", "mf", "mf", "fw", "fw", "fw" };
            ArrayList<Player> playerList = new ArrayList<>();
            for (String position : positions) {
                playerList = playerMap.get(position);
                for (Player player : playerList) {
                    System.out.print(player);
                }
                System.out.print("선발 선수 번호 선택 : ");
                int choice = Integer.parseInt(input.readLine());
                sb.append(choice).append("|");
                selectedPlayers.add(choice);
                playerList.removeIf(player -> player.getpNo() == choice);
            }
            if (selectedPlayers.size() == 11) {
                pw.println("ready");
                String status = br.readLine();
                if (status.equals("fail")) {
                    System.out.println("대기열 꽉 참 ");
                } else {
                    pw.println(sb);
                    String response = br.readLine();
                    if (response.equals("fail")) {
                        System.out.println("선수 인원 부족");
                    } else {
                        System.out.println(response);
                        String ABChoice = input.readLine();
                        pw.println(ABChoice);
                        System.out.println("준비 완료");
                    }
                }
            } else {
                System.out.println("선수 인원 부족");
            }
        }
    }

    public static void play(PrintWriter pw, BufferedReader br, BufferedReader input, ObjectInputStream ois,
            String sessionId) throws IOException {
        pw.println("play");
        pw.println(sessionId);
        while (true) {
            String response = br.readLine();
            System.out.println(response);
            if (response.equals("pass 입력") || response.equals("shoot 입력")) {
                String action = input.readLine();
                pw.println(action);
            }
            if (response.equals("게임 종료")) {
                break;
            }
            try {
                Thread.sleep(1500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
