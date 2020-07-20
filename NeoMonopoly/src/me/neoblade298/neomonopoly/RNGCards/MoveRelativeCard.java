package me.neoblade298.neomonopoly.RNGCards;

import org.bukkit.scheduler.BukkitRunnable;

import me.neoblade298.neomonopoly.Objects.Game;
import me.neoblade298.neomonopoly.Objects.GamePlayer;

public class MoveRelativeCard extends RNGCard {
	private Game game;
	private int move;
	
	public MoveRelativeCard(Game game, String name, int move) {
		super(game, name);
		this.game = game;
		this.move = move;
	}
	
	@Override
	public void onDraw(GamePlayer gp, String src) {
		super.onDraw(gp, src);
		new BukkitRunnable() { public void run() {
			game.movePlayer(gp, move, true, true);
			game.isBusy = false;
		}}.runTaskLater(game.main, 40L);
	}
}
