package model;

import java.util.Date;

public class Club {
	private int cNo;
	private String cName;
	private Date cCreation;

	public Club(int cNo, String cName, Date cCreation) {
		this.cNo = cNo;
		this.cName = cName;
		this.cCreation = cCreation;
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

	public Date getcCreation() {
		return cCreation;
	}

	public void setcCreation(Date cCreation) {
		this.cCreation = cCreation;
	}

	@Override
	public String toString() {
		return "cNo=" + cNo + ", cName=" + cName + ", cCreation=" + cCreation;
	}

}