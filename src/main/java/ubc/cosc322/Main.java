package ubc.cosc322;

import ygraph.ai.smartfox.games.BaseGameGUI;

public class Main {

	private static void go(BasePlayer player) {
		if (player.getGameGUI() == null) {
			player.Go();
		} else {
			BaseGameGUI.sys_setup();
			java.awt.EventQueue.invokeLater(new Runnable() {
				public void run() {
					player.Go();
				}
			});
		}
	}

	public static void main(String[] args) throws InterruptedException {
//		BasePlayer player1 = new BasePlayer\(args[0], args[1]);
//		RandomPlayer player1 = new RandomPlayer();
//		MinimaxPlayer player1 = new MinimaxPlayer("real_black", "abcxyz");
//		MinimaxPlayer player2 = new MinimaxPlayer("black", "");

//		go(new RandomPlayer());
//		Thread.sleep(100);
		go(new MinimaxPlayer("group 9", "group 9"));
	}
}
