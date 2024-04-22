import java.io.Serializable;
import java.util.Objects;

public class Player implements Comparable<Player>, Serializable {
	private static final long serialVersionUID = 1L;
	private String name;
	private int number;
	private int sho;
	private int pas;
	private int def;
	private String position;
	private int price;
	private int index;

	public Player() {
		this.name = null;
		this.number = 0;
		this.sho = 0;
		this.pas = 0;
		this.def = 0;
		this.position = null;
		this.price = 0;
		this.index = 0;
	}

	public Player(String name, int number, int sho, int pas, int def, String position, int price) {
		this.name = name;
		this.number = number;
		this.sho = sho;
		this.pas = pas;
		this.def = def;
		this.position = position;
		this.price = price;
		switch (position) {
			case "gk":
				this.index = 0;
				break;
			case "df":
				this.index = 1;
				break;
			case "mf":
				this.index = 2;
				break;
			case "fw":
				this.index = 3;
				break;
		}
	}

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getBackNo() {
		return number;
	}

	public void setBackNo(int number) {
		this.number = number;
	}

	public int getSho() {
		return sho;
	}

	public void setSho(int sho) {
		this.sho = sho;
	}

	public int getPas() {
		return pas;
	}

	public void setPas(int pas) {
		this.pas = pas;
	}

	public int getDef() {
		return def;
	}

	public void setDef(int def) {
		this.def = def;
	}

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	@Override
	public int hashCode() {
		return Objects.hash(name, number, position);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null || getClass() != obj.getClass())
			return false;
		Player other = (Player) obj;
		return number == other.number && Objects.equals(name, other.name) && Objects.equals(position, other.position);
	}

	@Override
	public int compareTo(Player o) {
		return this.index - o.index;
	}

	@Override
	public String toString() {
		return "이름 : " + name + ", 등 번호 : " + number + ", 슈팅 능력 : " + sho + ", 패스 능력 : " + pas + ", 수비 능력 : " + def
				+ ", 포지션 : " + position + ", 가격 : " + price + "\n";
	}
}