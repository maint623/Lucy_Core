package kr.lucymc.lucy_core.Lucy_Utils.Crops;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import java.util.ArrayList;
import java.util.List;

import static kr.lucymc.lucy_core.Lucy_Core.KeepCrops;
import static kr.lucymc.lucy_core.Lucy_Core.blocks;

public class Crops_Event implements Listener {

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event){
        if(KeepCrops.containsKey(event.getPlayer().getUniqueId().toString())) {
            Block b = event.getBlock();
            if (blocks.contains(b.getType())) {
                event.setCancelled(true);
            }
        }
    }
}
