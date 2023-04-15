package me.serbob.toastedplaytime.Commands;

import me.serbob.toastedplaytime.Managers.DatabaseManager;
import org.bukkit.ChatColor;
import org.bukkit.Statistic;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.sql.SQLException;

public class Playtime implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player player=(Player) sender;
        if(args.length<1) {
            sender.sendMessage(ChatColor.RED + "Usage: /playtime <check/claim>");
            return false;
        }
        int playtime = player.getStatistic(Statistic.PLAY_ONE_MINUTE);
        int days = playtime / (20 * 60 * 60 * 24);
        int weeks = days / 7;
        // !
        int hours = (playtime / (20 * 60 * 60)) % 24;
        // !
        int minutes = (playtime / (20 * 60)) % 60;
        int seconds = (playtime / 20) % 60;
        if(args[0].equalsIgnoreCase("check")) {
            player.sendMessage("Player " + player.getName() + " has played for " + weeks + " weeks, " +
                    days%7 + " days, " +
                    hours + " hours, " + minutes + " minutes and " + seconds + " seconds.");
        } else if(args[0].equalsIgnoreCase("claim")) {
            sender.sendMessage(ChatColor.RED+"WARNING");
            sender.sendMessage(ChatColor.GREEN+"If you don't see your current playtime don't worry, it updates every minute!");
            int claimedpt;
            int totalpt;
            try {
                totalpt = DatabaseManager.instance.getTotalPlaytime(player);
                claimedpt = DatabaseManager.instance.getClaimedPlaytime(player);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            sender.sendMessage(ChatColor.AQUA + "" + claimedpt);
            sender.sendMessage(ChatColor.AQUA + "" + totalpt);
            sender.sendMessage(ChatColor.GREEN+"You have claimed "+(totalpt-claimedpt)+" playtime points");
            try {
                DatabaseManager.instance.setClaimedPlaytime(player,
                        totalpt-claimedpt
                );
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        return true;
    }
}
