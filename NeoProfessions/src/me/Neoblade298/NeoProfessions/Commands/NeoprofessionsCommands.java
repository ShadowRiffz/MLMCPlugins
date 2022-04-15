package me.Neoblade298.NeoProfessions.Commands;

import java.util.ArrayList;
import java.util.logging.Level;

import org.apache.commons.lang3.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import de.tr7zw.nbtapi.NBTItem;
import io.lumine.xikage.mythicmobs.MythicMobs;
import me.Neoblade298.NeoProfessions.Professions;
import me.Neoblade298.NeoProfessions.Augments.ItemEditor;
import me.Neoblade298.NeoProfessions.Augments.Augment;
import me.Neoblade298.NeoProfessions.Inventories.CreateSlotInventory;

import me.Neoblade298.NeoProfessions.Inventories.InspectAugmentsInventory;
import me.Neoblade298.NeoProfessions.Inventories.RecipeSelectInventory;
import me.Neoblade298.NeoProfessions.Inventories.RecipeView;
import me.Neoblade298.NeoProfessions.Inventories.RepairInventory;
import me.Neoblade298.NeoProfessions.Inventories.SellInventory;
import me.Neoblade298.NeoProfessions.Inventories.StorageView;
import me.Neoblade298.NeoProfessions.Legacy.BlacksmithItems;
import me.Neoblade298.NeoProfessions.Managers.AugmentManager;
import me.Neoblade298.NeoProfessions.Managers.MinigameManager;
import me.Neoblade298.NeoProfessions.Managers.RecipeManager;
import me.Neoblade298.NeoProfessions.Managers.StorageManager;
import me.Neoblade298.NeoProfessions.Methods.ProfessionsMethods;
import me.Neoblade298.NeoProfessions.Utilities.Util;
import me.neoblade298.neogear.listeners.DurabilityListener;

public class NeoprofessionsCommands implements CommandExecutor {

	Professions main;

