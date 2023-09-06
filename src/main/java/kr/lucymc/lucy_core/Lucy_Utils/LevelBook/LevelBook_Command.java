package kr.lucymc.lucy_core.Lucy_Utils.LevelBook;

import kr.lucymc.lucy_core.Lucy_Core;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;
import java.util.Objects;

public class LevelBook_Command implements CommandExecutor {
    FileConfiguration config = Lucy_Core.getInstance().getConfig();
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player p) {
            if(args[0].equalsIgnoreCase("관리자")) {
                if (p.hasPermission("lucylevelbook.staff")) {
                    if (args[1].equalsIgnoreCase("발급")) {
                        if (args[2].isEmpty()) {
                            p.sendMessage(config.getString("LevelBook.message.noxp"));
                        } else {
                            ItemStack SUNFLOWER = new ItemStack(Material.BOOK, 1);
                            ItemMeta meta = SUNFLOWER.getItemMeta();
                            Objects.requireNonNull(meta).setDisplayName(config.getString("LevelBook.Name").replaceAll("%exp%",args[2]));
                            meta.setLore(List.of(config.getString("LevelBook.lore").split(",")));
                            SUNFLOWER.setItemMeta(meta);
                            p.getInventory().addItem(SUNFLOWER);
                        }
                    }
                }
            }
        }
        return true;
    }
}
