package me.Neoblade298.NeoProfessions.Recipes;

import java.util.ArrayList;

import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.Neoblade298.NeoProfessions.PlayerProfessions.ProfessionManager;
import me.Neoblade298.NeoProfessions.Storage.StorageManager;
import me.Neoblade298.NeoProfessions.Storage.StoredItemInstance;
import net.md_5.bungee.api.ChatColor;

public class Recipe {
	String key;
	String display;
	int exp;
	ArrayList<RecipeRequirement> reqs;
	ArrayList<StoredItemInstance> components;
	RecipeResult result;
	boolean canMulticraft;
	
	
	public Recipe(String key, String display, int exp, ArrayList<RecipeRequirement> reqs, ArrayList<StoredItemInstance> components, RecipeResult result, boolean canMulticraft) {
		this.key = key;
		this.display = display;
		this.exp = exp;
		this.reqs = reqs;
		this.components = components;
		this.result = result;
		this.canMulticraft = canMulticraft;
	}
	
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public ArrayList<RecipeRequirement> getReqs() {
		return reqs;
	}
	public void setReqs(ArrayList<RecipeRequirement> reqs) {
		this.reqs = reqs;
	}
	public ArrayList<StoredItemInstance> getComponents() {
		return components;
	}
	public void setComponents(ArrayList<StoredItemInstance> components) {
		this.components = components;
	}
	
	public boolean craftRecipe(Player p) {
		// First make sure everything required is available
		for (RecipeRequirement req : reqs) {
			if (!req.passesReq(p)) {
				p.sendMessage(req.failMessage(p));
				return false;
			}
		}
		for (StoredItemInstance component : components) {
			if (!StorageManager.playerHas(p, component.getItem().getId(), component.getAmount())) {
				p.sendMessage("§4[§c§lMLMC§4] §cYou lack the component: " + component.getItem().getDisplay());
				return false;
			}
		}
		
		// Take all things required
		for (RecipeRequirement req : reqs) {
			req.useReq(p);
		}
		for (StoredItemInstance component : components) {
			StorageManager.takePlayer(p, component.getItem().getId(), component.getAmount());
		}
		
		result.giveResult(p);
		ProfessionManager.getAccount(p.getUniqueId()).get("crafter").addExp(p, exp);
		p.playSound(p.getLocation(), Sound.BLOCK_ANVIL_USE, 1.0F, 1.0F);
		p.sendMessage("§4[§c§lMLMC§4] §7You successfully crafted: " + display);
		return true;
	}
	
	public ItemStack getReqsIcon(Player p) {
		ItemStack item = new ItemStack(result.getResultItem(p).getType());
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(result.getResultItem(p).getItemMeta().getDisplayName());
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("§6Requirements§7:");
		for (RecipeRequirement req : reqs) {
			lore.add(req.getLoreString(p));
		}
		lore.add("§6Components§7:");
		for (StoredItemInstance component : components) {
			String line = StorageManager.playerHas(p, component.getItem().getId(), component.getAmount()) ? "§a" : "§c";
			line += "- " + component.getAmount() + "x " + ChatColor.stripColor(component.getItem().getDisplay());
			lore.add(line);
		}
		lore.add("§7§m---");
		lore.add("§9§oPress 1 §7§ofor result view");
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}
	
	public RecipeResult getResult() {
		return result;
	}
	
	public boolean canMulticraft() {
		return canMulticraft;
	}
}
