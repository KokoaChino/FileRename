package org.example;

import java.io.File;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class FileReorder { // 临时重新排序文件命名
    public static void run(File root) {
        File[] child = root.listFiles(); // 处理的文件夹
        for (File ff: child) {
            String pat = "混合"; // 匹配串
            int cnt = 0;
            for (File f: ff.listFiles()) {
                String type = f.getName().substring(f.getName().lastIndexOf('.'));
                String name = f.getName(), newName = null;
                if (name.startsWith(pat)) {
                    newName = "AAA" + name;
                } else {
                    Pattern pattern = Pattern.compile("(\\d{8}_\\d{6})"); // 正则匹配时间戳
                    Matcher matcher = pattern.matcher(name);
                    if (matcher.find()) {
                        newName = "BBB" + matcher.group(1);
                    } else {
                        System.out.println(name + "：没有匹配到结果！！！");
                    }
                }
                f.renameTo(new File(f.getParent() + "\\" + newName + cnt++ + type));
            }
        }
    }
}