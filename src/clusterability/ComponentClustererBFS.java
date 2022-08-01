package clusterability;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.collections15.Transformer;

import edu.uci.ics.jung.graph.UndirectedSparseGraph;
import edu.uci.ics.jung.io.GraphIOException;
import interfaces.ComponentClustererU;
import model.link.LinkInfo;
import model.link.Sign;
import model.node.ClusterNode;

public class ComponentClustererBFS<V, E> implements ComponentClustererU<V, E> {

	private UndirectedSparseGraph<V, E> graph;
	private UndirectedSparseGraph<ClusterNode, E> clusterNetwork = new UndirectedSparseGraph<ClusterNode, E>();
	private List<UndirectedSparseGraph<V, E>> components;
	private List<UndirectedSparseGraph<V, E>> clustersWithNegativeLink = new ArrayList<>();
	private List<UndirectedSparseGraph<V, E>> clustersWithoutNegativeLink = new ArrayList<>();
	List<LinkInfo<V, E>> negativeEdges = new ArrayList<>();
	
	private Transformer<E, Sign> signTransformer;

	HashSet<V> visited = new HashSet<>();

	public ComponentClustererBFS(UndirectedSparseGraph<V, E> g, Transformer<E, Sign> signTransformer) {
		if (g == null || g.getVertexCount() == 0)
			throw new IllegalArgumentException("Empty network");
		this.graph = g;
		this.components = new ArrayList<>();
		this.signTransformer = signTransformer;
		identifyComponents();
	}

	private void identifyComponents() {
		Iterator<V> it = graph.getVertices().iterator();
		
		while (it.hasNext()) {
			V node = it.next();
			if (!visited.contains(node)) {
				identifyComponent(node);
			}
		}
		
		generateClusterNetwork();

		Collections.sort(components, (c1, c2) -> c2.getVertexCount() - c1.getVertexCount());
	}

	private void identifyComponent(V startNode) {
		LinkedList<V> queue = new LinkedList<>();
		queue.add(startNode);
		UndirectedSparseGraph<V, E> component = new UndirectedSparseGraph<V, E>();
		component.addVertex(startNode);

		while (!queue.isEmpty()) {
			V current = queue.removeFirst();
			
			Iterator<V> nit = graph.getNeighbors(current).iterator();
			
			while (nit.hasNext()) {
				V neighbor = nit.next();

				if (signTransformer.transform(this.graph.findEdge(current, neighbor)) != Sign.NEGATIVE) {
				
					if (!visited.contains(neighbor)) {
						visited.add(neighbor);
						queue.addLast(neighbor);
						component.addVertex(neighbor);
					}
					if (component.findEdge(current, neighbor) == null) {
						E link = graph.findEdge(current, neighbor);
						component.addEdge(link, current, neighbor);
					}
				
				}
			}
			
		}
		
		identifyNegativeLinkInComponent(component);
		components.add(component);
	}
	
	private void identifyNegativeLinkInComponent(UndirectedSparseGraph<V, E> component) {
		
		boolean negativeLink = false;
		
		List<V> nodes = new ArrayList<>(component.getVertices());
		
		for (int i = 0; i < nodes.size() - 1; i++) {
			V n1 = nodes.get(i);
			for (int j = i + 1; j < nodes.size(); j++) {
				V n2 = nodes.get(j);
				
				E link = this.graph.findEdge(n1, n2);
//				System.out.println(n1 + "-" + n2 + " ==> " + link + " |||| " + negativeLink);
				if (link != null) {
					Sign sign = signTransformer.transform(link);
					
					if (sign != Sign.POSITIVE) {

						if (component.findEdge(n1, n2) == null)
							component.addEdge(link, n1, n2);
						
						if (!negativeLink) {
							clustersWithNegativeLink.add(component);
						}
						
						this.negativeEdges.add(new LinkInfo<V, E>(sign, n1, n2, link));
						negativeLink = true;
					}
				}
			}
		}
		
		if (!negativeLink) {
			clustersWithoutNegativeLink.add(component);
		}
	}
	
	
	public void generateClusterNetwork() {
		for (int i = 0; i < this.components.size(); i++) {
			UndirectedSparseGraph<V, E> cluster_i = this.components.get(i);
			ClusterNode cn_i = new ClusterNode("cn_" + i, "" + i, cluster_i.getVertexCount(), this.clustersWithoutNegativeLink.contains(cluster_i));
			this.clusterNetwork.addVertex(cn_i);
			
			for (int j = i + 1; j < this.components.size(); j++) {
				UndirectedSparseGraph<V, E> cluster_j = this.components.get(j);
				
				E link = conectedClusters(cluster_i, cluster_j); // check each with each
				
				if (link != null) {
					ClusterNode cn_j = new ClusterNode("cn_" + j, "" + j, cluster_j.getVertexCount(), this.clustersWithoutNegativeLink.contains(cluster_j));
					this.clusterNetwork.addEdge(link, cn_i, cn_j);
				}
				
			}
		}
	}

	private E conectedClusters(UndirectedSparseGraph<V, E> c1, UndirectedSparseGraph<V, E> c2) {
		for (V v1 : c1.getVertices()) {
			for (V v2 : c2.getVertices()) {
				E link = this.graph.findEdge(v1, v2);
				if (link != null) {
					return link;
				}
			}
		}
		return null;
	}
	
	public UndirectedSparseGraph<ClusterNode, E> getClusterNetwork() {
		return this.clusterNetwork;
	}

	public boolean isClusterable() {
		return clustersWithNegativeLink.size() == 0;
	}
	
	public List<UndirectedSparseGraph<V, E>> getCoalitions() {
		return clustersWithoutNegativeLink;
	}
	
	public List<UndirectedSparseGraph<V, E>> getNonCoalitions() {
		return clustersWithNegativeLink;
	}
	
	public List<LinkInfo<V, E>> getNegativeLinks() throws GraphIOException {
		if (this.isClusterable()) 
			throw new GraphIOException("The graph is clustered.");
		
		return this.negativeEdges;
	}
	
//	@SuppressWarnings("unchecked")
//	public void exportComponentToGraphML(String fileName) {
//		Export export = new Export();
//		export.exportToGraphML(fileName, (UndirectedSparseGraph<Node, MarkedLink>) graph);
//	}

	// metode propisane interfejsom ComponentClustererU<V, E>
	@Override
	public List<UndirectedSparseGraph<V, E>> getComponents() {
		return components;
	}
	
	@Override
	public UndirectedSparseGraph<V, E> getGiantComponent() {
		return components.get(0);
	}

}
