package me.serbob.toastedplaytime;

import me.serbob.toastedplaytime.Commands.Playtime;
import me.serbob.toastedplaytime.Events.PlayerEvents;
import me.serbob.toastedplaytime.Managers.DatabaseManager;
import org.bukkit.Bukkit;
import org.bukkit.Statistic;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.sql.SQLException;

public final class ToastedPlaytime extends JavaPlugin {

    @Override
    public void onEnable() {
        saveDefaultConfig();
        getCommand("toastedpt").setExecutor(new Playtime());
        getServer().getPluginManager().registerEvents(new PlayerEvents(),this);
        connectDatabase();
        startScheduler();
    }

    @Override
    public void onDisable() {
    }

    public void connectDatabase() {
        String dbUrl = "jdbc:mysql://" + getConfig().getString("databaseHost") + ":" + getConfig().getString("databasePort");
        String dbUser = getConfig().getString("databaseUsername");
        String dbPassword = getConfig().getString("databasePassword");
        System.out.println("Database CONNECTED!");
        try {
            DatabaseManager.instance = new DatabaseManager(dbUrl, dbUser, dbPassword, getConfig().getString("databaseName"), "PlaytimeTable");
        } catch (RuntimeException e) {
            getServer().getPluginManager().disablePlugin(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void startScheduler() {
        new BukkitRunnable() {
            @Override
            public void run() {
                for(Player player: Bukkit.getOnlinePlayers()) {
                    try {
                        DatabaseManager.instance.setTotalPlaytime(player);
                        //DatabaseManager.instance.setUnclaimedPlaytime(player);
                    } catch (Exception ignored){};
                }
            }
        }.runTaskTimer(this, 0L, 20L*60);
    }
}
