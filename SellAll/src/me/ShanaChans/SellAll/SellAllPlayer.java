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
	private HashMap<Material, Integer> itemAmountSold;
	
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
        			int cap = itemSellCap.getOrDefault(material, SellAllManager.getItemCaps().get(material));
        			if(sold < cap)
        			{
        				if(!itemAmount.containsKey(material))
    					{
    						itemAmount.put(material, 0);
    						itemTotal.put(material, 0.00);
    					}
        				
        				if(sold + itemAmount.get(material) > cap || sold + items.getAmount() > cap)
        				{
        					int difference = (sold + items.getAmount()) - cap;
        					itemAmount.put(material, itemAmount.get(material) + (items.getAmount() - difference));
        					itemAmountSold.put(material, sold + (items.getAmount() - difference));
        					itemTotal.put(material, itemTotal.get(material) + ((items.getAmount() - difference) * SellAllManager.getItemPrices().get(material) * sellPriceModifier));
        					totalCost += (items.getAmount() - difference) * SellAllManager.getItemPrices().get(material) * sellPriceModifier;
        					inv.removeItem(new ItemStack(material, (items.getAmount() - difference)));
        				}
        				else
        				{
        					itemAmount.put(material, itemAmount.get(material) + items.getAmount());
        					itemAmountSold.put(material, sold + items.getAmount());
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
    		player.sendMessage("ง6No items to be sold.");
    	}
    	else
    	{
    		ComponentBuilder builder = new ComponentBuilder("ยง6[Sell Log]");
    		String text = "";
    		for(Material mat : itemAmount.keySet())
    		{
    			text = text.concat("ง6" + mat.name() + " ง7(" + itemAmount.get(mat) + "x) - " + "งe" + itemTotal.get(mat) + "g\n");
    		}	
    		text = text.concat("ง7TOTAL - งe" + totalCost + "g");
    		builder.event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(text))).event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/sellall limit"));
    		player.spigot().sendMessage(builder.create());
    		//NeoCore.getEconomy().depositPlayer(player, totalCost);
    	}
    }
	
	/**
	 * Lists out the players personal item caps
	 * @param player
	 */
	public void getSellCap(Player player, Player displayPlayer)
	{
		player.sendMessage("ง6O---={ " + displayPlayer.getName() + "'s Sell Limits }=---O");
		for(Material mat : itemSellCap.keySet())
		{
			player.sendMessage("ง7" + mat.name() + ": " + itemAmountSold.get(mat) + " / " + itemSellCap.get(mat));
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
			if(-1 < newAmount && newAmount < (itemSellCap.get(mat) + 1))
			{
				itemAmountSold.replace(mat, newAmount);
				player.sendMessage("ง6Changed sold amount!");
			}
		}
	}

	public HashMap<Material, Integer> getItemAmountSold() {
		return itemAmountSold;
	}
}
