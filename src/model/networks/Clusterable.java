package model.networks;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;
import java.util.Set;

import org.apache.commons.collections15.Transformer;

import edu.uci.ics.jung.graph.UndirectedSparseGraph;
import model.link.Sign;

public class Clusterable<V, E> {
	
	private Generator generator = new Generator();

	@SuppressWarnings("unchecked")
	private Set<V> visited(UndirectedSparseGraph<V, E> graph, V src, V dst, Transformer<E, Sign> signTransformer){
		Queue<V> queue = new LinkedList<>();
		Set<V> visited = new HashSet<>();
		queue.add(src);
		visited.add(src);
		while (!queue.isEmpty()) {
			V curr = queue.poll();
			for (V v : graph.getNeighbors(curr)) {
				if (!visited.contains(v)) {
					if (signTransformer.transform(graph.findEdge(curr, v)) == Sign.POSITIVE) {
						visited.add(v);
						queue.add(v);
						if (v.equals(dst)) {
							graph.addEdge((E) generator.getPositiveLink(), src, dst);
							return null;
						}
					} else {
						if (v.equals(dst)) {
							graph.addEdge((E) generator.getNegativeLink(), src, dst);
							return null;
						}
					}
				}
			}
		}
		return visited;
	}

	@SuppressWarnings("unchecked")
	public void addEdge(UndirectedSparseGraph<V, E> graph, V src, V dst, Transformer<E, Sign> signTransformer) {

		Random rnd = new Random();
		
		if (graph.getNeighborCount(src) == 0) {
			graph.addEdge(rnd.nextDouble() < 0.8 ? (E) generator.getPositiveLink() : (E) generator.getNegativeLink(), src, dst);
		} else {
			
			Set<V> visitedSrc = visited(graph, src, dst, signTransformer);
			
			Set<V> visitedDst = visited(graph, dst, src, signTransformer);
			
			
			if (visitedSrc != null && visitedDst != null) {
				for (V v1 : visitedDst) {
					for (V v2 : visitedSrc) {
						E e = graph.findEdge(v1, v2);
						if (e != null && signTransformer.transform(e) == Sign.NEGATIVE) {
							graph.addEdge((E) generator.getNegativeLink(), src, dst);
							return;
						}
					}
				}
				graph.addEdge(rnd.nextDouble() < 0.75 ? (E) generator.getPositiveLink() : (E) generator.getNegativeLink(), src, dst);
			}
		}
	}
	
}
