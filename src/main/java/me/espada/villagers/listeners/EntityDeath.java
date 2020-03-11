package me.espada.villagers.listeners;

import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Mob;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffectType;

import java.util.Objects;

import static me.espada.villagers.base.Utilities.format;

public class EntityDeath implements Listener {

  private JavaPlugin plugin;

  public EntityDeath(JavaPlugin plugin) {
    this.plugin = plugin;
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
            && ((Mob) passenger).hasPotionEffect(PotionEffectType.INVISIBILITY)) {
          passenger.setInvulnerable(false);
          ((LivingEntity) passenger).damage(9999999.9);
        }
      }
    }
  }

  @EventHandler
  public void onPlayerDeath(PlayerDeathEvent event) {
    LivingEntity entity = event.getEntity().getKiller();
    final Entity passenger = Objects.requireNonNull(entity).getPassenger();

    if (passenger != null) {
      if (passenger instanceof LivingEntity) {
        if (passenger instanceof Mob
            && passenger.getCustomName() != null
            && ((Mob) passenger).hasPotionEffect(PotionEffectType.INVISIBILITY)) {
          event.setDeathMessage(format(plugin.getConfig().getString("message")));
        }
      }
    }
  }
}
