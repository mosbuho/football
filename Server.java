import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;

import controller.ClubManager;
import controller.GamerManager;
import controller.PlayerManager;
import model.ClubVO;
import model.GamerVO;
import model.PlayerVO;

public class Server {
	public static final int PORT = 7777;
	private static Random random = new Random();
	public static ConcurrentHashMap<Socket, GamerVO> gameUserList = new ConcurrentHashMap<>();
	public static ConcurrentHashMap<String, ArrayList<PlayerVO>> gamingUserList = new ConcurrentHashMap<>();
	public static ConcurrentHashMap<String, String> gamingUserListAorB = new ConcurrentHashMap<>();
	private static AtomicBoolean turn = new AtomicBoolean(random.nextBoolean());
	private static ConcurrentHashMap<String, Integer> scores = new ConcurrentHashMap<>();
	private static Set<PrintWriter> writers = Collections.synchronizedSet(new HashSet<>());
	public static final int MAX_GAME_PLAYER = 2;
	public static int round = 0;
	public static int inGameTurn = 0;
	public static final int MAX_ROUNDS = 10;

	public static void main(String[] args) {
		try {
			ServerSocket ss = new ServerSocket(PORT);
			System.out.println("서버 온");

			ClubManager.defaultTeamCreate();
			PlayerManager.defaultPlayerCreate();
			GamerManager.logoutAllUsers();

			while (true) {
				Socket cs = ss.accept();
				System.out.println(cs + "접속");
				new Thread(() -> {
					try {
						csHandler(cs);
					} catch (IOException e) {
						System.out.println("쓰레드 핸들러 에러" + e.getMessage());
					} catch (InterruptedException e) {
						System.out.println("쓰레드 핸들러 에러" + e.getMessage());
					}
				}).start();
			}
		} catch (IOException e) {
			System.out.println("서버 소켓 에러 " + e.getMessage());
		}
	}

	public static void csHandler(Socket cs) throws IOException, InterruptedException {
		BufferedReader br = new BufferedReader(new InputStreamReader(cs.getInputStream()));
		PrintWriter pw = new PrintWriter(cs.getOutputStream(), true);
		ObjectOutputStream oos = new ObjectOutputStream(cs.getOutputStream());
		try {
			String request = null;
			while ((request = br.readLine()) != null) {
				switch (request) {
					case "register":
						register(br, pw, oos, cs);
						break;
					case "login":
						login(br, pw, cs);
						break;
					case "adminLogin":
						adminLogin(br, pw, cs);
						break;
					case "teamInfo":
						teamInfo(oos);
						break;
					case "playerInfo":
						playerInfo(oos);
						break;
					case "userInfo":
						userInfo(oos);
						break;
					case "myInfo":
						myInfo(br, pw, oos);
						break;
					case "adminUserInfo":
						adminUserInfo(br, pw, cs, oos);
						break;
					case "createTeam":
						createTeam(br, pw, cs);
						break;
					case "createPlayer":
						createPlayer(br, pw, cs);
						break;
					case "deleteTeam":
						deleteTeam(br, pw, cs);
						break;
					case "deletePlayer":
						deletePlayer(br, pw, cs);
						break;
					case "defaultTeamCreate":
						defaultTeamCreate(br, pw, cs);
						break;
					case "defaultPlayerCreate":
						defaultPlayerCreate(br, pw, cs);
						break;
					case "recruitmentPlayer":
						recruitmentPlayer(br, pw, cs);
						break;
					case "userDeletePlayer":
						userDeletePlayer(br, pw, cs);
						break;
					case "sellPlayer":
						sellPlayer(br, pw, cs);
						break;
					case "ready":
						ready(br, pw, cs);
						break;
					case "gamePlay":
						gamePlay(br, pw);
						break;
					case "exit":
						exit(br, pw, cs);
						return;
				}
			}
		} catch (IOException e) {
			System.out.println("클라이언트 연결 종료 : " + e.getMessage());
		} finally {
			br.close();
			pw.close();
			if (oos != null) {
				oos.flush();
				oos.close();
			}
		}
	}

	public static void ready(BufferedReader br, PrintWriter pw, Socket cs) throws IOException {
		String sessionId = br.readLine();
		if (gameUserList.size() >= MAX_GAME_PLAYER) {
			pw.println("fail");
			return;
		}
		GamerVO user = GamerManager.getUserBySessionId(sessionId);
		if (user != null) {
			if (user.getPlayers().size() < 11) {
				pw.println("fail");
			} else {
				pw.println("pass");
				gameUserList.put(cs, user);
				pw.println("A / B 선택");
				String playerAB = br.readLine();
				ArrayList<PlayerVO> playerList = (ArrayList<PlayerVO>) user.getPlayers();
				Collections.sort(playerList);
				gamingUserList.put(playerAB, playerList);
				gamingUserListAorB.put(sessionId, playerAB);
				System.out.println(cs + "게임 대기열 입장");
			}
		} else {
			pw.println("fail");
		}
	}

