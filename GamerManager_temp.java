package controller.Gamer;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import model.ClubManager;
import model.ClubVO;
import model.GamerVO;
import model.PlayerVO;

public class GamerManager {
	public static GamerVO getUserBySessionId(String sessionId) {
		List<GamerVO> userList = loadUserList();
		for (GamerVO user : userList) {
			if (user.getSessionId() != null && user.getSessionId().equals(sessionId)) {
				return user;
			}
		}
		return null;
	}

	public static boolean register(String userId, String userPw, String teamName) {
		List<GamerVO> userList = loadUserList();
		for (GamerVO user : userList) {
			if (user.getId().equals(userId)) {
				return false;
			}
		}
		List<ClubVO> teamList = ClubManager.loadTeamList();
		for (ClubVO team : teamList) {
			if (team.getName().equals(teamName)) {
				List<PlayerVO> selectTeamPlayers = team.getPlayers();
				GamerVO user = new GamerVO(userId, userPw, team.getName(), selectTeamPlayers);
				userList.add(user);
				saveUserList(userList);
				return true;
			}
		}
		return false;
	}

	public static String login(String userId, String userPw) {
		List<GamerVO> userList = loadUserList();
		for (GamerVO user : userList) {
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
		List<GamerVO> userList = loadUserList();
		for (GamerVO user : userList) {
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
		List<GamerVO> userList = loadUserList();
		for (GamerVO user : userList) {
			if (user.getSessionId() != null && user.getSessionId().equals(sessionId) && user.isAdmin()) {
				return true;
			}
		}
		return false;
	}

	public static void logout(String sessionId) {
		List<GamerVO> userList = loadUserList();
		for (GamerVO user : userList) {
			if (user.getSessionId() != null && user.getSessionId().equals(sessionId)) {
				user.setSessionId(null);
				saveUserList(userList);
				return;
			}
		}
	}

	public static synchronized List<GamerVO> loadUserList() {
		List<GamerVO> userList = new ArrayList<>();
		try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(USERFILE_PATH))) {
			userList = (List<GamerVO>) ois.readObject();
		} catch (FileNotFoundException e) {
		} catch (IOException | ClassNotFoundException e) {
			System.out.println("유저 목록 읽기 에러" + e.getMessage());
		}
		return userList;
	}

	public static synchronized void saveUserList(List<GamerVO> userList) {
		try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(USERFILE_PATH))) {
			oos.writeObject(userList);
		} catch (IOException e) {
			System.out.println("유저 목록 저장 에러" + e.getMessage());
		}
	}

	public static void logoutAllUsers() {
		List<GamerVO> userList = loadUserList();
		for (GamerVO user : userList) {
			if (user.getSessionId() != null) {
				user.setSessionId(null);
			}
		}
		saveUserList(userList);
	}
}