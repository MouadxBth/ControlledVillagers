package me.espada.villagers.commands;

import me.espada.villagers.base.CustomMobSpawner;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;

import java.util.stream.Stream;

import static me.espada.villagers.base.Utils.format;
import static me.espada.villagers.base.Utils.isLookingAt;

public class MutateCommand implements CommandExecutor {

  @Override
  public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
    if (!(sender instanceof Player)) {
      System.out.println("You can not execute this command, only plaeyrs can!");
      return false;
    }
    if (args.length > 1) return false;

    Player player = (Player) sender;

    Stream.of(EntityType.values())
        .forEach(
            entityType -> {
              if (!entityType.isAlive()) return;

              if (entityType.getKey().getKey().equalsIgnoreCase(args[0])) {

                player
                    .getNearbyEntities(10, 10, 10)
                    .forEach(
                        entity -> {
                          if (entity instanceof LivingEntity) {

                            if (!isLookingAt(player, (LivingEntity) entity)) {
                              player.sendMessage(format("&cYou are not looking at a villager!"));
                              return;
                            }

                            if (!(entity instanceof Villager)) {
                              player.sendMessage(format("&cYou are not looking at a villager!"));
                              return;
                            }

                            CustomMobSpawner.mutate(
                                player.getLocation(), entityType.getEntityClass(), entity);
                            player.sendMessage(format("&aSuccessfuly mutated!"));
                          }
                        });
              } else {
                player.sendMessage(format("&cCouldnt find a Living entity with that name!"));
              }
            });

    return true;
  }
}
