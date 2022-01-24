package com.github.jenya705.ase;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.Damageable;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * @author Jenya705
 */
public class AseListener implements Listener {

    private final Set<UUID> onCoolDown = new HashSet<>();

    @EventHandler
    public void rightClick(PlayerInteractAtEntityEvent event) {
        if (event.getPlayer().isSneaking()) {
            return;
        }
        Entity clickedEntity = event.getRightClicked();
        if (!(clickedEntity instanceof ArmorStand armorStand)) {
            return;
        }
        UUID uuid = event.getPlayer().getUniqueId();
        if (onCoolDown.contains(uuid)) {
            event.setCancelled(true);
            return;
        }
        PlayerInventory playerInventory = event.getPlayer().getInventory();
        ItemStack usedItem;
        if (event.getHand() == EquipmentSlot.HAND) {
            usedItem = playerInventory.getItemInMainHand();
        }
        else if (event.getHand() == EquipmentSlot.OFF_HAND) {
            usedItem = playerInventory.getItemInOffHand();
        }
        else {
            return;
        }
        boolean itemChanged = false;
        if (usedItem.getType() == Material.AIR) {
            return;
        }
        ItemType itemType = AsePlugin.getInstance().getType(usedItem);
        if (itemType == null) {
            return;
        }
        else if (itemType == ItemType.BASEPLATE_CHANGER) {
            armorStand.setBasePlate(!armorStand.hasBasePlate());
            if (usedItem.getItemMeta() instanceof Damageable damageable) {
                damageable.setDamage(damageable.getDamage() - 1);
                usedItem.setItemMeta(damageable);
                itemChanged = true;
            }
        }
        else if (itemType == ItemType.ARMS) {
            armorStand.setArms(true);
            usedItem.setType(Material.AIR);
            itemChanged = true;
        }
        if (itemChanged) {
            if (event.getHand() == EquipmentSlot.HAND) {
                playerInventory.setItemInMainHand(usedItem);
            }
            else if (event.getHand() == EquipmentSlot.OFF_HAND) {
                playerInventory.setItemInOffHand(usedItem);
            }
        }
        coolDown(event.getPlayer());
        event.setCancelled(true);
    }

    @EventHandler
    public void leftClick(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Player player)) {
            return;
        }
        if (!(event.getEntity() instanceof ArmorStand armorStand)) {
            return;
        }
        PlayerInventory inventory = player.getInventory();
        ItemStack usedItem = inventory.getItemInMainHand();
        if (armorStand.hasArms() && isAxe(usedItem.getType())) {
            ItemStack arms = AsePlugin.getInstance().createItem(ItemType.ARMS);
            if (!inventory.addItem(arms).isEmpty()) {
                player.getWorld().dropItem(player.getLocation(), arms);
            }
            armorStand.setArms(false);
        }
        else {
            return;
        }
        event.setCancelled(true);
    }

    private void coolDown(Player player) {
        player.sendActionBar(Component.text("On cooldown").color(NamedTextColor.RED));
        onCoolDown.add(player.getUniqueId());
        Bukkit.getScheduler().runTaskLater(AsePlugin.getInstance(), () -> onCoolDown.remove(player.getUniqueId()), 20);
    }

    private boolean isAxe(Material material) {
        return material == Material.WOODEN_AXE ||
                material == Material.STONE_AXE ||
                material == Material.IRON_AXE ||
                material == Material.GOLDEN_AXE ||
                material == Material.DIAMOND_AXE ||
                material == Material.NETHERITE_AXE;
    }

}
