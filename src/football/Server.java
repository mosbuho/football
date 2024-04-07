package football;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Server {
	public static final int PORT = 7777;

	public static void main(String[] args) {
		try {
			ServerSocket ss = new ServerSocket(PORT);
			System.out.println("서버 온");

			TeamManager.defaultTeamCreate();
			PlayerManager.defaultPlayerCreate();
			UserManager.logoutAllUsers();

			while (true) {
				Socket cs = ss.accept();
				System.out.println(cs + "접속");
				new Thread(() -> {
					try {
						csHandler(cs);
					} catch (IOException e) {
						System.out.println("쓰레드 핸들러 에러" + e.getMessage());
					}
				}).start();
			}
		} catch (IOException e) {
			System.out.println("서버 소켓 에러 " + e.getMessage());
		}
	}

	public static void csHandler(Socket cs) throws IOException {
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

	public static void register(BufferedReader br, PrintWriter pw, ObjectOutputStream oos, Socket cs)
			throws IOException {
		System.out.println(cs + " 회원가입 요청");
		String userId = br.readLine();
		String userPw = br.readLine();
		teamInfo(oos);
		String teamName = br.readLine();
		boolean isReg = UserManager.register(userId, userPw, teamName);
		pw.println(isReg ? "pass" : "fail");
	}

	public static void login(BufferedReader br, PrintWriter pw, Socket cs) throws IOException {
		System.out.println(cs + " 로그인 시도");
		String userId = br.readLine();
		String userPw = br.readLine();
		String sessionId = UserManager.login(userId, userPw);
		pw.println((sessionId != null) ? sessionId : "fail");
	}

	public static void adminLogin(BufferedReader br, PrintWriter pw, Socket cs) throws IOException {
		System.out.println(cs + " 어드민 로그인 시도");
		String userId = br.readLine();
		String userPw = br.readLine();
		String sessionId = UserManager.adminLogin(userId, userPw);
		pw.println((sessionId != null) ? sessionId : "fail");
	}

	public static void teamInfo(ObjectOutputStream oos) throws IOException {
		List<Team> teamList = TeamManager.loadTeamList();
		oos.writeObject(teamList);
		oos.flush();
	}

	public static void playerInfo(ObjectOutputStream oos) throws IOException {
		List<Player> player = PlayerManager.loadPlayerList();
		oos.writeObject(player);
		oos.flush();
	}

	public static void userInfo(ObjectOutputStream oos) throws IOException {
		List<User> userList = UserManager.loadUserList();
		oos.writeObject(userList);
		oos.flush();
	}

	public static void createTeam(BufferedReader br, PrintWriter pw, Socket cs) throws IOException {
		System.out.println(cs + " 팀 생성");
		String sessionId = br.readLine();
		String teamName = br.readLine();

		boolean isAdmin = UserManager.adminCheck(sessionId);

		ArrayList<Player> newPlayers = new ArrayList<>();

		for (int i = 0; i < 11; i++) {
			String playerName = br.readLine();
			int playerNumber = Integer.parseInt(br.readLine());
			int playerSho = Integer.parseInt(br.readLine());
			int playerPas = Integer.parseInt(br.readLine());
			int playerDef = Integer.parseInt(br.readLine());
			String playerPosition = br.readLine();
			int playerPrice = Integer.parseInt(br.readLine());
			Player player = new Player(playerName, playerNumber, playerSho, playerPas, playerDef, playerPosition,
					playerPrice);
			newPlayers.add(player);
		}

		if (isAdmin) {
			boolean createTeam = TeamManager.createTeam(teamName, newPlayers);
			List<Player> playerList = new ArrayList<Player>();
			List<Team> teamList = TeamManager.loadTeamList();
			for (Team team : teamList) {
				List<Player> players = team.getPlayers();
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

		boolean isAdmin = UserManager.adminCheck(sessionId);
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

		boolean isAdmin = UserManager.adminCheck(sessionId);
		if (isAdmin) {
			boolean deleteTeam = TeamManager.deleteTeam(teamName);
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

		boolean isAdmin = UserManager.adminCheck(sessionId);
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
		boolean isAdmin = UserManager.adminCheck(sessionId);
		if (isAdmin) {
			TeamManager.defaultTeamCreate();
		} else {
			pw.println("fail");
		}
		pw.println("pass");
	}

	public static void defaultPlayerCreate(BufferedReader br, PrintWriter pw, Socket cs) throws IOException {
		System.out.println(cs + " 선수 목록 초기화");
		String sessionId = br.readLine();
		boolean isAdmin = UserManager.adminCheck(sessionId);
		if (isAdmin) {
			PlayerManager.defaultPlayerCreate();
		} else {
			pw.println("fail");
		}
		pw.println("pass");
	}

	public static void exit(BufferedReader br, PrintWriter pw, Socket cs) throws IOException {
		System.out.println(cs + " 종료");
		String sessionId = br.readLine();
		if (sessionId != null && !sessionId.isEmpty()) {
			UserManager.logout(sessionId);
		}
	}

	public static void userDeletePlayer(BufferedReader br, PrintWriter pw, Socket cs) throws IOException {
		System.out.println(cs + " 방출");
		String sessionId = br.readLine();
		String playerName = br.readLine();
		int playerNumber = Integer.parseInt(br.readLine());
		String playerPosition = br.readLine();

		ArrayList<User> userList = (ArrayList<User>) UserManager.loadUserList();
		boolean userFound = false;
		for (User user : userList) {
			if (user.getSessionId().equals(sessionId)) {
				userFound = true;
				ArrayList<Player> userPlayers = (ArrayList<Player>) user.getPlayers();

				for (Player player : userPlayers) {
					if (player.getName().equals(playerName) && player.getNumber() == playerNumber
							&& player.getPosition().equals(playerPosition)) {
						user.removePlayer(player);
						UserManager.saveUserList(userList);
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

		ArrayList<User> userList = (ArrayList<User>) UserManager.loadUserList();
		boolean userFound = false;
		for (User user : userList) {
			if (user.getSessionId().equals(sessionId)) {
				userFound = true;
				ArrayList<Player> userPlayers = (ArrayList<Player>) user.getPlayers();

				for (Player player : userPlayers) {
					if (player.getName().equals(playerName) && player.getNumber() == playerNumber
							&& player.getPosition().equals(playerPosition)) {
						user.sellPlayer(player);
						UserManager.saveUserList(userList);
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

		ArrayList<User> userList = (ArrayList<User>) UserManager.loadUserList();
		ArrayList<Player> playerList = (ArrayList<Player>) PlayerManager.loadPlayerList();
		boolean userFound = false;
		boolean playerFound = false;

		for (User user : userList) {
			if (user.getSessionId().equals(sessionId)) {
				userFound = true;
				ArrayList<Player> userPlayers = (ArrayList<Player>) user.getPlayers();

				for (Player player : userPlayers) {
					if (player.getName().equals(playerName) && player.getNumber() == playerNumber
							&& player.getPosition().equals(playerPosition)) {
						pw.println("fail");
						return;
					}
				}

				for (Player player : playerList) {
					if (player.getName().equals(playerName) && player.getNumber() == playerNumber
							&& player.getPosition().equals(playerPosition)) {
						playerFound = true;
						if (user.getBalance() > player.getPrice()) {
							boolean addPlayer = user.addPlayer(player);
							UserManager.saveUserList(userList);
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
}