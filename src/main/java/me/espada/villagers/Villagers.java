package me.espada.villagers;

import me.espada.villagers.commands.MutateCommand;
import me.espada.villagers.commands.SpawnCommand;
import me.espada.villagers.listeners.CustomVillagerSpawner;
import me.espada.villagers.listeners.EntityAttack;
import me.espada.villagers.listeners.EntityDeath;
import me.espada.villagers.listeners.EntityDismount;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;

public final class Villagers extends JavaPlugin {

  private CustomVillagerSpawner spawner;

  @Override
  public void onEnable() {
    handleConfig();
    loadCommands();
    loadListeners();

    getLogger().info("Enabled Controlled Villagers!");
  }

  @Override
  public void onDisable() {
    HandlerList.unregisterAll(this);
    spawner.clear();
    getLogger().info("Disabled Controlled Villagers!");
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
    new EntityDismount(this);
    spawner = new CustomVillagerSpawner(this);
  }
}
