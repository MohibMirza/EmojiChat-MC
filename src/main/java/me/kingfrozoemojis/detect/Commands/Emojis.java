package me.kingfrozoemojis.detect.Commands;

import me.kingfrozoemojis.detect.Detect;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.event.EventHandler;
import org.w3c.dom.Text;


public class Emojis implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(label.equalsIgnoreCase("emojis")) {

            ComponentBuilder message  = new ComponentBuilder();

            Detect.getPlugin().getConfig().getConfigurationSection("emojis").getKeys(false).forEach(key -> {
                String emoji = Detect.getPlugin().getConfig().getStringList("emojis." + key).get(0);
                message.append(emoji);
                message.event(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, ":" + key + ":"));
                message.event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, TextComponent.fromLegacyText(":" + emoji + ": " + key
                        + " " + ChatColor.LIGHT_PURPLE + "/emojis")));

                message.append(" ");

            });

            sender.spigot().sendMessage(message.create());


        }
        return true;

    }
}
