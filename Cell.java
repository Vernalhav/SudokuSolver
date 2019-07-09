
public class Cell {
	private Pair<Integer, Integer> coord;
	private int value;
	
	public Cell(Pair<Integer, Integer> coord, int value) {
		this.coord = coord;
		this.value = value;
	}

	public Pair<Integer, Integer> getCoord() {
		return coord;
	}

	public void setCoord(Pair<Integer, Integer> coord) {
		this.coord = coord;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}
	
	public boolean equals(Cell b) {
		return b.coord.equals(this.coord);
	}
}
