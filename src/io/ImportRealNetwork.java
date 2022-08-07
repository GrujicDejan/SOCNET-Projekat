package io;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
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
		
		try (BufferedReader in = new BufferedReader(new FileReader(file))) {
			for (int i = 0; i < 4; i++) {
				in.readLine();
			}
			
			UndirectedSparseGraph<Node, SignedLink> graph = new UndirectedSparseGraph<>();
			String line;
			for (int i = 0; i < lines && (line = in.readLine()) != null; i++) {
				String[] tokens = line.split("\t");
				
				String id1 = tokens[0].trim();
				String id2 = tokens[1].trim();
				Node n1 = new Node("n_" + id1, id1);
				Node n2 = new Node("n_" + id2, id2);
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
		String regex = "(?sm)SRC:(?<node1>.*?) TGT:(?<node2>.*?) VOT:(?<sign>.*?) .*";
		Pattern pattern = Pattern.compile(regex);
		Matcher m = null;
		try (BufferedReader in = new BufferedReader(new FileReader(file))) {
			UndirectedSparseGraph<Node, SignedLink> graph = new UndirectedSparseGraph<>();
			String line;
			for (int i = 0; i < lines; i++) {
				StringBuilder sb = new StringBuilder();
				for (int n = 0; n < 7  && (line = in.readLine()) != null; n++) {
					sb.append(line + " ");
				}
				
				m = pattern.matcher(sb.toString());
				if (m.find()) {
					Integer s = Integer.parseInt(m.group("sign"));
					
					if (s != 0) {
					
						Node n1 = new Node(m.group("node1").trim(), m.group("node1").trim());
						Node n2 = new Node(m.group("node2").trim(), m.group("node2").trim());
						Sign sign = Sign.getSign(s);
						
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
					
				}
				
			}
			return graph;
		}
	
	}
	
	
}
