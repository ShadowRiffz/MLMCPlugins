package me.neoblade298.neomythicextension.objects;

import java.util.ArrayList;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import io.lumine.mythic.bukkit.MythicBukkit;
import io.lumine.mythic.core.spawning.spawners.SpawnerManager;

public class SpawnerMaker {
	private Location loc1;
	private Location loc2;
	private ArrayList<SpawnerDefinition> definitions;
	
	public SpawnerMaker() {
		definitions = new ArrayList<SpawnerDefinition>();
	}
	
	public void display(Player p) {
		p.sendMessage("§8===[§4§lSpawner Maker§8]===");
		if (loc1 != null) {
			p.sendMessage("§7Location 1: " + loc1.getBlockX() + " " + loc1.getBlockY() + " " + loc1.getBlockZ());
		}
		else {
			p.sendMessage("§7Location 1: Not set");
		}

		if (loc2 != null) {
			p.sendMessage("§7Location 2: " + loc2.getBlockX() + " " + loc2.getBlockY() + " " + loc2.getBlockZ());
		}
		else {
			p.sendMessage("§7Location 2: Not set");
		}
		
		if (!definitions.isEmpty()) { 
			p.sendMessage("§7Spawner Definitions:");
			int count = 0;
			for (SpawnerDefinition def : definitions) {
				def.display(p, count);
				count++;
			}
		}
		else {
			p.sendMessage("§7Spawner Definitions: Not set");
		}
	}
	
	public boolean generate(Player p) {
		if (loc1 == null) {
			p.sendMessage("§4[§c§lMLMC§4] §cLocation 1 was not properly set!");
			return false;
		}
		else if (loc2 == null) {
			p.sendMessage("§4[§c§lMLMC§4] §cLocation 2 was not properly set!");
			return false;
		}
		else if (definitions.isEmpty()) {
			p.sendMessage("§4[§c§lMLMC§4] §cNo spawner definitions were added!");
			return false;
		}
		
		// Start generation process, loc 1 is ALWAYS less than loc 2
		p.sendMessage("§4[§c§lMLMC§4] §7Commencing spawner generation!");
		Location loc = loc1.clone();
		SpawnerManager mm = MythicBukkit.inst().getSpawnerManager();
		for (int x = loc1.getBlockX(); x <= loc2.getBlockX(); x++) {
			loc.setY(loc1.getBlockY());
			for (int y = loc1.getBlockY(); y <= loc2.getBlockY(); y++) {
				loc.setZ(loc1.getBlockZ());
				for (int z = loc1.getBlockZ(); z <= loc2.getBlockZ(); z++) {
					Block block = loc.getBlock();
					for (SpawnerDefinition def : definitions) {
						if (def.getBlock().equals(block.getType())) {
							Location spawnLoc = loc.clone().subtract(0, 1, 0);
							String spawnerName = def.getName() + def.getIterator();
							mm.createSpawner(spawnerName, spawnLoc, def.getMob());
							mm.getSpawnerByName(spawnerName).setGroup(def.getGroup());
							def.iterate();
							block.setType(Material.AIR);
						}
					}
					loc.add(0, 0, 1);
				}
				loc.add(0, 1, 0);
			}
			loc.add(1, 0, 0);
		}
		p.sendMessage("§4[§c§lMLMC§4] §7Spawner generation complete!");
		return true;
	}
	
	public Location getLoc1() {
		return loc1;
	}
	
	public void setLoc1(Location loc1) {
		this.loc1 = loc1;
	}
	
	public Location getLoc2() {
		return loc2;
	}
	
	public void setLoc2(Location loc2) {
		this.loc2 = loc2;
	}
	
	public ArrayList<SpawnerDefinition> getDefinitions() {
		return definitions;
	}
	
	public void setDefinitions(ArrayList<SpawnerDefinition> definitions) {
		this.definitions = definitions;
	}
}
