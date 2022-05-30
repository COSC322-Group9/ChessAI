package ubc.cosc322;

import java.util.ArrayList;
import java.util.Arrays;

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

	@Override
	public String toString() {
		return "[" + this.x + ", " + this.y + "]";
	}
	
	public ArrayList<Integer> toArrayList() {
		return new ArrayList<Integer>(Arrays.asList(x, y));
	}
}
