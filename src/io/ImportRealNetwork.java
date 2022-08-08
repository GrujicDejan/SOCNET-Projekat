package io;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import edu.uci.ics.jung.graph.UndirectedSparseGraph;
import model.link.Sign;
import model.link.SignedLink;
import model.node.Node;

public class ImportRealNetwork {

	public static final int MAX_LINES = 4000;
	
	public static UndirectedSparseGraph<Node, SignedLink> readSlashdotOrEpinions(String file) 
			throws IllegalArgumentException, FileNotFoundException, IOException {
		return readSlashdotOrEpinions(file, MAX_LINES);
	}
	
	public static UndirectedSparseGraph<Node, SignedLink> readSlashdotOrEpinions(String file, int lines)
			throws IllegalArgumentException, FileNotFoundException, IOException {
		List<Node> nodes = new ArrayList<Node>();
		
		try (BufferedReader in = new BufferedReader(new FileReader(file))) {
			for (int i = 0; i < 4; i++) {
				in.readLine();
			}
			
			UndirectedSparseGraph<Node, SignedLink> graph = new UndirectedSparseGraph<>();
			String line;
			for (int i = 0; i < lines && (line = in.readLine()) != null; i++) {
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
	
	public static UndirectedSparseGraph<Node, SignedLink> readWiki(String file) throws FileNotFoundException, IOException {
		return readWiki(file, MAX_LINES);
	}
	
	public static UndirectedSparseGraph<Node, SignedLink> readWiki(String file, int lines) throws FileNotFoundException, IOException {
		List<Node> nodes = new ArrayList<>();
		
		try (BufferedReader in = new BufferedReader(new FileReader(file))) {
			String regex = "(?sm)SRC:(?<node1>.*?) TGT:(?<node2>.*?) VOT:(?<sign>.*?) .*";
			Pattern pattern = Pattern.compile(regex);
			
			UndirectedSparseGraph<Node, SignedLink> graph = new UndirectedSparseGraph<>();
			String line;
			for (int i = 0; i < lines; i++) {
				StringBuilder sb = new StringBuilder();
				for (int n = 0; n < 8  && (line = in.readLine()) != null; n++) {
					sb.append(line + " ");
				}
				
				Matcher m = pattern.matcher(sb.toString());
				if (m.find()) {

					Integer s = Integer.parseInt(m.group("sign"));
					
					if (s != 0) {
						Node n1 = null, n2 = null;
						try {
							n1 = nodes.stream().filter(n -> n.getId().equals(m.group("node1"))).findFirst().get();
							n2 = nodes.stream().filter(n -> n.getId().equals(m.group("node2"))).findFirst().get();
						} catch (NoSuchElementException e) {
							// TODO: handle exception
						}

						if (n1 == null) {
							n1 = new Node("n_" + m.group("node1"), m.group("node1"));
							nodes.add(n1);
						}
						if (n2 == null) {
							n2 = new Node("n_" + m.group("node2"), m.group("node2"));
							nodes.add(n2);
						}

						Sign sign = Sign.getSign(s);
						
						SignedLink link = graph.findEdge(n1, n2);
						
						if (link == null) { // Ako u grafu ne postoji veza izmejdu n1 i n2 spoji ih
							graph.addEdge(new SignedLink(sign), n1, n2);
						} else { // Ako postoji, odredi znak (+,+ = +) (+,- = -) (-,- = +)
							if (sign == Sign.NEGATIVE && link.getSign() == Sign.NEGATIVE) {
								link.setNewSign(Sign.POSITIVE);
							}
						}
						
					}
					
				}
				
			}
			return graph;
		}
	
	}
	
	
}
