package kr.lucymc.lucy_core.Lucy_PostBox;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;
import org.yaml.snakeyaml.parser.ParserException;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Base64;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import static kr.lucymc.lucy_core.Lucy_Core.connection;
import static kr.lucymc.lucy_core.Lucy_PostBox.PostBox_DB.PBSelect;
import static kr.lucymc.lucy_core.Lucy_PostBox.PostBox_DB.PBUpdate;
import static kr.lucymc.lucy_core.Lucy_PostBox.PostBox_Event.configs;
import static kr.lucymc.lucy_core.Lucy_PostBox.PostBox_YML.*;

public class PostBox_Command implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if(args.length != 0&&Objects.equals(args[0], "추천")&&sender.isOp()){
            String tableName = "userlist";
            String columnName = "UserName";
            String value = args[1];
            boolean dataExists = isDataExists(tableName, columnName, value);
            if(dataExists){
                for (int i = 0; i < getGiveItem().size();) {
                    PBItemUpdate(PBSSelect(value),getGiveItem().get(i));
                    i++;
                }
                if(Bukkit.getOfflinePlayer(value).isOnline()) {
                    Bukkit.getPlayer(value).sendMessage(configs.getString("PostBox.message.item"));
                }
            }
        }
        if (sender instanceof Player player) {
            if(args.length == 0){
                PBMenu(player,1);
            }else if(Objects.equals(args[0], "아이템설정")&&sender.isOp()){
                Inventory gui = Bukkit.createInventory(player, 9 * 3, configs.getString("PostBox.Item.prefix"));
                gui.setContents(LoadShopSlotFile());
                ItemStack glass = new ItemStack(Material.GREEN_STAINED_GLASS_PANE, 1);
                gui.setItem(18, glass);
                gui.setItem(19, glass);
                gui.setItem(20, glass);
                gui.setItem(21, glass);
                gui.setItem(22, glass);
                gui.setItem(23, glass);
                gui.setItem(24, glass);
                gui.setItem(25, glass);
                gui.setItem(26, glass);
                player.openInventory(gui);
            }else if(Objects.equals(args[0], "지급")&&sender.isOp()){
                String tableName = "userlist";
                String columnName = "UserName";
                String value = args[1];
                boolean dataExists = isDataExists(tableName, columnName, value);
                if(dataExists){
                    PBItemUpdate(PBSSelect(value), player.getItemInHand());
                    player.sendMessage(configs.getString("PostBox.message.give"));
                }
            }else if(Objects.equals(args[0], "아이템")){
                Inventory gui = Bukkit.createInventory(player, 9 * 3, configs.getString("PostBox.ShowItem.prefix"));
                gui.setContents(LoadShopSlotFile());
                ItemStack glass = new ItemStack(Material.GREEN_STAINED_GLASS_PANE, 1);
                gui.setItem(18, glass);
                gui.setItem(19, glass);
                gui.setItem(20, glass);
                gui.setItem(21, glass);
                gui.setItem(22, glass);
                gui.setItem(23, glass);
                gui.setItem(24, glass);
                gui.setItem(25, glass);
                gui.setItem(26, glass);
                player.openInventory(gui);
            }
        }
        return true;
    }
    public static void  PBItemUpdate(UUID player, ItemStack item){
        ResultSet rs = PBSelect(player);
        String tableName = "postbox";
        String columnName = "UserID";
        String value = player.toString();
        boolean dataExists = isDataExists(tableName, columnName, value);
        String DBObj = null;
        JsonArray jsonArr;
        int PrefixCount = 0;
        String BaseItemObj;
        JsonArray req_array = new JsonArray();
        try {
            ByteArrayOutputStream io = new ByteArrayOutputStream();
            BukkitObjectOutputStream os = new BukkitObjectOutputStream(io);
            os.writeObject(item);
            os.flush();
            byte[] ItemObj =  io.toByteArray();
            BaseItemObj = Base64.getEncoder().encodeToString(ItemObj);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        req_array.add(BaseItemObj);
        while (true) {
            try {
                if (!Objects.requireNonNull(rs).next()) break;
                DBObj = rs.getString("Array");
                PrefixCount = rs.getInt("Count");
            } catch (SQLException err) {
                throw new RuntimeException(err);
            }
        }
        try {
            jsonArr = (JsonArray) JsonParser.parseString(DBObj);
        } catch (ParserException e) {
            throw new RuntimeException(e);
        }
        jsonArr.add(req_array);
        PBUpdate(player,jsonArr,PrefixCount+1);
    }
    public static void PBMenu(Player p, int Page){
        //if(MPage <= Page || Page <= 1) return;
        Inventory gui = Bukkit.createInventory(p, 9 * 6, configs.getString("PostBox.GUI.name").replaceAll("%p%",p.getName()));
        ResultSet rs = PBSelect(p.getUniqueId());
        String DBObj = null;
        JsonArray jsonArr;
        while (true) {
            try {
                if (!Objects.requireNonNull(rs).next()) break;
                DBObj = rs.getString("Array");
            } catch (SQLException err) {
                throw new RuntimeException(err);
            }
        }
        try {
            jsonArr = (JsonArray) new JsonParser().parse(DBObj);
        } catch (ParserException e) {
            throw new RuntimeException(e);
        }
        long PrefixCount = jsonArr.size();
        String MaxPage = Integer.toString((int)Math.ceil((float)PrefixCount/(float)45));
        ItemStack info = new ItemStack(Material.SUNFLOWER, 1);
        ItemStack glass = new ItemStack(Material.BLACK_STAINED_GLASS_PANE, 1);
        ItemMeta infometa = info.getItemMeta();
        if(MaxPage.equals("0")){
            MaxPage = "1";
        }
        Objects.requireNonNull(infometa).setDisplayName("§f§l( §a§l"+Page+" §f/ §c§l"+MaxPage+" §f§l)");
        infometa.setLore(List.of((configs.getString("PostBox.GUI.info").replace("%have%",""+(long)jsonArr.size())).split(",")));
        info.setItemMeta(infometa);
        gui.setItem(49, info);
        gui.setItem(45, glass);
        gui.setItem(46, glass);
        gui.setItem(47, glass);
        gui.setItem(48, glass);
        gui.setItem(50, glass);
        gui.setItem(51, glass);
        gui.setItem(52, glass);
        gui.setItem(53, glass);
        if(!((int)Math.ceil((float)PrefixCount/(float)45) <= Page)){
            ItemStack NP = new ItemStack(Material.ARROW, 1);
            ItemMeta NPmeta = NP.getItemMeta();
            assert NPmeta != null;
            NPmeta.setDisplayName(configs.getString("PostBox.GUI.NextPage"));
            NP.setItemMeta(NPmeta);
            gui.setItem(53, NP);
        }
        if(!(Page <= 1)){
            ItemStack NP = new ItemStack(Material.ARROW, 1);
            ItemMeta NPmeta = NP.getItemMeta();
            assert NPmeta != null;
            NPmeta.setDisplayName(configs.getString("PostBox.GUI.BackPage"));
            NP.setItemMeta(NPmeta);
            gui.setItem(45, NP);
        }
        int Icount;
        if(Page*45 > (int)PrefixCount){
            Icount = ((int)PrefixCount-(Page-1)*45)-1;
        }else{
            Icount = 44;
        }
        for (int i = 0; i <= Icount; i++) {
            JsonElement element = jsonArr.get(i+45*(Page-1));
            ItemStack NewItem;
            try {
                ByteArrayInputStream in = new ByteArrayInputStream(Base64.getDecoder().decode(element.getAsString()));
                BukkitObjectInputStream is = new BukkitObjectInputStream(in);
                NewItem = (ItemStack) is.readObject();
            } catch (IOException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
            gui.setItem(i, NewItem);
        }

        p.openInventory(gui);
    }
    public static UUID PBSSelect(String userid) {
        String sql = "select * from userlist where UserName='"+userid+"';";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery(sql);
            UUID name = null;
            while(true){
                try {
                    if (!Objects.requireNonNull(rs).next()) break;
                    String names = rs.getString("UserID");
                    name = UUID.fromString(names);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
            rs.close();
            return name;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean isDataExists(String tableName, String columnName, String value) {
        boolean exists = false;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            String query = "SELECT COUNT(*) FROM " + tableName + " WHERE " + columnName + " = ?";
            statement = connection.prepareStatement(query);
            statement.setString(1, value);
            resultSet = statement.executeQuery();
            if (resultSet.next()) {
                int count = resultSet.getInt(1);
                exists = (count > 0);
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (resultSet != null) {
                try {
                    resultSet.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

        return exists;
    }
}
