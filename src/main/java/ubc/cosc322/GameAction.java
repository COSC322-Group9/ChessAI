package ubc.cosc322;

import java.util.ArrayList;

public class GameAction {
	public Position cur = null;
	public Position target = null;
	public Position arrow = null;

	public GameAction(Position cur, Position next, Position arrow) {
		this.cur = cur;
		this.target = next;
		this.arrow = arrow;
	}

	private boolean isTraversable(GameBoard board, Position _cur, Position target) {
		if (_cur.equals(target)) return false;
		

		Position cur = _cur.copy();
		if (cur.equals(target))
			return false;

		int dX = target.x - cur.x;
		int dY = target.y - cur.y;
		int u = dX == 0 ? 0 : (dX > 0 ? 1 : -1);
		int v = dY == 0 ? 0 : (dY > 0 ? 1 : -1);

		while (!cur.equals(target)) {
//			System.out.print(_cur + ": ");
//			System.out.println(cur + "->" + target);
			cur.update(cur.x + u, cur.y + v);
			if (!board.canMove(cur) && !cur.equals(target)) return false;
		}
		return true;
	}

	public boolean isValid(GameBoard board, int playerColor) throws Exception {
//		Check current position has queen
		if (board.getStateAt(this.cur) != playerColor) {
			System.out.println("check not same color. found " + board.getStateAt(this.cur) + " instead of " + playerColor);
			throw new NotFoundQueenException();
		}
//		Check if target position movable
		if (!isTraversable(board, this.cur, this.target)) {
			throw new CannotMoveQueenException();
		}
//		Check if arrow is movable
		GameBoard newBoard = board.copyWithChange(this.cur, this.target);
		if (!isTraversable(newBoard, this.target, this.arrow)) {
			throw new CannotMoveQueenException();
		}
		return true;
	}

	public ArrayList<Integer> getCurrentQueen() {
		return this.cur.toArrayList();
	}

	public ArrayList<Integer> getTarget() {
		return this.target.toArrayList();
	}

	public ArrayList<Integer> getArrow() {
		return this.arrow.toArrayList();
	}
	
	@Override
	public String toString() {
		return "[" + this.cur + ", " + this.target + ", " + this.arrow + "]";
	}
}
