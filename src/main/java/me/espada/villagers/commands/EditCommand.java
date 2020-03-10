package me.espada.villagers.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Optional;

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
      player.sendMessage(format("&cUsage: /villageredit [delay/period] [time in seconds]"));
      return false;
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

        plugin.getConfig().set("period", period);
        plugin.saveConfig();
        player.sendMessage("&cSuccessfuly modified the period to &6" + period + " &aseconds");
        player.sendMessage("&aPlease reload the plugin for the new changes to be applied!");
        break;
      case "delay":
        int delay;
        try {
          delay = Integer.parseInt(args[1]);
        } catch (Exception e) {
          player.sendMessage(format("&c" + args[1] + " is not an integer!"));
          return false;
        }

        plugin.getConfig().set("delay", delay);
        plugin.saveConfig();
        player.sendMessage("&cSuccessfuly modified the delay to &6" + delay + " &aseconds");
        player.sendMessage("&aPlease reload the plugin for the new changes to be applied!");
        break;
      case "chance":
        double chance;
        try {
          chance = Double.parseDouble(args[1]);
        } catch (Exception e) {
          player.sendMessage(format("&c" + args[1] + " is not a double!"));
          return false;
        }

        plugin.getConfig().set("chance", chance);
        plugin.saveConfig();
        player.sendMessage("&cSuccessfuly modified the chance to &6" + chance + " &a%");
        player.sendMessage("&aPlease reload the plugin for the new changes to be applied!");
        break;
    }

    return false;
  }
}
