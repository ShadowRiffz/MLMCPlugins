package me.neoblade298.neogear.objects;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import de.tr7zw.nbtapi.NBTItem;
import me.neoblade298.neogear.Gear;
import net.md_5.bungee.api.ChatColor;

public class GearConfig {
	private Gear main;
	public static final Pattern HEX_PATTERN = Pattern.compile("&(#[A-Fa-f0-9]{6})");
	public String name, display, title;
	public Material material;
	public ArrayList<String> prefixes, displayNames;
	public int duraBase;
	public ArrayList<String> requiredAugments;
	public ArrayList<Enchant> requiredEnchants, optionalEnchants;
	public int enchantmentMin, enchantmentMax;
	public int startingSlotsBase, startingSlotsRange;
	public int slotsMax;
	public int version;
	public HashMap<String, AttributeSet> attributes;
	public HashMap<String, RarityBonuses> rarities;
	public double price;
	
	public GearConfig(Gear main, String name, String display, String title, Material material, ArrayList<String> prefixes, ArrayList<String> displayNames,
			int duraBase, ArrayList<Enchant> requiredEnchants, ArrayList<Enchant> optionalEnchants, ArrayList<String> requiredAugments,
			int enchantmentMin, int enchantmentMax, HashMap<String, AttributeSet> attributes, HashMap<String, RarityBonuses> rarities, int slotsMax,
			int startingSlotsBase, int startingSlotsRange, double price, int version) {
		
		// Add color codes to all strings necessary
		for (String prefix : prefixes) {
			prefix = prefix.replaceAll("&", "§");
		}
		for (String displayName : displayNames) {
			displayName = displayName.replaceAll("&", "§");
		}
		
		this.main = main;
		this.name = name;
		if (title == null) {
			this.title = "§7Standard " + display;
		}
		else {
			this.title = title;
		}
		this.display = display;
		this.material = material;
		this.prefixes = prefixes;
		this.displayNames = displayNames;
		this.duraBase = duraBase;
		this.requiredEnchants = requiredEnchants;
		this.optionalEnchants = optionalEnchants;
		this.requiredAugments = requiredAugments;
		this.enchantmentMax = enchantmentMax;
		this.enchantmentMin = enchantmentMin;
		this.attributes = attributes;
		this.rarities = rarities;
		this.price = price;
		this.slotsMax = slotsMax;
		this.startingSlotsBase = startingSlotsBase;
		this.startingSlotsRange = startingSlotsRange;
		this.version = version;
	}
	
