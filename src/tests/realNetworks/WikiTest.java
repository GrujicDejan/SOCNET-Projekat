package tests.realNetworks;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

import org.apache.commons.collections15.Transformer;

import clusterability.ComponentClustererBFS;
import edu.uci.ics.jung.graph.UndirectedSparseGraph;
import io.ImportRealNetwork;
import model.link.Sign;
import model.link.SignedLink;
import model.node.Node;
import tests.Menu;

public class WikiTest {
	
	public static String FILE = "res/wiki-RfA.txt";
	
	private static int MAX_LINES = 20000;
	
	private static Transformer<SignedLink, Sign> signTransformer = new Transformer<SignedLink, Sign>() {
		public Sign transform(SignedLink ml) {
			return ml.getSign();
		}
	};
	
	@SuppressWarnings("unchecked")
	public static <V, E> void main(String[] args) {
		
		try (Scanner sc = new Scanner(System.in)) {
			UndirectedSparseGraph<Node, SignedLink> wikiGraph = ImportRealNetwork.readWiki(FILE, MAX_LINES);
			ComponentClustererBFS<Node, SignedLink> ccbfs = new ComponentClustererBFS<Node, SignedLink>(wikiGraph, signTransformer);
			
			Menu<V, E> menu = new Menu<>(signTransformer, "wiki");
			menu.printMenu();
			System.out.println("\n-------------------------------\nVas izbor: ");
			int in = sc.nextInt();
			while (in != 0) {
				menu.get((ComponentClustererBFS<V, E>) ccbfs, in);
//				menu.printMenu();
				System.out.println("\n-------------------------------\nVas izbor: ");
				in = sc.nextInt();
			}
		}  catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (Exception e) {
			// TODO: handle exceptione
			e.printStackTrace();
			
		}
	}

}
