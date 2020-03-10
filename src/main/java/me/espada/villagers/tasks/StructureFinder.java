package me.espada.villagers.tasks;

import org.bukkit.Location;
import org.bukkit.event.Cancellable;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.lang.reflect.InvocationTargetException;
import java.util.Collection;

import static me.espada.villagers.base.Utils.*;

public interface StructureFinder extends Cancellable {

  Collection<Location> getStructures();

  void track(Runnable runnable);

  class Builder {

    private final JavaPlugin plugin;
    private Location from;
    private String structure;
    private int delay, period;
    private Collection<Location> structures;

    public Builder(JavaPlugin plugin) {
      this.plugin = plugin;
    }

    public Builder setFrom(Location from) {
      this.from = from;
      return this;
    }

    public Builder setStructure(String structure) {
      this.structure = structure;
      return this;
    }

    public Builder setDelay(int delay) {
      this.delay = delay;
      return this;
    }

    public Builder setPeriod(int period) {
      this.period = period;
      return this;
    }

    public Builder setCollection(Collection<Location> structures) {
      this.structures = structures;
      return this;
    }

    public StructureFinder build() {
      return new StructureFinder() {

        private boolean cancelled = false;

        @Override
        public boolean isCancelled() {
          return cancelled;
        }

        @Override
        public void setCancelled(boolean cancel) {
          cancelled = cancel;
        }

        @Override
        public Collection<Location> getStructures() {
          return structures;
        }

        @Override
        public void track(Runnable runnable) {
          new BukkitRunnable() {

            @Override
            public void run() {
              if (isCancelled()) return;
              if (!plugin.isEnabled()) {
                getStructures().clear();
                cancel();
                return;
              }
              try {
                getStructures().add(getStructureLocationFrom(from.getWorld(), getStructure(from, structure)));
              } catch (NoSuchMethodException
                  | InstantiationException
                  | ClassNotFoundException
                  | InvocationTargetException
                  | IllegalAccessException e) {

                plugin
                    .getLogger()
                    .warning(
                        "Could not locate a structure with the name of "
                            + structure
                            + " printstack: "
                            + e.getMessage());
              }
            }
          }.runTaskTimer(plugin, delay * 20, period * 20);

          runnable.run();
        }
      };
    }
  }
}
