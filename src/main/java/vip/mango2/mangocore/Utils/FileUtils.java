package vip.mango2.mangocore.Utils;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.stream.Stream;

public class FileUtils {
    /**
     * 复制文件夹
     * @param source 源文件夹
     * @param target 目标文件夹
     * @throws IOException IO异常
     */
    public static void copyDirectory(File source, File target) throws IOException {
        if (!target.exists()) {
            target.mkdir();
        }
        for (File file : source.listFiles()) {
            File targetFile = new File(target, file.getName());
            if (file.isDirectory()) {
                copyDirectory(file, targetFile);
            } else {
                Files.copy(file.toPath(), targetFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
            }
        }
    }

    /**
     * 复制世界文件夹
     * @param source 源文件夹
     * @param target 目标文件夹
     * @throws IOException IO异常
     */
    public static void copyWorld(Path source, Path target) throws IOException {
        // 创建目标世界的文件夹
        if(!Files.exists(target)){
            Files.createDirectories(target);
        }

        // 复制 region 文件夹
        Path sourceRegion = source.resolve("region");
        Path targetRegion = target.resolve("region");
        if(Files.exists(sourceRegion) && Files.isDirectory(sourceRegion)) {
            if (!Files.exists(targetRegion)) {
                Files.createDirectories(targetRegion);
            }
            try (Stream<Path> paths = Files.walk(sourceRegion)) {
                paths.filter(Files::isRegularFile)
                        .forEach(sourcePath -> {
                            Path targetPath = targetRegion.resolve(sourceRegion.relativize(sourcePath));
                            try (
                                    InputStream in = new BufferedInputStream(Files.newInputStream(sourcePath));
                                    OutputStream out = new BufferedOutputStream(Files.newOutputStream(targetPath))
                            ) {
                                byte[] buffer = new byte[1024];
                                int lengthRead;
                                while ((lengthRead = in.read(buffer)) > 0) {
                                    out.write(buffer, 0, lengthRead);
                                    out.flush();
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        });
            }

            // 复制 level.dat 文件
            Path sourceLevelDat = source.resolve("level.dat");
            Path targetLevelDat = target.resolve("level.dat");
            if (Files.exists(sourceLevelDat) && Files.isRegularFile(sourceLevelDat)) {
                //Files.copy(sourceLevelDat, targetLevelDat, StandardCopyOption.REPLACE_EXISTING);
                try (
                        InputStream in = new BufferedInputStream(Files.newInputStream(sourceLevelDat));
                        OutputStream out = new BufferedOutputStream(Files.newOutputStream(targetLevelDat))
                ) {
                    byte[] buffer = new byte[1024];
                    int lengthRead;
                    while ((lengthRead = in.read(buffer)) > 0) {
                        out.write(buffer, 0, lengthRead);
                        out.flush();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 复制文件夹
     * @param source 源文件夹
     * @param target 目标文件夹
     */
    public static void copyDir(File source, File target)
    {
        if (source.isDirectory()){
            if (!target.exists()) {
                target.mkdirs();
            }
            if(!source.getName().equals("playerdata") && !source.getName().equals("stats")){
                for (String el : source.list()){
                    if (!el.equals("uid.dat") && !el.equals("session.lock")) {
                        copyDir(new File(source, el), new File(target, el));
                    }
                }
            }

        }else{
            try{
                if (!target.getParentFile().exists())
                {
                    new File(target.getParentFile().getAbsolutePath()).mkdirs();
                    target.createNewFile();
                }
                else if (!target.exists())
                {
                    target.createNewFile();
                }
                InputStream in = new FileInputStream(source);
                Object out = new FileOutputStream(target);

                byte[] buf = new byte[1024];
                int len;
                while ((len = in.read(buf)) > 0) {
                    ((OutputStream)out).write(buf, 0, len);
                }
                in.close();
                ((OutputStream)out).close();
            }
            catch (Exception exception)
            {
            }
        }
    }

    /**
     * 删除文件夹
     * @param dir 文件夹
     * @return 是否删除成功
     */
    public static boolean deleteDir(File dir)
    {
        if (dir.isDirectory()) {
            for (File f : dir.listFiles()) {
                if (!deleteDir(f)) {
                    return false;
                }
            }
        }
        return dir.delete();
    }
}
