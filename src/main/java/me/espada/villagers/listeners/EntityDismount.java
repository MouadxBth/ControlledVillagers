package me.espada.villagers.listeners;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Mob;
import org.bukkit.entity.Vehicle;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.vehicle.VehicleExitEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffectType;

public class EntityDismount implements Listener {

  public EntityDismount(JavaPlugin plugin) {
    plugin.getServer().getPluginManager().registerEvents(this, plugin);
  }

  @EventHandler
  public void onDismount(VehicleExitEvent event) {
    Vehicle vehicle = event.getVehicle();
    if (!vehicle.getType().equals(EntityType.VILLAGER)) return;

    if (vehicle.getPassengers().isEmpty()) return;

    boolean result =
        vehicle.getPassengers().stream()
            .filter(passenger -> passenger instanceof LivingEntity)
            .map(passenger -> (LivingEntity) passenger)
            .anyMatch(
                livingPassenger ->
                    livingPassenger instanceof Mob
                        && livingPassenger.getCustomName() != null
                        && livingPassenger.hasPotionEffect(PotionEffectType.INVISIBILITY));

    if (result) event.setCancelled(true);

  }
}
