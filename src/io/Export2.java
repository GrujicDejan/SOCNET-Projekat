package io;

import java.io.FileWriter;
import java.io.IOException;

import org.apache.commons.collections15.Transformer;

import edu.uci.ics.jung.graph.UndirectedSparseGraph;
import model.link.Sign;

public class Export2<V, E> {
	
	Transformer<E, Sign> signTransformer; 
	
	public Export2(Transformer<E, Sign> signTransformer) {
		this.signTransformer = signTransformer;
	}

	public void exportGML(UndirectedSparseGraph<V, E> graph, String fileName) {
		StringBuilder sb = new StringBuilder();
		sb.append("graph [\n\tmultigraph 1\n");
		int id = 0;
		for (V v : graph.getVertices()) {
			sb.append("    node [\n"
					+ "    id " + id + "\n"
					+ "    label \" " + v + "\"\n"
					+ "  ]\n");
			id++;
		}
		
		int i = 0;
		for(V v1 : graph.getVertices()) {
			int j = 0;
			for (V v2 : graph.getVertices()) {
				E e = graph.findEdge(v1, v2);
				if (e != null) {
					sb.append("  edge [\n"
							+ "    source " + i +"\n"
							+ "    target " + j +"\n"
							+ "    sign " + signTransformer.transform(e) +"\n"
							+ "  ]\n");
				}
				j++;
			}
			i++;
		}
		sb.append("]");
		
		try {
			FileWriter fw = new FileWriter("res/" + fileName + ".gml");
			fw.write(sb.toString());
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
