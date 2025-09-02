package com.github.raypho1.events;

import com.github.raypho1.items.deathtoken;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public class DeathListener implements Listener {
    private static final int MAX_DEATHS = 5; // Anzahl der Tode, bevor etwas passiert
    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        int deaths = player.getStatistic(Statistic.DEATHS); // Anzahl der Tode abrufen

        if (deaths >= MAX_DEATHS) {
            // Beispiel: Spieler wird zum Spawn teleportiert und erhält eine Nachricht
            player.teleport(player.getWorld().getSpawnLocation());
            player.setGameMode(GameMode.SPECTATOR);
            Bukkit.broadcastMessage(ChatColor.DARK_AQUA + "⚠️ " + player.getName() + " is out! ⚠️");
        } else {
            Bukkit.broadcastMessage(ChatColor.GOLD + "⚠️ " + player.getName() + " has died " + deaths + " times! ⚠️");
            player.sendMessage("Deaths: " + deaths + "/" + MAX_DEATHS);
        }
    }
    @EventHandler
    public static void onRightClick(PlayerInteractEvent event){
        if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK){
            if(event.getItem() != null){
                if (event.getItem().getItemMeta().equals(deathtoken.token.getItemMeta())){
                    Player player = event.getPlayer();
                    PlayerInventory inventory = player.getInventory();
                    int currentDeaths = player.getStatistic(Statistic.DEATHS);
                    if (currentDeaths > 1) {
                        player.setStatistic(Statistic.DEATHS, currentDeaths - 1);
                        player.sendMessage("§o§4Death removed");
                        ItemStack itemInHand = player.getInventory().getItemInMainHand();
                        if (itemInHand.getAmount() > 1) {
                            itemInHand.setAmount(itemInHand.getAmount() - 1);
                        }
                        else {
                            // Wenn nur 1 Item vorhanden ist, entferne es komplett
                            player.getInventory().setItemInMainHand(new ItemStack(Material.SUNFLOWER));
                        }

                    }

                }
            }
        }
    }

}
