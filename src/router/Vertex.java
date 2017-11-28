package router;

import java.util.ArrayList;

public class Vertex{
	
	private ArrayList<Vertex> neighbours;
	private int index;
	
	/** Construct a new vertex with index d */
	public Vertex(int d){
		index = d;
		neighbours = new ArrayList<Vertex>();
	}
	
	/** Add a new neightbour */
	public void addNeightbour(Vertex v){
		neighbours.add(v);
	}
	
	/** remove a neighbouring vertex from adjacency list */
	public void removeNeighbour(Vertex v){
		neighbours.remove(v);
	}
	
	// Getters and Setters
	public ArrayList<Vertex> getNeighbours() {
		return neighbours;
	}

	public void setNeighbours(ArrayList<Vertex> neighbours) {
		this.neighbours = neighbours;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}
}
