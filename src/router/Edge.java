package router;

public class Edge implements Comparable<Edge> {
	
	private Vertex start, end;
	private int weight;
	
	
	public Edge(){
		start = null;
		end = null;
		weight = 0;
	}
	
	
	
	
	
	
	
	
	@Override
	public int compareTo(Edge e) {
		return this.getWeight() - e.getWeight();
	}

	//Getters and setters
	public Vertex getStart() {
		return start;
	}

	public void setStart(Vertex start) {
		this.start = start;
	}

	public Vertex getEnd() {
		return end;
	}

	public void setEnd(Vertex end) {
		this.end = end;
	}

	public int getWeight() {
		return weight;
	}

	public void setWeight(int weight) {
		this.weight = weight;
	}
}
