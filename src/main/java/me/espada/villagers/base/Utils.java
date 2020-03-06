package me.espada.villagers.base;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.LivingEntity;
import org.bukkit.util.Vector;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Objects;

public interface Utils {

  static boolean isLookingAt(LivingEntity one, LivingEntity two) {
    Location eye = one.getEyeLocation();
    Vector toEntity = two.getEyeLocation().toVector().subtract(eye.toVector());
    double dot = toEntity.normalize().dot(eye.getDirection());

    return dot > 0.99D;
  }

  static String format(String message) {
    return ChatColor.translateAlternateColorCodes('&', message);
  }

  static Location getLocationFromString(String s) {
    if (s == null || s.trim().equals("")) {
      return null;
    }
    final String[] parts = s.split(":");
    if (parts.length == 6) {
      World w = Bukkit.getServer().getWorld(parts[0]);
      double x = Double.parseDouble(parts[1]);
      double y = Double.parseDouble(parts[2]);
      double z = Double.parseDouble(parts[3]);
      float yaw = Float.parseFloat(parts[4]);
      float pitch = Float.parseFloat(parts[5]);
      return new Location(w, x, y, z, yaw, pitch);
    }
    return null;
  }

  static boolean inArea(Location targetLocation, Location location, int range) {
    final Location area =
        new Location(
            location.getWorld(),
            location.getX() + range,
            location.getY() + range,
            location.getZ() + range);

    if (location.getWorld() == null) return false;

    if (!location.getWorld().equals(area.getWorld())) return false;

    if ((targetLocation.getBlockX() >= location.getBlockX()
            && targetLocation.getBlockX() <= area.getBlockX())
        || (targetLocation.getBlockX() <= location.getBlockX()
            && targetLocation.getBlockX() >= area.getBlockX())) {

      return (targetLocation.getBlockZ() >= location.getBlockZ()
              && targetLocation.getBlockZ() <= area.getBlockZ())
          || (targetLocation.getBlockZ() <= location.getBlockZ()
              && targetLocation.getBlockZ() >= area.getBlockZ());
    }

    return false;
  }

  static String getStructure(Location location, String structure)
      throws NoSuchMethodException, SecurityException, IllegalAccessException,
          IllegalArgumentException, InvocationTargetException, ClassNotFoundException,
          InstantiationException {
    Method getHandle = Objects.requireNonNull(location.getWorld()).getClass().getMethod("getHandle");
    Object nmsWorld = getHandle.invoke(location.getWorld());
    Object blockPositionString =
        nmsWorld
            .getClass()
            .getMethod(
                "a",
                new Class[] {String.class, getNMSClass("BlockPosition"), int.class, boolean.class})
            .invoke(nmsWorld, structure, getBlockPosition(location), 100, false);
    return blockPositionString.toString();
  }

  static Class<?> getNMSClass(String nmsClassString) throws ClassNotFoundException {
    String version =
        Bukkit.getServer().getClass().getPackage().getName().replace(".", ",").split(",")[3] + ".";
    String name = "net.minecraft.server." + version + nmsClassString;
    return Class.forName(name);
  }

  static Object getBlockPosition(Location location)
      throws ClassNotFoundException, InstantiationException, IllegalAccessException,
          IllegalArgumentException, InvocationTargetException, NoSuchMethodException,
          SecurityException {
    Class<?> nmsBlockPosition = getNMSClass("BlockPosition");
    return nmsBlockPosition
        .getConstructor(new Class[] {Double.TYPE, Double.TYPE, Double.TYPE})
        .newInstance(location.getX(), location.getY(), location.getZ());
  }
}
