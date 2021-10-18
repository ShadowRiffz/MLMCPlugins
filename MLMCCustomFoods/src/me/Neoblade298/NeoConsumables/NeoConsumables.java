package me.Neoblade298.NeoConsumables;

import com.sucy.skill.SkillAPI;
import com.sucy.skill.api.util.FlagManager;
import com.sucy.skill.api.util.StatusFlag;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

public class NeoConsumables extends JavaPlugin implements Listener {
	HashMap<String, Consumable> foods = new HashMap<String, Consumable>();
	// These runnables take away attributes from players when they're done being used
	HashMap<UUID, HashMap<Consumable, BukkitTask>> attributes = new HashMap<UUID, HashMap<Consumable, BukkitTask>>();
	HashMap<UUID, Long> globalCooldowns = new HashMap<UUID, Long>();
	HashMap<UUID, HashMap<Consumable, Long>> foodCooldowns = new HashMap<UUID, HashMap<Consumable, Long>>();
	boolean isInstance = false;

	public void onEnable() {
		File file = new File(getDataFolder(), "foods.yml");
		if (!file.exists()) {
			saveResource("foods.yml", false);
		}
		isInstance = new File(getDataFolder(), "instance.yml").exists();
		loadConfigs();
		
		Bukkit.getPluginManager().registerEvents(this, this);
	}
	
	public void loadConfigs() {
		foods.clear();
		
		for (File file : new File(getDataFolder(), "consumables").listFiles()) {
			FileConfiguration itemConfig = YamlConfiguration.loadConfiguration(file);
			for (String s : itemConfig.getKeys(false)) {
				String name = itemConfig.getString(s + ".name").replaceAll("&", "§");
				Consumable cons = new Consumable(this, name);
				
				// Lore
				ArrayList<String> lore = new ArrayList<String>();
				for (String loreLine : itemConfig.getStringList(s + ".lore")) {
					lore.add(loreLine.replaceAll("&", "§"));
				}
				cons.setLore(lore);
				
				// Potion effects
				ArrayList<PotionEffect> potions = new ArrayList<PotionEffect>();
				PotionEffectType type;
				for (String potion : itemConfig.getStringList(s + ".effects")) {
					String[] split = potion.split(",");
					type = PotionEffectType.getByName(split[0]);
					int amp = Integer.parseInt(split[1]);
					int duration = Integer.parseInt(split[2]) * 20;
					PotionEffect effect = new PotionEffect(type, duration, amp);
					potions.add(effect);
				}
				cons.setPotions(potions);
				
				// Attributes
				Attributes attribs = new Attributes();
				for (String potion : itemConfig.getStringList(s + ".attributes")) {
					String[] split = potion.split(",");
					String attr = split[0];
					int amp = Integer.parseInt(split[1]);
					attribs.addAttribute(attr, amp);
				}
				if (!attribs.isEmpty()) {
					cons.setAttributes(attribs);
				}
				
				ArrayList<Sound> sounds = new ArrayList<Sound>();
				for (String sname : itemConfig.getStringList(s + ".sound-effects")) {
					Sound sound = Sound.valueOf(sname.toUpperCase());
					if (sound != null) {
						sounds.add(sound);
					}
				}
				cons.setSounds(sounds);

				cons.setSaturation(itemConfig.getDouble(s + ".saturation"));
				cons.setHunger(itemConfig.getInt(s + ".hunger"));
				cons.setHealth(itemConfig.getInt(s + ".health.amount"));
				cons.setHealthDelay(itemConfig.getInt(s + ".health.delay"));
				cons.setHealthTime(itemConfig.getInt(s + ".health.repetitions"));
				cons.setMana(itemConfig.getInt(s + ".mana.amount"));
				cons.setManaDelay(itemConfig.getInt(s + ".mana.delay"));
				cons.setManaTime(itemConfig.getInt(s + ".mana.repetitions"));
				cons.setCooldown(itemConfig.getLong(s + ".cooldown"));
				cons.setCommands(itemConfig.getStringList(s + ".commands"));
				cons.setWorlds(itemConfig.getStringList(s + ".worlds"));
				cons.setIgnoreGcd(itemConfig.getBoolean(s + ".ignore-gcd"));
				cons.setType(ConsumableType.fromString(itemConfig.getString(s + ".type")));
				foods.put(name, cons);
			}
		}
	}

