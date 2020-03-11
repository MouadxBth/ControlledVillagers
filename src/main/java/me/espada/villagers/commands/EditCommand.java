package me.espada.villagers.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static me.espada.villagers.base.Utilities.format;

public class EditCommand implements CommandExecutor {

  private final JavaPlugin plugin;

  public EditCommand(JavaPlugin plugin) {
    this.plugin = plugin;
    Optional.ofNullable(plugin.getCommand("villageredit"))
        .ifPresent(command -> command.setExecutor(this));
  }

  @Override
  public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
    if (!(sender instanceof Player)) {
      System.out.println("You can not execute this command, only plaeyrs can!");
      return false;
    }

    Player player = (Player) sender;

    if (args.length != 2) {
      if (args[0].equalsIgnoreCase("message")) {
        String message =
            Stream.of(args)
                .filter(string -> !string.equalsIgnoreCase("message"))
                .collect(Collectors.joining());

        setAndSave("message", message);
        reloadMessage("&aSuccessfuly modified the message to " + message, player);
        return true;
      } else {
        player.sendMessage(format("&cUsage: /villageredit [delay/period] [time in seconds]"));
        return false;
      }
    }

    switch (args[0].toLowerCase()) {
      case "period":
        int period;
        try {
          period = Integer.parseInt(args[1]);
        } catch (Exception e) {
          player.sendMessage(format("&c" + args[1] + " is not an integer!"));
          return false;
        }

        setAndSave("period", period);
        reloadMessage("&aSuccessfuly modified the period to &6" + period + " &aseconds", player);
        break;
      case "delay":
        int delay;
        try {
          delay = Integer.parseInt(args[1]);
        } catch (Exception e) {
          player.sendMessage(format("&c" + args[1] + " is not an integer!"));
          return false;
        }

        setAndSave("delay", delay);
        reloadMessage("&aSuccessfuly modified the delay to &6" + delay + " &aseconds", player);
        break;
      case "chance":
        double chance;
        try {
          chance = Double.parseDouble(args[1]);
        } catch (Exception e) {
          player.sendMessage(format("&c" + args[1] + " is not a double!"));
          return false;
        }

        setAndSave("chance", chance);
        reloadMessage("&aSuccessfuly modified the chance to &6" + chance + " &a%", player);
        break;
    }

    return false;
  }

  private void reloadMessage(String message, Player player) {
    player.sendMessage(format(message));
    player.sendMessage(format("&aPlease reload the plugin for the new changes to be applied!"));
  }

  private void setAndSave(String key, Object value) {
    plugin.getConfig().set(key, value);
    plugin.saveConfig();
  }
}
