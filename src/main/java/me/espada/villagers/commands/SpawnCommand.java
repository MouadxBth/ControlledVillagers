package me.espada.villagers.commands;

import me.espada.villagers.base.CustomMobSpawner;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.IronGolem;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Optional;
import java.util.stream.Stream;

import static me.espada.villagers.base.Utils.format;

public class SpawnCommand implements CommandExecutor {

  public SpawnCommand(JavaPlugin plugin) {
    Optional.ofNullable(plugin.getCommand("villagerspawn"))
        .ifPresent(command -> command.setExecutor(this));
  }

  @Override
  public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
    if (!(sender instanceof Player)) {
      System.out.println("You can not execute this command, only players can!");
      return false;
    }

    Player player = (Player) sender;

    if (args.length != 1) {
      player.sendMessage(format("&cUsage: /villagerspawn [Mob]"));
      return false;
    }

    Stream.of(EntityType.values())
        .forEach(
            entityType -> {
              if (!entityType.isAlive()) return;

              if (!entityType.getKey().getKey().equalsIgnoreCase(args[0])) {
                player.sendMessage(format("&cCouldnt find a Living entity with that name!"));
                return;
              }

              CustomMobSpawner.spawnAt(player.getLocation(), IronGolem.class, Villager.class);
              player.sendMessage(format("&aSuccessfuly spawned!"));
            });

    return true;
  }
}
