package tests.modelNetworks;

import java.util.Scanner;

import org.apache.commons.collections15.Transformer;

import clusterability.ComponentClustererBFS;
import edu.uci.ics.jung.graph.UndirectedSparseGraph;
import model.link.Sign;
import model.link.SignedLink;
import model.networks.BarabasiAlbertModel;
import model.node.Node;
import tests.Menu;

public class BarabasiAlbertModelTest {
	
	private static UndirectedSparseGraph<Node, SignedLink> graph = new UndirectedSparseGraph<>();
	private static Transformer<SignedLink, Sign> signTransformer;
	
	public BarabasiAlbertModelTest(int n, int m0, int e0, int m) {
		
		signTransformer = new Transformer<SignedLink, Sign>() {
			@Override
			public Sign transform(SignedLink ml) {
				return ml.getSign();
			}

		};
		
		BarabasiAlbertModel<Node, SignedLink> ba = new BarabasiAlbertModel<Node, SignedLink>(n, m0, e0, m, signTransformer);
		ba.getGraph(graph);
	}

	@SuppressWarnings("unchecked")
	public static <V, E> void main(String[] args) {
		@SuppressWarnings("unused")
		BarabasiAlbertModelTest ba = new BarabasiAlbertModelTest(300, 10, 30, 4);
		
		UndirectedSparseGraph<Node, SignedLink> baGraph = graph;
		ComponentClustererBFS<Node, SignedLink> ccbfs = new ComponentClustererBFS<Node, SignedLink>(baGraph, signTransformer);
		
		Menu<V, E> menu = new Menu<>(signTransformer, "BarabasiAlbert");
		menu.printMenu();
		try (Scanner sc = new Scanner(System.in)) {
			System.out.println("\n-------------------------------\nVas izbor: ");
			int in = sc.nextInt();
			while (in != 0) {
				menu.get((ComponentClustererBFS<V, E>) ccbfs, in);
				//menu.printMenu();
				System.out.println("\n-------------------------------\nVas izbor: ");
				in = sc.nextInt();
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

}
