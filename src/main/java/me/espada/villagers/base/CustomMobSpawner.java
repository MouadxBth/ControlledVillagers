package me.espada.villagers.base;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Objects;

public interface CustomMobSpawner {

  static <Passenger extends Entity, Type extends Entity> void spawnAt(
      Location location, Class<Passenger> passenger, Class<Type> entity) {
    Entity type = Objects.requireNonNull(location.getWorld()).spawn(location, entity);
    Entity riding = Objects.requireNonNull(location.getWorld()).spawn(location, passenger);

    PotionEffect invisibility = PotionEffectType.INVISIBILITY.createEffect(Integer.MAX_VALUE, 2);

    if (riding instanceof LivingEntity) {
      LivingEntity livingEntity = (LivingEntity) riding;
      invisibility.apply(livingEntity);
      livingEntity.setCollidable(false);
      livingEntity.setCollidable(false);
    }

    riding.setSilent(true);
    riding.setInvulnerable(true);

    type.getPassengers().add(riding);
  }

  static <Passenger extends Entity, Type extends Entity> void mutate(
      Location location, Class<Passenger> passenger, Type entity) {
    Entity riding = Objects.requireNonNull(location.getWorld()).spawn(location, passenger);

    PotionEffect invisibility = PotionEffectType.INVISIBILITY.createEffect(Integer.MAX_VALUE, 2);

    if (riding instanceof LivingEntity) {
      LivingEntity livingEntity = (LivingEntity) riding;
      invisibility.apply(livingEntity);
      livingEntity.setCollidable(false);
      livingEntity.setCollidable(false);
    }

    riding.setSilent(true);
    riding.setInvulnerable(true);

    entity.getPassengers().add(riding);
  }
}
