package com.github.jenya705.ase;

import de.tr7zw.changeme.nbtapi.NBTItem;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

public final class AsePlugin extends JavaPlugin {

    public static final String typeKey = "ase_t";

    private static AsePlugin instance;

    public static AsePlugin getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {
        instance = this;
        getServer().getPluginManager().registerEvents(new AseListener(), this);
        getCommand("ase").setExecutor(new AseCommand());
    }

    @Override
    public void onDisable() {

    }

    public ItemStack createItem(ItemType type) {
        ItemStack itemStack = new ItemStack(type.getMaterial());
        ItemMeta meta = itemStack.getItemMeta();
        meta.displayName(type.getName());
        return setType(itemStack, type);
    }

    public ItemStack setType(ItemStack itemStack, ItemType itemType) {
        NBTItem nbtItem = new NBTItem(itemStack);
        nbtItem.setByte(typeKey, (byte) itemType.ordinal());
        return nbtItem.getItem();
    }

    public ItemType getType(ItemStack itemStack) {
        NBTItem nbtItem = new NBTItem(itemStack);
        return nbtItem.hasKey(typeKey) ? ItemType.values()[nbtItem.getByte(typeKey)] : null;
    }

}
