package tests.metrics;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import edu.uci.ics.jung.algorithms.metrics.Metrics;
import edu.uci.ics.jung.algorithms.shortestpath.UnweightedShortestPath;
import edu.uci.ics.jung.graph.Graph;
import model.node.Node;

public class NetworkMetrics<V, E> {

	Graph<V, E> graph = null;
	Map<V, Double> clusteringCoeficientMap = null;
	
	public NetworkMetrics(Graph<V,E> graph) {
		this.graph = graph;
		clusteringCoeficientMap = Metrics.clusteringCoefficients(this.graph);
	}

	@SuppressWarnings("unchecked")
	public double averageClusteringCoeficient() {		
		double sum = 0.0;

		Iterator<Node> it = (Iterator<Node>) graph.getVertices().iterator();
		while (it.hasNext()) {
			Node n = it.next();
			double ccForN = clusteringCoeficientMap.get(n);
			sum += ccForN;
		}
		return sum / graph.getVertexCount();
	}
	
	public List<V> getNodesWithMaxClusteringCoefficient() {
		List<V> nodes = new ArrayList<>();
		V node = null;
		
		for (Map.Entry<V, Double> entry : this.clusteringCoeficientMap.entrySet()) {
			if (node == null) {
				node = entry.getKey();
			} else if (entry.getValue() > this.clusteringCoeficientMap.get(node)) {
				node = entry.getKey();
			} 
		}
		
		double d = this.clusteringCoeficientMap.get(node);
		nodes = (List<V>) this.graph.getVertices().stream()
					.filter(v -> this.clusteringCoeficientMap.get(v) == d)
					.toList();
		
		return nodes;
	}
	
	public double getSmallWorldCoeff() {
		UnweightedShortestPath<V, E> usp = new UnweightedShortestPath<>(graph);
		List<V> nodes = new ArrayList<>(this.graph.getVertices());
		double sum = 0.0;

		for (int i = 0; i < this.graph.getVertexCount() - 1; i++) {
			for (int j = i + 1; j < this.graph.getVertexCount(); j++) {
				Number n = usp.getDistance(nodes.get(i), nodes.get(j));
				if (n == null)
					throw new IllegalArgumentException("Graph have more then one components!");
				sum += usp.getDistance(nodes.get(i), nodes.get(j)).doubleValue();
			} 
		}
		
		return sum / (this.graph.getVertexCount() * (this.graph.getVertexCount() - 1));
	}
	
}
