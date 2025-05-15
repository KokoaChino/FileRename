package org.example.file;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class FileReorder { // 临时重新排序文件命名

    public static final String PATH = """
            D:\\下载\\modified
            """.trim();

    public static void main(String[] args) {
        File[] child = new File(PATH).listFiles();
        for (File ff: child) {
            String pat = "混合";
            int cnt = 0;
            for (File f: ff.listFiles()) {
                String type = f.getName().substring(f.getName().lastIndexOf('.'));
                String name = f.getName(), newName = null;
                if (name.startsWith(pat)) {
                    newName = "AAA" + name;
                } else {
                    Pattern pattern = Pattern.compile("(\\d{8}_\\d{6})");
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