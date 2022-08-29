package me.neoblade298.neocore.bar;

import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import me.neoblade298.neocore.NeoCore;
import me.neoblade298.neocore.player.PlayerTags;

public class CoreBar {
	private BossBar bar;
	private String topic = "";
	private boolean enabled;
	
	public CoreBar(Player p, BossBar bar, PlayerTags ptags) {
		this.bar = bar;
		bar.setVisible(false);
		bar.addPlayer(p);
		enabled = !ptags.exists("disable-corebar", p.getUniqueId());
	}
	
	public void setTitle(String title) {
		if (!enabled) return;
		if (!bar.isVisible()) bar.setVisible(true);
		bar.setTitle(title);
	}
	
	public void setColor(BarColor color) {
		bar.setColor(color);
	}
	
	public void setProgress(double progress) {
		bar.setProgress(progress);
		if (progress == 1) {
			new BukkitRunnable() {
				public void run() {
					if (progress == 1) {
						setVisible(false);
					}
				}
			}.runTaskLaterAsynchronously(NeoCore.inst(), 20L);
		}
	}
	
	public void setVisible(boolean visible) {
		if (!enabled && visible) return; // Don't make bar visible if not enabled
		bar.setVisible(visible);
	}
	
	public void setStyle(BarStyle style) {
		bar.setStyle(style);
	}
	
	public BarColor getColor() {
		return bar.getColor();
	}
	
	public BarStyle getStyle() {
		return bar.getStyle();
	}
	
	public double getProgress() {
		return bar.getProgress();
	}
	
	public boolean isVisible() {
		return bar.isVisible();
	}
	
	public String getTitle() {
		return bar.getTitle();
	}
	
	public String getTopic() {
		return topic;
	}
	
	public void setTopic(String topic) {
		this.topic = topic;
	}
	
	public boolean toggleEnabled() {
		setEnabled(!enabled);
		return enabled;
	}
	
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
		if (!enabled && bar.isVisible()) {
			bar.setVisible(false);
			topic = "";
		}
	}
}
