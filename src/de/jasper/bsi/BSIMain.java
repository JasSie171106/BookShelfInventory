package de.jasper.bsi;

import de.jasper.bsi.events.BSIEvents;
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

public class BSIMain extends JavaPlugin {

    @Override
    public void onEnable() {
        getLogger().info("Plugin Loaded!");
        Bukkit.getServer().getPluginManager().registerEvents(new BSIEvents(), this);
    }

    @Override
    public void onDisable() {
        getLogger().info("Plugin Disabled!");
        if (!BSIEvents.bookMap.isEmpty()) {
            BSIMain.saveInv();
        }
    }

    public static void saveInv() {
        BSIEvents.file.delete();
        BSIEvents.file = new File("plugins/bsi", "inventories.yml");
        BSIEvents.items = YamlConfiguration.loadConfiguration(BSIEvents.file);
        BSIEvents.bookMap.forEach((location, inventory) -> {

            List<ItemStack> items = new ArrayList<>();
            Collections.addAll(items, inventory.getContents());

            BSIEvents.items.set(String.valueOf(location), items);
        });

        try {
            BSIEvents.items.save(BSIEvents.file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static Inventory getInv(Location location) {
        Inventory inventory = Bukkit.createInventory(null, 9, "§cBookshelf §fInventory");

        if (BSIEvents.bookMap.containsKey(location))
            return BSIEvents.bookMap.get(location);

        if (BSIEvents.items.contains(String.valueOf(location))) {

            List<?> itemlist = BSIEvents.items.getList(String.valueOf(location));

            for (int i = 0; i < 9; i++) {
                assert itemlist != null;
                inventory.setItem(i, (ItemStack) itemlist.get(i));
            }
            System.out.println(inventory.toString());

            BSIEvents.bookMap.put(location, inventory);

        } else {
            inventory.setItem(0, new ItemStack(Material.BOOK));
            inventory.setItem(1, null);
            inventory.setItem(2, null);
            inventory.setItem(3, new ItemStack(Material.BOOK));
            inventory.setItem(4, null);
            inventory.setItem(5, new ItemStack(Material.BOOK));
            inventory.setItem(6, null);
            inventory.setItem(7, new ItemStack(Material.BOOK));
            inventory.setItem(8, new ItemStack(Material.BOOK));
            BSIEvents.bookMap.put(location, inventory);
        }

        return inventory;
    }
}
