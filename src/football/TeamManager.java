package football;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

public class TeamManager {
	private static final String TEAMFILE_PATH = "team.dba";

	public static boolean createTeam(String teamName, ArrayList<Player> players) {
		List<Team> teamList = loadTeamList();
		for (Team team : teamList) {
			if (team.getName().equals(teamName)) {
				return false;
			}
		}
		Team team = new Team(teamName, players);
		teamList.add(team);
		saveTeamList(teamList);
		return true;
	}

	public static boolean deleteTeam(String teamName) {
		List<Team> teamList = loadTeamList();
		for (Team team : teamList) {
			if (team.getName().equals(teamName)) {
				teamList.remove(team);
				saveTeamList(teamList);
				return true;
			}
		}
		return false;
	}

	public static synchronized List<Team> loadTeamList() {
		List<Team> teamList = new ArrayList<>();
		try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(TEAMFILE_PATH))) {
			teamList = (List<Team>) ois.readObject();
		} catch (FileNotFoundException e) {
		} catch (IOException | ClassNotFoundException e) {
			System.out.println("팀 목록 읽기 에러" + e.getMessage());
		}
		return teamList;
	}

	public static synchronized void saveTeamList(List<Team> teamList) {
		try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(TEAMFILE_PATH))) {
			oos.writeObject(teamList);
		} catch (IOException e) {
			System.out.println("팀 목록 저장 에러" + e.getMessage());
		}
	}

	public static void defaultTeamCreate() {
		List<Team> teamList = new ArrayList<>();
		Team team1 = new Team("Tottenham Spurs");
		team1.addPlayer(new Player("Heung Min Son", 7, 98, 80, 49, "fw", 98));
		team1.addPlayer(new Player("Richarlison", 9, 79, 72, 52, "fw", 46));
		team1.addPlayer(new Player("Brennan Johnson", 22, 74, 67, 44, "fw", 36));
		team1.addPlayer(new Player("James Maddison", 10, 81, 86, 63, "mf", 88));
		team1.addPlayer(new Player("Rodrigo Bentancur", 30, 67, 81, 67, "mf", 39));
		team1.addPlayer(new Player("Pierre-Emile Højbjerg", 5, 73, 78, 69, "mf", 52));
		team1.addPlayer(new Player("Pedro Porro", 23, 73, 77, 89, "df", 44));
		team1.addPlayer(new Player("Guglielmo Vicario", 13, 98, 80, 91, "gk", 39));
		team1.addPlayer(new Player("Cristian Romero", 17, 46, 59, 82, "df", 85));
		team1.addPlayer(new Player("Micky van de Ven", 37, 43, 59, 87, "df", 26));
		team1.addPlayer(new Player("Destiny Udogie", 4, 63, 69, 80, "df", 39));
		teamList.add(team1);

		Team team2 = new Team("Manchester City");
		team2.addPlayer(new Player("Erling Haaland", 9, 93, 66, 46, "fw", 195));
		team2.addPlayer(new Player("Phil Foden", 47, 79, 82, 42, "fw", 117));
		team2.addPlayer(new Player("Jack Grealishn", 10, 76, 84, 49, "fw", 156));
		team2.addPlayer(new Player("Kevin De Bruyne", 17, 88, 94, 67, "mf", 208));
		team2.addPlayer(new Player("Rodri", 16, 73, 80, 62, "mf", 114));
		team2.addPlayer(new Player("Bernardo Silva", 20, 78, 86, 65, "mf", 156));
		team2.addPlayer(new Player("Kyle Walker", 2, 63, 77, 83, "df", 91));
		team2.addPlayer(new Player("Nathan Aké", 6, 53, 72, 87, "df", 83));
		team2.addPlayer(new Player("Joško Gvardiol", 24, 54, 69, 89, "df", 104));
		team2.addPlayer(new Player("Sergio Gómez", 21, 70, 75, 81, "df", 39));
		team2.addPlayer(new Player("Stefan Ortega", 18, 73, 86, 92, "gk", 28));
		teamList.add(team2);
		saveTeamList(teamList);
	}
}