package router;

public class Node<D>{

	private D data;
	private Node<D> next;
	
	/**
	 * Constructs a new node with data and a next Node
	 * @param d String Data
	 * @param n Next Node
	 */
	public Node(D d, Node<D> n){
		data = d;
		next = n;
	}
	
	/**
	 * Makes an exact copy of a Node
	 * @param n Node to be copied
	 */
	public Node(Node<D> n) {
		data = n.getData();
		next = n.getNext();
	}
	
	/**
	 * Make a new node with string data
	 * @param d data
	 */
	public Node(D d){
		data = d;
		next = null;
	}
	
	/**
	 * Set the node's data
	 * @param s data
	 */
	public void setData(D s){
		data = s;
	}
	
	/**
	 * Set the next node 
	 * @param n next Node
	 */
	public void setNext(Node<D> n){
		next = n;
	}
	
	/**
	 * Get the next node
	 * @return returns Node Next
	 */
	public Node<D> getNext(){
		return next;
	}
	
	/**
	 * Get the data from a Node
	 * @return String Data
	 */
	public D getData(){
		return data;
	}
	
}
