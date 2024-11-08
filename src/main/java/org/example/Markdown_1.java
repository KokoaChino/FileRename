package org.example;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Markdown_1 { // 图片链接添加前缀

    // ![](笔记图片/13%20JavaJUC%20多线程-1.jpg)
    private static final String regex = "!\\[]\\((.*?)\\)"; // ["笔记图片/13%20JavaJUC%20多线程-1.jpg"]
    private static final Pattern pattern = Pattern.compile(regex);
    private static final String prefix = "https://gitee.com/kokoachino/picture-bed/raw/master/";

    public static void main(String[] args) {
        Path root = Paths.get("src\\main\\resources\\笔记");
        dfs(root);
    }

    private static void dfs(Path path) {
        if (!Files.isDirectory(path)) { // 不是文件夹
            if (path.getFileName().toString().endsWith(".md")) { // 是 .md 文件
                System.out.println(path.getFileName());
                edit(path); // 执行修改
            }
            return;
        }
        List<Path> list;
        try {
            list = Files.list(path).toList();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        list.forEach(Markdown_1::dfs); // 递归操作
    }

    private static void edit(Path path) {
        try {
            String content = new String(Files.readAllBytes(path)); // 读取文件为字符串
            Matcher matcher = pattern.matcher(content); // 正则匹配
            String updatedContent = matcher.replaceAll(result -> { // 替换全部匹配
                String g1 = result.group(1);
                String sub = g1.substring(g1.indexOf("笔记图片"));
                return "![](" + prefix + sub + ")";
            });
            Files.write(path, updatedContent.getBytes()); // 写入更新后的字符串
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}