	public ItemStack generateItem(String rarity, int level) {
		ItemStack item = new ItemStack(material);
		if (rarities.get(rarity).material != null) {
			item = new ItemStack(rarities.get(rarity).material);
		}
		
		ItemMeta meta = item.getItemMeta();
		ArrayList<String> lore = new ArrayList<String>();
		
		// If item is a shield, give it damage
		if (material.equals(Material.SHIELD)) {
			meta.addAttributeModifier(Attribute.GENERIC_ATTACK_DAMAGE, new AttributeModifier(new UUID(1L, 2L), "Shield", 5, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.HAND));
		}
		
		// Decide Prefix
		String prefix;
		ArrayList<String> rarityPrefixes = rarities.get(rarity).prefixes;
		if (rarityPrefixes != null && rarityPrefixes.size() != 0) {
			prefix = rarityPrefixes.get(Gear.gen.nextInt(rarityPrefixes.size()));
		}
		else {
			prefix = prefixes.get(Gear.gen.nextInt(prefixes.size()));
		}
		
		// Rest of display
		String display = displayNames.get(Gear.gen.nextInt(displayNames.size()));
		meta.setDisplayName(main.rarities.get(rarity).colorCode + prefix + " " + display);
		
		
		// Add required enchantments
		if (main.rarities.get(rarity).isEnchanted) {
			meta.addEnchant(Enchantment.LUCK, 1, true);
		}
		for (Enchant enchant : requiredEnchants) {
			int lv = enchant.min + Gear.gen.nextInt(enchant.max - enchant.min + 1);
			meta.addEnchant(enchant.enchantment, lv, true);
		}
		meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		
		// Add optional enchantments
		if (enchantmentMax > 0) {
			int optEnchantNum = enchantmentMin + Gear.gen.nextInt(enchantmentMax - enchantmentMin);
			ArrayList<Enchant> optEnchants = new ArrayList<Enchant>();
			optEnchants.addAll(optionalEnchants);
			if (optEnchants.size() > 0) {
				for (int i = 0; i < optEnchantNum; i++) {
					Enchant enchant = optEnchants.get(Gear.gen.nextInt(optEnchants.size()));
					int lv = enchant.min + Gear.gen.nextInt(enchant.max - enchant.min + 1);
					meta.addEnchant(enchant.enchantment, lv, true);
					optEnchants.remove(enchant);
				}
			}
		}
		
		// Slots and augments
		int maxSlots = slotsMax;
		if (rarities.get(rarity).slotsMax != -1) {
			maxSlots = rarities.get(rarity).slotsMax;
		}
		int currentSlot = 0;
		int numSlots = startingSlotsBase;
		if (rarities.get(rarity).startingSlotsBase != -1) {
			numSlots = rarities.get(rarity).startingSlotsBase;
		}
		int slotsRange = startingSlotsRange;
		if (rarities.get(rarity).startingSlotsRange != -1) {
			slotsRange = rarities.get(rarity).startingSlotsRange;
		}
		if (slotsRange > 0) {
			numSlots += Gear.gen.nextInt(slotsRange + 1);
		}
		
		if (level == 0) {
			level = 1;
		}
		// Lore part 1
		lore.add(translateHexCodes("&7Title: " + this.title));
		lore.add("§7Type: " + this.display);
		lore.add("§7Rarity: " + main.rarities.get(rarity).displayName);
		lore.add("§7Level: " + level);
		lore.add("§7Max Slots: " + maxSlots);
		lore.add("§8§m-----");
		// Lore part 2
		for (String key : Gear.attributeOrder.keySet()) {
			double amount = 0;
			String line = null;
			if (attributes.containsKey(key)) {
				AttributeSet attr = attributes.get(key);
				amount += attr.generateAmount(level);
				String id = attr.getAttr();
				if (id.equals("hlr") || id.equals("rrg")) {
					double amt = amount;
					amt /= 10;
					line = attr.format(amt);
				}
				else {
					line = attr.format(amount);
				}
			}
			if (rarities.get(rarity).attributes.containsKey(key)) {
				AttributeSet attr = rarities.get(rarity).attributes.get(key);
				amount += attr.generateAmount(level);
				String id = attr.getAttr();
				if (id.equals("hlr") || id.equals("rrg")) {
					double amt = amount;
					amt /= 10;
					line = attr.format(amt);
				}
				else {
					line = attr.format(amount);
				}
			}
			
			if (amount > 0) {
				lore.add(line);
			}
		}
		// Lore part 3, only add separator if there was at least 1 attribute
		HashMap<String, Integer> nbtIntegers = new HashMap<String, Integer>();
		HashMap<String, String> nbtStrings = new HashMap<String, String>();
		if (lore.size() >= 6) { lore.add("§8§m-----"); }
		for (String augment : requiredAugments) {
			currentSlot++;
			String args[] = augment.split(":");
			lore.add(translateHexCodes(args[0]));
			nbtIntegers.put("slot" + currentSlot + "Line", lore.size() - 1);
			nbtIntegers.put("slot" + currentSlot + "Level", Integer.parseInt(args[2]));
			nbtStrings.put("slot" + currentSlot + "Augment", args[1]);
		}
		for (int i = 0; i < numSlots; i++) {
			currentSlot++;
			lore.add("§8[Empty Slot]");
			nbtIntegers.put("slot" + currentSlot + "Line", lore.size() - 1);
		}
		
		int durability = duraBase + rarities.get(rarity).duraBonus;
		lore.add("§7Durability " + durability + " / " + durability);
		meta.setLore(lore);
		
		item.setItemMeta(meta);
		NBTItem nbti = new NBTItem(item);
		double price = level * main.rarities.get(rarity).priceModifier;
		nbti.setDouble("value", price);
		nbti.setString("gear", name);
		nbti.setInteger("version", version);
		nbti.setInteger("slotsMax", maxSlots);
		nbti.setInteger("level", level);
		nbti.setString("rarity", rarity.toLowerCase());
		nbti.setInteger("slotsCreated", numSlots);
		for (String key : nbtIntegers.keySet()) {
			nbti.setInteger(key, nbtIntegers.get(key));
		}
		for (String key : nbtStrings.keySet()) {
			nbti.setString(key, nbtStrings.get(key));
		}
		return nbti.getItem();
	}
	
