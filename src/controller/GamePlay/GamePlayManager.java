package controller.GamePlay;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;

import controller.OwnerDAO;
import controller.Gamer.GamerDAO;
import model.Player;

public class GamePlayManager {
    private static Random random = new Random();
    private static ConcurrentHashMap<String, String> gamingUserSA = new ConcurrentHashMap<>();
    private static ConcurrentHashMap<String, ArrayList<Player>> gamingUserPL = new ConcurrentHashMap<>();
    private static AtomicBoolean turn = new AtomicBoolean(random.nextBoolean());
    private static ConcurrentHashMap<String, Integer> score = new ConcurrentHashMap<>();
    private static Set<PrintWriter> writers = Collections.synchronizedSet(new HashSet<>());
    private static final int MAX_GAME_PLAYER = 2;
    private static int round = 0;
    private static int inGameTurn = 0;
    private static final int MAX_ROUNDS = 4;

    public static void ready(BufferedReader br, PrintWriter pw, Socket cs) throws IOException {
        if (gamingUserSA.size() >= MAX_GAME_PLAYER) {
            pw.println("fail");
        } else {
            pw.println("pass");
            String data = br.readLine();
            String[] temp = data.split("\\|");
            String sessionId = temp[0];
            ArrayList<Player> playerList = OwnerDAO.getGamerPlayers(data);
            if (playerList.size() == 11) {
                pw.println("A / B 선택");
                String playerAB = br.readLine();
                gamingUserSA.put(sessionId, playerAB);
                Collections.sort(playerList);
                gamingUserPL.put(playerAB, playerList);
            } else {
                pw.println("fail");
            }
        }
    }

    public static void play(BufferedReader br, PrintWriter pw) throws InterruptedException, IOException {
        round = 0;
        String sessionId = br.readLine();
        writers.add(pw);
        boolean playerA = false;
        for (Map.Entry<String, String> entry : gamingUserSA.entrySet()) {
            if (entry.getKey().equals(sessionId)) {
                if (entry.getValue().equals("A")) {
                    playerA = true;
                    break;
                }
            }
        }
        ArrayList<Player> aPlayerList = null;
        ArrayList<Player> bPlayerList = null;
        String playerAa = null;
        String playerBb = null;
        for (Map.Entry<String, ArrayList<Player>> entry : gamingUserPL.entrySet()) {
            if (entry.getKey().equals("A")) {
                playerAa = "A";
                aPlayerList = entry.getValue();
            } else {
                playerBb = "B";
                bPlayerList = entry.getValue();
            }
        }
        while (round < MAX_ROUNDS) {
            if ((playerA && turn.get()) || (!playerA && !turn.get())) {
                Player att = null;
                Player att2 = null;
                Player def = null;
                if (inGameTurn <= 1) {
                    pw.println("pass 입력");
                    String action = br.readLine();
                    if (action.equals("pass")) {
                        if (inGameTurn == 0) {
                            if (turn.get()) {
                                att = aPlayerList.get(random.nextInt(4));
                                att2 = aPlayerList.get(random.nextInt(3) + 5);
                                def = bPlayerList.get(random.nextInt(3) + 8);
                            } else {
                                att = bPlayerList.get(random.nextInt(4));
                                att2 = bPlayerList.get(random.nextInt(3) + 5);
                                def = aPlayerList.get(random.nextInt(3) + 8);
                            }
                        } else {
                            if (turn.get()) {
                                att = aPlayerList.get(random.nextInt(3) + 5);
                                att2 = aPlayerList.get(random.nextInt(3) + 8);
                                def = bPlayerList.get(random.nextInt(3) + 5);
                            } else {
                                att = bPlayerList.get(random.nextInt(3) + 5);
                                att2 = bPlayerList.get(random.nextInt(3) + 8);
                                def = aPlayerList.get(random.nextInt(3) + 5);
                            }
                        }
                        if ((att.getpPas() + att2.getpPas()) / 2 > def.getpDef()) {
                            broadcastMessage(String.format("%s %s이(가) %s에게 %s로 연결",
                                    Words.getPlayerWords(), att.getpName(), att2.getpName(),
                                    Words.getPasWords()));
                            inGameTurn++;
                        } else {
                            broadcastMessage(String.format("%s %s이(가) %s에게 %s 시도",
                                    Words.getPlayerWords(), att.getpName(), att2.getpName(),
                                    Words.getPasWords()));
                            broadcastMessage(String.format("%s %s의 %s(으)로 차단",
                                    Words.getPlayerWords(), def.getpName(),
                                    Words.getDefWords()));
                            inGameTurn = 0;
                            turn.set(!turn.get());
                            round++;
                            if (round == MAX_ROUNDS) {
                                pw.println("gameEnd");
                                break;
                            }
                        }
                    }
                } else {
                    pw.println("shoot 입력");
                    String action = br.readLine();
                    if (action.equals("shoot")) {
                        if (turn.get()) {
                            att = aPlayerList.get(random.nextInt(3) + 8);
                            def = bPlayerList.get(random.nextInt(4));
                        } else {
                            att = bPlayerList.get(random.nextInt(3) + 8);
                            def = aPlayerList.get(random.nextInt(4));
                        }
                        if (att.getpSho() > def.getpDef()) {
                            broadcastMessage(String.format("%s %s의 %s이 %s",
                                    Words.getPlayerWords(), att.getpName(),
                                    Words.getShoWords(),
                                    Words.getGoalWords()));
                            score.put(playerA ? playerAa : playerBb,
                                    score.getOrDefault(playerA ? playerAa : playerBb, 0) + 1);
                            broadcastMessage((String.format("A %s : B %s", score.getOrDefault(playerAa, 0),
                                    score.getOrDefault(playerBb, 0))));
                            inGameTurn = 0;
                            round++;
                            turn.set(!turn.get());
                        } else {
                            broadcastMessage(String.format("%s %s의 %s",
                                    Words.getPlayerWords(), att.getpName(),
                                    Words.getShoWords()));
                            broadcastMessage(String.format("%s %s의 %s(으)로 차단",
                                    Words.getPlayerWords(), def.getpName(),
                                    Words.getDefWords()));
                            inGameTurn = 0;
                            round++;
                            turn.set(!turn.get());
                        }
                    }
                }
            } else {
                pw.println("");
            }
            Thread.sleep(3000);
        }
        int scoreA = score.getOrDefault(playerAa, 0);
        int scoreB = score.getOrDefault(playerBb, 0);
        String winnerSessionId = null;
        if (scoreA > scoreB) {
            broadcastMessage(String.format("A %s : B %s A 승리", scoreA, scoreB));
            for (Map.Entry<String, String> entry : gamingUserSA.entrySet()) {
                if (entry.getValue().equals(playerAa)) {
                    winnerSessionId = entry.getKey();
                    GamerDAO.updatePoint(winnerSessionId);
                    break;
                }
            }
        } else if (scoreA < scoreB) {
            broadcastMessage(String.format("A %s : B %s B 승리", scoreA, scoreB));
            for (Map.Entry<String, String> entry : gamingUserSA.entrySet()) {
                if (entry.getValue().equals(playerBb)) {
                    winnerSessionId = entry.getKey();
                    GamerDAO.updatePoint(winnerSessionId);
                    break;
                }
            }
        } else {
            broadcastMessage(String.format("A %s : B %s 동점", scoreA, scoreB));
        }
        pw.println("게임 종료");
        gamingUserSA.clear();
        gamingUserPL.clear();
        writers.clear();
    }

    public static void broadcastMessage(String msg) {
        for (PrintWriter writer : writers) {
            writer.println(msg);
        }
    }
}