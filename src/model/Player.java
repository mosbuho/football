package model;

import java.io.Serializable;

public class Player implements Serializable {
	private int pNo;
	private int cNo;
	private String pName;
	private String pUniformNo;
	private String pPosition;
	private int pSho;
	private int pPas;
	private int pDef;
	private int pPrice;

	public Player(int pNo, int cNo, String pName, String pUniformNo, String pPosition, int pSho, int pPas, int pDef,
			int pPrice) {
		this.pNo = pNo;
		this.cNo = cNo;
		this.pName = pName;
		this.pUniformNo = pUniformNo;
		this.pPosition = pPosition;
		this.pSho = pSho;
		this.pPas = pPas;
		this.pDef = pDef;
		this.pPrice = pPrice;
	}

	public int getpNo() {
		return pNo;
	}

	public void setpNo(int pNo) {
		this.pNo = pNo;
	}

	public String getpName() {
		return pName;
	}

	public void setpName(String pName) {
		this.pName = pName;
	}

	public String getpUniformNo() {
		return pUniformNo;
	}

	public void setpUniformNo(String pUniformNo) {
		this.pUniformNo = pUniformNo;
	}

	public String getpPosition() {
		return pPosition;
	}

	public void setpPosition(String pPosition) {
		this.pPosition = pPosition;
	}

	public int getpSho() {
		return pSho;
	}

	public void setpSho(int pSho) {
		this.pSho = pSho;
	}

	public int getpPas() {
		return pPas;
	}

	public void setpPas(int pPas) {
		this.pPas = pPas;
	}

	public int getpDef() {
		return pDef;
	}

	public void setpDef(int pDef) {
		this.pDef = pDef;
	}

	public int getpPrice() {
		return pPrice;
	}

	public void setpPrice(int pPrice) {
		this.pPrice = pPrice;
	}

	@Override
	public String toString() {
		return String.format("선수 번호 : %d, 팀 번호 : %d, 선수 이름 : %s, 등번호 : %s, 포지션 : %s, 슈팅 : %d, 패스 : %d, 수비 : %d, 가격 : %d",
				pNo, cNo, pName, pUniformNo, pPosition, pSho, pPas, pDef, pPrice);
	}

}