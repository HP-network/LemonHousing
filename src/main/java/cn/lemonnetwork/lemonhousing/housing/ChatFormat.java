package cn.lemonnetwork.lemonhousing.housing;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class ChatFormat implements Listener {
    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        String message = event.getMessage();
        for (Player p : Bukkit.getWorld("").getPlayers()) {
            p.sendMessage(player.getPrefix() + player.getName() + "§f: " + message);
        }
    }
}
