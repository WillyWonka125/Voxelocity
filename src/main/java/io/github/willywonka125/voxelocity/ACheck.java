package io.github.willywonka125.voxelocity;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitScheduler;

public class ACheck implements Listener {
	
	Voxel vox = new Voxel();
	
	
	ArrayList<Player> checkedIn = new ArrayList<Player>();
	
	public void kickUnchecked (ArrayList<Player> toKick) {
		for (int i = 0; i < toKick.size(); i++) {
			toKick.get(i).kickPlayer("AFK check has automatically kicked you. Please relog.");
		}
	}
	
	public void makeToKick() {
		for (int i = 0; i < Bukkit.getOnlinePlayers().size(); i++) {
			if (checkedIn.contains(Bukkit.getOnlinePlayers().))
		}
	}
	
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onJoin(PlayerJoinEvent event) {
			//
			BukkitScheduler schedular = Bukkit.getServer().getScheduler();
			schedular.scheduleAsyncRepeatingTask(vox, new Runnable() {

				public void run() {
					// Don't make this long af, son. Don't want to stall the server
					vox.getServer().broadcastMessage(ChatColor.RED + "AFK check! Do /vox acheck within 1 minute or you will be kicked!");
					try {
						TimeUnit.MINUTES.sleep(1);
					} catch (InterruptedException e) {
						e.printStackTrace(); //Should handle it fine...
					}
					
					
					
				}
				
			}, 0L, 18000L);
		}
		
	
}
