import java.util.HashMap;

public class SudokuGraph {
	
	HashMap<Cell, Vertex> edges;
	
	public SudokuGraph() {
		edges = new HashMap<Cell, Vertex>();
	}
	
	// TODO
	public SudokuGraph(int[][] values) {
		edges = new HashMap<Cell, Vertex>();
	}
	
	public void addEdge(Cell u, Cell v) {
		if (hasEdge(u, v)) return;
		
		if (edges.get(u) == null) addVertex(u);
		if (edges.get(v) == null) addVertex(v);
		
		edges.get(u).addAdjacency(edges.get(v));
	}
	
	public void addVertex(Cell u) {
		Vertex newV = new Vertex(u);
		edges.put(u, newV);
	}
	
	public boolean hasEdge(Cell u, Cell v) {
		Vertex current = edges.get(u);
		
		if (current == null) return false;
		return current.isAdjacent(edges.get(v));
	}
}