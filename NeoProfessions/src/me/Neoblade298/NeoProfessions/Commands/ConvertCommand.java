package me.Neoblade298.NeoProfessions.Commands;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.logging.Level;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.scheduler.BukkitRunnable;

import me.Neoblade298.NeoProfessions.Professions;
import me.Neoblade298.NeoProfessions.Managers.CurrencyManager;
import me.Neoblade298.NeoProfessions.Managers.ProfessionManager;
import me.Neoblade298.NeoProfessions.Managers.RecipeManager;
import me.Neoblade298.NeoProfessions.PlayerProfessions.Profession;
import me.Neoblade298.NeoProfessions.PlayerProfessions.ProfessionType;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.model.user.User;
import net.luckperms.api.model.user.UserManager;


public class ConvertCommand implements CommandExecutor {
	Professions main;
	ArrayList<String> currencyTypes = new ArrayList<String>();
	HashMap<String, String> legendaryRecipes = new HashMap<String, String>();
	HashMap<Integer, Integer> essenceLimit = new HashMap<Integer, Integer>();
	
	public ConvertCommand(Professions main) {
		this.main = main;
		currencyTypes.add("essence");
		currencyTypes.add("ruby");
		currencyTypes.add("amethyst");
		currencyTypes.add("sapphire");
		currencyTypes.add("emerald");
		currencyTypes.add("topaz");
		currencyTypes.add("garnet");
		currencyTypes.add("adamantium");
		
		legendaryRecipes.put("recipes.tlegend1", "DragonScrambledEggs");
		legendaryRecipes.put("recipes.tlegend2", "NeosFullCourseSpecial");
		legendaryRecipes.put("recipes.tlegend3", "UltimatoeRoastedSoup");
		legendaryRecipes.put("recipes.tlegend4", "ShanasPokeBowl");
		legendaryRecipes.put("recipes.tlegend5", "MattiforniaRoll");
		legendaryRecipes.put("recipes.tlegend6", "JJJawbreaker");
		legendaryRecipes.put("recipes.tlegend7", "SupersSundae");
		legendaryRecipes.put("recipes.tlegend8", "CommunityCake");
		legendaryRecipes.put("recipes.tlegend9", "MonasBobaTea");
		legendaryRecipes.put("recipes.tlegend10", "RandumsCheesecake");
		
		essenceLimit.put(5, 2500);
		essenceLimit.put(10, 2000);
		essenceLimit.put(15, 1500);
		essenceLimit.put(20, 1000);
		essenceLimit.put(25, 1000);
		essenceLimit.put(30, 1000);
		essenceLimit.put(35, 1000);
		essenceLimit.put(40, 1000);
		essenceLimit.put(45, 1000);
		essenceLimit.put(50, 1000);
		essenceLimit.put(55, 1000);
		essenceLimit.put(60, 1000);
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String lbl, String[] args) {
		if (args.length == 0 && sender.hasPermission("neoprofessions.admin")) {
			sender.sendMessage("Starting conversion of professions...");
			convertAll();
			return true;
		}
		return false;
	}
	
	private void convertAll() {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			Connection con = DriverManager.getConnection(Professions.connection, Professions.sqlUser, Professions.sqlPass);
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM neoprofessions_currency;");
			
			new BukkitRunnable() {
				public void run() {
					HashMap<String, HashMap<Integer, Integer>> currencies = new HashMap<String, HashMap<Integer, Integer>>();
				    RegisteredServiceProvider<LuckPerms> provider = Bukkit.getServicesManager().getRegistration(LuckPerms.class);
			        LuckPerms api = provider.getProvider();
					UserManager mngr = api.getUserManager();
					try {	
						Statement stmt = con.createStatement();
						int count = 0;
						int skipped = 0;
						while (rs.next()) {
							UUID uuid = UUID.fromString(rs.getString(1));
							try {
								currencies.put("essence", parseLine(rs.getString(2)));
								currencies.put("ruby", parseLine(rs.getString(3)));
								currencies.put("amethyst", parseLine(rs.getString(4)));
								currencies.put("sapphire", parseLine(rs.getString(5)));
								currencies.put("emerald", parseLine(rs.getString(6)));
								currencies.put("topaz", parseLine(rs.getString(7)));
								currencies.put("garnet", parseLine(rs.getString(8)));
								currencies.put("adamantium", parseLine(rs.getString(9)));
								
								// If everything is empty, skip it
								boolean empty = true;
								for (String type : currencyTypes) {
									if (!currencies.get(type).isEmpty()) {
										empty = false;
										break;
									}
								}
								if (empty) {
									skipped++;
									continue;
								}
								
								// Convert essence first
								CurrencyManager.convertPlayer(uuid, currencies.get("essence"), stmt);
								
								// Convert ores into exp
								HashMap<ProfessionType, Profession> profs = new HashMap<ProfessionType, Profession>();
								for (ProfessionType prof : ProfessionType.values()) {
									profs.put(prof, new Profession(prof));
								}
								profs.get(ProfessionType.HARVESTER).convertExp(currencies.get("ruby"), 2);
								profs.get(ProfessionType.HARVESTER).convertExp(currencies.get("amethyst"), 2);
								profs.get(ProfessionType.LOGGER).convertExp(currencies.get("emerald"), 2);
								profs.get(ProfessionType.LOGGER).convertExp(currencies.get("sapphire"), 2);
								profs.get(ProfessionType.STONECUTTER).convertExp(currencies.get("topaz"), 2);
								profs.get(ProfessionType.STONECUTTER).convertExp(currencies.get("garnet"), 2);
								profs.get(ProfessionType.CRAFTER).convertExp(currencies.get("adamantium"), 1);
								ProfessionManager.convertPlayer(uuid, profs, stmt);
								
								stmt.executeBatch();

								CompletableFuture<User> userFuture = mngr.loadUser(uuid);
								HashSet<String> knowledge = new HashSet<String>();
								userFuture.thenAcceptAsync(user -> {
									user.getNodes().stream()
									.filter(e -> legendaryRecipes.containsKey(e.getKey()))
									.forEach(e -> knowledge.add(legendaryRecipes.get(e.getKey())));
									RecipeManager.convertPlayer(uuid, knowledge, stmt);
								});
								
								count++;
								if (count % 50 == 0 && count != 0) {
									Bukkit.getLogger().log(Level.INFO, "[NeoProfessions] Completed " + count + " conversions. Skipped " + skipped + ".");
								}
							}
							catch (Exception e) {
								Bukkit.getLogger().log(Level.WARNING, "[NeoProfessions] Failed to convert UUID: " + uuid);
								e.printStackTrace();
								this.cancel();
								return;
							}
						}
						Bukkit.getLogger().log(Level.INFO, "[NeoProfessions] Done! A total of " + count + " conversions. Skipped " + skipped + ".");
					}
					catch (Exception ex) {
						ex.printStackTrace();
					}
				}
			}.runTaskAsynchronously(main);

			// Save account
		} catch (Exception exception) {
			exception.printStackTrace();
		}
	}
	
	private HashMap<Integer, Integer> parseLine(String line) {
		HashMap<Integer, Integer> currency = new HashMap<Integer, Integer>();
		String[] args = line.split(":");
		int count = 0;
		for (int i = 5; i <= 60; i += 5) {
			int num = Integer.parseInt(args[count++]);
			if (num > 0) {
				currency.put(i, Math.min(num, essenceLimit.get(i)));
			}
		}
		return currency;
	}
}