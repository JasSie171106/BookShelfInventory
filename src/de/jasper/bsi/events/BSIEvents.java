package de.jasper.bsi.events;

import de.jasper.bsi.BSIMain;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;

import java.io.File;
import java.util.*;

public class BSIEvents implements Listener {
    public static File file = new File("plugins/bsi", "inventories.yml");
    public static YamlConfiguration items = YamlConfiguration.loadConfiguration(file);
    public static final HashMap<Location, Inventory> bookMap = new HashMap<>();

    static Random random = new Random();

    @EventHandler
    public void onClickBookshelf(PlayerInteractEvent e) {
        Action a = e.getAction();
        Player p = e.getPlayer();
        Block b = e.getClickedBlock();

        if (b != null) {
            Location blockLoc = e.getClickedBlock().getLocation();
            if (b.getType() == Material.BOOKSHELF && Objects.equals(e.getHand(), EquipmentSlot.HAND) && a.equals(Action.RIGHT_CLICK_BLOCK) && !p.isSneaking()) {
                openInv(p, blockLoc);
                e.setCancelled(true);
            }
        }
    }

    public void openInv(Player p, Location loc) {
        p.openInventory(Objects.requireNonNull(BSIMain.getInv(loc)));
    }

    @EventHandler
    public static void onBreak(BlockBreakEvent e) {
        if (bookMap.containsKey(e.getBlock().getLocation())) {
            Inventory mapinv = bookMap.get(e.getBlock().getLocation());


            for (int i = 0; i < 9; i++) {
                if (mapinv.getItem(i) != null) {
                    e.getBlock().getWorld().dropItemNaturally(e.getBlock().getLocation(), Objects.requireNonNull(mapinv.getItem(i)));
                    e.getBlock().getDrops().clear();
                }
            }

            standartInv(mapinv);
        } else if (items.contains(String.valueOf(e.getBlock().getLocation()))) {
            Inventory listinv = BSIMain.getInv(e.getBlock().getLocation());

            for (int i = 0; i < 9; i++) {
                if (listinv.getItem(i) != null) {
                    e.getBlock().getWorld().dropItemNaturally(e.getBlock().getLocation(), Objects.requireNonNull(listinv.getItem(i)));
                    e.getBlock().getDrops().clear();
                }
            }

            standartInv(listinv);
        }
    }

    public static void standartInv(Inventory inv) {
        List<Integer> list = new ArrayList<Integer>();
        inv.setItem(0, null);
        inv.setItem(1, null);
        inv.setItem(2, null);
        inv.setItem(3, null);
        inv.setItem(4, null);
        inv.setItem(5, null);
        inv.setItem(6, null);
        inv.setItem(7, null);
        inv.setItem(8, null);

        for (int i = 0; i < 3; ) {
            int randomIndex = random.nextInt(0, 9);
            if (!list.contains(randomIndex)) {
                inv.setItem(randomIndex, new ItemStack(Material.BOOK));
                list.add(randomIndex);
                i++;
            }
        }

        int chanceEnchantment = random.nextInt(0, 500);
        System.out.println(chanceEnchantment);
        ItemStack book = new ItemStack(Material.ENCHANTED_BOOK);
        EnchantmentStorageMeta meta = (EnchantmentStorageMeta) book.getItemMeta();
        switch (chanceEnchantment) {
            case 93:
                meta.addEnchant(Enchantment.DURABILITY, 2, true);
                book.setItemMeta(meta);
                inv.addItem(book);
                break;
            case 134:
                meta.addEnchant(Enchantment.LOYALTY, 2, true);
                book.setItemMeta(meta);
                inv.addItem(book);
                break;
            case 246:
                meta.addEnchant(Enchantment.KNOCKBACK, 1, true);
                book.setItemMeta(meta);
                inv.addItem(book);
                break;
            case 381:
                meta.addEnchant(Enchantment.MULTISHOT, 1, true);
                book.setItemMeta(meta);
                inv.addItem(book);
                break;
            case 498:
                meta.addEnchant(Enchantment.DAMAGE_ALL, 3, true);
                book.setItemMeta(meta);
                inv.addItem(book);
                break;
            case 7:
                meta.addEnchant(Enchantment.DURABILITY, 1, true);
                book.setItemMeta(meta);
                inv.addItem(book);
                break;
        }
    }

    @EventHandler
    public void cancelPlace(BlockPlaceEvent e) {
        if (e.getBlockAgainst().getType() == Material.BOOKSHELF && !e.getPlayer().isSneaking())
            e.setBuild(false);
    }

}