	public static void gamePlay(BufferedReader br, PrintWriter pw) throws IOException, InterruptedException {
		String sessionId = br.readLine();
		writers.add(pw);

		boolean playerA = false;
		for (Map.Entry<String, String> entry : gamingUserListAorB.entrySet()) {
			if (entry.getKey().equals(sessionId)) {
				if (entry.getValue().equals("A")) {
					playerA = true;
					break;
				}
			}
		}

		ArrayList<PlayerVO> aPlayerList = null;
		ArrayList<PlayerVO> bPlayerList = null;
		String playerAa = null;
		String playerBb = null;
		for (Map.Entry<String, ArrayList<PlayerVO>> entry : gamingUserList.entrySet()) {
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
				PlayerVO att = null;
				PlayerVO att2 = null;
				PlayerVO def = null;
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
						if ((att.getPas() + att2.getPas()) / 2 > def.getDef()) {
							broadcastMessage((playerA && turn.get() ? playerAa : playerBb) + " 패스 성공");
							inGameTurn++;
							round++;
						} else {
							broadcastMessage((playerA && turn.get() ? playerAa : playerBb) + " 패스 실패");
							inGameTurn = 0;
							turn.set(!turn.get());
							round++;
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
						if (att.getSho() > def.getDef()) {
							broadcastMessage((playerA && turn.get() ? playerAa : playerBb) + " 슛 성공");
							scores.put((playerA && turn.get() ? playerAa : playerBb), 1);
							inGameTurn = 0;
							turn.set(!turn.get());
							round++;
						} else {
							broadcastMessage((playerA && turn.get() ? playerAa : playerBb) + " 슛 실패");
							inGameTurn = 0;
							turn.set(!turn.get());
							round++;
						}
					}
				}
				if (scores.size() == 2) {
					String winner = scores.entrySet().stream().max(Map.Entry.comparingByValue()).get().getKey();
					broadcastMessage(winner + " 승리");
				}
			} else {
				pw.println("수비 턴");
			}
			Thread.sleep(3000);
		}
		pw.println("gameEnd");
		gamingUserList.clear();
		scores.clear();
		gameUserList.clear();
		writers.clear();
		round = 0;
	}

	public static void broadcastMessage(String msg) {
		for (PrintWriter writer : writers) {
			writer.println(msg);
		}
	}

	public static void register(BufferedReader br, PrintWriter pw, ObjectOutputStream oos, Socket cs)
			throws IOException {
		System.out.println(cs + " 회원가입 요청");
		String userId = br.readLine();
		String userPw = br.readLine();
		teamInfo(oos);
		String teamName = br.readLine();
		boolean isReg = GamerManager.register(userId, userPw, teamName);
		pw.println(isReg ? "pass" : "fail");
	}

	public static void login(BufferedReader br, PrintWriter pw, Socket cs) throws IOException {
		System.out.println(cs + " 로그인 시도");
		String userId = br.readLine();
		String userPw = br.readLine();
		String sessionId = GamerManager.login(userId, userPw);
		pw.println((sessionId != null) ? sessionId : "fail");
	}

	public static void adminLogin(BufferedReader br, PrintWriter pw, Socket cs) throws IOException {
		System.out.println(cs + " 어드민 로그인 시도");
		String userId = br.readLine();
		String userPw = br.readLine();
		String sessionId = GamerManager.adminLogin(userId, userPw);
		pw.println((sessionId != null) ? sessionId : "fail");
	}

	public static void teamInfo(ObjectOutputStream oos) throws IOException {
		List<ClubVO> teamList = ClubManager.loadTeamList();
		oos.writeObject(teamList);
		oos.flush();
	}

	public static void playerInfo(ObjectOutputStream oos) throws IOException {
		List<PlayerVO> player = PlayerManager.loadPlayerList();
		oos.writeObject(player);
		oos.flush();
	}

	public static void adminUserInfo(BufferedReader br, PrintWriter pw, Socket cs, ObjectOutputStream oos)
			throws IOException {
		String sessionId = br.readLine();
		boolean isAdmin = GamerManager.adminCheck(sessionId);
		if (isAdmin) {
			pw.println("pass");
			List<GamerVO> userList = GamerManager.loadUserList();
			oos.writeObject(userList);
			oos.flush();
		} else {
			pw.println("fail");
		}
	}

	public static void myInfo(BufferedReader br, PrintWriter pw, ObjectOutputStream oos) throws IOException {
		String sessionId = br.readLine();
		GamerVO user = GamerManager.getUserBySessionId(sessionId);
		if (user != null) {
			pw.println("pass");
			oos.writeObject(user);
			oos.flush();
		} else {
			pw.println("fail");
		}
	}

	public static void userInfo(ObjectOutputStream oos) throws IOException {
		List<GamerVO> userList = GamerManager.loadUserList();
		oos.writeObject(userList);
		oos.flush();
	}

	public static void createTeam(BufferedReader br, PrintWriter pw, Socket cs) throws IOException {
		System.out.println(cs + " 팀 생성");
		String sessionId = br.readLine();
		String teamName = br.readLine();
		boolean isAdmin = GamerManager.adminCheck(sessionId);
		ArrayList<PlayerVO> newPlayers = new ArrayList<>();
		for (int i = 0; i < 11; i++) {
			String playerName = br.readLine();
			int playerNumber = Integer.parseInt(br.readLine());
			int playerSho = Integer.parseInt(br.readLine());
			int playerPas = Integer.parseInt(br.readLine());
			int playerDef = Integer.parseInt(br.readLine());
			String playerPosition = br.readLine();
			int playerPrice = Integer.parseInt(br.readLine());
			PlayerVO player = new PlayerVO(playerName, playerNumber, playerSho, playerPas, playerDef, playerPosition,
					playerPrice);
			newPlayers.add(player);
		}
		if (isAdmin) {
			boolean createTeam = ClubManager.createTeam(teamName, newPlayers);
			List<PlayerVO> playerList = new ArrayList<PlayerVO>();
			List<ClubVO> teamList = ClubManager.loadTeamList();
			for (ClubVO team : teamList) {
				List<PlayerVO> players = team.getPlayers();
				playerList.addAll(players);
			}
			PlayerManager.savePlayerList(playerList);
			pw.println(createTeam ? "pass" : "fail");
		} else {
			pw.println("fail");
		}
	}

	public static void createPlayer(BufferedReader br, PrintWriter pw, Socket cs) throws IOException {
		System.out.println(cs + " 선수 생성");
		String sessionId = br.readLine();
		String playerName = br.readLine();
		int playerNumber = Integer.parseInt(br.readLine());
		int playerSho = Integer.parseInt(br.readLine());
		int playerPas = Integer.parseInt(br.readLine());
		int playerDef = Integer.parseInt(br.readLine());
		String playerPosition = br.readLine();
		int playerPrice = Integer.parseInt(br.readLine());

		boolean isAdmin = GamerManager.adminCheck(sessionId);
		if (isAdmin) {
			boolean createPlayer = PlayerManager.createPlayer(playerName, playerNumber, playerSho, playerPas, playerDef,
					playerPosition, playerPrice);
			pw.println(createPlayer ? "pass" : "fail");
		} else {
			pw.println("fail");
		}
	}

	public static void deleteTeam(BufferedReader br, PrintWriter pw, Socket cs) throws IOException {
		System.out.println(cs + " 팀 삭제");
		String sessionId = br.readLine();
		String teamName = br.readLine();

		boolean isAdmin = GamerManager.adminCheck(sessionId);
		if (isAdmin) {
			boolean deleteTeam = ClubManager.deleteTeam(teamName);
			pw.println(deleteTeam ? "pass" : "fail");
		} else {
			pw.println("fail");
		}
	}

	public static void deletePlayer(BufferedReader br, PrintWriter pw, Socket cs) throws IOException {
		System.out.println(cs + " 선수 삭제");
		String sessionId = br.readLine();
		String playerName = br.readLine();
		int playerNumber = Integer.parseInt(br.readLine());
		String playerPosition = br.readLine();
		boolean isAdmin = GamerManager.adminCheck(sessionId);
		if (isAdmin) {
			boolean deletePlayer = PlayerManager.deletePlayer(playerName, playerNumber, playerPosition);
			pw.println(deletePlayer ? "pass" : "fail");
		} else {
			pw.println("fail");
		}
	}

	public static void defaultTeamCreate(BufferedReader br, PrintWriter pw, Socket cs) throws IOException {
		System.out.println(cs + " 팀 목록 초기화");
		String sessionId = br.readLine();
		boolean isAdmin = GamerManager.adminCheck(sessionId);
		if (isAdmin) {
			ClubManager.defaultTeamCreate();
		} else {
			pw.println("fail");
		}
		pw.println("pass");
	}

	public static void defaultPlayerCreate(BufferedReader br, PrintWriter pw, Socket cs) throws IOException {
		System.out.println(cs + " 선수 목록 초기화");
		String sessionId = br.readLine();
		boolean isAdmin = GamerManager.adminCheck(sessionId);
		if (isAdmin) {
			PlayerManager.defaultPlayerCreate();
		} else {
			pw.println("fail");
		}
		pw.println("pass");
	}

	public static void userDeletePlayer(BufferedReader br, PrintWriter pw, Socket cs) throws IOException {
		System.out.println(cs + " 방출");
		String sessionId = br.readLine();
		String playerName = br.readLine();
		int playerNumber = Integer.parseInt(br.readLine());
		String playerPosition = br.readLine();
		ArrayList<GamerVO> userList = (ArrayList<GamerVO>) GamerManager.loadUserList();
		boolean userFound = false;
		for (GamerVO user : userList) {
			if (user.getSessionId().equals(sessionId)) {
				userFound = true;
				ArrayList<PlayerVO> userPlayers = (ArrayList<PlayerVO>) user.getPlayers();
				for (PlayerVO player : userPlayers) {
					if (player.getName().equals(playerName) && player.getNumber() == playerNumber
							&& player.getPosition().equals(playerPosition)) {
						user.removePlayer(player);
						GamerManager.saveUserList(userList);
						pw.println("pass");
						return;
					}
				}
				pw.println("fail");
				return;
			}
		}
		if (!userFound) {
			pw.println("fail");
		}
	}

	public static void sellPlayer(BufferedReader br, PrintWriter pw, Socket cs) throws IOException {
		System.out.println(cs + " 판매");
		String sessionId = br.readLine();
		String playerName = br.readLine();
		int playerNumber = Integer.parseInt(br.readLine());
		String playerPosition = br.readLine();
		ArrayList<GamerVO> userList = (ArrayList<GamerVO>) GamerManager.loadUserList();
		boolean userFound = false;
		for (GamerVO user : userList) {
			if (user.getSessionId().equals(sessionId)) {
				userFound = true;
				ArrayList<PlayerVO> userPlayers = (ArrayList<PlayerVO>) user.getPlayers();
				for (PlayerVO player : userPlayers) {
					if (player.getName().equals(playerName) && player.getNumber() == playerNumber
							&& player.getPosition().equals(playerPosition)) {
						user.sellPlayer(player);
						GamerManager.saveUserList(userList);
						pw.println("pass");
						return;
					}
				}
				pw.println("fail");
				return;
			}
		}
		if (!userFound) {
			pw.println("fail");
		}
	}

	public static void recruitmentPlayer(BufferedReader br, PrintWriter pw, Socket cs) throws IOException {
		System.out.println(cs + " 영입");
		String sessionId = br.readLine();
		String playerName = br.readLine();
		int playerNumber = Integer.parseInt(br.readLine());
		String playerPosition = br.readLine();

		ArrayList<GamerVO> userList = (ArrayList<GamerVO>) GamerManager.loadUserList();
		ArrayList<PlayerVO> playerList = (ArrayList<PlayerVO>) PlayerManager.loadPlayerList();
		boolean userFound = false;
		boolean playerFound = false;

		for (GamerVO user : userList) {
			if (user.getSessionId().equals(sessionId)) {
				userFound = true;
				ArrayList<PlayerVO> userPlayers = (ArrayList<PlayerVO>) user.getPlayers();

				for (PlayerVO player : userPlayers) {
					if (player.getName().equals(playerName) && player.getNumber() == playerNumber
							&& player.getPosition().equals(playerPosition)) {
						pw.println("fail");
						return;
					}
				}

				for (PlayerVO player : playerList) {
					if (player.getName().equals(playerName) && player.getNumber() == playerNumber
							&& player.getPosition().equals(playerPosition)) {
						playerFound = true;
						if (user.getBalance() > player.getPrice()) {
							boolean addPlayer = user.addPlayer(player);
							GamerManager.saveUserList(userList);
							if (addPlayer) {
								pw.println("pass");
							} else {
								pw.println("fail");
							}
							return;
						}
					}
				}
			}
		}
		if (!userFound || !playerFound) {
			pw.println("fail");
		}
	}

	public static void exit(BufferedReader br, PrintWriter pw, Socket cs) throws IOException {
		System.out.println(cs + " 종료");
		String sessionId = br.readLine();
		if (sessionId != null && !sessionId.isEmpty()) {
			GamerManager.logout(sessionId);
		}
	}
}