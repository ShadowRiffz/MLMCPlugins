package me.Neoblade298.NeoProfessions.Commands;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.Map.Entry;

import org.apache.commons.lang3.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import de.tr7zw.nbtapi.NBTItem;
import me.Neoblade298.NeoProfessions.CurrencyManager;
import me.Neoblade298.NeoProfessions.Professions;
import me.Neoblade298.NeoProfessions.Augments.ItemEditor;
import me.Neoblade298.NeoProfessions.Augments.Augment;
import me.Neoblade298.NeoProfessions.Augments.AugmentManager;
import me.Neoblade298.NeoProfessions.Inventories.CreateSlotInventory;
import me.Neoblade298.NeoProfessions.Inventories.InspectAugmentsInventory;
import me.Neoblade298.NeoProfessions.Inventories.RepairInventory;
import me.Neoblade298.NeoProfessions.Inventories.SellInventory;
import me.Neoblade298.NeoProfessions.Legacy.Items.BlacksmithItems;
import me.Neoblade298.NeoProfessions.Legacy.Items.CommonItems;
import me.Neoblade298.NeoProfessions.Legacy.Items.DrinksRecipeItems;
import me.Neoblade298.NeoProfessions.Legacy.Items.IngredientRecipeItems;
import me.Neoblade298.NeoProfessions.Legacy.Items.MasonItems;
import me.Neoblade298.NeoProfessions.Legacy.Items.StonecutterItems;
import me.Neoblade298.NeoProfessions.Methods.ProfessionsMethods;
import me.Neoblade298.NeoProfessions.Utilities.Util;
import me.neoblade298.neogear.listeners.DurabilityListener;

public class NeoprofessionsCommands implements CommandExecutor {

	private static final Random gen = new Random();
	Professions main;
	Util util;
	CommonItems common;
	BlacksmithItems bItems;
	StonecutterItems sItems;
	MasonItems mItems;
	IngredientRecipeItems ingr;
	DrinksRecipeItems drink;

