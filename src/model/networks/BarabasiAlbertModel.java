package model.networks;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.apache.commons.collections15.Transformer;

import edu.uci.ics.jung.graph.UndirectedSparseGraph;
import model.link.Sign;

public class BarabasiAlbertModel<V, E> {

	private int n;
	private int m0;
	private int e0;
	private int m;

	private Transformer<E, Sign> linkTransformer;
	
	private Generator generator = new Generator();
	
	public BarabasiAlbertModel(int n, int m0, int e0, int m, Transformer<E, Sign> linkTransformer) {
		
		if (n <= 0) 
			throw new IllegalArgumentException("Number of nodes must be positive!");
		
		if (e0 < 0) 
			throw new IllegalArgumentException("Number of edges in Erdos Renyi network must be positive! (e0)");
		
		if (m0 > n || m0 <= 0) 
			throw new IllegalArgumentException("Number of nodes in Erdos Renyi network must be positive and less than n! (m0)");
		
		if (m >= m0) 
			throw new IllegalArgumentException("Number of new connections must be less than number of nodes in initial graph! (m)");
		
		if (linkTransformer == null)
			throw new IllegalArgumentException("linkTransformer can't be null!");
		
		this.n = n;
		this.m0 = m0;
		this.e0 = e0;
		this.m = m;
		this.linkTransformer = linkTransformer;
	}

	@SuppressWarnings("unchecked")
	public void getGraph(UndirectedSparseGraph<V, E> graph) {
		ErdosRenyiModel<V, E> er = new ErdosRenyiModel<>(m0, e0, linkTransformer);
		er.getGraph(graph);
		
		Random rnd = new Random();
		List<V> nodes = new ArrayList<>(graph.getVertices());	
		List<V> degs = new ArrayList<>();
		for (int i = 0; i < this.m0; i++) {
			for (int j = 0; j < graph.degree(nodes.get(i)); j++) 
				degs.add(nodes.get(i));
		}
		
		for (int i = this.m0; i < this.n; i++) {
			V v = (V) generator.getNode();
			graph.addVertex(v);
			for (int j = 0; j < this.m; j++) {
				V old;
				do {
					old = degs.get(rnd.nextInt(degs.size()));
				} while (v.equals(old));
				if (graph.findEdge(v, old) == null) {
					Clusterable<V, E> c = new Clusterable<>();
					c.addLink(graph, v, old, linkTransformer);
					degs.add(old);
				}
			}
			
			for (int j = 0; j < this.m; j++)
				degs.add(v);
		}		
	}
	
}
