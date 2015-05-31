package io.github.willywonka125.voxelocity;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public class Voxel extends JavaPlugin {
	
	private ACheck ac = new ACheck();
	
	public void onEnable() {
		getLogger().info("Looking splending today. Keep it up!");
		getServer().getPluginManager().registerEvents(ac, this);
	}
	
	public void onDisable() {
		
	}
	
	public Plugin getPlugin() {
		return this;
	}
	
	public String[] help = {
			ChatColor.GOLD + "Voxelocity help menu",
			ChatColor.GRAY + "/voxel ci" + ChatColor.GOLD + " - AFK check-in command"
		};
	
	public boolean onCommand (CommandSender sender, Command cmd, String label, String[] args) {
		if (getServer().getConsoleSender() == sender) { //FINALLY IMPLEMENTED!!!
			getServer().getConsoleSender().sendMessage("Commands may only be run as a player.");
		} else if (args.length > 0) {
			
			if (args[0].equalsIgnoreCase("acheck")) {
				ac.checkedIn.add((Player) sender);
			}
			
		} else {
			sender.sendMessage(help);
		}
		return true;
	}

}
