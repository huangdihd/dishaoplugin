package vip.dicloud;

import org.bukkit.*;
import vip.dicloud.Metrics;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.MapMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.map.*;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.CachedServerIcon;

import javax.annotation.ParametersAreNonnullByDefault;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.*;
import java.util.*;
import java.util.List;

import static org.bukkit.Bukkit.*;
import static vip.dicloud.dishao.*;

class Pinv{
    Boolean isonline;
    Player player;
    PlayerInventory playerInventory;
    Player opener;
    Inventory infoinv;
    ItemStack mainhand;
    public Pinv(Player player1,Player opener1){
        opener = opener1;
        player = player1;
        File PlayerData = new File(dishao.getPlugin(dishao.class).getDataFolder().getAbsolutePath() + File.separator + "PlayerData" + File.separator + player.getName() + ".yml");
        isonline = YamlConfiguration.loadConfiguration(PlayerData).getBoolean("isonline");
        playerInventory = player.getInventory();
        infoinv = createInventory(player, 6 * 9, "关于玩家" + player.getName() + "的详情信息");
        mainhand = playerInventory.getItemInMainHand();
        ItemStack name = new ItemStack(Material.PLAYER_HEAD);
        if(isonline) {
            SkullMeta meta = (SkullMeta) name.getItemMeta();
            meta.setOwner(player.getName());
            name.setItemMeta(meta);
        }
        ItemMeta mm = name.getItemMeta();
        if(!config.getBoolean("player-online-check",false)){
            mm.setLore(Arrays.asList(player.getName()));
        }else {
            if (isonline) {
                mm.setLore(Arrays.asList(player.getName(), ChatColor.GREEN + "正版验证"));
            } else {
                mm.setLore(Arrays.asList(player.getName(), ChatColor.DARK_BLUE + "离线验证"));
            }
        }
        mm.setDisplayName("玩家详情");
        name.setItemMeta(mm);
        infoinv.setItem(0,name);
        ItemStack gamemode = new ItemStack(Material.DIAMOND);
        if(player.getGameMode().equals(GameMode.CREATIVE)){
            gamemode.setType(Material.GRASS_BLOCK);
        } else if (player.getGameMode().equals(GameMode.SURVIVAL)) {
            gamemode.setType(Material.IRON_SWORD);
        }else if (player.getGameMode().equals(GameMode.ADVENTURE)) {
            gamemode.setType(Material.PAPER);
        }else{
            gamemode.setType(Material.ENDER_EYE);
        }
        mm = gamemode.getItemMeta();
        mm.setLore(Arrays.asList(player.getGameMode().toString()));
        mm.setDisplayName("游戏模式");
        gamemode.setItemMeta(mm);
        infoinv.setItem(1,gamemode);
        ItemStack world = new ItemStack(Material.STONE);
        mm = world.getItemMeta();
        mm.setLore(Arrays.asList(player.getWorld().getName()));
        mm.setDisplayName("所在世界");
        world.setItemMeta(mm);
        infoinv.setItem(2,world);
        for(int i = 9;i < 45;i++){
            infoinv.setItem(i,this.getItem(i - 9));
        }
        infoinv.setItem(45,this.getPlayerInventory().getItemInMainHand());
        infoinv.setItem(46,this.getPlayerInventory().getItemInOffHand());
        infoinv.setItem(47,this.getPlayerInventory().getHelmet());
        infoinv.setItem(48,this.getPlayerInventory().getChestplate());
        infoinv.setItem(49,this.getPlayerInventory().getLeggings());
        infoinv.setItem(50,this.getPlayerInventory().getBoots());
        ItemStack loc = new ItemStack(Material.STRUCTURE_VOID);
        mm = loc.getItemMeta();
        mm.setLore(Arrays.asList(player.getLocation().getBlockX() + "",player.getLocation().getBlockY() + "",player.getLocation().getBlockZ() + "","点击传送至"));
        mm.setDisplayName("位置");
        loc.setItemMeta(mm);
        infoinv.setItem(3,loc);
        for(int i = 9;i < 45;i++){
            infoinv.setItem(i,this.getItem(i - 9));
        }
        infoinv.setItem(45,this.getPlayerInventory().getItemInMainHand());
        infoinv.setItem(46,this.getPlayerInventory().getItemInOffHand());
        infoinv.setItem(47,this.getPlayerInventory().getHelmet());
        infoinv.setItem(48,this.getPlayerInventory().getChestplate());
        infoinv.setItem(49,this.getPlayerInventory().getLeggings());
        infoinv.setItem(50,this.getPlayerInventory().getBoots());
    }
    public Player getPlayer(){
        return player;
    }
    public PlayerInventory getPlayerInventory(){
        return playerInventory;
    }
    public ItemStack getItem(int index){
        return playerInventory.getItem(index);
    }
    public Player getOpener(){
        return opener;
    }
    public Inventory getInfoinv(){
        return infoinv;
    }
    public void setitem(int index,ItemStack i){
        playerInventory.setItem(index,i);
    }
    public void upinfoinv(){
        for(int i = 9;i < 45;i++){
            infoinv.setItem(i,this.getItem(i - 9));
        }
        infoinv.setItem(45,this.getPlayerInventory().getItemInMainHand());
        mainhand = infoinv.getItem(45);
        infoinv.setItem(46,this.getPlayerInventory().getItemInOffHand());
        infoinv.setItem(47,this.getPlayerInventory().getHelmet());
        infoinv.setItem(48,this.getPlayerInventory().getChestplate());
        infoinv.setItem(49,this.getPlayerInventory().getLeggings());
        infoinv.setItem(50,this.getPlayerInventory().getBoots());
        ItemStack gamemode = new ItemStack(Material.DIAMOND);
        if(player.getGameMode().equals(GameMode.CREATIVE)){
            gamemode.setType(Material.GRASS_BLOCK);
        } else if (player.getGameMode().equals(GameMode.SURVIVAL)) {
            gamemode.setType(Material.IRON_SWORD);
        }else if (player.getGameMode().equals(GameMode.ADVENTURE)) {
            gamemode.setType(Material.PAPER);
        }else{
            gamemode.setType(Material.ENDER_EYE);
        }
        ItemMeta mm = gamemode.getItemMeta();
        mm.setLore(Arrays.asList(player.getGameMode().toString()));
        mm.setDisplayName("游戏模式");
        gamemode.setItemMeta(mm);
        infoinv.setItem(1,gamemode);
        ItemStack world = new ItemStack(Material.STONE);
        mm = world.getItemMeta();
        mm.setLore(Arrays.asList(player.getWorld().getName()));
        mm.setDisplayName("所在世界");
        world.setItemMeta(mm);
        infoinv.setItem(2,world);
        ItemStack loc = new ItemStack(Material.STRUCTURE_VOID);
        mm = loc.getItemMeta();
        mm.setLore(Arrays.asList(player.getLocation().getBlockX() + "",player.getLocation().getBlockY() + "",player.getLocation().getBlockZ() + "","点击传送至"));
        mm.setDisplayName("位置");
        loc.setItemMeta(mm);
        infoinv.setItem(3,loc);
    }
    public void downinfoinv(){
        for(int i = 0;i < 36;i++){
            getPlayerInventory().setItem(i, getInfoinv().getItem(i + 9));
        }
        if(!Objects.equals(infoinv.getItem(playerInventory.getHeldItemSlot() + 9), mainhand)){
            infoinv.setItem(45,infoinv.getItem(playerInventory.getHeldItemSlot() + 9));
            mainhand = infoinv.getItem(playerInventory.getHeldItemSlot() + 9);
        } else if (!Objects.equals(infoinv.getItem(45), mainhand)){
            infoinv.setItem(playerInventory.getHeldItemSlot() + 9,infoinv.getItem(45));
            mainhand = infoinv.getItem(45);
        }

        player.getInventory().setItemInMainHand(mainhand);
        player.getInventory().setItemInOffHand(infoinv.getItem(46));
        player.getInventory().setHelmet(infoinv.getItem(47));
        player.getInventory().setChestplate(infoinv.getItem(48));
        player.getInventory().setLeggings(infoinv.getItem(49));
        player.getInventory().setBoots(infoinv.getItem(50));
    }
    public String getzhihzen(){return opener.getName() + "->" + player.getName();}
}
class Update{
    static String openFile(String filePath) {
        int HttpResult; // 服务器返回的状态
        String ee = new String();
        try
        {
            URL url =new URL(filePath); // 创建URL
            URLConnection urlconn = url.openConnection(); // 试图连接并取得返回状态码
            urlconn.connect();
            HttpURLConnection httpconn =(HttpURLConnection)urlconn;
            HttpResult = httpconn.getResponseCode();
            if(HttpResult != HttpURLConnection.HTTP_OK) {
                System.out.print("无法获取更新数据,请检查网络!");
            } else {
                InputStreamReader isReader = new InputStreamReader(urlconn.getInputStream(),"UTF-8");
                BufferedReader reader = new BufferedReader(isReader);
                StringBuffer buffer = new StringBuffer();
                String line; // 用来保存每行读取的内容
                line = reader.readLine(); // 读取第一行
                while (line != null) { // 如果 line 为空说明读完了
                    buffer.append(line);
                    line = reader.readLine();
                    if(line != null){
                        buffer.append("\n");
                    }
                }
                System.out.print(buffer.toString());
                ee = buffer.toString();
            }
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return  ee;
    }
}
class Bukkitio {
    // 定义一个用于向指定玩家或者控制台发送消息的方法
    public void commandsay(CommandSender commandsender, String n) {
        // 如果 commandsender 参数不是一个 Player 类型的对象
        if (!(commandsender instanceof Player)) {
            // 调用 JavaPlugin.getPlugin 函数来获取一个 dishao.class 类型的对象
            JavaPlugin plugin = JavaPlugin.getPlugin(dishao.class);
            // 使用该对象的 getLogger 方法来打印 n 参数
            plugin.getLogger().info(n);
        }
        // 否则，如果 commandsender 参数是一个 Player 类型的对象
        else {
            // 向该玩家发送一条消息，内容为 n 参数
            ((Player) commandsender).sendMessage(n);
        }
    }

