package org.csg.sproom;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.entity.Player;
import org.csg.Fwmain;
import org.csg.Utils.MessageUtils;
import org.csg.Utils.OSUtils;
import org.csg.group.Lobby;
import org.csg.sproom.Event.TemporaryWorldEvent;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class TemporaryWorldManager implements WorldManager {

    private List<World> temporaryWorlds = new ArrayList<>();

    private final Fwmain plugin;

    public TemporaryWorldManager(Fwmain plugin) {
        this.plugin = plugin;
    }

    @Override
    public void createTemporaryWorldFromSource(String sourceWorldName, Lobby lobby, List<Player> senderList) {
        World sourceWorld = Bukkit.getWorld(sourceWorldName);
        if (sourceWorld == null) {
            MessageUtils.ConsoleInfoMsg("&7目标世界不存在: &c" + sourceWorldName);
            return;
        }

        // 生成新世界的名称和文件夹路径
        String newWorldName = sourceWorldName + "_TEMP_" + System.currentTimeMillis();
        File sourceWorldFolder = sourceWorld.getWorldFolder();
        File newWorldFolder = new File(plugin.getWorldpath(), newWorldName);

        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            // 复制世界文件夹
            try {
                MessageUtils.ConsoleInfoMsg("&7正在复制世界文件夹...");
                OSUtils.copyWorld(sourceWorldFolder.toPath(), newWorldFolder.toPath());
            } catch (IOException e) {
                e.printStackTrace();
            }
            // 在主线程中加载世界
            Bukkit.getScheduler().runTask(plugin, () -> {
                WorldCreator creator = new WorldCreator(newWorldName);
                creator.environment(sourceWorld.getEnvironment());
                World newWorld = Bukkit.createWorld(creator);
                temporaryWorlds.add(newWorld);
                // 世界加载完成后触发事件
                Bukkit.getPluginManager().callEvent(new TemporaryWorldEvent(newWorld, lobby, senderList));
            });
        });
    }

    @Override
    public void createTemporaryWorldFromZip(File zipFile) {

    }

    @Override
    public void createTemporaryWorldFromWorldEdit(File schematicFile) {
    }

    public boolean unloadTemporaryWorld(String worldName) {
        World world = Bukkit.getWorld(worldName);
        if (world == null) {
            return false;
        }

        World defaultWorld = Bukkit.getWorlds().get(0);
        for (Player player : world.getPlayers()) {
            player.teleport(defaultWorld.getSpawnLocation());
        }

        // 卸载世界
        boolean isUnloaded = Bukkit.unloadWorld(world, true);
        if (!isUnloaded) {
            return false; // 无法卸载世界
        }

        // 异步删除世界文件夹
        File worldFolder = world.getWorldFolder();

        if (Fwmain.getInstance().isEnabled()) {
            Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
                try {
                    MessageUtils.ConsoleInfoMsg("&7正在尝试删除世界文件夹...");
                    deleteDirectory(worldFolder.toPath());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        } else {
            try {
                MessageUtils.ConsoleInfoMsg("&7正在尝试删除世界文件夹...");
                deleteDirectory(worldFolder.toPath());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return true;
    }

    private static void deleteDirectory(Path path) throws IOException {
        if (Files.exists(path)) {
            Files.walk(path)
                    .sorted((a, b) -> b.compareTo(a)) // 从叶子节点开始删除
                    .forEach(p -> {
                        try {
                            Files.delete(p);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });
        }
    }
}
