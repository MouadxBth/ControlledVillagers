package me.espada.villagers.commands;

import me.espada.villagers.base.CustomMobSpawner;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Optional;
import java.util.stream.Stream;

import static me.espada.villagers.base.Utils.format;
import static me.espada.villagers.base.Utils.isLookingAt;

public class MutateCommand implements CommandExecutor {

  public MutateCommand(JavaPlugin plugin) {
    Optional.ofNullable(plugin.getCommand("villagermutate"))
        .ifPresent(command -> command.setExecutor(this));
  }

  @Override
  public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
    if (!(sender instanceof Player)) {
      System.out.println("You can not execute this command, only plaeyrs can!");
      return false;
    }

    Player player = (Player) sender;

    if (args.length != 1) {
      player.sendMessage(format("&cUsage: /villagermutate [Mob]"));
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
      player
          .getNearbyEntities(20, player.getLocation().getY(), 20)
          .forEach(
              entity -> {
                if (entity instanceof LivingEntity) {

                  if (!isLookingAt(player, (LivingEntity) entity)) {
                    return;
                  }

                  if (!entity.getType().equals(EntityType.VILLAGER)) {
                    player.sendMessage(format("&cYou are not looking at a villager!"));
                    return;
                  }

                  CustomMobSpawner.mutate(
                      player.getLocation(), entityType.get().getEntityClass(), entity);
                  player.sendMessage(format("&aSuccessfuly mutated!"));
                }
              });
    }

    return true;
  }
}
