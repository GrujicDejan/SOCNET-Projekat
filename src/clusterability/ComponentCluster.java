package clusterability;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections15.Transformer;

import edu.uci.ics.jung.graph.UndirectedSparseGraph;
import model.edge.Mark;
import model.edge.MarkedEdge;

public class ComponentCluster<V,E> {
	
	private UndirectedSparseGraph<V, E> graph;
	private Transformer<E, Mark> markTransformer;
	
	private List<UndirectedSparseGraph<V, E>> clustersWithNegativeLink = new ArrayList<>();
	private List<UndirectedSparseGraph<V, E>> clustersWithoutNegativeLink = new ArrayList<>();
	private List<UndirectedSparseGraph<V, E>> components;
	List<MarkedEdge<E, V>> negativeEdges = new ArrayList<>();
	

}
