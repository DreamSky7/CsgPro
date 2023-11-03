package vip.mango2.mangocore.Utils;

import org.bukkit.Bukkit;
import vip.mango2.mangocore.Enum.OperSystem;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class ServerUtils {

    /**
     * 获取操作系统类型
     * @return 操作系统类型 {@link OperSystem}
     */
    public static OperSystem analyseOs() {
        String os = System.getProperty("os.name");
        if (os.toLowerCase().contains("windows")) {
            return OperSystem.WINDOWS;
        } else if (os.toLowerCase().contains("mac")) {
            return OperSystem.MAC;
        } else if (os.toLowerCase().contains("unix")) {
            return OperSystem.UNIX;
        } else if (os.toLowerCase().contains("linux")) {
            return OperSystem.LINUX;
        } else {
            return OperSystem.UNKNOWN;
        }
    }

    /**
     * 获取核心文件列表
     * @return 核心文件列表
     */
    public static List<File> getCoreFileList() {
        File root = new File("./");
        List<File> bukkitCoreList = new ArrayList<>();
        Arrays.stream(System.getProperty("java.class.path").split(";")).filter(e -> e.endsWith(".jar")).forEach(e -> {
            File file = new File(e);
            try (JarFile jar = new JarFile(file)){
                JarEntry entry = jar.getJarEntry("version.json");

                if(jar.getJarEntry("version.json") != null
                        || jar.getJarEntry("mohist_libraries.txt") != null
                        || entry != null){
                    bukkitCoreList.add(file);
                }
            }catch (Exception err){
                throw new RuntimeException(err);
            }
        });
        return bukkitCoreList;
    }
}
