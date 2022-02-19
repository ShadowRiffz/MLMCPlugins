package me.Neoblade298.NeoConsumables.objects;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.scheduler.BukkitTask;

import com.sucy.skill.SkillAPI;
import com.sucy.skill.api.player.PlayerData;
import com.sucy.skill.api.util.FlagManager;
import com.sucy.skill.api.util.StatusFlag;

import me.Neoblade298.NeoConsumables.Consumables;
import me.Neoblade298.NeoConsumables.runnables.AttributeRunnable;
import me.Neoblade298.NeoConsumables.runnables.AttributeTask;
import me.Neoblade298.NeoConsumables.runnables.HealthRunnable;
import me.Neoblade298.NeoConsumables.runnables.ManaRunnable;

public class FoodConsumable extends Consumable {
	ArrayList<PotionEffect> potions = new ArrayList<PotionEffect>();
	Attributes attributes = null;
	int attributeTime;
	ArrayList<String> commands = new ArrayList<String>();
	ArrayList<String> worlds = new ArrayList<String>();
	double saturation;
	int hunger;
	int health, healthTime, healthDelay;
	int mana, manaTime, manaDelay;
	long cooldown = 0;
	boolean ignoreGcd = false;
	
	public FoodConsumable(Consumables main, String name, ArrayList<Sound> sounds, ArrayList<String> lore, HashMap<String, String> nbt) {
		super(main, name, sounds, lore, nbt);
	}

	public int getHunger() {
		return this.hunger;
	}

	public double getSaturation() {
		return this.saturation;
	}

	public boolean isSimilar(ItemStack item) {
		ItemMeta meta = item.getItemMeta();
		if (!getLore().isEmpty()) {
			if (!meta.hasLore()) {
				return false;
			}

			ArrayList<String> flore = getLore();
			ArrayList<String> mlore = (ArrayList<String>) meta.getLore();

			for (int i = 0; i < flore.size(); i++) {
				String fLine = getLore().get(i);
				String mLine = mlore.get(i);
				if (!mLine.contains(fLine)) {
					return false;
				}
			}
		}
		return true;
	}

	public boolean canUse(Player p, ItemStack item) {
		// Check gcd
		UUID uuid = p.getUniqueId();
		if (!isOffGcd(p) && !ignoreGcd) {
			long remainingCooldown = main.globalCooldowns.get(p.getUniqueId()) - System.currentTimeMillis();
			remainingCooldown /= 1000L;
			String message = "§4[§c§lMLMC§4] &cConsumables cooldown: §e" + remainingCooldown + "s§7.";
			message = message.replaceAll("&", "§");
			p.sendMessage(message);
			return false;
		}

		// Check per-consumable cooldown
		if (main.foodCooldowns.containsKey(uuid) && main.foodCooldowns.get(uuid).containsKey(this)) {
			long nextEat = main.foodCooldowns.get(uuid).get(this);
			if (System.currentTimeMillis() < nextEat) {
				long remainingCooldown = nextEat - System.currentTimeMillis();
				remainingCooldown /= 1000L;
				String message = "§4[§c§lMLMC§4] " + this.displayname + " §ccooldown: §e" + remainingCooldown + "s§7.";
				message = message.replaceAll("&", "§");
				p.sendMessage(message);
				return false;
			}
		}
		
		// Check world compatible
		if (!getWorlds().contains(p.getWorld().getName())) {
			String message = "§4[§c§lMLMC§4] §cYou cannot use this consumable in this world";
			p.sendMessage(message);
			return false;
		}
		return true;
	}
	
	private boolean isOffGcd(Player p) {
		if (main.globalCooldowns.containsKey(p.getUniqueId())) {
			return System.currentTimeMillis() > main.globalCooldowns.get(p.getUniqueId());
		}
		return true;
	}

