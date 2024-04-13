package football;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class UserManager {
	private static final String USERFILE_PATH = "user.dba";

	public static User getUserBySessionId(String sessionId) {
		List<User> userList = loadUserList();
		for (User user : userList) {
			if (user.getSessionId() != null && user.getSessionId().equals(sessionId)) {
				return user;
			}
		}
		return null;
	}

	public static boolean register(String userId, String userPw, String teamName) {
		List<User> userList = loadUserList();
		for (User user : userList) {
			if (user.getId().equals(userId)) {
				return false;
			}
		}
		List<Team> teamList = TeamManager.loadTeamList();
		for (Team team : teamList) {
			if (team.getName().equals(teamName)) {
				List<Player> selectTeamPlayers = team.getPlayers();
				User user = new User(userId, userPw, team.getName(), selectTeamPlayers);
				userList.add(user);
				saveUserList(userList);
				return true;
			}
		}
		return false;
	}

	public static String login(String userId, String userPw) {
		List<User> userList = loadUserList();
		for (User user : userList) {
			if (user.getId().equals(userId) && user.getPw().equals(userPw)) {
				if (user.getSessionId() == null) {
					String uuid = UUID.randomUUID().toString();
					user.setSessionId(uuid);
					saveUserList(userList);
					return uuid;
				}
			}
		}
		return null;
	}

	public static String adminLogin(String userId, String userPw) {
		List<User> userList = loadUserList();
		for (User user : userList) {
			if (user.getId().equals(userId) && user.getPw().equals(userPw) && user.isAdmin()) {
				if (user.getSessionId() == null) {
					String uuid = UUID.randomUUID().toString();
					user.setSessionId(uuid);
					saveUserList(userList);
					return uuid;
				}
			}
		}
		return null;
	}

	public static boolean adminCheck(String sessionId) {
		List<User> userList = loadUserList();
		for (User user : userList) {
			if (user.getSessionId() != null && user.getSessionId().equals(sessionId) && user.isAdmin()) {
				return true;
			}
		}
		return false;
	}

	public static void logout(String sessionId) {
		List<User> userList = loadUserList();
		for (User user : userList) {
			if (user.getSessionId() != null && user.getSessionId().equals(sessionId)) {
				user.setSessionId(null);
				saveUserList(userList);
				return;
			}
		}
	}

	public static synchronized List<User> loadUserList() {
		List<User> userList = new ArrayList<>();
		try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(USERFILE_PATH))) {
			userList = (List<User>) ois.readObject();
		} catch (FileNotFoundException e) {
		} catch (IOException | ClassNotFoundException e) {
			System.out.println("유저 목록 읽기 에러" + e.getMessage());
		}
		return userList;
	}

	public static synchronized void saveUserList(List<User> userList) {
		try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(USERFILE_PATH))) {
			oos.writeObject(userList);
		} catch (IOException e) {
			System.out.println("유저 목록 저장 에러" + e.getMessage());
		}
	}

	public static void logoutAllUsers() {
		List<User> userList = loadUserList();
		for (User user : userList) {
			if (user.getSessionId() != null) {
				user.setSessionId(null);
			}
		}
		saveUserList(userList);
	}
}