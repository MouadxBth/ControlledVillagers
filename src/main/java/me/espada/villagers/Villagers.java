package me.espada.villagers;

import me.espada.villagers.commands.MutateCommand;
import me.espada.villagers.commands.SpawnCommand;
import me.espada.villagers.listeners.CustomVillagerSpawner;
import me.espada.villagers.listeners.EntityAttack;
import me.espada.villagers.listeners.EntityDeath;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Optional;

public final class Villagers extends JavaPlugin {

  @Override
  public void onEnable() {
    loadCommands();
    loadListeners();
  }

  @Override
  public void onDisable() {
    HandlerList.unregisterAll(this);
  }

  private void loadCommands() {
    Optional.ofNullable(getCommand("spawn"))
        .ifPresent(command -> command.setExecutor(new SpawnCommand()));
    Optional.ofNullable(getCommand("mutate"))
        .ifPresent(command -> command.setExecutor(new MutateCommand()));
  }

  private void loadListeners() {
    new EntityAttack(this);
    new EntityDeath(this);
    new CustomVillagerSpawner(this);
  }
}
