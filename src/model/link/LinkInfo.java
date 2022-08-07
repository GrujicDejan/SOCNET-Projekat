package model.link;

public class LinkInfo<V, E> extends SignedLink {
	
	private V source;
	private V target;
	private E link;
	
	public LinkInfo(Sign sign, V source, V target, E link) {
		super(sign);
		this.source = source;
		this.target = target;
		this.link = link;
	}
	
	public V getSource() {
		return source;
	}
	public V getTarget() {
		return target;
	}
	public E getLink() {
		return link;
	}

	@Override
	public String toString() {
		return "[source=" + source + ", target=" + target + ", sign=" + getSign() + "]";
	}
	
}
