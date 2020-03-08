package me.neoblade298.neoplaceholders;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import be.maximvdw.placeholderapi.PlaceholderAPI;
import be.maximvdw.placeholderapi.PlaceholderReplaceEvent;
import be.maximvdw.placeholderapi.PlaceholderReplacer;

public class NeoprofessionsPlaceholders {
	private Main main;
	private me.Neoblade298.NeoProfessions.Main plugin;
	private String[] types = {"essence", "ruby", "amethyst", "sapphire", "emerald", "topaz", "garnet", "adamantium"};
	
	public NeoprofessionsPlaceholders (Main main) {
		this.main = main;
		plugin = (me.Neoblade298.NeoProfessions.Main) Bukkit.getPluginManager().getPlugin("Neoprofessions");
	}

	public void registerPlaceholders() {

		for (int i = 5; i <= 60; i += 5) {
			final int j = i;
			for (String type : types) {
				PlaceholderAPI.registerPlaceholder(this.main, "professions_" + type + "_" + i, new PlaceholderReplacer() {
					@Override
					public String onPlaceholderReplace(PlaceholderReplaceEvent e) {
						boolean online = e.isOnline();
						Player p = e.getPlayer();
						String placeholder = "Loading...";
		
						if (online && p != null && plugin.cManager.containsPlayer(p)) {
							return "" + plugin.cManager.get(p, type, j);
						}
						return placeholder;
					}
				});
			}
		}
	}

}
