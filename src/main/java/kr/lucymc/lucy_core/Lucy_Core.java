package kr.lucymc.lucy_core;

import kr.lucymc.lucy_core.Lucy_Fly.Fly_Command;
import kr.lucymc.lucy_core.Lucy_Fly.Fly_Event;
import kr.lucymc.lucy_core.Lucy_Fly.Fly_PAPI;
import kr.lucymc.lucy_core.Lucy_Fly.Fly_TabCompleter;
import kr.lucymc.lucy_core.Lucy_PermissionBook.PermissionBook_Command;
import kr.lucymc.lucy_core.Lucy_PermissionBook.PermissionBook_Event;
import kr.lucymc.lucy_core.Lucy_PermissionBook.PermissionBook_TabCompleter;
import kr.lucymc.lucy_core.Lucy_PlayTime.PlayTime_Event;
import kr.lucymc.lucy_core.Lucy_PlayTime.PlayTime_PAPI;
import kr.lucymc.lucy_core.Lucy_PostBox.PostBox_Command;
import kr.lucymc.lucy_core.Lucy_PostBox.PostBox_Event;
import kr.lucymc.lucy_core.Lucy_PostBox.PostBox_TabCompleter;
import kr.lucymc.lucy_core.Lucy_Prefix.Prefix_Command;
import kr.lucymc.lucy_core.Lucy_Prefix.Prefix_Event;
import kr.lucymc.lucy_core.Lucy_Prefix.Prefix_TabCompleter;
import kr.lucymc.lucy_core.Lucy_Utils.CommandCool.CommandCool_Event;
import kr.lucymc.lucy_core.Lucy_Utils.Crops.Crops_Event;
import kr.lucymc.lucy_core.Lucy_Utils.Crops.Crops_Keep_Command;
import kr.lucymc.lucy_core.Lucy_Utils.LevelBook.LevelBook_Command;
import kr.lucymc.lucy_core.Lucy_Utils.LevelBook.LevelBook_Event;
import kr.lucymc.lucy_core.Lucy_Utils.LevelBook.LevelBook_TabCompleter;
import kr.lucymc.lucy_core.Lucy_Utils.PVP.PVP_Command;
import kr.lucymc.lucy_core.Lucy_Utils.PVP.PVP_Event;
import kr.lucymc.lucy_core.Lucy_Utils.UUID_DB.UUID_DB_Event;
import net.luckperms.api.LuckPerms;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;
import java.util.logging.Level;

public final class Lucy_Core extends JavaPlugin {
    private static Lucy_Core INSTANCE;
    public static Lucy_Core getInstance() {
        return INSTANCE;
    }
    public static Connection connection;
    public static HashMap<String, Integer> PVPMode = new HashMap<>();
    public static HashMap<String, Integer> KeepCrops = new HashMap<>();
    public static HashMap<String, Integer> PlayTimes = new HashMap<>();
    public static HashMap<String, Integer> maps = new HashMap<>();
    public static ArrayList<Material> blocks = new ArrayList<Material>();
    public static ArrayList<Material> ClickItems = new ArrayList<Material>();
    FileConfiguration config = this.getConfig();
    public static LuckPerms api;

