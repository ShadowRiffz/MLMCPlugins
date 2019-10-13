package me.Neoblade298.NeoProfessions.Commands;

import org.apache.commons.lang3.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.Damageable;

import com.sucy.skill.SkillAPI;
import com.sucy.skill.api.player.PlayerClass;
import me.Neoblade298.NeoProfessions.Main;
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


public class NeoprofessionsCommands implements CommandExecutor {
	
	Main main;
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
	
	public NeoprofessionsCommands(Main main, BlacksmithMethods b, StonecutterMethods s, CulinarianMethods c, MasonMethods m) {
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
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String lbl, String[] args) {
		
		if(sender.hasPermission("neoprofessions.admin") || sender.isOp()) {
			Player p = null;
			if(sender instanceof Player) {
				p = (Player) sender;
			}
			
			if (args.length == 0) {
				sender.sendMessage("§7- §4/neoprofessions level [playername] <amount>");
				sender.sendMessage("§7- §4/neoprofessions reset [playername]");
				sender.sendMessage("§7- §4/neoprofessions sober [playername]");
				sender.sendMessage("§7- §4/neoprofessions repair [playername]");
				sender.sendMessage("§7- §4/neoprofessions <playername> get essence [level]");
				sender.sendMessage("§7- §4/neoprofessions <playername> get fragment [level]");
				sender.sendMessage("§7- §4/neoprofessions <playername> get repair [level]");
				sender.sendMessage("§7- §4/neoprofessions <playername> get ingr [22-24]");
				sender.sendMessage("§7- §4/neoprofessions <playername> get durability [weapon/armor] [level]");
				sender.sendMessage("§7- §4/neoprofessions <playername> get ore [attribute or 1-7] [level] <amount>");
				sender.sendMessage("§7- §4/neoprofessions <playername> get gem [weapon/armor] [attribute] [level]");
				sender.sendMessage("§7- §4/neoprofessions <playername> get overload [weapon/armor] [attribute] [level]");
				sender.sendMessage("§7- §4/neoprofessions <playername> get [basic/advanced] [charm]");
				return true;
			}
			else {
				// /neoprofessions level playername
				if (args[0].equalsIgnoreCase("sober")) {
					if (args.length == 2) {
						main.culinarianListeners.drunkness.put(Bukkit.getPlayer(args[1]), 0);
						util.sendMessage(Bukkit.getPlayer(args[1]), "&7Successfully sobered!");
					}
				}
				if (args[0].equalsIgnoreCase("repair")) {
					if (args.length == 2) {
						Player target = Bukkit.getPlayer(args[1]);
						ItemStack item = target.getInventory().getItemInMainHand();
						Util util = new Util();
						BlacksmithUtils bUtils = new BlacksmithUtils();
						ItemMeta im = item.getItemMeta();
						((Damageable) im).setDamage(0);
						item.setItemMeta(im);
						if (bUtils.canRepair(item)) {
							util.setCurrentDurability(item, util.getMaxDurability(item));
						}
						util.sendMessage(Bukkit.getPlayer(args[1]), "&7Item repaired successfully!");
					}
				}
				if (args[0].equalsIgnoreCase("level")) {
					if (args.length == 2) {
						PlayerClass pClass = SkillAPI.getPlayerData(Bukkit.getPlayer(args[1])).getClass("profession");
						if (pClass != null) {
							if (pClass.getLevel() <= 60) {
								pClass.setLevel(pClass.getLevel() + 1);
								pClass.setPoints(pClass.getPoints() + 2);
								util.sendMessage(Bukkit.getPlayer(args[1]), "&7Your profession level is now &e" + pClass.getLevel() + "&7!");
							}
						}
					}
					else if (args.length == 3) {
						PlayerClass pClass = SkillAPI.getPlayerData(Bukkit.getPlayer(args[1])).getClass("profession");
						int levels = Integer.parseInt(args[2]);
						if (pClass != null) {
							if (pClass.getLevel() + levels <= 60) {
								pClass.setLevel(pClass.getLevel() + levels);
								pClass.setPoints(pClass.getPoints() + (2 * levels));
								util.sendMessage(Bukkit.getPlayer(args[1]), "&4[&c&lMLMC&4] &7Your profession level is now &e" + pClass.getLevel() + "&7!");
							}
						}
					}
				}
				else if (args[0].equalsIgnoreCase("reset")) {
					PlayerClass pClass = SkillAPI.getPlayerData(Bukkit.getPlayer(args[1])).getClass("profession");
					if (pClass != null) {
						if (pClass.getData().getName().equalsIgnoreCase("Blacksmith")) {
							blacksmithMethods.resetPlayer(Bukkit.getPlayer(args[1]));
						}
						else if (pClass.getData().getName().equalsIgnoreCase("Stonecutter")) {
							stonecutterMethods.resetPlayer(Bukkit.getPlayer(args[1]));
						}
						else if (pClass.getData().getName().equalsIgnoreCase("Culinarian")) {
							culinarianMethods.resetPlayer(Bukkit.getPlayer(args[1]));
						}
						else if (pClass.getData().getName().equalsIgnoreCase("Mason")) {
							masonMethods.resetPlayer(Bukkit.getPlayer(args[1]));
						}
					}
				}
				else if (args[0].equalsIgnoreCase("get")) {
					if(args[1].equalsIgnoreCase("essence")) {
						p.getInventory().addItem(common.getEssence(Integer.parseInt(args[2])));
					}
					else if(args[1].equalsIgnoreCase("fragment")) {
						p.getInventory().addItem(common.getEssenceFragment(Integer.parseInt(args[2])));
					}
					else if(args[1].equalsIgnoreCase("repair")) {
						p.getInventory().addItem(bItems.getRepairItem(Integer.parseInt(args[2])));
					}
					else if(args[1].equalsIgnoreCase("durability")) {
						p.getInventory().addItem(bItems.getDurabilityItem(Integer.parseInt(args[3]), args[2]));
					}
					else if(args[1].equalsIgnoreCase("ore")) {
						int amount = 1;
						if (args.length == 5) {
							amount = Integer.parseInt(args[4]);
						}
						if (StringUtils.isNumeric(args[2])) {
							p.getInventory().addItem();
						}
						else {
							p.getInventory().addItem(sItems.getOre(args[2], Integer.parseInt(args[3])));
						}
					}
					else if(args[1].equalsIgnoreCase("gem")) {
						if(args[2].equalsIgnoreCase("weapon")) {
							p.getInventory().addItem(sItems.getWeaponGem(args[3], Integer.parseInt(args[4]), false));
						}
						else if(args[2].equalsIgnoreCase("armor")) {
							p.getInventory().addItem(sItems.getArmorGem(args[3], Integer.parseInt(args[4]), false));
						}
					}
					else if(args[1].equalsIgnoreCase("ingr")) {
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
					else if(args[1].equalsIgnoreCase("overload")) {
						if(args[2].equalsIgnoreCase("weapon")) {
							p.getInventory().addItem(sItems.getWeaponGem(args[3], Integer.parseInt(args[4]), true));
						}
						else if(args[2].equalsIgnoreCase("armor")) {
							p.getInventory().addItem(sItems.getArmorGem(args[3], Integer.parseInt(args[4]), true));
						}
					}
					else if(args[1].equalsIgnoreCase("basic")) {
						if(args[2].equalsIgnoreCase("exp")) {
							p.getInventory().addItem(mItems.getExpCharm(false));
						}
						else if(args[2].equalsIgnoreCase("drop")) {
							p.getInventory().addItem(mItems.getDropCharm(false));
						}
						else if(args[2].equalsIgnoreCase("looting")) {
							p.getInventory().addItem(mItems.getLootingCharm(false));
						}
						else if(args[2].equalsIgnoreCase("traveler")) {
							p.getInventory().addItem(mItems.getTravelerCharm());
						}
						else if(args[2].equalsIgnoreCase("recovery")) {
							p.getInventory().addItem(mItems.getRecoveryCharm());
						}
					}
					else if(args[1].equalsIgnoreCase("advanced")) {
						if(args[2].equalsIgnoreCase("exp")) {
							p.getInventory().addItem(mItems.getExpCharm(true));
						}
						else if(args[2].equalsIgnoreCase("drop")) {
							p.getInventory().addItem(mItems.getDropCharm(true));
						}
						else if(args[2].equalsIgnoreCase("looting")) {
							p.getInventory().addItem(mItems.getLootingCharm(true));
						}
						else if(args[2].equalsIgnoreCase("hunger")) {
							p.getInventory().addItem(mItems.getHungerCharm());
						}
						else if(args[2].equalsIgnoreCase("secondchance")) {
							p.getInventory().addItem(mItems.getSecondChanceCharm());
						}
						else if(args[2].equalsIgnoreCase("quickeat")) {
							p.getInventory().addItem(mItems.getQuickEatCharm());
						}
					}
				}
				else {
					p = Bukkit.getPlayer(args[0]);
					if (args[1].equalsIgnoreCase("get")) {
						if(args[2].equalsIgnoreCase("essence")) {
							p.getInventory().addItem(common.getEssence(Integer.parseInt(args[3])));
						}
						else if(args[2].equalsIgnoreCase("fragment")) {
							p.getInventory().addItem(common.getEssenceFragment(Integer.parseInt(args[3])));
						}
						else if(args[2].equalsIgnoreCase("repair")) {
							p.getInventory().addItem(bItems.getRepairItem(Integer.parseInt(args[3])));
						}
						else if(args[2].equalsIgnoreCase("durability")) {
							p.getInventory().addItem(bItems.getDurabilityItem(Integer.parseInt(args[4]), args[3]));
						}
						else if(args[2].equalsIgnoreCase("ore")) {
							if (StringUtils.isNumeric(args[3])) {
								p.getInventory().addItem(sItems.getOre(Integer.parseInt(args[3]), Integer.parseInt(args[4])));
							}
							else {
								p.getInventory().addItem(sItems.getOre(args[3], Integer.parseInt(args[4])));
							}
						}
						else if(args[1].equalsIgnoreCase("ingr")) {
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
						else if(args[2].equalsIgnoreCase("gem")) {
							if(args[3].equalsIgnoreCase("weapon")) {
								p.getInventory().addItem(sItems.getWeaponGem(args[4], Integer.parseInt(args[5]), false));
							}
							else if(args[3].equalsIgnoreCase("armor")) {
								p.getInventory().addItem(sItems.getArmorGem(args[4], Integer.parseInt(args[5]), false));
							}
						}
						else if(args[2].equalsIgnoreCase("overload")) {
							if(args[3].equalsIgnoreCase("weapon")) {
								p.getInventory().addItem(sItems.getWeaponGem(args[4], Integer.parseInt(args[5]), true));
							}
							else if(args[3].equalsIgnoreCase("armor")) {
								p.getInventory().addItem(sItems.getArmorGem(args[4], Integer.parseInt(args[5]), true));
							}
						}
						else if(args[2].equalsIgnoreCase("basic")) {
							if(args[3].equalsIgnoreCase("exp")) {
								p.getInventory().addItem(mItems.getExpCharm(false));
							}
							else if(args[3].equalsIgnoreCase("drop")) {
								p.getInventory().addItem(mItems.getDropCharm(false));
							}
							else if(args[3].equalsIgnoreCase("looting")) {
								p.getInventory().addItem(mItems.getLootingCharm(false));
							}
							else if(args[3].equalsIgnoreCase("traveler")) {
								p.getInventory().addItem(mItems.getTravelerCharm());
							}
							else if(args[3].equalsIgnoreCase("recovery")) {
								p.getInventory().addItem(mItems.getRecoveryCharm());
							}
						}
						else if(args[2].equalsIgnoreCase("advanced")) {
							if(args[3].equalsIgnoreCase("exp")) {
								p.getInventory().addItem(mItems.getExpCharm(true));
							}
							else if(args[3].equalsIgnoreCase("drop")) {
								p.getInventory().addItem(mItems.getDropCharm(true));
							}
							else if(args[3].equalsIgnoreCase("looting")) {
								p.getInventory().addItem(mItems.getLootingCharm(true));
							}
							else if(args[3].equalsIgnoreCase("hunger")) {
								p.getInventory().addItem(mItems.getHungerCharm());
							}
							else if(args[3].equalsIgnoreCase("secondchance")) {
								p.getInventory().addItem(mItems.getSecondChanceCharm());
							}
							else if(args[3].equalsIgnoreCase("quickeat")) {
								p.getInventory().addItem(mItems.getQuickEatCharm());
							}
						}
					}
				}
				return true;
			}
		}
		else {
			util.sendMessage((Player)sender, "&cYou are not an admin!");
			return true;
		}
	}
}