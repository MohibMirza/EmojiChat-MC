package me.kingfrozoemojis.detect;

import me.kingfrozoemojis.detect.Commands.Emojis;
import me.kingfrozoemojis.detect.Commands.HouseKeeping;
import me.kingfrozoemojis.detect.Commands.TitleCreate;
import me.kingfrozoemojis.detect.Events.EmojiTalk;
import org.bukkit.plugin.java.JavaPlugin;

public final class Detect extends JavaPlugin {

    public static Detect plugin;

    @Override
    public void onEnable() {
        System.out.println("[EmojiChat] EmojiChat is loading emojis...");

        System.out.println("[EmojiChat] EmojiChat is up and running!");

        plugin = this;

        saveDefaultConfig();

       getCommand("emojichat").setExecutor(new HouseKeeping());
       getCommand("emojis").setExecutor(new Emojis());
       getCommand("titlecreate").setExecutor(new TitleCreate());


        getServer().getPluginManager().registerEvents(new EmojiTalk(), this);

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        System.out.println("[EmojiChat] EmojiChat shutting down :(");
    }

    public static Detect getPlugin() {
        return plugin;
    }


}
