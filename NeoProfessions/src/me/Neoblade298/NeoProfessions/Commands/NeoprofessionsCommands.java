package me.Neoblade298.NeoProfessions.Commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.sucy.skill.SkillAPI;
import com.sucy.skill.api.player.PlayerClass;
import me.Neoblade298.NeoProfessions.Main;
import me.Neoblade298.NeoProfessions.Items.BlacksmithItems;
import me.Neoblade298.NeoProfessions.Items.CommonItems;
import me.Neoblade298.NeoProfessions.Items.MasonItems;
import me.Neoblade298.NeoProfessions.Items.StonecutterItems;
import me.Neoblade298.NeoProfessions.Methods.BlacksmithMethods;
import me.Neoblade298.NeoProfessions.Utilities.Util;


public class NeoprofessionsCommands implements CommandExecutor {
	
	Main main;
	BlacksmithMethods blacksmithMethods;
	
	public NeoprofessionsCommands(Main main) {
		this.main = main;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String lbl, String[] args) {
		
		if(sender.hasPermission("neoprofessions.admin")) {
			Player p = null;
			if(sender instanceof Player) {
				p = (Player) sender;
			}
			
			if (args.length == 0) {
				if(sender.hasPermission("blacksmith.admin")) {
					Util.sendMessage(p, "&7- &4/neoprofessions get essence [level]");
					Util.sendMessage(p, "&7- &4/neoprofessions get fragment [level]");
					Util.sendMessage(p, "&7- &4/neoprofessions get repair [level]");
					Util.sendMessage(p, "&7- &4/neoprofessions get durability [weapon/armor] [level]");
					Util.sendMessage(p, "&7- &4/neoprofessions get ore [attribute] [level]");
					Util.sendMessage(p, "&7- &4/neoprofessions get gem [weapon/armor] [attribute] [level]");
					Util.sendMessage(p, "&7- &4/neoprofessions get overload [weapon/armor] [attribute] [level]");
					Util.sendMessage(p, "&7- &4/neoprofessions get [basic/advanced] [charm]");
				}
				return true;
			}
			else {
				// /neoprofessions level playername
				if (args[0].equalsIgnoreCase("level")) {
					PlayerClass pClass = SkillAPI.getPlayerData(Bukkit.getPlayer(args[1])).getClass("profession");
					pClass.setLevel(pClass.getLevel() + 1);
					pClass.setPoints(pClass.getPoints() + 2);
				}
				else if (args[0].equalsIgnoreCase("get")) {
					if(args[1].equalsIgnoreCase("essence")) {
						p.getInventory().addItem(CommonItems.getEssence(Integer.parseInt(args[2])));
					}
					else if(args[1].equalsIgnoreCase("fragment")) {
						p.getInventory().addItem(CommonItems.getEssenceFragment(Integer.parseInt(args[2])));
					}
					else if(args[1].equalsIgnoreCase("repair")) {
						p.getInventory().addItem(BlacksmithItems.getRepairItem(Integer.parseInt(args[2])));
					}
					else if(args[1].equalsIgnoreCase("durability")) {
						p.getInventory().addItem(BlacksmithItems.getDurabilityItem(Integer.parseInt(args[3]), args[2]));
					}
					else if(args[1].equalsIgnoreCase("ore")) {
						p.getInventory().addItem(StonecutterItems.getOre(args[2], Integer.parseInt(args[3])));
					}
					else if(args[1].equalsIgnoreCase("gem")) {
						if(args[2].equalsIgnoreCase("weapon")) {
							p.getInventory().addItem(StonecutterItems.getWeaponGem(args[3], Integer.parseInt(args[4]), false));
						}
						else if(args[2].equalsIgnoreCase("armor")) {
							p.getInventory().addItem(StonecutterItems.getArmorGem(args[3], Integer.parseInt(args[4]), false));
						}
					}
					else if(args[1].equalsIgnoreCase("overload")) {
						if(args[2].equalsIgnoreCase("weapon")) {
							p.getInventory().addItem(StonecutterItems.getWeaponGem(args[3], Integer.parseInt(args[4]), true));
						}
						else if(args[2].equalsIgnoreCase("armor")) {
							p.getInventory().addItem(StonecutterItems.getArmorGem(args[3], Integer.parseInt(args[4]), true));
						}
					}
					else if(args[1].equalsIgnoreCase("basic")) {
						if(args[2].equalsIgnoreCase("exp")) {
							p.getInventory().addItem(MasonItems.getExpCharm(false));
						}
						else if(args[2].equalsIgnoreCase("drop")) {
							p.getInventory().addItem(MasonItems.getDropCharm(false));
						}
						else if(args[2].equalsIgnoreCase("looting")) {
							p.getInventory().addItem(MasonItems.getLootingCharm(false));
						}
						else if(args[2].equalsIgnoreCase("traveler")) {
							p.getInventory().addItem(MasonItems.getTravelerCharm());
						}
						else if(args[2].equalsIgnoreCase("recovery")) {
							p.getInventory().addItem(MasonItems.getRecoveryCharm());
						}
					}
					else if(args[1].equalsIgnoreCase("advanced")) {
						if(args[2].equalsIgnoreCase("exp")) {
							p.getInventory().addItem(MasonItems.getExpCharm(true));
						}
						else if(args[2].equalsIgnoreCase("drop")) {
							p.getInventory().addItem(MasonItems.getDropCharm(true));
						}
						else if(args[2].equalsIgnoreCase("looting")) {
							p.getInventory().addItem(MasonItems.getLootingCharm(true));
						}
						else if(args[2].equalsIgnoreCase("hunger")) {
							p.getInventory().addItem(MasonItems.getHungerCharm());
						}
						else if(args[2].equalsIgnoreCase("secondchance")) {
							p.getInventory().addItem(MasonItems.getSecondChanceCharm());
						}
						else if(args[2].equalsIgnoreCase("quickeat")) {
							p.getInventory().addItem(MasonItems.getQuickEatCharm());
						}
					}
				}
				return true;
			}
		}
		else {
			Util.sendMessage((Player)sender, "&cYou are not an admin!");
			return true;
		}
	}
}