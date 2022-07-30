package io;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import org.apache.commons.collections15.Transformer;

import edu.uci.ics.jung.graph.UndirectedSparseGraph;
import edu.uci.ics.jung.io.GraphMLWriter;
import model.edge.Mark;
import model.edge.MarkedLink;
import model.node.Node;

public class Export {

	GraphMLWriter<Node, MarkedLink> writer = new GraphMLWriter<Node, MarkedLink>();

	public void exportToGraphML(String fileName, UndirectedSparseGraph<Node, MarkedLink> g) {

		writer.setVertexIDs(new Transformer<Node, String>() {
			public String transform(Node n) {
				return n.getId();
			}
		});

		writer.setEdgeIDs(new Transformer<MarkedLink, String>() {
			public String transform(MarkedLink ml) {
				return ml.getId();
			}
		});

		writer.addEdgeData("mark", "mark", "", new Transformer<MarkedLink, String>() {
			public String transform(MarkedLink ml) {
				if (ml.getMark() == Mark.POSITIVE) {
					return "1";
				} else {
					return "-1";
				}
			}
		});
		
//		GraphMLMetadata<MarkedLink> gmlm = new GraphMLMetadata<MarkedLink>("mark", "", new Transformer<MarkedLink, String>() {
//			public String transform(MarkedLink ml) {
//				if (ml.getMark() == Mark.POSITIVE) {
//					return "1";
//				} else {
//					return "-1";
//				}
//			}
//		});
//		Map<String, GraphMLMetadata<MarkedLink>> edge_map = new HashMap<>();
//		edge_map.put("mark", gmlm);
//		
//		writer.setEdgeData(edge_map);
		
		
		try {
			PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter("res/" + fileName + ".graphml")));
			writer.save(g, pw);
		} catch (IOException e) {
			System.out.println("err..." + e);
		}

	}

}
