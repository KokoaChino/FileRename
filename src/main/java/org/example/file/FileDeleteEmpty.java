package org.example.file;

import java.io.File;
import java.util.Objects;


public class FileDeleteEmpty { // 删除所有空文件夹

    public static final String PATH = """
            D:\\下载\\modified
            """.trim();

    public static void main(String[] args) {
        run(new File(PATH));
    }

    public static void run(File folder) {
        for (File f: Objects.requireNonNull(folder.listFiles(File::isDirectory))) run(f);
        File[] emptyCheck = folder.listFiles(File::isDirectory);
        if (emptyCheck == null || emptyCheck.length == 0) {
            if (!folder.delete()) {
                System.out.println("删除空文件夹失败: " + folder.getAbsolutePath());
            }
        }
    }
}