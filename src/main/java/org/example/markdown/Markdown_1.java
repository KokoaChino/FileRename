package org.example.markdown;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Markdown_1 { // 文章添加摘要

    private static final String regex = "---";
    private static final Pattern pattern = Pattern.compile(regex);

    public static void main(String[] args) {
        Path root = Paths.get("src\\main\\resources\\笔记");
        dfs(root);
    }

    private static void dfs(Path path) {
        if (!Files.isDirectory(path)) {
            if (path.getFileName().toString().endsWith(".md")) {
                System.out.println(path.getFileName());
                edit(path);
            }
            return;
        }
        List<Path> list;
        try {
            list = Files.list(path).toList();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        list.forEach(Markdown_1::dfs);
    }

    private static void edit(Path path) {
        try {
            String content = new String(Files.readAllBytes(path));
            Matcher matcher = pattern.matcher(content);
            for (int cnt = 0; cnt < 2; cnt++) {
                matcher.find();
            }
            String tags = content.substring(content.indexOf('[') + 1, content.indexOf(']'));
            String insert = "\n\n" + tags + "\n\n<!-- more -->\n\n";
            String updatedContent = new StringBuilder(content).insert(matcher.end() + 1, insert).toString();
            Files.write(path, updatedContent.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}