package xyz.magstar.minearea;

import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.List;

import static com.google.common.primitives.Ints.asList;

public class Main extends JavaPlugin implements Listener {
    FileConfiguration cfg = YamlConfiguration.loadConfiguration(new File(getDataFolder() + "/config.yml"));
    String prefix = cfg.getString("Prefix");
    String noPerm = cfg.getString("Messages.noPerm");
    public boolean contain (Object o, List l){
        boolean b = false;
        for (Object obj : l) {
            if (obj == o) {
                b = true;
                return b;
            }
        }
        return b;
    }
    public void onEnable() {
        getLogger().info("================================");
        getLogger().info(" ");
        getLogger().info("§b插件名称: §fMagStarMineArea");
        getLogger().info("§b插件作者: §fBerry_so");
        getLogger().info("§b工作室: §f星星逹 Stars Creation");
        getLogger().info(" ");
        getLogger().info("================================");
        saveDefaultConfig();
        getLogger().info("          §a>>> 注册 <<<");
        List<String> world = (List<String>) cfg.get("EffectiveWorld");
        assert world != null;
        for (String a : world){
            getLogger().info(prefix + "§f[§a✔§f]检测到配置中世界: " + a);
        }
        getLogger().info("         §a>>> 注册完成 <<<");
        Server s = getServer();
        s.getPluginManager().registerEvents(this, this);
        Objects.requireNonNull(Bukkit.getPluginCommand("msma")).setTabCompleter(new TabHandler());
        String[] version = Bukkit.getMinecraftVersion().split("\\.");
        String major = version[0];
        String minor = version[1];
        String NMSBaseHead = "net.minecraft.server.v" + major + "_" + minor + "_R";
        for (int i = 1; i <= 9; i++){
            String versions = NMSBaseHead + i;
            try {
                Class.forName(versions + ".ItemStack");
                getLogger().info(prefix + "§f[§a✔§f]检测到NMS包对应服务器版本: " + major + "_" + minor + "_R" + i);
                break;
            }catch(ClassNotFoundException ignored){}
        }
    }
    public String removeColor (String s) {
        String a = "§1";
        String b = "§2";
        String c = "§3";
        String d = "§4";
        String e = "§5";
        String f = "§6";
        String g = "§7";
        String h = "§8";
        String i = "§9";
        String j = "§0";
        String k = "§a";
        String l = "§b";
        String m = "§c";
        String n = "§d";
        String o = "§e";
        String p = "§f";
        String q = "§o";
        String r = "§m";
        String t = "§n";
        s = s.replace(k,"");
        s = s.replace(j,"");
        s = s.replace(i,"");
        s = s.replace(h,"");
        s = s.replace(g,"");
        s = s.replace(f,"");
        s = s.replace(e,"");
        s = s.replace(d,"");
        s = s.replace(c,"");
        s = s.replace(b,"");
        s = s.replace(a,"");
        s = s.replace(l,"");
        s = s.replace(m,"");
        s = s.replace(n,"");
        s = s.replace(o,"");
        s = s.replace(p,"");
        s = s.replace(q,"");
        s = s.replace(r,"");
        s = s.replace(t,"");
        return s;
    }
    public boolean ifPlayerInFile (Player p){
        File f = new File(getDataFolder()+ "/PlayerData/playerdata.yml");
        FileConfiguration playerdata = YamlConfiguration.loadConfiguration(f);
        ConfigurationSection sec = playerdata.getConfigurationSection("PlayerData");
        boolean b = false;
        if (sec != null){
            Set<String> player = sec.getKeys(false);
            for (String name : player) {
                if (Objects.equals(name, p.getName())){
                    b = true;
                    break;
                }
            }
        }
        return !b;
    }
    public void saveFile (File file, FileConfiguration F) {
        try {
            F.save(file);
        }
        catch (IOException e) {
            Bukkit.getConsoleSender().sendMessage(prefix + "§c尝试生成数据文件夹失败！报错信息已在后台生成↑↑↑");
        }
    }
    public void createDataFile (Player sender, Player p){
        File f = new File(getDataFolder()+ "/PlayerData/playerdata.yml");
        if (!f.exists()) {
            if (!f.getParentFile().exists()){
                f.getParentFile().mkdirs();
            }
            try{
                if (sender.hasPermission("magstarminearea.reload")){
                    sender.sendMessage(prefix + "§a正在尝试储存玩家" + p.getName() + "§a的数据时发现playerdata不存在");
                    sender.sendMessage(prefix + "§a正在生成文件playerdata.yml");
                }
                f.createNewFile();
            }catch (IOException e){
                p.sendMessage(prefix + "§c尝试生成数据文件失败！请联系管理员或服主！");
                getLogger().warning(prefix + "§c尝试生成数据文件夹失败！报错信息已生成↑↑↑");
                e.printStackTrace();
            }
        }
        FileConfiguration playerdata = YamlConfiguration.loadConfiguration(f);
        playerdata.set ("PlayerData." + p.getName() + ".Level", 0);
        playerdata.set ("PlayerData." + p.getName() + ".Exp", 0);
        saveFile(f,playerdata);
        p.sendMessage(prefix + cfg.get("Messages.AddPlayerSuccess"));
    }
    public void fileAddPlayer (Player p) {
        File f = new File(getDataFolder()+ "/PlayerData/playerdata.yml");
        FileConfiguration playerdata = YamlConfiguration.loadConfiguration(f);
        ConfigurationSection sec = playerdata.getConfigurationSection("PlayerData");
        assert sec != null;
        Set<String> name = sec.getKeys(false);
        boolean b = false;
        for (String a : name){
            if (a.equalsIgnoreCase(p.getName())){
                b = true;
                break;
            }
        }
            playerdata.set ("PlayerData." + p.getName() + ".Level", 0);
            playerdata.set ("PlayerData." + p.getName() + ".Exp", 0);
            saveFile(f,playerdata);
            p.sendMessage(prefix + cfg.get("Messages.AddPlayerSuccess"));
    }
    public void fileRemovePlayer (Player p) {
        File f = new File(getDataFolder()+ "/PlayerData/playerdata.yml");
        FileConfiguration playerdata = YamlConfiguration.loadConfiguration(f);
        ConfigurationSection sec = playerdata.getConfigurationSection("PlayerData");
        assert sec != null;
        Set<String> name = sec.getKeys(false);
        boolean b = false;
        for (String a : name){
            if (a.equalsIgnoreCase(p.getName())){
                b = true;
                break;
            }
        }
        if (b) {
            p.sendMessage((String) Objects.requireNonNull(cfg.get("Messages.AlreadyIs")));
            playerdata.set ("PlayerData." + p.getName(), null);
            saveFile(f,playerdata);
            p.sendMessage((String) Objects.requireNonNull(cfg.get("Messages.RemPlayerSuccess")));
        }
    }
    public List<Material> getRegisteredBlocks () {
        List<Material> mats = new ArrayList<>();
        Set<String> type = cfg.getConfigurationSection("RegisteredBlock").getKeys(false);
        for (String mat : type) {
            mats.add(Material.getMaterial(mat));
        }
        return mats;
    }
    public HashMap<Material, Integer> getExpMap() {
        HashMap<Material, Integer> map = new HashMap<>();
        List<Material> mats = getRegisteredBlocks();
        for(Material mat : mats) {
            Integer exp = (Integer) cfg.get("RegisteredBlock." + mat.toString() + ".ExpPerBlock");
            map.put(mat, exp);
        }
        return map;
    }
    public void addExp (Player p, Block b) {
        File f = new File(getDataFolder()+ "/PlayerData/playerdata.yml");
        FileConfiguration playerdata = YamlConfiguration.loadConfiguration(f);
        List<Material> mats = getRegisteredBlocks();
        HashMap<Material, Integer> map = getExpMap();
        for (Material mat : mats) {
            if (b.getType() == mat) {
                Integer exp = map.get(mat);
                Integer exp0 = (Integer) playerdata.get("PlayerData." + p.getName() + ".Exp");
                exp0 = exp0 + exp;
                Integer explvl = (Integer) cfg.get("ExpPerLevel");
                if (exp0 >= explvl) {
                    exp0 = exp0 - explvl;
                    Integer lvl = (Integer) playerdata.get("PlayerData." + p.getName() + ".Level");
                    lvl = lvl + 1;
                    playerdata.set("PlayerData." + p.getName() + ".Exp", exp0);
                    playerdata.set("PlayerData." + p.getName() + ".Level", lvl);
                    saveFile(f,playerdata);
                } else {
                    playerdata.set("PlayerData." + p.getName() + ".Exp", exp0);
                    saveFile(f,playerdata);
                }
            }
        }
    }
    public void ReachableItemHandlerGUI(Player p) {
        Inventory inv = Bukkit.createInventory(Bukkit.getPlayer(p.getName()), 6*9, (String) Objects.requireNonNull(cfg.get("ItemSetGui.Name")));
        ItemStack side = new ItemStack(Objects.requireNonNull(Material.getMaterial((String) Objects.requireNonNull(cfg.get("ItemSetGui.Side.Id")))));
        ItemMeta sidem = side.getItemMeta();
        sidem.setDisplayName((String) cfg.get("ItemSetGui.Side.Display"));
        sidem.setLore((List<String>)cfg.get("ItemSetGui.Side.Lore"));
        side.setItemMeta(sidem);
        List<String> itemlore = (List<String>) cfg.get("ItemSetGui.ItemLore");
        int i = 9;
        inv.setItem(0, side);
        inv.setItem(1, side);
        inv.setItem(2, side);
        inv.setItem(3, side);
        inv.setItem(4, side);
        inv.setItem(5, side);
        inv.setItem(6, side);
        inv.setItem(7, side);
        inv.setItem(8, side);
        inv.setItem(45, side);
        inv.setItem(46, side);
        inv.setItem(47, side);
        inv.setItem(48, side);
        inv.setItem(49, side);
        inv.setItem(50, side);
        inv.setItem(51, side);
        inv.setItem(52, side);
        inv.setItem(53, side);
        for (Material m : getRegisteredBlocks()){
            ItemStack block = new ItemStack(m);
            ItemMeta blockm = block.getItemMeta();
            blockm.setLore(itemlore);
            block.setItemMeta(blockm);
            inv.setItem(i,block);
            i++;
        }
        Objects.requireNonNull(Bukkit.getPlayer(p.getName())).openInventory(inv);
    }
    public boolean isNum(String str)
    {
        return str.matches("^[-+]?(([0-9]+)([.]([0-9]+))?|([.]([0-9]+))?)$");
    }
    public void saveItemData(ItemStack cursor, File f, int chance, boolean enable, int i){
        FileConfiguration data = YamlConfiguration.loadConfiguration(f);
        data.set("ItemHandlerList." + i + ".ItemData", cursor);
        data.set("ItemHandlerList." + i + ".Chance", chance);
        data.set("ItemHandlerList." + i + ".Enable", enable);
        saveFile(f, data);
    }
    public void removeItemData(File f, int i){
        FileConfiguration data = YamlConfiguration.loadConfiguration(f);
        data.set("ItemHandlerList." + i + ".ItemData", null);
        data.set("ItemHandlerList." + i + ".Chance", null);
        data.set("ItemHandlerList." + i + ".Enable", null);
        data.set("ItemHandlerList." + i, null);
        saveFile(f, data);
    }
    public Integer booleanToInt(boolean b) {
        int t = 1;
        int f = 0;
        if (b) {
            return t;
        }else {
            return f;
        }
    }
    public Boolean intToBoolean(int i){
        if (i == 1){
            return true;
        }else if(i == 0) {
            return false;
        }
        return false;
    }
    public void DropItemHandlerGui(Player p, String s){
        Inventory inv = Bukkit.createInventory(Bukkit.getPlayer(p.getName()), 6*9, (String) cfg.get("DropSetGui.Name") + ":" + s);
        ItemStack side1 = new ItemStack(Objects.requireNonNull(Material.getMaterial((String) cfg.get("DropSetGui.Side1.Id"))));

        ItemMeta side1m = side1.getItemMeta();
        side1m.setDisplayName((String) cfg.get("DropSetGui.Side1.Display"));
        side1m.setLore((List<String>)cfg.get("DropSetGui.Side1.Lore"));
        side1.setItemMeta(side1m);

        ItemStack side2 = new ItemStack(Objects.requireNonNull(Material.getMaterial((String) cfg.get("DropSetGui.Side2.Id"))));
        ItemMeta side2m = side2.getItemMeta();
        side2m.setDisplayName((String) cfg.get("DropSetGui.Side2.Display"));
        side2m.setLore((List<String>)cfg.get("DropSetGui.Side2.Lore"));
        side2.setItemMeta(side2m);

        ItemStack Chance = new ItemStack(Objects.requireNonNull(Material.getMaterial((String) cfg.get("DropSetGui.Chance.Id"))));
        ItemMeta Chancem = Chance.getItemMeta();
        Chancem.setDisplayName((String) cfg.get("DropSetGui.Chance.Display"));
        List<String> lore = (List<String>)cfg.get("DropSetGui.Chance.Lore");
        Chancem.setLore((List<String>)cfg.get("DropSetGui.Chance.Lore"));
        Chance.setItemMeta(Chancem);

        ItemStack Enable = new ItemStack(Objects.requireNonNull(Material.getMaterial((String) cfg.get("DropSetGui.Enable.Id"))));
        ItemMeta Enablem = Enable.getItemMeta();
        Enablem.setDisplayName((String) cfg.get("DropSetGui.Enable.Display"));
        Enablem.setLore((List<String>)cfg.get("DropSetGui.Enable.Lore"));
        Enable.setItemMeta(Enablem);

        ItemStack Disable = new ItemStack(Objects.requireNonNull(Material.getMaterial((String) cfg.get("DropSetGui.Disable.Id"))));
        ItemMeta Disablem = Disable.getItemMeta();
        Disablem.setDisplayName((String) cfg.get("DropSetGui.Disable.Display"));
        Disablem.setLore((List<String>)cfg.get("DropSetGui.Disable.Lore"));
        Disable.setItemMeta(Disablem);
        File f = new File(getDataFolder() + "/DropItemSetData/" + s + ".yml");
        if (!f.exists()){
            if (!f.getParentFile().exists()){
                f.getParentFile().mkdirs();
            }
            try{
                f.createNewFile();
            }catch(IOException ea){
                ea.printStackTrace();
                getLogger().warning(prefix + "§c尝试生成数据文件夹失败！报错信息已生成↑↑↑");
            }
        }
        inv.setItem(0, side1);
        inv.setItem(4, side1);
        inv.setItem(8, side1);
        inv.setItem(9, side1);
        inv.setItem(13, side1);
        inv.setItem(17, side1);
        inv.setItem(18, side1);
        inv.setItem(22, side1);
        inv.setItem(26, side1);
        inv.setItem(27, side1);
        inv.setItem(31, side1);
        inv.setItem(35, side1);
        inv.setItem(36, side1);
        inv.setItem(40, side1);
        inv.setItem(44, side1);
        inv.setItem(45, side1);
        inv.setItem(49, side1);
        inv.setItem(53, side1);
        inv.setItem(1, side2);
        inv.setItem(2, side2);
        inv.setItem(3, side2);
        inv.setItem(5, side2);
        inv.setItem(6, side2);
        inv.setItem(7, side2);
        inv.setItem(10, side2);
        inv.setItem(11, side2);
        inv.setItem(12, side2);
        inv.setItem(14, side2);
        inv.setItem(15, side2);
        inv.setItem(16, side2);
        inv.setItem(19, side2);
        inv.setItem(20, side2);
        inv.setItem(21, side2);
        inv.setItem(23, side2);
        inv.setItem(24, side2);
        inv.setItem(25, side2);
        inv.setItem(28, side2);
        inv.setItem(29, side2);
        inv.setItem(30, side2);
        inv.setItem(32, side2);
        inv.setItem(33, side2);
        inv.setItem(34, side2);
        inv.setItem(37, side2);
        inv.setItem(38, side2);
        inv.setItem(39, side2);
        inv.setItem(41, side2);
        inv.setItem(42, side2);
        inv.setItem(43, side2);
        inv.setItem(46, side2);
        inv.setItem(47, side2);
        inv.setItem(48, side2);
        inv.setItem(50, side2);
        inv.setItem(51, side2);
        inv.setItem(52, side2);
        HashMap<Integer, Integer> map = new HashMap<Integer, Integer>();
        map.put(1,1);
        map.put(2,5);
        map.put(3,10);
        map.put(4,14);
        map.put(5,19);
        map.put(6,23);
        map.put(7,28);
        map.put(8,32);
        map.put(9,37);
        map.put(10,41);
        map.put(11,46);
        map.put(12,50);
            File g = new File (getDataFolder() + "/DropItemSetData/" + s + ".yml");
            FileConfiguration cfg = YamlConfiguration.loadConfiguration(g);
            ConfigurationSection sec = cfg.getConfigurationSection("ItemHandlerList");
            Set<String> keysset;
            int keys = 0;
            if (sec != null){
                keysset = sec.getKeys(false);
                keys = keysset.size();
            }
            for(int lm= 1; lm<= keys; lm++){
                ItemStack is = cfg.getItemStack("ItemHandlerList." + lm + ".ItemData");
                Integer chance = cfg.getInt("ItemHandlerList." + lm + ".Chance");
                boolean b = cfg.getBoolean("ItemHandlerList." + lm + ".Enable");
                if (is != null){
                    int slot = map.get(lm);
                    inv.setItem(slot, is);
                    List<String> lorec = Chance.getLore();
                    assert lorec != null;
                    lorec.add("§f当前权重: " + chance);
                    ItemMeta meta = Chance.getItemMeta();
                    meta.setLore(lorec);
                    Chance.setItemMeta(meta);
                    inv.setItem(slot+1, Chance);
                    meta.setLore(lore);
                    Chance.setItemMeta(meta);
                    if (b) {
                        inv.setItem(slot+2, Enable);
                    }else{
                        inv.setItem(slot+2,Disable);
                    }
                }
            }
        Objects.requireNonNull(Bukkit.getPlayer(p.getName())).openInventory(inv);
    }
    public void dropItem(ItemStack s, Block b){
        b.getWorld().dropItem(b.getLocation(), s);
    }
    @EventHandler
    public void onClickDropItemSetGUI(InventoryClickEvent e){
        List<Integer> slot1 = asList(1, 5, 10, 14, 19, 23, 28, 32, 37, 41, 46, 50);
        List<Integer> slot2 = new ArrayList<>();
        for (int slot : slot1){
            slot2.add(slot + 1);
        }
        List<Integer> slot3 = new ArrayList<>();
        for (int slot : slot2){
            slot3.add(slot + 1);
        }
        Inventory inv = e.getInventory();
        Player p = (Player) e.getWhoClicked();
        ItemStack target = e.getCurrentItem();
        ItemStack cursor = e.getCursor();
        String title = p.getOpenInventory().getTitle();

        ItemStack side1 = new ItemStack(Objects.requireNonNull(Material.getMaterial((String) cfg.get("DropSetGui.Side1.Id"))));
        ItemMeta side1m = side1.getItemMeta();
        side1m.setDisplayName((String) cfg.get("DropSetGui.Side1.Display"));
        side1m.setLore((List<String>)cfg.get("DropSetGui.Side1.Lore"));
        side1.setItemMeta(side1m);

        ItemStack side2 = new ItemStack(Objects.requireNonNull(Material.getMaterial((String) cfg.get("DropSetGui.Side2.Id"))));
        ItemMeta side2m = side2.getItemMeta();
        side2m.setDisplayName((String) cfg.get("DropSetGui.Side2.Display"));
        side2m.setLore((List<String>)cfg.get("DropSetGui.Side2.Lore"));
        side2.setItemMeta(side2m);

        ItemStack Chance = new ItemStack(Objects.requireNonNull(Material.getMaterial((String) cfg.get("DropSetGui.Chance.Id"))));
        ItemMeta Chancem = Chance.getItemMeta();
        Chancem.setDisplayName((String) cfg.get("DropSetGui.Chance.Display"));
        List<String> lore = (List<String>)cfg.get("DropSetGui.Chance.Lore");
        Chancem.setLore(lore);
        Chance.setItemMeta(Chancem);

        ItemStack Enable = new ItemStack(Objects.requireNonNull(Material.getMaterial((String) cfg.get("DropSetGui.Enable.Id"))));
        ItemMeta Enablem = Enable.getItemMeta();
        Enablem.setDisplayName((String) cfg.get("DropSetGui.Enable.Display"));
        Enablem.setLore((List<String>)cfg.get("DropSetGui.Enable.Lore"));
        Enable.setItemMeta(Enablem);

        ItemStack Disable = new ItemStack(Objects.requireNonNull(Material.getMaterial((String) cfg.get("DropSetGui.Disable.Id"))));
        ItemMeta Disablem = Disable.getItemMeta();
        Disablem.setDisplayName((String) cfg.get("DropSetGui.Disable.Display"));
        Disablem.setLore((List<String>)cfg.get("DropSetGui.Disable.Lore"));
        Disable.setItemMeta(Disablem);
        if (title.contains((String) Objects.requireNonNull(cfg.get("DropSetGui.Name")))){
            String[] arg = title.split(":");
            File f = new File(getDataFolder() + "/DropItemSetData/" + arg[1] + ".yml");
            ItemStack mat0 = new ItemStack(Objects.requireNonNull(Material.getMaterial(arg[1])));
            String mat = mat0.getItemMeta().getDisplayName();
            FileConfiguration itemdata = YamlConfiguration.loadConfiguration(f);
            ConfigurationSection sec = itemdata.getConfigurationSection("ItemHandlerList");
            if (target != null){
                if (target.getType() == side2.getType()){
                    assert cursor != null;
                    if (cursor.getType() != Material.AIR){
                        if (slot1.contains(e.getRawSlot())){
                            e.setCancelled(true);
                            ItemStack cur = new ItemStack(cursor);
                            cur.setAmount(1);
                            inv.setItem(e.getRawSlot(), cur);
                            inv.setItem(e.getRawSlot() + 1, Chance);
                            inv.setItem(e.getRawSlot() + 2, Enable);
                        }else if (slot2.contains(e.getRawSlot())){
                            e.setCancelled(true);
                            ItemStack cur = new ItemStack(cursor);
                            cur.setAmount(1);
                            inv.setItem(e.getRawSlot() - 1, cur);
                            inv.setItem(e.getRawSlot(), Chance);
                            inv.setItem(e.getRawSlot() + 1, Enable);
                        }else if (slot3.contains(e.getRawSlot())){
                            e.setCancelled(true);
                            ItemStack cur = new ItemStack(cursor);
                            cur.setAmount(1);
                            inv.setItem(e.getRawSlot() - 2, cur);
                            inv.setItem(e.getRawSlot() - 1, Chance);
                            inv.setItem(e.getRawSlot(), Enable);
                        }
                        ItemStack cur = new ItemStack(cursor);
                        cur.setAmount(1);
                        int i = 1;
                        if (sec != null) {
                        Set<String> set = sec.getKeys(false);
                        i = set.size() + 1;
                        }
                        saveItemData(cur, f, 0, true, i);
                    }else {
                        e.setCancelled(true);
                    }
                }else if (target.getType() != Chance.getType() && target.getType() != Enable.getType() && e.getRawSlot() <= 53 && target.getType() != Disable.getType()){
                    e.setCancelled(true);
                    if (slot1.contains(e.getRawSlot())){
                        e.setCancelled(true);
                        assert cursor != null;
                        ItemStack cur = new ItemStack(cursor);
                        cur.setAmount(1);
                        inv.setItem(e.getRawSlot(), side2);
                        inv.setItem(e.getRawSlot() + 1, side2);
                        inv.setItem(e.getRawSlot() + 2, side2);
                    }else if (slot2.contains(e.getRawSlot())){
                        e.setCancelled(true);
                        assert cursor != null;
                        ItemStack cur = new ItemStack(cursor);
                        cur.setAmount(1);
                        inv.setItem(e.getRawSlot() - 1, side2);
                        inv.setItem(e.getRawSlot(), side2);
                        inv.setItem(e.getRawSlot() + 1, side2);
                    }else if (slot3.contains(e.getRawSlot())){
                        e.setCancelled(true);
                        assert cursor != null;
                        ItemStack cur = new ItemStack(cursor);
                        cur.setAmount(1);
                        inv.setItem(e.getRawSlot() - 2, side2);
                        inv.setItem(e.getRawSlot() - 1, side2);
                        inv.setItem(e.getRawSlot(), side2);
                    }
                    int is = 1;
                    if (sec != null) {
                        Set<String> set = sec.getKeys(false);
                        is = set.size();
                    }
                    int im = is + 1;
                    int ia = 0;
                    for (int i = 1; i <= im; i++){
                        if (itemdata.getItemStack("ItemHandlerList." + i + ".ItemData") != null) {
                            if (itemdata.getItemStack("ItemHandlerList." + i + ".ItemData").getType() == target.getType()){
                                ia = i;
                                break;
                            }
                        }
                    }
                    removeItemData(f, ia);
                }else if (target.getType() == Enable.getType()){
                    e.setCancelled(true);
                    HashMap<Integer, Integer> map = new HashMap<>();
                    map.put(3,1);
                    map.put(7,2);
                    map.put(12,3);
                    map.put(16,4);
                    map.put(21,5);
                    map.put(25,6);
                    map.put(30,7);
                    map.put(34,8);
                    map.put(39,9);
                    map.put(43,10);
                    map.put(48,11);
                    map.put(52,12);
                    int num = map.get(e.getRawSlot());
                    itemdata.set("ItemHandlerList." + num + ".Enable", false);
                    saveFile(f, itemdata);
                    inv.setItem(e.getSlot(), Disable);
                }else if (target.getType() == Disable.getType()){
                    e.setCancelled(true);
                    HashMap<Integer, Integer> map = new HashMap<>();
                    map.put(3,1);
                    map.put(7,2);
                    map.put(12,3);
                    map.put(16,4);
                    map.put(21,5);
                    map.put(25,6);
                    map.put(30,7);
                    map.put(34,8);
                    map.put(39,9);
                    map.put(43,10);
                    map.put(48,11);
                    map.put(52,12);
                    int num = map.get(e.getRawSlot());
                    itemdata.set("ItemHandlerList." + num + ".Enable", true);
                    saveFile(f, itemdata);
                    inv.setItem(e.getSlot(), Enable);
                }else if (target.getType() == Chance.getType()){
                    e.setCancelled(true);
                }
            }
        }
    }
    @EventHandler
    public void onClickReachableItemHandlerGUI(InventoryClickEvent e) {
        Player p = (Player)e.getWhoClicked();
        ItemStack target = e.getCurrentItem();
        List<Material> mats = getRegisteredBlocks();
        ItemStack side = new ItemStack(Objects.requireNonNull(Material.getMaterial((String) Objects.requireNonNull(cfg.get("ItemSetGui.Side.Id")))));
        ItemMeta sidem = side.getItemMeta();
        sidem.setDisplayName((String) cfg.get("ItemSetGui.Side.Display"));
        sidem.setLore((List<String>)cfg.get("ItemSetGui.Side.Lore"));
        side.setItemMeta(sidem);
        List<String> matlore = (List<String>)cfg.get("ItemSetGui.ItemLore");
        Inventory inv = e.getInventory();
        InventoryView invv = p.getOpenInventory();
        if (invv.getTitle() == cfg.get("ItemSetGui.Name")){
            int slot = 9;
            for (Material mat: mats) {
                ItemStack i = new ItemStack(mat);
                ItemMeta im = i.getItemMeta();
                im.setLore(matlore);
                i.setItemMeta(im);
                inv.setItem(slot, i);
                slot++;
            }
            e.setCancelled(true);
            if (target != null) {
                if (target.getType() != side.getType() && contain(target.getType(), mats)) {
                    DropItemHandlerGui(p, Objects.requireNonNull(e.getCurrentItem()).getType().toString());
                }
            }
        }
    }
    public int getAllWeight (String s, Player p) {
        File f = new File(getDataFolder() + "/DropItemSetData/" + s + ".yml");
        FileConfiguration config = YamlConfiguration.loadConfiguration(f);
        ConfigurationSection sec = config.getConfigurationSection("ItemHandlerList");
        int chance = 0;
        if (sec != null){
            Set<String> keys = sec.getKeys(false);
            for(int i = 1; i<= keys.size(); i++){
                //p.sendMessage("获取到第" + i + "个掉落物权重: " + config.getInt("ItemHandlerList." + i + ".Chance"));
                chance = chance + config.getInt("ItemHandlerList." + i + ".Chance");
                //p.sendMessage("总权重现在值: "+chance);
            }
            return chance;
        }else{
            return chance;
        }
    }
    public boolean ifDoubleSituate (double a, double x, double y){
        if (x <= y){
            return a >= x && a <= y;
        }else{
            return a >= y && a <= x;
        }
    }
    public boolean isBlockInArea(Location loc, String s, Player p){
        int line = 0;
        File f = new File(getDataFolder() + "/WorldData/" + s + ".yml");
        FileConfiguration cfg = YamlConfiguration.loadConfiguration(f);
        ConfigurationSection sec1 = cfg.getConfigurationSection(s);
        double x = loc.getX();
        double y = loc.getY();
        double z = loc.getZ();
        //p.sendMessage("被挖掘方块坐标: (" + x + ", " + y + ", " + z + ")");
        if (sec1 == null) {
            return false;
        }else {
            line = sec1.getKeys(false).size();
            ConfigurationSection sec2 = cfg.getConfigurationSection(s + "." + line);
            if (sec2.getKeys(false).size() == 1){
                line = line - 1;
                for (int i = 1; i <= line; i++){
                    double x1 = cfg.getDouble(s + "." + i + ".FirstLocation.x");
                    double x2 = cfg.getDouble(s + "." + i + ".SecondLocation.x");
                    double y1 = cfg.getDouble(s + "." + i + ".FirstLocation.y");
                    double y2 = cfg.getDouble(s + "." + i + ".SecondLocation.y");
                    double z1 = cfg.getDouble(s + "." + i + ".FirstLocation.z");
                    double z2 = cfg.getDouble(s + "." + i + ".SecondLocation.z");
                    //p.sendMessage("序列号为" + i + "的第一个坐标为: (" + x1 + ", " + y1 + ", " + z1 + ")");
                    //p.sendMessage("序列号为" + i + "的第二个坐标为: (" + x2 + ", " + y2 + ", " + z2 + ")");
                    if (ifDoubleSituate(x, x1, x2)){
                        //p.sendMessage("检测x坐标成功");
                        if (ifDoubleSituate(y, y1, y2)){
                            //p.sendMessage("检测y坐标成功");
                            //p.sendMessage("检测z坐标成功");
                            return ifDoubleSituate(z, z1, z2);
                        }
                    }
                }
            }else {
                for (int i = 1; i <= line; i++){
                    double x1 = cfg.getDouble(s + "." + i + ".FirstLocation.x");
                    double x2 = cfg.getDouble(s + "." + i + ".SecondLocation.x");
                    double y1 = cfg.getDouble(s + "." + i + ".FirstLocation.y");
                    double y2 = cfg.getDouble(s + "." + i + ".SecondLocation.y");
                    double z1 = cfg.getDouble(s + "." + i + ".FirstLocation.z");
                    double z2 = cfg.getDouble(s + "." + i + ".SecondLocation.z");
                   // p.sendMessage("序列号为" + i + "的第一个坐标为: (" + x1 + ", " + y1 + ", " + z1 + ")");
                    //p.sendMessage("序列号为" + i + "的第二个坐标为: (" + x2 + ", " + y2 + ", " + z2 + ")");
                    if (ifDoubleSituate(x, x1, x2)){
                        //p.sendMessage("检测x坐标成功");
                        if (ifDoubleSituate(y, y1, y2)){
                            //p.sendMessage("检测y坐标成功");
                            //p.sendMessage("检测z坐标成功");
                            return ifDoubleSituate(z, z1, z2);
                        }
                    }
                }
            }
        }
        return false;
    }
    public void createLocationFile(String s, Player p){
        File f = new File(getDataFolder()+ "/WorldData/" + s +  ".yml");
        if (!f.exists()) {
            if (!f.getParentFile().exists()){
                f.getParentFile().mkdirs();
            }
            try{
                if (p.hasPermission("magstarminearea.reload")){
                    p.sendMessage(prefix + "§a正在尝试世界" + s + "§a的矿洞数据时发现WorldData不存在");
                    p.sendMessage(prefix + "§a正在生成文件" + s + ".yml");
                }
                f.createNewFile();
            }catch (IOException e){
                p.sendMessage(prefix + "§c尝试生成数据文件失败！请联系管理员或服主！");
                getLogger().warning(prefix + "§c尝试生成数据文件夹失败！报错信息已生成↑↑↑");
                e.printStackTrace();
            }
        }
        FileConfiguration worlddata = YamlConfiguration.loadConfiguration(f);
        worlddata.set (s + ".1.FirstLocation.x", 0);
        worlddata.set (s + ".1.FirstLocation.y", 0);
        worlddata.set (s + ".1.FirstLocation.z", 0);
        worlddata.set (s + ".1.SecondLocation.x", 0);
        worlddata.set (s + ".1.SecondLocation.y", 0);
        worlddata.set (s + ".1.SecondLocation.z", 0);
        saveFile(f,worlddata);
        p.sendMessage(prefix + cfg.get("Messages.SaveWorldSuccess"));

    }
    @EventHandler
    public void onPlayerDug (BlockBreakEvent e){
        if (Objects.requireNonNull(cfg.getList("EffectiveWorld")).contains(e.getPlayer().getWorld().getName())){
            File f = new File(getDataFolder()+ "/PlayerData/playerdata.yml");
            FileConfiguration playerdata = YamlConfiguration.loadConfiguration(f);
            Player p = e.getPlayer();
            if ((p.getInventory().getItemInMainHand().getType() != Material.AIR) && p.getInventory().getItemInMainHand().hasItemMeta()){
                if (p.getInventory().getItemInMainHand().getItemMeta().hasLore()) {
                    ItemStack i = p.getInventory().getItemInMainHand();
                    List<String> lore = i.getLore();
                    String lvlExecute = (String) cfg.get("LevelExecutor");
                    String PicExecute = (String) cfg.get("PickaxeExecutor");
                    String split = (String) cfg.get("Split");
                    String al = null;
                    boolean b1 = false;
                    boolean b2 = false;
                    boolean b3 = false;
                    assert lore != null;
                    for (String a : lore) {
                        assert PicExecute != null;
                        if (a.contains(PicExecute)){
                            b1 = true;
                        }
                        assert lvlExecute != null;
                        if (a.contains(lvlExecute)){
                            al = a;
                            b2 = true;
                        }if (a.contains((String) Objects.requireNonNull(cfg.get("AdminExecutor")))){
                            b3 = true;
                        }
                    }
                    File bd = new File(getDataFolder() + "/DropItemSetData/" + e.getBlock().getType() + ".yml");
                    FileConfiguration bdc = YamlConfiguration.loadConfiguration(bd);
                    ConfigurationSection sec = bdc.getConfigurationSection("ItemHandlerList");
                    int keySize = 0;
                    if (sec != null){
                        Set<String> keys = sec.getKeys(false);
                        keySize = keys.size();
                    }
                    if (b1) {
                        if (isBlockInArea(e.getBlock().getLocation(), e.getPlayer().getWorld().getName(), e.getPlayer())){
                            if (getRegisteredBlocks().contains(e.getBlock().getType())){
                                    int cooldown = (int) cfg.get("RegisteredBlock." + e.getBlock().getType() + ".BreakCooldown");
                                if (b2){
                                    assert split != null;
                                    String[] lvlore = al.split(split);
                                    if ((int) playerdata.get("PlayerData." + p.getName() + ".Level") < Integer.parseInt(removeColor(lvlore[1]))){
                                        e.setCancelled(true);
                                        p.sendMessage(prefix + cfg.get("Messages.LvlNotEnough"));
                                    }else {
                                        if (getRegisteredBlocks().contains(e.getBlock().getType())){
                                            addExp(p, e.getBlock());
                                            e.setCancelled(true);
                                            if (sec != null){
                                                int allWeight = getAllWeight(e.getBlock().getType().toString(), p);
                                                //p.sendMessage("总权重: " + allWeight);
                                                double random = Math.random();
                                                //p.sendMessage("随机值: " + random);
                                                double initial = 0;
                                                for (int c = 1; c <= keySize; c++){
                                                    double initial1 = initial + bdc.getInt("ItemHandlerList." + c + ".Chance") / allWeight;
                                                    //p.sendMessage("遍历前: "+ initial);
                                                    //p.sendMessage("遍历后: "+ initial1);
                                                    if (random <= initial1 && random > initial){
                                                        dropItem(bdc.getItemStack("ItemHandlerList." + c + ".ItemData"), e.getBlock());
                                                        break;
                                                    }
                                                }
                                            }
                                            Material mat = e.getBlock().getType();
                                            e.getBlock().setType(Material.BEDROCK);
                                            Bukkit.getScheduler().runTaskLater(Objects.requireNonNull(Bukkit.getPluginManager().getPlugin("xyz/magstar/minearea")), new Runnable() {
                                                @Override
                                                public void run() {
                                                    e.getBlock().setType(mat);
                                                }
                                            }, cooldown * 20L);
                                            // Robot r = new Robot();
                                            // r.delay(cooldown * 1000);
                                        }
                                        else {
                                            e.setCancelled(true);
                                        }
                                    }
                                } else {
                                    if (getRegisteredBlocks().contains(e.getBlock().getType())){
                                        addExp(p, e.getBlock());
                                        e.setCancelled(true);
                                        if (sec != null){
                                            int allWeight = getAllWeight(e.getBlock().getType().toString(), p);
                                            //p.sendMessage("方块名: " + e.getBlock().getType().toString());
                                            //p.sendMessage("总权重: " + allWeight);
                                            double random = Math.random();
                                            //p.sendMessage("随机值: " + random);
                                            double initial = 0;
                                            for (int c = 1; c <= keySize; c++){
                                                double division = bdc.getDouble("ItemHandlerList." + c + ".Chance") / allWeight;
                                                //p.sendMessage("遍历前: "+ initial);
                                                double initial0 = initial;
                                                initial = initial + division;
                                                //p.sendMessage("遍历时: "+ division);
                                                //p.sendMessage("遍历后: "+ initial);
                                                if (random <= initial && random > initial0){
                                                    dropItem(bdc.getItemStack("ItemHandlerList." + c + ".ItemData"), e.getBlock());
                                                    break;
                                                }
                                            }
                                        }
                                        Material mat = e.getBlock().getType();
                                        e.getBlock().setType(Material.BEDROCK);
                                        Bukkit.getScheduler().runTaskLater(Objects.requireNonNull(Bukkit.getPluginManager().getPlugin("xyz/magstar/minearea")), new Runnable() {
                                            @Override
                                            public void run() {
                                                e.getBlock().setType(mat);
                                            }
                                        }, cooldown * 20L);
                                        //Robot r = new Robot();
                                        //r.delay(cooldown * 1000);
                                    }else {
                                        e.setCancelled(true);
                                    }
                                }
                            }else {
                                e.setCancelled(true);
                                p.sendMessage(prefix + cfg.get("Messages.IncorrectBlock"));
                            }
                        }else {
                            p.sendMessage(prefix + cfg.get("Messages.BlockNotInArea"));
                            e.setCancelled(true);
                        }
                    }else if (b3){
                        Block b = e.getBlock();
                        Location location;
                        String s = b.getWorld().getName();
                        if (Objects.requireNonNull(cfg.getList("EffectiveWorld")).contains(e.getPlayer().getWorld().getName())){
                            if (b.getType() != Material.AIR) {
                                location = b.getLocation();
                                File f1 = new File(getDataFolder() + "/WorldData/" + s + ".yml");
                                if (!f1.exists()){
                                    createLocationFile(s, e.getPlayer());
                                }
                                FileConfiguration worlddata = YamlConfiguration.loadConfiguration(f1);
                                double x = location.getX();
                                double y = location.getY();
                                double z = location.getZ();
                                int key0 = 0;
                                ConfigurationSection sec1 = worlddata.getConfigurationSection(s);
                                if (sec1 != null){
                                    key0 = sec1.getKeys(false).size();
                                }
                                if (key0 >= 1){
                                    ConfigurationSection sec2 = worlddata.getConfigurationSection(s + "." + key0);
                                    assert sec2 != null;
                                    int key1 = sec2.getKeys(false).size();
                                    if (key1 == 1){
                                        worlddata.set(s + "." + key0 + ".SecondLocation.x", x);
                                        worlddata.set(s + "." + key0 + ".SecondLocation.y", y);
                                        worlddata.set(s + "." + key0 + ".SecondLocation.z", z);
                                        p.sendMessage(prefix + cfg.get("Messages.SecondPosition") + "(" + x + ", " + y + ", " + z + ")");
                                        saveFile(f1, worlddata);
                                        e.setCancelled(true);
                                    }else if (key1 == 2){
                                        key0 = key0 + 1;
                                        worlddata.set(s + "." + key0 + ".FirstLocation.x", x);
                                        worlddata.set(s + "." + key0 + ".FirstLocation.y", y);
                                        worlddata.set(s + "." + key0 + ".FirstLocation.z", z);
                                        p.sendMessage(prefix + cfg.get("Messages.FirstPosition") + "(" + x + ", " + y + ", " + z + ")");
                                        saveFile(f1, worlddata);
                                        e.setCancelled(true);
                                    }
                                }else{
                                    key0 = key0 + 1;
                                    worlddata.set(s + "." + key0 + ".FirstLocation.x", x);
                                    worlddata.set(s + "." + key0 + ".FirstLocation.y", y);
                                    worlddata.set(s + "." + key0 + ".FirstLocation.z", z);
                                    p.sendMessage(prefix + cfg.get("Messages.FirstPosition") + "(" + x + ", " + y + ", " + z + ")");
                                    saveFile(f1, worlddata);
                                    e.setCancelled(true);
                                }
                            }
                        }
                    }else if (!b1 && !b3) {
                        e.setCancelled(true);
                        p.sendMessage(prefix + cfg.get("Messages.UsePickaxe"));
                    }
                }else{
                    e.setCancelled(true);
                    p.sendMessage(prefix + cfg.get("Messages.UsePickaxe"));
                }
            }else {
                e.setCancelled(true);
                p.sendMessage(prefix + cfg.get("Messages.UsePickaxe"));
            }
        }
    }
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, String[] args) {
        String onlyPlayer = cfg.getString("Messages.OnlyPlayer");
        if ((args.length == 0) || (args[0].equalsIgnoreCase("help"))){
            sender.sendMessage("§e========== §f[ §b§lMagStar§3§lMineArea §f] §e==========");
            sender.sendMessage("§b作者: §dBerry_so 浆果");
            sender.sendMessage(" ");
            sender.sendMessage("§7指令缩写: msgs;magstargem");
            sender.sendMessage(" ");
            sender.sendMessage("§e/MagStarMineArea help            §d| §b打开此帮助页面");
            sender.sendMessage("§e/MagStarMineArea reload          §d| §b重载配置文件");
            sender.sendMessage("§e/MagStarMineArea drop            §d| §b打开掉落物配置菜单");
            sender.sendMessage("§e/MagStarMineArea job             §d| §b选择职业/取消职业");
            sender.sendMessage(" ");
            sender.sendMessage("§e========== §f[ §b§lMagStar§3§lMineArea §f] §e==========");
        }
        else if (args[0].equalsIgnoreCase("reload")){
            if(sender.hasPermission("magstarminearea.reload")){
                try {
                    if (!getDataFolder().exists()){
                        getDataFolder().mkdir();
                    }
                        File f = new File (getDataFolder(), "config.yml");
                    if (!f.exists()){
                        saveDefaultConfig();
                    }
                    reloadConfig();
                    saveConfig();
                    cfg = YamlConfiguration.loadConfiguration(new File(getDataFolder() + "/config.yml"));
                    String reload = (String)cfg.get("Messages.Reload");
                    sender.sendMessage(prefix + reload);
                } catch (Exception e){
                    getLogger().info(prefix + "在重载插件的时候出现了预料之外的错误。");
                    getLogger().info(prefix + "§b========================= §c错误信息 ↓ ↓ ↓ §b=========================");
                    e.printStackTrace();
                    getLogger().info(prefix + "§b========================= §c错误信息 ↑ ↑ ↑ §b=========================");
                }
            } else{
                sender.sendMessage(prefix + noPerm);
            }
            return true;
        }
        else if (args[0].equalsIgnoreCase("drop")){
            if ((sender instanceof  Player)){
                if (sender.hasPermission("magstarminearea.drop")){
                    ReachableItemHandlerGUI((Player) sender);
                }else {
                    sender.sendMessage(prefix + noPerm);
                }
            }else {
                sender.sendMessage(prefix + onlyPlayer);
            }
        }
        else if (args[0].equalsIgnoreCase("job")){
            if ((sender instanceof Player)){
                if (sender.hasPermission("magstarminearea.job")){
                    File f = new File (getDataFolder()+ "/PlayerData/playerdata.yml");
                    if (!f.exists()){
                        createDataFile((Player)sender, (Player)sender);
                    }else{
                        if (ifPlayerInFile((Player) sender)){
                            fileAddPlayer((Player)sender);
                        }else{
                            fileRemovePlayer((Player)sender);
                        }
                    }
                }else{
                    sender.sendMessage(prefix + noPerm);
                }
            }else {
                sender.sendMessage(prefix + onlyPlayer);
            }
        }
        else if (args[0].equalsIgnoreCase("exp")){
            if ((sender instanceof Player)){
                if (sender.hasPermission("magstarminearea.exp")){
                    File f = new File (getDataFolder()+ "/PlayerData/playerdata.yml");
                    if (!f.exists()){
                        createDataFile((Player)sender, (Player)sender);
                    }else{
                        if (ifPlayerInFile((Player) sender)){
                            sender.sendMessage(prefix + cfg.getString("Messages.AlreadyNot"));
                        }else{
                            FileConfiguration fg = YamlConfiguration.loadConfiguration(f);
                            int lvl = fg.getInt("PlayerData." + sender.getName() + ".Level");
                            int exp = fg.getInt("PlayerData." + sender.getName() + ".Exp");
                            sender.sendMessage(prefix + "§a你现在的矿工等级为: " + lvl);
                            sender.sendMessage(prefix + "§a你现在的矿工经验为: " + exp);
                        }
                    }
                }
            }else {
                sender.sendMessage(prefix + onlyPlayer);
            }
        }
        return true;
    }
}