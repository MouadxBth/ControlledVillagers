package me.espada.villagers.listeners;

import me.espada.villagers.base.CustomMobSpawner;
import me.espada.villagers.tasks.StructureFinder;
import org.bukkit.entity.IronGolem;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.InvocationTargetException;
import java.util.*;

import static me.espada.villagers.base.Utils.*;

public class CustomVillagerSpawner implements Listener {

  private final JavaPlugin plugin;
  private StructureFinder.Builder finder;
  private Map<UUID, StructureFinder> map;

  public CustomVillagerSpawner(JavaPlugin plugin) {
    this.plugin = plugin;
    this.finder =
        new StructureFinder.Builder(plugin)
            .setCollection(new HashSet<>())
            .setDelay(plugin.getConfig().getInt("delay"))
            .setPeriod(plugin.getConfig().getInt("period"))
            .setStructure("Village");
    this.map = new HashMap<>();
    plugin.getServer().getPluginManager().registerEvents(this, plugin);
  }

  @EventHandler
  public void onPlayerJoin(PlayerJoinEvent event) {
    final Player player = event.getPlayer();
    final UUID uuid = player.getUniqueId();

    final StructureFinder structureFinder = finder.setFrom(player.getLocation()).build();
    structureFinder.track(
        () ->
            player
                .getNearbyEntities(20, 20, 20)
                .forEach(
                    entity -> {
                      if (!(entity instanceof Villager)) return;

                      try {
                        if (inArea(
                            getLocationFromString(getStructure(player.getLocation(), "Village")),
                            entity.getLocation(),
                            50)) {
                          Random random = new Random();

                          if (random.nextInt(100) >= 50) {
                            CustomMobSpawner.spawnAt(
                                entity.getLocation(), IronGolem.class, Villager.class);
                          }
                        }
                      } catch (NoSuchMethodException
                          | InstantiationException
                          | ClassNotFoundException
                          | InvocationTargetException
                          | IllegalAccessException e) {

                        plugin
                            .getLogger()
                            .warning(
                                "Could not locate a structure with the name of Village, cancelling the search... {printstack: "
                                    + e.getMessage());
                        structureFinder.setCancelled(true);
                      }
                    }));

    map.put(uuid, structureFinder);
  }

  @EventHandler
  public void onPlayerQuit(PlayerQuitEvent event) {
    final UUID uuid = event.getPlayer().getUniqueId();

    if (map.get(uuid) != null) {
      map.get(uuid).setCancelled(true);
      map.remove(uuid);
    }
  }

  public void clear() {
    if(map != null) map.values().forEach(runnable -> runnable.setCancelled(true));
  }
}
