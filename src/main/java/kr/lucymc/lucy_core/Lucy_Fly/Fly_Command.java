package kr.lucymc.lucy_core.Lucy_Fly;

import kr.lucymc.lucy_core.Lucy_Core;
import org.bukkit.Bukkit;
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

import static kr.lucymc.lucy_core.Lucy_Core.maps;
import static kr.lucymc.lucy_core.Lucy_Fly.Fly_Calculation.FlyTimeMessage;
import static kr.lucymc.lucy_core.Lucy_Fly.Fly_DB.FlyDel;
import static kr.lucymc.lucy_core.Lucy_Fly.Fly_DB.FlyInsert;

public class Fly_Command implements CommandExecutor {
    FileConfiguration config = Lucy_Core.getInstance().getConfig();
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player player) {
            if (args.length == 0) {
                if(player.getAllowFlight()) {
                    player.setAllowFlight(false);
                    player.sendMessage(Objects.requireNonNull(config.getString("Fly.Fly_OFF")));
                }else{
                    if(maps.get(player.getUniqueId().toString()) > 0|| player.hasPermission("lucyfly.infinity")) {
                        player.setAllowFlight(true);
                        player.sendMessage(Objects.requireNonNull(config.getString("Fly.Fly_ON")));
                    }else{
                        player.sendMessage(Objects.requireNonNull(config.getString("Fly.NotHaveFlyTimeMessage")));
                    }
                }
            }else if (player.hasPermission("lucyfly.staff")&&args[0].equalsIgnoreCase("발급")) {
                if(args.length == 1){
                    player.sendMessage(Objects.requireNonNull(config.getString("Fly.CouponCountNotSet")));
                }else if(args.length == 2){
                    player.sendMessage(Objects.requireNonNull(config.getString("Fly.HNotSet")));
                }else if(args.length == 3){
                    player.sendMessage(Objects.requireNonNull(config.getString("Fly.MNotSet")));
                }else if(args.length == 4){
                    player.sendMessage(Objects.requireNonNull(config.getString("Fly.SNotSet")));
                }else{
                    ItemStack Coupon = new ItemStack(Material.PAPER, Integer.parseInt(args[1]));
                    ItemMeta CouponMeta = Coupon.getItemMeta();
                    Objects.requireNonNull(CouponMeta).setDisplayName(FlyTimeMessage(config,"Fly.CouponPrefix",Integer.parseInt(args[2]) * 3600 + Integer.parseInt(args[3]) * 60 + Integer.parseInt(args[4])));
                    CouponMeta.setLore(List.of(Objects.requireNonNull(config.getString("Fly.CouponLore")).split(",")));
                    Coupon.setItemMeta(CouponMeta);
                    player.getInventory().addItem(Coupon);
                }
            }else if (player.hasPermission("lucyfly.staff")&&args[0].equalsIgnoreCase("초기화")) {
                if(args.length == 1){
                    FlyDel(player.getUniqueId());
                    maps.remove(player.getUniqueId().toString());
                    player.sendMessage(Objects.requireNonNull(config.getString("Fly.DeleteMyFlyTimeMessage")));
                    FlyInsert(player.getUniqueId(),0);
                    maps.put(""+player.getUniqueId(),0);
                }else{
                    Player inplayer = Bukkit.getPlayerExact(args[1]);
                    if(inplayer != null){
                        if(player!=inplayer){
                            FlyDel(inplayer.getUniqueId());
                            maps.remove(inplayer.getUniqueId().toString());
                            player.sendMessage(Objects.requireNonNull(config.getString("Fly.DeleteFlyTimeMessage")).replace("%a%",player.getDisplayName()).replace("%p%",inplayer.getDisplayName()));
                            inplayer.sendMessage(Objects.requireNonNull(config.getString("Fly.DeleteFlyTimeMessage")).replace("%a%",player.getDisplayName()).replace("%p%",inplayer.getDisplayName()));
                            FlyInsert(inplayer.getUniqueId(),0);
                            maps.put(""+inplayer.getUniqueId(),0);
                        }else{
                            FlyDel(player.getUniqueId());
                            maps.remove(player.getUniqueId().toString());
                            player.sendMessage(Objects.requireNonNull(config.getString("Fly.DeleteMyFlyTimeMessage")));
                            FlyInsert(player.getUniqueId(),0);
                            maps.put(""+player.getUniqueId(),0);
                        }
                    }else{
                        player.sendMessage(Objects.requireNonNull(config.getString("Fly.NotFindPlayerMessage")).replace("%p%",inplayer.getDisplayName()));
                    }
                }
            }else if(args[0].equalsIgnoreCase("남은시간")) {
                //FlyUpdate(player.getUniqueId(),maps.get(""+player.getUniqueId()));
                player.sendMessage(FlyTimeMessage(config,"Fly.CheckFlyTime",maps.get(""+player.getUniqueId())));
            }
        }
        return true;
    }
}
