package kr.lucymc.lucy_core.Lucy_Utils.PVP;

import kr.lucymc.lucy_core.Lucy_Core;
import org.bukkit.Bukkit;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import static kr.lucymc.lucy_core.Lucy_Core.PVPMode;
import static kr.lucymc.lucy_core.Lucy_Utils.PVP.PVP_DB.*;

public class PVP_Event implements Listener {
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player players = event.getPlayer();
        String tableName = "pvp";
        String columnName = "UserID";
        String value = ""+players.getUniqueId();
        boolean dataExists = isDataExists(tableName, columnName, value);
        if(!dataExists){
            PVPInsert(players.getUniqueId(),0);
            PVPMode.put(""+players.getUniqueId(),0);
        }else{
            Bukkit.getServer().getScheduler().scheduleAsyncDelayedTask(Lucy_Core.getInstance(),new Runnable() {public void run() {
                PVPMode.put(""+players.getUniqueId(),PVPSelect(players.getUniqueId()));
            }}, 3);
        }
    }
    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        PVPUpdate(player.getUniqueId(),PVPMode.get(""+player.getUniqueId()));
        PVPMode.remove(""+player.getUniqueId());
    }
    @EventHandler
    public void onEntityDamage(EntityDamageByEntityEvent e){
        boolean isCitizensNPC = e.getEntity().hasMetadata("NPC");
        if (e.getEntity() instanceof Player&&e.getDamager() instanceof Player&&!isCitizensNPC) {
            Player v = (Player) e.getEntity();
            Player a = (Player) e.getDamager();
            if(!a.isOp()) {
                if (PVPMode.get("" + v.getUniqueId()) == 1 || PVPMode.get("" + a.getUniqueId()) == 1) {
                    e.setCancelled(true);
                }
            }
        }else if(e.getEntity() instanceof Player && e.getDamager() instanceof Arrow && !isCitizensNPC) {
            Arrow arrow = (Arrow) e.getDamager();
            Player v = (Player) e.getEntity();
            if(arrow.getShooter() instanceof Player p) {
                if(!p.isOp()) {
                    if (PVPMode.get("" + v.getUniqueId()) == 1 || PVPMode.get("" + p.getUniqueId()) == 1) {
                        e.setCancelled(true);
                    }
                }
            }
        }
    }
}

