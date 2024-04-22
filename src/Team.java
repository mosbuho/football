import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Team implements Serializable {
	private static final long serialVersionUID = 1L;
	private String name;
	private ArrayList<Player> players;
	private Map<String, Integer> positionCount;

	public Team() {
		this.name = null;
		this.players = new ArrayList<>();
		this.positionCount = new HashMap<>();
		this.positionCount.put("gk", 0);
		this.positionCount.put("df", 0);
		this.positionCount.put("mf", 0);
		this.positionCount.put("fw", 0);
		Collections.sort(players);
	}

	public Team(String name) {
		this.name = name;
		this.players = new ArrayList<Player>();
		this.positionCount = new HashMap<>();
		this.positionCount.put("gk", 0);
		this.positionCount.put("df", 0);
		this.positionCount.put("mf", 0);
		this.positionCount.put("fw", 0);
		Collections.sort(players);
	}

	public Team(String name, ArrayList<Player> players) {
		this.name = name;
		this.players = players;
		this.positionCount = new HashMap<>();
		this.positionCount.put("gk", 0);
		this.positionCount.put("df", 0);
		this.positionCount.put("mf", 0);
		this.positionCount.put("fw", 0);
		Collections.sort(players);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ArrayList<Player> getPlayers() {
		return players;
	}

	public void setPlayers(ArrayList<Player> players) {
		this.players = players;
	}

	public boolean addPlayer(Player player) {
		if (players.size() >= 11) {
			return false;
		} else {
			String position = player.getPosition();
			if (position.equals("gk") && positionCount.get("gk") < 1) {
				players.add(player);
				positionCount.put("gk", positionCount.get("gk") + 1);
			} else if (position.equals("df") && positionCount.get("df") < 4) {
				players.add(player);
				positionCount.put("df", positionCount.get("df") + 1);
			} else if (position.equals("mf") && positionCount.get("mf") < 3) {
				players.add(player);
				positionCount.put("mf", positionCount.get("mf") + 1);
			} else if (position.equals("fw") && positionCount.get("fw") < 3) {
				players.add(player);
				positionCount.put("fw", positionCount.get("fw") + 1);
			} else {
				return false;
			}
		}
		return true;
	}

	public void removePlayer(Player player) {
		if (players.remove(player)) {
			String position = player.getPosition();
			positionCount.put(position, positionCount.get(position) - 1);
			return;
		} else {
			return;
		}
	}

	@Override
	public int hashCode() {
		return Objects.hash(name);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null || getClass() != obj.getClass())
			return false;
		Team other = (Team) obj;
		return Objects.equals(name, other.name);
	}

	@Override
	public String toString() {
		return "팀 : " + name + "\n" + players;
	}
}