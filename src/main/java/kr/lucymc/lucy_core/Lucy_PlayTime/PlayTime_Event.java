package kr.lucymc.lucy_core.Lucy_PlayTime;

import kr.lucymc.lucy_core.Lucy_Core;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;

import static kr.lucymc.lucy_core.Lucy_Core.PlayTimes;
import static kr.lucymc.lucy_core.Lucy_PlayTime.PlayTime_DB.*;


public class PlayTime_Event implements Listener {
    FileConfiguration config = Lucy_Core.getInstance().getConfig();
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        String player = event.getPlayer().getUniqueId().toString();
        Player players = event.getPlayer();
        String tableName = "playtime";
        String columnName = "UserID";
        String value = ""+players.getUniqueId();
        boolean dataExists = isDataExists(tableName, columnName, value);
        if(!dataExists){
            PlayInsert(players.getUniqueId(),0);
            PlayTimes.put(""+players.getUniqueId(),0);
        }else{
            Bukkit.getServer().getScheduler().scheduleAsyncDelayedTask(Lucy_Core.getInstance(),new Runnable() {public void run() {
                PlayTimes.put(""+players.getUniqueId(),PlaySelect(players.getUniqueId()));
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        if (!PlayTimes.containsKey("" + event.getPlayer().getUniqueId())) {
                            cancel();
                        } else {
                            PlayTimes.put(player, PlayTimes.get(player) + 1);
                        }
                    }
                }.runTaskTimer(Lucy_Core.getInstance(),0L, 20L);
            }}, config.getInt("PlayTime.DBLoadTick"));
        }
    }
    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        PlayUpdate(player.getUniqueId(),PlayTimes.get(""+player.getUniqueId()));
        PlayTimes.remove(""+player.getUniqueId());
    }
}

