package io;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import org.apache.commons.collections15.Transformer;

import edu.uci.ics.jung.graph.UndirectedSparseGraph;
import edu.uci.ics.jung.io.GraphMLWriter;
import model.link.Sign;
import model.link.SignedLink;
import model.node.Node;

public class Export {

	GraphMLWriter<Node, SignedLink> writer = new GraphMLWriter<Node, SignedLink>();

	public void exportToGraphML(String fileName, UndirectedSparseGraph<Node, SignedLink> g) {

		writer.setVertexIDs(new Transformer<Node, String>() {
			public String transform(Node n) {
				return n.getId();
			}
		});

		writer.setEdgeIDs(new Transformer<SignedLink, String>() {
			public String transform(SignedLink ml) {
				return ml.getId();
			}
		});

		writer.addEdgeData("sign", "sign", "", new Transformer<SignedLink, String>() {
			public String transform(SignedLink ml) {
				if (ml.getSign() == Sign.POSITIVE) {
					return "1";
				} else {
					return "-1";
				}
			}
		});
		
		
		try {
			PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter("res/" + fileName + ".graphml")));
			writer.save(g, pw);
		} catch (IOException e) {
			System.out.println("err..." + e);
		}

	}

}
