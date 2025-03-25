package cn.lemonnetwork.lemonhousing;

import cn.lemonnetwork.lemonhousing.commands.MainCommand;
import cn.lemonnetwork.lemonhousing.commands.MyHouseCommand;
import cn.lemonnetwork.lemonhousing.database.MongoDB;
import cn.lemonnetwork.lemonhousing.housing.ChatFormat;
import cn.lemonnetwork.lemonhousing.housing.EntityDamage;
import cn.lemonnetwork.lemonhousing.housing.Join;
import cn.lemonnetwork.lemonhousing.lobby.PlayerJoin;
import cn.lemonnetwork.lemonhousing.util.RegistersUtils;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public final class LemonHousing extends JavaPlugin {

    public boolean lobbyMode = true;

    private static LemonHousing instance;

    @Override
    public void onEnable() {
        instance = this;
        // Plugin startup logic
        loadConfig();
        loadLobbyMode();
        loadDatabase();
        loadCommand();
        loadBungeeCord();
        loadListener();
        if (!lobbyMode) {
            loadWorldFile();
        }

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public static LemonHousing getInstance() {
        return instance;
    }
}
