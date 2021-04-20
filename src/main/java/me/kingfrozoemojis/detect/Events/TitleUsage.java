package me.kingfrozoemojis.detect.Events;

import me.kingfrozoemojis.detect.Commands.TitleCreate;
import me.kingfrozoemojis.detect.Detect;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.model.data.DataMutateResult;
import net.luckperms.api.model.user.User;
import net.luckperms.api.node.Node;
import net.luckperms.api.node.NodeType;
import net.luckperms.api.node.types.InheritanceNode;
import net.luckperms.api.node.types.PrefixNode;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class TitleUsage implements Listener {

    public static Map<String, Long> cooldowns;
    public static int count;

    @EventHandler
    public void wearTitle(PlayerInteractEvent event) {
        count++;
        if(count > 99) {
            cooldowns.clear();
            count = 0;
        }

        Player player = event.getPlayer();
        ItemStack item = player.getInventory().getItemInMainHand();

        if(!(event.getAction() == Action.RIGHT_CLICK_AIR ||
                event.getAction() == Action.RIGHT_CLICK_BLOCK) ) return;
        if(item.getType() != Material.LEATHER_HORSE_ARMOR) return;
        if(!item.hasItemMeta()) return;
        if(!item.getItemMeta().hasCustomModelData()) return;

        if(cooldowns.containsKey(player.getName())){
            long time = System.currentTimeMillis(), cooldown = cooldowns.get(player.getName());
            if(time < cooldown) {
                long timeLeft = (cooldown - time)/1000L;
                // player.sendMessage(ChatColor.RED + "You cannot apply another title for " + timeLeft + " seconds!");
                return;
            }
        }

        LuckPerms lp_api = Detect.getLPAPI();

        User user = lp_api.getPlayerAdapter(Player.class).getUser(player);
        String prefix = user.getCachedData().getMetaData().getPrefix();

        if(prefix == null) prefix = "";
        if(prefix.equals(item.getItemMeta().getDisplayName()) ) {
            return;
        }

        prefix = prefix.replaceAll("§", "&");

        player.getInventory().setItem(player.getInventory().getHeldItemSlot(), new ItemStack(Material.AIR));

        cooldowns.put(player.getName(), (System.currentTimeMillis() + 1000) );

        returnTitle(player, prefix);

        String title_new = item.getItemMeta().getDisplayName();
        user.data().clear(NodeType.PREFIX.predicate(mn -> mn.getPriority() == 2));
        PrefixNode newPrefix = PrefixNode.builder(title_new,2).build();
        user.data().add(newPrefix);
        lp_api.getUserManager().saveUser(user);
    }



    public void returnTitle(Player player, String prefix) {
        FileConfiguration config = Detect.getPlugin().getConfig();
        for(String key : config.getConfigurationSection("titles").getKeys(false)) {

            String title = config.getString("titles." + key + ".Title");

            if(prefix.compareToIgnoreCase(title) == 0) {
                ItemStack title_item = TitleCreate.createTitle(key, "red");
                player.getInventory().addItem(title_item);
            }
        }
    }

}
