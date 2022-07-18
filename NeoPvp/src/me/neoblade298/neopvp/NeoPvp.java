package me.neoblade298.neopvp;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.flags.registry.FlagConflictException;
import com.sk89q.worldguard.protection.flags.registry.FlagRegistry;

import me.neoblade298.neocore.NeoCore;
import me.neoblade298.neocore.commands.CommandManager;
import me.neoblade298.neocore.interfaces.Manager;
import me.neoblade298.neopvp.commands.*;
import me.neoblade298.neopvp.generators.GeneratorManager;
import net.md_5.bungee.api.ChatColor;

public class NeoPvp extends JavaPlugin {
	private static NeoPvp inst;
	private static ArrayList<Manager> mngrs = new ArrayList<Manager>();
	public static StateFlag PROTECTION_ALLOWED_FLAG;
	
	public void onEnable() {
		Bukkit.getServer().getLogger().info("NeoPvp Enabled");
		inst = this;
		mngrs.add(new GeneratorManager());
		initCommands();
		
		NeoCore.registerIOComponent(this, new PvpManager());
		// WorldGuard
	    //SessionManager sessionManager = WorldGuard.getInstance().getPlatform().getSessionManager();
	    //sessionManager.registerHandler(RequiredTagFlagHandler.FACTORY, null);
	}
	
	private void initCommands() {
		CommandManager mngr = new CommandManager("adminwar", "mycommand.staff", ChatColor.DARK_RED, this);
		mngr.registerCommandList("");
		mngr.register(new CmdAWarCreate(), new CmdAWarDisplay(),
				new CmdAWarInfo(), new CmdAWarLimit(), new CmdAWarNew(), new CmdAWarTeam1(),
				new CmdAWarTeam2());
		
		mngr = new CommandManager("pvp", this);
		mngr.register(new CmdPvpBase());
		mngr.register(new CmdPvpRemoveProtection());
		
		mngr = new CommandManager("adminpvp", "mycommand.staff", ChatColor.DARK_RED, this);
		mngr.register(new CmdAPvpAddProtection());
		mngr.register(new CmdAPvpRemoveProtection());
	}
	
	@Override
	public void onLoad() {
		// WorldGuard
		FlagRegistry registry = WorldGuard.getInstance().getFlagRegistry();
	    try {
	        // create a flag with the name "my-custom-flag"
	        StateFlag flag = new StateFlag("protection-allowed", false);
	        registry.register(flag);
	        PROTECTION_ALLOWED_FLAG = flag; // only set our field if there was no error
	    } catch (FlagConflictException e) {
	        // some other plugin registered a flag by the same name already.
	        // you can use the existing flag, but this may cause conflicts - be sure to check type
	    	e.printStackTrace();
	    	
	    } catch (IllegalStateException e) {
	    	e.printStackTrace();
	    }
	}
	
	public void onDisable() {
	    org.bukkit.Bukkit.getServer().getLogger().info("NeoPvp Disabled");
	    super.onDisable();
	}
	
	public static NeoPvp inst() {
		return inst;
	}
}
