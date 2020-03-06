package me.espada.villagers.listeners;

import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffectType;

public class EntityDeath implements Listener {

  public EntityDeath(JavaPlugin plugin) {
    plugin.getServer().getPluginManager().registerEvents(this, plugin);
  }

  @EventHandler
  public void onDeath(EntityDeathEvent entityDeathEvent) {
    LivingEntity entity = entityDeathEvent.getEntity();
    if (!entity.getPassengers().isEmpty()) {
      entity
          .getPassengers()
          .forEach(
              passenger -> {
                if (passenger instanceof LivingEntity) {
                  if (entity.hasPotionEffect(PotionEffectType.INVISIBILITY)) {
                    entity.setInvulnerable(false);
                    entity.damage(9999.9);
                  }
                }
              });
    }
  }
}
