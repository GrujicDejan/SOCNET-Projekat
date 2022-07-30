package clusterability;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.collections15.Transformer;

import edu.uci.ics.jung.graph.UndirectedSparseGraph;
import interfaces.ComponentClustererU;
import io.Export;
import model.edge.Mark;
import model.edge.MarkedLink;
import model.node.Node;

public class ComponentClustererBFS<V, E> implements ComponentClustererU<V, E> {

	private UndirectedSparseGraph<V, E> graph;
	private List<UndirectedSparseGraph<V, E>> components;
	
	private List<UndirectedSparseGraph<V, E>> clusterWithNegativeLink = new ArrayList<>();
	List<MarkedLink> negativeEdges = new ArrayList<>();
	
	private Transformer<E, Mark> markTransformer;

	HashSet<V> visited = new HashSet<V>();

	public ComponentClustererBFS(UndirectedSparseGraph<V, E> g) {
		if (g == null || g.getVertexCount() == 0)
			throw new IllegalArgumentException("Empty network");
		this.graph = g;
		components = new ArrayList<UndirectedSparseGraph<V, E>>();
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
		
		Collections.sort(components, (c1, c2) -> c2.getVertexCount() - c1.getVertexCount());
	}

	private void identifyComponent(V startNode) {
		LinkedList<V> queue = new LinkedList<V>();
		queue.add(startNode);
		visited.add(startNode);
		UndirectedSparseGraph<V, E> component = new UndirectedSparseGraph<V, E>();
		component.addVertex(startNode);
		
		while (!queue.isEmpty()) {
			V current = queue.removeFirst();
			
			Iterator<V> nit = graph.getNeighbors(current).iterator();
			while (nit.hasNext()) {
				V neighbor = nit.next();
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
		identifyNegativeLinkInComponent(component);
		components.add(component);
	}
	
	private void identifyNegativeLinkInComponent(UndirectedSparseGraph<V, E> component) {
		
		boolean hasNegativeLink = false;
		
		List<V> nodes = new ArrayList<>(component.getVertices());
		
		Iterator<V> it1 = nodes.iterator();
		Iterator<V> it2 = nodes.iterator();
		
		if (it2.hasNext()) {
			it2.next();
		}
		
		while (it2.hasNext()) {
			V n1 = it1.next();
			V n2 = it2.next();
			
			E link = this.graph.findEdge(n1, n2);
			
			if (link != null) {
				Mark mark = Mark.POSITIVE;//markTransformer.transform(link);
				
				if (mark == Mark.NEGATIVE) {
					if (component.findEdge(n1, n2) == null) {
						component.addEdge(link, n1, n2);
					}
					if (!hasNegativeLink) {
						clusterWithNegativeLink.add(component);
					}
					this.negativeEdges.add(new MarkedLink(Mark.NEGATIVE));
					hasNegativeLink = true;
				}
				
			}
		}
		
	}
	
	@SuppressWarnings("unchecked")
	public void exportComponentToGraphML(String fileName) {
		Export export = new Export();
		export.exportToGraphML(fileName, (UndirectedSparseGraph<Node, MarkedLink>) graph);
	}

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
