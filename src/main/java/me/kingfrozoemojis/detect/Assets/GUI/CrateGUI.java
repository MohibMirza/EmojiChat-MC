package me.kingfrozoemojis.detect.Assets.GUI;

import me.kingfrozoemojis.detect.Detect;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class CrateGUI implements Listener {

    public static Inventory[] crateTypes = new Inventory[5];
    // crateTypes[0] is the normal crate inv
    // crateTypes[4] is mythical

    public static List<Inventory> activeSpinners = new ArrayList<Inventory>();

    // FIRST LETS HAVE THE MAIN GUI THAT EVERYONE SEES
    // THEN IF THEY DECIDE TO SPIN, TAKE THE CRATE FROM THEIR HAND,
    // AND SPIN THE PERSONALIZED CRATE
    // MAKE SURE THAT THEY CANNOT MOVE ITEMS FROM THEIR INVENTORY

    @EventHandler
    public void crateClick(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack item = player.getInventory().getItemInMainHand();

        if(event.getHand() != EquipmentSlot.HAND) return;
        player.sendMessage("Test");
        if(item == null) return;
        if(item.getType() != Material.PAPER) return;
        if(!item.hasItemMeta()) return;
        if(!item.getItemMeta().hasCustomModelData()) return;

        int customModelData = item.getItemMeta().getCustomModelData();

        if(customModelData < 407 || customModelData > 411) return;

       player.openInventory(crateTypes[customModelData-407]);
    }

    public static void instantiateCrates() {
        List<String> crateArtCodes = Detect.getPlugin().getConfig().getStringList("crate-titles");
        System.out.println(crateArtCodes.size());
        for(int i = 0; i < 5; i++)
            crateTypes[i] =  Bukkit.createInventory(null, 54, crateArtCodes.get(i));
    }
}
