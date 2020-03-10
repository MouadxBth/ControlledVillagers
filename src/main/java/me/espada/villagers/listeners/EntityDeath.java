package me.espada.villagers.listeners;

import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Mob;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class EntityDeath implements Listener {

  public EntityDeath(JavaPlugin plugin) {
    plugin.getServer().getPluginManager().registerEvents(this, plugin);
  }

  @EventHandler
  public void onDeath(EntityDeathEvent entityDeathEvent) {
    LivingEntity entity = entityDeathEvent.getEntity();
    final Entity passenger = entity.getPassenger();

    if (passenger != null) {
      if (passenger instanceof LivingEntity) {
        if (passenger instanceof Mob
            && passenger.getCustomName() != null
            && passenger.getCustomName().contains("number")) {
          passenger.setInvulnerable(false);
          ((LivingEntity) passenger).damage(9999999.9);
        }
      }
    }
  }
}
