package me.neoblade298.neomythicextension.objects;

import org.bukkit.Material;
import org.bukkit.entity.Player;

public class SpawnerDefinition {
	private String mob;
	private String name;
	private String group;
	private Material block;
	private int iterator;
	
	public SpawnerDefinition() {
		iterator = 1;
	}
	
	public void display(Player p, int count) {
		p.sendMessage("§e" + count + " " + block + "§7: §c" + mob + ", §7spawner §c" + name + "§7, group §c" + group + "§7.");
	}
	
	public String getMob() {
		return mob;
	}
	public void setMob(String mob) {
		this.mob = mob;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getGroup() {
		return group;
	}
	public void setGroup(String group) {
		this.group = group;
	}
	public Material getBlock() {
		return block;
	}
	public void setBlock(Material block) {
		this.block = block;
	}
	public int getIterator() {
		return iterator;
	}
	public void iterate() {
		this.iterator++;
	}
}
