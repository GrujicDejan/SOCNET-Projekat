package io;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;

import org.apache.commons.collections15.Transformer;

import edu.uci.ics.jung.graph.UndirectedSparseGraph;
import edu.uci.ics.jung.io.GraphIOException;
import edu.uci.ics.jung.io.graphml.EdgeMetadata;
import edu.uci.ics.jung.io.graphml.GraphMLReader2;
import edu.uci.ics.jung.io.graphml.GraphMetadata;
import edu.uci.ics.jung.io.graphml.HyperEdgeMetadata;
import edu.uci.ics.jung.io.graphml.NodeMetadata;
import model.link.Mark;
import model.link.MarkedLink;
import model.node.Node;

public class Import {
	
	BufferedReader fileReader;
	
	public UndirectedSparseGraph<Node, MarkedLink> importGraph(String fileName) {
		
		try {
			fileReader = new BufferedReader(new FileReader(fileName));
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		Transformer<GraphMetadata, UndirectedSparseGraph<Node, MarkedLink>> gt = new Transformer<GraphMetadata, UndirectedSparseGraph<Node, MarkedLink>>() {
			public UndirectedSparseGraph<Node, MarkedLink> transform(GraphMetadata metadata) {
				return new UndirectedSparseGraph<Node, MarkedLink>();
			}
		};

		Transformer<NodeMetadata, Node> nt = new Transformer<NodeMetadata, Node>() {
			public Node transform(NodeMetadata metadata) {
				String id = metadata.getId();
				Node n = new Node(id, id);
				return n;
			}
		};

		Transformer<EdgeMetadata, MarkedLink> lt = new Transformer<EdgeMetadata, MarkedLink>() {
			public MarkedLink transform(EdgeMetadata metadata) {
				int m = Integer.parseInt(metadata.getProperty("mark"));
				Mark mark = Mark.POSITIVE;
				if (m == -1) {
					mark = Mark.NEGATIVE;
				}
				MarkedLink ml = new MarkedLink(mark);
				ml.setId(metadata.getSource() + "-" + metadata.getTarget());
				return new MarkedLink(mark);
			}
		};

		Transformer<HyperEdgeMetadata, MarkedLink> ht = new Transformer<HyperEdgeMetadata, MarkedLink>() {
			public MarkedLink transform(HyperEdgeMetadata metadata) {
				return new MarkedLink();
			}
		};

		GraphMLReader2<UndirectedSparseGraph<Node, MarkedLink>, Node, MarkedLink> reader = new GraphMLReader2<UndirectedSparseGraph<Node, MarkedLink>, Node, MarkedLink>(
				fileReader, gt, nt, lt, ht);

		try {
			UndirectedSparseGraph<Node, MarkedLink> g = reader.readGraph();
			return g;
		} catch (GraphIOException e) {
			System.out.println("Greska prilikom ucitavanja mreze" + e);
		}
		return null;

	}

}
