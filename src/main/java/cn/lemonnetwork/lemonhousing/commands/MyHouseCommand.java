package cn.lemonnetwork.lemonhousing.commands;

import cn.lemonnetwork.lemonhousing.lobby.menu.MyHousingMenu;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class MyHouseCommand extends Command {
    public MyHouseCommand() {
        super("myHouse");
    }

    @Override
    public boolean execute(CommandSender commandSender, String s, String[] strings) {
        if (!(commandSender instanceof Player)) {
            commandSender.sendMessage("§c后台滚蛋");
            return false;
        }
        Player player = (Player) commandSender;
        MyHousingMenu.openMenu(player);
        return false;
    }
}
