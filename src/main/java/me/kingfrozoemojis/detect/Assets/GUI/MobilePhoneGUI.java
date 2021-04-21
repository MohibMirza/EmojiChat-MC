package me.kingfrozoemojis.detect.Assets.GUI;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.*;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class MobilePhoneGUI implements Listener {

    public static Inventory phone;

    @EventHandler
    public void invClick(InventoryClickEvent event) {
        // event.getWhoClicked().sendMessage( event.getView().getTitle().compareTo("§fꈉ鄛暝鄟婪ꌅꌅ鄝肤鄞涞脿膛ꈇ郓") + "");
        if(!event.getView().getTitle().equals("§fꈉ鄛暝鄟婪ꌅꌅ鄝肤鄞涞脿膛ꈇ郓")) return;
        int slot = event.getSlot();
        int row = (int) (slot / 9.0);
        int col = slot % 9;
        String name = event.getWhoClicked().getName();
        Player player = null;
        if(event.getWhoClicked() instanceof Player)  player = Bukkit.getPlayer(name);
        if(player == null) return;
        if(slot == -999){
            player.closeInventory();
            return;
        }
        event.setCancelled(true);

        // player.sendMessage(slot + "");

        if(col < 4){
            if(row == 3) {
                player.sendMessage("Chat Rooms");
            }else {
                player.sendMessage("Friends");
            }
        }else if(col < 7){
            if(row < 2) {
                player.sendMessage("Settings");
            }else{
                player.sendMessage("Profile");
            }
        } else {
            if(row < 2) {
                player.sendMessage("Settings");
            }else{
                player.sendMessage("Party");
            }
        }
    }

    @EventHandler
    public void openInv(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack item = event.getPlayer().getInventory().getItemInMainHand();

        if(item == null) return;
        if(item.getType() != Material.PAPER) return;
        if(!item.getItemMeta().hasCustomModelData()) return;
        if(item.getItemMeta().getCustomModelData() != 297) return;
        player.openInventory(phone);
    }

    public static void createInventory() {
        phone = Bukkit.createInventory(null, 36, ChatColor.translateAlternateColorCodes('&', "&fꈉ鄛暝鄟婪ꌅꌅ鄝肤鄞涞脿膛ꈇ郓"));
    }
}
