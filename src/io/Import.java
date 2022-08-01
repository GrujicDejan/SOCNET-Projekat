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
import model.link.Sign;
import model.link.SignedLink;
import model.node.Node;

public class Import {
	
	BufferedReader fileReader;
	
	public UndirectedSparseGraph<Node, SignedLink> importGraph(String fileName) {
		
		try {
			fileReader = new BufferedReader(new FileReader(fileName));
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		Transformer<GraphMetadata, UndirectedSparseGraph<Node, SignedLink>> gt = new Transformer<GraphMetadata, UndirectedSparseGraph<Node, SignedLink>>() {
			public UndirectedSparseGraph<Node, SignedLink> transform(GraphMetadata metadata) {
				return new UndirectedSparseGraph<Node, SignedLink>();
			}
		};

		Transformer<NodeMetadata, Node> nt = new Transformer<NodeMetadata, Node>() {
			public Node transform(NodeMetadata metadata) {
				String id = metadata.getId();
				Node n = new Node(id, id);
				return n;
			}
		};

		Transformer<EdgeMetadata, SignedLink> lt = new Transformer<EdgeMetadata, SignedLink>() {
			public SignedLink transform(EdgeMetadata metadata) {
				String m = metadata.getProperty("sign");
				Sign sign = Sign.POSITIVE;
				if (m.equals("NEGATIVE")) {
					sign = Sign.NEGATIVE;
				}
				SignedLink ml = new SignedLink(sign);
				ml.setId(metadata.getSource() + "-" + metadata.getTarget());
				return new SignedLink(sign);
			}
		};

		Transformer<HyperEdgeMetadata, SignedLink> ht = new Transformer<HyperEdgeMetadata, SignedLink>() {
			public SignedLink transform(HyperEdgeMetadata metadata) {
				return new SignedLink();
			}
		};

		GraphMLReader2<UndirectedSparseGraph<Node, SignedLink>, Node, SignedLink> reader = new GraphMLReader2<UndirectedSparseGraph<Node, SignedLink>, Node, SignedLink>(
				fileReader, gt, nt, lt, ht);

		try {
			UndirectedSparseGraph<Node, SignedLink> g = (UndirectedSparseGraph<Node, SignedLink>) reader.readGraph();
			return g;
		} catch (GraphIOException e) {
			System.out.println("Greska prilikom ucitavanja mreze" + e);
		}
		return null;

	}

}
