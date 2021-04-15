package me.kingfrozoemojis.detect.Commands;

import me.kingfrozoemojis.detect.Detect;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;

import java.util.HashMap;
import java.util.List;

public class TitleCreate implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "Only players can perform this action currently.");
        }

        if(args.length < 2) {
            sender.sendMessage(ChatColor.RED + "USAGE: /titlecreate <NAME> <COLOR>");
            return false;
        }

        ItemStack title = createTitle(args[0], args[1]);

        if(title == null) {
            sender.sendMessage(ChatColor.RED + "Invalid parameters.");
            return false;
        }

        Player player = (Player) sender;

        HashMap<Integer, ItemStack> remains = player.getInventory().addItem(title);

        if(!remains.isEmpty()) {
            sender.sendMessage(ChatColor.RED + "Your inventory is full. Create space and try again.");
            return false;
        }

        sender.sendMessage(ChatColor.GREEN + "Title successfully generated!");

        return false;
    }

    public static ItemStack createTitle(String titleName, String color) {
        String path = "titles." + titleName;
        if(!Detect.getPlugin().getConfig().contains(path)) {
            return null;
        }

        ItemStack title = new ItemStack(Material.LEATHER_HORSE_ARMOR);

        LeatherArmorMeta meta = (LeatherArmorMeta) title.getItemMeta();

        meta.setColor(Color.fromRGB(255, 0, 0));

        FileConfiguration config = Detect.getPlugin().getConfig();

        meta.setDisplayName(ChatColor.translateAlternateColorCodes('&',
                config.getString(path + ".Title")));

        List<String> lore = config.getStringList(path + ".Description");

        for(int i = 0; i < lore.size(); i++)
            lore.set(i, ChatColor.translateAlternateColorCodes('&', lore.get(i)) );

        meta.setLore(lore);

        meta.setCustomModelData(1);
        meta.addItemFlags(ItemFlag.values());
        title.setItemMeta(meta);

        return title;
    }


}
