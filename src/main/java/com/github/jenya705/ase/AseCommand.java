package com.github.jenya705.ase;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

/**
 * @author Jenya705
 */
public class AseCommand implements TabExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length < 1) return false;
        if (args[0].equalsIgnoreCase("give")) {
            if (args.length < 2) {
                sender.sendMessage(Component.text("Specify item to give").color(NamedTextColor.RED));
                return true;
            }
            String item = args[1];
            ItemType itemType =
                    Arrays
                            .stream(ItemType.values())
                            .filter(it -> it.name().equalsIgnoreCase(item))
                            .findAny()
                            .orElse(null);
            if (itemType == null) {
                sender.sendMessage(Component.text(item + " is not exist").color(NamedTextColor.RED));
                return true;
            }
            ItemStack itemStack = AsePlugin.getInstance().createItem(itemType);
            Player player;
            if (args.length < 3) {
                if (sender instanceof Player) {
                    player = (Player) sender;
                }
                else {
                    sender.sendMessage(Component.text("Only players can use this command without third argument").color(NamedTextColor.RED));
                    return true;
                }
            }
            else {
                player = Bukkit.getPlayer(args[2]);
            }
            if (player == null) {
                sender.sendMessage(Component.text("This player is not exist").color(NamedTextColor.RED));
                return true;
            }
            player.getInventory().addItem(itemStack);
        }
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        if (args.length == 1) {
            return List.of("give");
        }
        if (args[0].equalsIgnoreCase("give")) {
            if (args.length == 2) {
                return Arrays.stream(ItemType.values())
                        .map(it -> it.name().toLowerCase(Locale.ROOT))
                        .collect(Collectors.toList());
            }
        }
        return null;
    }
}
