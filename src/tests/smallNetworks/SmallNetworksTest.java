package tests.smallNetworks;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

import org.apache.commons.collections15.Transformer;

import clusterability.ComponentClustererBFS;
import edu.uci.ics.jung.graph.UndirectedSparseGraph;
import io.Import;
import model.link.Sign;
import model.link.SignedLink;
import model.node.Node;
import tests.Menu;

public class SmallNetworksTest {
	
	public static String FILE1 = "res/clustered1.txt";
	public static String FILE2 = "res/clustered2.txt";
	
	public static String FILE3 = "res/nonclustered1.txt";
	public static String FILE4 = "res/nonclustered2.txt";
	
	private static Transformer<SignedLink, Sign> signTransformer = new Transformer<SignedLink, Sign>() {
		public Sign transform(SignedLink ml) {
			return ml.getSign();
		}
	};
	
	@SuppressWarnings("unchecked")
	public static <V, E> void main(String[] args) {
		
		try (Scanner sc = new Scanner(System.in)) {
			Import imp = new Import();
			UndirectedSparseGraph<Node, SignedLink> clusterd1 = imp.importSmallGraph(FILE1);
			UndirectedSparseGraph<Node, SignedLink> clusterd2 = imp.importSmallGraph(FILE2);
			UndirectedSparseGraph<Node, SignedLink> nonclusterd1 = imp.importSmallGraph(FILE3);
			UndirectedSparseGraph<Node, SignedLink> nonclusterd2 = imp.importSmallGraph(FILE4);
			ComponentClustererBFS<Node, SignedLink> ccbfs1 = new ComponentClustererBFS<Node, SignedLink>(clusterd1, signTransformer);
			ComponentClustererBFS<Node, SignedLink> ccbfs2 = new ComponentClustererBFS<Node, SignedLink>(clusterd2, signTransformer);
			ComponentClustererBFS<Node, SignedLink> ccbfs3 = new ComponentClustererBFS<Node, SignedLink>(nonclusterd1, signTransformer);
			ComponentClustererBFS<Node, SignedLink> ccbfs4 = new ComponentClustererBFS<Node, SignedLink>(nonclusterd2, signTransformer);
			
			Menu<V, E> menu1 = new Menu<>(signTransformer, "clustered1");
			Menu<V, E> menu2 = new Menu<>(signTransformer, "clustered2");
			Menu<V, E> menu3 = new Menu<>(signTransformer, "nonclustered1");
			Menu<V, E> menu4 = new Menu<>(signTransformer, "nonclustered2");
			
			menu1.printMenu();
			System.out.println("\n-------------------------------\nVas izbor: ");
			int in = sc.nextInt();
			while (in != 0) {
				System.out.print("Mreza 1 ---> ");
				menu1.get((ComponentClustererBFS<V, E>) ccbfs1, in);
				System.out.print("\nMreza 2 ---> ");
				menu2.get((ComponentClustererBFS<V, E>) ccbfs2, in);
				System.out.print("\nMreza 3 ---> ");
				menu3.get((ComponentClustererBFS<V, E>) ccbfs3, in);
				System.out.print("\nMreza 4 ---> ");
				menu4.get((ComponentClustererBFS<V, E>) ccbfs4, in);
				System.out.println("\n-------------------------------\n");
				
				menu1.printMenu();
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
