package me.Neoblade298.NeoProfessions.Commands;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.Map.Entry;

import org.apache.commons.lang3.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.inventory.meta.Damageable;

import com.sucy.skill.SkillAPI;
import com.sucy.skill.api.player.PlayerClass;

import de.tr7zw.nbtapi.NBTItem;
import me.Neoblade298.NeoProfessions.CurrencyManager;
import me.Neoblade298.NeoProfessions.Professions;
import me.Neoblade298.NeoProfessions.Augments.ItemEditor;
import me.Neoblade298.NeoProfessions.Augments.AugmentManager;
import me.Neoblade298.NeoProfessions.Inventories.InspectAugmentsInventory;
import me.Neoblade298.NeoProfessions.Inventories.SellInventory;
import me.Neoblade298.NeoProfessions.Items.BlacksmithItems;
import me.Neoblade298.NeoProfessions.Items.CommonItems;
import me.Neoblade298.NeoProfessions.Items.DrinksRecipeItems;
import me.Neoblade298.NeoProfessions.Items.IngredientRecipeItems;
import me.Neoblade298.NeoProfessions.Items.MasonItems;
import me.Neoblade298.NeoProfessions.Items.StonecutterItems;
import me.Neoblade298.NeoProfessions.Methods.BlacksmithMethods;
import me.Neoblade298.NeoProfessions.Methods.CulinarianMethods;
import me.Neoblade298.NeoProfessions.Methods.MasonMethods;
import me.Neoblade298.NeoProfessions.Methods.StonecutterMethods;
import me.Neoblade298.NeoProfessions.Utilities.BlacksmithUtils;
import me.Neoblade298.NeoProfessions.Utilities.Util;
import me.neoblade298.neogear.listeners.DurabilityListener;

public class NeoprofessionsCommands implements CommandExecutor {

	private static final int LEVEL_INTERVAL = 5;
	Professions main;
	BlacksmithMethods blacksmithMethods;
	StonecutterMethods stonecutterMethods;
	CulinarianMethods culinarianMethods;
	MasonMethods masonMethods;
	Util util;
	CommonItems common;
	BlacksmithItems bItems;
	StonecutterItems sItems;
	MasonItems mItems;
	IngredientRecipeItems ingr;
	DrinksRecipeItems drink;
	CurrencyManager cm;

	public NeoprofessionsCommands(Professions main, BlacksmithMethods b, StonecutterMethods s, CulinarianMethods c,
			MasonMethods m) {
		this.main = main;
		this.blacksmithMethods = b;
		this.stonecutterMethods = s;
		this.culinarianMethods = c;
		this.masonMethods = m;
		util = new Util();
		common = new CommonItems();
		bItems = new BlacksmithItems();
		sItems = new StonecutterItems();
		mItems = new MasonItems();
		ingr = new IngredientRecipeItems();
		cm = main.cManager;
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
			if (!main.cManager.validType(args[2])) {
				Util.sendMessage(p, "&cInvalid type!");
				return true;
			}
			int level = Integer.parseInt(args[3]);
			if (!(level <= 60 && level > 0 && level % 5 == 0)) {
				Util.sendMessage(p, "&cInvalid level!");
				return true;
			}
			int amount = Integer.parseInt(args[4]);
			if (amount <= 0 || amount >= 99999 || !main.cManager.hasEnough(p, args[2], level, amount)) {
				Util.sendMessage(p, "&cInvalid amount!");
				return true;
			}
			Player recipient = Bukkit.getPlayer(args[1]);
			main.cManager.add(recipient, args[2], level, amount);
			main.cManager.subtract(p, args[2], level, amount);
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
			if (!main.cManager.validType(args[2])) {
				Util.sendMessage(p, "&cInvalid type!");
				return true;
			}
			int level = Integer.parseInt(args[3]);
			if (!(level <= 60 && level > 0 && level % 5 == 0)) {
				Util.sendMessage(p, "&cInvalid level!");
				return true;
			}
			Util.sendMessage(p, "&7Balance: &e" + main.cManager.get(Bukkit.getPlayer(args[1]), args[2], level));
			return true;
		}
		else if (args.length == 3 && args[0].equalsIgnoreCase("balance")) {
			if (!main.cManager.validType(args[1])) {
				Util.sendMessage(p, "&cInvalid type!");
				return true;
			}
			int level = Integer.parseInt(args[2]);
			if (!(level <= 60 && level > 0 && level % 5 == 0)) {
				Util.sendMessage(p, "&cInvalid level!");
				return true;
			}
			Util.sendMessage(p, "&7Balance: &e" + main.cManager.get(p, args[1], level));
			return true;
		}

