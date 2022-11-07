package vip.dicloud;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import javax.annotation.ParametersAreNonnullByDefault;
import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import static vip.dicloud.dishao.config;
import static vip.dicloud.dishao.motdl;
import static vip.dicloud.dishao.motdt;

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
class Bukkitio{
    public void commandsay(CommandSender commandsender,String n){
        if(!(commandsender instanceof Player)){
            JavaPlugin.getPlugin(dishao.class).getLogger().info(n);
        }else{
            Player player = (Player)commandsender;
            player.sendMessage(n);
        }
    }
    public void saytoserver(String n){
        System.out.println(n);
    }
}
class PPlayer {
    public boolean isplayer(String s){
        for(Player p1 : Bukkit.getOnlinePlayers()){
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
    static String Motd;
    static FileConfiguration config;
    static ArrayList<String> motdl;
    static long motdt;
    public void onEnable() {
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
        getLogger().info(s);
        if (Bukkit.getPluginCommand("zisha") != null) {
            Objects.requireNonNull(Bukkit.getPluginCommand("zisha")).setExecutor(new Kill_Command());
        }
        if (Bukkit.getPluginCommand("help") != null) {
            Objects.requireNonNull(Bukkit.getPluginCommand("help")).setExecutor(new Help_Command());
        }
        if (Bukkit.getPluginCommand("tell") != null) {
            Objects.requireNonNull(Bukkit.getPluginCommand("tell")).setExecutor(new Tell_Command());
        }
        if (Bukkit.getPluginCommand("out") != null) {
            Objects.requireNonNull(Bukkit.getPluginCommand("out")).setExecutor(new Out_command());
        }
        if (Bukkit.getPluginCommand("tp") != null) {
            Objects.requireNonNull(Bukkit.getPluginCommand("tp")).setExecutor(new Tp_command());
        }
        if (Bukkit.getPluginCommand("kick") != null) {
            Objects.requireNonNull(Bukkit.getPluginCommand("kick")).setExecutor(new Kick_command());
        }
        if (Bukkit.getPluginCommand("dishao") != null) {
            Objects.requireNonNull(Bukkit.getPluginCommand("dishao")).setExecutor(new Info_Command());
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
                Player player2 = (Player) Bukkit.getPlayer(args[0]);
                if ((commandSender instanceof Player) || (!args[0].toLowerCase().equals("server"))) {
                    if(commandSender instanceof Player){
                        if((Player)commandSender == Bukkit.getPlayer(args[0])){
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
            for(Player i : Bukkit.getOnlinePlayers()){
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
                Player p = Bukkit.getPlayer(args[0]);
                PPlayer pPlayer = new PPlayer();
                if(pPlayer.isplayer(args[0])){
                    player.teleport(new Location(Bukkit.getPlayer(args[0]).getWorld(), Bukkit.getPlayer(args[0]).getLocation().getBlockX(), Bukkit.getPlayer(args[0]).getLocation().getBlockY(), Bukkit.getPlayer(args[0]).getLocation().getBlockZ()));
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
            Player p = Bukkit.getPlayer(args[0]);
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
            for(Player i : Bukkit.getOnlinePlayers()){
                l.add(i.getName());
            }
            return l;
        }
        List n = new ArrayList<>();
        return n;
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
                    for(Player p : Bukkit.getOnlinePlayers()) {
                        p.recalculatePermissions();
                    }
                }
                dishao.getPlugin(dishao.class).reloadConfig();
                config = dishao.getPlugin(dishao.class).getConfig();
                motdl = (ArrayList<String>) dishao.getPlugin(dishao.class).getConfig().getList("server-motd.motd-list");
                motdt = config.getLong("server-motd.motd-time",3000L);
                per = config.getBoolean("permissions",true);
                for(Player p : Bukkit.getOnlinePlayers()) {
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
            
            if (args[1].equals("permissions-list")) {
                List s = config.getList("permissions-list", null);
                PPlayer pPlayer = new PPlayer();
                for (int i = 0; i < n.size(); i++) {
                    String g = (String) n.get(i);
                    s.add(g);
                }
                return s;
            }else{
                List s = config.getList("off-permissions-list", null);;
                PPlayer pPlayer = new PPlayer();
                for (int i = 0; i < n.size(); i++) {
                    String g = (String) n.get(i);
                    s.add(g);
                }
                return s;
            }
        }
        List n = new ArrayList<>();
        return n;
    }
}