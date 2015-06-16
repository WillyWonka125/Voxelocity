package io.github.willywonka125.voxelocity;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import net.milkbowl.vault.economy.Economy;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public class Voxel extends JavaPlugin implements Listener {
	
	public String ver = "0.2";
	public static Economy econ = null;
	public Plugin plugin;
	
	private Wild wild = new Wild();
	private PocketChest pchest = new PocketChest();
	
	CommandExecutor wildExec = (CommandExecutor) wild;
	CommandExecutor pchestExec = (CommandExecutor) pchest;
	
	private File datafile;
	public FileConfiguration data;
	
	public void onEnable() {
		plugin = this;
		getLogger().info("Looking splended today. Keep it up!");
		getCommand("wild").setExecutor(wildExec);
		getCommand("pchest").setExecutor(pchestExec);
		getServer().getPluginManager().registerEvents(this, this);
		this.saveDefaultConfig();
		
		try {
			getDataFiles();
		} catch (IOException e) {
			getLogger().log(Level.SEVERE, "Voxelocity was unable to create data file.");
			getLogger().log(Level.SEVERE, "Plugin will now disable. Please ensure you have the newest build.");
			Bukkit.getPluginManager().disablePlugin(this);
		}
		
		if (!setupEconomy()) {
			getLogger().log(Level.SEVERE, "Vault not found, plugin will now disable.");
			Bukkit.getPluginManager().disablePlugin(this);
		}
		
		for (int i=1; i<this.getConfig().getList("warps").size(); i++) {
			warps.add((String) this.getConfig().getList("warps").get(i));
		}
		
	}
	
	public void onDisable() {
		this.saveConfig();
		try {
			data.save(datafile);
		} catch (IOException e) {
			getLogger().log(Level.SEVERE, "Voxelocity was unable to save data file.", e);
		}
	}
	
    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        econ = rsp.getProvider();
        return econ != null;
    }
	
	private void getDataFiles() throws IOException { //Create playerdata.yml!
		if (!this.getDataFolder().exists()) this.getDataFolder().mkdir();
		datafile = new File(this.getDataFolder(), "playerdata.yml");
		if (!datafile.exists()) datafile.createNewFile();
		data = YamlConfiguration.loadConfiguration(datafile);
		
		try {
			data.getString("version");
		} catch (NullPointerException e) {
			data.set("version", ver);
			data.createSection("players");
		}
	}
	
	public ArrayList<String> warps = new ArrayList<String>();
	
	public Inventory getMenuInv () {
		ItemStack tpStack = new ItemStack(Material.getMaterial(getConfig().getInt("tpMenuItem")), 1);
		ItemMeta tpMeta = tpStack.getItemMeta();
		tpMeta.setDisplayName(ChatColor.GOLD + "" + ChatColor.BOLD + "Teleport");
		List<String> lore = new ArrayList<String>(); 
	    lore.add(ChatColor.GRAY + "Open the teleportation menu.");
		tpMeta.setLore(lore);
		tpStack.setItemMeta(tpMeta);
		
		Inventory menuInv = Bukkit.createInventory(null, 9, "Menu");
		menuInv.setItem(0, tpStack);
		return menuInv;
	}
	
	public Inventory getTpInv () {
		Inventory tpInv = Bukkit.createInventory(null, 9, "Teleport"); //Make the initial inventory
		Material mat = Material.getMaterial(getConfig().getInt("warpItem"));
		
		for (int i=0; i<warps.size(); i++) { //This adds items that represent each warp
			ItemStack tmp = new ItemStack(mat, i+1);
			ItemMeta meta = tmp.getItemMeta();
			meta.setDisplayName(ChatColor.GOLD + warps.get(i));
			tmp.setItemMeta(meta);
			tpInv.setItem(i, tmp);
		}
		
		ItemStack back = new ItemStack(Material.ARROW, 1); //Create the back button to return to main menu
		ItemMeta backmeta = back.getItemMeta();
		backmeta.setDisplayName(ChatColor.RED + "" + ChatColor.BOLD + "Back");
		back.setItemMeta(backmeta);
		tpInv.setItem(8, back);
		
		return tpInv; //Return the completed inventory
	}
	
	public void tpFromInv (String warp, Player player) {
		getServer().dispatchCommand(player, "warp " + warps.get(warps.indexOf(warp)));
	}
	
	@EventHandler
	public void onInvClick(InventoryClickEvent event) {
		Player player = (Player) event.getWhoClicked();
		ItemStack clicked = event.getCurrentItem();
		if (event.getInventory().getName().equals(getMenuInv().getName())) {
			if (clicked.getType() == Material.getMaterial(getConfig().getInt("tpMenuItem"))) {
				event.setCancelled(true);
				player.closeInventory();
				player.openInventory(getTpInv());
			}
		} else if (event.getInventory().getName().equals(getTpInv().getName())) {
			if (clicked.getType() == Material.ARROW) {
				event.setCancelled(true);
				player.closeInventory();
				player.openInventory(getMenuInv());
			} else {
				event.setCancelled(true);
				tpFromInv(ChatColor.stripColor(clicked.getItemMeta().getDisplayName()), player); }
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
		} else if (args[0].equalsIgnoreCase("reload")){
			reloadConfig();
			sender.sendMessage(ChatColor.RED + "Voxelocity config reloaded");
		} else {
			Player player = Bukkit.getPlayer(sender.getName());
			player.openInventory(getMenuInv());
		}
		return true;
	}

}
