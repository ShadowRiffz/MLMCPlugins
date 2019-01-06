package me.Neoblade298.NeoProfessions.Listeners;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.CraftingInventory;
import org.bukkit.inventory.ItemStack;

import me.Neoblade298.NeoProfessions.Main;

public class CulinarianListeners implements Listener {
	
	Main main;
	public CulinarianListeners(Main main) {
		this.main = main;
	}

	@EventHandler
	public void onInventoryClick(InventoryClickEvent e) {
		Player p = (Player) e.getView().getPlayer();
		if(!(e.getView().getTopInventory() instanceof CraftingInventory)) {
			return;
		}
		CraftingInventory inv = (CraftingInventory) e.getView().getTopInventory();
		Bukkit.getScheduler().runTask(main, new Runnable() {
			public void run() {
				System.out.println(inv.getRecipe());
				if(inv.contains(new ItemStack(Material.APPLE))) {
					inv.setResult(new ItemStack(Material.STONE));
					p.updateInventory();
				}
				else {
					System.out.println("Nothing");
				}
			}
		});
	}
	
	@EventHandler
	public void onInventoryDrag(InventoryDragEvent e) {
		Player p = (Player) e.getView().getPlayer();
		if(!(e.getView().getTopInventory() instanceof CraftingInventory)) {
			return;
		}
		CraftingInventory inv = (CraftingInventory) e.getView().getTopInventory();
		if(inv.contains(new ItemStack(Material.ANVIL))) {
			inv.setResult(new ItemStack(Material.WOOD));
			p.updateInventory();
		}
	}
	
	@EventHandler
	public void onItemCraft(CraftItemEvent e) {
		System.out.println(e.getInventory().getContents());
	}

	
}