	public void use(final Player p, ItemStack item) {
		// Sounds, commands
		p.sendMessage("§4[§c§lMLMC§4] §7You ate " + displayname + "§7!");
		for (Sound sound : getSounds()) {
			p.getWorld().playSound(p.getEyeLocation(), sound, 1.0F, 1.0F);
		}
		executeCommands(p);
		
		
		// Food event only happens if hunger is changing
		UUID uuid = p.getUniqueId();
		int foodLevel = Math.min(20, p.getFoodLevel() + getHunger());
		if (getHunger() > 0) {
			FoodLevelChangeEvent event = new FoodLevelChangeEvent(p, foodLevel);
			Bukkit.getPluginManager().callEvent(event);
			if (!event.isCancelled()) {
				p.setFoodLevel(foodLevel);
				p.setSaturation((float) Math.min(p.getFoodLevel(), this.getSaturation() + p.getSaturation()));
			}
		}
		
		// Calculate multipliers and remove flags
		double garnish = 1, preserve = 1, spice = 1;
		for (String line : item.getItemMeta().getLore()) {
			if (line.contains("Garnished")) {
				String toParse = line.substring(line.indexOf('(') + 1, line.indexOf('x'));
				garnish = Double.parseDouble(toParse);
			}
			if (line.contains("Preserved")) {
				String toParse = line.substring(line.indexOf('(') + 1, line.indexOf('x'));
				preserve = Double.parseDouble(toParse);
			}
			if (line.contains("Spiced")) {
				String toParse = line.substring(line.indexOf('(') + 1, line.indexOf('x'));
				spice = Double.parseDouble(toParse);
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

		// Set cooldowns
		long nextEat = System.currentTimeMillis() + (long) (this.cooldown * 50L * preserve);
		long ticks = (long) (this.cooldown * preserve);
		if (!ignoreGcd) {
			main.globalCooldowns.put(uuid, System.currentTimeMillis() + 20000L);
		}
		if (this.cooldown > 0) {
			main.foodCooldowns.get(uuid).put(this, nextEat);
			Bukkit.getScheduler().scheduleSyncDelayedTask(main, new Runnable() {
				public void run() {
					String message = "§4[§c§lMLMC§4] " + displayname + "&7 can be eaten again.";
					message = message.replaceAll("&", "§");
					p.sendMessage(message);
				}
			}, ticks);
		}

		// Potion effects
		for (PotionEffect effect : this.getEffect()) {
			p.addPotionEffect(effect);
		}

		// SkillAPI Attributes
		if (attributes != null) {
			// If food already consumed before, remove the attributes, cancel the remove task, then add new
			if (main.attributes.get(uuid).containsKey(this)) {
				AttributeTask at = main.attributes.get(uuid).get(this);
				at.getAttr().removeAttributes(p);
				at.getTask().cancel();
			}
			Attributes newAttr = attributes.clone();
			newAttr.multiply(garnish);
			newAttr.applyAttributes(p);
			BukkitTask newTask = new AttributeRunnable(main, p, newAttr, this).runTaskLater(main, this.attributeTime);
			main.attributes.get(uuid).put(this, new AttributeTask(newTask, newAttr));
		}

		// Health and mana regen
		PlayerData data = SkillAPI.getPlayerData(p);

		if (getHealthTime() == 0 && !p.isDead()) {
				p.setHealth(Math.min(p.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue(), p.getHealth() + (health * spice)));
		}
		else if (getHealthTime() > 0 && !p.isDead()) {
			new HealthRunnable(p, health * spice, getHealthTime()).runTaskTimer(main, 0, getHealthDelay());
		}
		
		if (data.getMainClass().getData().getManaName().contains("MP")) {
			if (getManaTime() == 0 && !p.isDead()) {
				data.setMana(Math.min(data.getMaxMana(), data.getMana() + (mana * spice)));
			}
			else if (getManaTime() > 0 && !p.isDead()) {
				new ManaRunnable(p, mana * spice, getHealthTime()).runTaskTimer(main, 0, getManaDelay());
			}
		}
		
		item.setAmount(item.getAmount() - 1);
	}

	public void executeCommands(Player player) {
		for (String cmd : this.commands) {
			Bukkit.dispatchCommand(Bukkit.getConsoleSender(), cmd.replaceAll("%p", player.getName()));
		}
	}

	public void setHealth(int health) {
		this.health = health;
	}

	public void setMana(int mana) {
		this.mana = mana;
	}

	public void setCooldown(long cooldown) {
		this.cooldown = cooldown;
	}

	public void setHealthTime(int healthTime) {
		this.healthTime = healthTime;
	}

	public void setManaTime(int manaTime) {
		this.manaTime = manaTime;
	}

	public void setHealthDelay(int healthDelay) {
		this.healthDelay = healthDelay;
	}

	public void setManaDelay(int manaDelay) {
		this.manaDelay = manaDelay;
	}

	public void setWorlds(List<String> worlds) {
		this.worlds = new ArrayList<String>(worlds);
	}

	public int getHealth() {
		return this.health;
	}

	public int getMana() {
		return this.mana;
	}

	public int getHealthTime() {
		return this.healthTime;
	}

	public int getManaTime() {
		return this.manaTime;
	}

	public int getHealthDelay() {
		return this.healthDelay;
	}

	public int getManaDelay() {
		return this.manaDelay;
	}

	public List<String> getWorlds() {
		return this.worlds;
	}

	public void addEffect(PotionEffect effect) {
		this.potions.add(effect);
	}

	public ArrayList<PotionEffect> getEffect() {
		return this.potions;
	}

	public Attributes getAttributes() {
		return this.attributes;
	}
	
	public void setAttributes(Attributes attributes) {
		this.attributes = attributes;
	}

	public ArrayList<PotionEffect> getPotions() {
		return potions;
	}

	public void setPotions(ArrayList<PotionEffect> effects) {
		this.potions = effects;
	}

	public int getAttributeTime() {
		return attributeTime;
	}

	public void setAttributeTime(int attributeTime) {
		this.attributeTime = attributeTime;
	}

	public ArrayList<String> getCommands() {
		return commands;
	}

	public void setCommands(ArrayList<String> commands) {
		this.commands = commands;
	}

	public boolean isIgnoreGcd() {
		return ignoreGcd;
	}

	public void setIgnoreGcd(boolean ignoreGcd) {
		this.ignoreGcd = ignoreGcd;
	}

	public long getCooldown() {
		return cooldown;
	}

	public void setWorlds(ArrayList<String> worlds) {
		this.worlds = worlds;
	}

	public void setLore(ArrayList<String> lore) {
		this.lore = lore;
	}

	public void setSaturation(double saturation) {
		this.saturation = saturation;
	}

	public void setHunger(int hunger) {
		this.hunger = hunger;
	}
}
