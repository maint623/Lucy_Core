package kr.lucymc.lucy_core.Lucy_PlayTime;

import kr.lucymc.lucy_core.Lucy_Core;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;

import static kr.lucymc.lucy_core.Lucy_Core.PlayTimes;


public class PlayTime_PAPI extends PlaceholderExpansion {

    private final Lucy_Core plugin;

    public PlayTime_PAPI(Lucy_Core plugin) {
        this.plugin = plugin;
    }

    @Override
    public String getIdentifier() {
        return "LUCY-PlayTime";
    }

    @Override
    public String getAuthor() {
        return plugin.getDescription().getAuthors().toString();
    }

    @Override
    public String getVersion() {
        return plugin.getDescription().getVersion();
    }

    @Override
    public boolean persist() {
        return true;
    }

    @Override
    public String onRequest(OfflinePlayer player, String identifier) {
        if (player == null) return "";
        if(PlayTimes.containsKey(""+player.getUniqueId())) {
            if (identifier.equals("Play-Second")) {
                return String.valueOf(PlayTimes.get(""+player.getUniqueId()));
            }
        }else{
            return "R?oading..";
        }
        return null;
    }
}
