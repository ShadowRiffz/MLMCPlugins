package io.github.bananapuncher714;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

public class Tier {
	String name;
	boolean hideEnchants;
	boolean customDurability;
	boolean unbreakable;
	ArrayList<String> materials;
	ArrayList<String> requiredLore;
	ArrayList<String> optionalLore;
	ArrayList<String> requiredEnchantments;
	ArrayList<String> optionalEnchantments;
	ArrayList<String> prefixes;
	ArrayList<String> names;
	ArrayList<String> suffixes;
	int loreMin;
	int loreMax;
	int enchMin;
	int enchMax;
	int preMin;
	int preMax;
	int sufMin;
	int sufMax;
	int durMin;
	int durMax;
	int durStep;
	int perMin;
	int perMax;

	public Tier(String n, ArrayList<String> mat, ArrayList<String> rl, ArrayList<String> ol, ArrayList<String> re,
			ArrayList<String> oe, ArrayList<String> p, ArrayList<String> na, ArrayList<String> s, boolean un) {
		this.name = n;
		this.materials = mat;
		this.requiredLore = rl;
		this.optionalLore = ol;
		this.requiredEnchantments = re;
		this.optionalEnchantments = oe;
		this.prefixes = p;
		this.names = na;
		this.suffixes = s;
		this.unbreakable = un;
	}

	public void setValues(boolean hideEnchants, boolean cd, int lmi, int lma, int emi, int ema, int pmi, int pma,
			int smi, int sma, int dmi, int dma, int dst, int pemi, int pema) {
		this.hideEnchants = hideEnchants;
		this.customDurability = cd;
		this.loreMin = lmi;
		this.loreMax = lma;
		this.enchMin = emi;
		this.enchMax = ema;
		this.preMin = pmi;
		this.preMax = pma;
		this.sufMin = smi;
		this.sufMax = sma;
		this.durMin = dmi;
		this.durMax = dma;
		this.durStep = dst;
		this.perMin = pemi;
		this.perMax = pema;
	}

	public String getName() {
		return this.name;
	}

	public ItemStack getItem() {
		Random r = new Random();
		String randomMaterial = "280";
		String itemName = "Banana";
		if (this.materials.size() > 0) {
			randomMaterial = (String) this.materials.get(r.nextInt(this.materials.size()));
		}
		if (this.names.size() > 0) {
			itemName = parseRandom(selectRandom(this.names));
		}
		String[] rawMaterials = randomMaterial.split(":");
		Material material;
		try {
			material = Material.getMaterial(rawMaterials[0]);
		} catch (Exception e) {
			material = Material.getMaterial(rawMaterials[0]);
		}
		int data = 0;
		if (rawMaterials.length > 1) {
			data = Integer.parseInt(rawMaterials[1]);
		}
		ItemStack item = new ItemStack(material, 1, (short) data);
		if ((material == Material.SKELETON_SKULL) && (rawMaterials.length > 2)) {
			SkullMeta meta = (SkullMeta) item.getItemMeta();
			meta.setOwner(rawMaterials[2]);
			item.setItemMeta(meta);
		}
		ItemMeta meta = item.getItemMeta();
		int preAmount = this.preMax;
		if (this.preMax - this.preMin > 0) {
			preAmount = r.nextInt(this.preMax - this.preMin + 1) + this.preMin;
		}
		ArrayList<String> unusedPrefixes = new ArrayList(this.prefixes);
		for (int i = 0; i < preAmount; i++) {
			if (unusedPrefixes.size() <= 0) {
				break;
			}
			String chosenPre = parseRandom(selectRandom(unusedPrefixes));
			remove(unusedPrefixes, chosenPre);
			itemName = chosenPre + " " + itemName;
		}
		int sufAmount = this.sufMax;
		if (this.sufMax - this.sufMin > 0) {
			sufAmount = r.nextInt(this.sufMax - this.sufMin + 1) + this.sufMin;
		}
		ArrayList<String> unusedSuffixes = new ArrayList(this.suffixes);
		for (int i = 0; i < sufAmount; i++) {
			if (unusedSuffixes.size() <= 0) {
				break;
			}
			String chosenSuf = parseRandom(selectRandom(unusedSuffixes));
			remove(unusedSuffixes, chosenSuf);
			itemName = itemName + " " + chosenSuf;
		}
		ArrayList<String> lore = new ArrayList();
		for (String s : this.requiredLore) {
			lore.add(parseRandom(s));
		}
		int loreAmount = r.nextInt(this.loreMax - this.loreMin + 1) + this.loreMin;
		Object randomLore = new ArrayList(this.optionalLore);
		for (int i = 0; i < loreAmount; i++) {
			if (((ArrayList) randomLore).size() <= 0) {
				break;
			}
			String rlore = selectRandom((ArrayList) randomLore);
			remove((ArrayList) randomLore, rlore);
			lore.add(parseRandom(rlore));
		}
		meta.setDisplayName(itemName);

		int maxDur = (this.durMax - this.durMin) / this.durStep;
		int overDur = r.nextInt(maxDur + 1) * this.durStep + this.durMin;
		double durPercent = 0.01D * (r.nextInt(this.perMax - this.perMin + 1) + this.perMin);
		int usedDur = (int) (overDur * durPercent);
		if (this.customDurability) {
			String durString = MLMCTieredItemsMain.DURABILITY_STRING;
			durString = durString.replaceAll("%1", String.valueOf(usedDur));
			durString = durString.replaceAll("%2", String.valueOf(overDur));
			lore.add(durString);
		}
		if (MLMCTieredItemsMain.DAMAGEABLES.contains(material)) {
			item.setDurability((short) (int) (material.getMaxDurability() * (1.0D - durPercent)));
		}
		meta.setLore(lore);
		if (this.hideEnchants) {
			meta.addItemFlags(new ItemFlag[] { ItemFlag.HIDE_ENCHANTS });
		}
		if (this.unbreakable) {
			meta.setUnbreakable(true);
		}
		item.setItemMeta(meta);
		applyEnchants(item);
		return item;
		// return NBTEditor.setItemTag(item, "TRUE!!!", new Object[] { "MLMCTieredItemsAnvillable" });
	}

