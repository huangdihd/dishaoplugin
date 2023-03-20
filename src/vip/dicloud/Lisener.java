package vip.dicloud;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.inventory.*;
import org.bukkit.event.player.*;
import org.bukkit.event.server.*;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
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
        File PlayerData = new File(dishao.getPlugin(dishao.class).getDataFolder().getAbsolutePath() + File.separator + "PlayerData");
        boolean b = false;
        boolean isonline = false;
        String playerName = e.getPlayer().getName();
        if (playerName.length() >= 3 && playerName.length() <= 16 && playerName.matches("[a-zA-Z0-9_]+")) {
            if (config.getBoolean("player-online-check", false)) {
                try {
                    URL url = new URL("https://api.mojang.com/users/profiles/minecraft/" + playerName);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setUseCaches(false);
                    connection.setConnectTimeout(2500);
                    connection.setReadTimeout(2500);
                    connection.connect();
                    int responseCode = connection.getResponseCode();
                    if (responseCode == HttpURLConnection.HTTP_OK) {
                        InputStream inputStream = connection.getInputStream();
                        InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                        JsonObject jsonObject = new JsonParser().parse(inputStreamReader).getAsJsonObject();
                        UUID playerUUID = UUID.fromString(jsonObject.get("id").getAsString().replaceFirst("(\\p{XDigit}{8})(\\p{XDigit}{4})(\\p{XDigit}{4})(\\p{XDigit}{4})(\\p{XDigit}+)", "$1-$2-$3-$4-$5"));
                        isonline = e.getPlayer().getUniqueId().equals(playerUUID);
                        //e.getPlayer().sendMessage(ChatColor.BLUE + playerUUID.toString() + ChatColor.GREEN + e.getPlayer().getUniqueId() + ChatColor.YELLOW + isonline);
                    } else if (responseCode == HttpURLConnection.HTTP_FORBIDDEN) {
                        e.getPlayer().kickPlayer("微软服务器繁忙，请稍后再试(最长10分钟)");
                    } else if (responseCode == HttpURLConnection.HTTP_GATEWAY_TIMEOUT || responseCode == HttpURLConnection.HTTP_CLIENT_TIMEOUT) {
                        e.getPlayer().kickPlayer("连接微软正版验证api超时,如多次发生这个错误,请联系腐竹关闭正版检测功能或更换正版账号不允许的用户名");
                    }
                } catch (Exception E) {
                    e.getPlayer().kickPlayer("无法连接微软正版验证api,请联系腐竹关闭正版检测功能或更换正版账号不允许的用户名");
                }
            }
        }
        if(Objects.requireNonNull(PlayerData.listFiles()).length != 0) {
            for (File i : PlayerData.listFiles()){
                FileConfiguration playerconfig = YamlConfiguration.loadConfiguration(i);
                    if(i.getName().equals(e.getPlayer().getName() + ".yml")){
                        playerconfig.set("name",e.getPlayer().getName());
                        playerconfig.set("uuid",e.getPlayer().getUniqueId().toString());
                        playerconfig.set("last-ip",Objects.requireNonNull(e.getPlayer().getAddress()).getHostString());
                        playerconfig.set("isonline",isonline);
                        b = true;
                        try {
                            playerconfig.save(new File(dishao.getPlugin(dishao.class).getDataFolder().getAbsolutePath() + File.separator + "PlayerData" + File.separator + e.getPlayer().getName() + ".yml"));
                        } catch (IOException ex) {
                            throw new RuntimeException(ex);
                        }
                        break;
                }
            }
        }
        if(isonline){
            String newname = config.getString("online-player-prefix","§a[正版玩家]§r") + e.getPlayer().getName();
            e.getPlayer().setDisplayName(newname);
            e.getPlayer().setPlayerListName(newname);
        }
        if(!b){
            File file = new File(dishao.getPlugin(dishao.class).getDataFolder().getAbsolutePath() + File.separator + "PlayerData" + File.separator + e.getPlayer().getName() + ".yml");
            try {
                file.createNewFile();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
            FileConfiguration pc = YamlConfiguration.loadConfiguration(file);
            pc.set("name",e.getPlayer().getName());
            pc.set("uuid",e.getPlayer().getUniqueId().toString());
            pc.set("last-ip",Objects.requireNonNull(e.getPlayer().getAddress()).getHostString());
            pc.set("isonline",isonline);
            try {
                pc.save(file);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }
        e.setJoinMessage(config.getString("join-message","§9玩家%player%加入了游戏!").replace("%player%",e.getPlayer().getName()));
        Bukkit.getScheduler().runTask(plugin, new Runnable() {
            @Override
            public void run() {
                if(config.getString("superuser","").equals(e.getPlayer().getName())){
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
        e.setQuitMessage(config.getString("quit-message","§c玩家%player%退出了游戏!").replace("%player%",e.getPlayer().getName()));
    }
    @EventHandler
    public void OnMotd(ServerListPingEvent e){
        if(config.getBoolean("server-motd.enable")) {
            e.setMotd(Motd.replace("%nl%", "\n"));
            e.setServerIcon(icon);
            if (config.getBoolean("server-motd.load-print")) {
                if (!Motd.contains("%nl%")) {
                    dishao.getPlugin(dishao.class).getLogger().info("ip为" + e.getAddress() + "的玩家加载了标题:" + Motd);
                } else {
                    for (String i : Motd.split("%nl%")) {
                        dishao.getPlugin(dishao.class).getLogger().info("ip为" + e.getAddress() + "的玩家加载了标题:" + i);
                    }
                }
            }
        }
    }
    @EventHandler
    public void Onchangeinv(InventoryClickEvent e){
        if(e.getWhoClicked().getOpenInventory().getTitle().contains("关于玩家")) {
            if(Objects.requireNonNull(Objects.requireNonNull(Objects.requireNonNull(Objects.requireNonNull(e.getWhoClicked().getOpenInventory().getTopInventory()).getItem(1)).getItemMeta()).getLore()).size() == 1) {
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
                    if(Objects.requireNonNull(Objects.requireNonNull(Objects.requireNonNull(Objects.requireNonNull(e.getWhoClicked().getOpenInventory().getTopInventory()).getItem(1)).getItemMeta()).getLore()).size() == 1) {
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
            if(Objects.requireNonNull(Objects.requireNonNull(Objects.requireNonNull(Objects.requireNonNull(e.getWhoClicked().getOpenInventory().getTopInventory()).getItem(1)).getItemMeta()).getLore()).size() == 1) {
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
    public void Ons(PlayerToggleSneakEvent e){
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