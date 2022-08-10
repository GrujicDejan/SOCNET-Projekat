package tests;

import java.util.List;

import org.apache.commons.collections15.Transformer;

import clusterability.ComponentClustererBFS;
import edu.uci.ics.jung.graph.UndirectedSparseGraph;
import edu.uci.ics.jung.io.GraphIOException;
import model.link.LinkInfo;
import model.link.Sign;
import model.link.SignedLink;
import tests.metrics.NetworkMetrics;

public class Menu<V, E> {
	
	private Transformer<SignedLink, Sign> signTransformer;
	private String name;
	
	public Menu(Transformer<SignedLink, Sign> signTransformer, String graphName) {
		this.signTransformer = signTransformer;
		this.name = graphName;
	}
	
	public void printMenu() {
		System.out.println("1  -> Prikaz osnovnih informacija o mrezi"); // br cvorova i grana
		System.out.println("2  -> Da li je mreza klasterabilna?");
		System.out.println("3  -> Koliko mreza klastera ima cvorova?");
		System.out.println("4  -> Koliko mreza ima klastera koji nisu koalicije?");
		System.out.println("5  -> Koje veze treba izbrisati da bi mreza bila klasterabilna?");
		System.out.println("6  -> Koliko mreza ima koalicija?");
		System.out.println("7  -> Koliko graf ima pozitivnih linkova?");
		System.out.println("8  -> Koliko graf ima negativnih linkova?");
		System.out.println("9  -> Analiza gigantskoj komponenti?");
		System.out.println("10 -> Export giganteske komponente u graphml fajl?");
		System.out.println("11 -> Export mreze u graphml fajl?");
		System.out.println("0  -> KRAJ");
	}
	
	public void get(ComponentClustererBFS<V, E> ccbfs, int choice) {
		switch (choice) {
		case 1: {
			System.out.println("\nBroj cvorova -> " + ccbfs.getNumberOfNodes() + "\n" + "Broj linkova -> " + ccbfs.getNumberOfLinks());
			try {
				NetworkMetrics<V, E> m = new NetworkMetrics<V, E>(ccbfs.getGraph());
				System.out.println("Small world koeficijent ->" + m.getSmallWorldCoeff());
				System.out.println("Prosecan koeficijent klasterisanja ->" + m.averageClusteringCoeficient());
				List<V> nodes = m.getNodesWithMaxClusteringCoefficient();
				if (nodes.size() == 1) {
					System.out.println("Cvor sa najvecim stepenom klasterisanja ->" + m.getNodesWithMaxClusteringCoefficient());
				} else {
					System.out.println("Cvorovi sa najvecim stepenom klasterisanja:");
					for (V v : nodes) {
						System.out.println(v);
					}
				}
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
			}
			break;
		}
		case 2: {
			if (ccbfs.isClusterable()) {
				System.out.println("Mreza je klasterabilna");
			} else {
				System.out.println("Mreza nije klasterabilna");
			}
			break;
		}
		case 3: {
			System.out.println("Mreza ima " + ccbfs.getClusterNetwork().getVertexCount() + " klastera");
			break;
		}
		case 4: {
			System.out.println("Mreza ima " + ccbfs.getNonCoalitions().size() + " klastera koji nisu koalicije");
			break;
		}
		case 5: {
			try {
				List<LinkInfo<V, E>> links = ccbfs.getNegativeLinks();
				System.out.println("Broj linkova koje treba izbrisati: " + links.size());
				System.out.println("Linkovi koje treba izbrisati da bi mreza bila klasterabilna:");
				for (LinkInfo<V, E> l : links) {
					System.out.println(l);
				}
			} catch (GraphIOException e) {
				System.out.println("Nije potrebno ukloniti linkove, mreza je klasterabilna");
			}
			break;
		}
		case 6: {
			System.out.println("Mreza ima " + ccbfs.getCoalitions().size() + " koalicija");
			break;
		}
		case 7: {
			long n = ccbfs.getGraph()
					.getEdges()
					.stream()
					.filter(l -> signTransformer.transform((SignedLink) l) == Sign.POSITIVE)
					.count();
			System.out.println("Graf ima " + n + " pozitivnih linkova");
			break;
		}
		case 8: {
			long n = ccbfs.getGraph()
					.getEdges()
					.stream()
					.filter(l -> signTransformer.transform((SignedLink) l) == Sign.NEGATIVE)
					.count();
			System.out.println("Graf ima " + n + " negativnih linkova");
			break;
		}
		case 9: {
			UndirectedSparseGraph<V, E> gc = ccbfs.getGiantComponent();
			System.out.println("Gigantska komponenta ima " + gc.getVertexCount() + " cvorova");
			System.out.println("Gigantska komponenta ima " + gc.getEdgeCount() + " linkova");
			NetworkMetrics<V, E> m = new NetworkMetrics<V, E>(gc);
			System.out.println("Small world koeficijent klastera ->" + m.getSmallWorldCoeff());
			System.out.println("Prosecan koeficijent klasterisanja ->" + m.averageClusteringCoeficient());
			List<V> nodes = m.getNodesWithMaxClusteringCoefficient();
			if (nodes.size() < 2) {
				System.out.println("Cvor sa najvecim stepenom klasterisanja ->" + m.getNodesWithMaxClusteringCoefficient());
			} else {
				System.out.println("Cvorovi sa najvecim stepenom klasterisanja:");
				for (int i = 0; i < nodes.size(); i++) {
					System.out.println(nodes.get(i));
				}
			}
			break;
		}
		case 10: {
			ccbfs.exportGigantComponentToGraphML(name + "_gigantComponent");
			System.out.print("Gigantska komponenta expotovana u gml formatu");
			break;
		}
		case 11: {
			ccbfs.exportNetworkToGraphML(name);
			System.out.print("Mreza expotovana u gml formatu");
			break;
		}
		default:
			System.out.println("Pogresan unos!");
		}
	}

}
