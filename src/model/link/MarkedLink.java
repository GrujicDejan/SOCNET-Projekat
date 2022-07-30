package model.link;

public class MarkedLink {
	
	private Mark mark;
	private String id;
	
	public MarkedLink(Mark mark) {
		this.mark = mark;
	}
	
	public MarkedLink() {
		
	}
	
	public Mark getMark() {
		return this.mark;
	}
	
	public void setNewMark(Mark newMark) {
		this.mark = newMark;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	public String getId() {
		return id;
	}

	@Override
	public String toString() {
		if (mark == Mark.NEGATIVE) {
			return "-";
		} else if (mark == Mark.POSITIVE) {
			return "+";
		} else {
			return "";
		}
	}

}
