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

	private boolean isTraversable(GameBoard board, Position cur, Position target) {
		if (cur.equals(target))
			return false;

		int dX = target.x - cur.y;
		int dY = target.y - cur.y;
		int u = dX == 0 ? 0 : (dX > 0 ? 1 : -1);
		int v = dY == 0 ? 0 : (dY > 0 ? 1 : -1);

		do {
			cur.update(cur.x + u, cur.y + v);
			if (!board.canMove(cur))
				return false;
		} while (!cur.equals(target));

		return true;
	}

	public boolean isValid(GameBoard board, int playerColor) throws Exception {
//		Check current position has queen
		if (board.getStateAt(this.cur) == 0 || board.getStateAt(this.cur) != playerColor) {
			throw new NotFoundQueenException();
		}
//		Check if target position movable
		if (!isTraversable(board, this.cur, this.target)) {
			throw new CannotMoveQueenException();
		}
//		Check if arrow is being thrown at the target
		if (this.arrow == this.target) {
			throw new CannotMoveQueenException();
		}
//		Check if arrow is movable
		if (!isTraversable(board, this.target, this.arrow) || this.arrow != this.cur) {
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
}
