package model;

public class Player {
	private int pNo;
	private String pName;
	private String pUniformNo;
	private String pPosition;
	private int pSho;
	private int pPas;
	private int pDef;
	private int pPrice;

	public Player(int pNo, String pName, String pUniformNo, String pPosition, int pSho, int pPas, int pDef,
			int pPrice) {
		this.pNo = pNo;
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
		return "pNo=" + pNo + ", pName=" + pName + ", pUniformNo=" + pUniformNo + ", pPosition=" + pPosition
				+ ", pSho=" + pSho + ", pPas=" + pPas + ", pDef=" + pDef + ", pPrice=" + pPrice;
	}

}