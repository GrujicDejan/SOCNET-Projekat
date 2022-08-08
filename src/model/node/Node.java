package model.node;

public class Node {

	private String label;
	private String id;

	public Node(String label, String id) {
		this.label = label;
		this.id = id;
	}

	public String getLabel() {
		return label;
	}
	
	public String getId() {
		return id;
	}

	@Override
	public String toString() {
		return "Node [label=" + label + ", id=" + id + "]";
	}

}
