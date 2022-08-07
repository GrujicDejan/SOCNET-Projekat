package tests.modelNetworks;

import java.util.Scanner;

import org.apache.commons.collections15.Transformer;

import clusterability.ComponentClustererBFS;
import edu.uci.ics.jung.graph.UndirectedSparseGraph;
import model.link.Sign;
import model.link.SignedLink;
import model.networks.ErdosRenyiModel;
import model.node.Node;
import tests.Menu;


public class ErdosRenyiModelTest {

	private static UndirectedSparseGraph<Node, SignedLink> graph = new UndirectedSparseGraph<>();
	private static Transformer<SignedLink, Sign> signTransformer;
	
	public ErdosRenyiModelTest(int nodes, int edges) {
		
		signTransformer = new Transformer<SignedLink, Sign>() {
			@Override
			public Sign transform(SignedLink ml) {
				return ml.getSign();
			}

		};
		
		ErdosRenyiModel<Node, SignedLink> er = new ErdosRenyiModel<Node, SignedLink>(nodes, edges, signTransformer);
		er.getGraph(graph);
	}

	@SuppressWarnings("unchecked")
	public static <V, E> void main(String[] args) {
		@SuppressWarnings("unused")
		ErdosRenyiModelTest er = new ErdosRenyiModelTest(150, 300);
		
		UndirectedSparseGraph<Node, SignedLink> erGraph = graph;
		ComponentClustererBFS<Node, SignedLink> ccbfs = new ComponentClustererBFS<Node, SignedLink>(erGraph, signTransformer);
		
		Menu<V, E> menu = new Menu<>(signTransformer, "ErdosRenyi");
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
