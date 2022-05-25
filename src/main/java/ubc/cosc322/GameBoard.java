package ubc.cosc322;

import java.util.ArrayList;
import java.util.Collections;

import ygraph.ai.smartfox.games.GamePlayer;

public class GameBoard {

	final public int NUM_ROW = 11;
	final public int NUM_COL = 11;
	final public int NO_QUEEN = 0;
	final public int ARROW = 3;

	private ArrayList<Integer> stateArray = null;

	public GameBoard(ArrayList<Integer> stateArray) {
		this.stateArray = stateArray;
	}

	public void updateState(GameAction action, BasePlayer player) throws Exception {
//		check for exceptions
		try {
			action.isValid(this, player);
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
		System.out.println(pos.x + ' ' + pos.y + (pos.x * NUM_ROW + pos.y)); // TODO: Remove after debug
		return pos.x * NUM_ROW + pos.y;
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
