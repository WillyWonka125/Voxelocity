package io.github.willywonka125.voxelocity;

import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Wild implements CommandExecutor{
	
	Random rn = new Random();
	
	double rangeMax = 7000;
	double rangeMin = -7000;
	
	public void teleportPlayer(Player player, World world) {
		double randomX = rangeMin + (rangeMax - rangeMin) * rn.nextDouble();
		double randomZ = rangeMin + (rangeMax - rangeMin) * rn.nextDouble();
		Location targ = new Location(world, randomX, 0, randomZ);
		targ.setY((double) world.getHighestBlockYAt(targ) + 2);
		
		if (targ.getBlock().isLiquid()) {
			targ.getBlock().setType(Material.GLASS);
		}
		
		player.teleport(targ);
	}

	@SuppressWarnings("deprecation")
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
			teleportPlayer(Bukkit.getServer().getPlayer(sender.getName()), Bukkit.getServer().getPlayer(sender.getName()).getWorld());
			sender.sendMessage(ChatColor.GRAY + "[" + ChatColor.GOLD + "Voxel" + ChatColor.GRAY + "]" + ChatColor.RED + " Teleported to random location.");
		return true;
	}

}
