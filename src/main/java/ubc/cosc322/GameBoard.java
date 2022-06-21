package ubc.cosc322;

import java.util.ArrayList;
import java.util.Collections;

class QueenMoves {
	final public static int NUM_ROW = 11;
	final public static int NUM_COL = 11;

	public static boolean isInValidRange(Position pos) {
		return 0 < pos.x && pos.x < NUM_ROW && 0 < pos.y && pos.y < NUM_COL;
	}

	private static ArrayList<Position> getRightMoves(GameBoard board, Position cur) {
		ArrayList<Position> res = new ArrayList<Position>();
		int i = 1;
		Position pos = new Position(cur.x + i, cur.y);
		while (isInValidRange(pos) && board.canMove(pos)) {
			res.add(pos.copy());
			pos.update(pos.x + i, pos.y);
		}
		return res;
	}

	private static ArrayList<Position> getLeftMoves(GameBoard board, Position cur) {
		ArrayList<Position> res = new ArrayList<Position>();
		int i = 1;
		Position pos = new Position(cur.x - i, cur.y);
		while (isInValidRange(pos) && board.canMove(pos)) {
			res.add(pos.copy());
			pos.update(pos.x - i, pos.y);
		}
		return res;
	}

	private static ArrayList<Position> getUpMoves(GameBoard board, Position cur) {
		ArrayList<Position> res = new ArrayList<Position>();
		int i = 1;
		Position pos = new Position(cur.x, cur.y + i);
		while (isInValidRange(pos) && board.canMove(pos)) {
			res.add(pos.copy());
			pos.update(pos.x, pos.y + i);
		}
		return res;
	}

	private static ArrayList<Position> getDownMoves(GameBoard board, Position cur) {
		ArrayList<Position> res = new ArrayList<Position>();
		int i = 1;
		Position pos = new Position(cur.x, cur.y - i);
		while (isInValidRange(pos) && board.canMove(pos)) {
			res.add(pos.copy());
			pos.update(pos.x, pos.y - i);
		}
		return res;
	}

	private static ArrayList<Position> getUpRightMoves(GameBoard board, Position cur) {
		ArrayList<Position> res = new ArrayList<Position>();
		int i = 1;
		Position pos = new Position(cur.x + i, cur.y + i);
		while (isInValidRange(pos) && board.canMove(pos)) {
			res.add(pos.copy());
			pos.update(pos.x + i, pos.y + i);
		}
		return res;
	}

	private static ArrayList<Position> getUpLeftMoves(GameBoard board, Position cur) {
		ArrayList<Position> res = new ArrayList<Position>();
		int i = 1;
		Position pos = new Position(cur.x - i, cur.y + i);
		while (isInValidRange(pos) && board.canMove(pos)) {
			res.add(pos.copy());
			pos.update(pos.x - i, pos.y + i);
		}
		return res;
	}

	private static ArrayList<Position> getDownRightMoves(GameBoard board, Position cur) {
		ArrayList<Position> res = new ArrayList<Position>();
		int i = 1;
		Position pos = new Position(cur.x + i, cur.y - i);
		while (isInValidRange(pos) && board.canMove(pos)) {
			res.add(pos.copy());
			pos.update(pos.x + i, pos.y - i);
		}
		return res;
	}

	private static ArrayList<Position> getDownLeftMoves(GameBoard board, Position cur) {
		ArrayList<Position> res = new ArrayList<Position>();
		int i = 1;
		Position pos = new Position(cur.x - i, cur.y - i);
		while (isInValidRange(pos) && board.canMove(pos)) {
			res.add(pos.copy());
			pos.update(pos.x - i, pos.y - i);
		}
		return res;
	}

