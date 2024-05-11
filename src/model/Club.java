package model;

import java.io.Serializable;
import java.util.ArrayList;

public class Club implements Serializable {
	private int cNo;
	private String cName;
	private ArrayList<Player> playerList;

	public Club(int cNo, String cName) {
		this.cNo = cNo;
		this.cName = cName;
		this.playerList = new ArrayList<>();
	}

	public int getcNo() {
		return cNo;
	}

	public void setcNo(int cNo) {
		this.cNo = cNo;
	}

	public String getcName() {
		return cName;
	}

	public void setcName(String cName) {
		this.cName = cName;
	}

	public ArrayList<Player> getPlayerList() {
		return playerList;
	}

	public void setPlayerList(ArrayList<Player> playerList) {
		this.playerList = playerList;
	}

	public void addPlayer(Player player) {
		this.playerList.add(player);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(String.format("팀 번호: %d, 팀 이름: %s\n", cNo, cName));
		for (Player player : playerList) {
			sb.append(player).append("\n");
		}
		return sb.toString();
	}
}