	private String parseRandom(String string) {
		Random r = new Random();
		String parsed = string;
		Pattern pattern = Pattern.compile("random<(\\d*?),(\\d*?)>");
		Matcher matcher = pattern.matcher(parsed);
		while (matcher.find()) {
			int lower = Integer.parseInt(matcher.group(1));
			int higher = Integer.parseInt(matcher.group(2));
			int random = r.nextInt(higher - lower) + lower;
			parsed = parsed.replaceFirst("random<(\\d*?),(\\d*?)>", String.valueOf(random));
		}
		Pattern tpattern = Pattern.compile("random<(\\d+),(\\d+),(\\d+)>");
		Matcher tmatcher = tpattern.matcher(parsed);
		while (tmatcher.find()) {
			int lower = Integer.parseInt(tmatcher.group(1));
			int higher = Integer.parseInt(tmatcher.group(2));
			int step = Integer.parseInt(tmatcher.group(3));
			int maxStep = (higher - lower) / step;
			int finalNum = r.nextInt(maxStep + 1) * step + lower;
			parsed = parsed.replaceFirst("random<(\\d+),(\\d+),(\\d+)>", String.valueOf(finalNum));
		}
		return parsed;
	}

	private String selectRandom(ArrayList<String> lore) {
		HashMap<String, Integer> weightedLore = new HashMap();
		int totalWeight = 0;
		for (String l : lore) {
			Pattern pattern = Pattern.compile(":(\\d*)$");
			Matcher matcher = pattern.matcher(l);
			String parsed = l.replaceAll(":(\\d*)$", "");
			int weight = 1;
			if (matcher.find()) {
				weight = Integer.parseInt(matcher.group(1));
			}
			weightedLore.put(parsed, Integer.valueOf(weight));
			totalWeight += weight;
		}
		if (totalWeight <= 0) {
			return null;
		}
		Random rand = new Random();
		int index = Math.abs(rand.nextInt(totalWeight + 1));
		int sum = 0;
		int i = 0;
		ArrayList<String> ores = new ArrayList(weightedLore.keySet());
		while (sum < index) {
			sum += ((Integer) weightedLore.get(ores.get(i++))).intValue();
		}
		return (String) ores.get(Math.max(0, i - 1));
	}

	private void applyEnchants(ItemStack item) {
		Random r = new Random();
		for (String s : this.requiredEnchantments) {
			addEnchantment(item, s, r);
		}
		ArrayList<String> opEnch = new ArrayList();
		for (String s : this.optionalEnchantments) {
			String[] enchList = s.split(":");
			if (enchList.length < 4) {
				enchList = new String[] { enchList[0], enchList[1], enchList[2], "1" };
			}
			opEnch.add(enchList[0] + ":" + enchList[1] + ":" + enchList[2] + ":" + enchList[3]);
		}
		int enchAmount = this.enchMax;
		if (this.enchMax - this.enchMin > 0) {
			enchAmount = r.nextInt(this.enchMax - this.enchMin + 1) + this.enchMin;
		}
		for (int i = 0; i < enchAmount; i++) {
			if (opEnch.size() <= 0) {
				break;
			}
			String enchn = selectRandom(opEnch);
			addEnchantment(item, enchn, r);
		}
	}

	private void addEnchantment(ItemStack i, String enchantment, Random r) {
		String[] enchList = enchantment.split(":");
		Enchantment enchant = null;
		try {
			enchant = Enchantment.getByName(enchList[0]);
		} catch (Exception e) {
			e.printStackTrace();
		}
		int min = Integer.parseInt(enchList[1]);
		int max = Integer.parseInt(enchList[2]);
		int randLevel = max;
		if (max - min > 0) {
			randLevel = r.nextInt(max - min + 1) + min;
		}
		if (enchant != null) {
			i.addUnsafeEnchantment(enchant, randLevel);
		}
	}

	private void remove(ArrayList<String> list, String toRemove) {
		for (int i = 0; i < list.size(); i++) {
			if (((String) list.get(i)).contains(toRemove)) {
				list.remove(i);
				return;
			}
		}
	}
}
