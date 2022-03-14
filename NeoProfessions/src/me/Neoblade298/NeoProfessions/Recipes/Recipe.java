package me.Neoblade298.NeoProfessions.Recipes;

import java.util.ArrayList;

import org.bukkit.Sound;
import org.bukkit.entity.Player;

import me.Neoblade298.NeoProfessions.PlayerProfessions.ProfessionManager;
import me.Neoblade298.NeoProfessions.Storage.StorageManager;
import me.Neoblade298.NeoProfessions.Storage.StoredItemInstance;

public class Recipe {
	String key;
	String display;
	int exp;
	ArrayList<RecipeRequirement> reqs;
	ArrayList<StoredItemInstance> components;
	RecipeResult result;
	
	
	public Recipe(String key, String display, int exp, ArrayList<RecipeRequirement> reqs, ArrayList<StoredItemInstance> components) {
		this.key = key;
		this.display = display;
		this.exp = exp;
		this.reqs = reqs;
		this.components = components;
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
			if (!StorageManager.playerHas(p, component.getItem().getID(), component.getAmount())) {
				p.sendMessage("§4[§c§lMLMC§4] §cYou lack the component: " + component.getItem().getDisplay());
				return false;
			}
		}
		
		// Take all things required
		for (RecipeRequirement req : reqs) {
			req.useReq(p);
		}
		for (StoredItemInstance component : components) {
			StorageManager.takePlayer(p, component.getItem().getID(), component.getAmount());
		}
		
		result.giveResult(p);
		ProfessionManager.getAccount(p.getUniqueId()).get("crafting").addExp(p, exp);
		p.playSound(p.getLocation(), Sound.BLOCK_ANVIL_USE, 1.0F, 1.0F);
		p.sendMessage("§4[§c§lMLMC§4] §7You successfully crafted: " + display);
		return true;
	}
}
