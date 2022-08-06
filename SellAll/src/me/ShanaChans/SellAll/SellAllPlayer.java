package me.ShanaChans.SellAll;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.TreeMap;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import me.neoblade298.neocore.NeoCore;
import me.neoblade298.neocore.util.PaginatedList;
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
	
	public void sellAll(Inventory inv, Player player, boolean isSelling)
	{
		HashMap<Material, Integer> itemAmount = new HashMap<Material, Integer>();
		HashMap<Material, Double> itemTotal = new HashMap<Material, Double>();
		HashMap<Material, Integer> tempAmount = new HashMap<Material, Integer>();
		
		if(!isSelling)
		{
			SellAllManager.getPlayerConfirmInv().put(player.getUniqueId(), inv);
		}
		
		double totalCost = 0;
		
    	for(ItemStack items : inv.getContents())
    	{
    		if(items != null && !items.hasItemMeta())
    		{
    			Material material = items.getType();
    			double sellMultiplier = SellAllManager.getMultiplier(player);
    			double sellBooster = SellAllManager.getBooster(player);
    			double sellPriceModifier = (sellMultiplier - 1) + (sellBooster - 1) + 1;
    			
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
    						tempAmount.put(material, sold);
    					}
        				
        				if(isSelling)
    					{
        					if(sold + itemAmount.get(material) > itemSellCap.get(material) || sold + items.getAmount() > itemSellCap.get(material)) 
            				{
        						int difference = (sold + items.getAmount()) - itemSellCap.get(material);
            					itemAmount.put(material, itemAmount.get(material) + (items.getAmount() - difference));
            					itemTotal.put(material, itemTotal.get(material) + ((items.getAmount() - difference) * SellAllManager.getItemPrices().get(material) * sellPriceModifier));
            					totalCost += (items.getAmount() - difference) * SellAllManager.getItemPrices().get(material) * sellPriceModifier;
        						itemAmountSold.put(material, sold + (items.getAmount() - difference));
        						inv.removeItem(new ItemStack(material, (items.getAmount() - difference)));
            				}
            				else
            				{
            					itemAmount.put(material, itemAmount.get(material) + items.getAmount());
            					itemTotal.put(material, itemTotal.get(material) + (items.getAmount() * SellAllManager.getItemPrices().get(material) * sellPriceModifier));
            					totalCost += items.getAmount() * SellAllManager.getItemPrices().get(material) * sellPriceModifier;
        						itemAmountSold.put(material, sold + items.getAmount());
            					inv.removeItem(new ItemStack(material, items.getAmount()));
            				}
    					}
        				else
        				{
        					if(tempAmount.get(material) + itemAmount.get(material) > itemSellCap.get(material) || tempAmount.get(material) + items.getAmount() > itemSellCap.get(material))
            				{
            					int difference = (tempAmount.get(material) + items.getAmount()) - itemSellCap.get(material);
            					itemAmount.put(material, itemAmount.get(material) + (items.getAmount() - difference));
            					itemTotal.put(material, itemTotal.get(material) + ((items.getAmount() - difference) * SellAllManager.getItemPrices().get(material) * sellPriceModifier));
            					totalCost += (items.getAmount() - difference) * SellAllManager.getItemPrices().get(material) * sellPriceModifier;
            					tempAmount.put(material, tempAmount.get(material) + (items.getAmount() - difference));
            				}
            				else
            				{
            					itemAmount.put(material, itemAmount.get(material) + items.getAmount());
            					itemTotal.put(material, itemTotal.get(material) + (items.getAmount() * SellAllManager.getItemPrices().get(material) * sellPriceModifier));
            					totalCost += items.getAmount() * SellAllManager.getItemPrices().get(material) * sellPriceModifier;
            					tempAmount.put(material, tempAmount.get(material) + items.getAmount());
            				}
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
    		if(!isSelling)
    		{
    			ComponentBuilder builder = new ComponentBuilder("§6[Hover For Sell Log]");
    			DecimalFormat df = new DecimalFormat("0.00");
            	String text = "§7§oClick to confirm or do /sellall confirm\n";
            	for(Material mat : itemAmount.keySet())
            	{
            		text = text.concat("§6" + mat.name() + " §7(" + itemAmount.get(mat) + "x) - " + "§e" + df.format(itemTotal.get(mat)) + "g\n");
            	}	
            	text = text.concat("§7TOTAL - §e" + df.format(totalCost) + "g");
            	builder.event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(text))).event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/sellall confirm"));
            	player.spigot().sendMessage(builder.create());
    		}
    		else
    		{
    			DecimalFormat df = new DecimalFormat("0.00");
    			NeoCore.getEconomy().depositPlayer(player, totalCost);
    			player.sendMessage("§6Riches Sold: §e" + df.format(totalCost) + "g");
    		}
    	}
    }
	
	/**
	 * Lists out the players personal item caps
	 * @param player
	 */
	public void getSellCap(Player player, Player displayPlayer, int pageNumber, TreeMap<Material, Double> sort)
	{
		PaginatedList<String> list = new PaginatedList<String>();
		for(Material mat : sort.keySet())
		{
			list.add("§7" + mat.name() + ": " + itemAmountSold.getOrDefault(mat, 0) + " / " + SellAllManager.getItemCaps().get(mat));
		}
		if(-1 < pageNumber && pageNumber < list.pages())
		{
			player.sendMessage("§6O---={ " + displayPlayer.getName() + "'s Sell Limits }=---O");
			for(String output : list.get(pageNumber))
			{
				player.sendMessage(output);
			}
			String nextPage ="/sellall cap " + displayPlayer.getName() + " " + (pageNumber + 2); 
			String prevPage = "/sellall cap " + displayPlayer.getName() + " " + (pageNumber); 
			list.displayFooter(player, pageNumber, nextPage, prevPage);
			return;
		}
		player.sendMessage("§7Invalid page");
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