	public void updateAttributes(Player p, ItemStack item) {
		NBTItem nbti = new NBTItem(item);
		if (!nbti.hasKey("version")) {
			return;
		}
		String rarity = nbti.getString("rarity");
		int level = nbti.getInteger("level");
		
		ItemMeta meta = item.getItemMeta();
		List<String> lore = meta.getLore();
		ListIterator<String> loreIter = lore.listIterator();
		while (loreIter.hasNext()) {
			if (loreIter.next().contains("---")) {
				break;
			}
		}
		
		String line = loreIter.next();
		boolean hasChanged = false;
		for (String key : Gear.attributeOrder.keySet()) {
			String format = Gear.attributeOrder.get(key);
			int formatIndex = format.indexOf('+');
			if (formatIndex == -1) {
				formatIndex = format.indexOf('-');
			}
			String attr = format.substring(0, formatIndex - 1);
			
			if (line.contains(attr)) {
				int index = line.indexOf('+');
				if (index == -1) {
					index = line.indexOf('-');
				}
				String num = line.substring(index);
                int amt = Integer.parseInt(num.replaceAll("[^0-9-]", ""));
                
                AttributeSet aset = attributes.get(key);
                AttributeSet rset = rarities.get(rarity).attributes.get(key);
                
                int min = aset.getMinAmount(level) + rset.getMinAmount(level);
                int max = min + aset.getRange() + rset.getRange();
                
                // Attribute has updated value
                if (amt < min || amt > max) {
                	hasChanged = true;
                	loreIter.remove();
                	// If max = 0, attribute was deleted entirely
                	if (max > 0) {
                    	loreIter.add(aset.format(min + Gear.gen.nextInt(max - min + 1)));
                	}
                }
            	line = loreIter.next();
			}
			else {
                AttributeSet aset = attributes.get(key);
                AttributeSet rset = rarities.get(rarity).attributes.get(key);

                int min = aset.getMinAmount(level) + rset.getMinAmount(level);
                int max = min + aset.getRange() + rset.getRange();
                // New attribute was added to gear
                if (max > 0) {
                	hasChanged = true;
                	loreIter.previous();
                	loreIter.add(aset.format(min + Gear.gen.nextInt(max - min + 1)));
                	loreIter.next();
                }
			}
		}
		meta.setLore(lore);
		item.setItemMeta(meta);
		if (hasChanged) {
			p.sendMessage("§4[§c§lMLMC§4] §7Your item's stats have been changed due to server balancing.");
		}
	}
	
	private String translateHexCodes(String textToTranslate) {
		Matcher matcher = HEX_PATTERN.matcher(textToTranslate);
		StringBuffer buffer = new StringBuffer();

		while (matcher.find()) {
			matcher.appendReplacement(buffer, ChatColor.of(matcher.group(1)).toString());
		}

		return ChatColor.translateAlternateColorCodes('&', matcher.appendTail(buffer).toString());
	}
}
