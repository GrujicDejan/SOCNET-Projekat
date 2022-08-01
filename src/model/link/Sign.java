package model.link;

public enum Sign {
	
	POSITIVE(1), NEGATIVE(-1);
	
	private int value;
	
	private Sign(int value) {
		this.value = value;
	}
	
	public static Sign getSign(int value) {
		for (Sign m : values()) {
			if (m.value == value) {
				return m;
			}
		}
		throw new IllegalArgumentException("Sign with value " + value + " not found");
	}

	
}
