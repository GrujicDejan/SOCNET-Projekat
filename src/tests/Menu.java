package tests;

import java.util.List;

import org.apache.commons.collections15.Transformer;

import clusterability.ComponentClustererBFS;
import edu.uci.ics.jung.graph.UndirectedSparseGraph;
import edu.uci.ics.jung.io.GraphIOException;
import model.link.LinkInfo;
import model.link.Sign;
import model.link.SignedLink;

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
			System.out.println("Broj cvorova -> " + ccbfs.getNumberOfNodes() + "\n" + "Broj linkova -> " + ccbfs.getNumberOfLinks());
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
			break;
		}
		case 10: {
			ccbfs.exportGigantComponentToGraphML(name + "_gigantComponent");
			break;
		}
		case 11: {
			ccbfs.exportNetworkToGraphML("name");
			break;
		}
		default:
			throw new IllegalArgumentException("Unexpected value: " + choice);
		}
	}

}
