package football;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.List;

public class Client {
	public static final int PORT = 7777;

	public static void main(String[] args) {
		try (Socket cs = new Socket("localhost", PORT)) {
			String sessionId = null;
			boolean isAdmin = false;
			BufferedReader br = new BufferedReader(new InputStreamReader(cs.getInputStream()));
			BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
			PrintWriter pw = new PrintWriter(cs.getOutputStream(), true);
			ObjectInputStream ois = new ObjectInputStream(cs.getInputStream());
			int menu = 0;
			String menuChoice = null;

			while (true) {
				if (sessionId == null) {
					System.out.println("1. 회원가입 2. 로그인 3. 관리자 4. 종료");
					menu = Integer.parseInt(input.readLine());
					switch (menu) {
					case 1:
						menuChoice = "register";
						break;
					case 2:
						menuChoice = "login";
						break;
					case 3:
						menuChoice = "adminLogin";
						break;
					case 4:
						menuChoice = "exit";
						break;
					default:
						System.out.println("올바른 값 입력");
						break;
					}
				} else if (isAdmin) {
					System.out.println(
							"1. 팀 목록 2. 선수 목록 3. 유저 목록 4. 팀 생성 5. 선수 생성 6. 팀 삭제 7. 선수 삭제 8. 팀 목록 초기화 9. 선수 목록 초기화 10. 종료");
					menu = Integer.parseInt(input.readLine());
					switch (menu) {
					case 1:
						menuChoice = "teamInfo";
						break;
					case 2:
						menuChoice = "playerInfo";
						break;
					case 3:
						menuChoice = "userInfo";
						break;
					case 4:
						menuChoice = "createTeam";
						break;
					case 5:
						menuChoice = "createPlayer";
						break;
					case 6:
						menuChoice = "deleteTeam";
						break;
					case 7:
						menuChoice = "deletePlayer";
						break;
					case 8:
						menuChoice = "defaultTeamCreate";
						break;
					case 9:
						menuChoice = "defaultPlayerCreate";
						break;
					case 10:
						menuChoice = "exit";
						break;
					default:
						System.out.println("올바른 값 입력");
						break;
					}
				} else {
					System.out.println("1. 팀 목록 2. 선수 목록 3. 순위 4. 이적 시장 5. 내 팀 관리 6. 플레이 7. 종료");
					menu = Integer.parseInt(input.readLine());
					switch (menu) {
					case 1:
						menuChoice = "teamInfo";
						break;
					case 2:
						menuChoice = "playerInfo";
						break;
					case 3:
						menuChoice = "userInfo";
						break;
					case 4:
						menuChoice = "transMarket";
						break;
					case 5:
						System.out.println("1. 방출 2. 판매 3. 영입");
						int inMenu = Integer.parseInt(input.readLine());
						switch (inMenu) {
						case 1:
							menuChoice = "userDeletePlayer";
							break;
						case 2:
							menuChoice = "sellPlayer";
							break;
						case 3:
							menuChoice = "transMarket";
							break;
						default:
							System.out.println("올바른 값 입력");
							break;
						}
						break;
					case 6:
						menuChoice = "play";
						break;
					case 7:
						menuChoice = "exit";
						break;
					default:
						System.out.println("올바른 값 입력");
						break;
					}
				}

				switch (menuChoice) {
				case "register":
					register(pw, input, br, ois);
					break;
				case "login":
					String loginCheck = login(pw, input, br);
					if (loginCheck != null) {
						sessionId = loginCheck;
						System.out.println("로그인 성공");
					} else {
						System.out.println("로그인 실패");
					}
					break;
				case "adminLogin":
					loginCheck = adminLogin(pw, input, br);
					if (loginCheck != null) {
						sessionId = loginCheck;
						isAdmin = true;
						System.out.println("로그인 성공");
					} else {
						System.out.println("로그인 실패");
					}
					break;
				case "teamInfo":
					pw.println("teamInfo");
					teamInfo(ois);
					break;
				case "playerInfo":
					pw.println("playerInfo");
					playerInfo(ois);
					break;
				case "userInfo":
					pw.println("userInfo");
					userInfo(ois);
					break;
				case "createTeam":
					createTeam(pw, input, br, sessionId);
					break;
				case "createPlayer":
					createPlayer(pw, input, br, sessionId);
					break;
				case "deleteTeam":
					deleteTeam(pw, input, br, sessionId);
					break;
				case "deletePlayer":
					deletePlayer(pw, input, br, sessionId);
					break;
				case "defaultTeamCreate":
					defaultTeamCreate(pw, br, sessionId);
					break;
				case "defaultPlayerCreate":
					defaultPlayerCreate(pw, br, sessionId);
					break;
				case "transMarket":
					pw.println("playerInfo");
					playerInfo(ois);
					recruitmentPlayer(pw, input, br, sessionId);
					break;
				case "userDeletePlayer":
					userDeletePlayer(pw, input, br, sessionId);
					break;
				case "sellPlayer":
					sellPlayer(pw, input, br, sessionId);
					break;
				case "exit":
					exit(pw, sessionId);
					return;
				}
			}
		} catch (UnknownHostException e) {
			System.out.println("소켓 에러 " + e.getMessage());
		} catch (IOException e) {
			System.out.println("소켓 에러 " + e.getMessage());
		}
	}

