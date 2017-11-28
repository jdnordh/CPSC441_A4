package router;

import java.util.ArrayList;

public class DiGraph {
	
	private Vertex [] vertices;
	private ArrayList<Edge> edges;
	private int [] [] adjMatrix;
	
	/** Default constructor */
	public DiGraph(){
		vertices = null;
		edges = new ArrayList<Edge>();
		adjMatrix = null;
	}
	/** Construct a graph with v verices */
	public DiGraph(int v){
		vertices = new Vertex[v];
		for (int i = 0; i < v; i++){
			vertices[i] = new Vertex(i);
		}
		edges = new ArrayList<Edge>();
		adjMatrix = null;
	}

	public void addEdge(int start, int end){
		vertices[start].addNeightbour(vertices[end]);
	}
	
	/** adds edges based on the adjacency matrix*/
	private void updateEdges(){
		for (int i = 0; i < adjMatrix.length; i++){
			for (int j = 0; j < adjMatrix.length; j++){
				if (adjMatrix[i][j] == 1) {
					this.addEdge(i, j);
				}
			}
		}
	}
	
	// getters and setters
	public Vertex [] getVertices() {
		return vertices;
	}
	public void setVertices(Vertex [] vertices) {
		this.vertices = vertices;
	}
	public ArrayList<Edge> getEdges() {
		return edges;
	}
	public void setEdges(ArrayList<Edge> edges) {
		this.edges = edges;
	}
	public int [] [] getAdjMatrix() {
		return adjMatrix;
	}
	public void setAdjMatrix(int [] [] adjMatrix) {
		this.adjMatrix = adjMatrix;
		this.updateEdges();
	}
}
