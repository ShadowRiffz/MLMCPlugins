package me.neoblade298.neoinfinitybow;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public class Commands implements CommandExecutor {
	public Commands() {
	}

	public boolean onCommand(CommandSender sender, Command cmd, String lbl, String[] args) {
		if ((args.length == 1) && (sender.isOp())) {
			PlayerInventory inv = org.bukkit.Bukkit.getPlayer(args[0]).getInventory();
			ItemStack item = inv.getItemInMainHand();
			ItemStack offItem = inv.getItemInOffHand();
			if (item.getEnchantmentLevel(Enchantment.ARROW_INFINITE) == 0
					&& offItem.getEnchantmentLevel(Enchantment.ARROW_INFINITE) == 0) {
				inv.removeItem(new ItemStack(Material.ARROW));
			}
		}

		return true;
	}
}