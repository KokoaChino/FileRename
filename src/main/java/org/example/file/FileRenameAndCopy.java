package org.example.file;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;


public class FileRenameAndCopy { // 拷贝文件并重命名

    public static final String sourceFilePath = """
            D:\\下载\\modified
            """.trim();
    public static final String targetDirPath = """
            D:\\下载\\modified
            """.trim();
    public static final String newFileName = """
            D:\\下载\\modified
            """.trim();

    public static void main(String[] args) {
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
            throw new RuntimeException(e);
        }
    }
}