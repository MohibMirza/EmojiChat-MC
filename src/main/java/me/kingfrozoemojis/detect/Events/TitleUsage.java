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

        // player.sendMessage(prefix);

        FileConfiguration config = Detect.getPlugin().getConfig();

        int cmd_new = item.getItemMeta().getCustomModelData();

        player.getInventory().setItem(player.getInventory().getHeldItemSlot(), new ItemStack(Material.AIR));

        cooldowns.put(player.getName(), (System.currentTimeMillis() + 1000) );
        player.sendMessage("Cooldown applied");

        for(String key : config.getConfigurationSection("titles").getKeys(false)) {

            String title = config.getString("titles." + key + ".Title");
            int cmd_key = config.getInt("titles." + key + ".CustomModelData");

            if(prefix.equalsIgnoreCase(title)) {
                // player.sendMessage("Returning your title back to you!");
                ItemStack title_item = TitleCreate.createTitle(key, "red");
                player.getInventory().addItem(title_item);
            }

            if(cmd_new == cmd_key) {
                String title_new = config.getString("titles." + key + ".Title");
                PrefixNode node = PrefixNode.builder(title_new,2).build();

                player.sendMessage(title_new);

                DataMutateResult result = user.data().add(node);
                result = user.data().remove(PrefixNode.builder(prefix, 2).build());
                lp_api.getUserManager().saveUser(user);
                // player.sendMessage("Prefix updated!");
            }
        }
    }

}
