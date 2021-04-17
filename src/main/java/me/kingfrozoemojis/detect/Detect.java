package me.kingfrozoemojis.detect;

import me.kingfrozoemojis.detect.Commands.Emojis;
import me.kingfrozoemojis.detect.Commands.HouseKeeping;
import me.kingfrozoemojis.detect.Commands.TitleCreate;
import me.kingfrozoemojis.detect.Events.EmojiTalk;
import me.kingfrozoemojis.detect.Events.TitleUsage;
import net.luckperms.api.LuckPerms;
import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;

public final class Detect extends JavaPlugin {

    private static Detect plugin;
    private static LuckPerms lp_api;

    @Override
    public void onEnable() {
        System.out.println("[EmojiChat] EmojiChat is loading emojis...");

        System.out.println("[EmojiChat] EmojiChat is up and running!");

        plugin = this;

        TitleUsage.cooldowns = new HashMap<String, Long>();

        saveDefaultConfig();

       getCommand("emojichat").setExecutor(new HouseKeeping());
       getCommand("emojis").setExecutor(new Emojis());
       getCommand("titlecreate").setExecutor(new TitleCreate());


        getServer().getPluginManager().registerEvents(new EmojiTalk(), this);
        getServer().getPluginManager().registerEvents(new TitleUsage(), this);
        RegisteredServiceProvider<LuckPerms> provider = Bukkit.getServicesManager().getRegistration(LuckPerms.class);
        if (provider != null) {
           lp_api  = provider.getProvider();
        }

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        System.out.println("[EmojiChat] EmojiChat shutting down :(");
    }

    public static Detect getPlugin() {
        return plugin;
    }

    public static LuckPerms getLPAPI() { return lp_api; }


}
