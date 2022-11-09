package de.jasper.jb;

import de.jasper.jb.events.JBEvents;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.*;
public class JoelBooksMain extends JavaPlugin {

    @Override
    public void onEnable() {
        getLogger().info("Plugin Loaded!");
        Bukkit.getServer().getPluginManager().registerEvents(new JBEvents(), this);
    }

    @Override
    public void onDisable() {
        getLogger().info("Plugin Disabled!");
        if (!JBEvents.bookmap.isEmpty()) {
            JoelBooksMain.saveInv();
        }
    }

    public static void saveInv() {
        JBEvents.file.delete();
        JBEvents.file = new File("plugins/bookshelfinv", "items.yml");
        JBEvents.items = YamlConfiguration.loadConfiguration(JBEvents.file);
        JBEvents.bookmap.forEach((location, inventory) -> {

            List<ItemStack> items = new ArrayList<>();
            Collections.addAll(items, inventory.getContents());

            JBEvents.items.set(String.valueOf(location), items);
        });

        try {
            JBEvents.items.save(JBEvents.file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static Inventory getInv(Location location) {
        Inventory inventory = Bukkit.createInventory(null, 9, "§cBookshelf §fInventory");

        if (JBEvents.bookmap.containsKey(location))
            return JBEvents.bookmap.get(location);

        if (JBEvents.items.contains(String.valueOf(location))) {

            List<?> itemlist = JBEvents.items.getList(String.valueOf(location));

            for (int i = 0; i < 9; i++) {
                assert itemlist != null;
                inventory.setItem(i, (ItemStack) itemlist.get(i));
            }
            System.out.println(inventory.toString());

            JBEvents.bookmap.put(location, inventory);

        } else {
            inventory.setItem(0, new ItemStack(Material.BOOK));
            inventory.setItem(3, new ItemStack(Material.BOOK));
            inventory.setItem(5, new ItemStack(Material.BOOK));
            inventory.setItem(7, new ItemStack(Material.BOOK));
            inventory.setItem(8, new ItemStack(Material.BOOK));
            JBEvents.bookmap.put(location, inventory);
        }

        return inventory;
    }
}