	@EventHandler
	public void onPlayerInteractEvent(PlayerInteractEvent e) {
		final Player p = e.getPlayer();
		if ((!e.getAction().equals(Action.RIGHT_CLICK_AIR)) && (!e.getAction().equals(Action.RIGHT_CLICK_BLOCK))) {
			return;
		}
		if (!e.getHand().equals(EquipmentSlot.HAND)) {
			return;
		}
		if (!SkillAPI.isLoaded(p)) {
			return;
		}
		ItemStack item = p.getInventory().getItemInMainHand();
		if ((!item.hasItemMeta()) || (!item.getItemMeta().hasDisplayName())) {
			return;
		}
		
		ItemMeta meta = item.getItemMeta();
		boolean quickEat = false;
		if (meta.hasLore()) {
			for (String line : meta.getLore()) {
				if (line.contains("Quick Eat") && !item.getType().equals(Material.PRISMARINE_CRYSTALS)) {
					quickEat = true;
				}
			}
		}

		// Find the food in the inv
		Consumable food = null;
		if (quickEat) {
			ItemStack[] contents = p.getInventory().getContents();
			for (int i = 0; i < 36; i++) {
				ItemStack invItem = contents[i];
				if (invItem != null && invItem.hasItemMeta() && invItem.getItemMeta().hasLore()) {
					ItemMeta invMeta = invItem.getItemMeta();
					if (!foods.containsKey(invMeta.getDisplayName())) {
						continue;
					}
					food = foods.get(invMeta.getDisplayName());
					if (!food.isSimilar(invMeta)) {
						continue;
					}
					if (!food.canEat(p)) {
						continue;
					}
					break;
				}
			}
		}
		else {
			if (!foods.containsKey(meta.getDisplayName())) {
				food = foods.get(meta.getDisplayName());
				if (!food.isSimilar(meta)) {
					return;
				}
				if (!food.canEat(p)) {
					return;
				}
			}
		}
		
		if (food == null) {
			return;
		}
		// Food can be eaten, calculate multipliers and remove flags
		double garnishMultiplier = 1, preserveMultiplier = 1, spiceMultiplier = 1;
		for (String line : meta.getLore()) {
			if (line.contains("Garnished")) {
				String toParse = line.substring(line.indexOf('(') + 1, line.indexOf('x'));
				garnishMultiplier = Double.parseDouble(toParse);
			}
			if (line.contains("Preserved")) {
				String toParse = line.substring(line.indexOf('(') + 1, line.indexOf('x'));
				preserveMultiplier = Double.parseDouble(toParse);
			}
			if (line.contains("Spiced")) {
				String toParse = line.substring(line.indexOf('(') + 1, line.indexOf('x'));
				spiceMultiplier = Double.parseDouble(toParse);
			}
			if (line.contains("Remedies")) {
				if (line.contains("Remedies stun")) {
					FlagManager.removeFlag(p, StatusFlag.STUN);
				}
				else if (line.contains("Remedies curse")) {
					FlagManager.removeFlag(p, "curse");
				}
				else if (line.contains("Remedies root")) {
					FlagManager.removeFlag(p, StatusFlag.ROOT);
				}
				else if (line.contains("Remedies silence")) {
					FlagManager.removeFlag(p, StatusFlag.SILENCE);
				}
			}
		}
		
		// Use consumable
		food.eat(p, garnishMultiplier, spiceMultiplier, preserveMultiplier);
		ItemStack clone = item.clone();
		clone.setAmount(1);
		p.getInventory().removeItem(clone);
		e.setCancelled(true);
	}

	@EventHandler
	public void onPlayerQuitEvent(PlayerQuitEvent e) {
		// Add way to delete attribute tasks
	}

	@EventHandler
	public void onPlayerJoinEvent(PlayerLoginEvent e) {
		Player p = e.getPlayer();
		if (isInstance) {
			// Remove cooldowns for food if joining a boss instance
			UUID uuid = p.getUniqueId();
			foodCooldowns.put(uuid, new HashMap<Consumable, Long>());
			attributes.put(uuid, new HashMap<Consumable, BukkitTask>());
			globalCooldowns.remove(uuid);
		}
	}

	@EventHandler
	public void onPlayerPlace(BlockPlaceEvent e) {
		ItemStack item = e.getItemInHand();
		if (item.hasItemMeta() && item.getItemMeta().hasLore()
				&& item.getItemMeta().getLore().get(0).contains("Potential Rewards")) {
			e.setCancelled(true);
		}
	}

	public boolean isOffGcd(Player p) {
		if (this.globalCooldowns.containsKey(p.getUniqueId())) {
			return System.currentTimeMillis() > this.globalCooldowns.get(p.getUniqueId());
		}
		return true;
	}
}
