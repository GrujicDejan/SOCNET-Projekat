package model.networks;

import java.util.function.Supplier;

import model.link.Sign;
import model.link.SignedLink;
import model.node.Node;

public class Generator {
	
	Supplier<Node> nodeGenerator;
	Supplier<SignedLink> positiveLinkGenerator;
	Supplier<SignedLink> negativeLinkGenerator;
	
	public Generator() {
		
		nodeGenerator = new Supplier<Node>() {
			private int i = -1;
			@Override
			public Node get() {
				i++;
				return new Node("n_" + i, "" + i);
			}
		};
		
		positiveLinkGenerator = new Supplier<SignedLink>() {
			@Override
			public SignedLink get() {
				return new SignedLink(Sign.POSITIVE);
			}
		};
		
		negativeLinkGenerator = new Supplier<SignedLink>() {
			@Override
			public SignedLink get() {
				return new SignedLink(Sign.NEGATIVE);
			}
		};
		
	}
	
	public Node getNode() {
		return nodeGenerator.get();
	}
	
	public SignedLink getNegativeLink() {
		return negativeLinkGenerator.get();
	}
	
	public SignedLink getPositiveLink() {
		return positiveLinkGenerator.get();
	}

}