	public NeoprofessionsCommands(Professions main) {
		this.main = main;
		util = new Util();
		common = new CommonItems();
		bItems = new BlacksmithItems();
		sItems = new StonecutterItems();
		mItems = new MasonItems();
		ingr = new IngredientRecipeItems();
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String lbl, String[] args) {

		Player p = null;
		if (sender instanceof Player) {
			p = (Player) sender;
		}

		if (args.length == 0) {
			sender.sendMessage("§7- §c/prof convert §7- Converts item in mainhand to new gear system");
			sender.sendMessage("§7- §c/prof inspect §7- Checks your mainhand item's augments");
			sender.sendMessage("§7- §c/prof pay [player] [essence/oretype] [level] [amount]");
			sender.sendMessage(
					"§7- §c/prof liquidate [essence/oretype] [level] [amount] §7- Virtualizes all ore and essence in inventory");
			sender.sendMessage(
					"§7- §c/prof solidify [essence/oretype] [level] [amount] §7- Turns ore/essence into an item in inventory");
			sender.sendMessage("§7- §c/prof balance <player> [essence/oretype] [level]");
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
						ItemStack converted = bItems.getRepairItem(lv);
						converted.setAmount(amt);
						p.getInventory().addItem(converted);
						Util.sendMessage(p, "&7Successfully converted item!");
					}
				}
			}
			else {
				ItemEditor editor = new ItemEditor(main);
				String result = editor.convertItem(p);
				DurabilityListener.fullRepairItem(p, main);
				if (result == null) {
					Util.sendMessage(p, "&7Successfully converted item!");
				}
				else {
					Util.sendMessage(p, "&cItem conversion failed, " + result);
				}
			}
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
		else if (args.length == 5 && args[0].equalsIgnoreCase("pay")) {
			if (Bukkit.getPlayer(args[1]) == null) {
				Util.sendMessage(p, "&cPlayer must be online!");
				return true;
			}
			if (!Professions.cm.validType(args[2])) {
				Util.sendMessage(p, "&cInvalid type!");
				return true;
			}
			int level = Integer.parseInt(args[3]);
			if (!(level <= 60 && level > 0 && level % 5 == 0)) {
				Util.sendMessage(p, "&cInvalid level!");
				return true;
			}
			int amount = Integer.parseInt(args[4]);
			if (amount <= 0 || amount >= 99999 || !Professions.cm.hasEnough(p, args[2], level, amount)) {
				Util.sendMessage(p, "&cInvalid amount!");
				return true;
			}
			Player recipient = Bukkit.getPlayer(args[1]);
			Professions.cm.add(recipient, args[2], level, amount);
			Professions.cm.subtract(p, args[2], level, amount);
			Util.sendMessage(p,
					"&7You paid &e" + recipient.getName() + " " + amount + " Lv " + level + " " + args[2]);
			Util.sendMessage(recipient,
					"&e" + p.getName() + "&7 has paid you &e" + amount + " Lv " + level + " " + args[2]);
			return true;
		}
		else if (args.length == 4 && args[0].equalsIgnoreCase("balance")) {
			if (Bukkit.getPlayer(args[1]) == null) {
				Util.sendMessage(p, "&cPlayer must be online!");
				return true;
			}
			if (!Professions.cm.validType(args[2])) {
				Util.sendMessage(p, "&cInvalid type!");
				return true;
			}
			int level = Integer.parseInt(args[3]);
			if (!(level <= 60 && level > 0 && level % 5 == 0)) {
				Util.sendMessage(p, "&cInvalid level!");
				return true;
			}
			Util.sendMessage(p, "&7Balance: &e" + Professions.cm.get(Bukkit.getPlayer(args[1]), args[2], level));
			return true;
		}
		else if (args.length == 3 && args[0].equalsIgnoreCase("balance")) {
			if (!Professions.cm.validType(args[1])) {
				Util.sendMessage(p, "&cInvalid type!");
				return true;
			}
			int level = Integer.parseInt(args[2]);
			if (!(level <= 60 && level > 0 && level % 5 == 0)) {
				Util.sendMessage(p, "&cInvalid level!");
				return true;
			}
			Util.sendMessage(p, "&7Balance: &e" + Professions.cm.get(p, args[1], level));
			return true;
		}

		// /prof solidify [type] [level] [amount]
		else if (args.length == 4 && args[0].equalsIgnoreCase("solidify")) {
			if (p.getGameMode().equals(GameMode.CREATIVE)) {
				Util.sendMessage(p, "&cCannot be in creative mode for this command!");
				return true;
			}
			if (!Professions.cm.containsKey(args[1])) {
				Util.sendMessage(p, "&cInvalid essence or ore type!");
				return true;
			}
			if (!StringUtils.isNumeric(args[2]) || !StringUtils.isNumeric(args[3])) {
				Util.sendMessage(p, "&cLevel and amount should be a number!");
				return true;
			}
			
			int level = Integer.parseInt(args[2]);
			int amount = Integer.parseInt(args[3]);
			if (level % 5 != 0 || level <= 0 || level > 60) {
				Util.sendMessage(p, "&cInvalid level!");
				return true;
			}
			if (!Professions.cm.hasEnough(p, args[1], level, amount)) {
				Util.sendMessage(p, "&cYou don't have enough to solidify that amount!");
				return true;
			}
			
			HashMap<Integer, ItemStack> result = null;
			if (args[1].equalsIgnoreCase("essence")) {
				result = p.getInventory().addItem(util.setAmount(common.getEssence(level, false), amount));
			}
			else {
				result = p.getInventory().addItem(util.setAmount(sItems.getOreSolidify(args[1], level), amount));
			}
			if (!result.isEmpty()) {
				int notAdded = 0;
				for (Entry<Integer, ItemStack> item : result.entrySet()) {
					notAdded += item.getValue().getAmount();
				}
				Professions.cm.subtract(p, args[1], level, amount - notAdded);
				Util.sendMessage(p, "&7Solidified &e" + (amount - notAdded) + " &7" + args[1] + "!");
				return true;
			}
			else {
				Professions.cm.subtract(p, args[1], level, amount);
				Util.sendMessage(p, "&7Solidified &e" + amount + " &7" + args[1] + "!");
				return true;
			}
		}
		// /prof liquidate [type] [level] [amount]
		else if (args.length == 4 && args[0].equalsIgnoreCase("liquidate")) {
			if (p.getGameMode().equals(GameMode.CREATIVE)) {
				Util.sendMessage(p, "&cCannot be in creative mode for this command!");
				return true;
			}
			if (!Professions.cm.containsKey(args[1])) {
				Util.sendMessage(p, "&cInvalid essence or ore type!");
				return true;
			}
			if (!StringUtils.isNumeric(args[2]) || !StringUtils.isNumeric(args[3])) {
				Util.sendMessage(p, "&cLevel and amount should be a number!");
				return true;
			}
			
			int level = Integer.parseInt(args[2]);
			int amount = Integer.parseInt(args[3]);
			if (level % 5 != 0 || level <= 0 || level > 60) {
				Util.sendMessage(p, "&cInvalid level!");
				return true;
			}
			if (args[1].equalsIgnoreCase("essence") && p.getInventory().containsAtLeast(common.getEssence(level, false), amount)) {
				HashMap<Integer, ItemStack> result = p.getInventory()
						.removeItem(util.setAmount(common.getEssence(level, false), amount));
				if (!result.isEmpty()) {
					int notAdded = 0;
					for (Entry<Integer, ItemStack> item : result.entrySet()) {
						notAdded += item.getValue().getAmount();
					}
					Professions.cm.add(p, args[1], level, amount - notAdded);
					Util.sendMessage(p, "&7Liquidated &e" + (amount - notAdded) + " &7essence!");
					return true;
				}
				else {
					Professions.cm.add(p, args[1], level, amount);
					Util.sendMessage(p, "&7Liquidated &e" + amount + " &7essence!");
					return true;
				}
			}
			else if (!args[1].equalsIgnoreCase("essence") && p.getInventory().containsAtLeast(sItems.getOreSolidify(args[1], level), amount)) {
				HashMap<Integer, ItemStack> result = p.getInventory()
						.removeItem(util.setAmount(sItems.getOreSolidify(args[1], level), amount));
				if (!result.isEmpty()) {
					int notAdded = 0;
					for (Entry<Integer, ItemStack> item : result.entrySet()) {
						notAdded += item.getValue().getAmount();
					}
					Professions.cm.add(p, args[1], level, amount - notAdded);
					Util.sendMessage(p, "&7Liquidated &e" + (amount - notAdded) + " &7essence!");
					return true;
				}
				else {
					Professions.cm.add(p, args[1], level, amount);
					Util.sendMessage(p, "&7Liquidated &e" + amount + " &7" + args[1] + "!");
					return true;
				}
			}
			else {
				Util.sendMessage(p, "&cYou don't have enough to liquidate that amount!");
				return true;
			}
		}

		if (sender.hasPermission("neoprofessions.admin") || sender.isOp()) {
			if (args.length == 0) {
				sender.sendMessage("§7- §4/prof sell [playername]");
				sender.sendMessage("§7- §4/prof repair [playername]");
				sender.sendMessage("§7- §4/prof checkaugments [playername]");
				sender.sendMessage("§7- §4/prof createslot [playername]");
				sender.sendMessage("§7- §4/prof {reset/sober/repair} [playername]");
				sender.sendMessage("§7- §4/prof give <p> {essence/ore/repair/augment} <aug> [level] <amount>");
				sender.sendMessage("§7- §4/prof artifact <playername>");
				sender.sendMessage("§7- §4/prof givepaint [playername] R G B");
				return true;
			}
			else {
				if (args[0].equalsIgnoreCase("sell")) {
					if (args.length == 1) {
						new SellInventory(main, Bukkit.getPlayer(args[1]));
					}
					else {
						new SellInventory(main, p);
					}
					return true;
				}
				if (args[0].equalsIgnoreCase("repair")) {
					
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
				if (args[0].equalsIgnoreCase("createslot")) {
					
					if (args.length == 1) {
						ProfessionsMethods.createSlot(p, p.getInventory().getItemInMainHand());
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
				if (args[0].equalsIgnoreCase("checkaugments")) {
					if (args.length == 1) {
						System.out.println(AugmentManager.playerAugments.get((Player) sender));
					}
					else {
						System.out.println(AugmentManager.playerAugments.get(Bukkit.getPlayer(args[1])));
					}
					return true;
				}
				else if (args[0].equalsIgnoreCase("debug")) {
					main.debug = !main.debug;
					p.sendMessage("Debug set to " + main.debug);
					return true;
				}
				// /prof givepaint [player] R G B
				else if (args[0].equalsIgnoreCase("givepaint")) {
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
				// /prof give [essence/oretype/repair/augment] <aug> [level] [amount]
				else if (args[0].equalsIgnoreCase("give")) {
					int offset = 0;
					String aug = null;
					int amt = 1;
					if (Bukkit.getPlayer(args[1]) != null) {
						p = Bukkit.getPlayer(args[1]);
						offset++;
					}
					String type = args[1 + offset];
					// Is an augment
					if (!StringUtils.isNumeric(args[2 + offset])) {
						aug = args[2 + offset];
						offset++;
					}
					int lv = Integer.parseInt(args[2 + offset]);
					if (args.length > 3 + offset) {
						amt = Integer.parseInt(args[3 + offset]);
					}
					
					if (type.equalsIgnoreCase("randomore")) {
						String otype = CurrencyManager.types[gen.nextInt(7) + 1];
						Professions.cm.add(p, otype, lv, amt);
						Util.sendMessage(sender, "&7Successfully gave " + amt + " Lv " + lv + " " + type + " ore to " + p.getName() + "!");
						return true;
					}
					else if (type.equalsIgnoreCase("essence")) {
						Professions.cm.add(p, args[1], lv, amt);
						Util.sendMessage(sender, "&7Successfully gave " + amt + " Lv " + lv + " essence to " + p.getName() + "!");
						return true;
					}
					else if (type.equalsIgnoreCase("augment")) {
						Augment augment = null;
						if (AugmentManager.droptables.containsKey(aug)) {
							ArrayList<String> table = AugmentManager.droptables.get(aug);
							augment = AugmentManager.augmentMap.get(table.get(gen.nextInt(table.size()))).get(lv);
						}
						else if (AugmentManager.augmentMap.containsKey(aug)) {
							augment = AugmentManager.augmentMap.get(aug).get(lv);
						}
						else {
							ArrayList<String> table = AugmentManager.droptables.get("default");
							augment = AugmentManager.augmentMap.get(table.get(gen.nextInt(table.size()))).get(lv);
						}
						ItemStack item = augment.getItem(p);
						item.setAmount(amt);
						p.getInventory().addItem(item);
						Util.sendMessage(sender, "&7Successfully gave " + augment.getLine() + " §7to " + p.getName() + "!");
					}
					else if (type.equalsIgnoreCase("repair")) {
						ItemStack item = bItems.getRepairItem(lv);
						item.setAmount(amt);
						p.getInventory().addItem(item);
						Util.sendMessage(sender, "&7Successfully gave " + amt + " Lv " + lv + " repairs to " + p.getName() + "!");
					}
					return true;
				}
			}
		}
		return true;
	}
}