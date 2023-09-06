package kr.lucymc.lucy_core.Lucy_Utils.CommandCool;

import kr.lucymc.lucy_core.Lucy_Core;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.HashMap;

public class CommandCool_Event implements Listener {
    public static HashMap<String, Integer> Cool = new HashMap<>();
    FileConfiguration config = Lucy_Core.getInstance().getConfig();
    @EventHandler
    public void onCommandPreprocess(PlayerCommandPreprocessEvent event)
    {
        if(Cool.containsKey(""+event.getPlayer().getUniqueId())){
            event.setCancelled(true);
            event.getPlayer().sendMessage(config.getString("CommandCool.message.X"));
        }
    }
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Cool.put(""+event.getPlayer().getUniqueId(),1);
        Bukkit.getServer().getScheduler().scheduleAsyncDelayedTask(Lucy_Core.getInstance(),new Runnable() {public void run() {
            Cool.remove(""+event.getPlayer().getUniqueId());
        }}, 20 * 5);
    }
}
