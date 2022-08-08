package tests.metrics;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import edu.uci.ics.jung.algorithms.metrics.Metrics;
import edu.uci.ics.jung.algorithms.shortestpath.UnweightedShortestPath;
import edu.uci.ics.jung.graph.Graph;

public class NetworkMetrics<V, E> {

	Graph<V, E> graph = null;
	Map<V, Double> clusteringCoeficientMap = null;
	
	public NetworkMetrics(Graph<V,E> graph) {
		this.graph = graph;
		clusteringCoeficientMap = Metrics.clusteringCoefficients(this.graph);
	}

	public double averageClusteringCoeficient() {		
		double sum = 0.0;
		
		@SuppressWarnings("unchecked")
		Iterator<V> it = (Iterator<V>) this.clusteringCoeficientMap.values().iterator();
		while (it.hasNext()) {
			sum += (Double) it.next();
		}
		
		return sum / this.clusteringCoeficientMap.size();
	}
	
	public V getNodeWithMaxClusteringCoefficient() {
		V node = null;
		
		for (Map.Entry<V, Double> entry : this.clusteringCoeficientMap.entrySet()) {
			if (node == null) {
				node = entry.getKey();
			} else if (entry.getValue() > this.clusteringCoeficientMap.get(node)) {
				node = entry.getKey();
			}
		}
		
		return node;
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