	public static void register(PrintWriter pw, BufferedReader input, BufferedReader br, ObjectInputStream ois)
			throws IOException {
		pw.println("register");

		System.out.print("생성할 아이디 : ");
		String userId = input.readLine();
		pw.println(userId);

		System.out.print("비밀번호 : ");
		String userPw = input.readLine();
		pw.println(userPw);

		teamInfo(ois);

		System.out.print("팀 선택 : ");
		String teamName = input.readLine();
		pw.println(teamName);

		String response = br.readLine();
		System.out.println(response.equals("pass") ? "회원가입 완료" : "회원가입 실패");
	}

	public static String login(PrintWriter pw, BufferedReader input, BufferedReader br) throws IOException {
		pw.println("login");
		System.out.print("아이디 : ");
		String userId = input.readLine();
		System.out.print("비밀번호 : ");
		String userPw = input.readLine();

		pw.println(userId);
		pw.println(userPw);

		String response = br.readLine();
		if (!response.equals("fail")) {
			return response;
		} else {
			return null;
		}
	}

	public static String adminLogin(PrintWriter pw, BufferedReader input, BufferedReader br) throws IOException {
		pw.println("adminLogin");
		System.out.print("아이디: ");
		String userId = input.readLine();
		System.out.print("비밀번호: ");
		String userPw = input.readLine();

		pw.println(userId);
		pw.println(userPw);

		String response = br.readLine();
		if (!response.equals("fail")) {
			return response;
		} else {
			return null;
		}
	}

	public static void teamInfo(ObjectInputStream ois) {
		try {
			List<Team> teamList = (List<Team>) ois.readObject();
			System.out.println(teamList);
		} catch (ClassNotFoundException | IOException e) {
			System.out.println("팀 목록 수신 오류 : " + e.getMessage());
		}
	}

	public static void playerInfo(ObjectInputStream ois) {
		try {
			List<Player> playerList = (List<Player>) ois.readObject();
			System.out.println(playerList);
		} catch (ClassNotFoundException | IOException e) {
			System.out.println("선수 목록 수신 오류 : " + e.getMessage());
		}
	}

	public static void userInfo(ObjectInputStream ois) {
		try {
			List<User> userList = (List<User>) ois.readObject();
			System.out.println(userList);
		} catch (ClassNotFoundException | IOException e) {
			System.out.println("유저 목록 수신 오류 : " + e.getMessage());
		}
	}

	public static void createTeam(PrintWriter pw, BufferedReader input, BufferedReader br, String sessionId)
			throws IOException {
		pw.println("createTeam");
		pw.println(sessionId);
		System.out.print("생성할 팀 이름 : ");
		String teamName = input.readLine();
		pw.println(teamName);

		for (int i = 0; i < 11; i++) {
			System.out.print("생성할 선수 이름 : ");
			String playerName = input.readLine();
			System.out.print("생성할 선수 번호 : ");
			String playerNumber = input.readLine();
			System.out.print("생성할 선수 슛 능력치 : ");
			String playerSho = input.readLine();
			System.out.print("생성할 선수 패스 능력치 : ");
			String playerPas = input.readLine();
			System.out.print("생성할 선수 수비 능력치 : ");
			String playerDef = input.readLine();
			String playerPosition = null;

			while (true) {
				System.out.print("생성할 선수 포지션 (gk, df, mf, fw) : ");
				String str = input.readLine();
				if (str.matches("gk|df|mf|fw")) {
					playerPosition = str;
					break;
				} else {
					System.out.println("올바른 포지션 입력");
				}
			}
			System.out.print("생성할 선수 가격 : ");
			String playerPrice = input.readLine();

			pw.println(playerName);
			pw.println(playerNumber);
			pw.println(playerSho);
			pw.println(playerPas);
			pw.println(playerDef);
			pw.println(playerPosition);
			pw.println(playerPrice);
		}

		String response = br.readLine();
		System.out.println(response.equals("pass") ? "생성 완료" : "생성 실패");
	}

