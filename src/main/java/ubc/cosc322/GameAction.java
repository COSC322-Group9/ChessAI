package ubc.cosc322;

import java.util.ArrayList;

import ygraph.ai.smartfox.games.GamePlayer;

class QueenMoves {
	final private int NUM_ROW = 11;
	final private int NUM_COL = 11;

	private boolean isInValidRange(Position pos) {
		return 0 < pos.x && pos.x < NUM_ROW && 0 < pos.y && pos.y < NUM_COL;
	}

	private ArrayList<Position> getRightMoves(GameBoard board, Position cur) {
		ArrayList<Position> res = new ArrayList<Position>();
		int i = 1;
		Position pos = new Position(cur.x + i, cur.y);
		while (isInValidRange(pos) && board.canMove(pos)) {
			res.add(pos);
			++i;
			pos.update(pos.x + i, pos.y);
		}
		return res;
	}

	private ArrayList<Position> getLeftMoves(GameBoard board, Position cur) {
		ArrayList<Position> res = new ArrayList<Position>();
		int i = 1;
		Position pos = new Position(cur.x - i, cur.y);
		while (isInValidRange(pos) && board.canMove(pos)) {
			res.add(pos);
			++i;
			pos.update(pos.x - i, pos.y);
		}
		return res;
	}

	private ArrayList<Position> getUpMoves(GameBoard board, Position cur) {
		ArrayList<Position> res = new ArrayList<Position>();
		int i = 1;
		Position pos = new Position(cur.x, cur.y + i);
		while (isInValidRange(pos) && board.canMove(pos)) {
			res.add(pos);
			++i;
			pos.update(pos.x, pos.y + i);
		}
		return res;
	}

	private ArrayList<Position> getDownMoves(GameBoard board, Position cur) {
		ArrayList<Position> res = new ArrayList<Position>();
		int i = 1;
		Position pos = new Position(cur.x, cur.y - i);
		while (isInValidRange(pos) && board.canMove(pos)) {
			res.add(pos);
			++i;
			pos.update(pos.x, pos.y - i);
		}
		return res;
	}

	private ArrayList<Position> getUpRightMoves(GameBoard board, Position cur) {
		ArrayList<Position> res = new ArrayList<Position>();
		int i = 1;
		Position pos = new Position(cur.x + i, cur.y + i);
		while (isInValidRange(pos) && board.canMove(pos)) {
			res.add(pos);
			++i;
			pos.update(pos.x + i, pos.y + i);
		}
		return res;
	}

	private ArrayList<Position> getUpLeftMoves(GameBoard board, Position cur) {
		ArrayList<Position> res = new ArrayList<Position>();
		int i = 1;
		Position pos = new Position(cur.x - i, cur.y + i);
		while (isInValidRange(pos) && board.canMove(pos)) {
			res.add(pos);
			++i;
			pos.update(pos.x - i, pos.y + i);
		}
		return res;
	}

	private ArrayList<Position> getDownRightMoves(GameBoard board, Position cur) {
		ArrayList<Position> res = new ArrayList<Position>();
		int i = 1;
		Position pos = new Position(cur.x + i, cur.y - i);
		while (isInValidRange(pos) && board.canMove(pos)) {
			res.add(pos);
			++i;
			pos.update(pos.x + i, pos.y - i);
		}
		return res;
	}

	private ArrayList<Position> getDownLeftMoves(GameBoard board, Position cur) {
		ArrayList<Position> res = new ArrayList<Position>();
		int i = 1;
		Position pos = new Position(cur.x - i, cur.y - i);
		while (isInValidRange(pos) && board.canMove(pos)) {
			res.add(pos);
			++i;
			pos.update(pos.x - i, pos.y - i);
		}
		return res;
	}

	public ArrayList<Position> getAllMoves(GameBoard board, Position cur) {
		ArrayList<Position> res = new ArrayList<Position>();
		res.addAll(getRightMoves(board, cur));
		res.addAll(getLeftMoves(board, cur));
		res.addAll(getUpMoves(board, cur));
		res.addAll(getDownMoves(board, cur));
		res.addAll(getUpRightMoves(board, cur));
		res.addAll(getUpLeftMoves(board, cur));
		res.addAll(getDownRightMoves(board, cur));
		res.addAll(getDownLeftMoves(board, cur));
		return res;
	}
}

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
		if (cur.equals(target)) return false;
		
		int dX = target.x - cur.y;
		int dY = target.y - cur.y;
		int u = dX == 0 ? 0 : (dX > 0 ? 1 : -1);
		int v = dY == 0 ? 0 : (dY > 0 ? 1 : -1);

		do {
			cur.update(cur.x + u, cur.y + v);
			if (!board.canMove(cur)) return false;
		} while (!cur.equals(target));

		return true;
	}

	public boolean isValid(GameBoard board, BasePlayer player) throws Exception {
//		Check current position has queen
		if (board.getStateAt(this.cur) == 0 || board.getStateAt(this.cur) != player.getColor()) {
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

	public ArrayList<GameAction> listAllActions(GameBoard board, int playerColor) {
		ArrayList<GameAction> res = new ArrayList<GameAction>();
		int i = 0, j = 0;
		for (int val : board.getState()) {
			if (val == playerColor) {
				res.addAll(listAllActionsOfAQueen(board, new Position(i, j)));
			}
			i += (j + 1) % board.NUM_ROW; // go up every NUM_ROW times
			j = (j + 1) / board.NUM_ROW; // always 0 < j < NUM_ROW
		}

		return new ArrayList<GameAction>();
	}

	private ArrayList<GameAction> listAllActionsOfAQueen(GameBoard board, Position queen) {
		ArrayList<GameAction> res = new ArrayList<GameAction>();
		ArrayList<Position> allTargets = new QueenMoves().getAllMoves(board, queen);
		for (Position target : allTargets) {
			res.addAll(listAllArrowsOfAQueen(board, queen, target));
		}
		return res;
	}

	private ArrayList<GameAction> listAllArrowsOfAQueen(GameBoard board, Position queen, Position target) {
		ArrayList<GameAction> res = new ArrayList<GameAction>();
		GameBoard newBoard = board.copyWithChange(queen, target);
		ArrayList<Position> allMoves = new QueenMoves().getAllMoves(newBoard, target);
		for (Position arrow: allMoves) {
			res.add(new GameAction(queen, target, arrow));
		}
		return res;
	}
}


