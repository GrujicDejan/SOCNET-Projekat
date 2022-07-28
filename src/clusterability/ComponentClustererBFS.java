package clusterability;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import edu.uci.ics.jung.graph.UndirectedSparseGraph;
import interfaces.ComponentClustererU;

public class ComponentClustererBFS<V, E> implements ComponentClustererU<V, E> {

	// input
	private UndirectedSparseGraph<V, E> g;

	// output
	private List<UndirectedSparseGraph<V, E>> comps;

	HashSet<V> visited = new HashSet<V>();

	public ComponentClustererBFS(UndirectedSparseGraph<V, E> g) {
		if (g == null || g.getVertexCount() == 0)
			throw new IllegalArgumentException("Empty network");
		this.g = g;
		comps = new ArrayList<UndirectedSparseGraph<V, E>>();
		identifyComponents();
	}

	private void identifyComponents() {
		Iterator<V> it = g.getVertices().iterator();
		while (it.hasNext()) {
			V node = it.next();
			if (!visited.contains(node)) {
				identifyComponent(node);
			}
		}
		Collections.sort(comps, (c1, c2) -> c2.getVertexCount() - c1.getVertexCount());
	}

	private void identifyComponent(V startNode) {
		LinkedList<V> queue = new LinkedList<V>();
		queue.add(startNode);
		visited.add(startNode);
		UndirectedSparseGraph<V, E> component = new UndirectedSparseGraph<V, E>();
		component.addVertex(startNode);
		
		while (!queue.isEmpty()) {
			V current = queue.removeFirst();
			Iterator<V> nit = g.getNeighbors(current).iterator();
			while (nit.hasNext()) {
				V neighbor = nit.next();
				if (!visited.contains(neighbor)) {
					visited.add(neighbor);
					queue.addLast(neighbor);
					component.addVertex(neighbor);
				}
				if (component.findEdge(current, neighbor) == null) {
					E link = g.findEdge(current, neighbor);
					component.addEdge(link, current, neighbor);
				}
			}
		}
		comps.add(component);
	}

	// metode propisane interfejsom ComponentClustererU<V, E>
	@Override
	public List<UndirectedSparseGraph<V, E>> getComponents() {
		return comps;
	}
	
	@Override
	public UndirectedSparseGraph<V, E> getGiantComponent() {
		return comps.get(0);
	}

}
