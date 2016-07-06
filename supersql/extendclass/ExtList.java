package supersql.extendclass;

import java.util.ArrayList;
import java.util.Collection;

public class ExtList<T> extends ArrayList<T> {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	public ExtList () {
		super();
	}

	public ExtList(Collection<T> c) {
		super(c);
	}

	public ExtList<T> ExtsubList(int fromIndex, int toIndex) {
		return new ExtList<T>(this.subList(fromIndex, toIndex));
	}

	public int contain_itemnum() {
		return this.unnest().size();
	}

	public ExtList<T> unnest() {
		ExtList<T> list = new ExtList<T>();
		for (int i = 0; i < this.size(); i++) {
			T o = this.get(i);
			if (o instanceof ExtList) {
					list.addAll(((ExtList<T>) o).unnest());
			} else {
				list.add((T) o);
			}
		}
		return list;
	}
	
	
	/*
	 * Below functions are functions dedicated for better reading of info from parser
	 */
	public boolean containsNodeInDepth(String s){
		return !getNodesInDepth(s).isEmpty();
	}
	
	/**
	 * Get a list of sublists of this ExtList such as its first element is the string s
	 */
	public ArrayList<ExtList> getNodesInDepth(String s){
		ArrayList<ExtList> nodes = new ArrayList<ExtList>();
		this.getNodesInDepth(s, nodes);
		return nodes;
	}
	
	private void getNodesInDepth(String s, ArrayList<ExtList> nodes){
		for(int i=0; i<this.size(); i++){
			Object o = this.get(i);
			if(i==0 && s.equals(o)){
				nodes.add(this);
				break;
			}
			if(o instanceof ExtList)
				((ExtList)o).getNodesInDepth(s, nodes);
		}
	}
	
	public ExtList addObject(int index, T t){
		if(index >= this.size()){
			for(int i=this.size(); i<index; i++){
				this.add(null);
			}
		
			this.add(index, t);
		}
		else
			this.set(index, t);
		
		return this;
	}
}