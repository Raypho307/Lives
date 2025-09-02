package com.github.raypho1.events;

import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;
import com.github.raypho1.deahts.Main;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class ReviveBlockListener implements Listener {

    private static final Set<UUID> deadPlayers = new HashSet<>();

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        if (player.getStatistic(Statistic.DEATHS) >= 5) { // Spieler wird "permanent tot"
            deadPlayers.add(player.getUniqueId());
            player.sendMessage(ChatColor.RED + "You are death and must be revieved!");
            player.setGameMode(GameMode.SPECTATOR); // Setzt den Spieler in Zuschauermodus
        }
    }

    @EventHandler
    public void onBeaconRightClick(PlayerInteractEvent event) {
        if (event.getClickedBlock() != null && event.getClickedBlock().getType() == Material.BEACON) {
            event.setCancelled(true);
            Player player = event.getPlayer();
            Location beaconLocation = event.getClickedBlock().getLocation();
            openReviveGUI(event.getPlayer(), beaconLocation);

            // Speichere die Location des Beacons als Metadata des Spielers
            player.setMetadata("revive_beacon_location", new FixedMetadataValue(Main.getInstance(), beaconLocation));
        }
    }


    private void openReviveGUI(Player player, Location beaconLocation) {
        Inventory gui = Bukkit.createInventory(null, 9, ChatColor.GOLD + "Choose a Player to revive");

        for (UUID uuid : deadPlayers) {
            Player deadPlayer = Bukkit.getPlayer(uuid);
            if (deadPlayer != null) {
                ItemStack playerHead = new ItemStack(Material.PLAYER_HEAD);
                ItemMeta meta = playerHead.getItemMeta();
                meta.setDisplayName(ChatColor.RED + deadPlayer.getName());
                playerHead.setItemMeta(meta);
                gui.addItem(playerHead);
            }
        }

        player.openInventory(gui);
        player.setMetadata("revive_beacon", new FixedMetadataValue(Main.getInstance(), beaconLocation)); // Location speichern
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getView().getTitle().equals(ChatColor.GOLD + "Choose a Player to revive")) {
            event.setCancelled(true);

            ItemStack clickedItem = event.getCurrentItem();
            if (clickedItem != null && clickedItem.getType() == Material.PLAYER_HEAD) {
                String playerName = ChatColor.stripColor(clickedItem.getItemMeta().getDisplayName());
                Player revivedPlayer = Bukkit.getPlayer(playerName);

                if (revivedPlayer != null) {
                    revivedPlayer.setHealth(0.0);
                    revivedPlayer.setStatistic(Statistic.DEATHS, 0);
                    revivedPlayer.setGameMode(GameMode.SURVIVAL);
                    revivedPlayer.sendMessage(ChatColor.GREEN + "You are revived");

                    // Entferne den Spieler aus der Liste der toten Spieler
                    deadPlayers.remove(revivedPlayer.getUniqueId());

                    // Abrufe die Beacon-Location aus der Metadata des Spielers
                    Player clicker = (Player) event.getWhoClicked();
                    Location beaconLocation = (Location) clicker.getMetadata("revive_beacon_location").get(0).value();

                    // Hier kannst du die Location des Beacons verwenden (z.B. Beacon deaktivieren)
                    // beaconLocation.getBlock().setType(Material.AIR); // Beacon entfernen, falls gewünscht

                    clicker.sendMessage(ChatColor.GREEN + "You revived " + playerName);
                    clicker.closeInventory();
                }
            }
        }
    }


    private void activateBeacon(Location beaconLocation) {
        beaconLocation.getWorld().playSound(beaconLocation, Sound.BLOCK_BEACON_ACTIVATE, 1.0f, 1.0f);
        beaconLocation.getWorld().spawnParticle(Particle.END_ROD, beaconLocation.add(0.5, 1, 0.5), 50, 0.5, 1, 0.5, 0.1);

        new BukkitRunnable() {
            @Override
            public void run() {
                beaconLocation.getWorld().playSound(beaconLocation, Sound.BLOCK_BEACON_DEACTIVATE, 1.0f, 1.0f);
            }
        }.runTaskLater(Main.getInstance(), 100);
    }
}
