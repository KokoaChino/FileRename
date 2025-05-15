package org.example.file;

import java.io.File;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;


public class FileTreePrinter { // 打印文件结构

    public static final String PATH = """
            D:\\下载\\modified
            """.trim();

    public static void main(String[] args) {
        File rootDir = new File(PATH);
        System.out.println(rootDir.getName());
        printSubItems(rootDir, "");
    }

    private static void printSubItems(File dir, String prefix) {
        File[] items = dir.listFiles();
        if (items == null) return;
        List<File> sortedItems = Arrays.stream(items)
                .sorted(Comparator.comparing((File f) -> !f.isDirectory())
                .thenComparing(File::getName))
                .toList();
        for (int i = 0; i < sortedItems.size(); i++) {
            File item = sortedItems.get(i);
            boolean currentIsLast = (i == sortedItems.size() - 1);
            if (item.isDirectory()) {
                System.out.println(prefix + (currentIsLast ? "└── " : "├── ") + item.getName());
                String newPrefix = prefix + (currentIsLast ? "    " : "│   ");
                printSubItems(item, newPrefix);
            } else {
                System.out.println(prefix + (currentIsLast ? "└── " : "├── ") + item.getName());
            }
        }
    }
}