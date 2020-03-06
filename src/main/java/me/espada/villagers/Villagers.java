package me.espada.villagers;

import me.espada.villagers.commands.MutateCommand;
import me.espada.villagers.commands.SpawnCommand;
import me.espada.villagers.listeners.CustomVillagerSpawner;
import me.espada.villagers.listeners.EntityAttack;
import me.espada.villagers.listeners.EntityDeath;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;

public final class Villagers extends JavaPlugin {

  @Override
  public void onEnable() {
    handleConfig();
    loadCommands();
    loadListeners();
  }

  @Override
  public void onDisable() {
    HandlerList.unregisterAll(this);
  }

  private void handleConfig() {
    this.saveDefaultConfig();
  }

  private void loadCommands() {
    new SpawnCommand(this);
    new MutateCommand(this);
  }

  private void loadListeners() {
    new EntityAttack(this);
    new EntityDeath(this);
    new CustomVillagerSpawner(this);
  }
}
