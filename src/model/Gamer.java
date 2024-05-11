package model;

public class Gamer {
	private int gNo;
	private int cNo;
	private String gId;
	private String gPw;
	private String gSessionId;
	private boolean gIsAdmin;
	private int gBalance;
	private int gGkCount;
	private int gDfCount;
	private int gMfCount;
	private int gFwCount;

	public Gamer(int gNo, int cNo, String gId, String gPw, String gSessionId, boolean gIsAdmin, int gBalance,
			int gGkCount, int gDfCount, int gMfCount, int gFwCount) {
		this.gNo = gNo;
		this.cNo = cNo;
		this.gId = gId;
		this.gPw = gPw;
		this.gSessionId = gSessionId;
		this.gIsAdmin = gIsAdmin;
		this.gBalance = gBalance;
		this.gGkCount = gGkCount;
		this.gDfCount = gDfCount;
		this.gMfCount = gMfCount;
		this.gFwCount = gFwCount;
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

	public boolean isgIsAdmin() {
		return gIsAdmin;
	}

	public void setgIsAdmin(boolean gIsAdmin) {
		this.gIsAdmin = gIsAdmin;
	}

	public int getgBalance() {
		return gBalance;
	}

	public void setgBalance(int gBalance) {
		this.gBalance = gBalance;
	}

	public int getgGkCount() {
		return gGkCount;
	}

	public void setgGkCount(int gGkCount) {
		this.gGkCount = gGkCount;
	}

	public int getgDfCount() {
		return gDfCount;
	}

	public void setgDfCount(int gDfCount) {
		this.gDfCount = gDfCount;
	}

	public int getgMfCount() {
		return gMfCount;
	}

	public void setgMfCount(int gMfCount) {
		this.gMfCount = gMfCount;
	}

	public int getgFwCount() {
		return gFwCount;
	}

	public void setgFwCount(int gFwCount) {
		this.gFwCount = gFwCount;
	}

	@Override
	public String toString() {
		return "gNo=" + gNo + ", cNo=" + cNo + ", gId=" + gId + ", gPw=" + gPw + ", gSessionId=" + gSessionId
				+ ", gIsAdmin=" + gIsAdmin + ", gBalance=" + gBalance + ", gGkCount=" + gGkCount + ", gDfCount="
				+ gDfCount + ", gMfCount=" + gMfCount + ", gFwCount=" + gFwCount;
	}

}