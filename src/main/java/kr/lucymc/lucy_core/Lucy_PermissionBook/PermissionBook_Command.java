package kr.lucymc.lucy_core.Lucy_PermissionBook;

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

public class PermissionBook_Command implements CommandExecutor {
    FileConfiguration config = Lucy_Core.getInstance().getConfig();
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player p) {
            if(args[0].equalsIgnoreCase("관리자")) {
                if (p.hasPermission("lucypermissionbook.staff")) {
                    if (args[1].equalsIgnoreCase("발급")) {
                        if (args[2].isEmpty()) {
                            p.sendMessage(config.getString("PermissionBook.message.noBookName"));
                        } else if (args[3].isEmpty()) {
                            p.sendMessage(config.getString("PermissionBook.message.noBookLore"));
                        }else if (args[4].isEmpty()) {
                            p.sendMessage(config.getString("PermissionBook.message.noPermission"));
                        } else {
                            ItemStack SUNFLOWER = new ItemStack(Material.BOOK, 1);
                            ItemMeta meta = SUNFLOWER.getItemMeta();
                            Objects.requireNonNull(meta).setDisplayName(config.getString("PermissionBook.NamePrefix") + args[2].replaceAll("&", "§"));
                            meta.setLore(List.of((config.getString("PermissionBook.ItemLore").replaceAll("%Permission%",args[4]).replaceAll("%ex%",args[3].replaceAll("&", "§"))).split(",")));
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
