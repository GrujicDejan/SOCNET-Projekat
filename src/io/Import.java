package io;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

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
	
	public UndirectedSparseGraph<Node, SignedLink> importSmallGraph(String file)
			throws IllegalArgumentException, FileNotFoundException, IOException {
		List<Node> nodes = new ArrayList<Node>();
		
		try (BufferedReader in = new BufferedReader(new FileReader(file))) {
			for (int i = 0; i < 4; i++) {
				in.readLine();
			}
			
			UndirectedSparseGraph<Node, SignedLink> graph = new UndirectedSparseGraph<>();
			String line;
			while ((line = in.readLine()) != null) {
				String[] tokens = line.split("\t");
				
				Node n1 = null, n2 = null;
				try {
					n1 = nodes.stream().filter(n -> n.getId().equals(tokens[0].trim())).findFirst().get();
					n2 = nodes.stream().filter(n -> n.getId().equals(tokens[1].trim())).findFirst().get();
				} catch (NoSuchElementException e) {
					// TODO: handle exception
				}

				if (n1 == null) {
					n1 = new Node("n_" + tokens[0].trim(), tokens[0].trim());
					nodes.add(n1);
				}
				if (n2 == null) {
					n2 = new Node("n_" +tokens[1].trim(), tokens[1].trim());
					nodes.add(n2);
				}

				Sign sign = Sign.getSign(Integer.parseInt(tokens[2].trim()));
				
				SignedLink link = graph.findEdge(n1, n2);
				
				if (link == null) { // Ako u grafu ne postoji veza izmejdu n1 i n2 spoji ih
					graph.addEdge(new SignedLink(sign), n1, n2);
				} else { // Ako postoji, odredi znak (+,+ = +) (+,- = -) (-,- = +)
					if (sign == Sign.NEGATIVE) {
						if (link.getSign() == Sign.POSITIVE) {
							link.setNewSign(Sign.NEGATIVE);
						} else if (link.getSign() == Sign.NEGATIVE) {
							link.setNewSign(Sign.POSITIVE);
						}
					}
				}
				
			}
			return graph;
		}
	}

}
