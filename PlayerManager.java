package controller;


import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

import model.ClubVO;
import model.PlayerVO;

public class PlayerManager {
	private static final String PLAYERFILE_PATH = "player.dba";

	public static boolean createPlayer(String playerName, int playerNumber, int playerSho, int playerPas,
			int playerDef, String playerPosition, int price) {
		List<PlayerVO> playerList = loadPlayerList();
		for (PlayerVO player : playerList) {
			if (player.getName().equals(playerName) && player.getNumber() == playerNumber
					&& player.getPosition().equals(playerPosition)) {
				return false;
			}
		}
		PlayerVO player = new PlayerVO(playerName, playerNumber, playerSho, playerPas, playerDef, playerPosition, price);
		playerList.add(player);
		savePlayerList(playerList);
		return true;
	}

	public static boolean deletePlayer(String playerName, int playerNumber, String playerPosition) {
		List<PlayerVO> playerList = loadPlayerList();
		for (PlayerVO player : playerList) {
			if (player.getName().equals(playerName) && player.getNumber() == playerNumber
					&& player.getPosition().equals(playerPosition)) {
				playerList.remove(player);
				savePlayerList(playerList);
				return true;
			}
		}
		return false;
	}

	public static synchronized List<PlayerVO> loadPlayerList() {
		List<PlayerVO> playerList = new ArrayList<>();
		try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(PLAYERFILE_PATH))) {
			playerList = (List<PlayerVO>) ois.readObject();
		} catch (FileNotFoundException e) {
		} catch (IOException | ClassNotFoundException e) {
			System.out.println("선수 목록 읽기 에러" + e.getMessage());
		}
		return playerList;
	}

	public static synchronized void savePlayerList(List<PlayerVO> playerList) {
		try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(PLAYERFILE_PATH))) {
			oos.writeObject(playerList);
		} catch (IOException e) {
			System.out.println("선수 목록 저장 에러" + e.getMessage());
		}
	}

	public static void defaultPlayerCreate() {
		List<PlayerVO> playerList = new ArrayList<PlayerVO>();
		List<ClubVO> teamList = ClubManager.loadTeamList();
		for (ClubVO team : teamList) {
			List<PlayerVO> players = team.getPlayers();
			playerList.addAll(players);
		}
		savePlayerList(playerList);
	}
}