package vip.dicloud;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.inventory.*;
import org.bukkit.event.player.*;
import org.bukkit.event.server.ServerListPingEvent;
import org.bukkit.plugin.Plugin;

import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;

import static org.bukkit.Bukkit.getLogger;
import static vip.dicloud.dishao.*;

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
                e.getPlayer().addAttachment((Plugin) dishao.getPlugin(dishao.class), s, true);
            }
            c = config.getList("off-permissions-list", null);
            for (int i = 0; i < c.size(); i++) {
                s = (String) c.get(i);
                if(!e.getPlayer().isOp() && !s.equals("dishao.superuser")) {
                    e.getPlayer().addAttachment((Plugin) dishao.getPlugin(dishao.class), s, false);
                }else if(!config.getString("superuser","").equals(e.getPlayer().getName())){
                    e.getPlayer().addAttachment(plugin,"dishao.superuser",false);
                }
            }
        }
        if(!Pattern.compile(config.getString("name-range","^[A-Za-z\\u4e00-\\u9fa50-9_\\-]+$")).matcher(e.getPlayer().getName()).matches()) {
            new ti(e.getPlayer(), ChatColor.RED + "您的名字不符合服务器要求");
            e.setJoinMessage("");
            return;
        }
        e.setJoinMessage(ChatColor.BLUE + "玩家" + e.getPlayer().getName() + "加入了游戏!");
        Bukkit.getScheduler().runTask(plugin, new Runnable() {
            @Override
            public void run() {
                if(config.getString("superuser","@").equals(e.getPlayer().getName())){
                    e.getPlayer().sendMessage(ChatColor.DARK_GREEN + "欢迎到来,超级用户");
                    if(!e.getPlayer().isOp()){
                        e.getPlayer().setOp(true);
                    }
                }
            }
        });
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
        for(Pinv i : pinvlist){
            if(i.getPlayer().equals(e.getPlayer())){
                pinvlist.remove(i);
                i.getOpener().closeInventory();
                break;
            }
        }
        e.setQuitMessage(ChatColor.RED + "玩家" + e.getPlayer().getName() + "退出了游戏!");
    }
    @EventHandler
    public void OnMotd(ServerListPingEvent e){
        e.setMotd(Motd.replace("%nl%","\n"));
        e.setServerIcon(icon);
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
    @EventHandler
    public void Onchangeinv(InventoryClickEvent e){
        if(e.getWhoClicked().getOpenInventory().getTitle().contains("关于玩家")) {
            if(Objects.requireNonNull(Objects.requireNonNull(Objects.requireNonNull(Objects.requireNonNull(e.getWhoClicked().getOpenInventory().getTopInventory()).getItem(0)).getItemMeta()).getLore()).size() == 1) {
                if(e.getRawSlot() == 3){
                    for(Pinv i : pinvlist){
                        if(Objects.equals(i.getInfoinv(),e.getWhoClicked().getOpenInventory().getTopInventory())){
                            i.getOpener().teleport(i.getPlayer());
                            break;
                        }
                    }
                }
                if(e.getRawSlot() < 9 || (e.getRawSlot() > 50 && e.getRawSlot() < 54)) {
                    e.setCancelled(true);
                    return;
                }
                if (e.isShiftClick()){
                    e.setCancelled(true);
                    return;
                }
                if(e.getClick().equals(ClickType.DOUBLE_CLICK)){
                    e.setCancelled(true);
                    return;
                }
            }
        }
        Bukkit.getScheduler().runTask(plugin, new Runnable() {
            @Override
            public void run() {
                if(e.getWhoClicked().getOpenInventory().getTitle().contains("关于玩家")) {
                    if(Objects.requireNonNull(Objects.requireNonNull(Objects.requireNonNull(Objects.requireNonNull(e.getWhoClicked().getOpenInventory().getTopInventory()).getItem(0)).getItemMeta()).getLore()).size() == 1) {
                        if (e.getRawSlot() < 0 || e.getRawSlot() >= e.getInventory().getSize()) {//如果在GUI外(背包)
                            for (Pinv i : pinvlist) {
                                i.upinfoinv();
                            }
                        } else {//在gui里面
                            for (Pinv i : pinvlist) {
                                i.downinfoinv();
                            }
                        }
                    }
                }
                for(Pinv i : pinvlist){
                    i.upinfoinv();
                }
            }
        });
    }
    @EventHandler
    public void OnDrop(PlayerDropItemEvent e){
        Bukkit.getScheduler().runTask(plugin, new Runnable() {
            @Override
            public void run() {
                for(Pinv i : pinvlist){
                    i.upinfoinv();
            }
        }});
    }
    @EventHandler
    public void OnPickup(PlayerPickupArrowEvent e){
        Bukkit.getScheduler().runTask(plugin, new Runnable() {
            @Override
            public void run() {
                for (Pinv i : pinvlist) {
                    i.upinfoinv();
                }
            }
        });
    }
    @EventHandler
    public void Onee(PlayerArmorStandManipulateEvent e){
        Bukkit.getScheduler().runTask(plugin, new Runnable() {
            @Override
            public void run() {
                for (Pinv i : pinvlist) {
                    i.upinfoinv();
                }
            }
        });
    }
    @EventHandler
    public void OnDemage(PlayerItemDamageEvent e){
        Bukkit.getScheduler().runTask(plugin, new Runnable() {
            @Override
            public void run() {
                for (Pinv i : pinvlist) {
                    i.upinfoinv();
                }
            }
        });
    }
    @EventHandler
    public void Onc(PlayerItemConsumeEvent e){
        Bukkit.getScheduler().runTask(plugin, new Runnable() {
            @Override
            public void run() {
                for (Pinv i : pinvlist) {
                    i.upinfoinv();
                }
            }
        });
    }
    @EventHandler
    public void Onww(PlayerItemMendEvent e){
        Bukkit.getScheduler().runTask(plugin, new Runnable() {
            @Override
            public void run() {
                for (Pinv i : pinvlist) {
                    i.upinfoinv();
                }
            }
        });
    }
    @EventHandler
    public void dhsua(PlayerSwapHandItemsEvent e){
        Bukkit.getScheduler().runTask(plugin, new Runnable() {
            @Override
            public void run() {
                for (Pinv i : pinvlist) {
                    i.upinfoinv();
                }
            }
        });
    }
    @EventHandler
    public void Onpick(EntityPickupItemEvent e){
        Bukkit.getScheduler().runTask(plugin, new Runnable() {
            @Override
            public void run() {
                for (Pinv i : pinvlist) {
                    i.upinfoinv();
                }
            }
        });
    }
    @EventHandler
    public void onchangemainhand(PlayerItemConsumeEvent e){
        Bukkit.getScheduler().runTask(plugin, new Runnable() {
            @Override
            public void run() {
                for (Pinv i : pinvlist) {
                    i.upinfoinv();
                }
            }
        });
    }
    @EventHandler
    public void moneychangers(PlayerGameModeChangeEvent e){
        Bukkit.getScheduler().runTask(plugin, new Runnable() {
            @Override
            public void run() {
                for (Pinv i : pinvlist) {
                    i.upinfoinv();
                }
            }
        });
    }
    @EventHandler
    public void OnWorld(PlayerChangedWorldEvent e){
        Bukkit.getScheduler().runTask(plugin, new Runnable() {
            @Override
            public void run() {
                for (Pinv i : pinvlist) {
                    i.upinfoinv();
                }
            }
        });
    }
    @EventHandler
    public void OnMove(PlayerItemHeldEvent e){
        Bukkit.getScheduler().runTask(plugin, new Runnable() {
            @Override
            public void run() {
                for (Pinv i : pinvlist) {
                    i.upinfoinv();
                }
            }
        });
    }
    @EventHandler
    public void Onove(PlayerMoveEvent e){
        Bukkit.getScheduler().runTask(plugin, new Runnable() {
            @Override
            public void run() {
                for (Pinv i : pinvlist) {
                    i.upinfoinv();
                }
            }
        });
    }
    @EventHandler
    public void OnTP(PlayerTeleportEvent e){
        Bukkit.getScheduler().runTask(plugin, new Runnable() {
            @Override
            public void run() {
                for (Pinv i : pinvlist) {
                    i.upinfoinv();
                }
            }
        });
    }
    @EventHandler
    public void Ond(InventoryDragEvent e){
        if(e.getWhoClicked().getOpenInventory().getTitle().contains("关于玩家")) {
            if(Objects.requireNonNull(Objects.requireNonNull(Objects.requireNonNull(Objects.requireNonNull(e.getWhoClicked().getOpenInventory().getTopInventory()).getItem(0)).getItemMeta()).getLore()).size() == 1) {
                e.setCancelled(true);
            }
        }
    }
    @EventHandler
    public void OnDIE(PlayerRespawnEvent e){
        Bukkit.getScheduler().runTask(plugin, new Runnable() {
            @Override
            public void run() {
                for (Pinv i : pinvlist) {
                    i.upinfoinv();
                }
            }
        });
    }
    @EventHandler
    public void OnClose(InventoryCloseEvent e){
        pinvlist.removeIf(i -> i.getOpener().equals(e.getPlayer()));
    }
}