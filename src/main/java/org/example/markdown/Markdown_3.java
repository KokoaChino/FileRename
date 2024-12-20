package org.example.markdown;

import org.apache.commons.io.FileUtils;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Markdown_3 { // 提取图片链接并下载，以及修改图片链接

    // ![image-20230306171031953](https://oss.itbaima.cn/internal/markdown/2023/03/06/p54mXYhWdGbiVfn.png)
    private static final String regex = "!\\[.*?\\]\\((https?://.*?)\\)";// ["https://oss.itbaima.cn/internal/markdown/2023/03/06/p54mXYhWdGbiVfn.png"]
    private static final Pattern pattern = Pattern.compile(regex);
    private static final String prefixA = "笔记图片/13%20JavaJUC%20多线程-";
    private static final String prefixB = "13 JavaJUC 多线程-";
    private static final List<String> imageUrls = new ArrayList<>();

    public static void main(String[] args) {
        Path root = Paths.get("src\\main\\resources\\笔记");
        dfs(root);
        downloadImages();
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
        list.forEach(Markdown_3::dfs); // 递归操作
    }

    private static void edit(Path path) {
        try {
            String content = new String(Files.readAllBytes(path)); // 读取文件为字符串
            Matcher matcher = pattern.matcher(content); // 正则匹配
            while (matcher.find()) {
                String g = matcher.group(1);
                imageUrls.add(g);
            }
            AtomicInteger cnt = new AtomicInteger(1);
            String updatedContent = matcher.replaceAll(_ -> { // 替换全部匹配
                String newName = prefixA + cnt.getAndIncrement() + ".jpg";
                return "![](" + newName + ")";
            });
            Files.write(path, updatedContent.getBytes()); // 写入更新后的字符串
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void downloadImages() { // 下载并保存图片
        int cnt = 1;
        for (String imageUrl : imageUrls) {
            try {
                Connection.Response response = Jsoup.connect(imageUrl) // 连接到图片的 URL，设置用户代理，并忽略内容类型
                        .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/129.0.0.0 Safari/537.36")
                        .ignoreContentType(true).execute(); // 执行 HTTP 请求
                String fileName = prefixB + cnt++ + ".jpg"; // 文件名
                FileUtils.copyInputStreamToFile(new ByteArrayInputStream(response.bodyAsBytes()),
                        new File("src\\main\\resources\\笔记图片\\" + fileName)); // 生成图片文件
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}