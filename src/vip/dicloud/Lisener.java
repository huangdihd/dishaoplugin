package vip.dicloud;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.*;
import org.bukkit.event.server.ServerListPingEvent;
import org.bukkit.plugin.Plugin;

import java.util.List;

import static org.bukkit.Bukkit.getLogger;
import static vip.dicloud.dishao.config;
import static vip.dicloud.dishao.Motd;

class ti{
    public ti(Player player,String liyou){
        player.kickPlayer(liyou);
    }
}

public class Lisener implements org.bukkit.event.Listener {
    @EventHandler
    public void name(PlayerJoinEvent e) {
        boolean per = config.getBoolean("permissions",true);
        if(per) {
            List c = config.getList("permissions-list", null);
            getLogger().info(e.getPlayer().getName() + "进入了游戏");
            String s;
            for (int i = 0; i < c.size(); i++) {
                s = (String) c.get(i);
                if(!e.getPlayer().isOp()){
                    e.getPlayer().addAttachment((Plugin) dishao.getPlugin(dishao.class), s, true);
                }
            }
            c = config.getList("off-permissions-list", null);
            for (int i = 0; i < c.size(); i++) {
                s = (String) c.get(i);
                if(!e.getPlayer().isOp()) {
                    e.getPlayer().addAttachment((Plugin) dishao.getPlugin(dishao.class), s, false);
                }
            }
        }
        boolean b = true;
        String _name = e.getPlayer().getName();
        for(byte _b : _name.getBytes()){
            if(!Character.isLetter(_b)
                    && !Character.isDigit(_b)
                    && _b != '_'
                    && _b != '-'){
                b = false;
                break;
            }
        }
        if(b){
            getLogger().info("true");
        }else{
            getLogger().info("false");
        }
        if(!b) {
            new ti(e.getPlayer(), ChatColor.RED + "您的名字不符合服务器要求");
            e.setJoinMessage("");
            return;
        }
        e.setJoinMessage(ChatColor.BLUE + "玩家" + e.getPlayer().getName() + "加入了游戏!");
    }
    @EventHandler
    public void KickMessage(PlayerKickEvent e) {
        getLogger().info(e.getPlayer().getName() + "被踢出了游戏");
    }
    @EventHandler
    public void QuitMessage(PlayerQuitEvent e) {
        boolean per = config.getBoolean("permissions", true);
        if (per) {
            e.getPlayer().recalculatePermissions();
        }
        boolean b = true;
        String _name = e.getPlayer().getName();
        for (byte _b : _name.getBytes()) {
            if (!Character.isLetter(_b)
                    && !Character.isDigit(_b)
                    && _b != '_'
                    && _b != '-') {
                b = false;
                break;
            }
        }
        if (b) {
            getLogger().info("true");
        } else {
            getLogger().info("false");
        }
        if (!b) {
            e.setQuitMessage("");
            return;
        }
        e.setQuitMessage(ChatColor.RED + "玩家" + e.getPlayer().getName() + "退出了游戏!");
    }
    @EventHandler
    public void OnCommand(PlayerCommandPreprocessEvent e){
        if(e.getMessage().substring(1,e.getMessage().indexOf(" ")).equals("op") || e.getMessage().substring(1,e.getMessage().indexOf(" ")).equals("deop")){
            boolean per = config.getBoolean("permissions",true);
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
        }
    }
    @EventHandler
    public void OnMotd(ServerListPingEvent e){
        e.setMotd(Motd.replace("%nl%","\n"));
        if(config.getBoolean("server-motd.load-print")){
            if(!Motd.contains("%nl%")){
                dishao.getPlugin(dishao.class).getLogger().info("ip为" + e.getAddress() + "的玩家加载了标题:" + Motd);
            }else{
                for(String i : Motd.split("%nl%")){
                    dishao.getPlugin(dishao.class).getLogger().info("ip为" + e.getAddress() + "的玩家加载了标题:" + i);
                }
            }
        }
    }
}