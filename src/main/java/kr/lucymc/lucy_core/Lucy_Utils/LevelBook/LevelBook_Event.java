package kr.lucymc.lucy_core.Lucy_Utils.LevelBook;

import kr.lucymc.lucy_core.Lucy_Core;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import java.util.Objects;
import java.util.logging.Level;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.bukkit.Bukkit.getLogger;

public class LevelBook_Event implements Listener {
    FileConfiguration configs = Lucy_Core.getInstance().getConfig();
    @EventHandler
    public void PrefixBookClick(PlayerInteractEvent event) {
        if(!(event.getHand() == EquipmentSlot.HAND)) return;
        Player player = event.getPlayer();
        if(player.getItemInHand().hasItemMeta()) {
            if (Objects.requireNonNull(player.getItemInHand().getItemMeta()).hasDisplayName()) {
                if (player.getItemInHand().getItemMeta().getDisplayName().startsWith(configs.getString("LevelBook.NamePrefix"))) {
                    String inputString = player.getItemInHand().getItemMeta().getDisplayName();
                    String pattern = configs.getString("LevelBook.Matcher");
                    Pattern r = Pattern.compile(pattern);
                    Matcher m = r.matcher(inputString.replace(configs.getString("LevelBook.NamePrefix"),""));
                    String names = null;
                    if (m.find()) {
                        names = m.group(1).replaceAll("§[0-9a-fk-or]", "");
                    } else {
                        getLogger().log(Level.SEVERE, "[ 레벨북 ] 패턴과 일치하는 부분을 찾을 수 없습니다. (config.yml에 LevelBook.Matcher를 수정 해주세요.)");
                    }
                    if (!names.isEmpty()) {
                        try {
                            player.giveExp(Integer.parseInt(names.trim()));
                        } catch (NumberFormatException e) {
                            getLogger().log(Level.SEVERE, "[ 레벨북 ] XP 값으로 변환할 수 없습니다. : " + names);
                        }
                        if (player.getInventory().getItemInHand().getAmount() == 1) {
                            player.getInventory().setItemInHand(new ItemStack(Material.AIR));
                        } else {
                            player.getInventory().getItemInHand().setAmount(player.getInventory().getItemInHand().getAmount()-1);
                        }
                        player.sendMessage(configs.getString("LevelBook.message.addxp").replace("%exp%",names));
                    }
                }
            }
        }
    }
}
