package io.github.willywonka125.voxelocity;

import net.milkbowl.vault.economy.Economy;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;


public class PocketChest implements CommandExecutor {
	
	String[] help = {
			ChatColor.GREEN + "Voxelocity - PocketChest Help menu",
			ChatColor.GRAY + "/pchest buy <size> <name>" + ChatColor.GREEN + " - Purchase a PocketChest with [size] rows. Price depends on the size."
	};
	
	Voxel vox = new Voxel();
	Economy econ = Voxel.econ;
	int rowPrice = vox.getConfig().getInt("costPerRow");
	FileConfiguration data = vox.data;
	
	private String makeID (String uuid, int owned)  {
		String o = Integer.toString(owned);
		return (uuid + "-" + o); //Would return 12341325-1 for first chest, 12341325-2 for second
		
	}
	
	private ItemStack makeChest (Player player, int rows, String id, String name) {
		ItemStack inventoryChest = new ItemStack(Material.CHEST, 1);
		ItemMeta meta = inventoryChest.getItemMeta();
		meta.setDisplayName(ChatColor.GOLD + name);
		
		
		return inventoryChest;
	}
	
	private void purchaseChest(Player player, int rows, String name) {
		if (!(econ.has(player, rowPrice*rows))) {
			player.sendMessage(ChatColor.RED + "You can not afford that! Need " + econ.format(rowPrice*rows));
		} else { //Init player's data section
			if (!(data.isInt("players." + player.getUniqueId().toString() + ".owned"))) {
				data.createSection("players." + player.getUniqueId().toString());
				data.set("players." + player.getUniqueId().toString() + ".owned", 1);
				data.createSection("players." + player.getUniqueId().toString() + ".1");
				data.set("players." + player.getUniqueId().toString() + ".1.id", makeID(player.getUniqueId().toString(), 1));
				data.createSection("players." + player.getUniqueId().toString() + ".1.items");
			} else {
				if(data.getInt("players." + player.getUniqueId().toString() + ".owned") > vox.getConfig().getInt("maxChests")) {
					player.sendMessage(ChatColor.RED + "You already own the maximum number of chests.");
				} else {
					
				}
			}
		}
	}

	public boolean onCommand(CommandSender sender, Command cmd, String commandlabel, String[] args) {
		if (args.length == 0) {
			sender.sendMessage(help);
		} else if (args[0].equalsIgnoreCase("buy")) {
			int rows = 0;
			try {
				rows = Integer.parseInt(args[1]);
			} catch (NumberFormatException e) {
				sender.sendMessage(ChatColor.RED + args[1] + " is not a number!");
			}
			
			if (rows > vox.getConfig().getInt("maxChestRows")) {
				sender.sendMessage(ChatColor.RED + "You can't have more than " + vox.getConfig().getInt("maxChestRows") + " rows!");
			} else {
				if (args.length < 3) {
					sender.sendMessage(ChatColor.RED + "You must specify a name.");
				} else {
					purchaseChest((Player) sender, rows, args[2]); //I forget if I can cast Player to CommandSender...
				}
			}
		}
		return true;
	}

}
