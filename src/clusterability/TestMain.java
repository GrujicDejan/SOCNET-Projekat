package clusterability;

import java.util.List;

import org.apache.commons.collections15.Transformer;

import edu.uci.ics.jung.graph.UndirectedSparseGraph;
import io.Export;
import io.Import;
import model.link.Mark;
import model.link.MarkedLink;
import model.node.Node;

public class TestMain {

	public static <V, E> void main(String[] args) {
		
		Import imp = new Import();
		UndirectedSparseGraph<Node, MarkedLink> g1 =  imp.importGraph("res/simple.graphml");
		
		Export e = new Export();
//		e.exportToGraphML("test3", g1);
		
		Transformer<MarkedLink, Mark> markTransformer = new Transformer<MarkedLink, Mark>() {
			@Override
			public Mark transform(MarkedLink ml) {
				return ml.getMark();
			}

		};
		
		@SuppressWarnings("unchecked")
		ComponentClustererBFS<V, E> ccbfs = new ComponentClustererBFS<V, E>((UndirectedSparseGraph<V, E>) g1, (Transformer<E, Mark>) markTransformer); // transforemr??
		
//		ccbfs.exportComponentToGraphML("test2");
//		List<UndirectedSparseGraph<V, E>> comps = ccbfs.getComponents();
//		
//		for (int i = 0; i < comps.size(); i++) {
//			e.exportToGraphML("comp_"+i, (UndirectedSparseGraph<Node, MarkedLink>) comps.get(i));
//		}

		System.out.println(ccbfs.isClusterable());
	}
	
}



