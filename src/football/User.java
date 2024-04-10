package football;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class User implements Serializable {
	private static final long serialVersionUID = 1L;
	private String id;
	private String pw;
	private String sessionId;
	private boolean isAdmin;
	private String teamName;
	private int balance;
	private List<Player> players;
	private Map<String, Integer> positionCount;

	public User() {
		this.id = null;
		this.pw = null;
		this.sessionId = null;
		this.isAdmin = false;
		this.teamName = null;
		this.balance = 10000;
		this.players = new ArrayList<>();
		this.positionCount = new HashMap<>();
		this.positionCount.put("gk", 0);
		this.positionCount.put("df", 0);
		this.positionCount.put("mf", 0);
		this.positionCount.put("fw", 0);
	}

	public User(String id, String pw, String teamName, List<Player> players) {
		this.id = id;
		this.pw = pw;
		this.sessionId = null;
		this.isAdmin = false;
		this.teamName = teamName;
		this.balance = 10000;
		this.players = players;
		this.positionCount = new HashMap<>();
		this.positionCount.put("gk", 1);
		this.positionCount.put("df", 4);
		this.positionCount.put("mf", 3);
		this.positionCount.put("fw", 3);
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPw() {
		return pw;
	}

	public void setPw(String pw) {
		this.pw = pw;
	}

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	public boolean isAdmin() {
		return isAdmin;
	}

	public void setAdmin(boolean isAdmin) {
		this.isAdmin = isAdmin;
	}

	public String getTeamName() {
		return teamName;
	}

	public void setTeamName(String teamName) {
		this.teamName = teamName;
	}

	public int getBalance() {
		return balance;
	}

	public void setBalance(int balance) {
		this.balance = balance;
	}

	public List<Player> getPlayers() {
		return players;
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
		this.balance -= player.getPrice();
		return true;
	}

	public void removePlayer(Player player) {
		if (players.remove(player)) {
			String position = player.getPosition();
			positionCount.put(position, positionCount.get(position) - 1);
		}
	}

	public void sellPlayer(Player player) {
		if (players.remove(player)) {
			String position = player.getPosition();
			positionCount.put(position, positionCount.get(position) - 1);
			balance += player.getPrice();
		}
	}

	public Map<String, Integer> getPositionCount() {
		return positionCount;
	}

	public void setPositionCount(Map<String, Integer> positionCount) {
		this.positionCount = positionCount;
	}

	public String admintoString() {
		return "User [id=" + id + ", pw=" + pw + ", sessionId=" + sessionId + ", isAdmin=" + isAdmin + ", teamName="
				+ teamName + ", balance=" + balance + "\n" + players + ", positionCount=" + positionCount + "]";
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null || getClass() != obj.getClass())
			return false;
		User other = (User) obj;
		return Objects.equals(id, other.id);
	}

	@Override
	public String toString() {
		return "아이디 : " + id + ", 팀 : " + teamName + "\n" + players;
	}

}