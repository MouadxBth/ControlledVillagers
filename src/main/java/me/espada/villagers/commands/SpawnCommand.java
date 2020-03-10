package me.espada.villagers.commands;

import me.espada.villagers.base.CustomMobSpawner;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Optional;
import java.util.stream.Stream;

import static me.espada.villagers.base.Utilities.format;

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

    final Optional<EntityType> entityType =
        Stream.of(EntityType.values())
            .filter(entity -> entity.name().equalsIgnoreCase(args[0]))
            .findAny();

    if (!entityType.isPresent()) {
      player.sendMessage(format("&cCouldnt find a Living entity with that name!"));
      return false;
    }

    if (!entityType.get().isAlive()) {
      player.sendMessage(format("&cThats not a living entity"));
      return false;
    } else {
      CustomMobSpawner.spawnAt(player.getLocation(), entityType.get().getEntityClass(), Villager.class);
      player.sendMessage(format("&aSuccessfuly spawned!"));
    }

    return true;
  }
}
