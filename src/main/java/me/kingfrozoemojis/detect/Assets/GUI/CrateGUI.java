package me.kingfrozoemojis.detect.Assets.GUI;

import me.kingfrozoemojis.detect.Detect;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class CrateGUI implements Listener {

    public static List<Inventory> crateTypes = new ArrayList<Inventory>();
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
        if(item == null) return;
        if(item.getType() != Material.PAPER) return;
        if(!item.hasItemMeta()) return;
        if(!item.getItemMeta().hasCustomModelData()) return;

        int customModelData = item.getItemMeta().getCustomModelData();

        if(customModelData < 407 || customModelData > 411) return;

       player.openInventory(crateTypes.get(customModelData-407));
    }

    @EventHandler
    public void pressSpin(InventoryClickEvent event){
        Inventory inv = event.getClickedInventory();
        AtomicBoolean spinCrate = new AtomicBoolean(false);
        if(inv.getType().getDefaultTitle().equals("Player")) return;
        crateTypes.forEach(crate -> { if(inv.equals(crate)) spinCrate.set(true); });
        if(!spinCrate.get()) return;

        event.getWhoClicked().sendMessage(event.getSlot() + "");
    }

    @EventHandler
    public void invCloseAttempt(InventoryCloseEvent) {
        // DONT LET THEM CLOSE WHILE SPINNING
        // LET THEM CLOSE ON COLLECT PAGE THO
    }

    public static void instantiateCrates() {
        List<String> crateArtCodes = Detect.getPlugin().getConfig().getStringList("crate.cratetitles");
        if(crateArtCodes.isEmpty()) {
            System.out.println("Missing Config!");
            return;
        }
        System.out.println(crateArtCodes.size());
        for(int i = 0; i < 5; i++)
            crateTypes.add(Bukkit.createInventory(null, 54, crateArtCodes.get(i).replaceAll("&", "§") ) );
    }

    public static void spin(Player player){
        String crateType = player.getOpenInventory().getTitle();
        for(int i = 0; i < crateTypes.size(); i++) {
            if(crateType.equals(crateTypes.get(i))) {
                // CREATE A NEW INSTANCE OF THE CRATE + ANIMATE IT
            }

        }

    }
}
