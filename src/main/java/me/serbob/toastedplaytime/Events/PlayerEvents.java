package me.serbob.toastedplaytime.Events;

import me.serbob.toastedplaytime.Managers.DatabaseManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.sql.SQLException;

public class PlayerEvents implements Listener {
    @EventHandler
    public void PlayerJoined(PlayerJoinEvent event) throws SQLException {
        Player player = event.getPlayer();
        DatabaseManager.instance.addPlayer(player);
    }
}