	public static void createPlayer(PrintWriter pw, BufferedReader input, BufferedReader br, String sessionId)
			throws IOException {
		pw.println("createPlayer");
		pw.println(sessionId);

		System.out.print("생성할 선수 이름 : ");
		String playerName = input.readLine();
		System.out.print("생성할 선수 번호 : ");
		String playerNumber = input.readLine();
		System.out.print("생성할 선수 슛 능력치 : ");
		String playerSho = input.readLine();
		System.out.print("생성할 선수 패스 능력치 : ");
		String playerPas = input.readLine();
		System.out.print("생성할 선수 수비 능력치 : ");
		String playerDef = input.readLine();
		String playerPosition = null;

		while (true) {
			System.out.print("생성할 선수 포지션 (gk, df, mf, fw) : ");
			String str = input.readLine();
			if (str.matches("gk|df|mf|fw")) {
				playerPosition = str;
				break;
			} else {
				System.out.println("올바른 포지션 입력");
			}
		}
		System.out.print("생성할 선수 가격 : ");
		String playerPrice = input.readLine();

		pw.println(playerName);
		pw.println(playerNumber);
		pw.println(playerSho);
		pw.println(playerPas);
		pw.println(playerDef);
		pw.println(playerPosition);
		pw.println(playerPrice);

		String response = br.readLine();
		System.out.println(response.equals("pass") ? "생성 완료" : "생성 실패");
	}

	public static void deleteTeam(PrintWriter pw, BufferedReader input, BufferedReader br, String sessionId)
			throws IOException {
		pw.println("deleteTeam");
		pw.println(sessionId);

		System.out.print("삭제할 팀 이름 : ");
		String teamName = input.readLine();

		pw.println(teamName);

		String response = br.readLine();
		System.out.println(response.equals("pass") ? "삭제 완료" : "삭제 실패");
	}

	public static void deletePlayer(PrintWriter pw, BufferedReader input, BufferedReader br, String sessionId)
			throws IOException {
		pw.println("deletePlayer");
		pw.println(sessionId);

		System.out.print("삭제할 선수 이름 : ");
		String playerName = input.readLine();
		System.out.print("삭제할 선수 번호 : ");
		String playerNumber = input.readLine();
		System.out.print("삭제할 선수 포지션 : ");
		String playerPosition = input.readLine();

		pw.println(playerName);
		pw.println(playerNumber);
		pw.println(playerPosition);

		String response = br.readLine();
		System.out.println(response.equals("pass") ? "삭제 완료" : "삭제 실패");
	}

	public static void defaultTeamCreate(PrintWriter pw, BufferedReader br, String sessionId) throws IOException {
		pw.println("defaultTeamCreate");
		pw.println(sessionId);

		String response = br.readLine();
		System.out.println(response.equals("pass") ? "초기화 완료" : "초기화 실패");
	}

	public static void defaultPlayerCreate(PrintWriter pw, BufferedReader br, String sessionId) throws IOException {
		pw.println("defaultPlayerCreate");
		pw.println(sessionId);

		String response = br.readLine();
		System.out.println(response.equals("pass") ? "초기화 완료" : "초기화 실패");
	}

	public static void recruitmentPlayer(PrintWriter pw, BufferedReader input, BufferedReader br, String sessionId)
			throws IOException {
		pw.println("recruitmentPlayer");
		pw.println(sessionId);
		System.out.print("영입할 선수 이름 : ");
		String playerName = input.readLine();
		System.out.print("영입할 선수 번호 : ");
		String playerNumber = input.readLine();
		System.out.print("영입할 선수 포지션 : ");
		String playerPosition = input.readLine();

		pw.println(playerName);
		pw.println(playerNumber);
		pw.println(playerPosition);

		String response = br.readLine();
		if (response.equals("pass")) {
			System.out.println("영입 완료");
		} else {
			System.out.println("영입 실패");
		}
	}

	public static void userDeletePlayer(PrintWriter pw, BufferedReader input, BufferedReader br, String sessionId)
			throws IOException {
		pw.println("userDeletePlayer");
		pw.println(sessionId);

		System.out.print("방출할 선수 이름 : ");
		String playerName = input.readLine();
		System.out.print("방출할 선수 번호 : ");
		String playerNumber = input.readLine();
		System.out.print("방출할 선수 포지션 : ");
		String playerPosition = input.readLine();

		pw.println(playerName);
		pw.println(playerNumber);
		pw.println(playerPosition);

		String response = br.readLine();
		if (response.equals("pass")) {
			System.out.println("방출  완료");
		} else {
			System.out.println("방출 실패");
		}
	}

	public static void sellPlayer(PrintWriter pw, BufferedReader input, BufferedReader br, String sessionId)
			throws IOException {
		pw.println("sellPlayer");
		pw.println(sessionId);

		System.out.print("방출할 선수 이름 : ");
		String playerName = input.readLine();
		System.out.print("방출할 선수 번호 : ");
		String playerNumber = input.readLine();
		System.out.print("방출할 선수 포지션 : ");
		String playerPosition = input.readLine();

		pw.println(playerName);
		pw.println(playerNumber);
		pw.println(playerPosition);

		String response = br.readLine();
		if (response.equals("pass")) {
			System.out.println("판매 완료");
		} else {
			System.out.println("판매 실패");
		}
	}

	public static void exit(PrintWriter pw, String sessionId) {
		pw.println("exit");
		if (sessionId != null) {
			pw.println(sessionId);
		}
		System.out.println("종료");
	}
}