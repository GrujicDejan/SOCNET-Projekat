package tests.modelNetworks;

import java.util.Scanner;

import org.apache.commons.collections15.Transformer;

import clusterability.ComponentClustererBFS;
import edu.uci.ics.jung.graph.UndirectedSparseGraph;
import model.link.Sign;
import model.link.SignedLink;
import model.networks.WattsStrogatszModel;
import model.node.Node;
import tests.Menu;

public class WattsStrogatszModelTest {
	
	private static UndirectedSparseGraph<Node, SignedLink> graph = new UndirectedSparseGraph<>();
	private static Transformer<SignedLink, Sign> signTransformer;
	
	public WattsStrogatszModelTest(int n, int k, double p) {
		
		signTransformer = new Transformer<SignedLink, Sign>() {
			@Override
			public Sign transform(SignedLink ml) {
				return ml.getSign();
			}

		};
		
		WattsStrogatszModel<Node, SignedLink> ws = new WattsStrogatszModel<Node, SignedLink>(n, k, p, signTransformer);
		ws.getGraph(graph);
	}

	@SuppressWarnings("unchecked")
	public static <V, E> void main(String[] args) {
		@SuppressWarnings("unused")
		WattsStrogatszModelTest ws = new WattsStrogatszModelTest(300, 10, 0.30);
		
		UndirectedSparseGraph<Node, SignedLink> wsGraph = graph;
		ComponentClustererBFS<Node, SignedLink> ccbfs = new ComponentClustererBFS<Node, SignedLink>(wsGraph, signTransformer);
		
		Menu<V, E> menu = new Menu<>(signTransformer, "WattsStrogatsz");
		menu.printMenu();
		try (Scanner sc = new Scanner(System.in)) {
			System.out.println("\n-------------------------------\nVas izbor: ");
			int in = sc.nextInt();
			while (in != 0) {
				menu.get((ComponentClustererBFS<V, E>) ccbfs, in);
				menu.printMenu();
				System.out.println("\n-------------------------------\nVas izbor: ");
				in = sc.nextInt();
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

}
