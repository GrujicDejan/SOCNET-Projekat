package clusterability;

import edu.uci.ics.jung.graph.UndirectedSparseGraph;
import io.Export;
import io.Import;
import model.edge.MarkedLink;
import model.node.Node;

public class TestMain {

	public static <V, E> void main(String[] args) {
		
		Import imp = new Import();
		UndirectedSparseGraph<Node, MarkedLink> g1 =  imp.importGraph("res/simple.graphml");
		
//		Export e = new Export();
//		e.exportToGraphML("test3", g1);
		
		
		@SuppressWarnings("unchecked")
		ComponentClustererBFS<V, E> ccbfs = new ComponentClustererBFS<V, E>((UndirectedSparseGraph<V, E>) g1);
		
		ccbfs.exportComponentToGraphML("test");

	}
	
	
//	UndirectedSparseGraph<Node, MarkedLink> g2 = new UndirectedSparseGraph<Node, MarkedLink>();
//	Node n1 = new Node("n_1", "1");
//	Node n2 = new Node("n_2", "2");
//	Node n3 = new Node("n_3", "3");
//	Node n4 = new Node("n_4", "4");
//	g2.addVertex(n1);
//	g2.addVertex(n2);
//	g2.addVertex(n3);
//	g2.addVertex(n4);
//	MarkedLink ml1 = new MarkedLink(Mark.POSITIVE);
//	MarkedLink ml2 = new MarkedLink(Mark.NEGATIVE);
//	g2.addEdge(ml1, n1, n2);
////	g2.addEdge(ml1, n1, n3);
////	g2.addEdge(ml1, n1, n4);
//	g2.addEdge(ml2, n2, n3);
////	g2.addEdge(ml2, n2, n4);
//	
//	Export e = new Export();
//	e.exportToGraphML("2", g2);

}


//<edge id="1" source="n0" target="n2"/>
//<edge id="1" source="n1" target="n2"/>
//<edge id="-1" source="n2" target="n3"/>
//<edge id="1" source="n3" target="n5"/>
//<edge id="-1" source="n3" target="n4"/>
//<edge id="1" source="n4" target="n6"/>
//<edge id="1" source="n6" target="n5"/>
//<edge id="1" source="n5" target="n7"/>
//<edge id="-1"source="n6" target="n8"/>
//<edge id="1" source="n8" target="n7"/>
//<edge id="1" source="n8" target="n9"/>
//<edge id="1" source="n8" target="n10"/>
