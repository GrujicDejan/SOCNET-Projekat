package clusterability;

import java.util.List;

import org.apache.commons.collections15.Transformer;

import edu.uci.ics.jung.graph.UndirectedSparseGraph;
import edu.uci.ics.jung.io.GraphIOException;
import io.Export;
import io.Import;
import model.link.LinkInfo;
import model.link.Sign;
import model.link.SignedLink;
import model.node.ClusterNode;
import model.node.Node;

public class TestMain {

	public static <V, E> void main(String[] args) throws GraphIOException {
		
		Import imp = new Import();
		UndirectedSparseGraph<Node, SignedLink> g1 =  imp.importGraph("res/slashdot.graphml");
		
		Export e = new Export();
		e.exportToGraphML("test3", g1);
		
		Transformer<SignedLink, Sign> signTransformer = new Transformer<SignedLink, Sign>() {
			@Override
			public Sign transform(SignedLink ml) {
				return ml.getSign();
			}

		};
		
		@SuppressWarnings("unchecked")
		ComponentClustererBFS<V, E> ccbfs = new ComponentClustererBFS<V, E>((UndirectedSparseGraph<V, E>) g1, (Transformer<E, Sign>) signTransformer);
		
//		ccbfs.exportComponentToGraphML("test2");
		
		List<UndirectedSparseGraph<V, E>> comps = ccbfs.getComponents();
		System.out.println(comps.size());
//		for (int i = 0; i < comps.size(); i++) {
//			e.exportToGraphML("comp_"+i, (UndirectedSparseGraph<Node, SignedLink>) comps.get(i));
//		}
//
		System.out.println("is clusterable => " + ccbfs.isClusterable());

//		UndirectedSparseGraph<ClusterNode, E> clusterNetwork = ccbfs.getClusterNetwork();
//		for (ClusterNode cn : clusterNetwork.getVertices()) {
//			System.out.println(cn.getId() + " - " + cn.getLabel() + " - " + cn.getNumberOfNodes() + " - "  +cn.isCoaltion());
//		}
		
		List<LinkInfo<V, E>> negativeLinks = ccbfs.getNegativeLinks();
		for (LinkInfo<V, E> l : negativeLinks) {
			System.out.println(((Node)l.getTarget()).getId() + " -- " + ((Node)l.getSource()).getId());
		}
	}
	
}



