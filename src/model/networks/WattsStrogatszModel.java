package model.networks;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.apache.commons.collections15.Transformer;

import edu.uci.ics.jung.graph.UndirectedSparseGraph;
import model.link.Sign;

public class WattsStrogatszModel<V, E> {
	
	private int n;
	private int k;
	private double p;
	
	private Transformer<E, Sign> signTransformer;
	private Generator generator = new Generator();
	
	public WattsStrogatszModel(int n, int k, double p, Transformer<E, Sign> signTransformer) {
		
		if (n <= 0) 
			throw new IllegalArgumentException("Number of nodes must be positive!");
		
		if (k <= 0 || k > n)
			throw new IllegalArgumentException("Number of neighbors in initial graph must be in interval [1, n)");
		
		if (p < 0 || p > 1)
			throw new IllegalArgumentException("Probability must be in interval [0,1] !");
		
		if (signTransformer == null)
			throw new IllegalArgumentException("signTransformer can't be null!");
		
		this.n = n;
		this.k = k;
		this.p = p;
		this.signTransformer = signTransformer;
	}

	@SuppressWarnings("unchecked")
	public void getGraph(UndirectedSparseGraph<V, E> graph) {
		Clusterable<V, E> c = new Clusterable<>();
		
		for (int i = 0; i < this.n; i++) {
			graph.addVertex((V) generator.getNode());
		}
		
		List<V> nodes = new ArrayList<>(graph.getVertices());	
		for (int i = 0; i < this.n; i++) {
			for (int j = 1; j <= this.k/2; j++) {
				c.addEdge(graph, nodes.get(i), nodes.get((i + j) % this.n), signTransformer);
			}
		}
		
		Random rnd = new Random();
		for (int i = 0; i < this.n; i++) { 
			V v1 = nodes.get(i);
			for (V v2 : new ArrayList<V>(graph.getNeighbors(v1))) {
				if (rnd.nextDouble() < this.p && graph.getNeighborCount(v1) < this.n - 1) {
					int newDst = 0;
					boolean okDst = false;
					while (!okDst) {
						newDst = (int) (rnd.nextDouble() * this.n);
						okDst = newDst != i && graph.findEdge(v1, nodes.get(newDst)) == null;
					}
					graph.removeEdge(graph.findEdge(v1, v2));
					c.addEdge(graph, v1, nodes.get(newDst), signTransformer);
				}
			}
		}	
	}

}
