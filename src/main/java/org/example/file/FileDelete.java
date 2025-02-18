package org.example.file;

import java.io.File;
import java.util.Objects;


public class FileDelete {
    public static void run(File folder) { // 将文件夹内的匹配的所有文件删除
        for (File f: Objects.requireNonNull(folder.listFiles(File::isDirectory))) run(f);
        File[] files = folder.listFiles(f -> {
            if (f.isFile() && f.getName().lastIndexOf('.') >= 0) {
                return !f.getName().substring(f.getName().lastIndexOf('.')).equals(".ini");
            }
            return false;
        });

        File[] emptyCheck = folder.listFiles(File::isDirectory);
        if (emptyCheck == null || emptyCheck.length == 0) {
            if (!folder.delete()) {
                System.out.println("删除空文件夹失败: " + folder.getAbsolutePath());
            }
        }
    }
}
