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
	private Set<V> visited(UndirectedSparseGraph<V, E> targetGraph, V src, V dst, Transformer<E, Sign> markTransformer){
		Queue<V> queue = new LinkedList<>();
		Set<V> visited = new HashSet<>();
		queue.add(src);
		visited.add(src);
		while (!queue.isEmpty()) {
			V curr = queue.poll();
			for (V v : targetGraph.getNeighbors(curr)) {
				if (!visited.contains(v)) {
					if (markTransformer.transform(targetGraph.findEdge(curr, v)) == Sign.POSITIVE) {
						visited.add(v);
						queue.add(v);
						if (v.equals(dst)) {
							targetGraph.addEdge((E) generator.getPositiveLink(), src, dst);
							return null;
						}
					} else {
						if (v.equals(dst)) {
							targetGraph.addEdge((E) generator.getNegativeLink(), src, dst);
							return null;
						}
					}
				}
			}
		}
		return visited;
	}

	@SuppressWarnings("unchecked")
	public void addEdge(UndirectedSparseGraph<V, E> targetGraph, V src, V dst, Transformer<E, Sign> markTransformer) {

		Random rnd = new Random();
		
		if (targetGraph.getNeighborCount(src) == 0) {
			targetGraph.addEdge(rnd.nextDouble() < 0.8 ? (E) generator.getPositiveLink() : (E) generator.getNegativeLink(), src, dst);
		} else {
			
			Set<V> visitedSrc = visited(targetGraph, src, dst, markTransformer);
			
			Set<V> visitedDst = visited(targetGraph, dst, src, markTransformer);
			
			
			if (visitedSrc != null && visitedDst != null) {
				for (V v1 : visitedDst) {
					for (V v2 : visitedSrc) {
						E e = targetGraph.findEdge(v1, v2);
						if (e != null && markTransformer.transform(e) == Sign.NEGATIVE) {
							targetGraph.addEdge((E) generator.getNegativeLink(), src, dst);
							return;
						}
					}
				}
				targetGraph.addEdge(rnd.nextDouble() < 0.75 ? (E) generator.getPositiveLink() : (E) generator.getNegativeLink(), src, dst);
			}
		}
	}
	
}
