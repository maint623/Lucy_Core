package kr.lucymc.lucy_core.Lucy_Fly;

import kr.lucymc.lucy_core.Lucy_Core;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.FileConfiguration;

import static kr.lucymc.lucy_core.Lucy_Core.maps;
import static kr.lucymc.lucy_core.Lucy_Fly.Fly_Calculation.TimeFormat;

public class Fly_PAPI extends PlaceholderExpansion {

    private final Lucy_Core plugin;
    FileConfiguration config = Lucy_Core.getInstance().getConfig();

    public Fly_PAPI(Lucy_Core plugin) {
        this.plugin = plugin;
    }

    @Override
    public String getIdentifier() {
        return "LUCY-FLY";
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
        if(maps.containsKey(""+player.getUniqueId())) {
            if (identifier.equals("FLY-TIME-FORMAT")) {
                return TimeFormat(config, "Fly.TimeFormat", maps.get("" + player.getUniqueId()));
            } else if (identifier.equals("FLY-TIME-H")) {
                return String.valueOf(maps.get("" + player.getUniqueId()) / 3600);
            } else if (identifier.equals("FLY-TIME-M")) {
                return String.valueOf((maps.get("" + player.getUniqueId()) % 3600) / 60);
            } else if (identifier.equals("FLY-TIME-S")) {
                return String.valueOf(maps.get("" + player.getUniqueId()) % 60);
            } else if (player.getPlayer().isFlying()) {
                switch (identifier) {
                    case "IF-FLY-TIME-FORMAT" -> {
                        return TimeFormat(config, "Fly.TimeFormat", maps.get("" + player.getUniqueId()));
                    }
                    case "IF-FLY-TIME-H" -> {
                        return String.valueOf(maps.get("" + player.getUniqueId()) / 3600);
                    }
                    case "IF-FLY-TIME-M" -> {
                        return String.valueOf((maps.get("" + player.getUniqueId()) % 3600) / 60);
                    }
                    case "IF-FLY-TIME-S" -> {
                        return String.valueOf(maps.get("" + player.getUniqueId()) % 60);
                    }
                }
            }
        }else{
            return "R?oading..";
        }
        return null;
    }
}
