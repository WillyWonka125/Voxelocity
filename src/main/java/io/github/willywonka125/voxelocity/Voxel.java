package io.github.willywonka125.voxelocity;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

public class Voxel extends JavaPlugin implements Listener {
	
	private Wild wild = new Wild();
	
	CommandExecutor wildExec = (CommandExecutor) wild;
	
	
	public void onEnable() { //Gonna make an inventory menu!!! Get hyped!
		getLogger().info("Looking splended today. Keep it up!");
		getCommand("wild").setExecutor(wildExec);
		getServer().getPluginManager().registerEvents(this, this);
		
	}
	
	public void onDisable() {
		
	}
	
	public String[] warps = new String[] {
			"Portals",
			"shop",
			"Wild",
			"Wild2",
			"Wild3",
	};
	
	public Inventory getMenuInv () {
		ItemStack tpStack = new ItemStack(Material.ENDER_PEARL, 1);
		ItemMeta tpMeta = tpStack.getItemMeta();
		tpMeta.setDisplayName(ChatColor.GOLD + "" + ChatColor.BOLD + "Teleport");
		List<String> lore = new ArrayList<String>(); 
	    lore.add(ChatColor.GRAY + "Open the teleportation menu.");
		tpMeta.setLore(lore);
		
		Inventory menuInv = Bukkit.createInventory(null, 9, "Menu");
		menuInv.setItem(0, tpStack);
		return menuInv;
	}
	
	public Inventory getTpInv () {
		Inventory tpInv = Bukkit.createInventory(null, 9, "Teleport"); //Make the initial inventory
		
		for (int i=0; i<warps.length; i++) { //This adds items that represent each warp
			ItemStack tmp = new ItemStack(Material.DIAMOND_SWORD, i+1);
			ItemMeta meta = tmp.getItemMeta();
			meta.setDisplayName(ChatColor.GOLD + warps[i]);
			tpInv.setItem(i, tmp);
		}
		return tpInv;
	}
	
	public void tpFromInv (ItemStack clicked, Player player) {
		Bukkit.dispatchCommand(player, "warp " + warps[clicked.getAmount()]);
	}
	
	@EventHandler
	public void onInvClick(InventoryClickEvent event) {
		Player player = (Player) event.getWhoClicked();
		ItemStack clicked = event.getCurrentItem();
		if (event.getInventory().getName().equals(getMenuInv().getName())) {
			if (clicked.getType() == Material.ENDER_PEARL) {
				event.setCancelled(true);
				player.closeInventory();
				player.openInventory(getTpInv());
			}
		} else if (event.getInventory().getName().equals(getTpInv().getName())) {
			event.setCancelled(true);
			tpFromInv(clicked, player);
		}
		//Place future click shits here
	}
	
	public String[] help = {
			ChatColor.GOLD + "Voxelocity help menu",
			ChatColor.GRAY + "/wild" + ChatColor.GOLD + " - Teleport to a random coordinate"
		};
	
	@SuppressWarnings("deprecation")
	public boolean onCommand (CommandSender sender, Command cmd, String label, String[] args) {
		if (getServer().getConsoleSender() == sender) { //FINALLY IMPLEMENTED!!!
			getServer().getConsoleSender().sendMessage("Commands may only be run as a player.");
		} else {
			Player player = Bukkit.getPlayer(sender.getName());
			player.openInventory(getMenuInv());
		}
		return true;
	}

}
