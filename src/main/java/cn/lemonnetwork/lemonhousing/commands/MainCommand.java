package cn.lemonnetwork.lemonhousing.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class MainCommand extends Command {
    public MainCommand() {
        super("housing");
    }

    @Override
    public boolean execute(CommandSender commandSender, String s, String[] strings) {
        commandSender.sendMessage("§bLemon§aHousing §a - §e0.1");
        commandSender.sendMessage("§a作者: §bHPnetwork, LemonNetwork");
        commandSender.sendMessage("§a当前正在运行在 LemonSugar 1.8.8 核心中");
        commandSender.sendMessage("此服务器已通过正版授权验证.");
        return false;
    }
}
