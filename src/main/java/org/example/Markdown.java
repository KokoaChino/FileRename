package org.example;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Markdown {

    // ![](笔记图片/01%20Markdown%20语法-1.jpg)
    private static final String regex = "!\\[]\\((.*?)\\)"; // ["笔记图片/01%20Markdown%20语法-1.jpg"]
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
        list.forEach(Markdown::dfs); // 递归操作
    }

    private static void edit(Path path) {
        try {
            String content = new String(Files.readAllBytes(path)); // 读取文件为字符串
            Matcher matcher = pattern.matcher(content); // 正则匹配
            String updatedContent = matcher.replaceAll(result -> { // 替换全部匹配
                String g1 = result.group(1);
                return "![](" + prefix + g1 + ")";
            });
            Files.write(path, updatedContent.getBytes()); // 写入更新后的字符串
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}