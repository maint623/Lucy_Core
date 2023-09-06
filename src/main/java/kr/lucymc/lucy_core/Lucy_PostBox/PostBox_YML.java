package kr.lucymc.lucy_core.Lucy_PostBox;

import kr.lucymc.lucy_core.Lucy_Core;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

public class PostBox_YML {

    public static File SetShopFile() {
        return new File(Lucy_Core.getInstance().getDataFolder(), "PostBox_Item.yml");
    }
    public static boolean FindShopFile() {
        File f = SetShopFile();
        if (f.exists() || f.isFile()) {
            return true;
        } else {
            return false;
        }
    }

    public static void SaveShopFile() {
        File file = SetShopFile();
        FileConfiguration configFile = YamlConfiguration.loadConfiguration(file);
        configFile.set("Shop", "test");
        try {
            configFile.save(file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void SaveShopSlotFile(Map m) {
        File file = SetShopFile();
        FileConfiguration configFile = YamlConfiguration.loadConfiguration(file);
        configFile.set("slot", m);
        try {
            configFile.save(file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public static ArrayList<ItemStack> getGiveItem() {
        File file = SetShopFile();
        FileConfiguration configFile = YamlConfiguration.loadConfiguration(file);
        ArrayList<ItemStack> items = new ArrayList();
        for (int i=0; i<=17; i++) {
            if(configFile.getItemStack("slot."+i)==null) break;
            ItemStack item = configFile.getItemStack("slot."+i);
            items.add(item);
        }
        return items;
    }

    public static ItemStack[] LoadShopSlotFile() {
        File file = SetShopFile();
        FileConfiguration configFile = YamlConfiguration.loadConfiguration(file);
        ArrayList<ItemStack> items = new ArrayList();
        for (int i=0; i<=17; i++) {
            ItemStack item = configFile.getItemStack("slot."+i);
            items.add(item);
        }
        ItemStack[] array = items.toArray(new ItemStack[0]);
        return array;
    }
}
