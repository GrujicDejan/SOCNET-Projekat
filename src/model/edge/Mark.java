package model.edge;

public enum Mark {
	
	POSITIVE(1), NEGATIVE(-1);
	
	private int value;
	
	private Mark(int value) {
		this.value = value;
	}
	
	public static Mark getMark(int value) {
		for (Mark m : values()) {
			if (m.value == value) {
				return m;
			}
		}
		throw new IllegalArgumentException("Mark with value " + value + " not found");
	}

	
}
