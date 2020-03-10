package me.espada.villagers.listeners;

import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Mob;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class EntityAttack implements Listener {

  public EntityAttack(JavaPlugin plugin) {
    plugin.getServer().getPluginManager().registerEvents(this, plugin);
  }

  @EventHandler
  public void onDamage(EntityDamageByEntityEvent event) {
    final Entity damager = event.getDamager(), entity = event.getEntity();

    if (!(entity instanceof LivingEntity)) return;

    if (entity.getPassengers().isEmpty()) return;

    if (!(damager instanceof LivingEntity)) {
      if (!(damager instanceof Projectile)) return;

      final Projectile projectile = (Projectile) damager;
      entity
          .getPassengers()
          .forEach(
              passenger -> {
                if (passenger instanceof Mob
                    && passenger.getCustomName() != null
                    && passenger.getCustomName().contains("number")) {
                  ((Mob) passenger).setTarget((LivingEntity) projectile.getShooter());
                }
              });

    } else {
      entity
          .getPassengers()
          .forEach(
              passenger -> {
                  if (passenger instanceof Mob
                          && passenger.getCustomName() != null
                          && passenger.getCustomName().contains("number")) {
                  ((Mob) passenger).setTarget((LivingEntity) damager);
                }
              });
    }
  }
}
