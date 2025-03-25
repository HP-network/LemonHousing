package cn.lemonnetwork.lemonhousing.lobby;

import cn.lemonnetwork.lemonhousing.LemonHousing;
import cn.lemonnetwork.lemonhousing.database.MongoDB;
import cn.lemonnetwork.lemonsugar.utils.ScoreboardManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class PlayerJoin implements Listener {
    Plugin plugin = LemonHousing.getPlugin(LemonHousing.class);
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        new BukkitRunnable() {
            @Override
            public void run() {
                setScoreBoard();
            }
        }.runTaskTimer(plugin, 0L, 3000L);
    }
    public void setScoreBoard() {
        for (Player forPlayer : Bukkit.getOnlinePlayers()) {
            if (forPlayer.isOnline()) {
                SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yy");
                Calendar calendar = Calendar.getInstance();
                Date currentDate = calendar.getTime();
                String formattedDate = formatter.format(currentDate);
                ArrayList<String> Board = new ArrayList<>();

                Board.add("§b§l家园世界");
                Board.add("§1");
                Board.add("§7 " + formattedDate + " §8LLH1");
                Board.add("§2");
                Board.add("§f 欢迎来到");
                Board.add("§f Asiawings家园世界！");
                Board.add("§3");
                Board.add("§f 曲奇: §a" + MongoDB.getPlayerCookie(forPlayer.getUniqueId()));
                Board.add("§4");
                Board.add("§e 枫之屿 §f◆ §eAsiawings");
                Board.add("§5");
                String title = Board.get(0);
                Board.remove(0);
                ScoreboardManager sm = new ScoreboardManager(plugin, title, Board.toArray(new String[0]));
                sm.display(forPlayer);
            }
        }
    }
}
