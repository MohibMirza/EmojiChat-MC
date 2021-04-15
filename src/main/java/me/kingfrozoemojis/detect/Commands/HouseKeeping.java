package me.kingfrozoemojis.detect.Commands;

import me.kingfrozoemojis.detect.Detect;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class HouseKeeping implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(label.equalsIgnoreCase("EmojiChat")){
            if(!sender.hasPermission("EmojiChat.reload")) {
                sender.sendMessage(ChatColor.RED + "You don't have permission to do that!");
            }

            if(args.length == 1) {
                if(args[0].equalsIgnoreCase("reload")) {
                    sender.sendMessage("Reloading EmojiChat");
                    for(String message : Detect.getPlugin().getConfig().getStringList("reload.messages")) {
                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', message));

                    }
                    Detect.getPlugin().reloadConfig();
                }

            }

        }

        return true;
    }


}
