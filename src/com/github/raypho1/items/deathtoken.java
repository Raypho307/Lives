package com.github.raypho1.items;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class deathtoken {


    public static ItemStack token;

    public static void init(){
        createToken();
    }
    public static void createToken(){
        ItemStack item = new ItemStack(Material.SUNFLOWER, 1);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName("§6§l§nToken");
        List<String> lore =new ArrayList<>();
        lore.add("§bThis Token gives you minus one Death");
        meta.setLore(lore);
        meta.addEnchant(Enchantment.LOYALTY, 1, false);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        item.setItemMeta(meta);
        token = item;

        ShapedRecipe sr = new ShapedRecipe(NamespacedKey.minecraft("token"), item);
        sr.shape(" B ", "D D", "DDD");
        sr.setIngredient('B', Material.BOOK);
        sr.setIngredient('D', Material.DIAMOND);
        Bukkit.getServer().addRecipe(sr);
    }
}
