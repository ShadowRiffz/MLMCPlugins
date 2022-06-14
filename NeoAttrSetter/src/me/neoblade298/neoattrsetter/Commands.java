package me.neoblade298.neoattrsetter;

import java.util.ArrayList;

import org.apache.commons.lang.StringUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.sucy.skill.SkillAPI;
import com.sucy.skill.api.player.PlayerData;


public class Commands implements CommandExecutor{
	
	Main main;
	ArrayList<String> validAttrs;
	
	public Commands(Main main) {
		this.main = main;
		validAttrs = new ArrayList<String>();
		validAttrs.add("strength");
		validAttrs.add("dexterity");
		validAttrs.add("intelligence");
		validAttrs.add("spirit");
		validAttrs.add("endurance");
		validAttrs.add("perception");
		validAttrs.add("str");
		validAttrs.add("dex");
		validAttrs.add("int");
		validAttrs.add("spr");
		validAttrs.add("prc");
		validAttrs.add("end");
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String lbl, String[] args) {
		
		if(args.length > 0) {
			if(sender instanceof Player) {
				Player p = (Player) sender;
				if(p.getWorld().getName().equals("Argyll") ||
						p.getWorld().getName().equals("ClassPVP") ||
						p.getWorld().getName().equals("Dev")) {
					PlayerData data = SkillAPI.getPlayerData(p);
					
					// attrs reset
					if(args.length == 1 && args[0].equalsIgnoreCase("reset")) {
						data.refundAttributes();
						msgP(p, "&7Success! All attributes refunded.");
						return true;
					}
					// attrs set [attr] [amt]
					else if(args.length == 3 && args[0].equalsIgnoreCase("set")) {
						if(validAttrs.contains(args[1].toLowerCase())) {
							// fix shortened attrs
							String attr = args[1].toLowerCase();
							if(args[1].equalsIgnoreCase("str")) {
								attr = "strength";
							}
							if(args[1].equalsIgnoreCase("dex")) {
								attr = "dexterity";
							}
							if(args[1].equalsIgnoreCase("int")) {
								attr = "intelligence";
							}
							if(args[1].equalsIgnoreCase("spr")) {
								attr = "spirit";
							}
							if(args[1].equalsIgnoreCase("prc")) {
								attr = "perception";
							}
							if(args[1].equalsIgnoreCase("end")) {
								attr = "endurance";
							}
							
							if(StringUtils.isNumeric(args[2])) {
								int newNum = Integer.parseInt(args[2]);
								if(newNum >= 0) {
									int oldNum = data.getInvestedAttribute(attr);
									int diff = newNum - oldNum;
									if(diff > 0) {
										if(data.getAttributePoints() >= diff) {
											for(int i = diff; i > 0; i--) {
												data.upAttribute(attr);
											}
											msgP(p, "&7Success! Attribute set.");
											return true;
										}
										else {
											msgP(p, "&cNot enough attribute points to invest!");
											return true;
										}
									}
									else if(diff < 0) {
										for(int i = diff; i < 0; i++) {
											data.refundAttribute(attr);
										}
										msgP(p, "&7Success! Attribute set.");
										return true;
									}
								}
								else {
									msgP(p, "&cThe number you specify must be greater than or equal to 0!");
									return true;
								}
							}
							else {
								msgP(p, "&cYou must specify a number to set the attribute to!");
								return true;
							}
						}
						else {
							msgP(p, "&cInvalid attribute!");
							return true;
						}
					}
					else {
						msgP(p, "&cInvalid command!");
						return true;
					}
					
				}
				else {
					msgP(p, "&cYou may only adjust attributes in the quest world");
					return true;
				}
			}
			else {
				return false;
			}
		}
		
		if(args.length == 0) {
			msg(sender, "&7[&c&lAttribute Setter&7]");
			msg(sender, "&7- &c/attrs &7- Help menu");
			msg(sender, "&7- &c/attrs set [attr] [amt] &7- Sets an attribute to the desired amount");
			msg(sender, "&7- &c/attrs reset &7- Resets all attributes to 0");
			return true;
		}
		
		return true;
		
	}
	
	public void msg(CommandSender p, String msg) {
		p.sendMessage(msg.replaceAll("&", "§"));
	}
	public void msgP(CommandSender p, String msg) {
		p.sendMessage("§4[§c§lMLMC§4] " + msg.replaceAll("&", "§"));
	}
}