	public NeoprofessionsCommands(Professions main) {
		this.main = main;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String lbl, String[] args) {

		Player p = null;
		if (sender instanceof Player) {
			p = (Player) sender;
		}

		if (args.length == 0) {
			// /prof storage [min] [max]
			// /prof recipes [menu name] [recipe list]
			sender.sendMessage("§7- §c/prof convert §7- Converts item in mainhand to new gear system");
			sender.sendMessage("§7- §c/prof inspect §7- Checks your mainhand item's augments");
		}
		else if (args.length == 1 && args[0].equalsIgnoreCase("convert")) {
			ItemStack main = p.getInventory().getItemInMainHand();
			if (main.getType().equals(Material.PRISMARINE_CRYSTALS)) {
				if (main.hasItemMeta() && main.getItemMeta().hasLore()) {
					if (new NBTItem(main).hasKey("potency")) {
						Util.sendMessage(p, "&7This repair kit is already converted!");
						return true;
					}
					ArrayList<String> lore = (ArrayList<String>) main.getItemMeta().getLore();
					if (lore.get(0).contains("Right click to use")) {
						String lvLine = lore.get(2);
						int lv = Integer.parseInt(lvLine.substring(lvLine.indexOf("Lv ") + 3));
						int amt = main.getAmount();
						p.getInventory().removeItem(main);
						ItemStack converted = BlacksmithItems.getRepairItem(lv);
						converted.setAmount(amt);
						p.getInventory().addItem(converted);
						Util.sendMessage(p, "&7Successfully converted item!");
					}
				}
			}
			else if (main.getType().equals(Material.ENDER_PEARL)) {
				if (main.hasItemMeta() && main.getItemMeta().hasLore()) {
					if (new NBTItem(main).hasKey("augment")) {
						Util.sendMessage(p, "&7This item is already converted!");
						return true;
					}
					ArrayList<String> lore = (ArrayList<String>) main.getItemMeta().getLore();
					String type = null;
					if (lore.get(0).contains("Exp charm")) {
						type = "Experience";
					}
					else if (lore.get(9).contains("Drop")) {
						type = "Chest Chance";
					}
					
					if (type != null) {
						int lv = main.getEnchantmentLevel(Enchantment.DURABILITY);
						int amt = main.getAmount();
						p.getInventory().removeItem(main);
						ItemStack converted = AugmentManager.getFromCache(type, lv).getItem(p);
						converted.setAmount(amt);
						p.getInventory().addItem(converted);
						Util.sendMessage(p, "&7Successfully converted item!");
					}
				}
			}
			else if (main.getType().equals(Material.QUARTZ)) {
				if (main.hasItemMeta() && main.getItemMeta().hasLore()) {
					if (new NBTItem(main).hasKey("augment")) {
						Util.sendMessage(p, "&7This item is already converted!");
						return true;
					}
					ArrayList<String> lore = (ArrayList<String>) main.getItemMeta().getLore();
					String[] relicStrings = lore.get(0).split(" ");
					String relic = "Relic" + relicStrings[4];
					for (int i = 5; i < relicStrings.length; i++) {
						relic += relicStrings[i];
					}

					int amt = main.getAmount();
					ItemStack converted = MythicMobs.inst().getItemManager().getItemStack(relic);
					if (converted != null) {
						p.getInventory().removeItem(main);
						converted.setAmount(amt);
						p.getInventory().addItem(converted);
						Util.sendMessage(p, "&7Successfully converted item!");
					}
					else {
						Util.sendMessage(p, "&cFailed to convert item!");
					}
				}
			}
			else {
				ItemEditor editor = new ItemEditor(main);
				String result = editor.convertItem(p);
				if (result == null) {
					Util.sendMessage(p, "&7Successfully converted item!");
				}
				else {
					Util.sendMessage(p, "&cItem conversion failed, " + result);
				}
			}
			return true;
		}
		else if (args[0].equalsIgnoreCase("storage")) {
			new StorageView(p, Integer.parseInt(args[1]), Integer.parseInt(args[2]));
			return true;
		}
		else if (args[0].equalsIgnoreCase("recipes")) {
			new RecipeView(p, args[1].replaceAll("_", " "), RecipeManager.getRecipeList(args[2]), RecipeSelectInventory.class.getName());
			return true;
		}
		else if (args.length == 1 && args[0].equalsIgnoreCase("inspect")) {
			ItemStack item = p.getInventory().getItemInMainHand();
			if (item != null && !item.getType().isAir() && item.hasItemMeta()) {
				if(new NBTItem(item).hasKey("version")) {
					new InspectAugmentsInventory(main, p, item);
					return true;
				}
			}
			Util.sendMessage(p, "&cCould not inspect this item!");
			return true;
		}

		if (sender.hasPermission("neoprofessions.admin") || sender.isOp()) {
			if (args.length == 0) {
				sender.sendMessage("§7- §4/prof give [repair/augment] <aug> [level] [amount]");
				sender.sendMessage("§7- §4/prof give item [id] [amount]");
				sender.sendMessage("§7- §4/prof debug");
				sender.sendMessage("§7- §4/prof start [minigame] [playername]");
				sender.sendMessage("§7- §4/prof repair [playername]");
				sender.sendMessage("§7- §4/prof checkaugments [playername]");
				sender.sendMessage("§7- §4/prof createslot [playername]");
				sender.sendMessage("§7- §4/prof artifact <playername>");
				sender.sendMessage("§7- §4/prof givepaint [playername] R G B");
				return true;
			}
			else {
				if (args[0].equalsIgnoreCase("sell")) {
					if (args.length == 1) {
						new SellInventory(main, p);
					}
					else {
						new SellInventory(main, Bukkit.getPlayer(args[1]));
					}
					return true;
				}
				else if (args[0].equalsIgnoreCase("repair")) {
					if (args.length == 1) {
						DurabilityListener.fullRepairItem(p, p.getInventory().getItemInMainHand());
					}
					else {
						p = Bukkit.getPlayer(args[1]);
						ItemStack item = p.getInventory().getItemInMainHand();
						if (item == null || item.getType().isAir()) {
							Util.sendMessage(p, "§cYou're not holding anything in your hand!");
							return true;
						}
						NBTItem nbti = new NBTItem(item);
						if (!nbti.hasKey("gear") || !nbti.hasKey("level")) {
							Util.sendMessage(p, "§cThis item cannot be repaired! It may be outdated.");
							return true;
						}
						new RepairInventory(main, p);
					}
					return true;
				}
				else if (args[0].equalsIgnoreCase("createslot")) {
					if (args.length == 1) {
						ProfessionsMethods.createSlot(p, p.getInventory().getItemInMainHand());
						p.updateInventory();
					}
					else {
						p = Bukkit.getPlayer(args[1]);
						ItemStack item = p.getInventory().getItemInMainHand();
						if (item == null || item.getType().isAir()) {
							Util.sendMessage(p, "§cYou're not holding anything in your hand!");
							return true;
						}
						NBTItem nbti = new NBTItem(item);
						if (!nbti.hasKey("gear") || !nbti.hasKey("level")) {
							Util.sendMessage(p, "§cThis item cannot be repaired! It may be outdated.");
							return true;
						}
						new CreateSlotInventory(main, p);
					}
					return true;
				}
				else if (args[0].equalsIgnoreCase("checkaugments")) {
					if (args.length == 1) {
						Bukkit.getLogger().log(Level.INFO, "" + AugmentManager.getPlayerAugments((Player) sender));
					}
					else {
						Bukkit.getLogger().log(Level.INFO, "" + AugmentManager.getPlayerAugments(Bukkit.getPlayer(args[1])));
					}
					return true;
				}
				else if (args[0].equalsIgnoreCase("start")) {
					MinigameManager.startMinigame(Bukkit.getPlayer(args[2]), Integer.parseInt(args[1]));
					return true;
				}
				else if (args[0].equalsIgnoreCase("debug")) {
					main.debug = !main.debug;
					p.sendMessage("Debug set to " + main.debug);
					return true;
				}
				// /prof givepaint [player] R G B
				else if (args[0].equalsIgnoreCase("1paint")) {
					ProfessionsMethods.givePaint(Bukkit.getPlayer(args[1]), args[2], args[3], args[4]);
					return true;
				}
				// /prof artifact <playername>
				else if (args[0].equalsIgnoreCase("artifact")) {
					if (args.length == 2) {
						ProfessionsMethods.artifactItem(Bukkit.getPlayer(args[1]));
						return true;
					}
					else {
						ProfessionsMethods.artifactItem(p);
					}
				}
				// /prof give [repair/augment/item] <aug/item> [level] [amount]
				else if (args[0].equalsIgnoreCase("give") || args[0].equalsIgnoreCase("get")) {
					int offset = 0;
					String aug = null;
					int amt = 1;
					if (Bukkit.getPlayer(args[1]) != null) {
						p = Bukkit.getPlayer(args[1]);
						offset++;
					}
					String type = args[1 + offset];
					// Augment-specific parsing
					if (args[1 + offset].equalsIgnoreCase("augment")) {
						if (!StringUtils.isNumeric(args[2 + offset])) {
							aug = args[2 + offset].replaceAll("_", " ");
							offset++;
						}
					}

					int lv = Integer.parseInt(args[2 + offset]);
					int id = lv;
					lv -= lv % 5; // Round to nearest 5
					if (args.length > 3 + offset) {
						amt = Integer.parseInt(args[3 + offset]);
					}
		
					if (type.equalsIgnoreCase("augment")) {
						Augment augment = null;
						if (AugmentManager.isDroptable(aug)) {
							augment = AugmentManager.getViaDroptable(aug, lv);
						}
						else if (AugmentManager.hasAugment(aug.toLowerCase())) {
							augment = AugmentManager.getFromCache(aug.toLowerCase(), lv);
						}
						else {
							augment = AugmentManager.getViaDroptable("default", lv);
						}
						ItemStack item = augment.getItem(p);
						item.setAmount(amt);
						p.getInventory().addItem(item);
						Util.sendMessage(sender, "&7Successfully gave " + augment.getLine() + " §7to " + p.getName() + "!");
					}
					else if (type.equalsIgnoreCase("repair")) {
						ItemStack item = BlacksmithItems.getRepairItem(lv);
						item.setAmount(amt);
						p.getInventory().addItem(item);
						Util.sendMessage(sender, "&7Successfully gave " + amt + " Lv " + lv + " repairs to " + p.getName() + "!");
					}
					else if (type.equalsIgnoreCase("item")) {
						StorageManager.givePlayer(p, id, amt);
						Util.sendMessage(sender, "&7Successfully gave " + amt + " " + StorageManager.getItem(id).getDisplay() +
								" to " + p.getName() + "!");
					}
					return true;
				}
			}
		}
		return true;
	}
}