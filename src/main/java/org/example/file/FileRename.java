package org.example.file;

import java.io.File;
import java.util.Objects;


public class FileRename { // 将文件夹内的所有文件重命名
    public static void run(File folder) {
        for (File f: Objects.requireNonNull(folder.listFiles(File::isDirectory))) run(f);
        File[] files = folder.listFiles(f -> {
            if (f.isFile() && f.getName().lastIndexOf('.') >= 0) {
                return !f.getName().substring(f.getName().lastIndexOf('.')).equals(".ini");
            }
            return false;
        });
        if (files == null) return;
        int n = files.length;
        int m = String.valueOf(n).length();
        for (int i = 0; i < n; i++) {
            File f = files[i];
            String newName = folder.getName() + " " + String.format("%0" + m + "d", i + 1);
            String type = f.getName().substring(f.getName().lastIndexOf('.'));
            f.renameTo(new File(f.getParent() + "\\" + newName + type));
        }
    }
}