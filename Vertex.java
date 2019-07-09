import java.util.ArrayList;

public class Vertex {
	
	private ArrayList<Vertex> adj;
	private Cell item;
	
	public Vertex(Cell item) {
		this.adj = new ArrayList<>();
		this.item = item;
	}
	
	public Vertex(int x, int y, int value) {
		this.adj = new ArrayList<>();
		this.item = new Cell(new Pair<Integer, Integer>(x, y), value);
	}
	
	/**
	 * Adds newAdj to the adjacencies
	 * of the current vertex
	 * (unidirectional)
	 * @param newAdj
	 */
	public void addAdjacency(Vertex newAdj) {
		if (isAdjacent(newAdj)) return;
		adj.add(newAdj);
	}
	
	public boolean isAdjacent(Vertex u) {
		for (Vertex v : adj)
			if (v.equals(u)) return true;
		return false;
	}
	
	public boolean equals(Vertex v) {
		return item.equals(v.item);
	}
	
}
