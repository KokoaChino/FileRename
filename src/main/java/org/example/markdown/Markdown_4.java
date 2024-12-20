package org.example.markdown;

import org.example.file.FileUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Markdown_4 { // 提取所有图片至图床，并重命名

    // ![image-20241220120517337](C:\Users\Kokoa_Chino\AppData\Roaming\Typora\typora-user-images\image-20241220120517337.png)
    private static final String regex = "!\\[.*?]\\((.*?)\\)"; // ["C:\Users\Kokoa_Chino\AppData\Roaming\Typora\typora-user-images\image-20241220120517337.png"]
    private static final Pattern pattern = Pattern.compile(regex);
    private static final String prefix = "https://gitee.com/kokoachino/picture-bed/raw/master/其他";
    private static final String target = "C:\\Users\\Kokoa_Chino\\Desktop\\Learning materials\\图床\\其他";
    private static final String name = "项目 AAA";

    public static void main(String[] args) {
        Path root = Paths.get("src\\main\\resources\\笔记");
        dfs(root);
    }

    private static void dfs(Path path) {
        if (!Files.isDirectory(path)) { // 不是文件夹
            if (path.getFileName().toString().endsWith(".md")) { // 是 .md 文件
                System.out.println(path.getFileName());
                edit(path); // 执行操作
            }
            return;
        }
        List<Path> list;
        try {
            list = Files.list(path).toList();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        list.forEach(Markdown_4::dfs); // 递归操作
    }

    private static void edit(Path path) {
        try {
            String content = new String(Files.readAllBytes(path)); // 读取文件为字符串
            Matcher matcher = pattern.matcher(content); // 正则匹配
            AtomicInteger cnt = new AtomicInteger(1);
            String updatedContent = matcher.replaceAll(result -> { // 替换全部匹配
                String source = result.group(1);
                String fileName = name + "-" + cnt.getAndIncrement() + ".png";
                FileUtils.copyAndRenameFile(source, target, fileName);
                return "![" + fileName + "](" + prefix + "/" + fileName.replace(" ", "%20") + ")";
            });
            Files.write(path, updatedContent.getBytes()); // 写入更新后的字符串
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}