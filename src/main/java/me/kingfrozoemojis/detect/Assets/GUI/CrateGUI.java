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
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class CrateGUI implements Listener {

    public static List<Inventory> crateTypes = new ArrayList<Inventory>();
    // crateTypes[0] is the normal crate inv
    // crateTypes[4] is mythical

    public static List<Inventory> activeSpinners = new ArrayList<Inventory>();

    public static ItemStack[] contents = null;

    private static int itemIndex = 0;

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
        boolean spinCrate = false;
        int slot = -1;
        if(inv.getType().getDefaultTitle().equals("Player")) return;
        for(Inventory crate : crateTypes) {
            slot++;
            if(crate.equals(inv)) {
                spinCrate = true;
                break;
            }
        }
        if(!spinCrate) return;

        event.getWhoClicked().sendMessage(event.getSlot() + "");
    }

    @EventHandler
    public void invCloseAttempt(InventoryCloseEvent event) {
        // DONT LET THEM CLOSE WHILE SPINNING
        // LET THEM CLOSE ON COLLECT PAGE THO
        // REMOVE FROM activeSpinners once done
        // event.getPlayer().closeInventory();
        // spin(Bukkit.getPlayer(event.getPlayer().getName()));
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

    public static void shuffle(Inventory inv) {
        if(contents == null) {
            ItemStack[] items = new ItemStack[10];

            items[0] = new ItemStack(Material.COARSE_DIRT, 1);
            items[1] = new ItemStack(Material.ACACIA_BOAT, 1);
            items[2] = new ItemStack(Material.HORN_CORAL, 1);
            items[3] = new ItemStack(Material.BIRCH_BUTTON, 1);
            items[4] = new ItemStack(Material.DIAMOND_AXE, 1);
            items[5] = new ItemStack(Material.DIAMOND_SWORD, 1);
            items[6] = new ItemStack(Material.DIAMOND_ORE, 1);
            items[7] = new ItemStack(Material.GOLD_NUGGET, 1);
            items[8] = new ItemStack(Material.CYAN_BED, 1);
            items[9] = new ItemStack(Material.BARRIER, 1);
            // items[10] = new ItemStack(Material.END_PORTAL, 63);

            contents = items;
        }

        int startingIndex = ThreadLocalRandom.current().nextInt(contents.length);

        itemIndex += startingIndex;
        for(int itemstacks = 9 ; itemstacks < 18; itemstacks++) {
            inv.setItem(itemstacks, contents[(itemstacks + itemIndex) % contents.length]);
        }


    }

    public static void spin(final Player player){
        String crateType = player.getOpenInventory().getTitle();
        for(int i = 0; i < crateTypes.size(); i++) {
            if(crateType.equals(crateTypes.get(i))) {
                // CREATE A NEW INSTANCE OF THE CRATE + ANIMATE IT
            }

        }

        Inventory inv = Bukkit.createInventory(null, 54, "test");
        shuffle(inv);
        activeSpinners.add(inv);
        player.openInventory(inv);

        Random r = new Random();
        double seconds = 7.0 + (12.0 - 7.0) * r.nextDouble();

        new BukkitRunnable() {
            double delay = 0;
            int ticks = 0;
            boolean done = false;

            public void run() {
                if(done) return;

                ticks++;
                delay += 1 / (20 * seconds);
                if(ticks > delay * 10) {
                    ticks = 0;
                    for(int itemstacks = 9; itemstacks < 18; itemstacks++)
                        inv.setItem(itemstacks, contents[(itemstacks + itemIndex) % contents.length]);
                    itemIndex++;

                    if(delay >= 0.5) {
                        done = true;
                        new BukkitRunnable() {

                            public void run(){
                                ItemStack item = inv.getItem(13);
                                player.getInventory().addItem(item);
                                player.updateInventory();
                                player.closeInventory();
                                cancel();
                            }

                        }.runTaskLater(Detect.getPlugin(), 50);
                        cancel();
                    }
                }
            }

        }.runTaskTimer(Detect.getPlugin(), 0, 1);


    }
}