	public static ArrayList<Position> getAllMoves(GameBoard board, Position cur) {
		ArrayList<Position> res = new ArrayList<Position>();
//		System.out.println("RIGHT");
		res.addAll(getRightMoves(board, cur));
//		System.out.println("LEFT");
		res.addAll(getLeftMoves(board, cur));
//		System.out.println("UP");
		res.addAll(getUpMoves(board, cur));
//		System.out.println("DOWN");
		res.addAll(getDownMoves(board, cur));
//		System.out.println("UP RIGHT");
		res.addAll(getUpRightMoves(board, cur));
//		System.out.println("UP LEFT");
		res.addAll(getUpLeftMoves(board, cur));
//		System.out.println("DOWN RIGHT");
		res.addAll(getDownRightMoves(board, cur));
//		System.out.println("DOWN LEFT");
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

	public boolean updateState(GameAction action, int playerColor) throws Exception {
		try {
			action.isValid(this, playerColor);
		} catch (NotFoundQueenException e) {
			System.out.println("[OPPONENT LOSE]: Invalid queen choose");
			return false;
		} catch (CannotMoveQueenException e) {
			System.out.println("[OPPONENT LOSE]: Invalid action");
			return false;
		}

//		update state
		int posCur = getStateIndex(action.cur);
		int posNext = getStateIndex(action.target);
		int posArrow = getStateIndex(action.arrow);

		this.stateArray.set(posNext, getStateAt(action.cur));
		this.stateArray.set(posCur, NO_QUEEN);
		this.stateArray.set(posArrow, ARROW);

		return true;
	}

	public ArrayList<Integer> getState() {
		return this.stateArray;
	}

	public int getStateAt(int i, int j) {
		return this.getStateAt(new Position(i, j));
	}
	
	public int getStateAt(int index) {
		return this.stateArray.get(index);
	}

	public int getStateAt(Position pos) {
		return this.stateArray.get(this.getStateIndex(pos));
	}

	private int getStateIndex(Position pos) {
		return (NUM_COL - pos.x) * NUM_ROW + pos.y;
	}

	public boolean canMove(Position pos) {
		try {
			return this.getStateAt(pos) == NO_QUEEN;
		} catch (Exception e) {
			return false;
		}
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
		@SuppressWarnings("unchecked")
		ArrayList<Integer> newStateArray = (ArrayList<Integer>) this.getState().clone();

		int temp = this.getStateAt(target);
		newStateArray.set(this.getStateIndex(target), this.getStateAt(cur));
		newStateArray.set(this.getStateIndex(cur), temp);
		return new GameBoard(newStateArray);
	}

	public GameBoard copyWithChangeMove(GameAction action, int playerColor) throws Exception {
		try {
			action.isValid(this, playerColor);
		} catch (NotFoundQueenException e) {
			System.out.println("[OPPONENT LOSE]: Invalid queen choose");
		} catch (CannotMoveQueenException e) {
			System.out.println("[OPPONENT LOSE]: Invalid action");
		}
		
		@SuppressWarnings("unchecked")
		ArrayList<Integer> newStateArray = (ArrayList<Integer>) this.getState().clone();

		int temp = this.getStateAt(action.target);
		newStateArray.set(this.getStateIndex(action.target), this.getStateAt(action.cur));
		newStateArray.set(this.getStateIndex(action.cur), temp);
		newStateArray.set(this.getStateIndex(action.arrow), ARROW);
		return new GameBoard(newStateArray);
	}

	public ArrayList<GameAction> listAllActions(int playerColor) {
		ArrayList<GameAction> res = new ArrayList<GameAction>();
//		int cnt = 0;
//		for (int i = 0; i < 11; ++i) {
//			for (int j = 0; j < 11; ++j) {
//				System.out.print(this.stateArray.get(cnt++) + " ");
//			}
//			System.out.println();
//		}
		int i = NUM_COL, j = 0;
		for (int index = 0; index < this.getState().size(); ++index) {
			i = NUM_COL - index / NUM_ROW;
			j = index % NUM_ROW;
			int currentColor = this.getStateAt(index);

			if (currentColor == playerColor) {
//				System.out.println("Found queen at " + i + ", " + j);
				res.addAll(listAllActionsOfAQueen(new Position(i, j)));
			}
		}

		return res;
	}

	private ArrayList<GameAction> listAllActionsOfAQueen(Position queen) {
		ArrayList<GameAction> res = new ArrayList<GameAction>();
		ArrayList<Position> allTargets = new QueenMoves().getAllMoves(this, queen);
		for (Position target : allTargets) {
			if (target.equals(queen))
				continue;
			res.addAll(listAllArrowsOfAQueen(queen, target));
		}
		return res;
	}

	private ArrayList<GameAction> listAllArrowsOfAQueen(Position queen, Position target) {
		ArrayList<GameAction> res = new ArrayList<GameAction>();
		GameBoard newBoard = this.copyWithChange(queen, target);
		ArrayList<Position> allMoves = new QueenMoves().getAllMoves(newBoard, target);
		for (Position arrow : allMoves) {
			if (target.equals(arrow))
				continue;
			GameAction newAction = new GameAction(queen, target, arrow);
			res.add(newAction);
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