		// /prof solidify [type] [level] [amount]
		else if (args.length == 4 && args[0].equalsIgnoreCase("solidify")) {
			if (!p.getGameMode().equals(GameMode.CREATIVE)) {
				if (cm.containsKey(args[1])) {
					if (StringUtils.isNumeric(args[2]) && StringUtils.isNumeric(args[3])) {
						int level = Integer.parseInt(args[2]);
						int amount = Integer.parseInt(args[3]);
						if (level % 5 == 0 && level > 0 && level <= 60) {
							if (cm.hasEnough(p, args[1], level, amount)) {
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
									cm.subtract(p, args[1], level, amount - notAdded);
									Util.sendMessage(p, "&7Solidified &e" + (amount - notAdded) + " &7" + args[1] + "!");
									return true;
								}
								else {
									cm.subtract(p, args[1], level, amount);
									Util.sendMessage(p, "&7Solidified &e" + amount + " &7" + args[1] + "!");
									return true;
								}
							}
							else {
								Util.sendMessage(p, "&cYou don't have enough to solidify that amount!");
								return true;
							}
						}
						else {
							Util.sendMessage(p, "&cInvalid level!");
							return true;
						}
					}
					else {
						Util.sendMessage(p, "&cLevel and amount should be a number!");
						return true;
					}
				}
				else {
					Util.sendMessage(p, "&cInvalid essence or ore type!");
					return true;
				}
			}
			else {
				Util.sendMessage(p, "&cCannot be in creative mode for this command!");
				return true;
			}
		}
		// /prof liquidate [type] [level] [amount]
		else if (args.length == 4 && args[0].equalsIgnoreCase("liquidate")) {
			if (!p.getGameMode().equals(GameMode.CREATIVE)) {
				if (cm.containsKey(args[1])) {
					if (StringUtils.isNumeric(args[2]) && StringUtils.isNumeric(args[3])) {
						int level = Integer.parseInt(args[2]);
						int amount = Integer.parseInt(args[3]);
						if (level % 5 == 0 && level > 0 && level <= 60) {
							if (args[1].equalsIgnoreCase("essence") && p.getInventory().containsAtLeast(common.getEssence(level, false), amount)) {
								HashMap<Integer, ItemStack> result = p.getInventory()
										.removeItem(util.setAmount(common.getEssence(level, false), amount));
								if (!result.isEmpty()) {
									int notAdded = 0;
									for (Entry<Integer, ItemStack> item : result.entrySet()) {
										notAdded += item.getValue().getAmount();
									}
									cm.add(p, args[1], level, amount - notAdded);
									Util.sendMessage(p, "&7Liquidated &e" + (amount - notAdded) + " &7essence!");
									return true;
								}
								else {
									cm.add(p, args[1], level, amount);
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
									cm.add(p, args[1], level, amount - notAdded);
									Util.sendMessage(p, "&7Liquidated &e" + (amount - notAdded) + " &7essence!");
									return true;
								}
								else {
									cm.add(p, args[1], level, amount);
									Util.sendMessage(p, "&7Liquidated &e" + amount + " &7" + args[1] + "!");
									return true;
								}
							}
							else {
								Util.sendMessage(p, "&cYou don't have enough to liquidate that amount!");
								return true;
							}
						}
						else {
							Util.sendMessage(p, "&cInvalid level!");
							return true;
						}
					}
					else {
						Util.sendMessage(p, "&cLevel and amount should be a number!");
						return true;
					}
				}
				else {
					Util.sendMessage(p, "&cInvalid essence or ore type!");
					return true;
				}
			}
			else {
				Util.sendMessage(p, "&cCannot be in creative mode for this command!");
				return true;
			}
		}

		if (sender.hasPermission("neoprofessions.admin") || sender.isOp()) {
			if (args.length == 0) {
				sender.sendMessage("§7- §4/prof level/points [playername] <amount>");
				sender.sendMessage("§7- §4/prof sell [playername]");
				sender.sendMessage("§7- §4/prof checkaugments [playername]");
				sender.sendMessage("§7- §4/prof {reset/sober/repair} [playername]");
				sender.sendMessage("§7- §4/prof <playername> get {essence/repair} [level]");
				sender.sendMessage("§7- §4/prof <playername> get ingr [22-24]");
				sender.sendMessage("§7- §4/prof <playername> get augment [name] [level]");
				sender.sendMessage("§7- §4/prof <playername> get durability [weapon/armor] [level]");
				sender.sendMessage("§7- §4/prof <playername> get ore [attribute or 1-7] [level] <amount>");
				sender.sendMessage("§7- §4/prof <playername> get {gem/overload} [weapon/armor] [attribute] [level]");
				sender.sendMessage("§7- §4/prof <playername> get [basic/advanced] [charm]");
				sender.sendMessage("§7- §4/prof <playername> get augment [name] [level]");
				sender.sendMessage("§7- §4/prof <playername> add [essence/oretype] [level] [amount]");
				sender.sendMessage("§7- §4/prof artifact <playername>");
				sender.sendMessage("§7- §4/prof givepaint [playername] R G B");
				return true;
			}
			else {
				if (args[0].equalsIgnoreCase("sell")) {
					new SellInventory(main, Bukkit.getPlayer(args[1]));
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
				// /prof quickfix
				else if (args[0].equalsIgnoreCase("quickfix")) {
					ItemStack item = p.getInventory().getItemInMainHand();
					ItemMeta meta = item.getItemMeta();
					ArrayList<String> lore = (ArrayList<String>) meta.getLore();
					lore.set(0, lore.get(0) + "§0");
					meta.setLore(lore);
					item.setItemMeta(meta);
					return true;
				}
				// /prof givepaint [player] R G B
				else if (args[0].equalsIgnoreCase("givepaint")) {
					ItemStack item = new ItemStack(Material.POTION);
					PotionMeta meta = (PotionMeta) item.getItemMeta();
					meta.setDisplayName("§nDye");
					ArrayList<String> lore = new ArrayList<String>();
					lore.add("§c§lRed: §f" + args[2]);
					lore.add("§a§lGreen: §f" + args[3]);
					lore.add("§9§lBlue: §f" + args[4]);
					meta.setLore(lore);
					Color color = Color.fromRGB(Integer.parseInt(args[2]), Integer.parseInt(args[3]), Integer.parseInt(args[4]));
					meta.setColor(color);
					item.setItemMeta(meta);
					Bukkit.getPlayer(args[1]).getInventory().addItem(item);
					return true;
				}
				// /prof add [essence/oretype] [level] [amount]
				else if (args[0].equalsIgnoreCase("add")) {
					if (args[1].equalsIgnoreCase("randomore")) {
						Random gen = new Random();
						String type = CurrencyManager.types[gen.nextInt(7) + 1];
						int level = util.roundToLevel(Integer.parseInt(args[2]), LEVEL_INTERVAL);
						int amount = Integer.parseInt(args[3]);
						this.main.cManager.add(p, type, level, amount);
						Util.sendMessage(sender, "&7Successfully gave " + amount + " Lv " + level + " " + type + " ore to + " + p.getName() + "!");
						return true;
					}
					else {
						int level = util.roundToLevel(Integer.parseInt(args[2]), LEVEL_INTERVAL);
						int amount = Integer.parseInt(args[3]);
						this.main.cManager.add(p, args[1], level, amount);
						Util.sendMessage(sender, "&7Successfully gave " + amount + " Lv " + level + " essence to + " + p.getName() + "!");
						return true;
					}
				}
				// /prof artifact <playername>
				else if (args[0].equalsIgnoreCase("artifact")) {
					if (args.length == 2) {
						main.professionsMethods.artifactItem(Bukkit.getPlayer(args[1]));
						return true;
					}
					else {
						main.professionsMethods.artifactItem((Player) sender);
					}
				}
				else if (args[0].equalsIgnoreCase("sober")) {
					if (args.length == 2) {
						main.culinarianListeners.drunkness.put(Bukkit.getPlayer(args[1]), 0);
						Util.sendMessage(Bukkit.getPlayer(args[1]), "&7Successfully sobered!");
						return true;
					}
				}
				else if (args[0].equalsIgnoreCase("repair")) {
					if (args.length == 2) {
						Player target = Bukkit.getPlayer(args[1]);
						ItemStack item = target.getInventory().getItemInMainHand();
						Util util = new Util();
						BlacksmithUtils bUtils = new BlacksmithUtils();
						if (bUtils.canRepair(item)) {
							ItemMeta im = item.getItemMeta();
							((Damageable) im).setDamage(0);
							item.setItemMeta(im);
							if (bUtils.isGear(item)) {
								util.setCurrentDurability(item, util.getMaxDurability(item));
							}
							Util.sendMessage(Bukkit.getPlayer(args[1]), "&7Item repaired successfully!");
						}
						else {
							Util.sendMessage(Bukkit.getPlayer(args[1]), "&cItem no longer supported by server!");
						}
						return true;
					}
				}
				// /prof level playername
				else if (args[0].equalsIgnoreCase("level")) {
					if (args.length == 2) {
						PlayerClass pClass = SkillAPI.getPlayerData(Bukkit.getPlayer(args[1])).getClass("profession");
						if (pClass != null) {
							if (pClass.getLevel() < 60) {
								pClass.setLevel(pClass.getLevel() + 1);
								pClass.setPoints(pClass.getPoints() + 2);
								Util.sendMessage(Bukkit.getPlayer(args[1]),
										"&7Your profession level is now &e" + pClass.getLevel() + "&7!");
							}
						}
						return true;
					}
					else if (args.length == 3) {
						PlayerClass pClass = SkillAPI.getPlayerData(Bukkit.getPlayer(args[1])).getClass("profession");
						int levels = Integer.parseInt(args[2]);
						if (pClass != null) {
							if (pClass.getLevel() + levels <= 60) {
								pClass.setLevel(pClass.getLevel() + levels);
								pClass.setPoints(pClass.getPoints() + (2 * levels));
								Util.sendMessage(Bukkit.getPlayer(args[1]),
										"&7Your profession level is now &e" + pClass.getLevel() + "&7!");
							}
						}
						return true;
					}
				}
				else if (args[0].equalsIgnoreCase("points")) {
					if (args.length == 3) {
						PlayerClass pClass = SkillAPI.getPlayerData(Bukkit.getPlayer(args[1])).getClass("profession");
						int points = Integer.parseInt(args[2]);
						if (pClass != null) {
							pClass.setPoints(pClass.getPoints() + points);
							Util.sendMessage(Bukkit.getPlayer(args[1]),
									"&7You gained &e" + pClass.getLevel() + "profession points&7!");
						}
						return true;
					}
				}
				else if (args[0].equalsIgnoreCase("reset")) {
					PlayerClass pClass = SkillAPI.getPlayerData(Bukkit.getPlayer(args[1])).getClass("profession");
					if (pClass != null) {
						if (pClass.getData().getName().equalsIgnoreCase("Blacksmith")) {
							blacksmithMethods.resetPlayer(Bukkit.getPlayer(args[1]));
							return true;
						}
						else if (pClass.getData().getName().equalsIgnoreCase("Stonecutter")) {
							stonecutterMethods.resetPlayer(Bukkit.getPlayer(args[1]));
							return true;
						}
						else if (pClass.getData().getName().equalsIgnoreCase("Culinarian")) {
							culinarianMethods.resetPlayer(Bukkit.getPlayer(args[1]));
							return true;
						}
						else if (pClass.getData().getName().equalsIgnoreCase("Mason")) {
							masonMethods.resetPlayer(Bukkit.getPlayer(args[1]));
							return true;
						}
					}
					return true;
				}
				else if (args[0].equalsIgnoreCase("get")) {
					if (args[1].equalsIgnoreCase("essence")) {
						p.getInventory().addItem(common.getEssence(Integer.parseInt(args[2]), true));
					}
					else if (args[1].equalsIgnoreCase("augment")) {
						p.getInventory().addItem(AugmentManager.augmentMap.get(args[2].replaceAll("_", " ")).get(Integer.parseInt(args[3])).getItem(p));
					}
					else if (args[1].equalsIgnoreCase("repair")) {
						p.getInventory().addItem(bItems.getRepairItem(Integer.parseInt(args[2])));
					}
					else if (args[1].equalsIgnoreCase("durability")) {
						p.getInventory().addItem(bItems.getDurabilityItem(Integer.parseInt(args[3]), args[2]));
					}
					else if (args[1].equalsIgnoreCase("ore")) {
						int amount = 1;
						if (args.length == 5) {
							amount = Integer.parseInt(args[4]);
						}
						if (StringUtils.isNumeric(args[2])) {
							String oreName = null;
							switch (args[2]) {
							case "1":
								oreName = "ruby";
								break;
							case "2":
								oreName = "amethyst";
								break;
							case "3":
								oreName = "sapphire";
								break;
							case "4":
								oreName = "emerald";
								break;
							case "5":
								oreName = "topaz";
								break;
							case "6":
								oreName = "garnet";
								break;
							case "7":
								oreName = "adamantium";
								break;
							default:
								oreName = "adamantium";
								break;
							}
							cm.add(p, oreName, Integer.parseInt(args[3]), amount);
						}
						else {
							cm.add(p, args[2].toLowerCase(), Integer.parseInt(args[3]), amount);
						}
					}
					else if (args[1].equalsIgnoreCase("gem")) {
						if (args[2].equalsIgnoreCase("weapon")) {
							p.getInventory().addItem(sItems.getWeaponGem(args[3], Integer.parseInt(args[4]), false));
						}
						else if (args[2].equalsIgnoreCase("armor")) {
							p.getInventory().addItem(sItems.getArmorGem(args[3], Integer.parseInt(args[4]), false));
						}
					}
					else if (args[1].equalsIgnoreCase("ingr")) {
						if (args[2].equals("22")) {
							p.getInventory().addItem(ingr.getVodka());
						}
						else if (args[2].equals("23")) {
							p.getInventory().addItem(ingr.getRum());
						}
						else if (args[2].equals("24")) {
							p.getInventory().addItem(ingr.getTequila());
						}
					}
					else if (args[1].equalsIgnoreCase("overload")) {
						if (args[2].equalsIgnoreCase("weapon")) {
							p.getInventory().addItem(sItems.getWeaponGem(args[3], Integer.parseInt(args[4]), true));
						}
						else if (args[2].equalsIgnoreCase("armor")) {
							p.getInventory().addItem(sItems.getArmorGem(args[3], Integer.parseInt(args[4]), true));
						}
					}
					else if (args[1].equalsIgnoreCase("basic")) {
						if (args[2].equalsIgnoreCase("exp")) {
							p.getInventory().addItem(mItems.getExpCharm(false));
						}
						else if (args[2].equalsIgnoreCase("drop")) {
							p.getInventory().addItem(mItems.getDropCharm(false));
						}
						else if (args[2].equalsIgnoreCase("looting")) {
							p.getInventory().addItem(mItems.getLootingCharm(false));
						}
						else if (args[2].equalsIgnoreCase("traveler")) {
							p.getInventory().addItem(mItems.getTravelerCharm());
						}
						else if (args[2].equalsIgnoreCase("recovery")) {
							p.getInventory().addItem(mItems.getRecoveryCharm());
						}
					}
					else if (args[1].equalsIgnoreCase("advanced")) {
						if (args[2].equalsIgnoreCase("exp")) {
							p.getInventory().addItem(mItems.getExpCharm(true));
						}
						else if (args[2].equalsIgnoreCase("drop")) {
							p.getInventory().addItem(mItems.getDropCharm(true));
						}
						else if (args[2].equalsIgnoreCase("looting")) {
							p.getInventory().addItem(mItems.getLootingCharm(true));
						}
						else if (args[2].equalsIgnoreCase("hunger")) {
							p.getInventory().addItem(mItems.getHungerCharm());
						}
						else if (args[2].equalsIgnoreCase("secondchance")) {
							p.getInventory().addItem(mItems.getSecondChanceCharm());
						}
						else if (args[2].equalsIgnoreCase("quickeat")) {
							p.getInventory().addItem(mItems.getQuickEatCharm());
						}
					}
					return true;
				}
				else {
					p = Bukkit.getPlayer(args[0]);
					// /prof player add [essence/oretype] [level] [amount]
					if (args[1].equalsIgnoreCase("add")) {
						if (args[2].equalsIgnoreCase("randomore")) {
							Random gen = new Random();
							String type = CurrencyManager.types[gen.nextInt(7) + 1];
							int level = util.roundToLevel(Integer.parseInt(args[3]), LEVEL_INTERVAL);
							int amount = Integer.parseInt(args[4]);
							this.main.cManager.add(p, type, level, amount);
							Util.sendMessage(sender, "&7Successfully gave " + amount + " Lv " + level + " " + type + " ore to + " + p.getName() + "!");
							return true;
						}
						else {
							int level = util.roundToLevel(Integer.parseInt(args[3]), LEVEL_INTERVAL);
							int amount = Integer.parseInt(args[4]);
							this.main.cManager.add(p, args[2], level, amount);
							Util.sendMessage(sender, "&7Successfully gave " + amount + " Lv " + level + " " + "essence to + " + p.getName() + "!");
							return true;
						}
					}
					if (args[1].equalsIgnoreCase("get")) {
						if (args[2].equalsIgnoreCase("essence")) {
							p.getInventory().addItem(common.getEssence(Integer.parseInt(args[3]), true));
						}
						else if (args[2].equalsIgnoreCase("augment")) {
							p.getInventory().addItem(AugmentManager.augmentMap.get(args[3].replaceAll("_", " ")).get(Integer.parseInt(args[4])).getItem(p));
						}
						else if (args[2].equalsIgnoreCase("repair")) {
							p.getInventory().addItem(bItems.getRepairItem(Integer.parseInt(args[3])));
						}
						else if (args[2].equalsIgnoreCase("durability")) {
							p.getInventory().addItem(bItems.getDurabilityItem(Integer.parseInt(args[4]), args[3]));
						}
						else if (args[2].equalsIgnoreCase("ore")) {
							int amount = 1;
							if (args.length == 6) {
								amount = Integer.parseInt(args[5]);
							}
							if (StringUtils.isNumeric(args[3])) {
								ItemStack ore = sItems.getOre(Integer.parseInt(args[3]), Integer.parseInt(args[4]));
								ore.setAmount(amount);
								p.getInventory().addItem(ore);
							}
							else {
								ItemStack ore = sItems.getOre(args[3], Integer.parseInt(args[4]));
								ore.setAmount(amount);
								p.getInventory().addItem(ore);
							}
						}
						else if (args[2].equalsIgnoreCase("ingr")) {
							if (args[3].equals("22")) {
								p.getInventory().addItem(ingr.getVodka());
							}
							else if (args[3].equals("23")) {
								p.getInventory().addItem(ingr.getRum());
							}
							else if (args[3].equals("24")) {
								p.getInventory().addItem(ingr.getTequila());
							}
						}
						else if (args[2].equalsIgnoreCase("gem")) {
							if (args[3].equalsIgnoreCase("weapon")) {
								p.getInventory()
										.addItem(sItems.getWeaponGem(args[4], Integer.parseInt(args[5]), false));
							}
							else if (args[3].equalsIgnoreCase("armor")) {
								p.getInventory().addItem(sItems.getArmorGem(args[4], Integer.parseInt(args[5]), false));
							}
						}
						else if (args[2].equalsIgnoreCase("overload")) {
							if (args[3].equalsIgnoreCase("weapon")) {
								p.getInventory().addItem(sItems.getWeaponGem(args[4], Integer.parseInt(args[5]), true));
							}
							else if (args[3].equalsIgnoreCase("armor")) {
								p.getInventory().addItem(sItems.getArmorGem(args[4], Integer.parseInt(args[5]), true));
							}
						}
						else if (args[2].equalsIgnoreCase("basic")) {
							if (args[3].equalsIgnoreCase("exp")) {
								p.getInventory().addItem(mItems.getExpCharm(false));
							}
							else if (args[3].equalsIgnoreCase("drop")) {
								p.getInventory().addItem(mItems.getDropCharm(false));
							}
							else if (args[3].equalsIgnoreCase("looting")) {
								p.getInventory().addItem(mItems.getLootingCharm(false));
							}
							else if (args[3].equalsIgnoreCase("traveler")) {
								p.getInventory().addItem(mItems.getTravelerCharm());
							}
							else if (args[3].equalsIgnoreCase("recovery")) {
								p.getInventory().addItem(mItems.getRecoveryCharm());
							}
						}
						else if (args[2].equalsIgnoreCase("advanced")) {
							if (args[3].equalsIgnoreCase("exp")) {
								p.getInventory().addItem(mItems.getExpCharm(true));
							}
							else if (args[3].equalsIgnoreCase("drop")) {
								p.getInventory().addItem(mItems.getDropCharm(true));
							}
							else if (args[3].equalsIgnoreCase("looting")) {
								p.getInventory().addItem(mItems.getLootingCharm(true));
							}
							else if (args[3].equalsIgnoreCase("hunger")) {
								p.getInventory().addItem(mItems.getHungerCharm());
							}
							else if (args[3].equalsIgnoreCase("secondchance")) {
								p.getInventory().addItem(mItems.getSecondChanceCharm());
							}
							else if (args[3].equalsIgnoreCase("quickeat")) {
								p.getInventory().addItem(mItems.getQuickEatCharm());
							}
						}
					}
					return true;
				}
			}
		}
		return true;
	}
}