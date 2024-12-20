package org.example.markdown;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Markdown_2 { // 文章添加摘要

    private static final String regex = "---";
    private static final Pattern pattern = Pattern.compile(regex);

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
        list.forEach(Markdown_2::dfs); // 递归操作
    }

    private static void edit(Path path) {
        try {
            String content = new String(Files.readAllBytes(path)); // 读取文件为字符串
            Matcher matcher = pattern.matcher(content); // 正则匹配
            for (int cnt = 0; cnt < 2; cnt++) {
                matcher.find();
            }
            String tags = content.substring(content.indexOf('[') + 1, content.indexOf(']'));
            String insert = "\n\n" + tags + "\n\n<!-- more -->\n\n";
            String updatedContent = new StringBuilder(content).insert(matcher.end() + 1, insert).toString();
            Files.write(path, updatedContent.getBytes()); // 写入更新后的字符串
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
