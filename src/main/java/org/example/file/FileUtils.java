package org.example.file;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;


public class FileUtils { // 文件工具类

    public static void copyAndRenameFile(String sourceFilePath,
                                         String targetDirPath,
                                         String newFileName) { // 拷贝文件，并重命名
        Path sourcePath = Paths.get(sourceFilePath);
        Path targetDir = Paths.get(targetDirPath);
        Path targetPath = targetDir.resolve(newFileName);
        System.out.println(sourcePath + " -> " + targetPath);
        try {
            if (Files.notExists(targetDir)) {
                Files.createDirectories(targetDir);
            }
            Files.copy(sourcePath, targetPath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}