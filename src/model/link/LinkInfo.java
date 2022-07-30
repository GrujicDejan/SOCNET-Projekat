package model.link;

public class LinkInfo<V, E> extends MarkedLink {
	
	private V source;
	private V target;
	private E link;
	
	public LinkInfo(Mark mark, V source, V target, E link) {
		super(mark);
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
		return "LinkInfo [source=" + source + ", target=" + target + ", link=" + link + "]";
	}
	
}
