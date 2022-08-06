package model.networks;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.apache.commons.collections15.Transformer;

import edu.uci.ics.jung.graph.UndirectedSparseGraph;
import model.link.Sign;

public class ErdosRenyiModel<V, E> {
	
	private int n;
	private int e;
	
	private Transformer<E, Sign> signTransformer;
	
	private Generator generator = new Generator();
	
	public ErdosRenyiModel(int n, int e, Transformer<E, Sign> signTransformer) {
		
		if (n <= 0) 
			throw new IllegalArgumentException("Number of nodes must be positive!");
		
		if (e < 0) 
			throw new IllegalArgumentException("Number of links must be positive!");
		
		this.n = n;
		this.e = e;
		this.signTransformer = signTransformer;
	}

	@SuppressWarnings("unchecked")
	public void getGraph(UndirectedSparseGraph<V, E> graph) {
		for (int i = 0; i < this.n; i++) {
			graph.addVertex((V) generator.getNode());
		}
		
		double maxEdgeNumber = this.n * (this.n - 1) / 2.0;
		Random rnd = new Random();
		
		List<V> nodes = new ArrayList<>(graph.getVertices());
		
		while (graph.getEdgeCount() < this.e) {
			for (int i = 0; i < this.n - 1; i++) {
				for (int j = i + 1; j < this.n; j++) {
					if (rnd.nextDouble() < 1.0/maxEdgeNumber && graph.findEdge(nodes.get(i), nodes.get(j)) == null) {
						Clusterable<V, E> c = new Clusterable<>();
						c.addLink(graph, nodes.get(i), nodes.get(j), signTransformer);
					}
				}
			}
		}		
	}
}
