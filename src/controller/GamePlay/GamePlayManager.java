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
    private static final int MAX_ROUNDS = 2;

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
        String[] pas_words = {
                "필드를 가로지르는 정밀 스루 패스",
                "공중을 지배하는 높은 헤딩 패스",
                "순식간에 이뤄지는 빠른 원투 패스",
                "눈에 띄지 않는 은밀한 오프더볼 패스",
                "수비수를 현혹하는 매혹적인 드리블 패스",
                "수비 라인을 무너뜨리는 강력한 원투패스",
                "골대를 정조준하는 날카로운 드리븐 패스",
                "상황 판단력을 빛내는 즉흥적 오너십 패스",
                "결정적인 순간의 완벽한 타이밍 원투패스",
                "팀 플레이를 극대화하는 윙퍼스 패스"
        };
        String[] def_words = {
                "철벽 같은 솔리드 블로킹",
                "상대 팀의 공격 중심을 차단하는 포인트 디펜스",
                "공격적인 강력한 프레스",
                "그림자처럼 따라붙는 쉐도잉",
                "주요 공격수를 제압하는 마크맨십",
                "공을 되찾는 예리한 루스볼 리커버리",
                "전장을 꿰뚫는 필드 비전",
                "수비진을 지휘하는 리더쉽",
                "정교한 수비 포지셔닝",
                "공을 차지하는 볼 윈닝",
                "적절한 순간의 결정적인 체크",
                "위기를 예방하는 프리벤트 디펜스",
                "시기적절한 타이밍 수비",
                "공격의 날을 꺾는 스파이크 블로킹",
                "리바운드를 지배하는 컨트롤"
        };
        String[] sho_words = {
                "경이로운 아크로바틱 슛",
                "힘찬 파워 슛",
                "정교함을 자랑하는 테크니컬 슛",
                "직관적인 체감 슈팅",
                "결정적인 순간의 킬러 슛",
                "장거리를 가르는 런칭 슛",
                "수비의 벽을 뚫는 파괴적인 슛",
                "연습의 결실, 리본 슛",
                "하늘을 나는 에어본 슛",
                "비어 있는 골대를 찾아내는 오픈 넷 슛",
                "승리를 부르는 챔피언스 리그 슛",
                "포스트를 울리는 강력한 슛",
                "하늘 높이 솟는 스파이더맨 슈팅",
                "아름다운 바이시클 킥"
        };
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
                            broadcastMessage(String.format("%s이(가) %s에게 %s \n패스 성공", att.getpName(), att2.getpName(),
                                    pas_words[random.nextInt(pas_words.length)]));
                            inGameTurn++;
                        } else {
                            broadcastMessage(String.format("%s이(가) %s에게 %s", att.getpName(), att2.getpName(),
                                    pas_words[random.nextInt(pas_words.length)]));
                            broadcastMessage(String.format("%s의 %s", def.getpName(),
                                    def_words[random.nextInt(def_words.length)]));
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
                            broadcastMessage(String.format("%s의 %s \n골", att.getpName(),
                                    sho_words[random.nextInt(sho_words.length)]));
                            score.put(playerA ? playerAa : playerBb,
                                    score.getOrDefault(playerA ? playerAa : playerBb, 0) + 1);
                            inGameTurn = 0;
                            round++;
                            turn.set(!turn.get());
                        } else {
                            broadcastMessage(String.format("%s의 %s", att.getpName(),
                            sho_words[random.nextInt(sho_words.length)]));
                            broadcastMessage(String.format("%s의 %s", def.getpName(),
                                    def_words[random.nextInt(def_words.length)]));
                            inGameTurn = 0;
                            round++;
                            turn.set(!turn.get());
                        }
                    }
                }
            } else {
                pw.println("수비 턴");
            }
            Thread.sleep(1500);
        }
        int scoreA = score.getOrDefault(playerAa, 0);
        int scoreB = score.getOrDefault(playerBb, 0);
        if (scoreA > scoreB) {
            pw.println(String.format("A %s : B %s A 승리", scoreA, scoreB));
            String winnerSessionId = null;
            for (Map.Entry<String, String> entry : gamingUserSA.entrySet()) {
                if (entry.getValue().equals(playerAa)) {
                    winnerSessionId = entry.getKey();
                    GamerDAO.updatePoint(winnerSessionId);
                    break;
                }
            }
        } else if (scoreA < scoreB) {
            pw.println(String.format("A %s : B %s B 승리", scoreA, scoreB));
        } else {
            pw.println(String.format("A %s : B %s 동점", scoreA, scoreB));
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