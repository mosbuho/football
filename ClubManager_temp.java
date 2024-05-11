package model;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

public class ClubManager {
	private static final String TEAMFILE_PATH = "team.dba";

	public static boolean createTeam(String teamName, ArrayList<PlayerVO> players) {
		List<ClubVO> teamList = loadTeamList();
		for (ClubVO team : teamList) {
			if (team.getName().equals(teamName)) {
				return false;
			}
		}
		ClubVO team = new ClubVO(teamName, players);
		teamList.add(team);
		saveTeamList(teamList);
		return true;
	}

	public static boolean deleteTeam(String teamName) {
		List<ClubVO> teamList = loadTeamList();
		for (ClubVO team : teamList) {
			if (team.getName().equals(teamName)) {
				teamList.remove(team);
				saveTeamList(teamList);
				return true;
			}
		}
		return false;
	}

	public static synchronized List<ClubVO> loadTeamList() {
		List<ClubVO> teamList = new ArrayList<>();
		try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(TEAMFILE_PATH))) {
			teamList = (List<ClubVO>) ois.readObject();
		} catch (FileNotFoundException e) {
		} catch (IOException | ClassNotFoundException e) {
			System.out.println("팀 목록 읽기 에러" + e.getMessage());
		}
		return teamList;
	}

	public static synchronized void saveTeamList(List<ClubVO> teamList) {
		try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(TEAMFILE_PATH))) {
			oos.writeObject(teamList);
		} catch (IOException e) {
			System.out.println("팀 목록 저장 에러" + e.getMessage());
		}
	}

	public static void defaultTeamCreate() {
		List<ClubVO> teamList = new ArrayList<>();
		ClubVO team1 = new ClubVO("Tottenham Spurs");
		team1.addPlayer(new PlayerVO("Heung Min Son", 7, 98, 80, 49, "fw", 98));
		team1.addPlayer(new PlayerVO("Richarlison", 9, 79, 72, 52, "fw", 46));
		team1.addPlayer(new PlayerVO("Brennan Johnson", 22, 74, 67, 44, "fw", 36));
		team1.addPlayer(new PlayerVO("James Maddison", 10, 81, 86, 63, "mf", 88));
		team1.addPlayer(new PlayerVO("Rodrigo Bentancur", 30, 67, 81, 67, "mf", 39));
		team1.addPlayer(new PlayerVO("Pierre-Emile Højbjerg", 5, 73, 78, 69, "mf", 52));
		team1.addPlayer(new PlayerVO("Pedro Porro", 23, 73, 77, 89, "df", 44));
		team1.addPlayer(new PlayerVO("Guglielmo Vicario", 13, 98, 80, 91, "gk", 39));
		team1.addPlayer(new PlayerVO("Cristian Romero", 17, 46, 59, 82, "df", 85));
		team1.addPlayer(new PlayerVO("Micky van de Ven", 37, 43, 59, 87, "df", 26));
		team1.addPlayer(new PlayerVO("Destiny Udogie", 4, 63, 69, 80, "df", 39));
		teamList.add(team1);

		ClubVO team2 = new ClubVO("Manchester City");
		team2.addPlayer(new PlayerVO("Erling Haaland", 9, 93, 66, 46, "fw", 195));
		team2.addPlayer(new PlayerVO("Phil Foden", 47, 79, 82, 42, "fw", 117));
		team2.addPlayer(new PlayerVO("Jack Grealishn", 10, 76, 84, 49, "fw", 156));
		team2.addPlayer(new PlayerVO("Kevin De Bruyne", 17, 88, 94, 67, "mf", 208));
		team2.addPlayer(new PlayerVO("Rodri", 16, 73, 80, 62, "mf", 114));
		team2.addPlayer(new PlayerVO("Bernardo Silva", 20, 78, 86, 65, "mf", 156));
		team2.addPlayer(new PlayerVO("Kyle Walker", 2, 63, 77, 83, "df", 91));
		team2.addPlayer(new PlayerVO("Nathan Aké", 6, 53, 72, 87, "df", 83));
		team2.addPlayer(new PlayerVO("Joško Gvardiol", 24, 54, 69, 89, "df", 104));
		team2.addPlayer(new PlayerVO("Sergio Gómez", 21, 70, 75, 81, "df", 39));
		team2.addPlayer(new PlayerVO("Stefan Ortega", 18, 73, 86, 92, "gk", 28));
		teamList.add(team2);
		saveTeamList(teamList);

		Team team3 = new Team("Fc Barcelona");
		team3.addPlayer(new Player("Lewandowski", 9, 91, 80, 44, "fw", 120));
		team3.addPlayer(new Player("Lamin Yamal", 27, 95, 80, 45, "fw", 125));
		team3.addPlayer(new Player("Joao Felix", 14, 79, 78, 40, "fw", 50));
		team3.addPlayer(new Player("Frenkie DeJong", 21, 69, 86, 77, "mf", 130));
		team3.addPlayer(new Player("Ilkay Gundogan", 22, 81, 84, 73, "mf", 85));
		team3.addPlayer(new Player("Pedri", 8, 69, 82, 70, "mf", 130));
		team3.addPlayer(new Player("Balde", 3, 79, 78, 35, "df", 115));
		team3.addPlayer(new Player("Jules Kounde", 23, 45, 68, 86, "df", 95));
		team3.addPlayer(new Player("Ronald Araujo", 4, 51, 65, 86, "df", 115));
		team3.addPlayer(new Player("Joao Cancelo", 2, 73, 85, 80, "df", 80));
		team3.addPlayer(new Player("Marc-Andre Ter Stergen", 1, 85, 89, 47, "gk", 115));
		teamList.add(team3);

	}
}
