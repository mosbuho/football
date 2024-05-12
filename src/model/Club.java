package model;

import java.io.Serializable;
import java.util.ArrayList;

public class Club implements Serializable {
	private int cNo;
	private String cName;
	private int gBalance;
	private ArrayList<Player> playerList;

	public Club(int cNo, String cName) {
		this.cNo = cNo;
		this.cName = cName;
		this.playerList = new ArrayList<>();
	}

	public Club(int cNo, String cName, int gBalance) {
		this.cNo = cNo;
		this.cName = cName;
		this.gBalance = gBalance;
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

	public int getgBalance() {
		return gBalance;
	}

	public void setgBalance(int gBalance) {
		this.gBalance = gBalance;
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

	public String toStringWithBalance() {
		StringBuilder sb = new StringBuilder();
		sb.append(String.format("팀 이름: %s, 자산 : %d\n", cName, gBalance));
		for (Player player : playerList) {
			sb.append(player);
		}
		return sb.toString();
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(String.format("팀 이름: %s\n", cName));
		for (Player player : playerList) {
			sb.append(player);
		}
		return sb.toString();
	}
}