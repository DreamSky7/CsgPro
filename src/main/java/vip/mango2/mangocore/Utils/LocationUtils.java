package vip.mango2.mangocore.Utils;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;

import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class LocationUtils {

    /**
     * 随机获取一个坐标
     * @param locationList 坐标列表
     * @return 坐标
     */
    public Location randomLocation(List<Location> locationList) {
        // 流处理随机列表中得坐标
        return locationList.stream()
                .skip(ThreadLocalRandom.current().nextInt(locationList.size()))
                .findFirst()
                .orElse(null);
    }

    /**
     * 获取两个坐标之间的距离
     * @param loc1 坐标1
     * @param loc2 坐标2
     * @return
     */
    public static double distanceBetween(Location loc1, Location loc2) {
        if (loc1.getWorld().equals(loc2.getWorld())) {
            return loc1.distance(loc2);
        } else {
            throw new IllegalArgumentException("两个坐标不在同一个世界。");
        }
    }

    /**
     * 查找最近的位置 列表中找到距离给定位置最近的位置。
     * @param reference 坐标
     * @param locations 坐标列表
     * @return
     */
    public static Location findNearestLocation(Location reference, List<Location> locations) {
        return locations.stream()
                .filter(loc -> loc.getWorld().equals(reference.getWorld()))
                .min(Comparator.comparingDouble(reference::distance))
                .orElse(null);
    }

    /**
     * 检查两个位置是否相等
     * @param loc1 位置1
     * @param loc2 位置2
     * @return 是否相等
     */
    public static boolean equalLocations(Location loc1, Location loc2) {
        if (loc1 == null || loc2 == null) {
            return false;
        }
        return loc1.getWorld().equals(loc2.getWorld()) &&
                loc1.getBlockX() == loc2.getBlockX() &&
                loc1.getBlockY() == loc2.getBlockY() &&
                loc1.getBlockZ() == loc2.getBlockZ();
    }

    /**
     * 将位置对象转换为字符串
     * @param loc 位置对象
     * @return 字符串
     */
    public static String locToString(Location loc){
        return String.format("%d %d %d %s %f %f",
                loc.getBlockX(),loc.getBlockY(),loc.getBlockZ(),loc.getWorld().getName()
                ,loc.getYaw(),loc.getPitch());
    }

    /**
     * 检查位置是否安全
     * @param location 位置
     * @return 是否安全
     */
    public static boolean isSafeLocation(Location location) {
        Block feet = location.getBlock();
        Block head = feet.getRelative(BlockFace.UP);
        Block ground = feet.getRelative(BlockFace.DOWN);
        return !feet.isLiquid() && !head.isLiquid() && ground.getType().isSolid();
    }

    /**
     * 将字符串转换为位置对象
     * @param loc 字符串
     * @return 位置对象
     */
    public static Location strToLocation(String loc) {
        if (StringUtils.isBlank(loc) || "none".equals(loc)) {
            return null;
        }
        // 通过空格分割字符串来提取各个组件。
        String[] parts = loc.split(" ");

        // 检查是否有足够的部分来创建一个Location。
        if (parts.length != 4 && parts.length != 6) {
            MessageUtils.consoleMessage(loc + "似乎不是有效的坐标（参数个数错误）。");
            return null;
        }

        try {
            // 从字符串解析x, y, z坐标。
            double x = Double.parseDouble(parts[0]);
            double y = Double.parseDouble(parts[1]);
            double z = Double.parseDouble(parts[2]);

            // 获取是否填写世界，不存在则取默认世界
            World world = Bukkit.getWorld(parts[3]) != null ? Bukkit.getWorld(parts[3]) : Bukkit.getWorlds().get(0);

            float yaw = parts.length == 6 ? Float.parseFloat(parts[4]) : 0.0f;
            float pitch = parts.length == 6 ? Float.parseFloat(parts[5]) : 0.0f;

            return new Location(world, x, y, z, yaw, pitch);
        } catch (NumberFormatException e) {
            MessageUtils.consoleMessage(loc + "似乎不是有效的坐标（数据类型错误）。");
            return null;
        }
    }
}
