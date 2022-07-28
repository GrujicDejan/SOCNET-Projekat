package model.edge;

public class MarkedEdge<E, V> {
	
	private E link;
	private V src;
	private V dest;
	private Mark mark;
	
	public MarkedEdge(E edge, V src, V dest, Mark mark) {
		this.link = edge;
		this.src = src;
		this.dest = dest;
		this.mark = mark;
	}
		
	public E getLink() {
		return link;
	}

	public V getSrc() {
		return src;
	}

	public V getDest() {
		return dest;
	}
	
	public Mark getMark() {
		return this.mark;
	}
	
	public void setNewMark(Mark newMark) {
		this.mark = newMark;
	}

	@Override
	public String toString() {
		String s = link.toString() + "[src:"+ src.toString() + ", dest:" + dest.toString();
		if (mark == Mark.NEGATIVE) {
			s += ", mark: (-)]";
		} else if (mark == Mark.POSITIVE) {
			s += ", mark: (+)]";
		} else {
			s += "]";
		}
		
		return s;
	}

}
