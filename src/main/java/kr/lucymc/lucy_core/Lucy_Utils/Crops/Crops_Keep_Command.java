package kr.lucymc.lucy_core.Lucy_Utils.Crops;

import kr.lucymc.lucy_core.Lucy_Core;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import static kr.lucymc.lucy_core.Lucy_Core.KeepCrops;

public class Crops_Keep_Command implements CommandExecutor {
    FileConfiguration config = Lucy_Core.getInstance().getConfig();
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player p) {
            if(KeepCrops.containsKey(p.getUniqueId().toString())){
                KeepCrops.remove(p.getUniqueId().toString());
                p.sendMessage(config.getString("Crops.KeepCrops.message.off"));
            }else {
                KeepCrops.put(p.getUniqueId().toString(), 1);
                p.sendMessage(config.getString("Crops.KeepCrops.message.on"));
            }
        }
        return true;
    }
}
