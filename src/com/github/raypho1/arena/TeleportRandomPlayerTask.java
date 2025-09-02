package com.github.raypho1.arena;

import com.github.raypho1.deahts.Main;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class TeleportRandomPlayerTask {

    private Location arenaLocation = new Location(Bukkit.getWorld("world"), 100, 64, 100); // Beispiel-Arena-Position

    public void startTeleportation() {
        // Diese Aufgabe wird alle 60 Sekunden (1200 Ticks) ausgeführt
        new BukkitRunnable() {
            @Override
            public void run() {
                teleportRandomPlayer(); // Ruft die Methode zur Teleportation des Spielers auf
            }
        }.runTaskTimer(Main.getInstance(), 0L, 200L); // Verwende hier Main.getInstance()
    }

    private void teleportRandomPlayer() {
        List<Player> onlinePlayers = new ArrayList<>(Bukkit.getServer().getOnlinePlayers());

        if (onlinePlayers.isEmpty()) {
            Bukkit.getConsoleSender().sendMessage("Es sind keine Spieler online.");
            return;
        }

        // Nimm einen zufälligen Spieler aus der Liste
        Random random = new Random();
        Player player = onlinePlayers.get(random.nextInt(onlinePlayers.size()));

        // Teleportiere den Spieler zur Arena
        player.teleport(arenaLocation);

        // Nachricht an alle Spieler senden
        Bukkit.broadcastMessage("§e" + player.getName() + " wurde in die Arena teleportiert!");
    }
}
