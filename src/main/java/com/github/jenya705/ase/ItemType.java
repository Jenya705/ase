package com.github.jenya705.ase;

import net.kyori.adventure.text.Component;
import org.bukkit.Material;


/**
 * @author Jenya705
 */
public enum ItemType {

    BASEPLATE_CHANGER(Material.DIAMOND_PICKAXE, "Baseplate changer"),
    ARMS(Material.STICK, "Arms")
    ;

    private final Material material;
    private final Component name;
    ItemType(Material material, String name) {
        this.material = material;
        this.name = Component.text(name);
    }

    public Material getMaterial() {
        return material;
    }

    public Component getName() {
        return name;
    }
}