    @Override
    public void onEnable() {
        INSTANCE = this;
        setConfig();
        LuckPerms();
        PAPI();
        LoadPlugin();
        DBConnect();
        KeepCrops();
        ClickedItem();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
    void ClickedItem(){
        ClickItems.add(Material.FEATHER);
        ClickItems.add(Material.PAPER);
        ClickItems.add(Material.CHEST);
        ClickItems.add(Material.PLAYER_HEAD);
    }
    void KeepCrops(){
        blocks.add(Material.PUMPKIN_STEM);
        blocks.add(Material.ATTACHED_PUMPKIN_STEM);
        blocks.add(Material.MELON_STEM);
        blocks.add(Material.ATTACHED_MELON_STEM);
    }
    void LoadPlugin(){
        if(config.getBoolean("Core.Utils")){
            Lucy_Utils();
        }
        if(config.getBoolean("Core.PermissionBook")){
            Lucy_PermissionBook();
        }
        if(config.getBoolean("Core.Prefix")){
            Lucy_Prefix();
        }
        if(config.getBoolean("Core.PlayTime")){
            Lucy_PlayTime();
        }
        if(config.getBoolean("Core.PostBox")){
            Lucy_PostBox();
        }
        if(config.getBoolean("Core.Fly")){
            Lucy_Fly();
        }
    }
    void Lucy_Fly(){
        getCommand("플라이").setExecutor(new Fly_Command());
        getCommand("플라이").setTabCompleter(new Fly_TabCompleter());
        getServer().getPluginManager().registerEvents(new Fly_Event(), this);
    }
    void Lucy_PostBox(){
        getCommand("우편함").setExecutor(new PostBox_Command());
        getCommand("우편함").setTabCompleter(new PostBox_TabCompleter());
        getServer().getPluginManager().registerEvents(new PostBox_Event(), this);
    }
    void Lucy_PlayTime(){
        getServer().getPluginManager().registerEvents(new PlayTime_Event(), this);
    }
    void Lucy_Prefix(){
        getServer().getPluginManager().registerEvents(new Prefix_Event(), this);
        getCommand("칭호").setTabCompleter(new Prefix_TabCompleter());
        getCommand("칭호").setExecutor(new Prefix_Command());
    }
    void Lucy_PermissionBook(){
        getServer().getPluginManager().registerEvents(new PermissionBook_Event(), this);
        getCommand("펄미션북").setTabCompleter(new PermissionBook_TabCompleter());
        getCommand("펄미션북").setExecutor(new PermissionBook_Command());
    }
    void Lucy_Utils(){
        getServer().getPluginManager().registerEvents(new PVP_Event(), this);
        getCommand("PVP").setExecutor(new PVP_Command());
        getServer().getPluginManager().registerEvents(new CommandCool_Event(), this);
        getCommand("레벨북").setExecutor(new LevelBook_Command());
        getServer().getPluginManager().registerEvents(new LevelBook_Event(), this);
        getServer().getPluginManager().registerEvents(new UUID_DB_Event(), this);
        getCommand("레벨북").setTabCompleter(new LevelBook_TabCompleter());
        getServer().getPluginManager().registerEvents(new Crops_Event(), this);
        getCommand("줄기").setExecutor(new Crops_Keep_Command());
    }
    void LuckPerms(){
        RegisteredServiceProvider<LuckPerms> provider = Bukkit.getServicesManager().getRegistration(LuckPerms.class);
        if (provider != null) {
            api = provider.getProvider();
        }
    }
    void PAPI(){
        if (getServer().getPluginManager().getPlugin("PlaceholderAPI") != null) {
            new PlayTime_PAPI(this).register();
            new Fly_PAPI(this).register();
        } else {
            getLogger().log(Level.SEVERE, "PlaceholderAPI 플러그인을 찾을 수 없습니다.");
        }
    }
    void DBConnect(){
        try {
            connection = DriverManager.getConnection(Objects.requireNonNull(config.getString("DB.URL")), config.getString("DB.ID"), config.getString("DB.PW"));
        } catch (Exception ex) {
            getLogger().log(Level.SEVERE, "DB오류 : "+ex);
        }
    }
    void setConfig(){
        File ConfigFile = new File(getDataFolder(), "config.yml");
        if(!ConfigFile.isFile()){
            //Lucy_DB
            config.addDefault("DB.ID", "root");
            config.addDefault("DB.PW", "INTY");
            config.addDefault("DB.URL", "jdbc:mysql://127.0.0.1:3307/lucy?autoReconnect=true");
            //Lucy_Core
            config.addDefault("Core.Utils", true);
            config.addDefault("Core.PermissionBook", true);
            config.addDefault("Core.Prefix", true);
            config.addDefault("Core.PostBox", true);
            config.addDefault("Core.Fly", true);
            config.addDefault("Core.PlayTime", true);
            //Lucy_PlayTime
            config.addDefault("PlayTime.DBLoadTick", 20);
            //Lucy_Utils
            config.addDefault("pvp.message.ON", "[ §cPVP§f ] 켜짐.");
            config.addDefault("pvp.message.OFF", "[ §cPVP§f ] 꺼짐.");
            config.addDefault("CommandCool.message.X", "[ §cX§f ] X.");
            config.addDefault("LevelBook.NamePrefix", "§f[ §a레벨북§f ] ");
            config.addDefault("LevelBook.Name", "§f[ §a레벨북§f ] %exp%xp");
            config.addDefault("LevelBook.Matcher", "(.*)xp");
            config.addDefault("LevelBook.lore", "§f========[ §6정보§f ]========,§8»§f 클릭하면 레벨이?!?!,§8» §e좌클릭§f으로 §a사용,§f======================");
            config.addDefault("LevelBook.message.noxp", "[ §a레벨북§f ] xp가 입력되지 않았습니다.");
            config.addDefault("LevelBook.message.addxp", "[ §a레벨북§f ] xp가 %exp%만큼 추가 되었습니다.");
            config.addDefault("Crops.KeepCrops.message.on", "[ §a줄기§f ] 줄기 보호가 켜졌습니다.");
            config.addDefault("Crops.KeepCrops.message.off", "[ §a줄기§f ] 줄기 보호가 꺼졌습니다.");
            //Lucy_PermissionBook
            config.addDefault("PermissionBook.NamePrefix", "§f[ §b§l펄미션§f ] ");
            config.addDefault("PermissionBook.BackNamePrefix", "§f[ §c§l페이북§f ] ");
            config.addDefault("PermissionBook.ItemLore", "§f========[ §6정보§f ]========,§8»§f %ex%,§8»§f %Permission%,§8» §e좌클릭§f으로 §a사용,§f======================");
            config.addDefault("PermissionBook.ExMatcher", "§8»§f (.*)");
            config.addDefault("PermissionBook.ExIndex", 2);
            config.addDefault("PermissionBook.UseIndex", 3);
            config.addDefault("PermissionBook.message.addPermission", "§f[ §b§l펄미션§f ] %Permission%§f 펄미션를 획득하였습니다.");
            config.addDefault("PermissionBook.message.havePermission", "§f[ §b§l펄미션§f ] 이미 %Permission%§f 펄미션이 있습니다. 페이북으로 지급합니다.");
            config.addDefault("PermissionBook.message.noBookName", "§f[ §b§l펄미션§f ] 펄미션 북의 이름이 없습니다.");
            config.addDefault("PermissionBook.message.noBookLore", "§f[ §b§l펄미션§f ] 펄미션 북의 설명이 없습니다.");
            config.addDefault("PermissionBook.message.noPermission", "§f[ §b§l펄미션§f ] 펄미션이 없습니다.");
            //Lucy_Prefix
            config.addDefault("Prefix.NamePrefix", "§f[ §b§l칭호§f ] ");
            config.addDefault("Prefix.ItemLore", "§f========[ §6정보§f ]========,§8» %ex%,§8» §e좌클릭§f으로 §a사용,§f======================");
            config.addDefault("Prefix.ExMatcher", "§8» (.*)");
            config.addDefault("Prefix.ExIndex", 1);
            config.addDefault("Prefix.message.noPrefixName", "§f[ §b§l칭호§f ] 칭호의 이름이 없습니다.");
            config.addDefault("Prefix.message.noPrefixLore", "§f[ §b§l칭호§f ] 칭호의 설명이 없습니다.");
            config.addDefault("Prefix.message.UsePrefix", "§f[ §b§l칭호§f ] %prefix%§f 칭호를 장착하였습니다.");
            config.addDefault("Prefix.message.DelPrefix", "§f[ §b§l칭호§f ] %prefix%§f 칭호를 삭제하였습니다.");
            config.addDefault("Prefix.message.addPrefix", "§f[ §b§l칭호§f ] %prefix%§f 칭호를 획득하였습니다.");
            config.addDefault("Prefix.message.havePrefix", "§f[ §b§l칭호§f ] 이미 %prefix%§f 칭호가 있습니다.");
            config.addDefault("Prefix.message.unSetPrefix", "§f[ §b§l칭호§f ] %prefix%§f 칭호를 장착 해제 하였습니다.");
            config.addDefault("Prefix.message.noSetPrefix", "§f[ §b§l칭호§f ] %prefix%§f 칭호를 장착 하고있지 않습니다.");
            config.addDefault("Prefix.Gui.info", "§f=========[ §6정보§f ]=========,§8» §e소지중§f : %have%,§f========================");
            config.addDefault("Prefix.Gui.BackPage", "§c§l이전 페이지");
            config.addDefault("Prefix.Gui.NextPage", "§a§l다음 페이지");
            config.addDefault("Prefix.Gui.user.Name", "§f[ §b§l칭호§f ] %p%님의 칭호");
            config.addDefault("Prefix.Gui.user.ItemLore", "§f=========[ §6정보§f ]=========,§8» %ex%,§8» §e좌클릭§f으로 §a장착,§8» §e우클릭§f으로 §c장착 해제,§f========================");
            config.addDefault("Prefix.Gui.user.Prefix", "§f[ §b§l칭호§f ] ");
            config.addDefault("Prefix.Gui.staff.SettingName", "§f[ §b§l칭호 관리§f ] %p%님의 칭호");
            config.addDefault("Prefix.Gui.staff.SettingNameMatcher", "§f\\[ §b§l칭호 관리§f \\] (.*)님의 칭호");
            config.addDefault("Prefix.Gui.staff.SettingPrefix", "§f[ §b§l칭호 관리§f ] ");
            config.addDefault("Prefix.Gui.staff.ItemLore", "§f=========[ §6정보§f ]=========,§8» %ex%,§8» §e좌클릭§f으로 §c삭제,§f========================");
            //Lucy_PostBox
            config.addDefault("PostBox.message.save", "[ 우편함 ] 아이템 저장.");
            config.addDefault("PostBox.message.give", "[ 우편함 ] 아이템 지급.");
            config.addDefault("PostBox.message.item", "[ 우편함 ] 추천 보상이 지급 되었습니다.");
            config.addDefault("PostBox.message.full", "[ 우편함 ] 인벤토리에 공간이 없습니다.");
            config.addDefault("PostBox.GUI.info", "§f=========[ §6정보§f ]=========,§8» §e우편§f : %have%,§f========================");
            config.addDefault("PostBox.GUI.prefix", "§f[ §b§l우편함§f ] ");
            config.addDefault("PostBox.GUI.name", "§f[ §b§l우편함§f ] %p%님의 우편함");
            config.addDefault("PostBox.GUI.BackPage", "§c§l이전 페이지");
            config.addDefault("PostBox.GUI.NextPage", "§a§l다음 페이지");
            config.addDefault("PostBox.Item.prefix", "§f[ §6아이템 설정 §f]");
            config.addDefault("PostBox.ShowItem.prefix", "§f[ §6아이템 §f]");
            //Lucy_Fly
            config.addDefault("Fly.NotHaveFlyTimeMessage", "[ 플라이 ] 플라이 시간이 부족합니다.");
            config.addDefault("Fly.HaveFlyTime", "[ 플라이 ] ,%h%시 ,%m%분 ,%s%초, 남음");
            config.addDefault("Fly.CouponPrefix", "[ 플라이 ] 플라이 쿠폰 ( ,%h%시 ,%m%분 ,%s%초, )");
            config.addDefault("Fly.CouponLore", "§f플라이 쿠폰,§f좌클릭 사용");
            config.addDefault("Fly.AddFlyTime", "[ 플라이 ] ,%h%시 ,%m%분 ,%s%초, 증가");
            config.addDefault("Fly.JoinFlyingMessage", "[ 플라이 ] 플라이가 켜져있습니다");
            config.addDefault("Fly.Fly_ON", "[ 플라이 ] 플라이를 켰습니다");
            config.addDefault("Fly.Fly_OFF", "[ 플라이 ] 플라이를 껐습니다");
            config.addDefault("Fly.ServerReloadKick", "서버 리로드중");
            config.addDefault("Fly.CheckFlyTime", "[ 플라이 ] ,%h%시 ,%m%분 ,%s%초, 남음");
            config.addDefault("Fly.TimeFormat", "%h%시 ,%m%분 ,%s%초");
            config.addDefault("Fly.H", "시");
            config.addDefault("Fly.M", "분");
            config.addDefault("Fly.S", "초");
            config.addDefault("Fly.DBLoadTick", 3);
            config.addDefault("Fly.ActionBar", false);
            config.addDefault("Fly.DeleteFlyTimeMessage", "[ 플라이 ] %a%님이 %p%님의 플라이 시간을 초기화 하였습니다.");
            config.addDefault("Fly.NotFindPlayerMessage", "[ 플라이 ] %p% 유저는 존재하지 않습니다.");
            config.addDefault("Fly.DeleteMyFlyTimeMessage", "[ 플라이 ] 플라이 시간을 초기화 하였습니다.");
            config.addDefault("Fly.CouponCountNotSet", "[ 플라이 ] 쿠폰 갯수를 입력해 주세요");
            config.addDefault("Fly.HNotSet", "[ 플라이 ] 시간를 입력해 주세요");
            config.addDefault("Fly.MNotSet", "[ 플라이 ] 분를 입력해 주세요");
            config.addDefault("Fly.SNotSet", "[ 플라이 ] 초를 입력해 주세요");
            config.options().copyDefaults(true);
            saveConfig();
        }
    }
}
