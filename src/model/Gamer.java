package model;

import java.io.Serializable;

public class Gamer implements Serializable {
	private int gNo;
	private int cNo;
	private String cName;
	private String gId;
	private String gPw;
	private String gSessionId;
	private int gIsAdmin;
	private int gBalance;
	private int gPoint;

	public Gamer(String gId, String cName, int gPoint) {
		this.gId = gId;
		this.cName = cName;
		this.gPoint = gPoint;
	}

	public Gamer(int gNo, int cNo, String cName, String gId, String gPw, String gSessionId, int gIsAdmin, int gBalance,
			int gPoint) {
		this.gNo = gNo;
		this.cNo = cNo;
		this.cName = cName;
		this.gId = gId;
		this.gPw = gPw;
		this.gSessionId = gSessionId;
		this.gIsAdmin = gIsAdmin;
		this.gBalance = gBalance;
		this.gPoint = gPoint;
	}

	public int getgNo() {
		return gNo;
	}

	public void setgNo(int gNo) {
		this.gNo = gNo;
	}

	public int getcNo() {
		return cNo;
	}

	public void setcNo(int cNo) {
		this.cNo = cNo;
	}

	public String getgId() {
		return gId;
	}

	public void setgId(String gId) {
		this.gId = gId;
	}

	public String getgPw() {
		return gPw;
	}

	public void setgPw(String gPw) {
		this.gPw = gPw;
	}

	public String getgSessionId() {
		return gSessionId;
	}

	public void setgSessionId(String gSessionId) {
		this.gSessionId = gSessionId;
	}

	public int getgIsAdmin() {
		return gIsAdmin;
	}

	public void setgIsAdmin(int gIsAdmin) {
		this.gIsAdmin = gIsAdmin;
	}

	public int getgBalance() {
		return gBalance;
	}

	public void setgBalance(int gBalance) {
		this.gBalance = gBalance;
	}

	public int getgPoint() {
		return gPoint;
	}

	public void setgPoint(int gPoint) {
		this.gPoint = gPoint;
	}

	@Override
	public String toString() {
		return String.format("아이디 : %s, 소속 팀 : %s, 승점 : %d\n", gId, cName, gPoint);
	}
}