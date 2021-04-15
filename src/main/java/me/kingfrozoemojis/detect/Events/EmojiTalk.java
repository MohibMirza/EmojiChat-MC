package me.kingfrozoemojis.detect.Events;

import me.kingfrozoemojis.detect.Detect;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.Iterator;
import java.util.Set;

public class EmojiTalk implements Listener {

    @EventHandler
    public void emojiTalkEvent(AsyncPlayerChatEvent event) {

        String message = event.getMessage();

        Player sender = event.getPlayer();

        int firstOccurrence = message.indexOf(':');

        if(firstOccurrence != -1) {

            String[] words = message.split("\\s+");

            boolean complete = false;
            for(int i = 0; i < words.length; i++) {
                String word = words[i];
                if(word.startsWith(":") && word.endsWith(":")) {

                    Set<String> keys = Detect.getPlugin().getConfig().getConfigurationSection("emojis").getKeys(false);

                    Iterator<String> iterator = keys.iterator();
                    while(iterator.hasNext()) {
                        String key = iterator.next();
                        if(key.equalsIgnoreCase(word.substring(1, word.length() -1 )) ) {
                            words[i] = Detect.getPlugin().getConfig().getStringList("emojis." + key).get(0);

                        }
                    }
                }
            }
            message = "";
            for(String s : words) {
                message += s + " ";
            }

            event.setMessage(message);

        }

    }
}
