package kr.lucymc.lucy_core.Lucy_Utils.UUID_DB;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.Objects;

import static kr.lucymc.lucy_core.Lucy_Utils.UUID_DB.UUID_DB_DB.*;

public class UUID_DB_Event implements Listener {
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        String tableName = "userlist";
        String columnName = "UserID";
        String value = ""+event.getPlayer().getUniqueId();
        boolean dataExists = isDataExists(tableName, columnName, value);
        if(dataExists){
            if(!(Objects.equals(PBSelect(event.getPlayer().getUniqueId()), event.getPlayer().getName()))){
                PBUpdate(event.getPlayer().getUniqueId(),event.getPlayer().getName());
            }
        }else{
            PBInsert(event.getPlayer().getUniqueId(),event.getPlayer().getName());
        }
    }
}
