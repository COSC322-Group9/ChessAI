package ubc.cosc322;

public class Position {
	int x;
	int y;

	public Position(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public void update(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public boolean equals(Position other) {
		return this.x == other.x && this.y == other.y;
	}
}
