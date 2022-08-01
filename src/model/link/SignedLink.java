package model.link;

public class SignedLink {
	
	private Sign sign;
	private String id;
	
	public SignedLink(Sign sign) {
		this.sign = sign;
	}
	
	public SignedLink() {
		
	}
	
	public Sign getSign() {
		return this.sign;
	}
	
	public void setNewSign(Sign newSign) {
		this.sign = newSign;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	public String getId() {
		return id;
	}

	@Override
	public String toString() {
		if (sign == Sign.NEGATIVE) {
			return "-";
		} else if (sign == Sign.POSITIVE) {
			return "+";
		} else {
			return "";
		}
	}

}
