package me.ShanaChans.SellAll;

import java.util.HashMap;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import me.neoblade298.neocore.NeoCore;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.hover.content.Text;

public class SellAllPlayer 
{
	private HashMap<Material, Integer> itemSellCap = new HashMap<Material, Integer>();
	private HashMap<Material, Integer> itemAmountSold = new HashMap<Material, Integer>();
	
	public SellAllPlayer(HashMap<Material, Integer> itemAmountSold)
	{
		this.itemAmountSold = itemAmountSold;
	}
	
	public int getItemCap(Material mat) 
	{
		return itemSellCap.get(mat);
	}

	public void setItemCap(Material mat, int newSellCap) 
	{
		itemSellCap.replace(mat, newSellCap);
	}
	
	public void sellAll(Inventory inv, Player player)
	{
		HashMap<Material, Integer> itemAmount = new HashMap<Material, Integer>();
		HashMap<Material, Double> itemTotal = new HashMap<Material, Double>();
		
		double totalCost = 0;
		
    	for(ItemStack items : inv.getContents())
    	{
    		if(items != null)
    		{
    			Material material = items.getType();
    			double sellPriceModifier = SellAllManager.getMultiplier(player);
    			
        		if(SellAllManager.getItemPrices().containsKey(material))
        		{
        			int sold = itemAmountSold.getOrDefault(material, 0);
        			itemSellCap.put(material, itemSellCap.getOrDefault(material, SellAllManager.getItemCaps().get(material)));
        			if(sold < itemSellCap.get(material))
        			{
        				if(!itemAmount.containsKey(material))
    					{
    						itemAmount.put(material, 0);
    						itemTotal.put(material, 0.00);
    					}
        				
        				if(sold + itemAmount.get(material) > itemSellCap.get(material) || sold + items.getAmount() > itemSellCap.get(material))
        				{
        					int difference = (sold + items.getAmount()) - itemSellCap.get(material);
        					itemAmountSold.put(material, sold + (items.getAmount() - difference));
        					itemAmount.put(material, itemAmount.get(material) + (items.getAmount() - difference));
        					itemTotal.put(material, itemTotal.get(material) + ((items.getAmount() - difference) * SellAllManager.getItemPrices().get(material) * sellPriceModifier));
        					totalCost += (items.getAmount() - difference) * SellAllManager.getItemPrices().get(material) * sellPriceModifier;
        					inv.removeItem(new ItemStack(material, (items.getAmount() - difference)));
        				}
        				else
        				{
        					itemAmountSold.put(material, sold + items.getAmount());
        					itemAmount.put(material, itemAmount.get(material) + items.getAmount());
        					itemTotal.put(material, itemTotal.get(material) + (items.getAmount() * SellAllManager.getItemPrices().get(material) * sellPriceModifier));
        					totalCost += items.getAmount() * SellAllManager.getItemPrices().get(material) * sellPriceModifier;
        					inv.removeItem(new ItemStack(material, items.getAmount()));
        				}
        			}
        		}
    		}
    	}
    	
    	if(totalCost == 0)
    	{
    		player.sendMessage("§6No items to be sold.");
    	}
    	else
    	{
    		ComponentBuilder builder = new ComponentBuilder("§6[Sell Log]");
        	String text = "";
        	for(Material mat : itemAmount.keySet())
        	{
        		text = text.concat("§6" + mat.name() + " §7(" + itemAmount.get(mat) + "x) - " + "§e" + itemTotal.get(mat) + "g\n");
        	}	
        	text = text.concat("§7TOTAL - §e" + totalCost + "g");
        	builder.event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(text))).event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/sellall confirm"));
        	player.spigot().sendMessage(builder.create());

    		NeoCore.getEconomy().depositPlayer(player, totalCost);
    	}
    }
	
	/**
	 * Lists out the players personal item caps
	 * @param player
	 */
	public void getSellCap(Player player, Player displayPlayer)
	{
		player.sendMessage("§6O---={ " + displayPlayer.getName() + "'s Sell Limits }=---O");
		for(Material mat : itemSellCap.keySet())
		{
			player.sendMessage("§7" + mat.name() + ": " + itemAmountSold.get(mat) + " / " + itemSellCap.get(mat));
		}
	}
	
	/**
	 * Sets a players sold amount for a material to a new value between -1 and the cap
	 * @param player
	 * @param mat
	 * @param newAmount
	 */
	public void setSold(Player player, Material mat, int newAmount)
	{
		if(itemAmountSold.containsKey(mat))
		{
			if(-1 < newAmount && newAmount < (itemSellCap.getOrDefault(mat, SellAllManager.getItemCaps().get(mat)) + 1))
			{
				itemAmountSold.put(mat, newAmount);
				player.sendMessage("§6Changed sold amount!");
			}
		}
	}

	public HashMap<Material, Integer> getItemAmountSold() {
		return itemAmountSold;
	}
	
	public void resetSold()
	{
		for(Material mat : itemAmountSold.keySet())
		{
			itemAmountSold.put(mat, 0);
		}
	}
}
