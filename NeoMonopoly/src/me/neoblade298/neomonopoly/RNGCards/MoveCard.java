package me.neoblade298.neomonopoly.RNGCards;

import org.bukkit.scheduler.BukkitRunnable;

import me.neoblade298.neomonopoly.Objects.Game;
import me.neoblade298.neomonopoly.Objects.GamePlayer;

public class MoveCard extends RNGCard{
	private Game game;
	private int position;
	
	public MoveCard(Game game, String name, int position) {
		super(game, name);
		this.game = game;
		this.position = position;
	}
	
	public void onDraw(GamePlayer gp, String src) {
		super.onDraw(gp, src);
		new BukkitRunnable() { public void run() {
			game.movePlayerAbsolute(gp, position, true, true);
			game.isBusy = false;
		}}.runTaskLater(game.main, 40L);
	}
}
