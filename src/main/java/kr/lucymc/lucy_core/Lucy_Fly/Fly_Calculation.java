package kr.lucymc.lucy_core.Lucy_Fly;

import org.bukkit.configuration.file.FileConfiguration;

import java.util.List;
import java.util.Objects;

public class Fly_Calculation {
    public static String TimeFormat(FileConfiguration config, String path, int seconds) {
        int hours = seconds / 3600;
        int minutes = (seconds % 3600) / 60;
        int Seconds = seconds % 60;
        List<String> CFT = List.of(Objects.requireNonNull(Objects.requireNonNull(config.getString(path)).split(",")));
        if(hours>0){
            return (CFT.get(0)+CFT.get(1)+CFT.get(2)).replace("%h%",""+hours).replace("%m%",""+minutes).replace("%s%",""+Seconds);
        }else{
            if(minutes>0){
                return (CFT.get(1)+CFT.get(2)).replace("%m%",""+minutes).replace("%s%",""+Seconds);
            }else{
                return (CFT.get(2)).replace("%s%",""+Seconds);
            }
        }
    }
    public static String FlyTimeMessage(FileConfiguration config, String path, int seconds) {
        int hours = seconds / 3600;
        int minutes = (seconds % 3600) / 60;
        int Seconds = seconds % 60;
        List<String> CFT = List.of(Objects.requireNonNull(Objects.requireNonNull(config.getString(path)).split(",")));
        if(hours>0){
            return (CFT.get(0)+CFT.get(1)+CFT.get(2)+CFT.get(3)+CFT.get(4)).replace("%h%",""+hours).replace("%m%",""+minutes).replace("%s%",""+Seconds);
        }else{
            if(minutes>0){
                return (CFT.get(0)+CFT.get(2)+CFT.get(3)+CFT.get(4)).replace("%m%",""+minutes).replace("%s%",""+Seconds);
            }else{
                return (CFT.get(0)+CFT.get(3)+CFT.get(4)).replace("%s%",""+Seconds);
            }
        }
    }
    private static int parseInteger(String str) {
        try {
            return Integer.parseInt(str.trim());
        } catch (NumberFormatException e) {
            return 0;
        }
    }
}
