package model.node;

public class ClusterNode extends Node {

	private int numberOfNodes;
	private boolean coaltion;
	
	public ClusterNode(String label, String id, int numberOfNodes, boolean coaltion) {
		super(label, id);
		this.numberOfNodes = numberOfNodes;
		this.coaltion = coaltion;
	}

	public int getNumberOfNodes() {
		return numberOfNodes;
	}

	public void setNumberOfNodes(int numberOfNodes) {
		this.numberOfNodes = numberOfNodes;
	}

	public boolean isCoaltion() {
		return coaltion;
	}

	public void setCoaltion(boolean coaltion) {
		this.coaltion = coaltion;
	}

	@Override
	public String toString() {
		return "ClusterNode [numberOfNodes=" + numberOfNodes + ", coaltion=" + coaltion + "]";
	}
	
	
	
}
