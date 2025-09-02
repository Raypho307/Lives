package com.github.raypho1.deahts;

import com.github.raypho1.events.DeathListener;
import com.github.raypho1.events.ReviveBlockListener;
import com.github.raypho1.items.deathtoken;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Statistic;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {
    public static Main instance;

    @Override
    public void onEnable(){
        getServer().getConsoleSender().sendMessage(ChatColor.AQUA + "[Lives]: Plugin Enabled");
        Bukkit.getPluginManager().registerEvents(new DeathListener(), this);
        Bukkit.getPluginManager().registerEvents(new ReviveBlockListener(), this); // Registriere den neuen Listener für den Beacon
        getCommand("setdeaths").setExecutor(this);
        deathtoken.init();
        instance = this;

        // Verzögere den Start des Teleportations-Tasks um 20 Ticks (1 Sekunde)
        //Bukkit.getScheduler().runTask(this, () -> {
            //new TeleportRandomPlayerTask().startTeleportation();
        //});
    }

    public static Main getInstance() {
        return instance; // Gibt die gespeicherte Instanz zurück
    }

    @Override
    public void onDisable(){
        getServer().getConsoleSender().sendMessage(ChatColor.DARK_RED + "[Lives]: Plugin Disabled");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("setdeaths")) {
            if (args.length != 2) {
                sender.sendMessage("§cVerwendung: /setdeaths <Spieler> <Anzahl>");
                return true;
            }

            Player target = Bukkit.getPlayer(args[0]);
            if (target == null) {
                sender.sendMessage("§cPlayer not found.");
                return true;
            }

            try {
                int deaths = Integer.parseInt(args[1]);
                if (deaths < 0) {
                    sender.sendMessage("§cThe number of deaths must be higher than 0.");
                    return true;
                }

                target.setStatistic(Statistic.DEATHS, deaths);
                sender.sendMessage("§aDeaths of " + target.getName() + " changed to " + deaths);
                target.sendMessage("§eYour deaths are now " + deaths);
            } catch (NumberFormatException e) {
                sender.sendMessage("§cPlease give a number.");
            }
            return true;
        }
        return false;
    }
}
