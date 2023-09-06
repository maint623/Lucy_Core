package kr.lucymc.lucy_core.Lucy_Utils.PVP;

import kr.lucymc.lucy_core.Lucy_Core;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import static kr.lucymc.lucy_core.Lucy_Core.PVPMode;
import static kr.lucymc.lucy_core.Lucy_Utils.PVP.PVP_DB.PVPUpdate;

public class PVP_Command implements CommandExecutor {
    FileConfiguration config = Lucy_Core.getInstance().getConfig();
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player p) {
            if(PVPMode.get(""+p.getUniqueId()) == 0){
                PVPMode.put(""+p.getUniqueId(),1);
                p.sendMessage(config.getString("pvp.message.ON"));
                PVPUpdate(p.getUniqueId(),PVPMode.get(""+p.getUniqueId()));
            }else{
                PVPMode.put(""+p.getUniqueId(),0);
                p.sendMessage(config.getString("pvp.message.OFF"));
                PVPUpdate(p.getUniqueId(),PVPMode.get(""+p.getUniqueId()));
            }
        }
        return true;
    }
}
