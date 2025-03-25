package cn.lemonnetwork.lemonhousing.housing;

import cn.lemonnetwork.lemonhousing.LemonHousing;
import cn.lemonnetwork.lemonhousing.util.ZipUtil;
import cn.lemonnetwork.lemonsugar.utils.LocationUtil;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.io.File;

public class Join implements Listener {
    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        String houseId = Bukkit.getRedisManager().get("housing", player.getUniqueId().toString());
        if (houseId == null) {
            player.kickPlayer("大袜子你怎么没请求");
            return;
        }


        if (!new File(LemonHousing.getInstance().getDataFolder().getParentFile().getParentFile(), houseId).exists()) {
            ZipUtil.unzip(new File(LemonHousing.getInstance().getDataFolder(), "Housing.zip"), new File(LemonHousing.getInstance().getDataFolder().getParentFile().getParentFile(), houseId));
        }

        World world = Bukkit.getWorld(houseId);
        if (world == null) {
            WorldCreator seed = new WorldCreator(houseId);
            seed.createWorld();
        }

        player.teleport(LocationUtil.stringToLocation(houseId + ",1.5,71,1.5,0,0"));

        Bukkit.getRedisManager().remove("housing", player.getUniqueId().toString());

    }


}
