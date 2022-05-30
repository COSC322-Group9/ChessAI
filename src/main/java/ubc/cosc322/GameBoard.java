package ubc.cosc322;

import java.util.ArrayList;
import java.util.Collections;

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

public class GameBoard {

	final public int NUM_ROW = 11;
	final public int NUM_COL = 11;
	final public int ARRAY_LENGTH = NUM_ROW * NUM_COL;
	final public int NO_QUEEN = 0;
	final public int ARROW = 3;

	private ArrayList<Integer> stateArray = null;

	public GameBoard(ArrayList<Integer> stateArray) {
		this.stateArray = stateArray;
	}

	public void updateState(GameAction action, int playerColor) throws Exception {
//		check for exceptions
		try {
			action.isValid(this, playerColor);
		} catch (NotFoundQueenException e) {
			System.out.println("[OPPONENT LOSE]: Invalid queen choose");
		} catch (CannotMoveQueenException e) {
			System.out.println("[OPPONENT LOSE]: Invalid action");
		}

//		update state
		int posCur = getStateIndex(action.cur);
		int posNext = getStateIndex(action.target);
		int posArrow = getStateIndex(action.arrow);

		this.stateArray.set(posNext, getStateAt(action.cur));
		this.stateArray.set(posCur, NO_QUEEN);
		this.stateArray.set(posArrow, ARROW);
	}

	public ArrayList<Integer> getState() {
		return this.stateArray;
	}

	public int getStateAt(Position pos) {
		return this.stateArray.get(this.getStateIndex(pos));
	}

	private int getStateIndex(Position pos) {
		return (NUM_COL - pos.x) * NUM_ROW + pos.y;
	}

	public boolean canMove(Position pos) {
		return this.getStateAt(pos) == NO_QUEEN;
	}

	@Override
	public String toString() {
		String str = "";
		for (int i = 0; i < NUM_COL; ++i) {
			for (int j = 0; j < NUM_ROW; ++j) {
				str += Integer.toString(this.stateArray.get(NUM_COL * i + j));
			}
		}
		return str;
	}

	public GameBoard copyWithChange(Position cur, Position target) {
		ArrayList<Integer> newStateArray = this.getState();
		Collections.swap(newStateArray, this.getStateIndex(cur), this.getStateIndex(target));
		return new GameBoard(newStateArray);
	}

	public ArrayList<GameAction> listAllActions(int playerColor) {
		ArrayList<GameAction> res = new ArrayList<GameAction>();
		int i = 0, j = 0;
		for (int val : this.getState()) {
			if (val == playerColor) {
				res.addAll(listAllActionsOfAQueen(new Position(i, j)));
			}
			i += (j + 1) % NUM_ROW; // go up every NUM_ROW times
			j = (j + 1) / NUM_ROW; // always 0 < j < NUM_ROW
		}

		return new ArrayList<GameAction>();
	}

	private ArrayList<GameAction> listAllActionsOfAQueen(Position queen) {
		ArrayList<GameAction> res = new ArrayList<GameAction>();
		ArrayList<Position> allTargets = new QueenMoves().getAllMoves(this, queen);
		for (Position target : allTargets) {
			res.addAll(listAllArrowsOfAQueen(queen, target));
		}
		return res;
	}

	private ArrayList<GameAction> listAllArrowsOfAQueen(Position queen, Position target) {
		ArrayList<GameAction> res = new ArrayList<GameAction>();
		GameBoard newBoard = this.copyWithChange(queen, target);
		ArrayList<Position> allMoves = new QueenMoves().getAllMoves(newBoard, target);
		for (Position arrow : allMoves) {
			res.add(new GameAction(queen, target, arrow));
		}
		return res;
	}
}

@SuppressWarnings("serial")
class NotFoundQueenException extends Exception {
	public NotFoundQueenException() {
		super("Unable to find a queen at this position");
	}

	public NotFoundQueenException(String msg) {
		super(msg);
	}
}

@SuppressWarnings("serial")
class CannotMoveQueenException extends Exception {
	public CannotMoveQueenException() {
		super("There's a problem when trying to move the queen or throw the arrow");
	}

	public CannotMoveQueenException(String msg) {
		super(msg);
	}
}