    // 定义一个用于向服务器控制台打印消息的方法
    public void saytoserver(String n) {
        // 调用 println 方法来打印 n 参数
        System.out.println(n);
    }
}


class PPlayer {
    public boolean isplayer(String s){
        for(Player p1 : getOnlinePlayers()){
            if(p1.getName().equals(s)){
                return true;
            }
        }
        return false;
    }
    public boolean in(String s1,String s2){
        if(s2.length() > s1.length()){
            return false;
        }if(s2.length() == 0){
            return true;
        }
        for(int i = 0;i < s2.length();i++){
            if(s1.charAt(i) != s2.charAt(i)){
                return false;
            }
        }
        return true;
    }
}

public class dishao extends JavaPlugin {
    private boolean[] previousOpStatus;
    static String Motd;
    static FileConfiguration config;
    static ArrayList<String> motdl;
    static long motdt;
    static CachedServerIcon icon;
    static Plugin plugin;
    static ArrayList<Pinv> pinvlist = new ArrayList<>();
    static String OBC;
    static String NMS;
    public int min(int a,int b){
        if(a > b){
            return b;
        }
        if (b > a) {
            return a;
        }
        return b;
    }
    public void onEnable() {
        Metrics metrics = new Metrics(this, 17966);
        saveDefaultConfig();
        saveConfig();
        config = dishao.getPlugin(dishao.class).getConfig();
        String s = config.getString("say","在插件目录下的config.yml中的say:后面更改要输出的内容");
        getLogger().info(" __     __");
        getLogger().info("|  \\   /  \\");
        getLogger().info("|   |  \\__");
        getLogger().info("|   |     \\");
        getLogger().info("|__/   \\__/");
        getLogger().info("ver:" + dishao.getPlugin(dishao.class).getDescription().getVersion());
        getLogger().info("By DicloudStudio");
        getLogger().info("插件已加载");
        getLogger().info("配置文件情况:");
        boolean per = config.getBoolean("permissions",true);
        boolean update = config.getBoolean("detect-updates",true);
        plugin = getPlugin(dishao.class);
        if(per){
            getLogger().info("权限管理模块(permissions):开启");
            getLogger().info("非op玩家所拥有的权限(permissions-list):");
            List c = config.getList("permissions-list", null);
            String s1;
            for (int i = 0; i < c.size(); i++) {
                s1 = (String) c.get(i);
                getLogger().info(s1);
            }
            getLogger().info("非op玩家关闭的权限(off-permissions-list):");
            c = config.getList("off-permissions-list", null);
            for (int i = 0; i < c.size(); i++) {
                s1 = (String) c.get(i);
                getLogger().info(s1);
            }
        }else{
            getLogger().info("权限管理模块(permissions):关闭");
        }
        if(new File(getDataFolder().getAbsolutePath() + File.separator + config.getString("server-motd.motd-icon")).exists()){
            try {
                icon = getServer().loadServerIcon(new File(getDataFolder().getAbsolutePath() + File.separator + config.getString("server-motd.motd-icon")));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else{
            try {
                new File(getDataFolder().getAbsolutePath() + File.separator + config.getString("server-motd.motd-icon")).createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        if(!new File(getDataFolder().getAbsolutePath() + File.separator + "PlayerData").exists()) {
            new File(getDataFolder().getAbsolutePath() + File.separator + "PlayerData").mkdir();
        }
        if(!new File(getDataFolder().getAbsolutePath() + File.separator + "image").exists()) {
            new File(getDataFolder().getAbsolutePath() + File.separator + "image").mkdir();
        }
        if(!new File(getDataFolder().getAbsolutePath() + File.separator + "ImageData").exists()) {
            new File(getDataFolder().getAbsolutePath() + File.separator + "ImageData").mkdir();
        }
        motdt = config.getLong("server-motd.motd-time", 3000L);
        motdl = (ArrayList<String>) config.getList("server-motd.motd-list");
        new Thread(() -> {
            ArrayList<String> ml = motdl;
            int i = 0;
            while(true) {
                if(!motdl.equals(ml)){
                    i = 0;
                    ml = motdl;
                }
                try {
                    Thread.sleep(motdt);
                } catch (InterruptedException var3) {
                    var3.printStackTrace();
                }
                Motd = (String) Objects.requireNonNull(motdl).get(i);
                i++;
                if(i == motdl.size()){
                    i = 0;
                }
            }
        }).start();
        String[] versions = Bukkit.getVersion().split("\\.");
        String major = versions[0];
        String minor = versions[1];
        String revision = "-1";
        String NMSBaseHead = "net.minecraft.server.v" + major + "_" + minor + "_R";
        for (int i = 1; i <= 9; i++) {
            String versionTest = NMSBaseHead + i;
            try {
                Class.forName(versionTest + ".ItemStack");
                revision = i + "";
                break;
            } catch (ClassNotFoundException ignored) {
            }
        }
        if(Objects.requireNonNull(new File(plugin.getDataFolder().getAbsolutePath() + File.separator + "ImageData").listFiles()).length != 0){
            for(File i : new File(plugin.getDataFolder().getAbsolutePath() + File.separator + "ImageData").listFiles()){
                MapView map =  Bukkit.getMap(YamlConfiguration.loadConfiguration(i).getInt("id"));
                for(MapRenderer j : map.getRenderers()){
                    map.removeRenderer(j);
                }
                map.addRenderer(new MapRenderer() {
                    @Override
                    public void render(MapView mapView, MapCanvas mapCanvas, Player player) {
                        try {
                            String filename = YamlConfiguration.loadConfiguration(i).getString("image");
                            BufferedImage image =  ImageIO.read( new File(plugin.getDataFolder().getAbsolutePath() + File.separator + "image" + File.separator + filename));
                            mapCanvas.drawImage(0,0,MapPalette.resizeImage(image));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        }
        getLogger().info("成功获取" + Objects.requireNonNull(new File(plugin.getDataFolder().getAbsolutePath() + File.separator + "ImageData").listFiles()).length + "个地图的图片数据!");
        OBC = "org.bukkit.craftbukkit.v" + major + "_" + minor + "_R" + revision;
        NMS = "net.minecraft.server.v" + major + "_" + minor + "_R" + revision;
        if(!revision.equals("-1")){
            getLogger().info("经过反射,您的minecraft版本为" + NMSBaseHead + revision + "!");
        }else{
            getLogger().info("反射获取失败!");
        }
        getLogger().info(s);
        if (getPluginCommand("zisha") != null) {
            Objects.requireNonNull(getPluginCommand("zisha")).setExecutor(new Kill_Command());
        }
        if (getPluginCommand("help") != null) {
            Objects.requireNonNull(getPluginCommand("help")).setExecutor(new Help_Command());
        }
        if (getPluginCommand("tell") != null) {
            Objects.requireNonNull(getPluginCommand("tell")).setExecutor(new Tell_Command());
        }
        if (getPluginCommand("out") != null) {
            Objects.requireNonNull(getPluginCommand("out")).setExecutor(new Out_command());
        }
        if (getPluginCommand("tp") != null) {
            Objects.requireNonNull(getPluginCommand("tp")).setExecutor(new Tp_command());
        }
        if (getPluginCommand("kick") != null) {
            Objects.requireNonNull(getPluginCommand("kick")).setExecutor(new Kick_command());
        }
        if (getPluginCommand("dishao") != null) {
            Objects.requireNonNull(getPluginCommand("dishao")).setExecutor(new Info_Command());
        }
        if (getPluginCommand("playerinfo") != null) {
            Objects.requireNonNull(getPluginCommand("playerinfo")).setExecutor(new playerinfo_Command());
        }
        if (getPluginCommand("playerinfolist") != null) {
            Objects.requireNonNull(getPluginCommand("playerinfolist")).setExecutor(new playerinfolist_Command());
        }
        if (getPluginCommand("image") != null) {
            Objects.requireNonNull(getPluginCommand("image")).setExecutor(new image_Command());
        }
        this.getServer().getPluginManager().registerEvents(new Lisener(), this);
        if(update) {
            String lastversion = Update.openFile("http://plugin.dicloud.vip/dishao_version.txt");
            if (!lastversion.equals(dishao.getPlugin(dishao.class).getDescription().getVersion())) {
                getLogger().info("插件有新版本" + lastversion + "!");
                getLogger().info("下载地址:plugin.dicloud.vip/dishao.jar");
            }
        }if(getPlugin(dishao.class).getDescription().getVersion().getBytes()[getPlugin(dishao.class).getDescription().getVersion().length() - 1] == 'B'){
            getLogger().info("该版本为测试版本,不保证稳定!");
        }
        new BukkitRunnable() {
            @Override
            public void run() {
                Player[] onlinePlayers = Bukkit.getServer().getOnlinePlayers().toArray(new Player[0]);
                boolean[] currentOpStatus = new boolean[onlinePlayers.length];

                for (int i = 0; i < onlinePlayers.length; i++) {
                    currentOpStatus[i] = onlinePlayers[i].isOp();
                }

                if (previousOpStatus == null) {
                    previousOpStatus = currentOpStatus;
                } else {
                    for (int i = 0; i < min(currentOpStatus.length,previousOpStatus.length); i++) {
                        if (currentOpStatus[i] != previousOpStatus[i]) {
                            Player player = onlinePlayers[i];
                            if (per) {
                                List c = config.getList("permissions-list", null);
                                String s;
                                for (int j = 0; j < c.size(); j++) {
                                    s = (String) c.get(j);
                                    if(!player.isOp()) {
                                        player.addAttachment((Plugin) dishao.getPlugin(dishao.class), s, true);
                                    }
                                }
                                c = config.getList("off-permissions-list", null);
                                for (int j = 0; j < c.size(); j++) {
                                    s = (String) c.get(j);
                                    if (!player.isOp()) {
                                        player.addAttachment((Plugin) dishao.getPlugin(dishao.class), s, false);
                                    }
                                }
                            }
                        }
                    }

                    previousOpStatus = currentOpStatus;
                }
            }
        }.runTaskTimer(this, 0L, 2L);
    }
    public void onDisable() {
        getLogger().info("插件已卸载");
    }
}
class Kill_Command implements CommandExecutor {
    @Override
    @ParametersAreNonnullByDefault
    public boolean onCommand(CommandSender commandSender, Command command, String label, String[] args) {
        if(commandSender instanceof Player){
            if(!commandSender.hasPermission("dishao.zisha")){
                commandSender.sendMessage(ChatColor.DARK_RED + "你没有使用该指令的权限!");
                return true;
            }
        }
        if(args.length > 0) {
            if (args[0].equals("help")) {
                new Bukkitio().commandsay(commandSender,"用来杀死你");
                return true;
            }
        }
        if(commandSender instanceof Player) {
            Player player = (Player) commandSender;
            player.setHealth(0);
        }else{
            new Bukkitio().commandsay(commandSender,"错误:不能杀死控制台!");
        }
        return true;
    }
}
class Help_Command implements TabExecutor {
    @Override
    @ParametersAreNonnullByDefault
    public boolean onCommand(CommandSender commandSender, Command command, String label, String[] args) {
        if(commandSender instanceof Player){
            if(!commandSender.hasPermission("dishao.help")){
                commandSender.sendMessage(ChatColor.DARK_RED + "你没有使用该指令的权限!");
                return true;
            }
        }
        if(args.length > 0) {
            if (args[0].equals("514")) {
                new Bukkitio().commandsay(commandSender,ChatColor.BLUE + "/514:" + ChatColor.GREEN + "使用/514来自杀");
                return true;
            }
            if(args[0].equals("w")){
                new Bukkitio().commandsay(commandSender,ChatColor.BLUE + "/w:" + ChatColor.GREEN + "使用/w来进行私聊");
                return true;
            }if(args[0].equals("out")){
                new Bukkitio().commandsay(commandSender,ChatColor.BLUE + "/out:" + ChatColor.GREEN + "使用/out来退出服务器");
                return true;
            }
        }
        new Bukkitio().commandsay(commandSender,"dishao插件帮助列表:");
        new Bukkitio().commandsay(commandSender,ChatColor.BLUE + "/514:" + ChatColor.GREEN + "使用/514来自杀");
        new Bukkitio().commandsay(commandSender,ChatColor.BLUE + "/w:" + ChatColor.GREEN + "使用/w来进行私聊");
        new Bukkitio().commandsay(commandSender,ChatColor.BLUE + "/out:" + ChatColor.GREEN + "使用/out来退出服务器");
        return true;
    }
    public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
        if(!(sender instanceof Player)){
            return null;
        }
        if(args.length == 1){
            List n = new ArrayList<>();
            n.add("514");n.add("w");n.add("out");
            List s = new ArrayList<>();
            PPlayer pPlayer = new PPlayer();
            for(int i = 0;i < n.size();i++){
                String g = (String) n.get(i);
                if(pPlayer.in(g,args[0])){
                    s.add(g);
                }
            }
            return s;
        }
        List n = new ArrayList<>();
        return n;
    }
}
class Tell_Command implements TabExecutor {
    @Override
    @ParametersAreNonnullByDefault
    public boolean onCommand(CommandSender commandSender, Command command, String label, String[] args) {
        if (commandSender instanceof Player) {
            if (!commandSender.hasPermission("dishao.tell")) {
                commandSender.sendMessage(ChatColor.DARK_RED + "你没有使用该指令的权限!");
                return true;
            }
        }
            if (args.length != 2) {
                if (args.length == 1) {
                    new Bukkitio().commandsay(commandSender,"缺少参数:[内容]");
                    return true;
                }
                new Bukkitio().commandsay(commandSender,"使用/w [玩家id] [内容]或/tell [玩家id] [内容]来进行私聊");
                return true;
            }
            PPlayer pPlayer = new PPlayer();
            if (pPlayer.isplayer(args[0]) || args[0].toLowerCase().equals("server")) {
                Player player2 = (Player) getPlayer(args[0]);
                if ((commandSender instanceof Player) || (!args[0].toLowerCase().equals("server"))) {
                    if(commandSender instanceof Player){
                        if((Player)commandSender == getPlayer(args[0])){
                            new Bukkitio().commandsay(commandSender,"错误:不能给自己发私聊!");
                            return true;
                        }
                        new Bukkitio().commandsay(commandSender,ChatColor.LIGHT_PURPLE + "发到"  + args[0] + "的私聊:" + args[1]);
                        if(args[0].toLowerCase().equals("server")){
                            new Bukkitio().saytoserver(ChatColor.LIGHT_PURPLE + "来自" + commandSender.getName() + "的私聊:" + args[1]);
                        }else{
                            player2.sendMessage(ChatColor.LIGHT_PURPLE + "来自" + commandSender.getName() + "的私聊:" + args[1]);
                        }
                    }else{
                        if(args[0].toLowerCase().equals("server")){
                            new Bukkitio().commandsay(commandSender,"错误:不能给自己发私聊!");
                            return true;
                        }
                        new Bukkitio().commandsay(commandSender,ChatColor.LIGHT_PURPLE + "发到" + args[0] + "的私聊:" + args[1]);
                        player2.sendMessage(ChatColor.LIGHT_PURPLE + "来自服务器控制台的私聊:" + args[1]);
                    }

                } else {
                    new Bukkitio().commandsay(commandSender,"错误:不能给自己发私聊!");
                }
                return true;
            }else{
                new Bukkitio().commandsay(commandSender,"错误:玩家不存在或离线!");
            }
            return true;
    }
    public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
        if(!(sender instanceof Player)){
            List n = new ArrayList<>();
            return n;
        }
        if(args.length > 2){
            List n = new ArrayList<>();
            return n;
        }
        if(args.length == 2){
            if(args[1].length() == 0) {
                return Collections.singletonList("[内容]");
            }else{
                List n = new ArrayList<>();
                return n;
            }
        }else if(args.length == 1){
            List<String> l = new ArrayList();
            for(Player i : getOnlinePlayers()){
                if(i != (Player)sender) {
                    l.add(i.getName());
                }
                l.add("Server");
            }
            return l;
        }
        List n = new ArrayList<>();
        return n;
    }
}
class Out_command implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String label, String[] args) {
        if (commandSender instanceof Player) {
            if (!commandSender.hasPermission("dishao.out")) {
                commandSender.sendMessage(ChatColor.DARK_RED + "你没有使用该指令的权限!");
                return true;
            }
        }else{
            new Bukkitio().saytoserver("错误:不能让服务器退出服务器!");
            return true;
        }
        Player player = (Player) commandSender;
        player.kickPlayer("您已退出服务器");
        return true;
    }
}
class Tp_command implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String label, String[] args) {
        if(!(commandSender instanceof Player)){
            new Bukkitio().commandsay(commandSender,"不能让服务器tp");
            return true;
        }
        Player player = (Player) commandSender;
        if(!player.hasPermission("dishao.tp")){
            player.sendMessage(ChatColor.DARK_RED + "你没有使用该指令的权限!");
            return true;
        }
        if(args.length == 1 || args.length == 3){
            if(args.length == 1){
                Player p = getPlayer(args[0]);
                PPlayer pPlayer = new PPlayer();
                if(pPlayer.isplayer(args[0])){
                    player.teleport(new Location(getPlayer(args[0]).getWorld(), getPlayer(args[0]).getLocation().getBlockX(), getPlayer(args[0]).getLocation().getBlockY(), getPlayer(args[0]).getLocation().getBlockZ()));
                    return true;
                }
                boolean l = true;
                String commands = "";
                if(args[0].charAt(0) == '-' && args[0].length() != 1){
                    commands = args[0].substring(1);
                }
                for(int i = 0;i < args[0].length();i++){
                    l = Character.isDigit(commands.charAt(i));
                    if(!l){break;}
                }
                if(l){
                    player.sendMessage("缺少参数:[y],[z]");
                    return true;
                }else{
                    player.sendMessage("错误:玩家不存在或离线!");
                    return true;
                }
            }else{
                String commands1 = "";
                if(args[0].charAt(0) == '-' && args[0].length() != 1){
                    commands1 = args[0].substring(1);
                }
                String commands2 = "";
                if(args[1].charAt(0) == '-' && args[1].length() != 1){
                    commands2 = args[1].substring(1);
                }
                String commands3 = "";
                if(args[2].charAt(0) == '-' && args[1].length() != 1){
                    commands3 = args[2].substring(1);
                }
                boolean l = true;
                for(int i = 0;i < commands1.length();i++){
                    l = Character.isDigit(commands1.charAt(i));
                    if(!l){break;}
                }
                boolean y = true;
                for(int i = 0;i < commands2.length();i++){
                    y = Character.isDigit(commands2.charAt(i));
                    if(!y){break;}
                }
                boolean s = true;
                for(int i = 0;i < commands3.length();i++){
                    s = Character.isDigit(commands3.charAt(i));
                    if(!s){break;}
                }
                if(l && y && s){
                    player.teleport(new Location(player.getWorld(), Integer.parseInt(args[0]), Integer.parseInt(args[1]), Integer.parseInt(args[2])));
                    return true;
                }
            }
        }else if(args.length == 2){
            String commands1 = "";
            if(args[0].charAt(0) == '-' && args[0].length() != 1){
                commands1 = args[0].substring(1);
            }
            String commands2 = "";
            if(args[1].charAt(0) == '-' && args[1].length() != 1){
                commands2 = args[1].substring(1);
            }
            boolean l = true;
            for(int i = 0;i < commands1.length();i++){
                l = Character.isDigit(commands1.charAt(i));
                if(!l){break;}
            }
            boolean y = true;
            for(int i = 0;i < commands2.length();i++){
                y = Character.isDigit(commands2.charAt(i));
                if(!y){break;}
            }
            if(l && y){
                player.sendMessage("缺少参数:[z]");
                return true;
            }
        }
        player.sendMessage("/tp用法:/tp [x] [y] [z]或者/tp [玩家名称]");
        return true;
    }
}
class Kick_command implements TabExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String label, String[] args) {
        if (commandSender instanceof Player) {
            if (!commandSender.hasPermission("dishao.kick")) {
                commandSender.sendMessage(ChatColor.DARK_RED + "你没有使用该指令的权限!");
                return true;
            }
        }
        Player player = (Player) commandSender;
        if(args.length == 2){
            Player p = getPlayer(args[0]);
            PPlayer pPlayer = new PPlayer();
            if(pPlayer.isplayer(args[0])){
                new ti(p,args[1]);
                new Bukkitio().commandsay(commandSender,"您已成功踢出玩家" + args[0] + "原因:" + args[1]);
                return true;
            }else{
                new Bukkitio().commandsay(commandSender,ChatColor.DARK_RED + "错误:玩家不存在或离线!");
                return true;
            }
        }
        if(args.length == 1){
            new Bukkitio().commandsay(commandSender,ChatColor.DARK_RED + "缺少参数:[踢出原因]");
            return true;
        }
        new Bukkitio().commandsay(commandSender,ChatColor.DARK_RED + "/kick使用方法:/kick [玩家名称] [踢出原因]");
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
        if(!(sender instanceof Player)){
            List n = new ArrayList<>();
            return n;
        }
        if(args.length > 2){
            List n = new ArrayList<>();
            return n;
        }
        if(args.length == 2){
            if(args[1].length() == 0) {
                return Collections.singletonList("[踢出原因]");
            }else{
                List n = new ArrayList<>();
                return n;
            }
        }else if(args.length == 1){
            List<String> l = new ArrayList();
            for(Player i : getOnlinePlayers()){
                l.add(i.getName());
            }
            return l;
        }
        List n = new ArrayList<>();
        return n;
    }
}
class playerinfo_Command implements TabExecutor{
    @Override
    @ParametersAreNonnullByDefault
    public boolean onCommand(CommandSender commandSender,Command command, String label,String[] args){
        if(commandSender instanceof Player){
            Player player = (Player) commandSender;
            if(player.hasPermission("dishao.playerinfo")){
                if(args.length == 0){
                    player.sendMessage("缺少参数:[玩家名称]!");
                }else if(args.length > 1){
                    player.sendMessage("错误:过多的参数!");
                }else if(!new PPlayer().isplayer(args[0])){
                    player.sendMessage(ChatColor.DARK_RED + "错误:玩家不存在或离线!");
                }else{
                    Pinv pinv = new Pinv(getPlayer(args[0]),player);
                    if(!pinvlist.contains(pinv)){
                        pinvlist.add(pinv);
                    }else{
                        pinv = pinvlist.get(pinvlist.indexOf(pinv));
                        pinvlist.add(pinv);
                    }
                    player.openInventory(pinv.getInfoinv());
                }
            }else{
                player.sendMessage(ChatColor.DARK_RED + "你没有使用该指令的权限!");
            }
        }else{
            if(args.length == 0){
                new Bukkitio().saytoserver("缺少参数:[玩家名称]!");
            }else if(args.length > 1){
                new Bukkitio().saytoserver("错误:过多的参数!");
            }else if(!new PPlayer().isplayer(args[0])){
                new Bukkitio().saytoserver(ChatColor.DARK_RED + "错误:玩家不存在或离线!");
            }else{
                new Bukkitio().saytoserver("玩家名称:" + args[0]);
                new Bukkitio().saytoserver("游戏模式:" + getPlayer(args[0]).getGameMode().toString());
                new Bukkitio().saytoserver("所在世界:" + getPlayer(args[0]).getWorld().getName());
                new Bukkitio().saytoserver("位置:" + getPlayer(args[0]).getLocation().getBlockX() + "," + getPlayer(args[0]).getLocation().getBlockY() + "," + getPlayer(args[0]).getLocation().getBlockZ());
                new Bukkitio().saytoserver("正版验证:" + YamlConfiguration.loadConfiguration(new File(dishao.getPlugin(dishao.class).getDataFolder().getAbsolutePath() + File.separator + "PlayerData" + File.separator + args[0] + ".yml")).getBoolean("isonline"));
            }
        }
        return true;
    }
    public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
        ArrayList<String> n = new ArrayList<>();
        if(args.length == 1){
            for(Player i : getOnlinePlayers()){
                n.add(i.getName());
            }
        }return n;
    }
}
class Info_Command implements TabExecutor {
    @Override
    @ParametersAreNonnullByDefault
    public boolean onCommand(CommandSender commandSender, Command command, String label, String[] args) {
        if(args.length > 0) {
            if (args[0].equals("info")) {
                if(commandSender instanceof Player) {
                    Player player = (Player) commandSender;
                    if ((!player.hasPermission("dishao.main_command.info")) && (!player.hasPermission("dishao.main_command"))) {
                        player.sendMessage(ChatColor.DARK_RED + "你没有使用该指令的权限!");
                        return true;
                    }
                }
                new Bukkitio().commandsay(commandSender,"插件版本:" + dishao.getPlugin(dishao.class).getDescription().getVersion());
                new Bukkitio().commandsay(commandSender,"配置文件情况:");
                boolean per = config.getBoolean("permissions",true);
                if(per){
                    new Bukkitio().commandsay(commandSender,"权限管理模块(permissions):开启");
                    new Bukkitio().commandsay(commandSender,"非op玩家所拥有的权限(permissions-list):");
                    List<?> c = config.getList("permissions-list", null);
                    String s1;
                    for (Object o : c) {
                        s1 = (String) o;
                        new Bukkitio().commandsay(commandSender, s1);
                    }
                    new Bukkitio().commandsay(commandSender,"非op玩家关闭的权限(off-permissions-list):");
                    c = config.getList("off-permissions-list", null);
                    for (Object o : c) {
                        s1 = (String) o;
                        new Bukkitio().commandsay(commandSender, s1);
                    }
                }else{
                    new Bukkitio().commandsay(commandSender,"权限管理模块(permissions):关闭");
                }
                return true;
            }
            if(args[0].equals("config")){
                if(commandSender instanceof Player) {
                    Player player = (Player) commandSender;
                    if ((!player.hasPermission("dishao.main_command.config")) && (!player.hasPermission("dishao.main_command"))) {
                        player.sendMessage(ChatColor.DARK_RED + "你没有使用该指令的权限!");
                        return true;
                    }
                }
                if(args.length >= 3){
                    if(args[1].equals("permissions")){
                        if(args.length == 3){
                            if(args[2].equals("true") || args[2].equals("false")){
                                if(args[2].equals("true")){
                                    dishao.getPlugin(dishao.class).getConfig().set("permissions",true);
                                    dishao.getPlugin(dishao.class).saveConfig();
                                    new Bukkitio().commandsay(commandSender,"成功将permissions改为true!");
                                    return true;
                                }else{
                                    dishao.getPlugin(dishao.class).getConfig().set("permissions",false);
                                    dishao.getPlugin(dishao.class).saveConfig();
                                    new Bukkitio().commandsay(commandSender,"成功将permissions改为false!");
                                    return true;
                                }
                            }else{
                                new Bukkitio().commandsay(commandSender,"错误的参数:[值]");
                                return true;
                            }
                        }else{
                            new Bukkitio().commandsay(commandSender,"错误:出现了更多的参数");
                            return true;
                        }
                    }else if(args[1].equals("permissions-list")){
                        if(args.length == 4){
                            if(args[2].equals("add")){
                                
                                List c = config.getList("permissions-list", null);
                                c.add(args[3]);
                                config.set("permissions-list",c);
                                dishao.getPlugin(dishao.class).saveConfig();
                                new Bukkitio().commandsay(commandSender,"成功在permission-list中添加" + args[3] + "!");
                                return true;
                            }else if(args[2].equals("remove")){
                                
                                List c = config.getList("permissions-list", null);
                                if(!c.contains(args[3])){
                                    new Bukkitio().commandsay(commandSender,"错误的参数:[值]");
                                    return true;
                                }
                                c.remove(args[3]);
                                dishao.getPlugin(dishao.class).getConfig().set("permissions-list",c);
                                dishao.getPlugin(dishao.class).saveConfig();
                                new Bukkitio().commandsay(commandSender,"成功在permissions-list中删除" + args[3] + "!");
                                return true;
                            }else{
                                new Bukkitio().commandsay(commandSender,"错误的参数:[动作]");
                                return true;
                            }
                        }else if(args.length == 3) {
                            new Bukkitio().commandsay(commandSender,"缺少参数:[值]");
                            return true;
                        }else{
                            new Bukkitio().commandsay(commandSender,"错误:出现了更多的参数");
                            return true;
                        }
                    }
                    else if(args[1].equals("off-permissions-list")){
                        if(args.length == 4){
                            if(args[2].equals("add")){
                                List c = config.getList("off-permissions-list", null);
                                c.add(args[3]);
                                dishao.getPlugin(dishao.class).getConfig().set("off-permissions-list",c);
                                new Bukkitio().commandsay(commandSender,"成功在off-permissions-list中添加" + args[3] + "!");
                                dishao.getPlugin(dishao.class).saveConfig();
                                return true;
                            }else if(args[2].equals("remove")){
                                
                                List c = config.getList("off-permissions-list", null);
                                if(!c.contains(args[3])){
                                    new Bukkitio().commandsay(commandSender,"错误的参数:[值]");
                                    return true;
                                }
                                c.remove(args[3]);
                                dishao.getPlugin(dishao.class).getConfig().set("off-permissions-list",c);
                                dishao.getPlugin(dishao.class).saveConfig();
                                new Bukkitio().commandsay(commandSender,"成功在off-permissions-list中删除" + args[3] + "!");
                                return true;
                            }else{
                                new Bukkitio().commandsay(commandSender,"错误的参数:[动作]");
                                return true;
                            }
                        }else if(args.length == 3) {
                            new Bukkitio().commandsay(commandSender,"缺少参数:[值]");
                            return true;
                        }else{
                            new Bukkitio().commandsay(commandSender,"错误:出现了更多的参数");
                            return true;
                        }
                    }
                    else if (args[1].equals("detect-updates")) {
                        if(args.length == 3){
                            if(args[2].equals("true")){
                                dishao.getPlugin(dishao.class).getConfig().set("detect-updates",true);
                                new Bukkitio().commandsay(commandSender,"成功将detect-updates修改为true");
                            } else if (args[2].equals("false")){
                                dishao.getPlugin(dishao.class).getConfig().set("detect-updates",false);
                                new Bukkitio().commandsay(commandSender,"成功将detect-updates修改为false");
                            }else{
                                new Bukkitio().commandsay(commandSender,"错误的参数:[值]");
                            }
                        }else{
                            new Bukkitio().commandsay(commandSender,"缺少参数:[值]");
                        }
                    } else{
                        new Bukkitio().commandsay(commandSender,"缺少参数:[键],[值],或[键],[动作],[值]");
                        return true;
                    }
                }
            }
            if(args[0].equals("reload")){
                if(commandSender instanceof Player) {
                    Player player = (Player) commandSender;
                    if ((!player.hasPermission("dishao.main_command.reload")) && (!player.hasPermission("dishao.main_command"))) {
                        player.sendMessage(ChatColor.DARK_RED + "你没有使用该指令的权限!");
                        return true;
                    }
                }
                boolean per = config.getBoolean("permissions",true);
                if (per) {
                    for(Player p : getOnlinePlayers()) {
                        p.recalculatePermissions();
                    }
                }
                dishao.getPlugin(dishao.class).reloadConfig();
                config = dishao.getPlugin(dishao.class).getConfig();
                if(new File(plugin.getDataFolder().getAbsolutePath() + File.separator + config.getString("server-motd.motd-icon")).exists()){
                    try {
                        icon = plugin.getServer().loadServerIcon(new File(plugin.getDataFolder().getAbsolutePath() + File.separator + config.getString("server-motd.motd-icon")));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }else{
                    try {
                        new File(plugin.getDataFolder().getAbsolutePath() + File.separator + config.getString("server-motd.motd-icon")).createNewFile();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
                motdl = (ArrayList<String>) dishao.getPlugin(dishao.class).getConfig().getList("server-motd.motd-list");
                motdt = config.getLong("server-motd.motd-time",3000L);
                per = config.getBoolean("permissions",true);
                for(Player p : getOnlinePlayers()) {
                    File PlayerData = new File(dishao.getPlugin(dishao.class).getDataFolder().getAbsolutePath() + File.separator + "PlayerData" + File.separator + p.getName() + ".yml");
                    boolean isonline = YamlConfiguration.loadConfiguration(PlayerData).getBoolean("isonline");
                    if(isonline){
                        String newname = config.getString("online-player-prefix","§a[正版玩家]§r") + p.getName();
                        p.setDisplayName(newname);
                        p.setPlayerListName(newname);
                    }
                    if (per) {
                        List c = config.getList("permissions-list", null);
                        String s;
                        for (int i = 0; i < c.size(); i++) {
                            s = (String) c.get(i);
                            if(!p.isOp()) {
                                p.addAttachment((Plugin) dishao.getPlugin(dishao.class), s, true);
                            }
                        }
                        c = config.getList("off-permissions-list", null);
                        for (int i = 0; i < c.size(); i++) {
                            s = (String) c.get(i);
                            if (!p.isOp()) {
                                p.addAttachment((Plugin) dishao.getPlugin(dishao.class), s, false);
                            }
                        }
                    }
                }
                if(Objects.requireNonNull(new File(plugin.getDataFolder().getAbsolutePath() + File.separator + "ImageData").listFiles()).length != 0){
                    for(File i : new File(plugin.getDataFolder().getAbsolutePath() + File.separator + "ImageData").listFiles()){
                        MapView map =  Bukkit.getMap(YamlConfiguration.loadConfiguration(i).getInt("id"));
                        for(MapRenderer j : map.getRenderers()){
                            map.removeRenderer(j);
                        }
                        map.addRenderer(new MapRenderer() {
                            @Override
                            public void render(MapView mapView, MapCanvas mapCanvas, Player player) {
                                try {
                                    String filename = YamlConfiguration.loadConfiguration(i).getString("image");
                                    BufferedImage image =  ImageIO.read( new File(plugin.getDataFolder().getAbsolutePath() + File.separator + "image" + File.separator + filename));
                                    mapCanvas.drawImage(0,0,MapPalette.resizeImage(image));
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                    }
                }
                new Bukkitio().commandsay(commandSender,"插件已重载");
            }
        }
        new Bukkitio().commandsay(commandSender,"插件版本:" + dishao.getPlugin(dishao.class).getDescription().getVersion());
        new Bukkitio().commandsay(commandSender,"配置文件情况:");
        
        boolean per = config.getBoolean("permissions",true);
        if(per){
            new Bukkitio().commandsay(commandSender,"权限管理模块(permissions):开启");
            new Bukkitio().commandsay(commandSender,"非op玩家所拥有的权限(permissions-list):");
            List c = config.getList("permissions-list", null);
            String s1;
            for (int i = 0; i < c.size(); i++) {
                s1 = (String) c.get(i);
                new Bukkitio().commandsay(commandSender,s1);
            }
            new Bukkitio().commandsay(commandSender,"非op玩家关闭的权限(off-permissions-list):");
            c = config.getList("off-permissions-list", null);
            for (int i = 0; i < c.size(); i++) {
                s1 = (String) c.get(i);
                new Bukkitio().commandsay(commandSender,s1);
            }
        }else{
            new Bukkitio().commandsay(commandSender,"权限管理模块(permissions):关闭");
        }
        return true;
    }
    public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
        if(args.length == 1){
            List n = new ArrayList<>();
            n.add("info");n.add("config");n.add("reload");
            List s = new ArrayList<>();
            PPlayer pPlayer = new PPlayer();
            for(int i = 0;i < n.size();i++){
                String g = (String) n.get(i);
                s.add(g);
            }
            return s;
        }
        if(args.length == 2 && args[0].equals("config")){
            List n = new ArrayList<>();
            n.add("permissions");n.add("permissions-list");n.add("off-permissions-list");n.add("detect-updates");
            List s = new ArrayList<>();
            PPlayer pPlayer = new PPlayer();
            for(int i = 0;i < n.size();i++){
                String g = (String) n.get(i);
                s.add(g);
            }
            return s;
        }
        if((args.length == 3 && args[0].equals("config")) && (args[1].equals("permissions") || args[1].equals("detect-updates"))){
            List n = new ArrayList<>();
            n.add("true");n.add("false");
            List s = new ArrayList<>();
            PPlayer pPlayer = new PPlayer();
            for(int i = 0;i < n.size();i++){
                String g = (String) n.get(i);
                s.add(g);
            }
            return s;
        }
        if((args.length == 3 && args[0].equals("config")) && (args[1].equals("permissions-list") || args[1].equals("off-permissions-list"))){
            List n = new ArrayList<>();
            n.add("add");n.add("remove");
            List s = new ArrayList<>();
            PPlayer pPlayer = new PPlayer();
            for(int i = 0;i < n.size();i++){
                String g = (String) n.get(i);
                s.add(g);
            }
            return s;
        }
        if(((args.length == 4 && args[2].equals("remove")) && args[0].equals("config")) && (args[1].equals("permissions-list") || args[1].equals("off-permission-list"))) {
            List n = new ArrayList<>();

            List s;
            PPlayer pPlayer = new PPlayer();
            if (args[1].equals("permissions-list")) {
                s = config.getList("permissions-list", null);
                for (int i = 0; i < n.size(); i++) {
                    String g = (String) n.get(i);
                    s.add(g);
                }
            }else{
                s = config.getList("off-permissions-list", null);
                ;
                for (int i = 0; i < n.size(); i++) {
                    String g = (String) n.get(i);
                    s.add(g);
                }
            }
            return s;
        }
        List n = new ArrayList<>();
        return n;
    }
}
class playerinfolist_Command implements TabExecutor{
    @Override
    @ParametersAreNonnullByDefault
    public boolean onCommand(CommandSender commandSender, Command command, String label, String[] args) {
        if(commandSender instanceof Player){
            if(!commandSender.getName().equals(config.getString("superuser"))){
                commandSender.sendMessage(ChatColor.DARK_RED + "你没有使用该指令的权限!");
                return true;
            }
        }
        if(args.length == 1){
            for(Pinv i : pinvlist){
                if(i.getzhihzen().equals(args[0])){
                    i.getOpener().closeInventory();
                    i.getOpener().sendMessage("你对" + i.getPlayer().getName() + "的查看被超级用户强制关闭!");
                    pinvlist.remove(i);
                    commandSender.sendMessage(ChatColor.DARK_GREEN + "成功关闭" + args[0] + "的详情链接");
                    return true;
                }
            }
            commandSender.sendMessage(ChatColor.DARK_RED + "错误:未找到相应的详情指针!");
        }else if(args.length == 0){
            commandSender.sendMessage(ChatColor.DARK_RED + "缺少参数:[详情指针]!");
        }else{
            commandSender.sendMessage(ChatColor.DARK_RED + "错误:过多的参数!");
        }
        return true;
    }
    public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
        ArrayList<String> n = new ArrayList<>();
        if(args.length == 1) {
            if (sender instanceof Player) {
                Player player = (Player)sender;
                if(!player.getName().equals(config.getString("superuser"))){
                    n.add("非超级用户无法访问详情指针列表");
                    return n;
                }
            }
            if (pinvlist.size() >= 1) {
                for (Pinv i : pinvlist) {
                    n.add(i.getOpener().getName() + "->" + i.getPlayer().getName());
                }
            }
        }
        return n;
    }
}
class image_Command implements TabExecutor{
    @Override
    @ParametersAreNonnullByDefault
    public boolean onCommand(CommandSender commandSender, Command command, String label, String[] args) {
        if(commandSender instanceof Player) {
            if (args.length == 1) {
                List imagel = new ArrayList<String>();
                if(Objects.requireNonNull(new File(plugin.getDataFolder().getAbsolutePath() + File.separator + "image").listFiles()).length == 0){
                    commandSender.sendMessage("错误:无效的图片名!");
                    return true;
                }
                for(File i : new File(plugin.getDataFolder().getAbsolutePath() + File.separator + "image").listFiles()){
                    imagel.add(i.getName());
                }
                if(!imagel.contains(args[0])){
                    commandSender.sendMessage("错误:无效的图片名!");
                    return true;
                }
                Player player = (Player) commandSender;
                MapView map = Bukkit.createMap(player.getWorld());
                BufferedImage image;
                try {
                    image = ImageIO.read(new File(plugin.getDataFolder().getAbsolutePath() + File.separator + "image" + File.separator + args[0]));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                for (MapRenderer renderer : map.getRenderers()) {
                    map.removeRenderer(renderer);
                }
                map.addRenderer(new MapRenderer() {
                    @Override
                    public void render(MapView mapView, MapCanvas mapCanvas, Player player) {
                        mapCanvas.drawImage(0,0,MapPalette.resizeImage(image));
                    }
                });
                ItemStack item = new ItemStack(Material.FILLED_MAP);
                MapMeta meta = (MapMeta)item.getItemMeta();
                File mcf = new File(plugin.getDataFolder().getAbsolutePath() + File.separator + "ImageData" + File.separator + map.getId() + ".yml");
                try {
                    mcf.createNewFile();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                FileConfiguration mc = YamlConfiguration.loadConfiguration(mcf);
                mc.set("id",map.getId());
                mc.set("image",args[0]);
                try {
                    mc.save(mcf);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                assert meta != null;
                meta.setMapView(map);
                item.setItemMeta(meta);
                player.getInventory().addItem(item);
            } else if (args.length == 0) {
                commandSender.sendMessage(ChatColor.DARK_RED + "缺少参数:[图片名]!");
            } else {
                commandSender.sendMessage(ChatColor.DARK_RED + "错误:过多的参数!");
            }
        }else {
            commandSender.sendMessage("非玩家不能使用该命令!");
        }
        return true;

    }
    public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
        ArrayList<String> n = new ArrayList<>();
        if(Objects.requireNonNull(new File(plugin.getDataFolder().getAbsolutePath() + File.separator + "image").listFiles()).length == 0){
            return n;
        }
        for(File i : new File(plugin.getDataFolder().getAbsolutePath() + File.separator + "image").listFiles()){
            n.add(i.getName());
        }
        return n;
    }
}